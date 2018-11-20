package com.windfish.gradle.foundation.os

import com.windfish.gradle.foundation.file.TarXzFile

/**
 * Created by kenhuang on 2018/11/13.
 */
abstract class Linux extends Unix{
    Linux() {
        super()
    }
    @Override
    File getNodeModulesDir(boolean isGlobal) {
        if (isGlobal){
            return  new File("/usr/lib/node_modules/")
        }else{
            return this.project.file('node_modules')
        }
    }

    abstract void installNodeWithCommand(String password)
    protected void installNodeByStep(String nodeVersion, String tmpDirPath, String urlString) {
        String password = Unix.getSudoPassword("Please enter sudo passphrase to install node:")
        if (nodeVersion == '') {
            this.installNodeWithCommand(password)
        } else {
            TarXzFile tarXzFile = new TarXzFile(tmpDirPath, "node-v${nodeVersion}-linux-${this.arch}")
            tarXzFile.download("${urlString}/${tarXzFile.name}")
            tarXzFile.untar(this.project,tmpDirPath){
                it.exclude "**/CHANGELOG.md"
                it.exclude "**/LICENSE"
                it.exclude "**/README.md"
            }
            this.executeCommand {
                it.standardInput = new ByteArrayInputStream(password.bytes)
                it.commandLine "sudo",'-S','cp','-r',"${this.project.file(tmpDirPath)}/${tarXzFile.name}",'/usr/'
            }
        }
    }

}
