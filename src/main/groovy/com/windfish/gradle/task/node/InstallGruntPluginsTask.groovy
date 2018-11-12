package com.windfish.gradle.task.node

import com.windfish.gradle.foundation.extension.GruntExtension

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
        if (this.isModuleInstalled("grunt-contrib-concat", extension.concatVersion)
                && this.isModuleInstalled("grunt-contrib-csslint", extension.csslintVersion)
                && this.isModuleInstalled("grunt-contrib-cssmin", extension.cssminVersion)
                && this.isModuleInstalled("grunt-contrib-jshint", extension.jshintVersion)
                && this.isModuleInstalled("grunt-contrib-uglify", extension.uglifyVersion)
                && this.isModuleInstalled("grunt-contrib-watch", extension.watchVersion)
                && this.isModuleInstalled("json", extension.jsonVersion)) {
            super.setCommandLine('echo', 'grunt plugins have been installed!')
        } else {
            String concatVersion = ''
            String csslintVersion = ''
            String cssminVersion = ''
            String jshintVersion = ''
            String uglifyVersion = ''
            String watchVersion = ''
            String jsonVersion = ''
            if (extension.concatVersion != null && extension.concatVersion != ''){
                concatVersion = "@${extension.concatVersion}"
            }
            if (extension.csslintVersion != null && extension.csslintVersion != ''){
                csslintVersion = "@${extension.csslintVersion}"
            }
            if (extension.cssminVersion != null && extension.cssminVersion != ''){
                cssminVersion = "@${extension.cssminVersion}"
            }
            if (extension.jshintVersion != null && extension.jshintVersion != ''){
                jshintVersion = "@${extension.jshintVersion}"
            }
            if (extension.uglifyVersion != null && extension.uglifyVersion != ''){
                uglifyVersion = "@${extension.uglifyVersion}"
            }
            if (extension.watchVersion != null && extension.watchVersion != ''){
                watchVersion = "@${extension.watchVersion}"
            }
            if (extension.jsonVersion != null && extension.jsonVersion != ''){
                jsonVersion = "@${extension.jsonVersion}"
            }
            super.setCommandLine(this.whichCommand('npm'), 'install', "grunt-contrib-concat${concatVersion}",
                    "grunt-contrib-csslint${csslintVersion}",
                    "grunt-contrib-cssmin${cssminVersion}",
                    "grunt-contrib-jshint${jshintVersion}",
                    "grunt-contrib-uglify${uglifyVersion}",
                    "grunt-contrib-watch${watchVersion}",
                    "json${jsonVersion}")
        }
    }

}




