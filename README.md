# Email Viewer Android App

I treated this as a secure, local-data prototype and made assumptions that prioritize clean architecture, explicit integrity validation, and encrypted persistence. Even though the scope is small, I structured it the way I would a production feature. MVVM, clear state handling, background processing, and visible verification feedback because that’s what scales and remains maintainable.

## Features

- **Protocol Buffer implementation**: Loads and parses `.pb` file containing an email message
- **Hash Verification**: Verifies SHA-256 hashes for email body and attached images
- **Encrypted Database**: Uses Room with SQLCipher for secure data storage
- **MVVM Architecture**: Clean separation of concerns with ViewModel pattern
- **Dependency Injection**: Hilt for dependency management
- **UI**: Jetpack Compose with Material 3 design
- **System theme mode**: Full support for dark/light mode

## My Assumptions

### Technical Assumptions

- **Single-message scope**, I assumed sample_email.pb contains exactly one serialized EmailMessage with single attachment/image
- **common Image formats** I assumed that the attached images in Protocol Buffer files are JPEG/JPG or PNG.
- **Single feature, scalable structure**, I assumed this is a prototype with one primary screen, but structured the app using MVVM so it can scale to multiple email views or future features.
- **Dark mode support is system-driven**, I assumed dark mode follows system theme, not a custom toggle, as this aligns with Android standards.

## Tech Stack and Libraries

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Hilt
- **Database**: Room with SQLCipher (encrypted)
- **Protocol Buffers**: Google Protobuf
- **Unit test**: JUnit, Truth, Mockk

## Build/Run Instructions
### Prerequisites

1. **Android Studio Otter** (2025.2.1) or newer
2. **JDK 11** or higher
3. **Android SDK** with:
    - Android 14 (API 36)
    - Android 7.0 (API 24) minimum
4. **Git** (optional, for cloning)


### Steps

1. **Clone the repository** (or extract the ZIP file):
   ```bash
   git clone https://github.com/ngabomugisharobert/PracticalTest.git
   cd PracticalTest
   ```

2. **Open in Android Studio**:
    - Launch Android Studio
    - Click "Open" and select the project directory
    - Wait for Gradle sync to complete

3. **Sync Gradle**:
   ```bash
   ./gradlew clean build
   ```

4. **Configure Protocol Buffers**:
    - The protobuf compiler is configured in `build.gradle.kts`
    - Gradle will automatically generate Java/Kotlin classes from `.proto` files
    - Generated files are in `build/generated/source/proto/`

5. **Download Dependencies**:
    - All dependencies will be downloaded automatically during Gradle sync
    - If needed, manually trigger: `./gradlew dependencies`


## Running the App
### On Emulator

1. **Create an AVD** (Android Virtual Device):
    - Tools → Device Manager → Create Device
    - Choose a device (e.g., Pixel 7)
    - Select API 36 (Android 14)

2. **Start emulator** and click Run

### First Launch Steps

1. App will request **file access permissions**
2. Click "**Select Email File**" button
3. Navigate to the `sample_email.pb` file (copy it to device storage first)
4. The app will:
    - Parse the protobuf file
    - Verify SHA-256 hashes
    - Store in encrypted database
    - Display the email with verification badges



