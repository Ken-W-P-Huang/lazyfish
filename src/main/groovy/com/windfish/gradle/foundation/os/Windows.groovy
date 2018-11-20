package com.windfish.gradle.foundation.os

import com.windfish.gradle.foundation.file.MsiFile

/**
 * Created by kenhuang on 2018/11/13.
 */
class Windows extends OS{

    Windows() {
        super()
        this.binPath = ":C:\\Program Files\\nodejs\\"
    }

    public void executeCommand(Closure closure){
        this.project.exec {
            closure(it)
        }
    }

    boolean isNodeInstalled() {
        return new File("C:\\Program Files\\nodejs\\node.exe").exists()
    }

    @Override
    String which(String command) {
        ByteArrayOutputStream output = new ByteArrayOutputStream()
        this.executeCommand {
            it.commandLine "where", command
            it.standardOutput = output
        }
        return output.toString().trim()
    }

    File getNodeModulesDir(boolean isGlobal) {
        if (isGlobal){
            return  new File('')
        }else{
            return this.project.file('node_modules')
        }
    }

    @Override
    protected void installNodeByStep(String nodeVersion,String tmpDirPath, String urlString) {
        if (nodeVersion == ''){
            nodeVersion = '10.13.0'
        }
        MsiFile msiFile = new MsiFile(tmpDirPath, "node-v${nodeVersion}-${this.arch}")
        msiFile.download("${urlString}/${msiFile.name}")
        this.executeCommand {
            it.commandLine "${msiFile.absolutePath}"
        }
    }

}
