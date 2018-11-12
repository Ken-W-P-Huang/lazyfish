package com.windfish.gradle.foundation.file

/**
 * Created by kenhuang on 2018/10/27.
 */
class JsFile extends AbstractFile {
    public final static String EXT = 'js'
    public final static String FULL_EXT = '.js'

    JsFile(String path, String name) {
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
}
