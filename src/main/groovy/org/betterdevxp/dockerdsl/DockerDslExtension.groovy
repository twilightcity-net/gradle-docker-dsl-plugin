package org.betterdevxp.dockerdsl

import org.gradle.api.Project

class DockerDslExtension {

    static final String NAME = "dockerdsl"

    private Project project

    DockerDslExtension(Project project) {
        this.project = project
    }

    void container(Closure closure) {
        ContainerConfig container = new ContainerConfig()
        project.configure(container, closure)
        new DockerTaskConfigurator(project, container).registerTasksAndConfigureDependencies()
    }

}
