package eu.tazl.installer.standalone;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author olitazl
 */
public class InstallerConfig {

    protected final PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    protected final ResourceLoader loader = resourcePatternResolver.getResourceLoader();



    protected final String izpackAppFilesPackName;
    protected final String izpackAppFilesPackDescription;
    protected final String izpackJREPackName;
    protected final String izpackJREPackDescription;
    protected final String izpackWindowsServicePackName;
    protected final String izpackWindowsServicePackDescription;
    protected final String prunsrvStartupMode;
    protected final String prunsrvServiceName;
    protected final String prunsrvStartClass;
    protected final String prunsrvDaemonLauncherClass;
    protected final String prunsrvStartParams;
    protected final String prunsrvStopClass;
    private final String prunsrvStopParams;
    protected final String prunsrvJvmOptions = "";
    protected final int prunsrvJvmMs;
    protected final int prunsrvJvmMx;
    protected final int prunsrvJvmSs;
    protected final String prunsrvDisplayName;
    protected final String prunsrvDescription;
    protected final int prunsrvStopTimeout;
    protected final String prunsrvLogPrefix;
    protected final String prunsrvLogLevel;
    protected final String prunsrvStdOutput;
    protected final String prunsrvStdError;
    protected final boolean prunsrvStartOnInstrall;

    final File izpackDir;
     final File distDir;
     final boolean buildUnixDist;
     final String prunsrvLogPath;
     final File installerOutputFile;
     final File buildOutputFile;
     final File installConfigFile;
     final File izpackOutputFile;
     final boolean use64BitJre;
     final boolean useX86LaunchersForX64Installer;
     final String installLauncher64Path;
     final String installLauncher32Path;
     final File distOutputFile;
     final String prunsrvLauncherJarFile;
     final List<String> appDataDirs;
     final String uninstallLauncher64Path;
     final String uninstallLauncher32Path;
     final String izpackDefaultInstallDir;
     final String prunsrvScriptsEncoding;
     final String izpackFrameIconPath;
     final String izpackHelloIconPath;
     final List<String> izpackAdditionalResourcePaths;
     final String izpackCompress;
     final File jreDir;
     final File artifact;
     final List<File> libs;
     final String izpackAppName;
     final String izpackAppVersion;
     final String izpackLang;
     final String izpackAdditionalPacksPath;







