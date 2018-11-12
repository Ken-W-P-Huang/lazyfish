package com.windfish.gradle.task.node

import com.windfish.gradle.AbstractProjectTest
import com.windfish.gradle.foundation.extension.NodeExtension
import spock.lang.Specification

public class NodeTaskTest extends AbstractProjectTest {

    def NodeTask() {

    }

    def setConfig(NodeExtension config) {

    }

    def enableGrunt() {

    }
    def whichCommand(){
        when:
        com.windfish.gradle.task.node.NodeTask  nodeTask = this.project.tasks.create('a',com.windfish.gradle.task.node.NodeTask)

        then:
        println nodeTask.whichCommand('which')
    }
}