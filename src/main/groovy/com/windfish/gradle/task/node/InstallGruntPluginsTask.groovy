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
        this.description = 'Install Grunt concat,csslint,cssmin,jshint,uglify,watch,json.'
        this.dependsOn(InstallGruntTask.NAME)
    }
    void setExtension(GruntExtension extension) {
        this.extension = extension
    }

    @Override
    void doExecute() {
        File nodeModulesDir = OS.instance.getNodeModulesDir(this.extension.isGlobal)
        this.installModule("grunt-contrib-concat",this.extension.concatVersion,nodeModulesDir)
        this.installModule("grunt-contrib-csslint",this.extension.csslintVersion,nodeModulesDir)
        this.installModule("grunt-contrib-cssmin",this.extension.cssminVersion,nodeModulesDir)
        this.installModule("grunt-contrib-jshint",this.extension.jshintVersion,nodeModulesDir)
        this.installModule("grunt-contrib-uglify",this.extension.uglifyVersion,nodeModulesDir)
        this.installModule("grunt-contrib-watch",this.extension.watchVersion,nodeModulesDir)
        this.installModule("json",this.extension.jsonVersion,nodeModulesDir)
    }

}




