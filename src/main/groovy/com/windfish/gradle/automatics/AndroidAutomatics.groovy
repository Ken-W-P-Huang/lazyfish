package com.windfish.gradle.automatics

import com.windfish.gradle.foundation.extension.AndroidExtension
import org.gradle.api.Project
/**
 * Created by kenhuang on 2018/10/30.
 */
class AndroidAutomatics extends JavaAutomatics {
    AndroidAutomatics(Project project) {
        super(project)
        this.project = project
        this.extension = project.getExtensions().create(AndroidExtension.NAME, AndroidExtension)

    }
}
