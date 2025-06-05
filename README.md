# SchumannCitywalk

An Android app to explore notable sights related to Schumann, music, and architecture in Zwickau.

## Features

- **Browse Sights:** View a list of important sights with images and descriptions.
- **Category Filter:** Filter sights by category: All, Music, or Architecture.
- **Map Integration:** See all sights on an interactive map with location markers.
- **Sight Details:** Tap a sight to view detailed information, images, and location.
- **Music Playback:** Listen to music related to each sight directly from the detail view.
- **Favorites:** Mark sights as favorites for quick access and filtering.
- **Visited/Unvisited Tracking:** Mark sights as visited or unvisited to track your exploration progress.
- **Notifications:** Receive reminders or updates about sights, such as visit suggestions or favorite confirmations.
- **Animations:** Enjoy smooth UI transitions and engaging visual effects throughout the app.

## Screenshots



## Tech Stack

- Java & Kotlin
- Android Fragments & RecyclerView
- MVVM architecture with SharedViewModel
- Google Maps integration
- MediaPlayer for music playback
- NotificationManager for notifications

## Getting Started

1. **Clone the repository:**
2. **Open in Android Studio.**
3. **Build and run on an emulator or device.**

## Project Structure

- `app/src/main/java/com/example/schumannechoes/`
  - `Sight.java` - Data model for sights
  - `SightsAdapter.java` - RecyclerView adapter for the list
  - `SharedViewModel.java` - Shared data between fragments
  - `fragments/SightsFragment.java` - Main list UI and filtering
  - `fragments/SightInfoFragment.java` - Detail view for each sight
  - `fragments/MapFragment.java` - Map view for all sights
- `app/src/main/res/layout/` - UI layouts
_Map Api key is removed

