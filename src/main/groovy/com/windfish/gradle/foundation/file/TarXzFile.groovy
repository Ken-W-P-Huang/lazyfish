package com.windfish.gradle.foundation.file

/**
 * Created by kenhuang on 2018/10/31.
 */
class TarXzFile extends TarFile {
    public final static String EXT = 'xz'
    public final static String FULL_EXT = '.tar.xz'

    TarXzFile(String path, String name) {
        super("${path}", "${name}${FULL_EXT}")
    }

    TarXzFile(File parent, String name) {
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
