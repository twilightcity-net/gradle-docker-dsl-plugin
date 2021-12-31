package org.betterdevxp.dockerdsl

import com.bmuschko.gradle.docker.DockerRemoteApiPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class DockerDslPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.pluginManager.apply(DockerRemoteApiPlugin)
        project.extensions.create(DockerDslExtension.NAME, DockerDslExtension, project)
    }

}
