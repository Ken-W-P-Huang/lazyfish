package com.windfish.gradle.foundation.extension

/**
 * Created by kenhuang on 2018/10/27.
 */
class JavaExtension {
    public final static String NAME = 'java'
    Map<String, String> dependencies =[:]
    Map<String, String> testDependencies=[:]
    Map<String, String> zipDependencies=[:]
    String license = ''
    String vcsType = 'git'
    Map<String, String> vcsRemotes =[:]
    boolean addReadme = false
    Map<String, List<String>> directories=[:]
    String zipTmpPath = "build/tmp/zip"
    String jarLibPath = "build/libs"
    /*映射main的内容到test并生成测试文件*/
    boolean mirrorToTest = false
    String testFramework = 'junit'
    Closure mirrorFilter = { include "**" }
    public Map<String, List<String>> ignoredTestMethodsMap = [
            "src/main/java/repository/entity":['set','get','equals','hashCode'],
             "default":['set','get'],
            ]

}

