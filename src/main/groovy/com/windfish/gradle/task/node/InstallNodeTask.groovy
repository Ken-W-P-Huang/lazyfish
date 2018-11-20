package com.windfish.gradle.task.node

import com.windfish.gradle.foundation.extension.NodeExtension
import com.windfish.gradle.foundation.os.OS

/**
 * Created by kenhuang on 2018/10/27.
 */
class InstallNodeTask extends NodeTask {
    public final static String NAME = "installNode"
    private NodeExtension extension
    private String tmpDirPath
    InstallNodeTask() {
        super()
        this.description = 'Download and install Node server.'
    }


    void setExtension(NodeExtension extension) {
        this.extension = extension
    }

    void setTmpDirPath(String tmpDirPath) {
        this.tmpDirPath = tmpDirPath
    }
    @Override
    void doExecute(){
        String urlString = "${this.extension.distUrl}/v${this.extension.version}"
        File tmpDir = this.project.file(tmpDirPath)
        tmpDir.mkdirs()
        OS.getInstance().installNode(this.extension.version,tmpDirPath,urlString)
    }
}
