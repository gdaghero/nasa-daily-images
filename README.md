Nasa Daily Images
==================

This is the repository for nasa daily images. It is entirely built with Kotlin and Jetpack Compose and it follows Android development best practices.

## Overview

The app contains the mandatory features specified in the challenge sheet:
- Daily images list screen
- Day photo list
- Photo screen
- Offline mode support
- Unit testing

## Architecture overview

The architecture is based on the official Android guide to app arhictecture https://developer.android.com/topic/architecture.
The app is divided in 3 layers, ui, domain and data and it is modularized.
The architecture follows a reactive programming model with unidirectional data flow starting at the data layer. It is achieved using streams of data, Kotlin Flows.

### Basic explanation / walk-through

When the app is first run it will attempt to load all the days information from the remote url, once loaded they will be shown to the user. If no internet connectivity is detected it will try to load from the database. Though every time the app launches it will try to refresh the days information. 

Same procedure for the detail of a day, it will try to fetch all the images for a given day, and if no connectivity is detected it will load it from the db. If no internet connectivity is detected and we have no local images it will show an error and pop the screen.

When the remote images are feteched, for each one of them a new coroutine is created on an external scope that triggers the download of a specific image. This is done in the external scope for the download job to be able to outlive the ViewModel scope and keep track of the downloads from the previous screen. Onces the downloads are being completed the images are updated locally with the local path of the image (cache directory). 
There is also download progress tracking for each image, the strategy here is to have a hashmap of `imageId` and `MutableStateFlow`, so the progress of each image download is emitted to this flow. This flow can be observed later to display the progress (This was a WIP, for the sake of the challenge it shows an undeterminate loading wheel. For it to function it'd need to be observed properly).

## Unit testing

Unit tests were created for the daily images screen (ui only). ViewModel tests and all the other related modules were not implemented, it will require mock libraries and more setup.

## Libraries used

- Jetpack compose
- Hilt (dependency injection)
- Material3 (ui, theme)
- Room (local db)
- Coil (image display)
- Retrofit (network requests)
- Kotlin Coroutines
- DateTime
- Secrets (nice .properties file parser, just to play around)
- + Other jetpack libs 
