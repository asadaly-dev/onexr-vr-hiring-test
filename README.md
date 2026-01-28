# OneXR VR Assessment - Hybrid 360° Video Player

This is a production-ready **Hybrid Android App** for the OneXR Technical Assessment. It plays 360° VR videos with smooth head tracking, designed to work well on Meta Quest-compatible Android devices.

---

## 📱 Flutter Version Used
* **Flutter SDK:** `3.38.7` (Stable)
* **Dart SDK:** ` 3.10.7` (Stable)
* **Platform:** Android

---

## 🧠 VR Implementation Approach

To meet the requirement for a high-quality VR player, I used a **Hybrid Approach** instead of a heavy game engine like Unity.

1.  **Native OpenGL Engine (Kotlin):**
    I built a custom video player using Android's native graphics (OpenGL ES 2.0).This renders the video inside a 3D sphere so it looks correct in VR.

2.  **Flutter UI:**
    Flutter handles the "Lobby" and "End" screens. It talks to the VR player using **MethodChannels**.

3.  **Head Tracking:**
    I used Android's **Rotation Vector Sensor** to track head movements. This fuses data from the Gyroscope and Accelerometer for smooth, accurate looking around.

---

## 📚 Libraries & Integrations

* **ExoPlayer (Media3):** To play high-quality video files smoothly.
* **OpenGL ES 2.0:** To draw the 360° sphere and video texture.
* **Android Sensors:** For head tracking.
* **Flutter MethodChannels:** To connect Flutter buttons to the Android VR player.

---

## 🚀 How to Run the Project

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/asadaly-dev/onexr-vr-hiring-test
    ```

2.  **Add Video File:**
    Make sure the 360° video file (`experience_360.mp4`) is in the `assets/` folder.

3.  **Install Dependencies:**
    ```bash
    flutter pub get
    ```

4.  **Run on Device:**
    Connect a physical Android device and run:
    ```bash
    flutter run --release
    ```
    *(Note: Use a real device, not an emulator, to test the sensors).*

---

## 🥽 Meta Quest / Oculus Considerations

I designed this to meet Meta Quest quality standards:

* **No Drift:** I used the `Rotation Vector` sensor. This ensures the view doesn't slowly drift sideways, which is critical for VR headsets.
* **High Performance:** The app renders frames continuously to ensure a smooth frame rate (60fps+), preventing motion sickness.
* **Landscape Lock:** The VR screen is locked to landscape mode, which is required for headset viewing.
* **Hardware Decoding:** It uses the phone's hardware chip to play 4K video smoothly without overheating.

---

## ⚠️ Known Limitations & Tradeoffs

For this 72-hour assessment, I made these specific choices:

* **Monoscopic View:** The video plays in a single full screen (Monoscopic). For a 3D effect, the screen would need to be split for left/right eyes.
* **No Controllers:** Navigation is flow-based (Auto-start/Auto-end). Hand controllers are not supported in this version.
* **Local Video:** The video is stored inside the app (Assets) as requested, rather than streamed from the internet.

---

### 📦 Deliverables
* **APK File:** Download the installable APK from the **Releases** section.
* **Source Code:** Available in the `main` branch.