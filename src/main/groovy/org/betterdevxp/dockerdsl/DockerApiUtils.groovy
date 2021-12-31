package org.betterdevxp.dockerdsl

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.model.Container
import com.github.dockerjava.api.model.Image

class DockerApiUtils {

    private static Image findImage(DockerClient client, String imageName) {
        List<Image> images = client.listImagesCmd().exec()
        images.find {
            it.repoTags?.contains(imageName)
        }
    }

    static boolean isImageLocal(DockerClient client, String imageName) {
        findImage(client, imageName) != null
    }

    private static Container findContainer(DockerClient client, String containerName) {
        List<Container> containers = client.listContainersCmd()
                .withShowAll(true)
                .exec()
        containers.find {
            it.names.contains("/${containerName}")
        }
    }

    static boolean isContainerCreated(DockerClient client, String containerName) {
        findContainer(client, containerName) != null
    }

    static boolean isContainerRunning(DockerClient client, String containerName) {
        Container container = findContainer(client, containerName)
        // TODO: are there any other states we should care about?  is there an enum that encompasses the states?
        container?.state == "running"
    }

}
