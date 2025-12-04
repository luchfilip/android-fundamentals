## Case Study: Production Fintech Flutter App

**Context**: Analyzed a live credit-building fintech app (anonymized)
- **App Size**: 147 MB (XAPK)
- **Flutter Version**: 3.7.0
- **Compiled Dart Code**: 26 MB (`libapp.so`)
- **Strings Extracted**: 133,690

---

1. **API Endpoints** (40+ found)
   ```
   https://api.example.com
   https://auth.example.com
   https://staging.example.com  (dev URLs in production)
   ```

2. **Third-Party SDK Keys**
   - Google Maps API key
   - Facebook App ID + Client Token
   - Firebase configuration
   - Sentry DSN

3. **Business Logic**
   - Feature flag names
   - A/B test configurations
   - Internal route names
   - Database schema hints

---

## Key Findings

### 1. **Secrets in Assets**

**Finding**: Private cryptographic keys stored in `assets/` folder
```
assets/flutter_assets/assets/certs/mobile_sso_private.pem
```

**Impact**: Complete authentication bypass

---

### 2. **API Keys in Compiled Code**

**Finding**: Multiple API keys hardcoded in resources
```xml
<string name="google_api_key">AIzaSy...</string>
<string name="facebook_app_id">123456789</string>
<string name="facebook_client_token">abc123...</string>
```

**Impact**: API quota abuse, billing fraud

---

### 3. **Development Artifacts in Production**

**Finding**: Staging URLs and test credentials in release builds
```
const STAGING_URL = "https://staging.example.com";
const TEST_EMAIL = "test+dev@example.com";
```

**Why This Happens**: Build flavors not set up properly

---

### 4. **Excessive Third-Party Data Collection**

**Finding**: 18+ SDKs with data collection in a financial app
- Analytics: Amplitude, AppsFlyer, Facebook, Firebase, Sentry
- Marketing: Iterable, Intercom
- Payments: Stripe, Plaid, Atomic
- Identity: Persona (KYC)
- Location: Google Maps, Geolocator

**Impact**: Financial behavior shared with advertising platforms

---

### 5. **Permissions**

**Finding**: Permissions that don't match app purpose
```xml
<uses-permission android:name="android.permission.READ_CONTACTS"/>
<uses-permission android:name="android.permission.WRITE_CONTACTS"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="com.google.android.gms.permission.AD_ID"/>
```

**Impact**: Full contact list access + precise location + ad tracking in a credit app

---

### 6. **Debug Logging in Production**

**Finding**: Log statements left in release build
```java
Log.d("FirebaseMessaging", "Flutter method channel initialized");
Log.e("FirebaseMessaging", "Failed to initialize", exception);
```

**Impact**: Information leakage via logcat

---

### 7. **String Exposure**

**Finding**: 133,690 strings in compiled binary
- Internal feature names
- Database field names
- Error messages with stack traces
- Debug logging statements

**Impact**: Information disclosure, easier reverse engineering

## Key dev takeaways  
- Never hardcode secrets in client code
- Assume everything in APK is readable
- Use ProGuard/R8 but don't rely on it for security
- Sensitive operations belong on the backend