package com.windfish.gradle.foundation

import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class ExtendedTaskTest extends Specification {
    final TemporaryFolder temporaryFolder = new TemporaryFolder()
//    def 'copy test'(){
//        when:
//        def
//        then:
//    }

    def 'copy function test'() {
        when:
        def sourcedir = new File("/home/kenhuang/Desktop/a")
        def destination = new File("/home/kenhuang/Desktop/b")

//        FileChannel inputChannel = new FileInputStream(sourcedir).getChannel()
//        FileChannel outputChannel = new FileOutputStream(destination).getChannel()
//
//        outputChannel.transferFrom(inputChannel,0,inputChannel.size())
//        inputChannel.close()
//        outputChannel.close()
        then:
        println destination.isDirectory()
        println destination.isFile()
    }

    def 'copy file to directory'() {
        when:
        def sourceFile = new File("/home/kenhuang/Desktop/a/新建文本 1.txt")
        def destinationDir = new File("/tmp/")
        FileUtil.copyTo(sourceFile, destinationDir)
        then:
        new File("/tmp/新建文本 1.txt").exists()


    }

    def 'copy file to file'() {
        when:
        def sourceFile = new File("/home/kenhuang/Desktop/a/新建文本 1.txt")
        def destinationFile = new File("/tmp/新建文本 1.txt")
        FileUtil.copyTo(sourceFile, destinationFile)
        then:
        new File("/tmp/新建文本 1.txt").exists()


    }

    def 'copy diretory to diretory'() {
        when:
        def sourceFile = new File("/home/kenhuang/Desktop/a/")
        def destinationFile = new File("/tmp/")
        FileUtil.copyTo(sourceFile, destinationFile)
        then:
        new File("/tmp/a").exists() && new File("/tmp/a").isDirectory()
    }

    def 'copy diretory to sdiretory'() {
        when:
        def sourceFile = new File("/home/kenhuang/Desktop/a/")
        def destinationFile = new File("/tmp/tmp")
        FileUtil.copyTo(sourceFile, destinationFile)
        then:
        new File("/tmp/tmp").exists() && new File("/tmp/tmp").isDirectory()
    }
}
