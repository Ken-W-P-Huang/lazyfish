import com.windfish.gradle.AbstractProjectTest
import com.windfish.gradle.foundation.file.GroovyFile
import com.windfish.gradle.foundation.file.JavaFile

public class JavaFileTest extends AbstractProjectTest {
    def 'parseMethods'() {
        given:
        JavaFile file = new JavaFile("/Volumes/Work/lazyfish/src/main/groovy/com/windfish/gradle/foundation/file/JavaFile.groovy")
        ///Volumes/Work/lazyfish/src/main/java/JarUtils.java
        when:
        file.parseMethods { method ->
            println method
        }
        then:
        println ''
    }

    def 'mirrorSpockFile'() {
        given:
        this.project.copy {
            from '/Volumes/Work/lazyfish/src'
            into 'src'
        }
        this.project.delete('src/test')
        this.project.file('src/test/groovy/com/windfish/gradle/foundation/file/').mkdirs()

        when:
        GroovyFile file = new GroovyFile(this.project.file('src/main/groovy/com/windfish/gradle/foundation/file/ZipFile.groovy').absolutePath)
        file.mirrorSpockFile(this.project.file('src/main/groovy'), this.project.file('src/test/groovy'))
        then:
        file.exists()
        println project.file('src/test/groovy/com/windfish/gradle/foundation/file/ZipFileTest.groovy')
        println ""
    }
}