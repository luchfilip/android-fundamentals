# Day 3: Build, Test & Ship

---

## Android Architecture Stack

```
┌─────────────────────────────────────┐
│           Your App (Hoarder)        │  ← You are here
├─────────────────────────────────────┤
│         Android Framework           │  ← Activity, View, Content Providers
├─────────────────────────────────────┤
│     Android Runtime (ART) + Libs    │  ← Runs your code
├─────────────────────────────────────┤
│      Hardware Abstraction Layer     │  ← Camera, Bluetooth, etc.
├─────────────────────────────────────┤
│           Linux Kernel              │  ← Memory, processes, drivers
└─────────────────────────────────────┘
```

---

## Kotlin → Machine Code

```
Kotlin Source → Kotlin Compiler → Java Bytecode → D8/R8 → DEX → [APK] → ART → Machine Code
                                                           ↑
                                                      Shipped to user
```

---

## D8 vs R8

```
┌─────────────────────────────────────────────────────────────────┐
│                        D8 (Dexer)                               │
│  • Converts .class → .dex                                       │
│  • Desugaring (Java 8+ on old Android)                          │
│  • Always runs (debug + release)                                │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                     R8 (Shrinker + D8)                          │
│  • Everything D8 does, PLUS:                                    │
│  • Shrinking (removes unused code)                              │
│  • Obfuscation (renames to a, b, c...)                          │
│  • Optimization (inlines, removes dead code)                    │
│  • Only when isMinifyEnabled = true                             │
└─────────────────────────────────────────────────────────────────┘
```

---

## ART Compilation (Android 7.0+)

```
┌─────────────────────────────────────────────────────────────────┐
│  Hybrid: AOT + JIT + Profile-Guided Optimization                │
│  ─────────────────────────────────────────────────              │
│  • Install: minimal compilation (fast install)                  │
│  • First runs: JIT compiles hot code, collects profiles         │
│  • Idle/charging: AOT compiles based on profiles                │
│  • Result: best of both worlds                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Flutter vs Native Android Compilation

```
ANDROID (Native/Kotlin)
───────────────────────
Build Time        Install Time       Runtime
    │                  │                │
    ▼                  ▼                ▼
 [D8/R8]            [ART]            [ART]
    │                  │                │
 Kotlin →          DEX →            JIT for
 DEX bytecode      Some AOT         hot paths


FLUTTER (Release Mode)
──────────────────────
Build Time        Install Time       Runtime
    │                  │                │
    ▼                  ▼                ▼
[Dart AOT]          [Copy]          [Execute]
    │                  │                │
 Dart →             Native →        Direct
 Native ARM64       to disk         execution
