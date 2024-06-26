# Kotlin Multiplatform Project

This is a Kotlin Multiplatform project targeting Android and iOS.

## Project Structure

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications. It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name. For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app, `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

## Key Dependencies

This project uses several key dependencies to provide functionality and facilitate development. Here's an overview of the most important ones:

- **Kotlin Multiplatform**: This is the core of the project, enabling code sharing between different platforms.
- **Jetpack Compose**: Used to build the user interface across all platforms.
- **Ktor**: Used for network calls across all platforms.
- **Kotlinx Coroutines**: Used for asynchronous programming.
- **Kotlinx Serialization**: Used for JSON serialization and deserialization.
- **Koin**: Used for dependency injection.
- **Kamel**: Used for asynchronous image loading.
- **Okio and Kotlin Crypto**: Used for data manipulation and cryptography.
- **Cupertino Adaptive**: Used to adapt the user interface to the specific platform.
- **AndroidX Navigation Compose**: Used for navigation within the app.

## Learn More

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…