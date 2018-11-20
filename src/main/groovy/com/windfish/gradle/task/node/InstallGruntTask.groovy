package com.windfish.gradle.task.node

import com.windfish.gradle.foundation.extension.GruntExtension
import com.windfish.gradle.foundation.os.OS

/**
 * Created by kenhuang on 2018/10/27.
 */
class InstallGruntTask extends NpmTask {
    public final static String NAME = "installGrunt"
    private GruntExtension extension
    InstallGruntTask() {
        super()
        this.description = 'Install Grunt application.'
    }

    void setExtension(GruntExtension extension) {
        this.extension = extension
    }

    @Override
    void doExecute() {
        File nodeModulesDir = OS.instance.getNodeModulesDir(this.extension.isGlobal)
        this.installModule("grunt",this.extension.gruntVersion, nodeModulesDir)
        this.installModule("grunt-cli",this.extension.gruntCliVersion,nodeModulesDir)
    }
}


