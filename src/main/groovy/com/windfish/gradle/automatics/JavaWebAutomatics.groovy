package com.windfish.gradle.automatics

import com.windfish.gradle.foundation.IntelliJIDEA
import com.windfish.gradle.foundation.IntelliJIDEATaskPhase
import com.windfish.gradle.foundation.extension.JavaWebExtension
import com.windfish.gradle.foundation.file.*
import com.windfish.gradle.foundation.os.OS
import com.windfish.gradle.task.node.*
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.bundling.War

/**
 * Created by kenhuang on 2018/10/30.
 */
class JavaWebAutomatics extends JavaAutomatics {

    JavaWebAutomatics(Project project) {
        super()
        this.project = project
        OS.instance.project = project
        this.extension = project.getExtensions().create(JavaWebExtension.NAME, JavaWebExtension)
    }

    @Override
    void execute() {
        super.execute()
        this.executeFrontendTasks()
        this.customizeWar()
    }

    private void downloadUnzip(String fileName, String ext, String urlString, String unzipPath) {
        ZipFile zipFile = new ZipFile("${this.project.file(this.extension.zipTmpPath)}", fileName)
        zipFile.download(urlString)
        zipFile.unzip(this.project, unzipPath) {
            include "**/${fileName}${ext}"
        }
    }
/******************************************************************************************************************
 * 创建前端任务，安装Node，Npm,Grunt,可以考虑gulp。WebService无用
 ******************************************************************************************************************/
    private void executeFrontendTasks() {
        /*Node*/
        this.project.tasks.create(InstallNodeTask.NAME, InstallNodeTask) {
            it.setExtension(this.extension.node)
            it.setTmpDirPath('build/tmp/node')
        }.execute()
        /*Grunt*/
        this.project.tasks.create(InstallGruntTask.NAME, InstallGruntTask) {
            it.setExtension(this.extension.grunt)
        }.execute()
        this.project.tasks.create(InstallGruntPluginsTask.NAME, InstallGruntPluginsTask){
            it.setExtension(this.extension.grunt)
        }.execute()
        this.project.tasks.create(CompileFrontendTask.NAME, CompileFrontendTask){
            it.isGlobal = this.extension.grunt.isGlobal
        }

        /*生成package.json*/

        NpmInitTask npmInit = this.project.tasks.create(NpmInitTask.NAME, NpmInitTask)
        NodeTask npmInitP = this.project.tasks.create('npmInitP', NodeTask) {
            it.description = 'add package name to package.json'
            if (this.project.hasProperty('war')){
                it.setCommandLine("node_modules/.bin/json", "-I", "-f", 'package.json', '-e',
                        "this.warName=\"${this.project.war.archiveName}\"")
            }else{
                it.setCommandLine("echo","Please apply plugin war for java web application!")
            }
        }
        npmInit.finalizedBy('npmInitP')
        npmInit.execute()
        npmInitP.execute()

        /*将生成Gruntfile.js文件，并将其中的js，css路径替换为设定的路径*/
        this.project.tasks.create('generateGruntfile', NodeTask) {
            File gruntFile = this.project.file("Gruntfile.js")
            InputStream inputStream = FileUtil.classLoader.getResourceAsStream("frontend/Gruntfile.js")
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
            String line
            StringBuilder builder = new StringBuilder()
            while ((line = reader.readLine()) != null) {
                builder.append(line).append(System.getProperty("line.separator"))
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(gruntFile))
            writer.write(builder.toString().replaceAll("src/main/webapp/css", ((JavaWebExtension) this.extension).cssPath)
                    .replaceAll("build/libs/exploded/<%= pkg.warName %>/css",
                    "build/libs/exploded/<%= pkg.warName %>/" + ((JavaWebExtension) this.extension).cssPathInWar)
                    .replaceAll("src/main/webapp/js", ((JavaWebExtension) this.extension).jsPath)
                    .replaceAll("build/libs/exploded/<%= pkg.warName %>/js",
                    "build/libs/exploded/<%= pkg.warName %>/" + ((JavaWebExtension) this.extension).jsPathInWar))
            inputStream.close()
            writer.close()
            it.setCommandLine("echo","Gruntfile is generated.")
        }.execute()
    }
/******************************************************************************************************************
 *  添加项目js css依赖
 ******************************************************************************************************************/
    @Override
    protected void addDependencies() {
        super.addDependencies()
        String key, value
        /*js*/
        if (this.extension.jsDependencies != null) {
            this.project.file(this.extension.jsPath).mkdirs()
            this.extension.jsDependencies.each { jsDependency ->
                key = jsDependency.getKey()
                value = jsDependency.getValue()
                if (key.endsWith(HtcFile.FULL_EXT) || value.endsWith(HtcFile.FULL_EXT)) {
                    HtcFile htcFile
                    /*下载结果是zip包的情况下只能在key加.htc加以区分*/
                    if (key.endsWith(HtcFile.FULL_EXT)) {
                        key = key.replace(HtcFile.FULL_EXT, "")
                    }
                    if (value.endsWith("/")) {
                        this.downloadUnzip(key, HtcFile.FULL_EXT, value, this.extension.jsPath)
                    } else {
                        new HtcFile("${this.project.file(this.extension.jsPath)}", key).download(value)
                    }
                } else if (value.endsWith(ZipFile.FULL_EXT) || value.endsWith("/")) {
                    /*zip包*/
                    this.downloadUnzip(key, JsFile.FULL_EXT, value, this.extension.jsPath)
                } else {
                    /*直接下载*/
                    new JsFile("${this.project.file(this.extension.jsPath)}", key).download(value)
                }
            }
        }
        /*css*/
        if (this.extension.cssDependencies != null) {
            this.project.file(this.extension.cssPath).mkdirs()
            this.extension.cssDependencies.each { cssDependency ->
                key = cssDependency.getKey()
                value = cssDependency.getValue()
                if (value.endsWith(ZipFile.FULL_EXT) || value.endsWith("/")) {
                    this.downloadUnzip(key, CssFile.FULL_EXT, value, this.extension.cssPath)
                } else {
                    /*直接下载*/
                    new CssFile("${this.project.file(this.extension.cssPath)}", key).download(value)
                }
            }
        }
    }
/******************************************************************************************************************
 * 定制war
 ******************************************************************************************************************/
    private void customizeWar() {
        String webXmlPath = "web${XmlFile.FULL_EXT}"
        File webXml
        if (this.extension.webXmlPath.endsWith(webXmlPath)) {
            webXmlPath = this.extension.webXmlPath
        } else {
            webXmlPath = "${this.extension.webXmlPath}/web.xml"
        }
        webXml = this.project.file(webXmlPath)
        if (webXml.exists()) {
            this.project.tasks.each { task ->
                if (task instanceof War) {
                    task.setWebXml(webXml)
                }
            }
        } else {
            throw new GradleException("${webXmlPath} doesn't exist!")
        }
        List<IntelliJIDEATaskPhase>phases = new LinkedList<>()
        phases.push(IntelliJIDEATaskPhase.BEFORE_COMPILE)
        /*排除原始css和js，并在打包任务开始前先执行前端编译任务*/
        this.project.tasks.each { task ->
            if (task instanceof War) {
                task.rootSpec.exclude("**/${((JavaWebExtension) this.extension).cssPath}/**")
                task.rootSpec.exclude("**/${((JavaWebExtension) this.extension).jsPath}/**")
                task.dependsOn CompileFrontendTask.NAME

                IntelliJIDEA.setPhases(CompileFrontendTask.NAME,this.project,phases)
            }
        }
        /*将hbm.xml的内容复制到war中*/
        this.project.tasks.create('copyHbmXml',Copy){
            from "src/main/java/${this.extension.hbmXmlPackageName}"
            into  "${this.project.sourceSets.main.output.classesDir}/${this.extension.hbmXmlPackageName}"
            include '**/*.hbm.xml'
        }
        IntelliJIDEA.setPhases('copyHbmXml',this.project,phases)
    }
}


