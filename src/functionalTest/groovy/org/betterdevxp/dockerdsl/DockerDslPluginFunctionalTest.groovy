package org.betterdevxp.dockerdsl

import spock.lang.Ignore
import spock.lang.Specification
import org.gradle.testkit.runner.GradleRunner

@Ignore
class DockerDslPluginFunctionalTest extends Specification {
    def "can run task"() {
        given:
        def projectDir = new File("build/functionalTest")
        projectDir.mkdirs()
        new File(projectDir, "settings.gradle") << ""
        new File(projectDir, "build.gradle") << """
            plugins {
                id('org.betterdevxp.dockerdsl')
            }
        """

        when:
        def runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("greeting")
        runner.withProjectDir(projectDir)
        def result = runner.build()

        then:
        result.output.contains("Hello from plugin 'org.betterdevxp.dockerdsl'")
    }
}
