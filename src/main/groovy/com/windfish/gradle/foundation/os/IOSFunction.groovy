package com.windfish.gradle.foundation.os

/**
 * Created by kenhuang on 2018/11/13.
 */

interface IOSFunction {
    public File getNodeModulesDir(boolean isGlobal)
    public boolean isNodeInstalled()
    public void installNode(String nodeVersion,String tmpDirPath, String urlString)
    public String which(String command)
}