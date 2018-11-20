package com.windfish.gradle.task.node

import com.windfish.gradle.foundation.os.OS

/**
 * Created by kenhuang on 2018/10/27.
 */
class NpmInitTask extends NodeTask {
    public final static String NAME = "npmInit"

    NpmInitTask() {
        super()
        this.description = 'Init npm project with package.json.'
        this.dependsOn(InstallGruntPluginsTask.NAME)
    }

    @Override
    void doExecute(){
        OS.instance.executeCommand {
            it.commandLine OS.instance.which('npm'), 'init', '-y'
        }
    }

}
