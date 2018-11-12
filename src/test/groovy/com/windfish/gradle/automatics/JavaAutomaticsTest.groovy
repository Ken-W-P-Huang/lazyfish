package com.windfish.gradle.automatics

import com.windfish.gradle.AbstractProjectTest
import com.windfish.gradle.foundation.extension.JavaExtension
import com.windfish.gradle.foundation.file.FileUtil
import org.gradle.api.internal.ClosureBackedAction
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer

/**
 * Created by kenhuang on 2018/10/30.
 */
class JavaAutomaticsTest extends AbstractProjectTest {
    def "Execute"() {

    }

    def "CreateProjectDirectories"() {

    }

    def "AddDependencies"() {

    }

    def "CopyLicense"() {
        given:
        when:
        JavaAutomatics javaAutomatics = new JavaAutomatics(this.project)
        this.project.extensions.configure(JavaExtension, new ClosureBackedAction<JavaExtension>({
            dependencies = ['servlet': 'javax.servlet:javax.servlet-api:3.1.0',
                            'netty'  : "io.netty:netty-all:4.1.19.Final"]
            testDependencies = ['mockito': 'org.mockito:mockito-core:1.+',
                                'junit'  : 'junit:junit:4.+',]
            vcsType = 'git'
            mirrorToTest = true
            license = 'Apache-2.0'
        }))
        javaAutomatics.extension = project.extensions.getByName('java')
        javaAutomatics.copyLicense()
        then:
        this.project.file('LICENSE').exists()
    }

    def "GenerateReadme"() {

    }

    def "GetProject"() {

    }

    def "SetProject"() {

    }

    def "test"() {
        given:

        when:
        this.project.plugins.apply('groovy')
        JavaPluginConvention javaPlugin = this.project.getConvention().getPlugin(JavaPluginConvention.class)
        SourceSetContainer sourceSets = javaPlugin.getSourceSets()
        SourceSet main = sourceSets.getByName("main")
        SourceSet test = sourceSets.getByName("test")
        then:
        main.getAllJava().srcDirs.each { source ->
            test.getAllJava().srcDirs.each { destination ->
                if (source.name == destination.name) {
                    println(source.absolutePath + destination.absolutePath)
                    FileUtil.mirrorSourceToTest(source, destination)
                }
            }
        }
    }
}
