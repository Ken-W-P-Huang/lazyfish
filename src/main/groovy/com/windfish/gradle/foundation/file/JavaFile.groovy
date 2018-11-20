package com.windfish.gradle.foundation.file

import org.gradle.internal.impldep.org.apache.commons.collections.map.HashedMap

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by kenhuang on 2018/10/27.
 */
class JavaFile extends AbstractFile {
    public final static String EXT = 'java'
    public final static String FULL_EXT = '.java'

    JavaFile(String fullPath) {
        super(fullPath)
    }

    JavaFile(String path, String name) {
        super("${path}/${name}${FULL_EXT}")

    }

    @Override
    public boolean isValid() {
        return super.isValid() && this.name.endsWith(EXT)
    }

    @Override
    public String getShortName() {
        return this.name.replace("${FULL_EXT}", "")
    }

    public String getFullExtension() {
        return FULL_EXT
    }

    public void mirrorJUnitFile(File sourceDir, File testDir,List<String>ignoredMethods) {
        String jUnitFileName = this.absolutePath.replaceFirst(sourceDir.absolutePath, testDir.absolutePath)
                .replace(this.getFullExtension(), "Test${FULL_EXT}")
        if (jUnitFileName != null) {
            JavaFile jUnitFile = new JavaFile(jUnitFileName)
            if (!jUnitFile.exists()) {
                String packageName = this.getPackageName(sourceDir)
                if (packageName != null && packageName != "") {
                    jUnitFile.append("package ${packageName};\n\n")
                }
                jUnitFile.append("import org.junit.Before;\n" +
                                 "import org.junit.After;\n" +
                                 "import org.junit.Test;\n\n" +
                                 "public class ${jUnitFile.getShortName()} {\n"
                                 )
                if (!ignoredMethods.contains('init')){
                    jUnitFile.append("    @Before\n" +
                                    "    public void init()  {\n" +
                                    "    }\n\n")
                }
                this.parseMethods(ignoredMethods) { methodName ->
                    jUnitFile.append("    @Test\n" +
                                     "    public void ${methodName}() {\n\n" +
                                     "    }\n\n")
                }
                if (!ignoredMethods.contains('destroy')) {
                    jUnitFile.append("    @After\n" +
                            "    public void destroy() {\n\n" +
                            "    }\n\n")
                }
                jUnitFile.append("}")
            }
        }
    }

    public void mirrorSpockFile(File sourceDir, File testDir,List<String>ignoredMethods) {
        String spockFilePath = this.absolutePath.replaceFirst(sourceDir.absolutePath, testDir.absolutePath)
                .replace(this.getFullExtension(), "Test${GroovyFile.FULL_EXT}")
        if (spockFilePath != null) {
            GroovyFile spockFile = new GroovyFile(spockFilePath)
            if (!spockFile.exists()) {
                String packageName = this.getPackageName(sourceDir)
                if (packageName != null && packageName != "") {
                    spockFile.append("package ${packageName};\n\n")
                }
                spockFile.append(
                        "import org.junit.Rule\n" +
                                "import spock.lang.Specification\n\n" +
                                "public class ${spockFile.getShortName()} extends Specification {\n" +
                                "\n")
                this.parseMethods(ignoredMethods) { methodName ->
                    spockFile.append(
                            "    def ${methodName}() {\n\n" +
                                    "    }\n\n")
                }
                spockFile.append("}")
            }
        }
    }
    /**
     * 根据目录路径获取本java文件的包名，即其相对根目录的路径
     * @param javaFilePath
     * @param rootPath
     * @return
     */
    public String getPackageName(File rootDir) {
        StringBuilder packageName = new StringBuilder()
        String[] elements = FileUtil.relativeTo(this.absolutePath, rootDir.absolutePath).split("/")
        for (int i = 0; i < elements.length - 1; i++) {
            if (elements[i] != "") {
                packageName.append(elements[i])
                if (i < elements.length - 2) {
                    packageName.append(".")
                }
            }
        }
        return packageName
    }

