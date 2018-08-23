# HangboardApp
HangboardApp is a training application for climbing. It generates hangboard workout based on user's climbing level (grade).

![main screen](https://drugggi.github.io/main_screen.jpg)

# Getting Started

HangboardApp is developed with the latest Android Studio https://developer.android.com/studio/index.html

git clone https://github.com/drugggi/HangboardApp.git

Android Studio -> Open an existing Android Studio project and select HangboardApp

# Usage

Select the hangboard by swiping the hangboard picture. Select the grade and duration with slider and the HangboardApp generates the holds and grips for the workout. Holds and grips can be shuffled until preferable workout hangs are found.

Time controls can be fully controlled in Time Controls menu. Holds and grips can be manually changed by long clicking target hang on the list until pop up list appears. User can only change left hand hold, right hand hold, or grip type at a time, So it takes three clicks to fully customize a hang.

Previous workouts and statistics can be accessed through statistics button. There user can browse workout history list and by pressing show graphs button many visual graphs are shown. Graphs are constructed with many important details about workouts, such as total time, time under tension, grip types used, hangboards used, hang difficulties, workload, intensity and workout power

![time controls](https://drugggi.github.io/Settings_menu.jpg)

![custom_hold](https://drugggi.github.io/Customhold.jpg)

When the Workout starts, holds and grips are visually represented with pictures and in text form. Timer is presented with countdown clock, progress bar and sound effects. Hangboard image can be moved and scaled by touching it. Increase or decrease timer text by long clicking it. Edit latest hang by long clicking first progress bar.

![Workout_screen](https://drugggi.github.io/workout.jpg)

During and after workout user can check the workout progress when the timer is paused. After the workout user is supposed to edit all the unsuccessful hangs and save the workout to database.

![Edit workout](https://drugggi.github.io/editing_workout.jpg)

All previous saved workouts are shown in a list. All the workout details are editable. If user don't want some workout to be shown in a list, it can be made hidden. For example if the workout was simply a warmup, or it was total failure, or it never happened. Only hidden workouts then can be deleted.

![Workout history](https://drugggi.github.io/database.jpg)

Show graphs button gathers a lot of information about all the workouts and represents them in graph form. For example grip type and hangboard type pie chart, hang difficulties, workload and date bar charts, workout time, time under tension, intensity, hang difficulty and workout power line charts.

![graphs](https://drugggi.github.io/graphs.jpg)
