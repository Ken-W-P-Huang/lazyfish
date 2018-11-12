package com.windfish.gradle.foundation.file

/**
 * Created by kenhuang on 2018/10/31.
 */
class PkgFile extends AbstractFile {
    public final static String EXT = 'pkg'
    public final static String FULL_EXT = '.pkg'

    PkgFile(String path, String name) {
        super("${path}/${name}${FULL_EXT}")
    }

    PkgFile(File parent, String name) {
        super(parent, "${name}${FULL_EXT}")
    }

    @Override
    public boolean isValid() {
        return super.isValid() && this.name.endsWith(FULL_EXT)
    }

    @Override
    public String getShortName() {
        return this.name.replace("${FULL_EXT}", "")
    }
}
