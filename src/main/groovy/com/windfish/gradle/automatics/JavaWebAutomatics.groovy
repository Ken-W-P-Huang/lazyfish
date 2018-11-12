package com.windfish.gradle.automatics

import com.windfish.gradle.foundation.extension.JavaWebExtension
import com.windfish.gradle.foundation.file.*
import com.windfish.gradle.task.node.CompileFrontendTask
import com.windfish.gradle.task.node.InstallGruntPluginsTask
import com.windfish.gradle.task.node.InstallGruntTask
import com.windfish.gradle.task.node.InstallNodeTask
import com.windfish.gradle.task.node.NodeTask
import com.windfish.gradle.task.node.NpmInitTask
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.bundling.War
/**
 * Created by kenhuang on 2018/10/30.
 */
class JavaWebAutomatics extends JavaAutomatics {

    JavaWebAutomatics(Project project) {
        super()
        this.project = project
        this.extension = project.getExtensions().create(JavaWebExtension.NAME, JavaWebExtension)
    }

    @Override
    void execute() {
        super.execute()
        this.executeFrontendTasks()
//        this.customizeWar()
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
        this.project.tasks.create(InstallNodeTask.NAME, InstallNodeTask) { task ->
            task.setExtension(this.extension.node, this.extension.node.tmpDirPath)
        }.execute()
        NpmInitTask npmInit = this.project.tasks.create(NpmInitTask.NAME, NpmInitTask)
        NodeTask npmInitP = this.project.tasks.create('npmInitP', NodeTask) { task ->
            task.description = 'add package name to package.json'
            commandLine "node_modules/.bin/json", "-I", "-f", 'package.json', '-e',
                    'this.warName=\"' + this.project.war.archiveName + '\"'
        }
        npmInit.finalizedBy('npmInitP')
        npmInit.execute()
        npmInitP.execute()

        /*Grunt*/
        this.project.tasks.create(InstallGruntTask.NAME, InstallGruntTask) { task ->
            task.setExtension(this.extension.grunt)
        }.execute()
        this.project.tasks.create(InstallGruntPluginsTask.NAME, InstallGruntPluginsTask){ task ->
            task.setExtension(this.extension.grunt)
        }.execute()
        this.project.tasks.create(CompileFrontendTask.NAME, CompileFrontendTask)

        /*将生成Gruntfile.js文件，并将其中的js，css路径替换为设定的路径*/
        this.project.tasks.create('generateGruntfile', DefaultTask) { task ->
            task.group = NodeTask.GROUP
            File gruntFile = this.project.file("Gruntfile.js")
            InputStream inputStream = FileUtil.classLoader.getResourceAsStream("frontend/Gruntfile.js")
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
            String line
            StringBuilder builder = new StringBuilder()
            while ((line = reader.readLine()) != null) {
                builder.append(line).append(System.getProperty("line.separator"))
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(gruntFile))
            writer.write(builder.toString().replaceAll("src/main/webapp/css/", ((JavaWebExtension) this.extension).cssPath)
                    .replaceAll("build/libs/exploded/<%= pkg.warName %>/css",
                    "build/libs/exploded/<%= pkg.warName %>/" + ((JavaWebExtension) this.extension).cssPathInWar)
                    .replaceAll("src/main/webapp/js/", ((JavaWebExtension) this.extension).jsPath)
                    .replaceAll("build/libs/exploded/<%= pkg.warName %>/js",
                    "build/libs/exploded/<%= pkg.warName %>/" + ((JavaWebExtension) this.extension).jsPathInWar))
            inputStream.close()
            writer.close()
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
        /*排除原始css和js，并在打包任务开始前先执行前端编译任务*/
        this.project.tasks.each { task ->
            if (task instanceof War) {
                task.rootSpec.exclude("**/${((JavaWebExtension) this.extension).cssPath}/**")
                task.rootSpec.exclude("**/${((JavaWebExtension) this.extension).jsPath}/**")
                task.dependsOn CompileFrontendTask.NAME
            }
        }
    }
}


