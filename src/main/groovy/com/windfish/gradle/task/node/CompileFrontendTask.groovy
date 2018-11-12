package com.windfish.gradle.task.node

import org.gradle.api.tasks.AbstractExecTask

/**
 * Created by kenhuang on 2018/11/6.
 */
class CompileFrontendTask extends NodeTask {
    public final static String NAME = "compileFrontend"

    CompileFrontendTask() {
        super()
        this.dependsOn(InstallGruntPluginsTask.NAME)
        this.setCommandLine(null)
    }

    @Override
    final void setCommandLine(Iterable args) {
        super.setCommandLine("node_modules/.bin/grunt")
    }

    @Override
    final AbstractExecTask setArgs(Iterable arguments) {
        return super.setArgs(["node_modules/.bin/grunt"])
    }

}


