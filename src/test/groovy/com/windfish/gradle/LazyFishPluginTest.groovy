package com.windfish.gradle

import com.windfish.gradle.task.node.NodeTask
import org.gradle.api.tasks.Exec

/**
 * Created by kenhuang on 2018/10/30.
 */
class LazyFishPluginTest extends AbstractProjectTest {


    void cleanup() {

    }

    def 'test'() {
        when:
//        task stopTomcat(type:Exec) {
//            workingDir '../tomcat/bin'
//
//            //on windows:
//            commandLine 'cmd', '/c', 'stop.bat'
//
//            //on linux
//            commandLine './stop.sh'
//
//            //store the output instead of printing to the console:
//            standardOutput = new ByteArrayOutputStream()
//
//            //extension method stopTomcat.output() can be used to obtain the output:
//            ext.output = {
//                return standardOutput.toString()
//            }
//        }

        def task = this.project.tasks.create('a', Exec) {


        }
        task.setCommandLine('echo', 'hello;', 'echo', ' ken!')
        File a = new File(this.project.file('src'), 'ab')
        println a.absolutePath
        then:
        task.execute()
    }

    def testWhich(){
        when:
        this.project.exec {
            commandLine 'env'
        }
        this.project.exec {
//            commandLine 'declare','PATH=\$PATH:/usr/local/bin'
//            ,"export PATH=\$PATH:/usr/local/bin"
//            String path =  this.environment.get('PATH')
//
            environment 'PATH', "${environment.PATH}:/usr/local/bin"
            commandLine 'which','node'
        }
//        this.environment('PATH',"$path:/usr/local/bin")
////        this.project.exec {
////            commandLine 'declare','PATH','=',"\$PATH:/usr/local/bin"
////        }
//        println this.environment
//        this.project.exec {
//            commandLine 'env'
//        }
        ByteArrayOutputStream output = new ByteArrayOutputStream()
        try {
            this.project.exec {
                environment 'PATH', "${environment.PATH}"
                commandLine "which", "node"
                standardOutput = output
            }
        } catch (Exception e) {
            println output
            println e
            output = new ByteArrayOutputStream()
        }

        ByteArrayInputStream input = new ByteArrayInputStream(output.buf)
        output = new ByteArrayOutputStream()
        this.project.exec {
            standardInput = input
            standardOutput = output
            commandLine "wc", "-l"
        }
        println (Integer.parseInt(output.toString().trim()) >= 1)
        then:
        println ""
    }
    def testb(){
        when:
         Exec b= this.project.tasks.create('b',Exec){
             commandLine "which", "node"
         }
         b.environment('PATH',"${b.environment.PATH}:/usr/local/bin")
         b.execute()
        then:
         println ""
    }

    def testa(){
        when:
        this.project.exec{
            environment 'PATH', "${environment.PATH}:/usr/local/bin"
            commandLine "which", "node"
        }
        then:
        println ""
    }
    def testc(){
        when:
        NodeTask a= this.project.tasks.create('a',NodeTask){
            commandLine "which", "which"
        }
        a.environment('PATH',"${a.environment.PATH}:/usr/local/bin")
        a.execute()
        then:
        println ""
    }
    def testd(){
        when:
        this.project.exec{
            environment 'PATH', "${environment.PATH}:/usr/local/bin"
            commandLine "npm", "list"
        }
        then:
        println ""
    }
}
class A extends Exec{


}

