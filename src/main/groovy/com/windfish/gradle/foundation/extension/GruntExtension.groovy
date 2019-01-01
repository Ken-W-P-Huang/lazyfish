package com.windfish.gradle.foundation.extension
/**
 * Created by kenhuang on 2018/10/30.
 */
class GruntExtension {
    String gruntVersion = ""
    boolean isGlobal =false
    File nodeModulesDir
    String gruntCliVersion = ""
    public Map<String, String> plugins=["grunt-contrib-concat":"",
                                        "grunt-contrib-csslint":"",
                                        "grunt-contrib-cssmin":"",
                                        "grunt-contrib-jshint":"",
                                        "grunt-contrib-uglify":"",
                                        "grunt-contrib-watch":"",
                                        "grunt-contrib-htmlmin":"",
                                        "grunt-includes":"",
                                        "grunt-contrib-less":"",
                                        "json":"",]
}


