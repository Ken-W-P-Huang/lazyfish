package com.windfish.gradle.task.node

import com.windfish.gradle.foundation.os.OS

/**
 * Created by kenhuang on 2018/11/9.
 */
class NpmTask extends  NodeTask{
    NpmTask(){
        super()
        this.dependsOn(InstallNodeTask.NAME)
    }


    /**
     * npm list 的运行速度太慢，改为只检查特定应用的文件夹是否存在。如果需要安装指定版本的插件，则将原来的插件删除。
     * @param moduleName
     * @param version
     * @return
     */
    public static   boolean isModuleInstalled( String moduleName, String version,File nodeModulesDir) {
//        ByteArrayOutputStream output = new ByteArrayOutputStream()
//        try {
//            if (version == null) {
//                this.executeCommand {
//                    it.commandLine this.which("npm"), "list", "${moduleName}"
//                    it.standardOutput = output
//                }
//            } else {
//                this.executeCommand {
//                    it.commandLine this.which("npm"), "list", "${moduleName}@${version}"
//                    it.standardOutput = output
//                }
//            }
//        } catch (Exception e) {
//            output = new ByteArrayOutputStream()
//        }
//        ByteArrayInputStream input = new ByteArrayInputStream(output.buf)
//        output = new ByteArrayOutputStream()
//        this.project.exec {
//            standardInput = input
//            standardOutput = output
//            commandLine "wc", "-l"
//        }
//        return (Integer.parseInt(output.toString().trim()) >= 1)
         return new File("${nodeModulesDir.absolutePath}/${moduleName}").exists()
    }

    protected void installModule(String moduleName,String moduleVersion, File nodeModulesDir){
        if (isModuleInstalled(moduleName, moduleVersion,nodeModulesDir)){
            println "${moduleName} has been installed."
        }else{
            String version = ''
            if (moduleVersion != null && moduleVersion != ''){
                version = "@${moduleVersion}"
            }
            OS.instance.executeCommand {
                it.commandLine OS.instance.which('npm'), 'install',"${moduleName}${version}"
            }
            println "${moduleName} is installed."
        }
    }
}
