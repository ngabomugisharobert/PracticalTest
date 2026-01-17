# Email Viewer Android App

An Android application that loads Protocol Buffer files, parses email messages, verifies data integrity using SHA-256 hashing, stores data in an encrypted database, and displays emails with verification badges.

## Features

- **Protocol Buffer Support**: Loads and parses `.pb` file containing an email message
- **Hash Verification**: Verifies SHA-256 hashes for email body and attached images
- **Encrypted Database**: Uses Room with SQLCipher for secure data storage
- **MVVM Architecture**: Clean separation of concerns with ViewModel pattern
- **Dependency Injection**: Hilt for dependency management
- **UI**: Jetpack Compose with Material 3 design
- **Dark Mode**: Full support for dark theme



