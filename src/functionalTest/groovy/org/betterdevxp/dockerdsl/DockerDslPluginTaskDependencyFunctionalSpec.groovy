package org.betterdevxp.dockerdsl


import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

class DockerDslPluginTaskDependencyFunctionalSpec extends Specification {

    File projectDir = new File("build/functionalTest")
    GradleRunner runner = GradleRunner.create()
            .forwardOutput()
            .withPluginClasspath()
            .withProjectDir(projectDir)

    def setup() {
        projectDir.deleteDir()
        projectDir.mkdirs()

        new File(projectDir, "settings.gradle") << ""
        new File(projectDir, "build.gradle") << """
plugins {
    id('org.betterdevxp.dockerdsl')
}

dockerdsl {            
    container {
        name "test"
        imageName "alpine:latest"
        args "sleep", "10"
        stopWaitTime 1
    }
}            
"""

        runner.withArguments("destroyTest").build()
    }

    def "destroy should stop and remove running container"() {
        given:
        runner.withArguments("startTest").build()

        when:
        BuildResult result = runner.withArguments("destroyTest").build()

        then:
        assert result.output.contains("Stopping container with ID 'test'")
        assert result.output.contains("Removing container with ID 'test'")
        assert result.output.contains("Removing image with ID 'alpine:latest'")
    }

    def "create should pull the image if not pulled"() {
        when:
        BuildResult result = runner.withArguments("createTest").build()

        then:
        assert result.output.contains("Pulling image 'alpine:latest'")
        assert result.output.contains("Created container with ID 'test'")
    }

    def "start should pull and create container if image not pulled"() {
        when:
        BuildResult result = runner.withArguments("startTest").build()

        then:
        assert result.output.contains("Pulling image 'alpine:latest'")
        assert result.output.contains("Created container with ID 'test'")
        assert result.output.contains("Starting container with ID 'test'")
    }

    def "remove should stop started container if running"() {
        given:
        runner.withArguments("startTest").build()

        when:
        BuildResult result = runner.withArguments("removeTest").build()

        then:
        assert result.output.contains("Stopping container with ID 'test'")
        assert result.output.contains("Removing container with ID 'test'")
    }

}
