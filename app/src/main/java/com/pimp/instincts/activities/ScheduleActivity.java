/*
 * Instincts
 * Copyright (C) 2017  Adithya J
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.pimp.instincts.activities;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.pimp.instincts.R;
import com.pimp.instincts.model.LocalJSONSource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity implements WeekView.EventClickListener,
        MonthLoader.MonthChangeListener, WeekView.EventLongPressListener {

    private WeekView weekView;
    private Date march9Date, march10Date, march11Date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        LocalJSONSource localJSONSource = new LocalJSONSource(this);

        weekView = (WeekView) findViewById(R.id.weekView);
        weekView.setOnEventClickListener(this);
        weekView.setMonthChangeListener(this);
        weekView.setEventLongPressListener(this);
        //weekView.setNumberOfVisibleDays(1);

        setupDateTimeInterpreter();

        String march9String = "09-03-2017 09:00:00";
        String march10String = "10-03-2017 09:00:00";
        String march11String = "11-03-2017 09:00:00";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        try {
            march9Date = simpleDateFormat.parse(march9String);
            march10Date = simpleDateFormat.parse(march10String);
            march11Date = simpleDateFormat.parse(march11String);
            Calendar calendar = dateToCalendar(march9Date);
            weekView.goToDate(calendar);
        } catch (Exception e) {
            Log.e("DATE", e.toString());
        }
    }

    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    private void setupDateTimeInterpreter() {
        weekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> events = new ArrayList<>();

        Calendar startTime = dateToCalendar(march9Date);
        startTime.set(Calendar.HOUR_OF_DAY, 9);
        startTime.set(Calendar.MINUTE, 0);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 11);
        WeekViewEvent weekViewEvent = new WeekViewEvent(1, "INSTINCTS INAUGURAL", startTime, endTime);
        weekViewEvent.setColor(getResources().getColor(R.color.colorPrimary));
        weekViewEvent.setLocation("\nMAIN AUDITORIUM");
        events.add(weekViewEvent);

        startTime = dateToCalendar(march9Date);
        startTime.set(Calendar.HOUR_OF_DAY, 11);
        startTime.set(Calendar.MINUTE, 0);
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 13);
        weekViewEvent = new WeekViewEvent(1, "SARAAL MAGAZINE RELEASE", startTime, endTime);
        weekViewEvent.setColor(getResources().getColor(R.color.colorAccent));
        weekViewEvent.setLocation("\nMAIN AUDITORIUM");
        events.add(weekViewEvent);

        startTime = dateToCalendar(march9Date);
        startTime.set(Calendar.HOUR_OF_DAY, 14);
        startTime.set(Calendar.MINUTE, 0);
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 17);
        weekViewEvent = new WeekViewEvent(1, "STUDENT VARIETY SHOW FINALS", startTime, endTime);
        weekViewEvent.setColor(getResources().getColor(R.color.colorPrimary));
        weekViewEvent.setLocation("\nMAIN AUDITORIUM");
        events.add(weekViewEvent);

        List<WeekViewEvent> matchedEvents = new ArrayList<>();
        for (WeekViewEvent event : events) {
            if (eventMatches(event, newYear, newMonth)) {
                matchedEvents.add(event);
            }
        }
        return matchedEvents;
    }

    @Override
    public void onEventClick(WeekViewEvent weekViewEvent, RectF rectF) {
        Toast.makeText(this, weekViewEvent.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventLongPress(WeekViewEvent weekViewEvent, RectF rectF) {

    }

    private boolean eventMatches(WeekViewEvent event, int year, int month) {
        return (event.getStartTime().get(Calendar.YEAR) == year &&
                event.getStartTime().get(Calendar.MONTH) == month - 1) ||
                (event.getEndTime().get(Calendar.YEAR) == year &&
                        event.getEndTime().get(Calendar.MONTH) == month - 1);
    }
}
