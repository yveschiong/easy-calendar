package com.yveschiong.easycalendar.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yveschiong.easycalendar.R;
import com.yveschiong.easycalendar.models.CalendarRange;
import com.yveschiong.easycalendar.utils.CalendarUtils;

import java.util.Calendar;
import java.util.Date;

public class MonthView extends View {

    private static final int MAX_GRID_ROWS = 6;
    private static final int MAX_GRID_COLUMNS = CalendarUtils.DAYS_IN_A_WEEK;

    private boolean isDebugMode = false;

    private Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint todayCirclePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint yearTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint monthTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint weekdayTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint dayNotInMonthTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint dayTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private Paint debugLinesPaint;

    private int todayCircleRadius;

    private Rect yearTextBounds = new Rect();
    private Rect monthTextBounds = new Rect();

    private int monthYearPadding;
    private int headerPadding;

    private CalendarRange monthRange = CalendarUtils.createCalendarMonthRange();

    // The earliest occurrence of the start of the week that we are showing
    private Calendar startOfCalendarDay = CalendarUtils.getEarliestStartOfWeek(monthRange.getStart());

    // Keep track of today's date for drawing the circle indicator
    private Calendar today = CalendarUtils.createCalendar();

    private String yearText;
    private String monthText;
    private String[] weekdaysTexts = CalendarUtils.getWeekdayShortNames();

    // These are used for laying out the grid of days
    private Rect[][] gridTextPositions = new Rect[MAX_GRID_ROWS][MAX_GRID_COLUMNS];

    // Indicates the y-coordinate where we can start the grid
    float gridStartY;

    // Dimensions of each grid cell
    float gridCellWidth;
    float gridCellHeight;

    // For debugging purposes only, to draw the grid line boundaries
    private Rect[][] gridLines;

    public MonthView(Context context) {
        super(context);
        init(context, null);
    }

