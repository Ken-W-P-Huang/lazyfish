package com.windfish.gradle.foundation.file

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.FileTree

import java.nio.channels.FileChannel

/**
 * Created by kenhuang on 2018/10/29.
 */
class FileUtil {
    public static void copy(File from, File to) {
        if (from.exists()) {
            if (from.isFile()) {
                if (to.exists()) {
                    if (to.isDirectory()) {
                        /*存在的目录，复制为其子文件*/
                        to = new File("${to.absolutePath}/${from.name}")
                    }
                } else {
                    /*默认上一级是目录*/
                    File dir = new File(to.absolutePath.substring(0, to.absolutePath.lastIndexOf('/')))
                    dir.mkdirs()
                }
                copyFile(from, to)
            } else {
                if (to.exists()) {
                    if (to.isFile()) {
                        /*存在并且是文件，不能复制*/
                        throw new GradleException("Directory ${from.absolutePath} could not be copied into " +
                                "${to.absolutePath} which is file type ")
                    } else {
                        to = new File("${to.absolutePath}/${from.name}")
                    }
                }
                to.mkdirs()
                List<File> stack = new LinkedList<>()
                File source, destination
                stack.push(from)
                while (stack.size() > 0) {
                    source = stack.pop()
                    destination = new File(
                            "${to.absolutePath}/${relativeTo(source.absolutePath, from.absolutePath)}")
                    if (source.isDirectory()) {
                        destination.mkdir()
                        source.listFiles().each {
                            stack.push(it)
                        }
                    } else {
                        copyFile(source, destination)
                    }
                }
            }
        } else {
            throw new GradleException("${from.absolutePath} does not exist! ")
        }
    }

    public static void copyResource(String resourcePath, File destination) {
        InputStream inputStream = FileUtil.classLoader.getResourceAsStream(resourcePath)
        OutputStream outputStream = new FileOutputStream(destination)
        byte[] bytes = new byte[1024]
        while (inputStream.read(bytes) != -1) {
            outputStream.write(bytes)
        }
        outputStream.close()
        inputStream.close()
    }

    private static void copyFile(File source, File destination) {
        FileChannel inputChannel = new FileInputStream(source).getChannel()
        FileChannel outputChannel = new FileOutputStream(destination).getChannel()
        outputChannel.transferFrom(inputChannel, 0, inputChannel.size())
        inputChannel.close()
        outputChannel.close()
    }
    /*不使用sourceSets实现，有点复杂*/

    public static void mkSubDirs(Project project, Map<String, List<String>> dirsInfo) {
        File dir
        dirsInfo.keySet().each { relativePath ->
            List<String> subDirs = dirsInfo.get(relativePath)
            subDirs.each { subDir ->
                dir = project.file("${relativePath}/${subDir}")
                if (!dir.exists()) {
                    dir.mkdirs()
                }
            }

        }
    }

    public static String relativeTo(String path, String relativePath) {
        if (path.startsWith(relativePath)) {
            return path.substring(relativePath.length())
        }
        return null
    }
    /**
     * 将源目录的内容按照test的标准映射到目标目录中
     */
    public static void mirrorSourceToTest(File sourceDir, File testDir, FileTree targetFiles, String testFramework) {
        String relativePath
        def fileFromSource, fileFromTest
        List<File> stack = new LinkedList<>()
        stack.push(sourceDir)
        while (stack.size() > 0) {
            fileFromSource = stack.pop()
            if (!fileFromSource.isHidden()) {
                relativePath = relativeTo(fileFromSource.absolutePath, sourceDir.absolutePath)
                if (fileFromSource.isDirectory()) {
                    fileFromSource.listFiles().each { file ->
                        stack.push(file)
                    }
                    if (relativePath != null && relativePath != "") {
                        fileFromTest = new File("${testDir.absolutePath}${relativePath}")
                        !fileFromTest.exists() && fileFromTest.mkdirs()
                    }
                } else {
                    if (targetFiles.contains(fileFromSource)) {
                        switch (getExtName(fileFromSource)) {
                            case JavaFile.EXT:
                                new JavaFile(fileFromSource.absolutePath).mirrorTestFile(sourceDir, testDir, testFramework)
                                break
                            case GroovyFile.EXT:
                                new GroovyFile(fileFromSource.absolutePath).mirrorTestFile(sourceDir, testDir, testFramework)
                                break
                            default:
                                break
                        }
                    }
                }
            }

        }
    }

    public static String getExtName(File file) {
        String extName = file.getName()
        if ((extName != null) && (extName.length() > 0)) {
            int dotPosition = extName.lastIndexOf('.');
            if ((dotPosition > -1) && (dotPosition < (extName.length() - 1))) {
                return extName.substring(dotPosition + 1)
            }
        }
        return extName
    }
}
