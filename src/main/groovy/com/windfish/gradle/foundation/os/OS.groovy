package com.windfish.gradle.foundation.os

import org.gradle.api.Project

/**
 * Created by kenhuang on 2018/11/13.
 */



abstract class OS implements IOSFunction {
    private static OS instance
    private Project project
    private static final Properties properties = System.getProperties()
    public String os
    public String arch
    protected String binPath
    public static OS getInstance(){
        if (instance == null) {
            synchronized (OS.class) {
                if (instance == null) {
                    String tempString = properties.getProperty("os.name").toLowerCase()
                    if (tempString.contains("windows")) {
                        instance = new Windows()
                        instance.os = "windows"
                    } else if (tempString.contains("mac")) {
                        instance = new MacOS()
                        instance.os = "macos"
                    } else if (tempString.contains("freebsd")) {
                        instance.os = "freebsd"
                    } else if (tempString.contains("sunos")) {
                        instance.os = "sunos"
                    } else if (tempString.contains("linux")){
                        tempString = properties.getProperty("os.version").toLowerCase()
                        if (tempString.contains("ubuntu")|| tempString.contains("deepin")) {
                            instance = new Ubuntu()
                            instance.os = "ubuntu"
                        } else if (tempString.contains("centos") || tempString.contains("redhat")) {
                            instance = new Redhat()
                            instance.os = "centos"
                        }
                    } else {
                        throw new IllegalArgumentException("Unsupported OS:${tempString}")
                    }
                    tempString = properties.getProperty("os.arch").toLowerCase()
                    if (tempString.contains("64")) {
                        instance.arch = "x64"
                    } else if (tempString.equals("arm")) {
                        tempString = 'uname -m'.execute().text.trim()
                        if (tempString.equals("armv8l")) {
                            instance.arch = "arm64"
                        } else {
                            instance.arch = tempString
                        }
                    } else {
                        instance.arch = "x86"
                    }
                }
            }
        }
        return instance
    }

    abstract protected void installNodeByStep(String nodeVersion,String tmpDirPath, String urlString)

    public void installNode(String nodeVersion,String tmpDirPath, String urlString){
        if (this.isNodeInstalled()) {
            println 'Node has been already installed.'
        }else{
            installNodeByStep(nodeVersion,tmpDirPath,urlString)
            println 'Node is installed.'
        }
    }


    public void executeCommand(Closure closure){
        this.project.exec {
            it.environment('PATH', "${it.environment.PATH}${this.binPath}")
            closure(it)
        }
    }

    Project getProject() {
        return project
    }

    void setProject(Project project) {
        this.project = project
    }
}
