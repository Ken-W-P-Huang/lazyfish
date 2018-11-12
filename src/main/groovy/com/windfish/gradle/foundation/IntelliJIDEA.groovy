package com.windfish.gradle.foundation

import com.windfish.gradle.foundation.file.FileUtil
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.gradle.api.Project

/**
 * Created by kenhuang on 2018/10/27.
 */
enum IntelliJIDEATaskPhase {
    BEFORE_SYNC(), AFTER_SYNC(), BEFORE_BUILD(), AFTER_BUILD(), BEFORE_REBUILD(), AFTER_REBUILD(),
}

class IntelliJIDEA {
    private Project project

    IntelliJIDEA(Project project) {
        this.project = project
    }
/******************************************************************************************************************
 * VCS
 ******************************************************************************************************************/
    private void addVcsXML(String vcsType) {
        def vcsXML = this.project.file(".idea/vcs.xml")
        this.project.file(".idea").mkdirs()
        def projectNode = new XmlSlurper().parseText("<project version=\"4\"/>")
        projectNode.appendNode {
            component(name: 'VcsDirectoryMappings') {
                mapping(directory: "\$PROJECT_DIR\$", vcs: "$vcsType")
            }
        }
        def newRootNode = new StreamingMarkupBuilder().bind { mkp.yield projectNode }.toString()
        XmlUtil.serialize(newRootNode, new FileOutputStream(vcsXML))
    }

    public void initVcs(String vcsType, Map<String, String> vcsRemotes) {
        switch (vcsType.toLowerCase()) {
            case "git":
                File gitDirectory = this.project.file(".git")
                if (!gitDirectory.exists()) {
                    println('git init'.execute([], this.project.rootDir).text.trim())
                }
                FileUtil.copyResource("cvs/git.gitignore", this.project.file(".gitignore"))
                this.addVcsXML("Git")
                //添加git远程服务器
                vcsRemotes.each { item ->
                    ("git remote add ${item.key} ${item.value}").execute([], this.project.rootDir).text.trim()
                }
                break
            case "svn":
            case "subversion":
                break
            case "cvs":
                break
            default:
                break
        }

    }
/******************************************************************************************************************
 * 设置Gradle任务何时自动执行
 ******************************************************************************************************************/
    public void setPhases(IntelliJIDEATaskPhase[] phases) {
        if (phases != null && phases.length > 0) {
            def phaseName
            phases.each { phase ->
                phaseName = phase.toString().toLowerCase()
                def workspaceXML = this.project.file(".idea/workspace.xml")
                def projectNode = new XmlSlurper().parse(workspaceXML)
                def doesTaskExist = false
                projectNode.component.find { item ->
                    if (item.@name.equals("ExternalProjectsManager")) {
                        if (item.system.state.task.activation."$phaseName".size() > 0) {
                            item.system.state.task.activation."$phaseName".task.each { taskNode ->
                                if (taskNode.@name.equals(this.name)) {
                                    doesTaskExist = true
                                    return
                                }
                            }
                            if (!doesTaskExist) {
                                item.system.state.task.activation."$phaseName".appendNode {
                                    task(name: this.name)
                                }
                            } else {
                                return
                            }
                        } else {
                            item.system.state.task.activation.appendNode {
                                "$phaseName" {
                                    task(name: this.name)
                                }
                            }
                        }
                        def newRootNode = new StreamingMarkupBuilder().bind { mkp.yield projectNode }.toString()
                        XmlUtil.serialize(newRootNode, new FileOutputStream(workspaceXML))
                    }
                }
            }
        } else {
            //todo 清空
        }

    }
}
