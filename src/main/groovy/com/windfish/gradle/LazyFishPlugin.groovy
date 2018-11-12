package com.windfish.gradle

import com.windfish.gradle.automatics.AndroidAutomatics
import com.windfish.gradle.automatics.HadoopAutomatics
import com.windfish.gradle.automatics.JavaAutomatics
import com.windfish.gradle.automatics.JavaWebAutomatics
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer

/**
 * Created by kenhuang on 2018/10/27.
 */
public class LazyFishPlugin implements Plugin<Project> {
    public static final String GROUP = "lazyfish"

    @Override
    void apply(Project project) {
        PluginContainer pluginContainer = project.getPlugins()
        def automatics = null
        if (pluginContainer.hasPlugin("war")) {
            automatics = new JavaWebAutomatics(project)
        } else if (pluginContainer.hasPlugin("android")) {
            automatics = new AndroidAutomatics(project)
        } else if (pluginContainer.hasPlugin("hadoop")) {
            automatics = new HadoopAutomatics(project)
        } else if (pluginContainer.hasPlugin("java")) {
            automatics = new JavaAutomatics(project)
        } else {
            throw new Exception("Unknown project type!This plugin should be applied after plugin java,android or war!")
        }
        project.afterEvaluate {
            automatics.execute()
        }
    }
}







