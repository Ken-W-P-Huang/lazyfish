package com.windfish.gradle.foundation.file

import com.windfish.gradle.AbstractProjectTest

/**
 * Created by kenhuang on 2018/10/27.
 */
class ZipFileTest extends AbstractProjectTest {
    def 'unzip'() {
        given:
        ZipFile file = new ZipFile(this.project.file(".").absolutePath, 'd3')
        when:
        println file.path
        file.download("https://github.com/d3/d3/releases/download/v5.7.0/d3.zip")
        file.unzip(this.project, this.project.file(".")) {
            include "**/d3.js"
        }
        then:
        this.project.file("./d3.js").exists()
    }

    def 'compile'() {
        given:
        this.project.plugins.apply('java')
        this.project.repositories.configure {
            mavenCentral()
        }
        ZipFile file = new ZipFile(this.project.file(".").absolutePath, 'wxpay')
        JarFile jarFile = new JarFile(this.project.file(".").absolutePath, 'wxpay-sdk-3.0.9')
        when:
        println file.path
        file.download("https://pay.weixin.qq.com/wiki/doc/api/download/WxPayAPI_JAVA.zip")
        file.compile(this.project, jarFile)
        then:
        jarFile.exists()
    }
}



