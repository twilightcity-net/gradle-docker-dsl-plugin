package org.betterdevxp.dockerdsl

import org.betterdevxp.testkit.GradleRunnerSupport
import org.gradle.testkit.runner.BuildResult
import spock.lang.Specification

class DockerDslPluginFunctionalSpec extends Specification implements GradleRunnerSupport {

    def setup() {
        initTestContainer()
        runner.withArguments("destroyTest").build()
    }

    private void initTestContainer(String dsl = null) {
        buildFile.text = """
plugins {
    id('org.betterdevxp.dockerdsl')
}
"""
        if (dsl != null) {
            buildFile << dsl
        } else {
            buildFile << """
dockerdsl {
    container {
        name "test"
        imageName "alpine:latest"
        args "sleep", "10"
        stopWaitTime 1
    }
}
"""
        }
    }

    def "pull should pull the image and skip if image already pulled"() {
        when:
        BuildResult result = run("pullTest")

        then:
        assert result.output.contains("Pulling image 'alpine:latest'")

        when:
        result = run("pullTest")

        then:
        assert result.output.contains("Task :pullTest SKIPPED")
    }

    def "destroy should remove the image and skip if image does not exist"() {
        given:
        run("pullTest")

        when:
        BuildResult result = run("destroyTest")

        then:
        assert result.output.contains("Removing image with ID 'alpine:latest'")

        when:
        result = run("destroyTest")

        then:
        assert result.output.contains("Task :destroyTest SKIPPED")
    }

    def "create should create the container and skip if container already exists"() {
        given:
        run("pullTest")

        when:
        BuildResult result = run("createTest")

        then:
        assert result.output.contains("Created container with ID 'test'")

        when:
        result = run("createTest")

        then:
        assert result.output.contains("Task :createTest SKIPPED")
    }

    def "start should start the container and skip if container already started"() {
        given:
        run("createTest")

        when:
        BuildResult result = run("startTest")

        then:
        assert result.output.contains("Starting container with ID 'test'")

        when:
        result = run("startTest")

        then:
        assert result.output.contains("Task :startTest SKIPPED")
    }

    def "stop should stop the container and skip if container not started"() {
        given:
        run("startTest")

        when:
        BuildResult result = run("stopTest")

        then:
        assert result.output.contains("Stopping container with ID 'test'")

        when:
        result = run("stopTest")

        then:
        assert result.output.contains("Task :stopTest SKIPPED")
    }

    def "remove should remove the container and skip if container does not exist"() {
        given:
        run("createTest")

        when:
        BuildResult result = run("removeTest")

        then:
        assert result.output.contains("Removing container with ID 'test'")

        when:
        result = run("removeTest")

        then:
        assert result.output.contains("Task :removeTest SKIPPED")
    }

    def "create should apply args to created container"() {
        given:
        initTestContainer("""
dockerdsl {
    container {
        name "test"
        imageName "alpine:latest"
        args "thisshouldfail"
    }
}
""")

        when:
        BuildResult result = runAndFail("startTest")

        then:
        assert result.output.contains('starting container process caused: exec: \\"thisshouldfail\\"')
    }

    def "stopWaitTime should stop the container within the specified time"() {
        given:
        initTestContainer("""
dockerdsl {
    container {
        name "test"
        imageName "alpine:latest"
        args "sleep", "5"
        stopWaitTime 2
    }
}

project.ext.start = null
stopTest.doFirst {
    project.ext.start = System.currentTimeMillis()
}
stopTest.doLast {
    long taskTime = System.currentTimeMillis() - project.ext.start
    if (taskTime < 2000 || taskTime > 5000) {
        throw new GradleException("stopWaitTime not respected, taskTime=" + taskTime)
    }
}
""")

        when:
        run("startTest", "stopTest")

        then:
        notThrown(Exception)
    }

}
