# AlchemyApp

**AlchemyApp** – an alchemy inventory and potion brewing companion designed especially for D&D players.

AlchemyApp empowers players to manage their alchemical ingredients and knowledge with ease. Whether you’re keeping detailed notes of your inventory or combining ingredients to brew potent potions, this app has you covered. *(Note: This app is still under active development.)*

## Table of Contents
1. [Vision Statement](#vision-statement)
2. [Features](#features)
3. [Usage](#usage)
4. [Architecture](#architecture)
5. [Package Layers](#package-layers)
6. [Branching Strategy](#branching-strategy)
7. [Dependencies](#dependencies)
8. [Development Environment](#development-environment)
9. [Contributors](#contributors)
10. [Project Status & Future Plans](#project-status--future-plans)
11. [Test](#test)

## Vision Statement
- [Vision Statement](./documents/vision_statment.md)

## Features

AlchemyApp currently provides the following capabilities:

- **User Authentication:**  
  On launch, you’ll encounter a login screen. Existing users can log in while newcomers have the option to register.

- **Ingredient Inventory:**  
  Track your alchemical ingredients with detailed information, including known effects and quantity.

- **Knowledgebook:**  
  Maintain a catalog of the ingredients and effects you know—a quick reference for every aspiring alchemist.

- **Foraging Mechanism:**  
  Tap the **Forage** button to discover and add new ingredients to your inventory.

- **Brew Screen:**  
  Combine two ingredients to create a new potion. If the selected ingredients share any commonalities, a corresponding potion—including its effects—is automatically added to your inventory.

- **Detailed Item Inspection:**  
  Click on any inventory item to view more details:
  - **Ingredients:** See the known effects and current quantity.
  - **Potions:** View its level, bonus dice, duration, and a comprehensive description.

- **Profile & Leveling Up:**  
  In your profile, you can log out or level up. **Important:** When leveling up, please ask your DM (Alex) to enter the password. Leveling up affects both your ingredient yield during foraging and the effectiveness of your brewed potions.

*Note:* All data is stored locally in an HSQLDB. It is strongly recommended that you keep physical notes on potions, levels, and ingredients—in case this setup changes in future updates.

## Usage

**AlchemyApp** is an Android Studio project. Follow these steps to set it up:

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/ablepacifist/AlchemyApp.git
   ```
2. **Open the Project in Android Studio:**
   - Launch Android Studio.
   - Choose **File > Open...** and navigate to the cloned repository.
   - Allow Android Studio to sync Gradle and resolve all dependencies.
3. **Run the App:**
   - Connect an Android device or start an emulator.
   - Click the **Run** button (green triangle) or press **Shift + F10**.
   - The app will compile, install, and launch on your device/emulator.
4. **Log In / Register:**
   - The login screen appears on launch.
   - Enter your credentials, or choose **Register** if you’re a new user.

## Architecture

- [Project Architecture Documentation](./documents/architecture_diagram.md)

### Architecture Diagram

Below is the architecture diagram for AlchemyApp. (This document and diagram will be updated as further enhancements are made.)

![Architecture Diagram](./documents/architecture_diagram.md)

## Package Layers

AlchemyApp is developed using a layered architecture. This structure ensures modularity so that parts (like logic or data persistence) can be replaced without affecting the entire app.

- **Object Layer:**  
  Contains the core data structures and interfaces.  
  [View Object Layer](https://github.com/ablepacifist/AlchemyApp/tree/main/app/src/main/java/alchemy/srsys/object)

- **UI Layer:**  
  Manages the user interface and interactions.  
  [View UI Layer](https://github.com/ablepacifist/AlchemyApp/tree/main/app/src/main/java/alchemy/srsys/UI)

- **Logic Layer:**  
  Handles business rules and bridges the UI with the data layers.  
  [View Logic Layer](https://github.com/ablepacifist/AlchemyApp/tree/main/app/src/main/java/alchemy/srsys/logic)

- **Data Layer:**  
  Responsible for data persistence and retrieval. (No Android-specific imports are used in this layer.)  
  [View Data Layer](https://github.com/ablepacifist/AlchemyApp/tree/main/app/src/main/java/alchemy/srsys/data)

- **Application Layer:**  
  Oversees application initialization and core services.  
  [View Application Layer](https://github.com/ablepacifist/AlchemyApp/tree/main/app/src/main/java/alchemy/srsys/application)

## Dependencies

The following dependencies are used to build and run AlchemyApp:

```gradle
implementation 'androidx.core:core:1.7.0'
implementation 'androidx.appcompat:appcompat:1.3.1'
implementation 'com.google.android.material:material:1.4.0'
implementation 'androidx.constraintlayout:constraintlayout:2.1.0'
implementation group: 'org.hsqldb', name: 'hsqldb', version: '2.4.1'
// Database
implementation fileTree(dir: 'libs', include: ['*.jar'])
implementation 'org.xerial:sqlite-jdbc:3.49.1.0'
implementation "androidx.room:room-runtime:2.5.2"
testImplementation 'androidx.test:core:1.4.0'
annotationProcessor "androidx.room:room-compiler:2.5.2"
// Testing
testImplementation 'junit:junit:4.13.2'
testImplementation 'org.mockito:mockito-core:3.12.4'
androidTestImplementation 'androidx.test.ext:junit:1.1.3'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
androidTestImplementation 'org.mockito:mockito-android:3.12.4'
implementation 'org.xerial:sqlite-jdbc:3.30.1'
```

## Development Environment

- **Android Studio Version:**  
  Android Studio Ladybug Feature Drop | 2024.2.2  
  Build #AI-242.23726.103.2422.12816248, built on December 17, 2024

- **Runtime Version:**  
  OpenJDK Runtime version: 21.0.4+-12508038-b607.1 (64-bit)

- **Java Version:**  
  Java(TM) SE Runtime Environment (build 17.0.10+11-LTS-240)  
  Java HotSpot(TM) 64-Bit Server VM (build 17.0.10+11-LTS-240, mixed mode, sharing)

## Contributors

- **ablepacifst** (@ablepacifist)

## Project Status & Future Plans

AlchemyApp is actively under development. Future updates will include:

- **Turn Timer:**  
  A feature to track the expiration of potion effects—adding an extra layer of strategy during gameplay.
- Continuous improvements to the brewing mechanics and inventory management system.

*Note:* Since data is stored locally, please continue to maintain physical notes of your potion recipes, levels, and ingredients.

## Test

Automated tests are integrated into the project. You can find them in the following directories:

- **Unit Tests:** `app/src/test/java/alchemy/srsys/tests`
- **Integration Tests:** `app/src/test/java/alchemy/srsys/tests/integration`
- **System Tests:** `app/src/androidTest/java/alchemy/srsys/tests`
```

