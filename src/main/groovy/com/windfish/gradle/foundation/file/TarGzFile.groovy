package com.windfish.gradle.foundation.file

/**
 * Created by kenhuang on 2018/10/31.
 */
class TarGzFile extends TarFile {
    public final static String EXT = 'gz'
    public final static String FULL_EXT = '.tar.gz'

    TarGzFile(String path, String name) {
        super("${path}/${name}${FULL_EXT}")
    }

    TarGzFile(File parent, String name) {
        super("${parent.absolutePath}/${name}${FULL_EXT}")
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
