# Contributing to Gradle Docker DSL Plugin

Thank you for your interest in contributing to the Gradle Docker DSL Plugin! This document provides guidelines and instructions for contributing to the project.

## Code of Conduct

By participating in this project, you agree to abide by our [Code of Conduct](CODE_OF_CONDUCT.md).

## Getting Started

1. Fork the repository
2. Clone your fork: `git clone https://github.com/your-username/gradle-docker-dsl-plugin.git`
3. Create a new branch: `git checkout -b feature/your-feature-name`

## Development Environment Setup

1. Ensure you have Java 11+ installed
2. Install Docker (required for running tests)
3. Run `./gradlew build` to verify your setup

## Development Workflow

### Branching Strategy

- `main` - Stable release branch
- `develop` - Development branch
- Feature branches should be created from `develop`
- Bugfix branches should be created from `main`

### Commit Messages

Please follow these guidelines for commit messages:

- Use the present tense ("Add feature" not "Added feature")
- Use the imperative mood ("Move cursor to..." not "Moves cursor to...")
- Limit the first line to 72 characters or less
- Reference issues and pull requests after the first line

Example:
```
Add container refresh functionality

- Implement refresh task generation
- Add tests for refresh functionality
- Update documentation

Fixes #123
```

### Pull Request Process

1. Ensure your branch is up to date with the target branch
2. Run all tests: `./gradlew test functionalTest`
3. Update documentation if needed
4. Submit a pull request to the appropriate branch:
   - Features: `develop`
   - Bugfixes: `main`

### Testing Requirements

- All new features must include unit tests
- Complex features should include functional tests
- Ensure all existing tests pass
- Maintain or improve test coverage

### Documentation

- Update README.md for user-facing changes
- Add or update API documentation for code changes
- Include examples for new features

## Code Style

The project follows the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) with some modifications:

- Use 4 spaces for indentation
- Maximum line length of 120 characters
- Use `@Override` annotation when implementing interface methods

## Release Process

1. Update version in `gradle.properties`
2. Update CHANGELOG.md
3. Create a release branch from `main`
4. Tag the release
5. Merge back to `develop`

## Questions?

If you have any questions about contributing, please:

1. Check the existing documentation
2. Search existing issues
3. Open a new issue if needed

Thank you for contributing to the Gradle Docker DSL Plugin! 