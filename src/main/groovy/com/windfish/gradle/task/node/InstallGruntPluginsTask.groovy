package com.windfish.gradle.task.node

import com.windfish.gradle.foundation.extension.GruntExtension
import com.windfish.gradle.foundation.os.OS

/**
 * Created by kenhuang on 2018/10/27.
 */
class InstallGruntPluginsTask extends NpmTask {
    public final static String NAME = "InstallGruntPlugins"
    private GruntExtension extension
    InstallGruntPluginsTask() {
        super()
        this.description = 'Install Grunt plugins.'
        this.dependsOn(InstallGruntTask.NAME)
    }
    void setExtension(GruntExtension extension) {
        this.extension = extension
    }

    @Override
    void doExecute() {
        File nodeModulesDir = OS.instance.getNodeModulesDir(this.extension.isGlobal)
        this.extension.plugins.each{plugin ->
            this.installModule(plugin.getKey(),plugin.getValue(),nodeModulesDir)
        }
    }

}




