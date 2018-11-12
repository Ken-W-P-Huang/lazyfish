package com.windfish.gradle.task.node

import org.gradle.api.Project

/**
 * Created by kenhuang on 2018/11/9.
 */
class NpmTask extends  NodeTask{
    NpmTask(){
        super()
        this.dependsOn(InstallNodeTask.NAME)
    }
    public  boolean isModuleInstalled( String moduleName, String version) {
        ByteArrayOutputStream output = new ByteArrayOutputStream()
        try {
            if (version == null) {
                this.executeCommand {
                    it.commandLine this.whichCommand("npm"), "list", "${moduleName}"
                    it.standardOutput = output
                }
            } else {
                this.executeCommand {
                    it.commandLine this.whichCommand("npm"), "list", "${moduleName}@${version}"
                    it.standardOutput = output
                }
            }
        } catch (Exception e) {
            println e
            println output
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
}
