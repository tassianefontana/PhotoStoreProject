#PhotoStoreProject

This project enables capturing photos using the camera of Android devices. The app has two main views:
-> MainFragment: 
  - Name (editable)
  - Age (editable, with a dropdown list ranging from 0 to 150).
  - Current Date (non-editable): Displays the system date.
  - A RecyclerView that shows a list of all the .jpg file names stored in the Download folder. Clicking on an item, it displays the corresponding image.
  - It also includes a button that navigates the user to the camera screen.

-> The camera screen has a more minimalist UI, it contains:
   - A TextureView to display the camera image.
   - A button that captures the photo and also retrieves the data from the main screen (name, age, date, and file name), which is then saved in an SQL database using the Room library.

The app is developed using native Android (Kotlin), implementing the Camera2 API, LiveData and ViewModel for state management, as well as Material Design 3 components.
