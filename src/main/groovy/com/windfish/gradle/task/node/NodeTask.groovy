package com.windfish.gradle.task.node

import com.windfish.gradle.foundation.os.OS
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created by kenhuang on 2018/11/8.
 */
class NodeTask extends DefaultTask{
    public static final String GROUP = "node"
    private List<String> arguments = []

    NodeTask() {
        super()
        this.group = GROUP
    }

    @TaskAction
    void doExecute(){
        List<String> args = this.arguments
        OS.getInstance().executeCommand {
            it.commandLine args
        }
    }

    public void  setCommandLine(String... arguments){
        this.arguments = arguments
    }

}
