[ ![Download](https://api.bintray.com/packages/yveschiong/easy-calendar/com.yveschiong.easycalendar/images/download.svg) ](https://bintray.com/yveschiong/easy-calendar/com.yveschiong.easycalendar/_latestVersion)

# easy-calendar

easy-calendar is an easy-to-implement, simple, and customizable set of calendar views for Android.

![device-2018-09-27-024338](https://user-images.githubusercontent.com/10403329/46127662-5b74c280-c1ff-11e8-840e-9a2d4ed2e64b.png)
![device-2018-09-27-023256](https://user-images.githubusercontent.com/10403329/46127680-692a4800-c1ff-11e8-8e9a-9068898e07df.png)

## Features
- Day view
- Month view
- Event creation
- Customization of colors
- Customization of size

## Download
Make sure you have defined the **jcenter()** repository in your project's **build.gradle** file:
```
allprojects {
    repositories {
        google()
        jcenter()
    }
}
```

Add the dependency to your module's **build.gradle** file:
```
dependencies {
    implementation 'com.yveschiong.view:easy-calendar:1.0.1'
}
```

## DayView Usage
For the day view, add the following to your **XML layout** (you'll need to wrap it inside a **ScrollView** for the view to be scrollable):
```xml
<ScrollView
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <com.yveschiong.easycalendar.views.DayView
            android:id="@+id/dayView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />

</ScrollView>
```

### Adding an event:
```java
Calendar start = CalendarUtils.createCalendar();
start.add(Calendar.HOUR_OF_DAY, 7);

Calendar end = CalendarUtils.createCalendar();
end.add(Calendar.HOUR_OF_DAY, 8);

dayView.addEvent(new Event("Morning Jog", "Just a jog to start my day!", new CalendarRange(start, end)));
```

### Clicks event handling:
```java
dayView.addEventClickedListener(new DayView.EventClickedListener() {
    @Override
    public void onEventClicked(Event event) {
        Calendar eventStart = event.getCalendarRange().getStart();
        Calendar eventEnd = event.getCalendarRange().getEnd();
    }
});
```

## MonthView Usage
For the month view, add the following to your **XML layout**:
```xml
<com.yveschiong.easycalendar.views.MonthView
        android:id="@+id/monthView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```

### Clicks event handling:
```java
monthView.setOnSelectedDayListener(new MonthView.OnSelectedDayListener() {
    @Override
    public void onSelectedDay(Calendar day) {
        // Do something with day
    }
});
```

### Chevron event handling:
```java
monthView.setOnChevronSelectedListener(new MonthView.OnChevronSelectedListener() {
    @Override
    public void onLeftChevronSelected() {
        // Do something on left chevron selected
    }

    @Override
    public void onRightChevronSelected() {
        // Do something on right chevron selected
    }
});
```

### Setting the display month:
```java
Calendar calendar =  Calendar.getInstance();
calendar.add(Calendar.MONTH, -1);
monthView.setMonth(calendar);
```

## Changelog
#### Version 1.0.1:
* Removed android library dependencies from the calendar range object.

#### Version 1.0.0:
* Initial release with a month and day view.

## License

This project is distributed under the terms of the MIT License. See file "LICENSE" for further reference.