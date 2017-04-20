package eu.tazl.installer;

import eu.tazl.installer.standalone.InstallerBuilder;
import eu.tazl.installer.standalone.InstallerConfig;
import eu.tazl.installer.standalone.InstallerConfigBuilder;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.io.IOUtils.closeQuietly;
import static org.apache.commons.io.IOUtils.copyLarge;



/**
 * Maven plugin, creates izPack installer
 *
 * @author alexkasko
 * @author olitazl
 * Date: 4/19/12
 * @goal installer
 * @phase package
 * @requiresDependencyResolution runtime
 */

public class InstallerMojo extends SettingsMojo implements InstallerConfigBuilder {

    /**
     * Plugin entry point
     *
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
            InstallerConfig config = getInstallerConfig();


            InstallerBuilder builder = new InstallerBuilder(config);
            builder.execute();
    }

    @SuppressWarnings("unchecked")
    public InstallerConfig getInstallerConfig() {
        Set<Artifact> set = project.getArtifacts();

        return new InstallerConfig(izpackAppFilesPackName, izpackAppFilesPackDescription, izpackJREPackName, izpackJREPackDescription,
                izpackWindowsServicePackName, izpackWindowsServicePackDescription, prunsrvStartupMode, prunsrvServiceName,
                prunsrvStartClass, prunsrvDaemonLauncherClass, prunsrvStartParams, prunsrvStopClass, prunsrvStopParams, prunsrvJvmMs,
                prunsrvJvmMx, prunsrvJvmSs, prunsrvDisplayName, prunsrvDescription, prunsrvStopTimeout, prunsrvLogPrefix,
                prunsrvLogLevel, prunsrvStdOutput, prunsrvStdError, prunsrvStartOnInstrall, izpackDir,distDir,buildUnixDist,
                prunsrvLogPath,installerOutputFile,buildOutputFile,installConfigFile,
                izpackOutputFile, use64BitJre, useX86LaunchersForX64Installer, installLauncher64Path, installLauncher32Path,
                distOutputFile, prunsrvLauncherJarFile, appDataDirs, uninstallLauncher64Path, uninstallLauncher32Path,
                izpackDefaultInstallDir, prunsrvScriptsEncoding, izpackFrameIconPath, izpackHelloIconPath, izpackAdditionalResourcePaths,
                izpackCompress, jreDir, project.getArtifact().getFile(), set.stream().map(x-> x.getFile()).collect(Collectors.toList()),
                izpackAppName, izpackAppVersion, izpackLang, izpackAdditionalPacksPath);

    }
}
