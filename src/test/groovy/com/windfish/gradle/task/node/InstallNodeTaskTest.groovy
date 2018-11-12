package com.windfish.gradle.task.node

import com.windfish.gradle.AbstractProjectTest
import com.windfish.gradle.foundation.extension.NodeExtension
import org.gradle.api.tasks.Exec

public class InstallNodeTaskTest extends AbstractProjectTest {

    def InstallNodeTask() {
        when:
        OutputStream output = new ByteArrayOutputStream()
//        this.project.exec {
////            commandLine 'which','/usr/bin/local/node'
////            standardOutput = output
//
//        }
        this.project.tasks.create('a', Exec) {
//            executable 'brew'
            commandLine 'brew', 'install', ''
//          commandLine "sudo","installer","-package"," /Users/kenhuang/Downloads/node-v10.13.0.pkg ","-target","\"/\""
            standardOutput = output
//            ignoreExitValue true
        }.execute()
//        |wc -l
        then:
        println output.toString()
    }

    def installLinuxNode(File tmpdir, String urlString) {

    }

    def isNodeInstalledInUnix() {

    }

    def isNodeInstalledInWindows() {

    }

    def setExtension(NodeExtension extension, File tmpdir) {

    }

}