    public void mirrorTestFile(File sourceDir, File testDir, String testFramework,List<String>ignoredMethods) {
        switch (testFramework) {
            case "junit":
                this.mirrorJUnitFile(sourceDir, testDir,ignoredMethods)
                break
            case "spock":
                this.mirrorSpockFile(sourceDir, testDir,ignoredMethods)
                break
            default:
                break
        }
    }

    /**
     * 正则表达式不准确，只要能区分出方法即可，现在不是在搞编译！！！
     * @param closure
     */
    private void parseMethods(List<String>ignoredMethods, Closure closure) {
        String content = this.text
        Iterator<String> iterator
        Map<String,Integer> methodMap = new HashMap<>()
        Integer count
        String method
        String separatorRegex = "[ \\t]"
        String variableRegex = "([\$_a-zA-Z][\$_a-zA-Z0-9]*)"
        String annotationRegex = "(@[\$_a-zA-Z][\$_a-zA-Z0-9]*${separatorRegex}+)"
        String variableDeclarationRegex = "(${annotationRegex}?${variableRegex}${separatorRegex}+${variableRegex})"
        String startRegex = "(?=${separatorRegex}*\\{?${separatorRegex}*)"
        String accessRegex = "(?=(.*((public)|(private)|(protected))${separatorRegex}+)?)"
        String staticRegex = "(?=(.*static${separatorRegex}+)?)"
        String finalRegex = "(?=(.*final${separatorRegex}+)?)"
        String synchronizedRegex = "(?=(.*synchronized${separatorRegex}+)?)"
        String volatileRegex = "(?=(.*volatile${separatorRegex}+)?)"
        String modifyRegex = "(?=${accessRegex}${staticRegex}${finalRegex})"
        String returnTypeRegex = "(?=(.*${variableRegex}${separatorRegex}+))"
        String methodNameRegex = "(${variableRegex}${separatorRegex}*)"
        String parametersRegex = "(\\(${separatorRegex}*(${variableDeclarationRegex}${separatorRegex}*," +
                "${separatorRegex}*)*${variableDeclarationRegex}?${separatorRegex}*\\)${separatorRegex}*)"
        String exceptionRegex = "(throws${separatorRegex}*${variableRegex}${separatorRegex}*)?"
        String endRegex = "((\\{${separatorRegex}*\\n)|(\\n${separatorRegex}*\\{))"
        String regex = "(${startRegex}${modifyRegex}${synchronizedRegex}${volatileRegex}${returnTypeRegex})" +
                "${methodNameRegex}(?=${parametersRegex}${exceptionRegex}${endRegex})"
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL)
        Matcher matcher = pattern.matcher(content)
        loop:while (matcher.find()) {
            method = content.substring(matcher.start(), matcher.end())
            iterator = ignoredMethods.iterator()
            while (iterator.hasNext()){
                if (method.startsWith(iterator.next())){
                    continue loop
                }
            }
            if (method.startsWith( 'catch')){
                continue loop
            }
            if (method.startsWith('equals')){
                continue loop
            }
            if (method.startsWith( 'hashCode')){
                continue loop
            }
            if (method.startsWith( 'toString')){
                continue loop
            }
            count = (Integer)methodMap.get(method)
            if (count == null){
                /*方法没有重载*/
                methodMap.put(method,0)
            }else{
                count += 1
                methodMap.put(method,count)
                method += count
            }
            closure(method)
        }
    }

//    public void copyImportStatement(JavaFile destination){
//        String content = this.text
//        String regex = "(?<=\n)[ \\t]*?import[ \\t].*?\n"
//        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL)
//        Matcher matcher = pattern.matcher(content)
//        while (matcher.find()) {
//            destination.append(content.substring(matcher.start(), matcher.end()))
//        }
//    }
}

