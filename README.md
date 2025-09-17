# DishCraft 🍕

DishCraft is a prototype Android application built with **Java / Android SDK** that simulates restaurant menu customization, ordering flows, and dashboard features.

This README explains how to **set up the environment, build the APK, and run it on a device or emulator**.

---

## 📂 Project Structure
```
DishCraft/
 ├── app/                   # Main Android app code
 ├── gradlew / gradlew.bat  # Gradle wrapper
 ├── build.gradle.kts       # Project build config
 └── settings.gradle.kts
```

---

## ⚙️ Prerequisites

Make sure you have these installed:

- **Java JDK 11** (required for the Gradle build)  
- **Android Studio** (recommended) or at least **Android SDK** + **Command line tools**  
- **Gradle Wrapper** (already included in repo, no need to install separately)  
- (Optional) **ADB** – Android Debug Bridge (comes with SDK `platform-tools`)  

---

## 🛠 Setup Instructions

### 1. Clone the repository
```bash
git clone https://github.com/kml-coder/DishCraft.git
cd DishCraft
```

### 2. Configure Android SDK path
Create a file named `local.properties` inside the project root if it doesn’t exist:

```
sdk.dir=C:\Users\<YourUsername>\AppData\Local\Android\Sdk
```

(Adjust path depending on your system.)

### 3. Build the APK
```bash
gradlew.bat build   # Windows
./gradlew build     # Mac/Linux
```

If successful, the APK will be generated here:
```
app/build/outputs/apk/debug/app-debug.apk
```

---

## ▶️ Running the App

### On a real Android device
1. Enable **Developer Options** and **USB Debugging** on your phone  
2. Connect via USB  
3. Install APK:
   ```bash
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```


## 🚧 Notes / Troubleshooting
- **Lint Errors**: If lint errors block build, you can disable abort-on-error in `build.gradle.kts`:  
  ```kotlin
  android {
      lint {
          abortOnError = false
          checkReleaseBuilds = false
      }
  }
  ```

 
