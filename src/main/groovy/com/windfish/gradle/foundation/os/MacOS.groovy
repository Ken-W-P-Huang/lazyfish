package com.windfish.gradle.foundation.os

import com.windfish.gradle.foundation.file.PkgFile

/**
 * Created by kenhuang on 2018/11/13.
 */
class MacOS extends Unix {
    MacOS() {
        super()
    }
    @Override
    File getNodeModulesDir(boolean isGlobal) {
        if (isGlobal){
            return  new File("/usr/local/lib/node_modules/")
        }else{
            return this.project.file('node_modules')
        }
    }

    @Override
    protected void installNodeByStep(String nodeVersion, String tmpDirPath, String urlString) {
        if (nodeVersion == '') {
            this.executeCommand {
                it.commandLine 'brew', 'install', 'node'
            }
        } else {
            PkgFile pkgFile = new PkgFile(tmpDirPath, "node-v${nodeVersion}")
            pkgFile.download("${urlString}/${pkgFile.name}")
            pkgFile.setExecutable(true)
            String password = Unix.getSudoPassword("Please enter sudo passphrase to install node:")
            this.executeCommand {
                it.standardInput = new ByteArrayInputStream(password.bytes)
                it.commandLine "sudo","-S","installer", "-package", "${pkgFile.absolutePath}", "-target", "/"
            }
        }
    }


}
