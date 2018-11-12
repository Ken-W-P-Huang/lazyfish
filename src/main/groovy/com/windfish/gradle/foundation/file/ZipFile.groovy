package com.windfish.gradle.foundation.file

import com.windfish.gradle.LazyFishPlugin
import com.windfish.gradle.foundation.MavenPomResolver
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.FileTree
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile

/**
 * Created by kenhuang on 2018/10/27.
 */
class ZipFile extends AbstractFile {
    public final static String EXT = 'zip'
    public final static String FULL_EXT = '.zip'

    ZipFile(String path, String name) {
        super("${path}/${name}${FULL_EXT}")

    }

    ZipFile(File parent, String name) {
        super(parent, "${name}${FULL_EXT}")
    }

    @Override
    public boolean isValid() {
        return super.isValid() && this.name.endsWith(EXT)
    }

    @Override
    public String getShortName() {
        return this.name.replace("${FULL_EXT}", "")
    }

    public void unzip(Project project, String unzipDirPathString, Closure filter) {
        if (!this.isValid()) {
            throw new GradleException("Zip file\" ${this.absolutePath}\" is invalid!")
        }
        File unzipDir = project.file(unzipDirPathString)
        if (!unzipDir.exists()) {
            unzipDir.mkdirs()
        }
        int length
        project.zipTree(this).matching(filter).each { item ->
            InputStream inputStream = new FileInputStream(item)
            OutputStream outputStream = new FileOutputStream(new File("${unzipDir.absolutePath}/${item.name}"))
            byte[] bytes = new byte[1024]
            while (true) {
                length = inputStream.read(bytes)
                if (length == -1) {
                    break
                }
                outputStream.write(bytes, 0, length)
            }
        }
    }

    public static String toUpperCaseAtFirst(String s) {
        char[] charArray = s.toCharArray();
        char A = 'A'
        if (charArray[0] >= A && charArray[0] <= 'Z') {
            charArray[0] -= 32;
        }
        return String.valueOf(charArray);
    }

    public void compile(Project project, JarFile jarFile) {
        if (!this.isValid()) {
            throw new GradleException("Zip file\" ${this.absolutePath}\" is invalid!")
        }
        project.file("build/tmp/class/").mkdirs()
        def module = toUpperCaseAtFirst(this.getName().replace("${FULL_EXT}", ""))
        /*添加zip内的pom文件所包含的依赖*/
        FileTree pomSource = project.zipTree(this).matching {
            include("**/pom.xml")
        }
        if (!pomSource.isEmpty()) {
            pomSource.visit { element ->
                if (!element.file.isDirectory()) {
                    def resolver = new MavenPomResolver(element.file.absolutePath)
                    resolver.dependencies.each { dependency ->
                        project.getDependencies().add("compile", dependency)
                    }
                    jarFile = new JarFile(jarFile.getParent(),
                            "${resolver.artifactId}-${resolver.version}")
                }
            }
        }
        /*添加编译和生成jar包任务*/
        project.tasks.create("compile${module}", JavaCompile) {
            source project.zipTree(this).files
            include '**/*.java'
            JavaPluginConvention javaPlugin = project.getConvention().getPlugin(JavaPluginConvention.class)
            SourceSetContainer sourceSets = javaPlugin.getSourceSets()
            classpath = sourceSets.main.compileClasspath
            destinationDir = project.file("build/tmp/class/${module}")
        }.group = LazyFishPlugin.GROUP

        project.tasks.create("jar${module}", Jar) {
            project.zipTree(this).matching {
                include("**/pom.xml")
                include("**/pom.properties")
                include("**/README.md")
                include("**/*.txt")
            }.each {
                from(it.absolutePath) {
                    into("META-INF/" + module.toLowerCase())
                }
            }
            from {
                project.file("build/tmp/class/${module}")
            }
            destinationDir = jarFile.getParentFile()
            archiveName = jarFile.name

            /*打包完成后，将jar包添加到项目依赖，这里不考虑testCompile*/
            project.getDependencies().add("compile", project.files("$archivePath"))
        }.group = LazyFishPlugin.GROUP
        //使用dependson无效
        project.tasks["compile${module}"].execute()
        project.tasks["jar${module}"].execute()
    }
}
