package com.windfish.gradle.foundation.os
/**
 * Created by kenhuang on 2018/11/13.
 */
class Redhat extends Linux {
    Redhat() {
        super()
    }

    @Override
    void installNodeWithCommand(String password) {
        this.executeCommand {
            it.standardInput = new ByteArrayInputStream(password.bytes)
            it.commandLine 'sudo', "-S", 'yum', 'install', '-y', 'nodejs', 'npm'
        }
    }

}
