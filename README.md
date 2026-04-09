# Weather App (Kotlin + Jetpack Compose)

Production-style sample Weather App using **Kotlin**, **Jetpack Compose (Material 3)**, and **MVVM**.

It fetches **current weather** from the **OpenWeatherMap** API and renders three distinct UI states: **Loading**, **Success**, and **Error** (with retry), plus a **refresh** action and simple **city search**.

## Features

- Current weather by city name
- Displays city, temperature (°C), description, and icon
- Loading / Success / Error UI state handling
- Retry button on failure + refresh button in the app bar
- Coroutines + `StateFlow` driven UI

## Tech Stack

- **Architecture:** MVVM (ViewModel exposes `StateFlow<WeatherUiState>`)
- **Networking:** Retrofit 2
- **JSON:** Kotlinx Serialization
- **Concurrency:** Kotlin Coroutines
- **UI:** Jetpack Compose + Material 3
- **Image loading:** Coil (weather icon)

## Project Structure

The main implementation is organized as:

- `app/src/main/java/com/example/weatherapp/data/model/WeatherResponse.kt`
- `app/src/main/java/com/example/weatherapp/data/remote/WeatherApi.kt`
- `app/src/main/java/com/example/weatherapp/data/remote/RetrofitInstance.kt`
- `app/src/main/java/com/example/weatherapp/ui/weather/WeatherViewModel.kt`
- `app/src/main/java/com/example/weatherapp/ui/weather/WeatherScreen.kt`
- `app/src/main/java/com/example/weatherapp/MainActivity.kt`

## Getting Started

### 1) Create an OpenWeatherMap API Key

Create an API key in your OpenWeatherMap account.

### 2) Add your API key

Set your key here:

- `app/src/main/java/com/example/weatherapp/data/remote/RetrofitInstance.kt`

```kotlin
const val API_KEY: String = "YOUR_API_KEY"
```

Note: For real apps, prefer storing secrets outside source control (e.g. `local.properties`, env vars, CI secrets, or encrypted storage).

### 3) Ensure Android permissions

Make sure your `AndroidManifest.xml` includes:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### 4) Gradle dependencies (reference)

If you’re integrating these files into an Android Studio project, you’ll typically need:

- Retrofit + OkHttp
- Kotlinx Serialization + Retrofit converter
- Compose Material 3
- Lifecycle ViewModel + Compose lifecycle runtime
- Coil Compose

Also ensure the Kotlin Serialization plugin is enabled:

```kotlin
plugins {
  id("org.jetbrains.kotlin.plugin.serialization")
}
```

Exact versions may vary based on your project’s Gradle/AGP setup.

## Running

From the project root (Android Studio / Gradle project):

```bash
./gradlew assembleDebug
./gradlew installDebug
```

Then launch the app on an emulator/device.

## Notes

- Default city is `"London"` (search field can be used to change it).
- Temperature units are metric (`units=metric`) so the UI displays °C.
- This repo may also contain an unrelated `calc.kt` from earlier experimentation; it’s not used by the Weather App code.

## License

MIT (or your preferred license)