    public MonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        handleAttributes(context, attrs);
        initTexts();
        initGrid();
    }

    private void handleAttributes(Context context, AttributeSet attrs) {
        // Set default colors and dimensions
        backgroundPaint.setColor(ContextCompat.getColor(context, R.color.monthViewDefaultBackgroundColor));

        todayCirclePaint.setColor(ContextCompat.getColor(context, R.color.monthViewDefaultTodayCircleColor));
        todayCirclePaint.setStrokeWidth(context.getResources().getDimensionPixelSize(R.dimen.monthViewDefaultTodayCircleStrokeWidth));
        todayCirclePaint.setStyle(Paint.Style.STROKE);

        yearTextPaint.setColor(ContextCompat.getColor(context, R.color.monthViewDefaultYearTextColor));
        yearTextPaint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.monthViewDefaultYearTextSize));

        monthTextPaint.setColor(ContextCompat.getColor(context, R.color.monthViewDefaultMonthTextColor));
        monthTextPaint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.monthViewDefaultMonthTextSize));

        weekdayTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        weekdayTextPaint.setColor(ContextCompat.getColor(context, R.color.monthViewDefaultWeekdayTextColor));
        weekdayTextPaint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.monthViewDefaultWeekdayTextSize));

        dayNotInMonthTextPaint.setColor(ContextCompat.getColor(context, R.color.monthViewDefaultDayNotInMonthTextColor));
        dayNotInMonthTextPaint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.monthViewDefaultDayNotInMonthTextSize));

        dayTextPaint.setColor(ContextCompat.getColor(context, R.color.monthViewDefaultDayTextColor));
        dayTextPaint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.monthViewDefaultDayTextSize));

        todayCircleRadius = context.getResources().getDimensionPixelSize(R.dimen.monthViewDefaultTodayCircleRadius);

        monthYearPadding = context.getResources().getDimensionPixelSize(R.dimen.monthViewDefaultMonthYearPadding);
        headerPadding = context.getResources().getDimensionPixelSize(R.dimen.monthViewDefaultHeaderPadding);

        if (isDebugMode) {
            debugLinesPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            debugLinesPaint.setColor(ContextCompat.getColor(context, R.color.monthViewDefaultDebugLinesColor));
            debugLinesPaint.setStyle(Paint.Style.STROKE);
        }

        if (attrs == null) {
            return;
        }

        // Set styled attributes
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MonthView);
        try {
            backgroundPaint.setColor(typedArray.getColor(R.styleable.MonthView_backgroundColor, backgroundPaint.getColor()));

            todayCirclePaint.setColor(typedArray.getColor(R.styleable.MonthView_todayCircleColor, todayCirclePaint.getColor()));

            // We don't want to lose precision if we can help it so we will check to see if the attribute is defined
            // and if not, then it would use the default stroke width fetched above instead of casting the stroke width to an integer
            if (typedArray.hasValue(R.styleable.MonthView_todayCircleStrokeWidth)) {
                todayCirclePaint.setStrokeWidth(typedArray.getDimensionPixelSize(R.styleable.MonthView_todayCircleStrokeWidth, (int) todayCirclePaint.getStrokeWidth()));
            }

            yearTextPaint.setColor(typedArray.getColor(R.styleable.MonthView_yearTextColor, yearTextPaint.getColor()));
            yearTextPaint.setTextSize(typedArray.getDimension(R.styleable.MonthView_yearTextSize, yearTextPaint.getTextSize()));

            monthTextPaint.setColor(typedArray.getColor(R.styleable.MonthView_monthTextColor, monthTextPaint.getColor()));
            monthTextPaint.setTextSize(typedArray.getDimension(R.styleable.MonthView_monthTextSize, monthTextPaint.getTextSize()));

            weekdayTextPaint.setColor(typedArray.getColor(R.styleable.MonthView_weekdayTextColor, weekdayTextPaint.getColor()));
            weekdayTextPaint.setTextSize(typedArray.getDimension(R.styleable.MonthView_weekdayTextSize, weekdayTextPaint.getTextSize()));

            dayNotInMonthTextPaint.setColor(typedArray.getColor(R.styleable.MonthView_dayNotInMonthTextColor, dayNotInMonthTextPaint.getColor()));
            dayNotInMonthTextPaint.setTextSize(typedArray.getDimension(R.styleable.MonthView_dayNotInMonthTextSize, dayNotInMonthTextPaint.getTextSize()));

            dayTextPaint.setColor(typedArray.getColor(R.styleable.MonthView_dayTextColor, dayTextPaint.getColor()));
            dayTextPaint.setTextSize(typedArray.getDimension(R.styleable.MonthView_dayTextSize, dayTextPaint.getTextSize()));

            todayCircleRadius = typedArray.getDimensionPixelSize(R.styleable.MonthView_todayCircleRadius, todayCircleRadius);

            monthYearPadding = typedArray.getDimensionPixelSize(R.styleable.MonthView_monthYearPadding, monthYearPadding);
            headerPadding = typedArray.getDimensionPixelSize(R.styleable.MonthView_headerPadding, headerPadding);

            isDebugMode = typedArray.getBoolean(R.styleable.MonthView_isDebugMode, isDebugMode);

            if (isDebugMode) {
                if (debugLinesPaint == null) {
                    debugLinesPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                }

                debugLinesPaint.setColor(typedArray.getColor(R.styleable.MonthView_debugLinesColor, ContextCompat.getColor(context, R.color.monthViewDefaultDebugLinesColor)));
                debugLinesPaint.setStyle(Paint.Style.STROKE);
            } else {
                debugLinesPaint = null;
            }
        } finally {
            typedArray.recycle();
        }
    }

    private void initTexts() {
        yearText = String.valueOf(monthRange.getStart().get(Calendar.YEAR));
        monthText = CalendarUtils.getMonthName(monthRange.getStart());
    }

    private void initGrid() {
        for (int i = 0; i < MAX_GRID_ROWS; i++) {
            for (int j = 0; j < MAX_GRID_COLUMNS; j++) {
                gridTextPositions[i][j] = new Rect();
            }
        }

        if (isDebugMode) {
            gridLines = new Rect[MAX_GRID_ROWS][MAX_GRID_COLUMNS];
            for (int i = 0; i < MAX_GRID_ROWS; i++) {
                for (int j = 0; j < MAX_GRID_COLUMNS; j++) {
                    gridLines[i][j] = new Rect();
                }
            }
        }
    }

    // region getters and setters
    public boolean isDebugMode() {
        return isDebugMode;
    }

    public void setDebugMode(boolean debugMode) {
        isDebugMode = debugMode;
        refresh();
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
    public int getTodayCircleColor() {
        return todayCirclePaint.getColor();
    }

    public void setTodayCircleColor(@ColorInt int color) {
        todayCirclePaint.setColor(color);
        invalidate();
    }

    public float getTodayCircleStrokeWidth() {
        return todayCirclePaint.getStrokeWidth();
    }

    public void setTodayCircleStrokeWidth(float strokeWidth) {
        todayCirclePaint.setStrokeWidth(strokeWidth);
        invalidate();
    }

    public int getTodayCircleRadius() {
        return todayCircleRadius;
    }

    public void setTodayCircleRadius(int todayCircleRadius) {
        this.todayCircleRadius = todayCircleRadius;
        invalidate();
    }

    @ColorInt
    public int getYearTextColor() {
        return yearTextPaint.getColor();
    }

    public void setYearTextColor(@ColorInt int color) {
        yearTextPaint.setColor(color);
        invalidate();
    }

    public float getYearTextSize() {
        return yearTextPaint.getTextSize();
    }

    public void setYearTextSize(float yearTextSize) {
        yearTextPaint.setTextSize(yearTextSize);
        refresh();
    }

    @ColorInt
    public int getMonthTextColor() {
        return monthTextPaint.getColor();
    }

    public void setMonthTextColor(@ColorInt int color) {
        monthTextPaint.setColor(color);
        invalidate();
    }

    public float getMonthTextSize() {
        return monthTextPaint.getTextSize();
    }

    public void setMonthTextSize(float monthTextSize) {
        monthTextPaint.setTextSize(monthTextSize);
        refresh();
    }

    @ColorInt
    public int getWeekdayTextColor() {
        return weekdayTextPaint.getColor();
    }

    public void setWeekdayTextColor(@ColorInt int color) {
        weekdayTextPaint.setColor(color);
        invalidate();
    }

    public float getWeekdayTextSize() {
        return weekdayTextPaint.getTextSize();
    }

    public void setWeekdayTextSize(float weekdayTextSize) {
        weekdayTextPaint.setTextSize(weekdayTextSize);
        refresh();
    }

    @ColorInt
    public int getDayNotInMonthTextColor() {
        return dayNotInMonthTextPaint.getColor();
    }

    public void setDayNotInMonthTextColor(@ColorInt int color) {
        dayNotInMonthTextPaint.setColor(color);
        invalidate();
    }

    public float getDayNotInMonthTextSize() {
        return dayNotInMonthTextPaint.getTextSize();
    }

    public void setDayNotInMonthTextSize(float dayNotInMonthTextSize) {
        dayNotInMonthTextPaint.setTextSize(dayNotInMonthTextSize);
        refresh();
    }

    @ColorInt
    public int getDayTextColor() {
        return dayTextPaint.getColor();
    }

    public void setDayTextColor(@ColorInt int color) {
        dayTextPaint.setColor(color);
        invalidate();
    }

    public float getDayTextSize() {
        return dayTextPaint.getTextSize();
    }

    public void setDayTextSize(float dayTextSize) {
        dayTextPaint.setTextSize(dayTextSize);
        refresh();
    }

    public int getMonthYearPadding() {
        return monthYearPadding;
    }

    public void setMonthYearPadding(int monthYearPadding) {
        this.monthYearPadding = monthYearPadding;
        refresh();
    }

    public int getHeaderPadding() {
        return headerPadding;
    }

    public void setHeaderPadding(int headerPadding) {
        this.headerPadding = headerPadding;
        refresh();
    }

    public CalendarRange getMonth() {
        return monthRange;
    }

    public void setMonth(Calendar day) {
        monthRange = CalendarUtils.createCalendarMonthRange(day);
        startOfCalendarDay = CalendarUtils.getEarliestStartOfWeek(monthRange.getStart());
        initTexts();
        refresh();
    }

    public void setMonth(Date day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        setMonth(calendar);
    }

    public String getYearText() {
        return yearText;
    }

    public String getMonthText() {
        return monthText;
    }

    public String[] getWeekdaysTexts() {
        return weekdaysTexts;
    }
    // endregion

    // region helper methods
    private void refresh() {
        requestLayout();
        invalidate();
    }
    // endregion

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        monthTextPaint.getTextBounds(monthText, 0, monthText.length(), monthTextBounds);

        // We use this to properly place the year underneath the month due to tails in text (like July)
        int monthTextBoundsBottom = monthTextBounds.bottom;

        // This sets the bounds ready to draw onto the canvas with just the left and top values
        monthTextBounds.offsetTo(
                (int) (getPaddingLeft() + (width - getPaddingLeft() - getPaddingRight() - monthTextBounds.width()) * 0.5f - monthTextBounds.left),
                headerPadding + getPaddingTop() + monthTextBounds.height() - monthTextBoundsBottom
        );

        yearTextPaint.getTextBounds(yearText, 0, yearText.length(), yearTextBounds);

        // We use this to properly place the grid underneath
        int yearTextBoundsBottom = yearTextBounds.bottom;

        // This sets the bounds ready to draw onto the canvas with just the left and top values
        yearTextBounds.offsetTo(
                (int) (getPaddingLeft() + (width - getPaddingLeft() - getPaddingRight() - yearTextBounds.width()) * 0.5f - yearTextBounds.left),
                monthTextBounds.top + monthTextBoundsBottom + yearTextBounds.height() - yearTextBoundsBottom + monthYearPadding
        );

        gridStartY = headerPadding + yearTextBounds.top + yearTextBoundsBottom;
        gridCellWidth = (width - getPaddingLeft() - getPaddingRight()) / weekdaysTexts.length;
        gridCellHeight = (height - getPaddingBottom() - gridStartY) / MAX_GRID_ROWS;

        // Set up the weekdays' bounds
        for (int i = 0; i < weekdaysTexts.length; i++) {
            Rect cell = gridTextPositions[0][i];
            weekdayTextPaint.getTextBounds(weekdaysTexts[i], 0, weekdaysTexts[i].length(), cell);
            cell.offsetTo(
                    (int) (getPaddingLeft() + (gridCellWidth - cell.width()) * 0.5f - cell.left + gridCellWidth * i),
                    (int) (gridStartY + (gridCellHeight + cell.height()) * 0.5f - cell.bottom)
            );
        }

        // Set up the days' bounds
        for (int i = 1; i < gridTextPositions.length; i++) {
            for (int j = 0; j < gridTextPositions[i].length; j++) {
                Rect cell = gridTextPositions[i][j];
                String dayText = CalendarUtils.getDayString(startOfCalendarDay);
                if (monthRange.intersects(startOfCalendarDay)) {
                    dayTextPaint.getTextBounds(dayText, 0, dayText.length(), cell);
                } else {
                    dayNotInMonthTextPaint.getTextBounds(dayText, 0, dayText.length(), cell);
                }

                cell.offsetTo(
                        (int) (getPaddingLeft() + (gridCellWidth - cell.width()) * 0.5f - cell.left + gridCellWidth * j),
                        (int) (gridStartY + (gridCellHeight + cell.height()) * 0.5f - cell.bottom + gridCellHeight * i)
                );

                startOfCalendarDay.add(Calendar.DATE, 1);
            }
        }

        // Reset the day so we don't need to recreate the calendar object
        startOfCalendarDay.add(Calendar.DATE, -MAX_GRID_COLUMNS * (MAX_GRID_ROWS - 1));

        if (isDebugMode) {
            for (int i = 0; i < MAX_GRID_ROWS; i++) {
                for (int j = 0; j < MAX_GRID_COLUMNS; j++) {
                    Rect cell = gridLines[i][j];
                    cell.left = (int) (getPaddingLeft() + gridCellWidth * j);
                    cell.top = (int) (gridStartY + gridCellHeight * i);
                    cell.right = cell.left + (int) gridCellWidth;
                    cell.bottom = cell.top + (int) gridCellHeight;
                }
            }
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw the background color
        canvas.drawColor(backgroundPaint.getColor());

        // Draw the month text
        canvas.drawText(monthText, monthTextBounds.left, monthTextBounds.top, monthTextPaint);

        // Draw the year text
        canvas.drawText(yearText, yearTextBounds.left, yearTextBounds.top, yearTextPaint);

        if (isDebugMode) {
            // Draw the grid for debugging
            for (int i = 0; i < MAX_GRID_ROWS; i++) {
                for (int j = 0; j < MAX_GRID_COLUMNS; j++) {
                    canvas.drawRect(gridLines[i][j], debugLinesPaint);
                }
            }
        }

        // Draw the weekdays texts
        for (int i = 0; i < weekdaysTexts.length; i++) {
            Rect cell = gridTextPositions[0][i];
            canvas.drawText(weekdaysTexts[i], cell.left, cell.top, weekdayTextPaint);
        }

        // Draw the days texts
        for (int i = 1; i < gridTextPositions.length; i++) {
            for (int j = 0; j < gridTextPositions[i].length; j++) {
                Rect cell = gridTextPositions[i][j];
                canvas.drawText(CalendarUtils.getDayString(startOfCalendarDay), cell.left, cell.top,
                        monthRange.intersects(startOfCalendarDay) ? dayTextPaint : dayNotInMonthTextPaint);

                // Draw the today circle indicator
                if (CalendarUtils.isSameDay(startOfCalendarDay, today)) {
                    canvas.drawCircle(getPaddingLeft() + gridCellWidth * (j + 0.5f), gridStartY + gridCellHeight * (i + 0.5f), todayCircleRadius, todayCirclePaint);
                }

                startOfCalendarDay.add(Calendar.DATE, 1);
            }
        }

        // Reset the day so we don't need to recreate the calendar object
        startOfCalendarDay.add(Calendar.DATE, -MAX_GRID_COLUMNS * (MAX_GRID_ROWS - 1));
    }
}
