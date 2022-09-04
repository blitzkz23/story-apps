# Dico-toori (Dicoding Sutoori)
Dico-toori is a submission project of Dicoding's "Belajar Pengembangan Aplikasi Android Intermediate" class.  In this application user could read stories 
regarding study experience, success story, or anything related to Dicoding.  API data used in this project provided by Dicoding itself <a href="https://story-api.dicoding.dev/v1/#/?id=dicoding-story">Dicoding Story API</a>.

## Architecture:
Clean Code Architecture with MVVM Pattern

## Tech Stack:
- Modulatization (core and app module), to separete business logic and UI.
- Retrofit2, for networking with API.
- Room DB, for data caching.
- Datastore Shared Preferences, for saving user session.
- Kotlin Coroutines Flow, for reactive progamming data flow.
- Dagger-Hilt Dependency Injection, for dependency injection.
- Camera X, used to take a picture and upload story in one of the API endpoint which require Multipart file.

## Feature:
- User authentication that connect to API
- Guest login
- Read stories data that are provided by API (logged in user)
- Post new story (logged in user and guest)

## License and Attributions:
<a href="https://storyset.com/people">People illustrations by Storyset</a>
