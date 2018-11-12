package com.windfish.gradle.automatics

import com.windfish.gradle.foundation.IntelliJIDEA
import com.windfish.gradle.foundation.extension.JavaExtension
import com.windfish.gradle.foundation.file.FileUtil
import com.windfish.gradle.foundation.file.JarFile
import com.windfish.gradle.foundation.file.ZipFile
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer

import java.nio.ByteBuffer
import java.nio.channels.FileChannel

/**
 * Created by kenhuang on 2018/10/30.
 */
class JavaAutomatics {
    Project project
    protected JavaExtension extension

    JavaAutomatics() {

    }

    JavaAutomatics(Project project) {
        this.project = project
        this.extension = project.getExtensions().create(JavaExtension.NAME, JavaExtension)

    }

    public void execute() {
        new IntelliJIDEA(this.project).initVcs(this.extension.vcsType, this.extension.vcsRemotes)
        this.createDirectories()
        this.copyLicense()
        this.generateReadme()
        this.addDependencies()
    }

/******************************************************************************************************************
 * 创建项目文件夹和文件
 ******************************************************************************************************************/
    protected void createDirectories() {
        if (this.extension.directories != null) {
            FileUtil.mkSubDirs(this.project, this.extension.directories)
        }
        if (this.extension.mirrorToTest) {
            JavaPluginConvention javaPlugin = this.project.getConvention().getPlugin(JavaPluginConvention.class)
            SourceSetContainer sourceSets = javaPlugin.getSourceSets()
            SourceSet main = sourceSets.getByName("main")
            SourceSet test = sourceSets.getByName("test")
            main.getAllJava().srcDirs.each { source ->
                test.getAllJava().srcDirs.each { destination ->
                    /*main的java子文件夹的内容映射到test的java子文件夹，groovy相同*/
                    if (source.name.equals(destination.name)) {
                        FileUtil.mirrorSourceToTest(source, destination,
                                this.project.fileTree(source, this.extension.mirrorFilter),
                                this.extension.testFramework.toLowerCase())
                    }
                }
            }
        }
    }
/******************************************************************************************************************
 *  添加项目依赖
 ******************************************************************************************************************/
    protected void addDependencies() {
        String key, value
        if (this.extension.dependencies != null) {
            this.extension.dependencies.each { dependency ->
                this.project.dependencies.add("compile", dependency.getValue())
            }
        }
        if (this.extension.testDependencies != null) {
            this.extension.testDependencies.each { testDependency ->
                this.project.dependencies.add("testCompile", testDependency.getValue())
            }
        }
        if (this.extension.zipDependencies != null) {
            ZipFile zipFile
            JarFile jarFile
            this.project.file(this.extension.zipTmpPath).mkdirs()
            this.extension.zipDependencies.each { zipDependency ->
                key = zipDependency.getKey()
                value = zipDependency.getValue()
                zipFile = new ZipFile("${this.project.file(this.extension.zipTmpPath)}", key)
                jarFile = new JarFile("${this.project.file(this.extension.jarLibPath)}", key)
                zipFile.download(value)
                zipFile.compile(this.project, jarFile)
            }
        }
    }
/******************************************************************************************************************
 * License & Readme
 ******************************************************************************************************************/
    protected void copyLicense() {
        File licenseFile = project.file('LICENSE')
        if (this.extension.license != null && this.extension.license != '') {
            if (!licenseFile.exists()) {
                FileUtil.copyResource("license/${this.extension.license}", licenseFile)
            }
        } else {
            if (licenseFile.exists()) {
                licenseFile.delete()
            }
        }
    }

    protected void generateReadme() {
        File readmeFile = this.project.file("README.md")
        if (this.extension.addReadme) {
            if (!readmeFile.exists()) {
                String content = "# \n\n# Getting Started\n"
                if (this.extension.license != null && this.extension.license != '') {
                    content = content.concat("\n\n# License\n\n${this.extension.license}\n")
                }
                content = content.concat("\n\n# Badges\n")
                FileChannel outputChannel = new FileOutputStream(readmeFile).getChannel()
                ByteBuffer buffer = ByteBuffer.wrap(content.bytes)
                outputChannel.write(buffer)
                outputChannel.close()
            }
        } else {
            if (readmeFile.exists()) {
                readmeFile.delete()
            }
        }
    }
}
