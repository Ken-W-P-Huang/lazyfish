package com.windfish.gradle.task.node

import com.windfish.gradle.foundation.Platform
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.TaskAction

/**
 * Created by kenhuang on 2018/11/8.
 */
class NodeTask extends Exec{
    public static final String GROUP = "node"
    NodeTask() {
        super()
        this.group = GROUP
        this.environment('PATH', "${this.environment.PATH}:${this.project.file('node/bin').absolutePath}")
        if (Platform.getInstance().os == 'windows'){

        }else{
            this.environment('PATH', "${this.environment.PATH}:/usr/local/bin")
        }
    }

    public void executeCommand(Closure closure){
        this.project.exec {
            environment 'PATH', "${this.environment.PATH}:${this.project.file('node/bin').absolutePath}"
            if (Platform.getInstance().os == 'windows'){

            }else{
                this.environment('PATH', "${this.environment.PATH}:/usr/local/bin")
            }
            closure(it)
        }
    }

    protected String whichCommand(String command){
        ByteArrayOutputStream output = new ByteArrayOutputStream()
        this.executeCommand {
            it.commandLine "which", command
            it.standardOutput = output
        }
        return output.toString().trim()
    }

}
