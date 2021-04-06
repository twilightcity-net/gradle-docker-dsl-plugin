package org.betterdevxp.dockerdsl

import spock.lang.Specification


class ContainerConfigSpec extends Specification {

    ContainerConfig config = new ContainerConfig()

    def "getDisplayName should return displayName if set"() {
        given:
        config.name = "some-name"
        config.displayName = "otherName"

        expect:
        assert config.displayName == "otherName"
    }

    def "getDisplayName should convert name to camel case if displayName not set"() {
        given:
        config.name = name

        expect:
        assert config.displayName == expectedDisplayName

        where:
        name                 | expectedDisplayName
        "container"          | "container"
        "the-container-name" | "theContainerName"
        "the_container_name" | "theContainerName"
        "the-container_name" | "theContainerName"
        "theContainerName"   | "theContainerName"
    }

}
