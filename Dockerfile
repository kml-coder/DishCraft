FROM openjdk:17-jdk-slim

# Android SDK & tools install
ENV ANDROID_SDK_ROOT=/opt/android-sdk
RUN apt-get update && apt-get install -y wget unzip git gradle libc6 && \
    rm -rf /var/lib/apt/lists/* && \
    mkdir -p $ANDROID_SDK_ROOT && \
    wget https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip -O cmdline-tools.zip && \
    unzip cmdline-tools.zip -d $ANDROID_SDK_ROOT/cmdline-tools && \
    rm cmdline-tools.zip

# SDK manager path 
ENV PATH=$ANDROID_SDK_ROOT/cmdline-tools/cmdline-tools/bin:$ANDROID_SDK_ROOT/platform-tools:$PATH

# Android SDK package download
RUN yes | sdkmanager --sdk_root=${ANDROID_SDK_ROOT} --licenses
RUN sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"

# Copy Project
WORKDIR /workspace
COPY . .

# Remove local.properties if exists (user-specific SDK path 문제 방지)
RUN rm -f local.properties || true

# Gradle building
CMD ["./gradlew", "clean" "build"]
