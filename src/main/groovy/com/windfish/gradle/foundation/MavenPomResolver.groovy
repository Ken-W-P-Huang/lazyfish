package com.windfish.gradle.foundation

import groovy.util.slurpersupport.GPathResult

class MavenPomResolver {
    private String groupId
    private String artifactId
    private String version
    private List<String> dependencies

    MavenPomResolver(String xmlPath) {
        def projectNode = new XmlSlurper().parse(xmlPath)
        this.groupId = projectNode.groupId
        this.artifactId = projectNode.artifactId
        this.version = projectNode.version
        this.dependencies = new LinkedList<>()
        projectNode.dependencies.dependency.each { item ->
            this.dependencies.add(this.toGradleNotation(item))
        }
    }

    private String toGradleNotation(GPathResult dependency) {
        return String.format(
                "%s:%s:%s",
                dependency.groupId,
                dependency.artifactId,
                dependency.version
        )
    }

    String getGroupId() {
        return this.groupId
    }

    String getArtifactId() {
        return this.artifactId
    }

    String getVersion() {
        return this.version
    }

    List<String> getDependencies() {
        return this.dependencies
    }
}