package org.betterdevxp.dockerdsl


import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

class DockerDslPluginFunctionalSpec extends Specification {

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
    }
}            
"""
    }

    def "pull should pull the image and skip if image already pulled"() {
        given:
        runner.withArguments("destroyTest").build()

        when:
        BuildResult result = runner.withArguments("pullTest").build()

        then:
        assert result.output.contains("Pulling image 'alpine:latest'")

        when:
        result = runner.withArguments("pullTest").build()

        then:
        assert result.output.contains("Task :pullTest SKIPPED")
    }

    def "destroy should remove the image and skip if image does not exist"() {
        given:
        runner.withArguments("pullTest").build()
        
        when:
        BuildResult result = runner.withArguments("destroyTest").build()

        then:
        assert result.output.contains("Removing image with ID 'alpine:latest'")
        
        when:
        result = runner.withArguments("destroyTest").build()

        then:
        assert result.output.contains("Task :destroyTest SKIPPED")
    }

    def "destroy should remove created container"() {
        given:
        runner.withArguments("pullTest", "createTest")

        when:
        BuildResult result = runner.withArguments("destroyTest").build()
        
        then:
        assert false
    }

    def "create should create the container and skip if container already exists"() {
        given:
        runner.withArguments("destroyTest", "pullTest").build()

        when:
        BuildResult result = runner.withArguments("createTest").build()

        then:
        assert result.output.contains("Created container with ID 'test'")

        when:
        result = runner.withArguments("createTest").build()

        then:
        assert result.output.contains("Task :createTest SKIPPED")
    }

    def "start should start the container and skip if container already started"() {
    }

    def "start should pull and create container if image not pulled"() {
    }

    def "stop should stop the container and skip if container not started"() {

    }

    def "remove should remove the container and skip if container does not exist"() {

    }

}