    public InstallerConfig(String izpackAppFilesPackName, String izpackAppFilesPackDescription, String izpackJREPackName, String izpackJREPackDescription, String izpackWindowsServicePackName, String izpackWindowsServicePackDescription, String prunsrvStartupMode, String prunsrvServiceName, String prunsrvStartClass, String prunsrvDaemonLauncherClass, String prunsrvStartParams, String prunsrvStopClass, String prunsrvStopParams, int prunsrvJvmMs, int prunsrvJvmMx, int prunsrvJvmSs, String prunsrvDisplayName, String prunsrvDescription, int prunsrvStopTimeout, String prunsrvLogPrefix, String prunsrvLogLevel, String prunsrvStdOutput, String prunsrvStdError, boolean prunsrvStartOnInstrall, File izpackDir, File distDir, boolean buildUnixDist, String prunsrvLogPath,
                           File installerOutputFile, File buildOutputFile, File installConfigFile, File izpackOutputFile,
                           boolean use64BitJre, boolean useX86LaunchersForX64Installer, String installLauncher64Path,
                           String installLauncher32Path, File distOutputFile, String prunsrvLauncherJarFile,
                           List<String> appDataDirs, String uninstallLauncher64Path, String uninstallLauncher32Path,
                           String izpackDefaultInstallDir, String prunsrvScriptsEncoding, String izpackFrameIconPath,
                           String izpackHelloIconPath, List<String> izpackAdditionalResourcePaths, String izpackCompress,
                           File jreDir, File artifact, List<File> libs, String izpackAppName, String izpackAppVersion,
                           String izpackLang, String izpackAdditionalPacksPath) {
        this.izpackAppFilesPackName = izpackAppFilesPackName;
        this.izpackAppFilesPackDescription = izpackAppFilesPackDescription;
        this.izpackJREPackName = izpackJREPackName;
        this.izpackJREPackDescription = izpackJREPackDescription;
        this.izpackWindowsServicePackName = izpackWindowsServicePackName;
        this.izpackWindowsServicePackDescription = izpackWindowsServicePackDescription;
        this.prunsrvStartupMode = prunsrvStartupMode;
        this.prunsrvServiceName = prunsrvServiceName;
        this.prunsrvStartClass = prunsrvStartClass;
        this.prunsrvDaemonLauncherClass = prunsrvDaemonLauncherClass;
        this.prunsrvStartParams = prunsrvStartParams;
        this.prunsrvStopClass = prunsrvStopClass;
        this.prunsrvStopParams = prunsrvStopParams;
        this.prunsrvJvmMs = prunsrvJvmMs;
        this.prunsrvJvmMx = prunsrvJvmMx;
        this.prunsrvJvmSs = prunsrvJvmSs;
        this.prunsrvDisplayName = prunsrvDisplayName;
        this.prunsrvDescription = prunsrvDescription;
        this.prunsrvStopTimeout = prunsrvStopTimeout;
        this.prunsrvLogPrefix = prunsrvLogPrefix;
        this.prunsrvLogLevel = prunsrvLogLevel;
        this.prunsrvStdOutput = prunsrvStdOutput;
        this.prunsrvStdError = prunsrvStdError;
        this.prunsrvStartOnInstrall = prunsrvStartOnInstrall;
        this.izpackDir = izpackDir;
        this.distDir = distDir;
        this.buildUnixDist = buildUnixDist;
        this.prunsrvLogPath = prunsrvLogPath;
        this.installerOutputFile = installerOutputFile;
        this.buildOutputFile = buildOutputFile;
        this.installConfigFile = installConfigFile;
        this.izpackOutputFile = izpackOutputFile;
        this.use64BitJre = use64BitJre;
        this.useX86LaunchersForX64Installer = useX86LaunchersForX64Installer;
        this.installLauncher64Path = installLauncher64Path;
        this.installLauncher32Path = installLauncher32Path;
        this.distOutputFile = distOutputFile;
        this.prunsrvLauncherJarFile = prunsrvLauncherJarFile;
        this.appDataDirs = appDataDirs;
        this.uninstallLauncher64Path = uninstallLauncher64Path;
        this.uninstallLauncher32Path = uninstallLauncher32Path;
        this.izpackDefaultInstallDir = izpackDefaultInstallDir;
        this.prunsrvScriptsEncoding = prunsrvScriptsEncoding;
        this.izpackFrameIconPath = izpackFrameIconPath;
        this.izpackHelloIconPath = izpackHelloIconPath;
        this.izpackAdditionalResourcePaths = izpackAdditionalResourcePaths;
        this.izpackCompress = izpackCompress;
        this.jreDir = jreDir;
        this.artifact = artifact;
        this.libs = libs;
        this.izpackAppName = izpackAppName;
        this.izpackAppVersion = izpackAppVersion;
        this.izpackLang = izpackLang;
        this.izpackAdditionalPacksPath = izpackAdditionalPacksPath;
    }

    public File getIzpackDir() {
        return izpackDir;
    }

    public File getDistDir() {
        return distDir;
    }

    public boolean isBuildUnixDist() {
        return buildUnixDist;
    }

    public String getPrunsrvLogPath() {
        return prunsrvLogPath;
    }

    public File getInstallerOutputFile() {
        return installerOutputFile;
    }

    public File getBuildOutputFile() {
        return buildOutputFile;
    }

    public File getInstallConfigFile() {
        return installConfigFile;
    }

    public File getIzpackOutputFile() {
        return izpackOutputFile;
    }

    public boolean isUse64BitJre() {
        return use64BitJre;
    }

    public boolean isUseX86LaunchersForX64Installer() {
        return useX86LaunchersForX64Installer;
    }

    public String getInstallLauncher64Path() {
        return installLauncher64Path;
    }

    public String getInstallLauncher32Path() {
        return installLauncher32Path;
    }

    public File getDistOutputFile() {
        return distOutputFile;
    }

    public String getPrunsrvLauncherJarFile() {
        return prunsrvLauncherJarFile;
    }

    public List<String> getAppDataDirs() {
        return appDataDirs;
    }

