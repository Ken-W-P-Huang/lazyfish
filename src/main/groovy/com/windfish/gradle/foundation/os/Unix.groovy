package com.windfish.gradle.foundation.os

import groovy.swing.SwingBuilder

/**
 * Created by kenhuang on 2018/11/13.
 */
abstract class Unix extends OS {
    Unix() {
        super()
        this.binPath = ":/usr/local/bin"
    }

    public String which(String command){
        ByteArrayOutputStream output = new ByteArrayOutputStream()
        this.executeCommand {
            it.commandLine "which", command
            it.standardOutput = output
        }
        return output.toString().trim()
    }

    public boolean isNodeInstalled() {
        ByteArrayOutputStream output = new ByteArrayOutputStream()
        try {
            this.executeCommand {it->
                it.commandLine "which", "node"
                it.standardOutput = output
            }
        } catch (Exception e) {
            output = new ByteArrayOutputStream()
        }
        ByteArrayInputStream input = new ByteArrayInputStream(output.buf)
        output = new ByteArrayOutputStream()
        this.project.exec {
            it.standardInput = input
            it.standardOutput = output
            it.commandLine "wc", "-l"
        }
        return (Integer.parseInt(output.toString().trim()) >= 1)
    }

    public static String getSudoPassword(String message){
        System.setProperty('java.awt.headless', 'false')
        String password=""
        if(System.console() == null) {
            SwingBuilder swingBuilder =   new SwingBuilder().edt {
                dialog(modal: true,
                        title: 'Sudo',
                        alwaysOnTop: true,
                        resizable: false,
                        locationRelativeTo: null,
                        pack: true,
                        show: true
                ) {
                    vbox {
                        label(text: message)
                        input = passwordField()
                        button(defaultButton: true, text: 'OK', actionPerformed: {
                            password = it.input.getText()
                            dispose()
                        })
                    }
                }
            }

        } else {
            password = new String(System.console().readPassword("\nPlease enter sudo passphrase: "))
        }
        return  password+System.lineSeparator()
    }
}
