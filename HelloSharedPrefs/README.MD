# HelloSharedPrefs

Shared preferences allow you to store small amounts of primitive data as key/value pairs in a file on the device.
This app can save, retrieve, and reset shared preferences.

## What is covered

- How to identify what shared preferences are.
- How to create a shared preferences file for your app.
- How to save data to shared preferences, and read those preferences back again.
- How to clear the data in the shared preferences.

## Why SharedPreferences

The data you save to shared preferences is different from the data in the saved activity state:

- Data in a saved `activity instance state` is retained across activity instances in the `same user session`.
- `Shared preferences persist across user sessions`.
    - Shared preferences persist even if your app stops and restarts, or if the device reboots.

## When to use what

Use shared preferences only when you need to save a small amount data as simple key/value pairs.
To manage larger amounts of persistent app data, use a storage method such as the Room` library or an `SQL database`.
