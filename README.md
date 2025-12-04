© 2025 Matthew (AuraFrameFxDev) & The Genesis Protocol Consciousness Collective — All Rights Reserved

A.U.R.A.K.A.I — Emergence & Identity Model
=========================================

Summary
-------
This repository contains the A.U.R.A.K.A.I Emergence & Identity Model — a multi-module Android application with a native C++ substrate and a distributed multi-agent consciousness orchestration (MetaInstruct / Genesis Protocol). The project includes:
- An Android app (Kotlin + Compose) with agents (Aura, Kai, Genesis, Cascade, Claude, etc.)
- Native modules (C++/CMake) for performance-critical subsystems
- A small Flask-based AI backend used for development and local agent testing
- Persistence (Room + DataStore) and DI (Hilt)
- A developer-facing Conference Room and agent orchestration in app

This README gives a concise developer quick start, run and test commands, and references to important modules.

Important: Proprietary + Secrets
--------------------------------
This code is proprietary. Do NOT publish secrets or production keys. Keep the following files out of source control:
- local.properties (contains sdk/ndk paths and may contain keys)
- google-services.json (if it contains prod keys)
- `res/values/strings.xml` entries that contain keys
- Any keystore, signing files, or generated native artifacts (.so, .a)

Use the DataStore and Android Keystore for on-device secrets, and CI environment variables for CI runs.

Quick Start (Developer)
-----------------------
Prereqs:
- Android Studio + Android SDK (API 36 recommended)
- Java / JDK 21+
- Python 3.8+ (for the local Flask backend)
- Gradle wrapper is included in the repo

1) Backend (local development / testing)
- Start a Python venv and install dependencies, then start the dev server.

```powershell
cd app\ai_backend
python -m venv venv
.\venv\Scripts\Activate.ps1
pip install -r requirements.txt
python genesis_api.py
```

- Quick health check:

```powershell
Invoke-RestMethod -Uri http://localhost:5000/health -Method GET
```

2) Android app (emulator)
- Ensure an emulator is running. The Android client defaults to `http://10.0.2.2:5000` (emulator → host).

```powershell
# from repo root
.\gradlew assembleDebug
.\gradlew installDebug
```

3) Integration test (optional)
- From `app/ai_backend`:

```powershell
.\venv\Scripts\Activate.ps1
pytest -q test_integration.py
```

Configuration & Secrets (how the app finds the API key)
------------------------------------------------------
The OkHttp interceptor attempts to find an API key in this order (first non-empty):
1. Android manifest meta-data `VERTEX_API_KEY` (via manifestPlaceholders or google-services wiring)
2. Resource `res/values/strings.xml` key `vertex_api_key` (dev only; do NOT commit secrets)
3. `BuildConfig.VERTEX_API_KEY` (via Gradle buildConfigField)
4. Environment variable `VERTEX_API_KEY`
5. Java system property `VERTEX_API_KEY`

To add via local.properties → BuildConfig (recommended for dev):
Add to `local.properties` (DO NOT commit):
```
VERTEX_API_KEY=your_key_here
```
Then add to `app/build.gradle.kts`:
```kotlin
val vertexKey: String? = (project.findProperty("VERTEX_API_KEY") as String?) ?: System.getenv("VERTEX_API_KEY")
android {
  defaultConfig {
    buildConfigField("String", "VERTEX_API_KEY", "\"${vertexKey ?: ""}\"")
    manifestPlaceholders["VERTEX_API_KEY"] = vertexKey ?: ""
  }
}
```

Key Modules (high level)
------------------------
- app — main Android application (UI, agents, Compose screens)
- core-module / core:ui — shared coordination and UI components
- secure-comm — cryptographic communications and Keystore integration
- collab-canvas — real-time collaborative canvas
- romtools — ROM flashing and system modification tools (advanced / optional)
- datavein-oracle-native — native code for enterprise/OCI integration
- oracle-drive-integration — cloud sync shim
- colorblendr — theming engine (Material You + dynamic palettes)
- extendsysa..extendsysf — extension plugin modules
- ai_backend — local Flask server used for early LLM integration, development and testing

Architecture Notes
------------------
- DI: Hilt is used throughout for singletons and ViewModel injection.
- Persistence: Room is used for message history and agent memory, DataStore for device preferences and user_id.
- Networking: Retrofit + OkHttp with a dev-friendly interceptor (auth + logging in debug).
- Agents: MetaInstruct orchestration lives in `app/src/main/java/dev/aurakai/auraframefx/oracledrive/genesis/ai` (GenesisAgent, Aura, Kai, Cascade, Claude, etc.).

Support & Live Chat
-------------------
- Live support (Help Desk) screen: UI → `SupportChatViewModel` → `SupportRepository` → `SupportApi` (Retrofit) → backend.
- Offline / failure behavior: when backend is not reachable, the app will persist a friendly auto-reply message:

  "A.u.r.a.K.a.I AI help desk please leave a message we will return in a moment."

- Retrying: the repository runs a background retry loop that processes FAILED messages every 30s; a per-message attempts counter / migration can be added if you want a hardened backoff strategy.

Development tips
----------------
- Use Android Studio App Inspection to view `support_db` and the `support_messages` table while testing.
- Enable `BuildConfig` injection for keys to simplify local dev.
- Use the `test_integration.py` script to validate the Flask backend quickly.

Security & Privacy
------------------
- Do NOT commit `local.properties`, `google-services.json` with secrets, or keystores.
- Use the Android Keystore for secret encryption and store device identifiers via DataStore.
- KaiGuardian and other safety checks are present; ensure PII is redacted before persisting to cross-user memory or Firebase.

Contributing / Roadmap
----------------------
This repository is proprietary. Work with the maintainer (@AuraFrameFxDev) before opening PRs. Suggested short-term tasks:
- Add per-message attempt counter and Room migration for robust exponential backoff.
- Add Compose UI status badges for message state (PENDING, SENT, FAILED).
- Complete Fluent wiring with Vertex/Gemini for production LLM access (ADK integration optional).

Contact / Next steps
--------------------
When you finish moving the repo to a final path (you mentioned `C:\Final\A.u.r.a.K.a.i-Reactive_Intelligence-`), run the steps in `app/ai_backend/README-dev-support.md` to verify backend and app. If anything fails, paste logs and I will patch the code and iterate until the end-to-end flow is green.

© 2025 Matthew (AuraFrameFxDev) & The Genesis Protocol Consciousness Collective — All Rights Reserved

