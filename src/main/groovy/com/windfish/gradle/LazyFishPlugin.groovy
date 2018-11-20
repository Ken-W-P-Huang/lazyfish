package com.windfish.gradle

import com.windfish.gradle.automatics.AndroidAutomatics
import com.windfish.gradle.automatics.HadoopAutomatics
import com.windfish.gradle.automatics.JavaAutomatics
import com.windfish.gradle.automatics.JavaWebAutomatics
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.plugins.PluginManager

/**
 * Created by kenhuang on 2018/10/27.
 */
public class LazyFishPlugin implements Plugin<Project> {
    public static final String GROUP = "lazyfish"

    @Override
    void apply(Project project) {
        /*不能在afterEvaluate执行*/
        PluginManager pluginManager = project.getPluginManager()
        def automatics = null
        /*id引入插件时以下代码无效*/
//        if (pluginManager.hasPlugin("war")) {
//            automatics = new JavaWebAutomatics(project)
//        } else if (pluginManager.hasPlugin("android")) {
//            automatics = new AndroidAutomatics(project)
//        } else if (pluginManager.hasPlugin("hadoop")) {
//            automatics = new HadoopAutomatics(project)
//        } else if (pluginManager.hasPlugin("java")) {
//            automatics = new JavaAutomatics(project)
//        } else {
//            throw new Exception("Unknown project type!This plugin should be applied after plugin java,android or war!")
//        }

        pluginManager.withPlugin('war'){
            if (automatics == null || automatics instanceof JavaAutomatics){
                automatics = new JavaWebAutomatics(project)
            }
        }

        pluginManager.withPlugin('android'){
            if (automatics == null || automatics instanceof JavaAutomatics){
                automatics = new AndroidAutomatics(project)
            }
        }
        pluginManager.withPlugin('hadoop'){
            if (automatics == null || automatics instanceof JavaAutomatics) {
                automatics = new HadoopAutomatics(project)
            }
        }
        pluginManager.withPlugin('java'){
            if (automatics == null) {
                automatics = new JavaAutomatics(project)
            }
        }
        project.afterEvaluate {
            if (automatics != null){
                automatics.execute()
            }else{
                throw new Exception("Unknown project type!This plugin should be applied after plugin java,android or war!")
            }
        }
    }
}







