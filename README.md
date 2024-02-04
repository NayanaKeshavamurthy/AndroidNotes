# Android Notes Mobile App

## Overview

Android Notes is a simple note-taking app that allows users to create and manage personal notes. The app uses RecyclerView, Multiple Activity, JSON File, and Option Menus to provide a seamless user experience.

## App Requirements

- Create and maintain personal notes with a title, note text, and last-update time.
- Save and load notes to/from the internal file system in JSON format.
- Main Activity with a list of notes displayed in time order (latest-update-first).
- Edit Activity for creating new notes or editing existing ones.
- About Activity displaying application details.
- Consistent layout design for both portrait and landscape orientations.
- Confirmation dialogs for note deletion and unsaved changes.
- Appropriate color scheme for a better user experience.

## Application Flow Diagrams

### 1. Add a New Note/Edit an Existing Note

1) Tap the Add button
    - Opens the Edit Activity with empty Title and Note Text areas.

2) Tap on a note to edit
    - Opens the Edit Activity displaying the selected note's Title and Note Text for editing.

3) Tap the Save button
    - Updates the note in the Main Activity's list or adds a new note.
    
4) Tap the Back button
    - Displays a confirmation dialog if changes have been made. Saves and exits if confirmed.

### 2. Delete an Existing Note

1) Long-press a Note
    - Opens a confirmation dialog for deleting the selected note.

2) Selecting YES will remove the note from the Main Activity's note list.

### 3. View the Application "About" Details

1) Tap the Info button in the Main Activity
    - Opens the About Activity displaying application details.

2) Tap the Back button to return.

## Layout Design

- Main Activity displays notes in a RecyclerView with a time-ordered list.
- Edit Activity allows editing of note title and text with a Save options-menu item.
- About Activity contains application details with a full-screen background image.

## Usage

1. Clone the repository.
2. Open the project in Android Studio.
3. Build and run the app on an Android emulator or device.
