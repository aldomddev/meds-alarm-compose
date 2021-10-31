# Meds alarm - compose

![](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![](https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white)
![GitHub repo size](https://img.shields.io/github/languages/code-size/aldomddev/meds-alarm-compose?style=for-the-badge&logo=github)

## A simple meds alarm Android app using [Jetpack Compose](https://developer.android.com/jetpack/compose)

The main purpose of this repository is to learn Jetpack Compose and evolve the application as the study progresses.

### Main functionalities

The app has 3 main screens:

- Today meds
- All meds
- Medication detail

The user is able to:

- Add a medication with name and description
- Choose the start date and time
- Choose the repeating interval in hours between predefined 4, 6, 8, 12 or custom value.
- Set the alarm as permanent or choose an end date
- Delete an alarm

All enabled alarms are rescheduled on device reboot

### Architecture and Technologies

- Clean architecture + MVVM - 3 broad layers separation: data, domain and presenter with object mappers for crossing boundaries
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Kotlin coroutines](https://developer.android.com/kotlin/coroutines)
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for dependency injection
- [Room](https://developer.android.com/training/data-storage/room) for database
- [Navigation component](https://developer.android.com/jetpack/compose/navigation) with Jetpack Compose support
- [Android Studio Arctic Fox](https://developer.android.com/studio)

### Adjustments and improvements

The project is not finished yet. The next updates will include the following main changes:

- [ ] Disable alarm option
- [ ] Open app on notification click
- [ ] Extract state management from `MedicationDetailScreen` - remove view model from parameter
- [ ] Set medication as taken and missed ones notification
- [ ] Splash screen
- [ ] Unit tests
- [ ] Update theme with final colors, typhography, etc
