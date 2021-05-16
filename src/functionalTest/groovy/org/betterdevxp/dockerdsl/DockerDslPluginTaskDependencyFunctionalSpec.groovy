package org.betterdevxp.dockerdsl

import org.gradle.testkit.runner.BuildResult
import spock.lang.Specification

class DockerDslPluginTaskDependencyFunctionalSpec extends Specification implements DockerDslPluginSupport {

    def setup() {
        initTestContainer()
        run("destroyTest")
    }

    def "destroy should stop and remove running container"() {
        given:
        run("startTest")

        when:
        BuildResult result = run("destroyTest")

        then:
        assert result.output.contains("Stopping container with ID 'test'")
        assert result.output.contains("Removing container with ID 'test'")
        assert result.output.contains("Removing image with ID 'alpine:latest'")
    }

    def "create should pull the image if not pulled"() {
        when:
        BuildResult result = run("createTest")

        then:
        assert result.output.contains("Pulling image 'alpine:latest'")
        assert result.output.contains("Created container with ID 'test'")
    }

    def "start should pull and create container if image not pulled"() {
        when:
        BuildResult result = run("startTest")

        then:
        assert result.output.contains("Pulling image 'alpine:latest'")
        assert result.output.contains("Created container with ID 'test'")
        assert result.output.contains("Starting container with ID 'test'")
    }

    def "remove should stop started container if running"() {
        given:
        run("startTest")

        when:
        BuildResult result = run("removeTest")

        then:
        assert result.output.contains("Stopping container with ID 'test'")
        assert result.output.contains("Removing container with ID 'test'")
    }

}
