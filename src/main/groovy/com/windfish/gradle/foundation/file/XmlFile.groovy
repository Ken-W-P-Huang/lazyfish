package com.windfish.gradle.foundation.file

/**
 * Created by kenhuang on 2018/11/2.
 */
class XmlFile extends AbstractFile {
    public final static String EXT = 'xml'
    public final static String FULL_EXT = '.xml'

    XmlFile(String fullPath) {
        super(fullPath)
    }

    XmlFile(String path, String name) {
        super("${path}/${name}${FULL_EXT}")

    }

    @Override
    public String getShortName() {
        return this.name.replace("${FULL_EXT}", "")
    }
}
