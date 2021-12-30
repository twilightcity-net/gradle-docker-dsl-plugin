package org.betterdevxp.testkit

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner

trait GradleRunnerSupport {

    private File projectDir = new File("build/gradleRunnerTestDir/${UUID.randomUUID()}")
    private GradleRunner runner

    File getProjectDir() {
        if (projectDir.exists() == false) {
            projectDir.mkdirs()
        }
        projectDir
    }

    File getBuildFile() {
        new File(getProjectDir(), "build.gradle")
    }

    GradleRunner getRunner() {
        if (runner == null) {
            File settingsFile = new File(projectDir, "settings.gradle")
            if (settingsFile.exists() == false) {
                settingsFile.text = ""
            }
            projectDir.mkdirs()
            runner = GradleRunner.create()
                    .forwardOutput()
                    .withPluginClasspath()
                    .withProjectDir(projectDir)
        }
        runner
    }

    private GradleRunner runnerWithArgumentsIncludingStackTrace(String... args) {
        if ((args as List).contains("-s") == false) {
            args = args + "-s"
        }
        getRunner().withArguments(args)
    }

    BuildResult run(String... args) {
        runnerWithArgumentsIncludingStackTrace(args).build()
    }

    BuildResult runAndFail(String ... args) {
        runnerWithArgumentsIncludingStackTrace(args).buildAndFail()
    }

}
