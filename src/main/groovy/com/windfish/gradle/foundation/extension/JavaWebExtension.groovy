package com.windfish.gradle.foundation.extension

import org.gradle.api.Action
import org.gradle.util.ConfigureUtil

/**
 * Created by kenhuang on 2018/10/27.
 */
class JavaWebExtension extends JavaExtension {
    public final static String NAME = 'javaweb'
    public Map<String, String> jsDependencies
    public Map<String, String> cssDependencies
    public String jsPath = "src/main/webapp/js"
    public String jsPathInWar = "js"
    public String cssPath = "src/main/webapp/css"
    public String cssPathInWar = "css"
    public NodeExtension node = new NodeExtension()
    public GruntExtension grunt = new GruntExtension()
    public String webXmlPath = "src/main/resources/"

    NodeExtension node(Closure closure) {
        return node(ConfigureUtil.configureUsing(closure))
    }

    void node(Action<? super NodeExtension> action) {
        if (this.node == null) this.node = new NodeExtension()
        action.execute(this.node)
    }

    GruntExtension grunt(Closure closure) {
        return grunt(ConfigureUtil.configureUsing(closure))
    }

    void grunt(Action<? super GruntExtension> action) {
        if (this.grunt == null) this.grunt = new GruntExtension()
        action.execute(this.grunt)
    }
}
