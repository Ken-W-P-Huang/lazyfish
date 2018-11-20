package com.windfish.gradle.foundation.file

import org.gradle.api.GradleException
import org.gradle.api.Project

/**
 * Created by kenhuang on 2018/10/31.
 */
class TarFile extends AbstractFile {
    public final static String EXT = 'tar'
    public final static String FULL_EXT = '.tar'

    TarFile(String fullPath) {
        super(fullPath)
    }

    TarFile(String path, String name) {
        super("${path}/${name}${FULL_EXT}")
    }

    TarFile(File parent, String name) {
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

    public void untar(Project project, String untarDirPathString, Closure filter) {
        if (!this.isValid()) {
            throw new GradleException("Tar file\" ${this.absolutePath}\" is invalid!")
        }
        File untarDir = project.file(untarDirPathString)
        if (!untarDir.exists()) {
            untarDir.mkdirs()
        }
        int length
        project.tarTree(this).matching(filter).each { item ->
            InputStream inputStream = new FileInputStream(item)
            OutputStream outputStream = new FileOutputStream(new File("${untarDir.absolutePath}/${item.name}"))
            byte[] bytes = new byte[1024]
            while (true) {
                length = inputStream.read(bytes)
                if (length == -1) {
                    break
                }
                outputStream.write(bytes, 0, length)
            }
        }
    }
}