```

| Aspect | Native Android | Flutter |
|--------|----------------|---------|
| What's in APK | DEX bytecode | Native machine code |
| When → native | On device | At build time |
| APK size | Smaller | Larger |
| Runtime optimization | Yes (PGO) | No |

---

## APK Structure

```
app.apk (unzipped)
├── classes.dex          ← Compiled Kotlin code
├── resources.arsc       ← Compiled resources
├── AndroidManifest.xml  ← Binary manifest
├── res/                 ← Drawables, layouts
│   ├── drawable-hdpi/
│   ├── drawable-xhdpi/
│   └── drawable-xxhdpi/
├── lib/                 ← Native libraries (.so)
│   ├── armeabi-v7a/
│   ├── arm64-v8a/
│   └── x86_64/
├── assets/              ← Raw assets
└── META-INF/            ← Signing info
```

---

## AAB Dynamic Delivery

```
┌─────────────────────────────────────────────────────────────────┐
│                    YOUR BUILD MACHINE                           │
│  Gradle Build → app-release.aab (contains everything)          │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼ Upload
┌─────────────────────────────────────────────────────────────────┐
│                    GOOGLE PLAY STORE                            │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                   Dynamic Delivery                       │   │
│  │  AAB → Split into optimized APKs per device config      │   │
│  │                                                          │   │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐   │   │
│  │  │ Pixel 8  │ │ Galaxy   │ │ Emulator │ │ Old      │   │   │
│  │  │ arm64    │ │ arm64    │ │ x86_64   │ │ armeabi  │   │   │
│  │  │ xxhdpi   │ │ xhdpi    │ │ xxhdpi   │ │ hdpi     │   │   │
│  │  │ 18MB     │ │ 16MB     │ │ 20MB     │ │ 12MB     │   │   │
│  │  └──────────┘ └──────────┘ └──────────┘ └──────────┘   │   │
│  └─────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼ Download (only what device needs)
┌─────────────────────────────────────────────────────────────────┐
│                    USER'S DEVICE                                │
│  Receives: Base APK + Config APKs = ~15-20MB instead of 50MB   │
└─────────────────────────────────────────────────────────────────┘
```

---

## Split APK Types

| Split Type | Contents |
|------------|----------|
| **Base APK** | Code, core resources, manifest (always installed) |
| **ABI Split** | Native libs for ONE architecture (arm64-v8a) |
| **Density Split** | Drawables for ONE screen density (xxhdpi) |
| **Language Split** | Strings for ONE language (en) |

---

## AAB Size Savings

| App Type | Universal APK | AAB (per device) | Savings |
|----------|---------------|------------------|---------|
| Simple native | 15MB | 8MB | 47% |
| Complex native | 80MB | 35MB | 56% |
| Flutter app | 25MB | 18MB | 28% |
| Game with assets | 150MB | 60MB | 60% |

---

## SDK Versions

```kotlin
android {
    compileSdk = 36   // Which SDK to compile against (use latest)
    
    defaultConfig {
        minSdk = 28       // Minimum Android version (API 28 = Android 9)
        targetSdk = 36    // Tested against (affects runtime behavior)
    }
}
```

---

## Build Types

```kotlin
buildTypes {
    debug {
        isDebuggable = true
        applicationIdSuffix = ".debug"
    }
    release {
        isMinifyEnabled = true      // Enable R8
        isShrinkResources = true    // Remove unused resources
        proguardFiles(...)
    }
}
```

---

## Kotlin Multiplatform (KMP)

```
┌─────────────────────────────────────────────────────────┐
│                    Shared Kotlin Code                    │
│         (Business logic, data models, networking)        │
├─────────────────┬─────────────────┬─────────────────────┤
│   Android App   │    iOS App      │   Desktop/Web/...   │
│   (Kotlin UI)   │   (Swift UI)    │                     │
└─────────────────┴─────────────────┴─────────────────────┘
```

| Aspect | Flutter | KMP |
|--------|---------|-----|
| UI | Cross-platform widgets | Native per platform |
| Language | Dart | Kotlin + Swift |
| Shared code | Everything | Business logic only |
| Look & feel | Same everywhere | Native per platform |

---

## Testing Pyramid

```
        /\
       /  \      UI Tests (few, slow, flaky)
      /----\
     /      \    Integration Tests (some)
    /--------\
   /          \  Unit Tests (many, fast, reliable)
  --------------
```

---

## Profiler Quick Reference

| Tool | Access | Use For |
|------|--------|---------|
| **Memory Profiler** | Profiler → Memory | Find leaks, force GC, heap dump |
| **CPU Profiler** | Profiler → CPU | Find main thread blocking |
| **Layout Inspector** | Tool Windows → Layout Inspector | Recomposition counts |

---

## Garbage Collection

```
┌─────────────────────────────────────────────────────────────────┐
│  Flutter GC                    │  ART GC                        │
│  ─────────────────────────     │  ─────────────────────         │
│  • Generational GC             │  • Concurrent Copying GC       │
│  • Very short pauses (~1-2ms)  │  • Occasional pauses (~5-10ms) │
│  • Rarely noticeable           │  • Can cause dropped frames    │
└─────────────────────────────────────────────────────────────────┘
```

---
