package com.windfish.gradle.task.node

import com.windfish.gradle.foundation.extension.GruntExtension

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
        if (this.isModuleInstalled("grunt", null)
                && this.isModuleInstalled("grunt-cli", null)) {
            super.setCommandLine('echo', 'grunt has been installed!')
        } else {
            String gruntCliVersion = ''
            String gruntVersion = ''
            if (extension.gruntVersion != null && extension.gruntVersion != ''){
                gruntVersion = "@${extension.gruntVersion}"
            }
            if (extension.gruntCliVersion != null && extension.gruntCliVersion != ''){
                gruntCliVersion = "@${extension.gruntCliVersion}"
            }
            super.setCommandLine(this.whichCommand('npm'), 'install',
                    "grunt${gruntVersion}",  "grunt-cli${gruntCliVersion}")
        }
    }

}


