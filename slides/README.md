
# Reuse features in Android applications

> An introduction to component modularity

## Introduction

### What to reuse?

- mainly focused on UI concerns
- independent, reusable and isolated
- unit of code to compile (i.e., Android Studio module)
- Almost like [React Component](https://reactjs.org/docs/react-component.html)

![module](https://github.com/RoRoche/kAndroidModular/raw/master/slides/assets/android_module.png)

###  Background: key concepts

#### Activity (since API level 1)

> one of the fundamental building blocks
>
> <https://developer.android.com/guide/components/activities/index.html>

- behind every screen stands a single `Activity`

#### Fragment (since API level 11)

> portion of user interface in an `Activity`
> 
> <https://android-developers.googleblog.com/2011/02/android-30-fragments-api.html>

![Fragments](https://developer.android.com/images/fundamentals/fragments.png)


#### ViewModel (since ACC)

> The `ViewModel` class is designed to store and manage UI-related data in a lifecycle conscious way. 
> The `ViewModel` class allows data to survive configuration changes such as screen rotations.
>
> <https://developer.android.com/topic/libraries/architecture/viewmodel.html>

![ViewModel lifecycle](https://developer.android.com/images/topic/libraries/architecture/viewmodel-lifecycle.png)

```kotlin
fun onCreate(savedInstanceState: Bundle) {
    // Create a ViewModel the first time the system calls an activity's onCreate() method.
    // Re-created activities receive the same MyViewModel instance created by the first activity.
    val viewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)
}
```

## Native solutions

### Activity + result code

> [Getting a Result from an Activity](https://developer.android.com/training/basics/intents/result.html)

#### Example

- First activity: fill a form to create a new operation
- Second activity: select a capable machine
- With the help of [Anko](https://github.com/Kotlin/anko)

![Anko](https://github.com/RoRoche/kAndroidModular/raw/master/slides/assets/anko.png)

```
sequenceDiagram
  user-->>CreateOperationActivity: click on select machine button
    CreateOperationActivity->>SelectMachineActivity: startActivityForResult
activate SelectMachineActivity
user-->>SelectMachineActivity: select a machine
    SelectMachineActivity->>CreateOperationActivity: machineId
deactivate SelectMachineActivity
```

![Start activity for result](https://github.com/RoRoche/kAndroidModular/raw/master/slides/assets/start_activity_for_result.png)

- `CreateOperationActivity`:

```kotlin
findViewById<Button>(R.id.button_select_machine).setOnClickListener {
    startActivityForResult<SelectMachineActivity>(
        SELECT_MACHINE,
        SelectMachineActivity.CAPABILITY to typeOfOperation
    )
}
```

- `SelectMachineActivity`:

```kotlin
class SelectMachineActivity : AppCompatActivity() {

    findViewById<Button>(R.id.select_machine_a).setOnClickListener {
        val intent = Intent()
        intent.putExtra(MACHINE_ID, 1L)
        setResult(
            Activity.RESULT_OK,
            intent
        )
        finish()
    }

    companion object Params {
        val CAPABILITY = "SelectMachineActivity:capability"
        val MACHINE_ID = "SelectMachineActivity:machineId"
    }
}
```

- `CreateOperationActivity`:

```kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode == SELECT_MACHINE) {
        if (resultCode == Activity.RESULT_OK) {
            selectedMachineId = data?.getLongExtra(SelectMachineActivity.MACHINE_ID, -1)
        }
    } else {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
```

- pros : 
  - stable
  - many libraries written this way
- cons : 
  - not composable (1 activity per screen)
  - break the code flow ([rx to the rescue](https://github.com/VictorAlbertos/RxActivityResult))

### Fragment + callbacks

> [Communicating with Other Fragments](https://developer.android.com/training/basics/fragments/communicating.html)

- The embedded `Fragment` defines a callback interface

```kotlin
class SelectMachineFragment : Fragment() {
    interface OnFragmentInteractionListener {
        fun onSelectedMachine(selectedMachineId: Long)
    }
}
```

- The `Activity` must implement this callback

```kotlin
class CreateOperationActivity : AppCompatActivity(), SelectMachineFragment.OnFragmentInteractionListener {
    override fun onSelectedMachine(selectedMachineId: Long) {
        this.selectedMachineId = selectedMachineId
    }
}
```

- The `Fragment` handles a reference to its callback

```kotlin
class SelectMachineFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
```

- The `Fragment` uses the callback interface to deliver the event to the parent activity

```kotlin
override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_select_machine, container, false)
    view.findViewById<Button>(R.id.select_machine_a).setOnClickListener {
        listener?.onSelectedMachine(1L)
    }
    return view
}
```

- The `Activity` can deliver a message to another `Fragment`:

```kotlin
class AnotherFragment : Fragment() {
    fun updateUi(selectedMachineId: Long) {
        TODO("update UI with selectedMachineId")
    }
}
```

```kotlin
class CreateOperationActivity : AppCompatActivity(), SelectMachineFragment.OnFragmentInteractionListener {
    override fun onSelectedMachine(selectedMachineId: Long) {
        val anotherFragment = supportFragmentManager.findFragmentById(R.id.another_fragment_container_id) as AnotherFragment
        if (anotherFragment == null) {
            anotherFragment.updateUi(selectedMachineId)
        } else {
            TODO("create and display AnotherFragment with selectedMachineId")
        }
    }
}
```

- pros : 
  - composable
  - now compatible with the ACC [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel.html)
- cons : 
  - boilerplate code
  - no compile-time checking

### Gradle product flavors

> [Configure Build Variants](https://developer.android.com/studio/build/build-variants.html)

```groovy
productFlavors {
    application1 {
        applicationId "fr.romain.application1"
    }
    application2 {
        applicationId "fr.romain.application2"
    }
}
```

- Pros:
  - few code
  - easy to extend styles
- Cons:
  - one single project
  - applications must be very similar

 ### Assessments

 - pros : 
   - native solutions are possible
 - cons : 
   - difficult to setup
   - difficult to compose
   - no navigation concerns

## Introduce a finite state machine (FSM)

### Foreword: key concepts

- states, events, etc.
- application as a FSM

### Setup with Android components

- `Fragment` to define a state of the application (i.e., a use case) and output event(s)
- `Activity` to manage states and how to navigate (i.e., the flow of events to change application state)

### Specific cases

#### Orientation changes

- Use of the ACC [`ViewModel`](https://developer.android.com/topic/libraries/architecture/viewmodel.html) 
- Define and share a specific [`ViewModel`]  between `Fragment`s

#### Dependency injection

- The hard case of [Dagger 2](https://google.github.io/dagger/)
  - Pros: code generation, hosted by Google
  - Cons: many concepts to know and huge amount of code to write
- A nice way with [Koin](https://github.com/Ekito/koin)

### Final MVVM architecture

![final architecture](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)

## Conclusion

### Benefits

- relevant MVVM architecture opportunities with [AAC](https://developer.android.com/topic/libraries/architecture/index.html) and [Data Binding Library](https://developer.android.com/topic/libraries/data-binding/index.html)
- power of the Kotlin language
- an elegant way to define the application flow
- no explicit coupling between screens
- increase testability
  - test at module level
  - test at application level
- adjustable to technical stack

### Main used Kotlin concepts

- [Extensions (functions, properties)](https://kotlinlang.org/docs/reference/extensions.html)
- [Object declarations](https://kotlinlang.org/docs/reference/object-declarations.html#object-declarations)
- [Delegated Properties](https://kotlinlang.org/docs/reference/delegated-properties.html)
- [Data classes](https://kotlinlang.org/docs/reference/data-classes.html)
- [Default and named arguments](https://kotlinlang.org/docs/reference/functions.html)

![kotlin-android-developers](https://github.com/RoRoche/kAndroidModular/raw/master/slides/assets/kotlin-android-developers.png)

### What's next?

#### Practical

- Syntax enhancement thanks to Kotlin
- Group redundant concerns in Java/Android libraries
- Android modules
  - expose feature through a repository

#### Ideal

- front-end with drag&drop feature to build application flow?
- Kotlin: build iOS application and share common modules?
- react-native: write and share common modules (mobile and desktop)?

## Thanks

- [Macoscope](http://macoscope.com/blog/) for many relevant articles
  - [Applications as State Machines](http://macoscope.com/blog/applications-as-state-machines/)
  - [Introducing SwiftyStateMachine](http://macoscope.com/blog/swifty-state-machine/)
- [Nicolas Chassagneux](https://github.com/NicoChacha) for many enriching discussions
