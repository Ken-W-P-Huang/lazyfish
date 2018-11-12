package com.windfish.gradle.foundation

/**
 * Created by kenhuang on 2018/10/27.
 */
class Platform {
    private static Platform instance
    public String os
    public String arch
    private final Properties properties = System.getProperties()

    public static Platform getInstance() {
        if (instance == null) {
            synchronized (Platform.class) {
                if (instance == null) {
                    instance = new Platform()
                }
            }
        }
        return instance
    }

    private Platform() {
        String tempString = this.properties.getProperty("os.name").toLowerCase()
        if (tempString.contains("windows")) {
            this.os = "windows"
        } else if (tempString.contains("mac")) {
            this.os = "macos"
        } else if (tempString.contains("linux")) {
            this.os = "linux"
        } else if (tempString.contains("freebsd")) {
            this.os = "unix"
        } else if (tempString.contains("sunos")) {
            this.os = "sunos"
        } else {
            throw new IllegalArgumentException("Unsupported OS: " + tempString)
        }
        tempString = this.properties.getProperty("os.arch").toLowerCase()
        if (tempString.contains("64")) {
            this.arch = "x64"
        } else if (tempString.equals("arm")) {
            tempString = 'uname -m'.execute().text.trim()
            if (tempString.equals("armv8l")) {
                this.arch = "arm64"
            } else {
                this.arch = tempString
            }
        } else {
            this.arch = "x86"
        }
    }
}
