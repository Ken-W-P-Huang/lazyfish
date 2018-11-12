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

This plugin could be used in MacOS and Linux. It's not tested in Windows.
The plugin must be applied after plugin 'java','war',etc as it will use the plugins which have been applied to the project to check which type the project is(java,javaweb,android).



Build script snippet for plugins DSL for Gradle 2.1 and later:

plugins {
  id 'java'
  id "com.windfish.gradle.lazyfish" version "1.0"
}

Build script snippet for use in older Gradle versions or where dynamic configuration is required:
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "com.windfish.gradle.lazyfish:lazyfish-gradle-plugin:1.0"
  }
}

apply plugin: "com.windfish.gradle.lazyfish"


# License

Apache-2.0


