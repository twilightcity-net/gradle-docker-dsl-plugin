package org.betterdevxp.dockerdsl


import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

class DockerDslPluginFunctionalSpec extends Specification {

    File projectDir = new File("build/functionalTest")
    File buildFile = new File(projectDir, "build.gradle")
    GradleRunner runner = GradleRunner.create()
            .forwardOutput()
            .withPluginClasspath()
            .withProjectDir(projectDir)

    def setup() {
        projectDir.deleteDir()
        projectDir.mkdirs()

        new File(projectDir, "settings.gradle") << ""
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

    def "create should create the container and skip if container already exists"() {
        given:
        runner.withArguments("pullTest").build()

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
        given:
        runner.withArguments("createTest").build()

        when:
        BuildResult result = runner.withArguments("startTest").build()

        then:
        assert result.output.contains("Starting container with ID 'test'")

        when:
        result = runner.withArguments("startTest").build()

        then:
        assert result.output.contains("Task :startTest SKIPPED")
    }

    def "stop should stop the container and skip if container not started"() {
        given:
        runner.withArguments("startTest").build()

        when:
        BuildResult result = runner.withArguments("stopTest").build()

        then:
        assert result.output.contains("Stopping container with ID 'test'")

        when:
        result = runner.withArguments("stopTest").build()

        then:
        assert result.output.contains("Task :stopTest SKIPPED")
    }

    def "remove should remove the container and skip if container does not exist"() {
        given:
        runner.withArguments("createTest").build()

        when:
        BuildResult result = runner.withArguments("removeTest").build()

        then:
        assert result.output.contains("Removing container with ID 'test'")

        when:
        result = runner.withArguments("removeTest").build()

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
        BuildResult result = runner.withArguments("startTest").buildAndFail()

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
        runner.withArguments("startTest", "stopTest").build()

        then:
        notThrown(Exception)
    }

}
