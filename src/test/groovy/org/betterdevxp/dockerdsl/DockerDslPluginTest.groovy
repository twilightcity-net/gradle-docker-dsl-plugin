package org.betterdevxp.dockerdsl

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class DockerDslPluginTest extends Specification {

    def "should create container lifecycle tasks"() {
        given:
        Project project = ProjectBuilder.builder().build()

        when:
        project.plugins.apply(DockerDslPlugin.NAME)
        project.extensions.findByType(DockerDslExtension).container {
            name "postgres"
            imageName "postgres:latest"
            publish "5432:5432"
            env "POSTGRES_USER=postgres"
            env "POSTGRES_PASSWORD=postgres"
        }

        then:
        assert project.tasks.findByName("pullPostgres") != null
        assert project.tasks.findByName("createPostgres") != null
        assert project.tasks.findByName("startPostgres") != null
        assert project.tasks.findByName("stopPostgres") != null
        assert project.tasks.findByName("removePostgres") != null
        assert project.tasks.findByName("destroyPostgres") != null
    }

}
