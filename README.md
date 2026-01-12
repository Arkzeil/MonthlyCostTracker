# Monthly Cost Tracker

## Project Description
The Monthly Cost Tracker is an Android application designed to help users track their monthly expenses. Users can add new transactions, view a list of all recorded transactions, and see a running total of their monthly costs. The application utilizes persistent storage to ensure that all transaction data is saved and available even after the app is closed.

## Features
*   **Add Transactions:** Easily add new expenses with a description and amount.
*   **View Transactions:** See a list of all recorded transactions.
*   **Total Monthly Cost:** Displays the sum of all recorded transactions for a clear overview of spending.
*   **Data Persistence:** All transaction data is stored locally using Room Database, ensuring data is not lost when the app is closed.
*   **Intuitive UI:** Built with Jetpack Compose for a modern and responsive user experience.

## Technologies Used
*   **Kotlin:** Primary programming language for Android development.
*   **Jetpack Compose:** Modern toolkit for building native Android UI.
*   **Room Persistence Library:** Provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.
*   **Android Navigation Compose:** For managing in-app navigation.
*   **Gradle Kotlin DSL:** For build automation and dependency management.

## Setup and Installation

To get a local copy up and running, follow these simple steps.

### Prerequisites
*   Android Studio (Bumblebee 2021.1.1 or newer recommended)
*   Android SDK (API Level 34 or higher recommended)
*   An Android Emulator or a physical Android device

### Installation

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/MonthlyCostTracker.git
    cd MonthlyCostTracker
    ```
2.  **Open in Android Studio:**
    Open the project in Android Studio. Android Studio will automatically download any missing SDK components and Gradle dependencies.

3.  **Build the project:**
    Sync the Gradle project (File > Sync Project with Gradle Files) and then build the project (Build > Make Project).
    Alternatively, you can build from the command line:
    ```bash
    ./gradlew build
    ```

4.  **Run on an Emulator or Device:**
    *   **Ensure Android SDK tools are in your PATH:**
        Make sure your `ANDROID_HOME` environment variable is set to your Android SDK path (e.g., `/opt/android-sdk`), and that `$ANDROID_HOME/emulator` and `$ANDROID_HOME/platform-tools` are included in your system's `PATH`.
        ```bash
        export ANDROID_HOME=/opt/android-sdk # Replace with your actual SDK path
        export PATH=$PATH:$ANDROID_HOME/emulator:$ANDROID_HOME/platform-tools
        ```
    *   **Create an Emulator (if you haven't already):**
        ```bash
        avdmanager create avd -n test_emulator -k "system-images;android-34;google_apis;x86_64"
        ```
        (You can replace `test_emulator` with your preferred AVD name and adjust the system image as needed.)
    *   **Start an Emulator:**
        ```bash
        emulator -avd test_emulator
        ```
        Wait for the emulator to fully boot up. This might take a few minutes.
    *   **Verify Emulator Connection:**
        Open a new terminal window and run:
        ```bash
        adb devices -l
        ```
        You should see your emulator listed (e.g., `emulator-5554 device product:sdk_gphone64_x86_64 model:sdk_gphone64_x86_64 device:generic_x86_64 transport_id:1`).
    *   **Install the App:**
        ```bash
        adb install app/build/outputs/apk/debug/app-debug.apk
        ```
    *   **Launch the App:**
        ```bash
        adb shell am start -n com.example.monthlycosttracker.ui/com.example.monthlycosttracker.ui.MainActivity
        ```
        Alternatively, from Android Studio, click the "Run 'app'" button after selecting your running emulator.

## Usage
1.  **Main Screen:** Upon launching the app, you will see the main screen displaying the "Total Monthly Cost" and a list of your transactions.
2.  **Add New Transaction:** Click the floating action button (plus icon) to navigate to the "Add New Transaction" screen.
3.  **Enter Details:** Input the transaction description and amount.
4.  **Save Transaction:** Click "Save Transaction" to add it to your list. The app will automatically navigate back to the main screen, and your total cost will be updated.
5.  **Data Persistence:** All transactions are saved automatically and will be available the next time you open the app.

## Future Enhancements
*   Editing and deleting existing transactions.
*   Filtering transactions by date or category.
*   Monthly/yearly reports and visualizations.
*   User authentication and cloud synchronization.
*   Support for multiple currencies.