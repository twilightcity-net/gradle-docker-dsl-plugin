package org.betterdevxp.dockerdsl

import org.gradle.testkit.runner.BuildResult
import spock.lang.Specification

class DockerDslPluginTaskConfigFunctionalSpec extends Specification implements DockerDslPluginSupport {

    def setup() {
        initTestContainer()
        runner.withArguments("removeTest", "pullTest").build()
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
