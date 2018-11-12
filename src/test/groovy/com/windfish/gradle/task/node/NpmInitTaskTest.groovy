package com.windfish.gradle.task.node

import com.windfish.gradle.AbstractProjectTest
import org.gradle.api.tasks.Exec
import spock.lang.Specification

public class NpmInitTaskTest extends AbstractProjectTest {

    def NpmTask() {

    }
    def testc(){
        when:
//        NpmInitTask a= this.project.tasks.create('a',NpmInitTask)
//        println this.project.projectDir
//        a.execute()

//        Exec a= this.project.tasks.create('a',Exec){
//            environment('PATH', "${environment.PATH}:/usr/local/bin")
//            commandLine '/usr/local/bin/npm','init','-y'
//        }
//        a.environment('PATH',"${a.environment.PATH}:/usr/local/bin")
//        a.execute()
        NodeTask task = this.project.tasks.create('b',NodeTask)
        String a =  task.whichCommand('npm')
        println a.trim()
        this.project.exec{
            environment 'PATH', "${environment.PATH}:/usr/local/bin"
            commandLine a.trim(), "init","-y"
        }

        then:
        println "hello"
    }
}