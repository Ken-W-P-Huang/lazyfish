package com.windfish.gradle.task.node

import com.windfish.gradle.foundation.Platform
import com.windfish.gradle.foundation.extension.NodeExtension
import com.windfish.gradle.foundation.file.MsiFile
import com.windfish.gradle.foundation.file.PkgFile
import com.windfish.gradle.foundation.file.TarXzFile
import groovy.swing.SwingBuilder
import org.gradle.api.GradleException

/**
 * Created by kenhuang on 2018/10/27.
 */
class InstallNodeTask extends NodeTask {
    public final static String NAME = "installNode"
    public static final String GROUP = "node"
    private NodeExtension extension

    InstallNodeTask() {
        super()
        this.description = 'Download and install Node server.'
    }

    private void installNodeInLinux(String tmpdir, String urlString, String password) {
        TarXzFile tarXzFile =
                new TarXzFile(tmpdir, "node-v${this.extension.version}-linux-${Platform.getInstance().arch}")
        tarXzFile.download("${urlString}/${tarXzFile.name}")
        tarXzFile.untar(this.project,tmpdir){
            exclude "**/CHANGELOG.md"
            exclude "**/LICENSE"
            exclude "**/README.md"
        }
        this.project.exec{
            standardInput = new ByteArrayInputStream(password.bytes)
            commandLine "sudo",'-S','cp','-r',"${this.project.file(tmpdir)}/${tarXzFile.name}",'/usr/'
        }
    }

    private boolean isNodeInstalledInUnix() {
        ByteArrayOutputStream output = new ByteArrayOutputStream()
        try {
            this.executeCommand {it->
                it.commandLine "which", "node"
                it.standardOutput = output
            }
        } catch (Exception e) {
            output = new ByteArrayOutputStream()
        }
        ByteArrayInputStream input = new ByteArrayInputStream(output.buf)
        output = new ByteArrayOutputStream()
        this.project.exec {
            standardInput = input
            standardOutput = output
            commandLine "wc", "-l"
        }
        return (Integer.parseInt(output.toString().trim()) >= 1)
    }

    private boolean isNodeInstalledInWindows() {
        return new File("C:\\Program Files\\nodejs\\node.exe").exists()
    }
    private String getSudoPassword(){
        String password=""
        if(System.console() == null) {
            SwingBuilder swingBuilder =   new SwingBuilder().edt {
                dialog(modal: true,
                        title: 'Sudo',
                        alwaysOnTop: true,
                        resizable: false,
                        locationRelativeTo: null,
                        pack: true,
                        show: true
                ) {
                    vbox {
                        label(text: "Please enter sudo passphrase:")
                        input = passwordField()
                        button(defaultButton: true, text: 'OK', actionPerformed: {
                            password = input.getText()
                            dispose()
                        })
                    }
                }
            }

        } else {
            password = new String(System.console().readPassword("\nPlease enter sudo passphrase: "))
        }
        return  password+System.lineSeparator()
    }

    void setExtension(NodeExtension extension, String tmpDirPath) {
        String password
        this.extension = extension
        String urlString = "${this.extension.distUrl}/v${this.extension.version}"
        File tmpDir = this.project.file(tmpDirPath)
        tmpDir.mkdirs()
        this.setCommandLine("echo", 'Node is already installed')
        switch (Platform.getInstance().os) {
            case 'windows':
                if (!this.isNodeInstalledInWindows()) {
                    MsiFile msiFile = new MsiFile(tmpDir, "node-v${this.extension.version}-${Platform.getInstance().arch}")
                    msiFile.download("${urlString}/${msiFile.name}")
                    this.project.exec{
                        commandLine "${msiFile.absolutePath}"
                    }
                    this.setCommandLine("echo", 'Node is installed')
                }
                break
            case 'macos':
                if (!this.isNodeInstalledInUnix()) {
                    if (this.extension.version == '') {
                        this.project.exec {
                            commandLine 'brew', 'install', 'node'
                        }
                    } else {
                        PkgFile pkgFile = new PkgFile(tmpDir, "node-v${this.extension.version}")
                        pkgFile.download("${urlString}/${pkgFile.name}")
                        pkgFile.setExecutable(true)
                        password = this.getSudoPassword()
                        this.project.exec {
                            standardInput = new ByteArrayInputStream(password.bytes)
                            commandLine "sudo","-S","installer", "-package", "${pkgFile.absolutePath}", "-target", "/"
                        }
                    }
                    this.setCommandLine("echo", 'Node is installed')
                }
                break

            case 'deepin' | 'ubuntu':
                if (!this.isNodeInstalledInUnix()) {
                    password = this.getSudoPassword()
                    if (this.extension.version == '') {
                        this.project.exec {
                            standardInput = new ByteArrayInputStream(password.bytes)
                            commandLine 'sudo', "-S",'apt-get', 'install', '-y', 'nodejs', 'npm'
                        }
                    } else {
                        this.installNodeInLinux(tmpDirStr, urlString,password)
                    }
                    this.setCommandLine("echo", 'Node is installed')
                }
                break

            case 'centos':
                if (!this.isNodeInstalledInUnix()) {
                    password = this.getSudoPassword()
                    if (this.extension.version == '') {
                        this.project.exec {
                            standardInput = new ByteArrayInputStream(password.bytes)
                            commandLine 'sudo', "-S", 'yum', 'install', '-y', 'nodejs', 'npm'
                        }
                    } else {
                        this.installNodeInLinux(tmpDirStr, urlString,password)
                    }
                    this.setCommandLine("echo", 'Node is installed')
                }
                break
            default:
                throw new GradleException('Unsupported OS to install Node.js!')
                break
        }

    }
}
