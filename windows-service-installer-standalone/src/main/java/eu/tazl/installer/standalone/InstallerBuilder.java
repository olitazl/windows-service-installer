package eu.tazl.installer.standalone;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.gson.Gson;
import com.izforge.izpack.compiler.CompilerConfig;
import org.apache.commons.cli.*;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.artifact.Artifact;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.zeroturnaround.zip.ZipUtil;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;

import static org.apache.commons.io.FileUtils.*;
import static org.apache.commons.io.FilenameUtils.getBaseName;
import static org.apache.commons.io.IOUtils.closeQuietly;


/**
 * Creates izPack installer
 *
 * @author olitazl
 * Date: 2017/04/20
 */

public class InstallerBuilder  {

    private final MustacheFactory mf = new DefaultMustacheFactory();
    private final InstallerConfig config;
    private PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    protected final ResourceLoader loader = resourcePatternResolver.getResourceLoader();


    public InstallerBuilder(InstallerConfig config){
        this.config = config;
    }

    public void execute() {
        try {
            Dirs dirs = prepareDirs();
            copyBin(dirs.bin);
            copyIzpack();
            copyUninstall(dirs.uninstall);
            copyPrunsrv(dirs.bin);
            copyAppData();
            markBinExecutable(dirs.bin);
            copyLibs(dirs.lib, config.getLibs());
            copyLauncher(dirs.bin, config.getArtifact());
            File jre = copyJRE();
            copyIzPackResources();
            runIzPackCompiler();
            packInstaller(jre);
            if(config.isBuildUnixDist()) packDist();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static File mkdirs(File dir) {
        if(dir.exists()) {
            if(dir.isFile()) throw new RuntimeException("Cannot write to directory: "+ dir.getAbsolutePath());
        } else {
            boolean res = dir.mkdirs();
            if(!res) throw new RuntimeException("Cannot create directory: "+ dir.getAbsolutePath());
        }
        return dir;
    }

    private Dirs prepareDirs() throws IOException {
        mkdirs(config.getIzpackDir());
        mkdirs(config.getDistDir());
        File bin = mkdirs(new File(config.getDistDir(), "bin"));
        File lib = mkdirs(new File(config.getDistDir(), "lib"));
        File uninstall = mkdirs(new File(config.getDistDir(), "uninstall"));
        mkdirs(new File(config.getDistDir(), "temp"));
        mkdirs(new File(config.getDistDir(), config.getPrunsrvLogPath()));
        return new Dirs(bin, lib, uninstall);
    }

    private void copyBin(File binDir) throws IOException, URISyntaxException {
        {
            Resource[] resources = resourcePatternResolver.getResources("classpath:/" +
                    "bin/*");
            List<Resource> list = Arrays.asList(resources);
            MustacheCopyFunction copy = new MustacheCopyFunction(binDir);
            list.stream().forEach(copy);
        }
        {
            File daemonDir = new File(binDir, "java-daemon");
            Resource[] daemon = resourcePatternResolver.getResources("classpath:/bin/java-daemon/*");
            CopyFunction copy = new CopyFunction(daemonDir);
            Arrays.asList(daemon).stream().forEach(copy);
        }
    }

    private void copyIzpack() throws IOException {
        Resource[] resources = resourcePatternResolver.getResources("classpath:/izpack/*");
        MustacheCopyFunction copy = new MustacheCopyFunction(config.getIzpackDir());
        Arrays.asList(resources).stream().forEach(copy);
        writeStringToFile(new File(config.getIzpackDir(), "default-install-dir.txt"), config.getIzpackDefaultInstallDir(), "UTF8");
    }

    private void copyUninstall(File uninstallDir) {
        if(config.isUse64BitJre() && !config.isUseX86LaunchersForX64Installer()) {
            copyResourceToDir(config.getInstallLauncher64Path(), config.getIzpackDir());
            copyResourceToDir(config.getUninstallLauncher64Path(), uninstallDir);
        } else {
            copyResourceToDir(config.getInstallLauncher32Path(), config.getIzpackDir());
            copyResourceToDir(config.getUninstallLauncher32Path(), uninstallDir);
        }
    }

    private void copyPrunsrv(File binDir) throws IOException {
        Resource[] resources = resourcePatternResolver.getResources("classpath:/prunsrv/*");
        List<Resource> list = Arrays.asList(resources);
        Predicate<Resource> notExe = (x) -> !x.getFilename().contains(".exe");
        MustacheCopyFunction copy = new MustacheCopyFunction(binDir, config.getPrunsrvScriptsEncoding());

        List<Resource> filteredList = list.stream().filter(notExe).collect(Collectors.toList());
        filteredList.stream().forEach(copy);

        String prunsrvPath = config.isUse64BitJre() ? "classpath:/prunsrv/prunsrv_x86_64.exe" : "classpath:/prunsrv/prunsrv_x86_32.exe";
        File prunsrvTarget = new File(binDir, "prunsrv.exe");
        copyResource(prunsrvPath, prunsrvTarget);
    }

    private void copyAppData() throws IOException {
        for(String dir : config.getAppDataDirs()) {
            File source = new File(dir);
            copyDirectoryToDirectory(source, config.getDistDir());
        }
    }

    private void markBinExecutable(File binDir) {
        List<File> files = Arrays.asList(binDir.listFiles());
        files.stream().filter(x -> x.getName().contains(".sh")).forEach(x -> x.setExecutable(true));
    }

    private void copyLibs(File libDir, List<File> libs) throws IOException {
        for (File lib : libs)
            copyFileToDirectory(lib, libDir);
    }

    @SuppressWarnings("unchecked")
    private void copyLibs(File libDir, Set<Artifact> artifacts) throws IOException {
        for(Artifact ar : artifacts) {
            copyFileToDirectory(ar.getFile(), libDir);
        }
    }

    private void copyLauncher(File binDir, File artifact) throws IOException {
        final File dest = new File(binDir, config.getPrunsrvLauncherJarFile());
        //final File launcher;
        //if(project.getArtifact().getFile() == null) {
            // unbinded build here
        //    launcher = new File(project.getBasedir(), "target/" + project.getArtifactId() + "-" + project.getVersion() + ".jar");
        //} else {
        //    launcher = project.getArtifact().getFile();
        //}
        copyFile(artifact, dest);
    }



    // copy JRE to use loose pack feature
    private File copyJRE() throws IOException {
        File innerJre = new File(config.getIzpackDir(), "jre");
        copyDirectory(config.getJreDir(), innerJre);
        return innerJre;
    }

    private void copyIzPackResources() {
        copyResourceToDir(config.getIzpackFrameIconPath(), config.getIzpackDir());
        copyResourceToDir(config.getIzpackHelloIconPath(), config.getIzpackDir());
        File lpdir = new File(config.getIzpackDir(), "bin/langpacks/installer");
        File flagdir = new File(config.getIzpackDir(), "bin/langpacks/flags");
        copyResourceToDir("classpath:/izpack/xxx.xml", lpdir);
        copyResourceToDir("classpath:/izpack/xxx.gif", flagdir);
        if (null != config.getIzpackAdditionalResourcePaths()) {
            File addres = new File(config.getIzpackDir(), "addres");
            for (String re : config.getIzpackAdditionalResourcePaths()) {
                copyResourceToDir(re, addres);
            }
        }
    }

    private void runIzPackCompiler() throws Exception {
        PrintStream console = System.out;
        try {
            // redirect izPack output to file
            PrintStream ps = new PrintStream(openOutputStream(config.getBuildOutputFile()), true, "UTF-8");
            System.setOut(ps);
            File installFile = null != config.getInstallConfigFile() ? config.getInstallConfigFile() : new File(config.getIzpackDir(), "izpack.xml");
            CompilerConfig compilerConfig = new CompilerConfig(installFile.getAbsolutePath(), config.getIzpackDir().getAbsolutePath(),
                    "standard", config.getIzpackOutputFile().getAbsolutePath(), config.getIzpackCompress(), null);
            CompilerConfig.setIzpackHome(config.getIzpackDir().getAbsolutePath());
            compilerConfig.executeCompiler();
        } finally {
            System.setOut(console);
        }
    }

   private void packInstaller(File jre) throws IOException {
        InputStream resStream = null;
        try {
            final String prefix = getBaseName(config.getInstallerOutputFile().getPath());
            ZipUtil.pack(jre, config.getInstallerOutputFile(), s -> prefix + "/jre/" + s);
            ZipUtil.addEntry(config.getInstallerOutputFile(), prefix + "/install.jar", config.getIzpackOutputFile());

            if(config.isUse64BitJre() && !config.isUseX86LaunchersForX64Installer()) {
                resStream = loader.getResource(config.getInstallLauncher64Path()).getInputStream();
            } else {
               resStream = loader.getResource(config.getInstallLauncher32Path()).getInputStream();
            }
            ZipUtil.addEntry(config.getInstallerOutputFile(), prefix + "/install.exe", IOUtils.toByteArray(resStream));
        } finally {
            closeQuietly(resStream);
        }
    }

    private void packDist() throws IOException {
        TarArchiveOutputStream tar = null;
        try {
            OutputStream out = openOutputStream(config.getDistOutputFile());
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            String prefix = getBaseName(config.getDistOutputFile().getPath());
            tar = new TarArchiveOutputStream(gzip);
            /*TarFunction tarfun = new TarFunction(distDir, prefix, tar);
            IOFileFilter uninstallFilter = new NotFileFilter(new NameFileFilter("uninstall"));
            Collection<File> files = listFiles(distDir, TrueFileFilter.TRUE, uninstallFilter, true);
            Collection<String> tarred = Collections2.transform(files, tarfun);
            new LinkedList<>(tarred);*/
        } finally {
            IOUtils.closeQuietly(tar);
        }
    }

    private class CopyFunction implements Consumer<Resource>, Function<Resource, File> {
        protected final File dir;

        private CopyFunction(File dir) {
            this.dir = dir;
        }

        public File apply(Resource input) {
            File file = new File(dir, input.getFilename());
            return copyResource(input, file);
        }

        @Override
        public void accept(Resource input) {
            apply(input);
        }
    }

    private class MustacheCopyFunction extends CopyFunction{
        private final String ftlOutputEncoding;

        private MustacheCopyFunction(File dir) {
            this(dir, "UTF8");
        }

        private MustacheCopyFunction(File dir, String ftlOutputEncoding) {
            super(dir);
            this.ftlOutputEncoding = ftlOutputEncoding;
        }

        @Override
        public File apply(Resource input) {
            String name = input.getFilename();
            final File file;
            if(name.endsWith(".mustache")) {
                OutputStreamWriter out = null;
                try {
                    file = new File(dir, name.substring(0, name.length() - 9));
                    out = new OutputStreamWriter(openOutputStream(file));
                    Mustache m = mf.compile(new InputStreamReader(input.getInputStream()), "installerconfig");
                    m.execute(out, InstallerBuilder.this.config).flush();
                } catch (IOException e) {
                    throw new RuntimeException("error while copying");
                } finally {
                    closeQuietly(out);
                }
            } else {
                file = super.apply(input);
            }
            return file;
        }
    }

    public File copyResourceToDir(String url, File dir) {
        mkdirs(dir);
        Resource re = loader.getResource(url);
        File target = new File(dir, re.getFilename());
        return copyResource(re, target);
    }

    public  File copyResource(String url, File target) {
        Resource re = loader.getResource(url);
        return copyResource(re, target);
    }

    public  File copyResource(Resource re, File target) {
        InputStream is = null;
        OutputStream os = null;
        try {
            if(!re.exists()) throw new IOException("Cannot load resource: '"+ re+ "'");
            is = re.getInputStream();
            os = FileUtils.openOutputStream(target);
            IOUtils.copyLarge(is, os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
        return target;
    }

    private class Dirs {
        private final File bin;
        private final File lib;
        private final File uninstall;

        private Dirs(File bin, File lib, File uninstall) {
            this.bin = bin;
            this.lib = lib;
            this.uninstall = uninstall;
        }
    }

    public static void main(String[] args) throws ParseException{
        Options options = new Options();
        options.addOption("c", true, "config file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        try{
        if(cmd.hasOption("c")){
            Gson gson = new Gson();
            InstallerConfig config = gson.fromJson(new FileReader(cmd.getOptionValue("c")), InstallerConfig.class);
            InstallerBuilder builder = new InstallerBuilder(config);
            builder.execute();
        }
        }catch (Exception ex){
            ex.printStackTrace();

        } finally {
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("builder -c config file", options);
        }


    }
}
