package com.windfish.gradle.task.node

import com.windfish.gradle.foundation.os.OS

/**
 * Created by kenhuang on 2018/11/6.
 */
class CompileFrontendTask extends NodeTask {
    public final static String NAME = "compileFrontend"
    private boolean isGlobal
    CompileFrontendTask() {
        super()
        this.dependsOn(InstallGruntPluginsTask.NAME)
    }

    void setIsGlobal(boolean isGlobal) {
        this.isGlobal = isGlobal
    }

    boolean getIsGlobal() {
        return isGlobal
    }

    @Override
    void doExecute(){
        OS.getInstance().executeCommand {
            it.commandLine "${OS.instance.getNodeModulesDir(this.isGlobal)}/.bin/grunt"
        }
    }
}


