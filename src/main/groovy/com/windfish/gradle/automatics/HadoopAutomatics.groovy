package com.windfish.gradle.automatics

import com.windfish.gradle.foundation.extension.HadoopExtension
import org.gradle.api.Project

/**
 * Created by kenhuang on 2018/10/30.
 */
class HadoopAutomatics extends JavaAutomatics {
    HadoopAutomatics(Project project) {
        super(project)
        this.project = project
        this.extension = project.getExtensions().create(HadoopExtension.NAME, HadoopExtension)
    }
}
