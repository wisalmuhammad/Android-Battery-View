# Android Battery View
---------

Simple way to show battery level. View can be customized in the form outer border thickness, Color and warning level. The view is scalable to adjust itself based on the user defined parameters. In case you need something else then please leave a feature request.
This simple also demonstrates and helps how to draw custom view.

# Usage
-------

```xml

<com.wisal.android.views.batteryview.BatteryView
        android:id="@+id/battery_view"
        android:layout_width="100dp"
        android:layout_height="50dp"
        app:battery_level="40"
        app:warning_level="15"
        app:charging="false"
        app:outer_border_stroke_width="2dp"
        app:outer_border_color="@android:color/darker_gray"/>

```

You can add this lib in your project as simple as add this line to your build.gradle file.

```
   implementation 'com.github.wisalmuhammad:Android-Battery-View:1.0.0

```
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```



# ScreenShots
------------

![Battery Charging](https://i.imgur.com/6TBYCGc.jpg)
![Battery No-Charging](https://i.imgur.com/gTeOSC5.jpg)


# Licence
----------

This project is licensed under [MIT](LICENSE.md) license.