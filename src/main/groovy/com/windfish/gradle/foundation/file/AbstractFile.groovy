package com.windfish.gradle.foundation.file

/**
 * Created by kenhuang on 2018/10/27.
 */
public abstract class AbstractFile extends File {
    AbstractFile(String pathname) {
        super(pathname)
    }

    AbstractFile(String parent, String child) {
        super(parent, child)
    }

    AbstractFile(File parent, String child) {
        super(parent, child)

    }

    AbstractFile(URI uri) {
        super(uri)
    }

    public boolean isValid() {
        return this.exists() && this.isFile()
    }

    public abstract String getShortName()

    public void download(String urlString) {
        if (!this.exists()) {
            String redirectUrl
            /* 重定向的连接末尾不能有"/"*/
            if (urlString.endsWith("/")) {
                urlString = urlString.substring(0, urlString.length() - 1)
            }
            new URL(urlString).openConnection().with { connection ->
                connection.instanceFollowRedirects = false
                redirectUrl = connection.getHeaderField("Location")
                //todo 不确定这么判断是否合适，使用URL的API会报异常
                if (!redirectUrl || !redirectUrl.contains(":")) {
                    new URL(urlString).withInputStream { i -> this.withOutputStream { it << i } }
                    return
                } else {
                    /* 从重定向连接下载文件 */
                    while (urlString) {
                        new URL(urlString).openConnection().with { redirectConnection ->
                            redirectConnection.instanceFollowRedirects = false
                            urlString = redirectConnection.getHeaderField("Location")
                            if (!urlString) {
                                this.withOutputStream { out ->
                                    redirectConnection.inputStream.with { inp ->
                                        out << inp
                                        inp.close()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
