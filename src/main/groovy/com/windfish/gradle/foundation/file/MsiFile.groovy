package com.windfish.gradle.foundation.file

/**
 * Created by kenhuang on 2018/10/31.
 */
class MsiFile extends AbstractFile {
    public final static String EXT = 'msi'
    public final static String FULL_EXT = '.msi'

    MsiFile(String path, String name) {
        super("${path}/${name}${FULL_EXT}")
    }

    MsiFile(File parent, String name) {
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
}