    public String getUninstallLauncher64Path() {
        return uninstallLauncher64Path;
    }

    public String getUninstallLauncher32Path() {
        return uninstallLauncher32Path;
    }

    public String getIzpackDefaultInstallDir() {
        return izpackDefaultInstallDir;
    }

    public String getPrunsrvScriptsEncoding() {
        return prunsrvScriptsEncoding;
    }

    public String getIzpackFrameIconPath() {
        return izpackFrameIconPath;
    }

    public String getIzpackHelloIconPath() {
        return izpackHelloIconPath;
    }

    public List<String> getIzpackAdditionalResourcePaths() {
        return izpackAdditionalResourcePaths;
    }

    public String getIzpackCompress() {
        return izpackCompress;
    }

    public File getJreDir() {
        return jreDir;
    }

    public File getArtifact() {
        return artifact;
    }

    public List<File> getLibs() {
        return libs;
    }

    public String getIzpackAppName() {
        return izpackAppName;
    }

    public String getIzpackAppVersion() {
        return izpackAppVersion;
    }

    public String getIzpackFrameIcon() {
        return FilenameUtils.getName(izpackFrameIconPath);
    }

    public String getIzpackHelloIcon() {
        return FilenameUtils.getName(izpackHelloIconPath);
    }


    public List<String> getIzpackAdditionalPacks() {
        try {
            return IOUtils.readLines(new InputStreamReader(loader.getResource(izpackAdditionalPacksPath).getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException("IzpackAdditionalPacks: Loading failed");
        }
    }

    public String getIzpackAppFilesPackName() {
        return izpackAppFilesPackName;
    }

    public String getIzpackAppFilesPackDescription() {
        return izpackAppFilesPackDescription;
    }

    public String getIzpackJREPackName() {
        return izpackJREPackName;
    }

    public String getIzpackJREPackDescription() {
        return izpackJREPackDescription;
    }

    public String getIzpackWindowsServicePackName() {
        return izpackWindowsServicePackName;
    }

    public String getIzpackWindowsServicePackDescription() {
        return izpackWindowsServicePackDescription;
    }


    public String getPrunsrvStartupMode() {
        return prunsrvStartupMode;
    }

    public String getPrunsrvServiceName() {
//        http://stackoverflow.com/questions/8519669/replace-non-ascii-character-from-string/8519863#8519863
        return prunsrvServiceName.replaceAll("[^\\x00-\\x7F]", "_");
    }

    public String getPrunsrvStartClass() {
        return prunsrvStartClass;
    }

    public String getPrunsrvDaemonLauncherClass() {
        return prunsrvDaemonLauncherClass;
    }

    public String getPrunsrvStartParams() {
        return null != prunsrvStartParams ? prunsrvStartParams : "start;" + prunsrvDaemonLauncherClass;
    }

    public String getPrunsrvStopClass() {
        return prunsrvStopClass;
    }

    public String getPrunsrvStopParams() {
        return null != prunsrvStopParams ? prunsrvStopParams : "stop;" + prunsrvDaemonLauncherClass;
    }

    public String getPrunsrvJvmOptions() {
        return prunsrvJvmOptions;
    }

    public int getPrunsrvJvmMs() {
        return prunsrvJvmMs;
    }

    public int getPrunsrvJvmMx() {
        return prunsrvJvmMx;
    }

    public int getPrunsrvJvmSs() {
        return prunsrvJvmSs;
    }

    public String getPrunsrvDisplayName() {
        return prunsrvDisplayName.replace("\"","\\\"");
    }

    public String getPrunsrvDescription() {
        return prunsrvDescription.replace("\"","\\\"");
    }

    public int getPrunsrvStopTimeout() {
        return prunsrvStopTimeout;
    }

    public String getPrunsrvLogPrefix() {
        return prunsrvLogPrefix;
    }

    public String getPrunsrvLogLevel() {
        return prunsrvLogLevel;
    }

    public String getPrunsrvStdOutput() {
        return prunsrvStdOutput;
    }

    public String getPrunsrvStdError() {
        return prunsrvStdError;
    }

    public boolean isPrunsrvStartOnInstrall() {
        return prunsrvStartOnInstrall;
    }
}
