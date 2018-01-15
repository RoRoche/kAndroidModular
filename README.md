# kAndroidModular

[AndroidModularSample](https://github.com/RoRoche/AndroidModularSample) reloaded and rewritten in Kotlin!

## Use cases

- Screen 1: type a user
- Screen 2: load and display GitHub repos for this user

## Technical stack

- MVVM architecture with [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/index.html) and [Data Binding](https://developer.android.com/topic/libraries/data-binding/index.html)
- REST layer with [retrofit](http://square.github.io/retrofit/) + [rx-android](https://github.com/ReactiveX/RxAndroid)
- DI with [Koin](https://github.com/Ekito/koin) (validated via its DryRun test)

## TODOs

- [X] use of FSM library [EasyFlow](https://github.com/Beh01der/EasyFlow)
- [X] check orientation changes
- [X] build repos screen with RecyclerView + Adapter
- [X] global unit test with mocked DI-components
- [ ] split into modules
- [ ] unit test each module
