# Android Fundamentals Training

A 3-day hands-on training program for learning Android development fundamentals.

**Repo:** https://github.com/luchfilip/android-fundamentals
**App:** Hoarder - a bookmark manager

---

## Training Approach

**Learn by Building:** Build a working bookmark management app throughout the 3 days, learning Android concepts as we implement features.

### The App: Bookmark Manager

- Save text/URLs shared from any app (via system share sheet)
- Display bookmarks in a list
- View bookmark details with delete confirmation
- Persist data locally with SharedPreferences
- Build, sign, and distribute AAB/APK

---

## DAY 1: Foundation & First Working Screen

1. **Android Studio Setup & Repo Clone**
    - Clone pre-configured project
    - Project structure walkthrough (Activity, Fragment, nav graph)
2. **Kotlin Essentials** *(practical examples with exercises)*
    - Variables, functions, lambdas
    - Null safety
    - Data classes
    - Extension functions
    - Scope functions (let, apply)
3. **Activities, Fragments & Lifecycle** (basics)
4. **Compose Fundamentals**
    - @Composable functions
    - @Preview for rapid development
    - State management (remember, mutableStateOf)
    - LazyColumn for lists
    - Basic layouts (Column, Row, Box)
    - Modifiers
    - Click handling
5. **MVI Clean Architecture from the start**
    - ViewModel basics (why, lifecycle, state management)
    - Hilt DI (explain what's pre-configured)
    - Repository pattern
    - StateFlow for UI state
6. **Coroutines Basics** (as needed for ViewModel)
    - launch
    - Suspend functions
    - Basic structured concurrency
7. **Build the List Screen**
    - Create Bookmark data class
    - Display list of hardcoded bookmarks (from memory)
    - CTA to add hardcoded bookmark
    - Click handler on list items

**End State:** Working list of bookmarks with FAB that adds items

---

## DAY 2: Communication, Intents & Navigation

1. **The Android Manifest**
    - Components declaration
    - Permissions
    - Intent filters
2. **Application and Context**
    - Application class
    - Context types and usage
3. **Intents Deep Dive**
    - Explicit vs implicit intents
    - Exported vs non-exported components
    - Intent filters
    - Communicating between activities
    - Communicating with other apps
4. **Receiving Share Intent (ACTION_SEND)**
    - Configure manifest for share target
    - Handle incoming text/URLs from any app
    - Add shared content to bookmark list
5. **Store to Shared Prefs**
    - Create repo
    - Create data models
    - Serialize/deserialize
    - Store/retrieve
6. **Polish the List**
    - Bottom sheet for delete confirmation
    - Loading and error states
7. **Jetpack Navigation Component**
    - Fragment navigation setup
    - Navigate to detail screen
    - Pass bookmark data between fragments
    - Navigation callbacks from Compose
8. **Detail Screen in Compose**
    - Show bookmark details
    - Display saved text/URL
    - Edit functionality
9. **Advanced Coroutines & Flow**
    - StateFlow vs SharedFlow
    - Flow operators (map, filter, combine)
    - Structured concurrency patterns
    - Error handling in coroutines

**End State:** Can share text from any app → saves to list → click to view details → delete with confirmation

---

## DAY 3: Data Persistence, Build & Ship, Testing & Optimization

Main focus is on how to distribute the app and how to prep for production with real world examples. Less building, more high level coverage of concepts.

1. **Android System Overview**
    - Architecture layers (Native Libraries, ART, Framework, Apps)
    - Android Runtime (ART)
    - How Android apps run
2. **APK vs AAB (Android App Bundle)**
    - What's inside APK: DEX files, resources, manifest, native libs, assets
    - What's inside AAB: Base module, feature modules, split APKs
    - How to build, install, test, and share
    - Why AAB is required for Play Store
    - Dynamic delivery concepts (high-level)
    - Size optimization with AAB
3. **Gradle Build System**
    - Project-level vs module-level build.gradle
    - Dependencies management
    - compileSdk, minSdk, targetSdk explained
    - Build types (debug, release), Flavors, Build variants (high level)
4. **Kotlin Multiplatform (KMP) Overview**
    - What is KMP and why it matters
    - Sharing code between platforms
    - High level KMP on Android vs iOS
    - KMP architecture overview
5. **Testing**
    - Unit testing ViewModels and Repository
    - Testing coroutines and Flow
    - Compose UI testing basics
    - Testing user interactions
6. **Performance & Optimization**
    - Memory leak detection and prevention
    - Profiling tools (CPU, memory, network)
    - Common performance pitfalls
    - Build optimization techniques
    - ProGuard/R8 for code shrinking
    - APK/AAB size optimization
7. **Signing & Publishing**
    - Generate signed AAB (terminal and Android Studio)
    - Keystore management
    - Firebase App Distribution
    - Play Store release basics
    - Release checklist

**End State:** Complete bookmark app with persistence, some testing and high level distribution

---

# Training Materials

---

## Day 1: Foundation & First Working Screen

### Setup
1. Clone repo, open in Android Studio
2. Wait for Gradle sync
3. **Disable AI Assistant** (Cmd+Shift+A → disable)
4. Run the app

---

### Kotlin Essentials

**val vs var**
```kotlin
val name = "Filip"    // Dart: final
var age = 30          // Dart: var
```

**Functions**
```kotlin
fun greet(name: String) = "Hello, $name"
```
```dart
String greet(String name) => 'Hello, $name';
```

**Trailing lambda** - curly braces outside parentheses
```kotlin
numbers.map { it * 2 }
```
```dart
numbers.map((n) => n * 2);
```

**Null safety** - identical to Dart
```kotlin
val len = name?.length ?: 0
```

**Data class** - one line gives you equals, hashCode, toString, copy
```kotlin
data class Person(val name: String, val age: Int)
```

**Extension functions** - add methods to existing classes
```kotlin
fun String.isValidEmail() = contains("@")
```

**Scope functions** - `let` and `apply` most common
```kotlin
name?.let { println(it) }
person.apply { age = 30 }
```

#### Exercises
| # | Task | File |
|---|------|------|
| 1 | Create `val` and `var`, try changing both | `KotlinPlayground.kt` |
| 2 | Use `?.` and `?:` with nullable string | `KotlinPlayground.kt` |
| 3 | Add `createdAt: Long` field to Bookmark | `Bookmark.kt` |
| 4 | Add `isValidUrl()` extension function | `Bookmark.kt` |
| 5 | Use `let` and `apply` with bookmark | `KotlinPlayground.kt` |

---

### Activities, Fragments & Lifecycle

**Activity** = Screen/Window. Single Activity architecture with Compose.

**Lifecycle:**
- `onCreate()` → like `initState()` in Flutter
- `onStart()` → Visible
- `onResume()` → Interactive
- `onPause()` → Losing focus
- `onStop()` → No longer visible
- `onDestroy()` → Destroyed

**Fragments:** Containers for Compose screens. Used with navigation.

---

### Compose Basics

**@Composable** - like Flutter's build()
```kotlin
@Composable
fun MyText() {
    Text("Hello")
}
```

**@Preview** - see UI without running app
```kotlin
@Preview(showBackground = true)
@Composable
fun MyTextPreview() {
    MyText()
}
```

**State** - `remember` survives recomposition
```kotlin
var count by remember { mutableStateOf(0) }
```

**What's `by`?** - unwraps .value so you write `count` instead of `count.value`

---

### Layouts

| Compose | Flutter |
|---------|---------|
| Column | Column |
| Row | Row |
| Box | Stack |
| LazyColumn | ListView.builder |

**Modifiers** - order matters!
```kotlin
Modifier.padding(16.dp).background(Color.Gray)  // padding inside
Modifier.background(Color.Gray).padding(16.dp)  // padding outside
```

#### Exercises
| # | Task | File |
|---|------|------|
| 6 | Change TopAppBar title | `HomeScreen.kt` |
| 7 | Add counter to FAB using `remember` | `HomeScreen.kt` |
| 8 | Add `.border()` to Card, observe order | `HomeScreen.kt` |
| 9 | Implement `EmptyState` composable | `HomeScreen.kt` |

---

### MVI Architecture

```
User Click → Action → ViewModel → State → UI
```

**StateFlow** - observable state holder
```kotlin
private val _uiState = MutableStateFlow(HomeUiState())
val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
```

**sealed interface** - compiler enforces exhaustive handling
```kotlin
sealed interface HomeAction {
    data object LoadBookmarks : HomeAction
    data class AddBookmark(val bookmark: Bookmark) : HomeAction
}
```

**Coroutines** - `suspend` = `async`, scope handles cancellation
```kotlin
viewModelScope.launch {
    val data = repository.getData()
}
```

---

### Repository Pattern

**ViewModel doesn't care where data comes from.**

```kotlin
interface BookmarkRepository {
    suspend fun getBookmarks(): List<Bookmark>
    suspend fun addBookmark(bookmark: Bookmark)
    suspend fun deleteBookmark(bookmarkId: String)
}
```

Today: in-memory list. Tomorrow: SharedPreferences. ViewModel code doesn't change.

---

### Hilt Dependency Injection

**Hilt wires everything automatically.**

```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: BookmarkRepository
) : ViewModel()
```

`@Inject` = 'give me this dependency'

Already set up in `di/AppModule.kt`. Follow the pattern and it works.

#### Exercises
| # | Task | File |
|---|------|------|
| 10 | Add `itemCount` to state, show in title | `HomeViewModel.kt`, `HomeScreen.kt` |
| 11 | Add `ClearAll` action | `HomeViewModel.kt` |
| 12 | Implement FAB onClick to add bookmark | `HomeScreen.kt` |
| 13 | Trace delete flow through code | `HomeViewModel.kt` |
| 14 | Polish list UI (dividers, colors, elevation) | `HomeScreen.kt` |
| 15 | Trace navigation flow | `NavGraph.kt`, `HomeViewModel.kt` |

---

## Day 2: Intents & Persistence

### Android Manifest

Declares: activities, permissions, intent filters

**Intent** - message to Android

| Type | Use |
|------|-----|
| Explicit | Open specific activity |
| Implicit | "Someone handle this URL" |

**Intent Filter** - "I can handle this type of intent"
```xml
<intent-filter>
    <action android:name="android.intent.action.SEND" />
    <data android:mimeType="text/plain" />
</intent-filter>
```

#### Exercises
| # | Task | File |
|---|------|------|
| 1 | Add internet permission | `AndroidManifest.xml` |
| 2 | Log app start | `HoarderApp.kt` |
| 3 | Add "Open in Browser" button | `DetailScreen.kt` |
| 4 | Check exported status | `AndroidManifest.xml` |

---

### Application & Context

**Application** = singleton, lives for entire app lifecycle
```kotlin
@HiltAndroidApp
class HoarderApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // App-wide initialization
    }
}
```

**Context types:**
- `applicationContext` = app-wide, safe to hold
- Activity/Fragment context = UI-specific, can leak memory

Rule: Use `applicationContext` when you don't need UI stuff.

---

### Share Intent

```
Chrome → Share → Intent → MainActivity → ShareHandler → ViewModel → UI
```

#### Exercises
| # | Task | File |
|---|------|------|
| 5 | Add share intent filter | `AndroidManifest.xml` |
| 6 | Create `ShareHandler` singleton | `ShareHandler.kt` (new) |
| 7 | Handle intent in MainActivity | `MainActivity.kt` |
| 8 | Observe ShareHandler, create bookmark | `HomeViewModel.kt` |
| 9 | Handle edge cases (non-URL, duplicates) | `HomeViewModel.kt` |

---

### Persistence

**SharedPreferences** - simple key-value storage
```kotlin
prefs.edit().putString("key", value).apply()
```

**apply() vs commit()** - async vs sync

#### Exercises
| # | Task | File |
|---|------|------|
| 10 | Add SharedPreferences + Gson persistence | `BookmarkRepositoryImpl.kt` |
| 11 | Add loading state with spinner | `HomeScreen.kt` |
| 12 | Add error state with retry | `HomeScreen.kt` |

---

### Jetpack Navigation

**Type-safe routes** - no string-based navigation
```kotlin
@Serializable
object Home

@Serializable
data class Detail(val bookmarkId: String)
```

**Events via SharedFlow** for navigation
```kotlin
private val _events = MutableSharedFlow<HomeEvent>()
val events: SharedFlow<HomeEvent> = _events.asSharedFlow()
```

ViewModel emits events → Screen observes and navigates

---

### Detail Screen

Same MVI pattern:
- `DetailUiState` holds state
- `DetailAction` defines user actions
- `DetailViewModel` handles logic
- `DetailScreen` observes and renders

Get bookmark ID from `SavedStateHandle`:
```kotlin
private val bookmarkId: String = checkNotNull(savedStateHandle["bookmarkId"])
```

---

### Advanced Coroutines & Flow

**StateFlow vs SharedFlow**

| StateFlow | SharedFlow |
|-----------|------------|
| Always has value | No initial value |
| Replays latest | One-time events |
| UI state | Navigation events |

**Common Flow operators:**
- `map` - transform each emission
- `filter` - only emit if condition true
- `catch` - handle errors
- `combine` - combine multiple flows
- `debounce` - wait for pause in emissions

**Structured concurrency** - scopes handle cancellation
```kotlin
viewModelScope.launch {
    // Cancelled when ViewModel is destroyed
}
```

**Error handling:**
```kotlin
try {
    val data = repository.getData()
    _uiState.update { it.copy(data = data) }
} catch (e: Exception) {
    _uiState.update { it.copy(error = e.message) }
}
```

#### Exercises
| # | Task | File |
|---|------|------|
| 13 | Add search with debounce | `HomeViewModel.kt`, `HomeScreen.kt` |
| 14 | Polish detail screen | `DetailScreen.kt` |

---

## Day 3: Build, Test & Ship

### Android System

```
Your App
───────────
Framework (Activity, Compose)
───────────
ART (runs your code)
───────────
Linux Kernel
```

**Compilation:** Kotlin → Java Bytecode → DEX → Native Code

#### Exercises
| # | Task | Where |
|---|------|-------|
| 1 | Unzip APK, explore contents | `app/build/outputs/apk/debug/` |

---

### APK vs AAB

**APK problem** - contains all densities, all architectures

**AAB solution** - Play Store generates optimized APKs per device

#### Exercises
| # | Task | Where |
|---|------|-------|
| 2 | Build APK and AAB, compare sizes | Build menu |

---

### Gradle

**SDK Versions**

| Version | Meaning |
|---------|---------|
| compileSdk | Which SDK to compile against (latest) |
| targetSdk | Which version you tested against |
| minSdk | Minimum Android version required |

**Build Types** - debug vs release

#### Exercises
| # | Task | File |
|---|------|------|
| 3 | Add `BuildConfig.BUILD_TIME`, log it | `build.gradle.kts`, `MainActivity.kt` |

---

### Kotlin Multiplatform (KMP)

**Share Kotlin code across platforms** - business logic, not UI

```
┌────────────────────────────────────────┐
│         Shared Kotlin Code             │
│  (Business logic, data, networking)    │
├──────────────┬─────────────────────────┤
│ Android App  │       iOS App           │
│ (Compose UI) │     (SwiftUI)           │
└──────────────┴─────────────────────────┘
```

**KMP vs Flutter:**
- Flutter: One codebase, one UI framework
- KMP: Shared logic, native UI per platform

**expect/actual pattern:**
```kotlin
// commonMain
expect fun getPlatformName(): String

// androidMain
actual fun getPlatformName() = "Android"

// iosMain
actual fun getPlatformName() = "iOS"
```

---

### Testing

**Testing pyramid** - many unit tests, few UI tests

**ViewModel testing** - FakeRepository + MainDispatcherRule

**Testing coroutines:**
```kotlin
@Test
fun `test with coroutines`() = runTest {
    viewModel.doSomethingAsync()
    advanceUntilIdle()  // Wait for all coroutines
    // Assert
}
```

**Compose UI testing:**
```kotlin
@get:Rule
val composeRule = createComposeRule()

@Test
fun emptyState_showsMessage() {
    composeRule.onNodeWithText("No bookmarks yet").assertIsDisplayed()
}
```

#### Exercises
| # | Task | File |
|---|------|------|
| 4 | Create `FakeBookmarkRepository` | `test/.../FakeBookmarkRepository.kt` |
| 5 | Write ViewModel tests | `test/.../HomeViewModelTest.kt` |

---

### Performance & Optimization

**Common issues:**
- Main thread blocking - use `Dispatchers.IO` for I/O
- Memory leaks - don't hold Activity references
- Unnecessary recomposition - use stable types
- Large images - load appropriate size

**Memory leak detection:**
- LeakCanary - automatic detection in debug builds
- Android Profiler - View → Tool Windows → Profiler

**Profiling tools:**
- CPU Profiler - find slow methods
- Memory Profiler - track allocations
- Network Profiler - inspect requests

**R8** - shrinks, obfuscates, optimizes
```kotlin
buildTypes {
    release {
        isMinifyEnabled = true
        isShrinkResources = true
    }
}
```

#### Exercises
| # | Task | File |
|---|------|------|
| 6 | Compare APK size with/without minify | `build.gradle.kts` |

---

### Signing & Publishing

**Keystore** - never lose your release keystore (can't update app without it!)

**Distribution options:**
| Method | Use Case |
|--------|----------|
| Play Store | Production (requires AAB) |
| Firebase App Distribution | Beta testing |
| Direct APK | Internal tools, sideloading |

**Firebase App Distribution:**
```bash
firebase appdistribution:distribute app-release.apk \
    --app YOUR_APP_ID \
    --groups "testers"
```


#### Exercises
| # | Task | Where |
|---|------|-------|
| 7 | Generate release keystore | Build → Generate Signed Bundle |
| 8 | Build signed APK | Build → Generate Signed Bundle |
| 9 | Install on device, test share intent | `adb install` or drag to emulator |

---

## Quick Reference

| Kotlin | Dart |
|--------|------|
| `val` | `final` |
| `var` | `var` |
| `?:` | `??` |
| `?.` | `?.` |
| `it` | implicit param |
| `data class` | class + boilerplate |
| `suspend` | `async` |
| `launch { }` | no equivalent |

| Compose | Flutter |
|---------|---------|
| `@Composable` | `build()` |
| `remember { }` | lives in State object |
| `Modifier` | decoration/padding widgets |
| `LazyColumn` | `ListView.builder` |
| `StateFlow` | `StreamBuilder` |
