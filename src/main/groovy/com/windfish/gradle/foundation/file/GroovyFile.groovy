package com.windfish.gradle.foundation.file
/**
 * Created by kenhuang on 2018/10/27.
 */
class GroovyFile extends JavaFile {
    public final static String EXT = 'groovy'
    public final static String FULL_EXT = '.groovy'

    GroovyFile(String fullPath) {
        super(fullPath)
    }

    GroovyFile(String path, String name) {
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
}
