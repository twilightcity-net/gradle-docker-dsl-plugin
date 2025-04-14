[![main Actions Status](https://github.com/betterdevxp/gradle-docker-dsl-plugin/workflows/gradle-build/badge.svg)](https://github.com/betterdevxp/gradle-docker-dsl-plugin/actions)

# Gradle Docker DSL Plugin

A Gradle plugin that provides a convenient Domain-Specific Language (DSL) for managing Docker containers in your build and test lifecycle. This plugin is part of the Twilight City ecosystem and embodies the testing best practices promoted by the Twilight City community. It builds upon the excellent [gradle-docker-plugin](https://github.com/bmuschko/gradle-docker-plugin) to provide a more intuitive and streamlined interface for container management.

## Overview

The Gradle Docker DSL Plugin is designed to simplify Docker container management in your Gradle builds, particularly for testing scenarios. It provides a declarative way to define and manage Docker containers, automatically handling the complete container lifecycle from pulling images to cleanup. The plugin is specifically tailored to support modern testing practices, making it easier to create isolated, reproducible test environments that can be seamlessly integrated into both local development and CI/CD pipelines.

### Key Features

- Simple, declarative DSL for container configuration
- Automatic task generation for container lifecycle management
- Smart task dependencies that only execute when needed
- Support for multiple containers with bulk operations
- Built on top of the battle-tested gradle-docker-plugin
- Comprehensive test coverage with both unit and functional tests

## Installation

Add the plugin to your `build.gradle` file:

```gradle
plugins {
    id 'net.twilightcity.docker-dsl' version '0.2.0'
}
```

## Usage

### Basic Container Configuration

Define your containers using the `dockerdsl` block:

```gradle
dockerdsl {
    container {
        name "postgres"
        imageName "postgres:9.4"
        portBinding "5432:5432"
        envVar "POSTGRES_USER=postgres"
        envVar "POSTGRES_PASSWORD=postgres"
    }
}
```

### Available Container Configuration Options

- `name`: Container name (required)
- `imageName`: Docker image name and tag (required)
- `portBinding`: Port mappings (host:container)
- `envVar`: Environment variables
- `args`: Container command arguments
- `stopWaitTime`: Time to wait for container to stop (in seconds)

### Generated Tasks

For each container definition, the plugin automatically creates the following tasks:

- `pull{ContainerName}`: Pulls the Docker image
- `create{ContainerName}`: Creates the container
- `start{ContainerName}`: Starts the container
- `stop{ContainerName}`: Stops the container
- `remove{ContainerName}`: Removes the container
- `destroy{ContainerName}`: Removes the Docker image

### Bulk Operations

When multiple containers are defined, additional bulk operation tasks are created:

- `startAllContainers`
- `stopAllContainers`
- `removeAllContainers`
- `refreshAllContainers`
- `destroyAllImages`

### Task Dependencies

Tasks are automatically configured with appropriate dependencies. For example:
- `create` depends on `pull`
- `start` depends on `create`
- `remove` depends on `stop`
- `destroy` depends on `remove`

Tasks are also smart about their execution - they will only run if their action is needed. For example, if you run `startPostgres` but the image hasn't been pulled yet, it will automatically execute `pullPostgres` and `createPostgres` first.

## Development

### Building the Plugin

```bash
./gradlew build
```

### Running Tests

```bash
./gradlew test
./gradlew functionalTest
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

We welcome contributions to the Gradle Docker DSL Plugin! Please see our [Contributing Guidelines](CONTRIBUTING.md) for details on how to get started, our development process, and how to submit pull requests.
