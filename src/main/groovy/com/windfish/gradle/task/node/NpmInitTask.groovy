package com.windfish.gradle.task.node

import org.gradle.api.Project
import org.gradle.api.tasks.AbstractExecTask

/**
 * Created by kenhuang on 2018/10/27.
 */
class NpmInitTask extends NodeTask {
    public final static String NAME = "npmInit"

    NpmInitTask() {
        super()
        this.description = 'Init npm project with package.json.'
        this.dependsOn(InstallGruntPluginsTask.NAME)
        this.setCommandLine(null)
    }

    @Override
    final void setCommandLine(Iterable args) {
        super.setCommandLine(this.whichCommand('npm'), 'init', '-y')
    }

    @Override
    final AbstractExecTask setArgs(Iterable arguments) {
        return super.setArgs([this.whichCommand('npm'), 'init', '-y',this.project.projectDir.absolutePath])
    }


}
