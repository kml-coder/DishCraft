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

- **Android Studio** (recommended) or at least **Android emulator** + **Command line tools**  
- (Optional) **Java JDK 17** (required for the Gradle build)  
- (Optional) **Gradle Wrapper** (already included in repo, no need to install separately)  
- (Optional) **ADB** – Android Debug Bridge (comes with SDK `platform-tools`)  

---

## 🛠 Setup Instructions (Using Docker)

### 1. Clone the repository
```bash
git clone https://github.com/kml-coder/DishCraft.git
cd DishCraft
```

### 2. Build the APK
(Mac Silicon)
```bash
docker buildx build --platform=linux/amd64 -t dishcraft_builder . # build image
docker run -it --name dishcraft_builder dishcraft_builder # run the container (which will start building apk too)
```
(Other OS)
```bash
docker-compose up --build
```

If successful, the APK will be generated here: workspace/app/build/outputs/apk/debug/app-debug.apk



### 3. Move the APK to local
```bash
docker cp dishcraft_builder_run:/workspace/app/build/outputs/apk/debug/app-debug.apk ./app-debug.apk
exit

docker rm dishcraft_builder_run # remove container
docker-compose down -v # remove everything (container, volume, service)
```

---

## 🛠 Setup Instructions (local)

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

### 1. On a real Android device
1. Enable **Developer Options** and **USB Debugging** on your phone  
2. Connect via USB  
3. Install APK:
   ```bash
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```
### 2. On a emulator
1. Make sure your **ANDROID_HOME** is in the right location if not:
   ```bash
   export ANDROID_HOME=$HOME/Library/Android/sdk
   export PATH=$ANDROID_HOME/emulator:$ANDROID_HOME/tools:$ANDROID_HOME/tools/bin:$ANDROID_HOME/platform-tools:$PATH
   ```
2. Check what emulator you have, and select one and run it:
   ```bash
   emulator -list-avds
   emulator -avd <your_avd_name>
   ```
   When it runs, turn on another terminal, and move to DishCraft/app/build/outputs/apk/debug
   ```bash
   cd app/build/outputs/apk/debug
   ~/Library/Android/sdk/platform-tools/adb install app-debug.apk
   ```
   it will install the app we build
4. Move to main screen and Find the App and run it
5. Boom!

### 3. Run it in Android stuido
1. Open DishCraft folder in your Android studio
2. Go to Tools->Device Manager, and add any emulator you want
3. Click the green run buttion to run the app
4. (Thumbs up)

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

 
