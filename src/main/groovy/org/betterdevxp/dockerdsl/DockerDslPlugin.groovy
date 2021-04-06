package org.betterdevxp.dockerdsl

import com.bmuschko.gradle.docker.DockerRemoteApiPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class DockerDslPlugin implements Plugin<Project> {

    static final String NAME = "org.betterdevxp.dockerdsl"

    @Override
    void apply(Project project) {
        project.plugins.apply(DockerRemoteApiPlugin)
        project.extensions.create(DockerDslExtension.NAME, DockerDslExtension)
    }

}
