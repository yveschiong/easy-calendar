package com.yveschiong.easycalendar.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.yveschiong.easycalendar.R;
import com.yveschiong.easycalendar.models.CalendarRange;
import com.yveschiong.easycalendar.models.Event;
import com.yveschiong.easycalendar.utils.CalendarUtils;
import com.yveschiong.easycalendar.utils.ResourceUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DayView extends View {

    private static final int HOURS = (int) TimeUnit.DAYS.toHours(1);
    private static final int MINUTES = (int) TimeUnit.HOURS.toMinutes(1);

    private SimpleDateFormat dateFormat = new SimpleDateFormat("h aa", Locale.getDefault());

    private Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint dividerLinesPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint timeBlockPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint timeBlockTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private Paint eventsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint eventsTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private int rowHeight;

    private int dividerLinesPadding;
    private int dividerLinesStrokeWidth;

    private int timeBlockWidth;
    private int defaultTimeBlockWidth;
    private float timeBlockScale;
    private float defaultTimeBlockScale;
    private Rect timeBlockTextBounds = new Rect();

    private RectF[] rowBounds = new RectF[HOURS];

    private CalendarRange dayRange = CalendarUtils.createCalendarRange();

    private String[] formattedHourTexts = new String[HOURS];

    private List<Event> events = new ArrayList<>();
    private Map<Event, RenderData> eventsRenderData = new LinkedHashMap<>();
    private int eventsPadding;
    private int eventsBorderRadius;
    private int eventsTextPadding;
    private int eventsMinHeight;
    private int eventsNumMaxEventsOverlappingTimes;

    private List<EventClickedListener> listeners = new ArrayList<>();

    // Class to store graphical data about an event
    private static class RenderData {
        // Used to represent the bounds of the event graphical object
        private RectF bounds = new RectF();

        // Used for text that wraps to new lines
        private StaticLayout textLayout;

        // Indicates all the overlapping events this event has (including itself)
        private List<Event> overlappingEvents = new ArrayList<>();

        // Indicates if this event needs to be updated graphically
        private boolean dirty = true;

        // Flag for if we should draw this event or not
        private boolean shouldDraw = true;
    }

    public interface EventClickedListener {
        void onEventClicked(Event event);
    }

    public DayView(Context context) {
        super(context);
        init(context, null);
    }

    public DayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        handleAttributes(context, attrs);

        // Initialize with empty values
        for (int i = 0; i < rowBounds.length; i++) {
            rowBounds[i] = new RectF();
        }

        formatHourTexts();
    }

    private void handleAttributes(Context context, AttributeSet attrs) {
        // Set default dimensions
        backgroundPaint.setColor(ContextCompat.getColor(context, R.color.dayViewDefaultBackgroundColor));
        dividerLinesPaint.setColor(ContextCompat.getColor(context, R.color.dayViewDefaultDividerLinesColor));
        timeBlockPaint.setColor(ContextCompat.getColor(context, R.color.dayViewDefaultTimeBlockColor));
        timeBlockTextPaint.setColor(ContextCompat.getColor(context, R.color.dayViewDefaultTimeBlockTextColor));
        timeBlockTextPaint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.dayViewDefaultTimeBlockTextSize));
        eventsPaint.setColor(ContextCompat.getColor(context, R.color.dayViewDefaultEventsColor));
        eventsTextPaint.setColor(ContextCompat.getColor(context, R.color.dayViewDefaultEventsTextColor));
        eventsTextPaint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.dayViewDefaultEventsTextSize));

        rowHeight = context.getResources().getDimensionPixelSize(R.dimen.dayViewDefaultRowHeight);

        dividerLinesPadding = context.getResources().getDimensionPixelSize(R.dimen.dayViewDefaultDividerLinesPadding);
        dividerLinesStrokeWidth = context.getResources().getDimensionPixelSize(R.dimen.dayViewDefaultDividerLinesStrokeWidth);

        defaultTimeBlockWidth = timeBlockWidth = context.getResources().getDimensionPixelSize(R.dimen.dayViewDefaultTimeBlockWidth);
        defaultTimeBlockScale = timeBlockScale = ResourceUtils.getFloatDimension(context, R.dimen.dayViewDefaultTimeBlockScale);

        eventsPadding = context.getResources().getDimensionPixelSize(R.dimen.dayViewDefaultEventsPadding);
        eventsBorderRadius = context.getResources().getDimensionPixelSize(R.dimen.dayViewDefaultEventsBorderRadius);
        eventsTextPadding = context.getResources().getDimensionPixelSize(R.dimen.dayViewDefaultEventsTextPadding);
        eventsMinHeight = context.getResources().getDimensionPixelSize(R.dimen.dayViewDefaultEventsMinHeight);
        eventsNumMaxEventsOverlappingTimes = context.getResources().getInteger(R.integer.dayViewDefaultNumMaxEventsOverlappingTimes);

        if (attrs == null) {
            return;
        }

        // Set styled attributes
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.DayView);
        try {
            backgroundPaint.setColor(typedArray.getColor(R.styleable.DayView_backgroundColor, backgroundPaint.getColor()));
            dividerLinesPaint.setColor(typedArray.getColor(R.styleable.DayView_dividerLinesColor, dividerLinesPaint.getColor()));
            timeBlockPaint.setColor(typedArray.getColor(R.styleable.DayView_timeBlockColor, timeBlockPaint.getColor()));
            timeBlockTextPaint.setColor(typedArray.getColor(R.styleable.DayView_timeBlockTextColor, timeBlockTextPaint.getColor()));
            timeBlockTextPaint.setTextSize(typedArray.getDimension(R.styleable.DayView_timeBlockTextSize, timeBlockTextPaint.getTextSize()));
            eventsPaint.setColor(typedArray.getColor(R.styleable.DayView_eventsColor, eventsPaint.getColor()));
            eventsTextPaint.setColor(typedArray.getColor(R.styleable.DayView_eventsTextColor, eventsTextPaint.getColor()));
            eventsTextPaint.setTextSize(typedArray.getDimension(R.styleable.DayView_eventsTextSize, eventsTextPaint.getTextSize()));

            rowHeight = typedArray.getDimensionPixelSize(R.styleable.DayView_rowHeight, rowHeight);

            dividerLinesPadding = typedArray.getDimensionPixelSize(R.styleable.DayView_dividerLinesPadding, dividerLinesPadding);
            dividerLinesStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.DayView_dividerLinesStrokeWidth, dividerLinesStrokeWidth);

            timeBlockWidth = typedArray.getDimensionPixelSize(R.styleable.DayView_timeBlockWidth, timeBlockWidth);
            timeBlockScale = typedArray.getFloat(R.styleable.DayView_timeBlockScale, timeBlockScale);

            eventsPadding = typedArray.getDimensionPixelSize(R.styleable.DayView_eventsPadding, eventsPadding);
            eventsBorderRadius = typedArray.getDimensionPixelSize(R.styleable.DayView_eventsBorderRadius, eventsBorderRadius);
            eventsTextPadding = typedArray.getDimensionPixelSize(R.styleable.DayView_eventsTextPadding, eventsTextPadding);
            eventsMinHeight = typedArray.getDimensionPixelSize(R.styleable.DayView_eventsMinHeight, eventsMinHeight);
            eventsNumMaxEventsOverlappingTimes = typedArray.getInteger(R.styleable.DayView_eventsNumMaxEventsOverlappingTimes, eventsNumMaxEventsOverlappingTimes);
        } finally {
            typedArray.recycle();
        }
    }

    // region getters and setters
    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
        formatHourTexts();
        invalidate();
    }

    @ColorInt
    public int getBackgroundColor() {
        return backgroundPaint.getColor();
    }

    public void setBackgroundPaint(@ColorInt int color) {
        backgroundPaint.setColor(color);
        invalidate();
    }

    @ColorInt
    public int getDividerLinesColor() {
        return dividerLinesPaint.getColor();
    }

    public void setDividerLinesColor(@ColorInt int color) {
        dividerLinesPaint.setColor(color);
        invalidate();
    }

    @ColorInt
    public int getTimeBlockColor() {
        return timeBlockPaint.getColor();
    }

    public void setTimeBlockColor(@ColorInt int color) {
        timeBlockPaint.setColor(color);
        invalidate();
    }

    @ColorInt
    public int getTimeBlockTextColor() {
        return timeBlockTextPaint.getColor();
    }

    public void setTimeBlockTextColor(@ColorInt int color) {
        timeBlockTextPaint.setColor(color);
        invalidate();
    }

    public float getTimeBlockTextSize() {
        return timeBlockTextPaint.getTextSize();
    }

    public void setTimeBlockTextSize(float timeBlockTextSize) {
        timeBlockTextPaint.setTextSize(timeBlockTextSize);
        refresh();
    }

    @ColorInt
    public int getEventsColor() {
        return eventsPaint.getColor();
    }

    public void setEventsColor(@ColorInt int color) {
        eventsPaint.setColor(color);
        invalidate();
    }

    @ColorInt
    public int getEventsTextColor() {
        return eventsTextPaint.getColor();
    }

    public void setEventsTextColor(@ColorInt int color) {
        eventsTextPaint.setColor(color);
        invalidate();
    }

    public float getEventsTextSize() {
        return eventsTextPaint.getTextSize();
    }

    public void setEventsTextSize(float eventsTextSize) {
        eventsTextPaint.setTextSize(eventsTextSize);
        setEventsDirty(true);
        refresh();
    }

    public int getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
        refresh();
    }

    public int getDividerLinesPadding() {
        return dividerLinesPadding;
    }

    public void setDividerLinesPadding(int dividerLinesPadding) {
        this.dividerLinesPadding = dividerLinesPadding;
        refresh();
    }

    public int getDividerLinesStrokeWidth() {
        return dividerLinesStrokeWidth;
    }

    public void setDividerLinesStrokeWidth(int dividerLinesStrokeWidth) {
        this.dividerLinesStrokeWidth = dividerLinesStrokeWidth;
        refresh();
    }

    public int getTimeBlockWidth() {
        return timeBlockWidth;
    }

    public void setTimeBlockWidth(int timeBlockWidth) {
        this.timeBlockWidth = timeBlockWidth;

        // Reset the scaling since this is a manual definition of the width
        timeBlockScale = defaultTimeBlockScale;

        setEventsDirty(true);

        refresh();
    }

    public float getTimeBlockScale() {
        return timeBlockScale;
    }

    public void setTimeBlockScale(float timeBlockScale) {
        this.timeBlockScale = timeBlockScale;
        refresh();
    }

    public Calendar getDay() {
        return dayRange.getStart();
    }

    public void setDay(Calendar day) {
        dayRange = CalendarUtils.createCalendarRange(day);
        formatHourTexts();

        // When a new day is set, clear out all previous events
        clearEvents();
    }

    public void setDay(Date day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        setDay(calendar);
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        if (events == null) {
            return;
        }

        for (Event event : events) {
            // We need to check if all the events can be added to this day
            if (!dayRange.intersects(event.getCalendarRange())) {
                return;
            }
        }

        this.events = events;
        refreshEventsRenderData();
        updateAllEventIntersections();
        refresh();
    }

    public void addEvent(Event event) {
        if (event == null) {
            return;
        }

        // We need to check if this event can be added to this day
        if (!dayRange.intersects(event.getCalendarRange())) {
            return;
        }

        events.add(event);
        createNewEventRenderData(event);
        updateAllEventIntersections();
        refresh();
    }

    public void removeEvent(Event event) {
        events.remove(event);
        eventsRenderData.remove(event);
        updateAllEventIntersections();
        refresh();
    }

    public void clearEvents() {
        events.clear();
        eventsRenderData.clear();
        refresh();
    }

    public int getEventsPadding() {
        return eventsPadding;
    }

    public void setEventsPadding(int eventsPadding) {
        this.eventsPadding = eventsPadding;
        setEventsDirty(true);
        refresh();
    }

    public int getEventsBorderRadius() {
        return eventsBorderRadius;
    }

    public void setEventsBorderRadius(int eventsBorderRadius) {
        this.eventsBorderRadius = eventsBorderRadius;
        invalidate();
    }

    public int getEventsTextPadding() {
        return eventsTextPadding;
    }

    public void setEventsTextPadding(int eventsTextPadding) {
        this.eventsTextPadding = eventsTextPadding;
        setEventsDirty(true);
        refresh();
    }

    public int getEventsMinHeight() {
        return eventsMinHeight;
    }

    public void setEventsMinHeight(int eventsMinHeight) {
        this.eventsMinHeight = eventsMinHeight;
        setEventsDirty(true);
        refresh();
    }

    public int getEventsNumMaxEventsOverlappingTimes() {
        return eventsNumMaxEventsOverlappingTimes;
    }

    public void setEventsNumMaxEventsOverlappingTimes(int eventsNumMaxEventsOverlappingTimes) {
        this.eventsNumMaxEventsOverlappingTimes = eventsNumMaxEventsOverlappingTimes;
        updateAllEventIntersections();
        setEventsDirty(true);
        refresh();
    }

    public List<EventClickedListener> getEventClickedListeners() {
        return listeners;
    }

    public void addEventClickedListener(EventClickedListener listener) {
        listeners.add(listener);
    }

    public void removeEventClickedListener(EventClickedListener listener) {
        listeners.remove(listener);
    }

    public void clearEventClickedListeners() {
        listeners.clear();
    }
    // endregion

    // region helper methods
    private void refresh() {
        requestLayout();
        invalidate();
    }

    private void refreshEventsRenderData() {
        eventsRenderData.clear();

        if (events == null || events.isEmpty()) {
            // Do not init event bounds for empty event list
            return;
        }

        for (Event event : events) {
            createNewEventRenderData(event);
        }
    }

    private void createNewEventRenderData(Event event) {
        if (event.getCalendarRange() == null) {
            // Do not update event bounds for an invalid event
            return;
        }

        // Set an empty object so we know to update it in the measure step
        eventsRenderData.put(event, new RenderData());
    }

    private float getCalendarRowY(Calendar calendar) {
        if (calendar.before(dayRange.getStart())) {
            // If the event from yesterday overlaps onto today, then start drawing from the top
            return 0.0f;
        }

        if (calendar.after(dayRange.getEnd())) {
            // If the event overlaps onto tomorrow, then draw to the bottom
            return rowHeight * HOURS;
        }

        return (calendar.get(Calendar.HOUR_OF_DAY) + ((float) calendar.get(Calendar.MINUTE) / MINUTES)) * rowHeight;
    }

    private void setEventsDirty(boolean dirty) {
        for (RenderData renderData : eventsRenderData.values()) {
            renderData.dirty = dirty;
        }
    }

    private void formatHourTexts() {
        // We want to only do this sparingly so this is not done on every draw
        Calendar day = (Calendar) dayRange.getStart().clone();
        for (int i = 0; i < formattedHourTexts.length; i++) {
            day.set(Calendar.HOUR_OF_DAY, i);
            formattedHourTexts[i] = dateFormat.format(day.getTime());
        }
    }

    private void updateAllEventIntersections() {
        for (Event event : eventsRenderData.keySet()) {
            updateEventIntersection(event);
        }
    }

    // Won't be too expensive since the assumption is that people won't have a lot
    // of time conflicts with their events (events with overlapping/intersecting time ranges)
    // so we won't spend too much effort investing into optimizations for now. Also
    // this method should not be called within the view life cycle methods and should be called
    // when the relevant model data changes
    private void updateEventIntersection(Event event) {
        RenderData renderData = eventsRenderData.get(event);

        // Reset the overlapping events
        renderData.overlappingEvents.clear();

        if (!renderData.shouldDraw) {
            // If we shouldn't draw, then there's no point to finding intersections
            return;
        }

        for (Map.Entry<Event, RenderData> entry : eventsRenderData.entrySet()) {
            Event eventEntry = entry.getKey();
            // Assuming map is in order
            if (event == eventEntry) {
                if (renderData.overlappingEvents.size() > eventsNumMaxEventsOverlappingTimes) {
                    renderData.overlappingEvents.clear();

                    // When the number of time overlaps is greater than the maximum, then
                    // we should not be showing any more events after the last overlap in terms of order
                    renderData.shouldDraw = false;
                }

                for (Event overlappingEvent : renderData.overlappingEvents) {
                    // Add this event to the list of overlapping events in each overlapping event earlier in the map
                    // so we can update the positions and size
                    eventsRenderData.get(overlappingEvent).overlappingEvents.add(event);
                }

                // Add event as overlapping with itself
                renderData.overlappingEvents.add(eventEntry);

                // We are only looking at the entries in front of this event's to see if this
                // qualifies to be shown under the max events overlapping count
                break;
            }

            if (event.getCalendarRange().intersects(eventEntry.getCalendarRange())) {
                renderData.overlappingEvents.add(eventEntry);
            }
        }
    }
    // endregion

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (eventsRenderData.isEmpty()) {
            // Pass to parent since we have nothing to touch
            return super.onTouchEvent(event);
        }

        Event touchedEvent = null;
        for (Map.Entry<Event, RenderData> entry : eventsRenderData.entrySet()) {
            if (!entry.getValue().bounds.contains(event.getX(), event.getY())) {
                continue;
            }

            // We touched this event
            touchedEvent = entry.getKey();
        }

        if (touchedEvent == null) {
            // No event has been touched so pass to parent
            return super.onTouchEvent(event);
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // We will need to consume the action down in order to get further touch events
                return true;
            case MotionEvent.ACTION_UP:
                // Notify listeners
                for (EventClickedListener listener : listeners) {
                    listener.onEventClicked(touchedEvent);
                }
                return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);

        // Set height as row height multiplied by 24 hours to represent this view's height
        int height = rowHeight * HOURS;

        // Set up the bounds for each row
        for (int i = 0; i < rowBounds.length; i++) {
            rowBounds[i].left = 0;
            rowBounds[i].top = i * rowHeight;
            rowBounds[i].right = width;
            rowBounds[i].bottom = (i + 1) * rowHeight;
        }

        // Time block scaling overrides manual width definitions
        if (timeBlockWidth == defaultTimeBlockWidth || timeBlockScale != defaultTimeBlockScale) {
            timeBlockWidth = (int) (timeBlockScale * width);
        }

        for (Map.Entry<Event, RenderData> entry : eventsRenderData.entrySet()) {
            Event event = entry.getKey();
            RenderData renderData = entry.getValue();
            if (!renderData.dirty) {
                // If not dirty then we don't need to update
                continue;
            }

            if (!renderData.shouldDraw) {
                // If we shouldn't draw, then don't build the render data
                continue;
            }

            // Calculate the event width with the available width space
            float eventWidth = (float) (width - timeBlockWidth - eventsPadding * (renderData.overlappingEvents.size() + 1)) / (renderData.overlappingEvents.size());

            // Get the x offset
            float left = timeBlockWidth + eventsPadding * (renderData.overlappingEvents.indexOf(event) + 1) + eventWidth * renderData.overlappingEvents.indexOf(event);

            // Account for the minimum event height for the top of the rect
            float top = Math.min(getCalendarRowY(event.getCalendarRange().getStart()), height - eventsMinHeight);

            // Just use the event width and the x offset
            float right = left + eventWidth;

            // Account for the minimum event height for the bottom of the rect
            float bottom = Math.max(getCalendarRowY(event.getCalendarRange().getEnd()), top + eventsMinHeight);

            renderData.bounds.set(left, top, right, bottom);

            // Object allocation needed, but will only occur when dirty bit is on and the layout needs to be updated.
            // Not too expensive since we are not changing the text frequently.
            // Create a static layout to contain the event name
            //renderData.textLayout = new StaticLayout(event.getName(), eventsTextPaint, (int) renderData.bounds.width() - eventsTextPadding * 2, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            renderData.textLayout = StaticLayout.Builder.obtain(event.getName(), 0, event.getName().length(), eventsTextPaint, (int) renderData.bounds.width() - eventsTextPadding << 1)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setLineSpacing(0.0f, 1.0f)
                    .setIncludePad(false)
                    .build();

            // Check if we can fit all the lines within the event rectangle, if not, we will need to truncate the text to fit
            if (renderData.textLayout.getLineTop(renderData.textLayout.getLineCount()) + eventsTextPadding * 2 > renderData.bounds.height()) {
                // Get the line of text that the end of the event bounds bottom intersects with
                int lineMax = renderData.textLayout.getLineForVertical((int) renderData.bounds.height() - eventsTextPadding * 2);
                String truncatedEventName = event.getName().substring(0, renderData.textLayout.getLineStart(lineMax));
                renderData.textLayout = StaticLayout.Builder.obtain(truncatedEventName, 0, truncatedEventName.length(), eventsTextPaint, (int) renderData.bounds.width() - eventsTextPadding << 1)
                        .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                        .setLineSpacing( 0.0f, 1.0f)
                        .setIncludePad(false)
                        .build();
            }

            // Set the dirty bit off so we don't update this again
            renderData.dirty = false;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw the background color
        canvas.drawColor(backgroundPaint.getColor());

        // Draw the side time block background
        canvas.drawRect(0, 0, timeBlockWidth, getMeasuredHeight(), timeBlockPaint);

        for (int i = 1; i < rowBounds.length; i++) {
            // Draw the dividers
            canvas.drawRect(
                    rowBounds[i].left + dividerLinesPadding + timeBlockWidth,
                    rowBounds[i].top,
                    rowBounds[i].right - dividerLinesPadding,
                    rowBounds[i].top + dividerLinesStrokeWidth,
                    dividerLinesPaint
            );

            String hourText = formattedHourTexts[i];
            timeBlockTextPaint.getTextBounds(hourText, 0, hourText.length(), timeBlockTextBounds);

            // We need to take into consideration the text bounds and the left and bottom displacement,
            // refer to https://stackoverflow.com/questions/11120392/android-center-text-on-canvas
            // This is why we subtract the text bounds left and bottom and should only be considered when drawing text.
            // The text is also drawn from the baseline for the y-coordinate so we also need to consider that
            canvas.drawText(
                    hourText,
                    rowBounds[i].left + (timeBlockWidth - timeBlockTextBounds.width()) * 0.5f - timeBlockTextBounds.left,
                    rowBounds[i].top + (dividerLinesStrokeWidth + timeBlockTextBounds.height()) * 0.5f - timeBlockTextBounds.bottom,
                    timeBlockTextPaint
            );
        }

        for (RenderData renderData : eventsRenderData.values()) {
            if (!renderData.shouldDraw) {
                // Don't draw
                continue;
            }

            // Draw the background for the event
            canvas.drawRoundRect(renderData.bounds, eventsBorderRadius, eventsBorderRadius, eventsPaint);

            // Draw the event name text with a static layout so we can wrap the text
            canvas.save();
            canvas.translate(renderData.bounds.left + eventsTextPadding, renderData.bounds.top + eventsTextPadding);
            renderData.textLayout.draw(canvas);
            canvas.restore();
        }
    }
}
