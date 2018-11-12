package com.windfish.gradle.foundation.file

import com.windfish.gradle.AbstractProjectTest

/**
 * Created by kenhuang on 2018/11/1.
 */
class FileUtilTest extends AbstractProjectTest {
    def "Copy"() {

    }

    def "CopyResource"() {

    }

    def "MkSubDirs"() {

    }

    def "RelativeTo"() {

    }

    def "MirrorSourceToTestExcluded"() {
        given:
        this.project.copy {
            from '/Volumes/Work/lazyfish/src'
            into 'src'
        }
        this.project.delete('src/test')
        String testFramework = 'spock'
        String filePath = '', verifyContent = null
        switch (testFramework) {
            case 'junit':
                filePath = 'src/test/groovy/com/windfish/gradle/foundation/file/ZipFileTest.java'
                verifyContent = 'import org.junit.Test;'
                break
            case 'spock':
                filePath = 'src/test/groovy/com/windfish/gradle/foundation/file/ZipFileTest.groovy'
                verifyContent = 'import spock.lang.Specification'
                break
        }
        when:
        FileUtil.mirrorSourceToTest(this.project.file('src/main'), this.project.file('src/test'),
                this.project.fileTree(this.project.file('src/main')) {
                    exclude '**/ZipFile.groovy'
                    include '**'
                }, testFramework)
        then:
        println this.project.file(filePath)
        !this.project.file(filePath).exists()
    }

    def "MirrorSourceToTestIncluded"() {
        given:
        this.project.copy {
            from '/Volumes/Work/lazyfish/src'
            into 'src'
        }
        this.project.delete('src/test')
        String testFramework = 'spock'
        String filePath = '', verifyContent = null
        switch (testFramework) {
            case 'junit':
                filePath = 'src/test/groovy/com/windfish/gradle/foundation/file/ZipFileTest.java'
                verifyContent = 'import org.junit.Test;'
                break
            case 'spock':
                filePath = 'src/test/groovy/com/windfish/gradle/foundation/file/ZipFileTest.groovy'
                verifyContent = 'import spock.lang.Specification'
                break
        }
        when:
        FileUtil.mirrorSourceToTest(this.project.file('src/main'), this.project.file('src/test'),
                this.project.fileTree(this.project.file('src/main')) {
                    include '**'
                }, testFramework)
        then:
        println this.project.file(filePath)
        this.project.file(filePath).exists()
        String text = this.project.file(filePath).text
        text.contains(verifyContent)
    }

    def "GetExtName"() {

    }
}
