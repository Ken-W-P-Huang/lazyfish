package com.windfish.gradle.foundation.file

import com.windfish.gradle.AbstractProjectTest

/**
 * Created by kenhuang on 2018/10/27.
 */
public class AbstractFileTest extends AbstractProjectTest {
    def 'download'() {
        given:
        ZipFile file = new ZipFile("build/tmp/test", 'd3')
        when:
        file.download("https://github.com/d3/d3/releases/download/v5.7.0/d3.zip")
        then:
        println file.absolutePath
        println this.project.path
        file.exists()
    }
}
