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

## Managing API Keys with BuildKonfig

In this project, we use the `buildKonfig` Gradle plugin to manage our API keys. This plugin is a code generation tool for Kotlin and Kotlin Multiplatform projects that generates a `BuildKonfig` object in your code. This object contains constants that you can define in your Gradle configuration file.

The API keys for the Marvel API are stored in a `local.properties` file in the root of the project. This file is added to `.gitignore` to ensure that the keys are not committed to the repository. Here's an example of what the `local.properties` file looks like:

\```ini
MARVEL_PRIVATE_KEY=your_private_key_here
MARVEL_PUBLIC_KEY=your_public_key_here
\```

The `buildKonfig` plugin reads these keys from the `local.properties` file and generates a `BuildKonfig` object with these keys as constants. This object is then used in the code to authorize HTTP requests. Here's an example of how the `BuildKonfig` object is used:

\```kotlin
fun HttpRequestBuilder.authorized() {
    val ts = Clock.System.now().epochSeconds.toString()
    val hash =
        md5Digest("$ts${BuildKonfig.MARVEL_PRIVATE_KEY}${BuildKonfig.MARVEL_PUBLIC_KEY}".toByteArray())
    parameter("ts", ts)
    parameter("hash", hash)
}
\```

This approach allows us to keep our API keys secure and out of version control, while still being able to use them in our code in a type-safe way.

Please replace `your_private_key_here` and `your_public_key_here` with your actual Marvel API keys.

## Learn More

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…