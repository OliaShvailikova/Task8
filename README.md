Task 8
Imagine you are briefed with writing an app for a social photography startup. They maintain a list
of scenic photo locations around some city. Their users would like to see all these default
locations, plus be able to add their own locations, as well as add notes about each location.
The app needs to import these default locations from a JSON file that you are provided with.
User added locations only need to be persisted locally. A user should be able to see all
locations on a map, as well as in a list. A user should be able to input and edit notes as text for
any location. You will need to make assumptions about how users will interact with the app.
Please write down all your assumptions clearly.
It's expected that this task will take candidates around 2-4 hours to complete.

Detailed requirements
● Include the attached JSON file of default locations in your project as an asset
● Provide a map screen (using any map SDK of your choosing)
● Allow custom locations to be added from the map screen
   ○ The user should be able to provide names for these locations
● Show pins for both default and custom locations on the map
● Provide a screen listing all locations, sorted by distance
● When locations are selected on either the map or list screen, show a detail screen
● In the detail screen, allow the user to enter notes about the location
● All information entered by the user must be persisted between app launches

Other requirements
● Use a native language: Swift/Objective-C for iOS and Java/Kotlin for Android
● Third-party libraries are allowed, except when they solve the whole problem
● The app must include unit tests
