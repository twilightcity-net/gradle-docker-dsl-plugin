package org.betterdevxp.dockerdsl

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.model.Image

class DockerApiUtils {

    private Image findImage(DockerClient client, String imageName) {
        List<Image> images = client.listImagesCmd().exec()
        images.find {
            it.repoTags?.contains(imageName)
        }
    }

    boolean isImageLocal(DockerClient client, String imageName) {
        findImage(client, imageName) != null
    }

    String getImageId(DockerClient client, String imageName) {
        findImage(client, imageName)?.id
    }

}
