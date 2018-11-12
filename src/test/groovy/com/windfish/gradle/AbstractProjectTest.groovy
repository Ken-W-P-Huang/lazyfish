package com.windfish.gradle

import org.gradle.api.internal.project.ProjectInternal
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class AbstractProjectTest extends Specification {
    @Rule
    final TemporaryFolder temporaryFolder = new TemporaryFolder()
    //创建临时文件夹作为项目文件夹

    def ProjectInternal project
    def File projectDir
    def File buildFile

    def setup() {
        this.projectDir = this.temporaryFolder.root
        this.project = (ProjectInternal) ProjectBuilder.builder()
                .withProjectDir(this.projectDir).build()
        this.buildFile = new File(this.temporaryFolder.getRoot(), 'build.gradle')

    }

    void createFileWithContent(String name, String content) {
        File file = new File(this.temporaryFolder.getRoot(), name)
        file.parentFile.mkdirs()
        file << content
    }

    BuildTask run(String... args) {
        return GradleRunner.create()
                .withProjectDir(this.projectDir)
                .withArguments(args)
                .withPluginClasspath()
                .build()
    }

    BuildTask runTask(String task) {
        return GradleRunner.create()
                .withProjectDir(this.projectDir)
                .withArguments(task)
                .withPluginClasspath()
                .build()
                .task(":${task}")
    }
}
