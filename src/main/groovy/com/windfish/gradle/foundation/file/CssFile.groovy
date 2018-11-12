package com.windfish.gradle.foundation.file

/**
 * Created by kenhuang on 2018/10/27.
 */
class CssFile extends AbstractFile {
    public final static String EXT = 'css'
    public final static String FULL_EXT = '.css'

    CssFile(String path, String name) {
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
