# 

# Getting Started

This is a gradle plugin which could be used to facilitate BC/S web application development.

1. Generate project folders automatically.
2. Download and compile third party source code and add the generated jar file as project dependency.
3. Init git repository and add git remote to it. In theory, other CVS could be supported but the function is not implemented
4. Generate README.txt and LICENSE.
5. Download required js and css based on the configuration
6. Install node,npm and grunt. Integrate grunt into the project tasks to generate prod js and css. 
7. Java,Javaweb,Android or Hadoop configuration could be mixed together in build.gradle.
8. Automatically generate test files in "src/test" directory base on the methods of each source files.

# How to use

Currently, this plugin could be used in MacOS and Linux. It's not tested in Windows.
The plugin must be applied after plugin 'java','war',etc as it will use the plugins which have been applied to the project to check which type the project is(java,javaweb,android).

Build script snippet for plugins DSL for Gradle 2.1 and later:
plugins {    
&nbsp;&nbsp;&nbsp;&nbsp;id 'java'  
&nbsp;&nbsp;&nbsp;&nbsp;id "com.windfish.gradle.lazyfish" version "1.0"  
}

Build script snippet for use in older Gradle versions or where dynamic configuration is required:   
buildscript {  
&nbsp;&nbsp;&nbsp;&nbsp;repositories {  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;maven {  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;url "https://plugins.gradle.org/m2/"  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}  
&nbsp;&nbsp;&nbsp;&nbsp;}  
&nbsp;&nbsp;&nbsp;&nbsp;dependencies {  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;classpath "com.windfish.gradle.lazyfish:lazyfish-gradle-plugin:1.0"  
&nbsp;&nbsp;&nbsp;&nbsp;}  
}  

apply plugin: "com.windfish.gradle.lazyfish"


# License

Apache-2.0

# Example

apply plugin:'war'  
apply plugin:'com.windfish.gradle.lazyfish'  

javaweb {  
&nbsp;&nbsp;&nbsp;&nbsp;dependencies=[  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'ali':'com.alipay.sdk:alipay-sdk-java:3.3.4.ALL',  
&nbsp;&nbsp;&nbsp;&nbsp;]  
&nbsp;&nbsp;&nbsp;&nbsp;testDependencies=[  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'mockito':  'org.mockito:mockito-core:1.+',  
&nbsp;&nbsp;&nbsp;&nbsp;]  
&nbsp;&nbsp;&nbsp;&nbsp;zipDependencies=[  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;wxpay:"https://pay.weixin.qq.com/wiki/doc/api/download/WxPayAPI_JAVA.zip",  
&nbsp;&nbsp;&nbsp;&nbsp;]  
&nbsp;&nbsp;&nbsp;&nbsp;jsDependencies=[  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;vue:"https://cdn.jsdelivr.net/npm/vue/dist/vue.js",   
&nbsp;&nbsp;&nbsp;&nbsp;]  
&nbsp;&nbsp;&nbsp;&nbsp;cssDependencies=[  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"font-awesome":"http://netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css",  
&nbsp;&nbsp;&nbsp;&nbsp;]  
&nbsp;&nbsp;&nbsp;&nbsp;license='Apache-2.0'  
&nbsp;&nbsp;&nbsp;&nbsp;vcsType='git'  
&nbsp;&nbsp;&nbsp;&nbsp;vcsRemotes=[remote:'']  
&nbsp;&nbsp;&nbsp;&nbsp;addReadme=true  
&nbsp;&nbsp;&nbsp;&nbsp;directories=[:]  
&nbsp;&nbsp;&nbsp;&nbsp;mirrorToTest=true  
&nbsp;&nbsp;&nbsp;&nbsp;testFramework='spock'  
&nbsp;&nbsp;&nbsp;&nbsp;mirrorFilter={  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;exclude "**/JarUtils.java"  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;include "**"  
&nbsp;&nbsp;&nbsp;&nbsp;}  
&nbsp;&nbsp;&nbsp;&nbsp;node{  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;version='10.13.0'  
&nbsp;&nbsp;&nbsp;&nbsp;}  
&nbsp;&nbsp;&nbsp;&nbsp;grunt{  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;gruntVersion = '1.0.2'  
&nbsp;&nbsp;&nbsp;&nbsp;}  
}


