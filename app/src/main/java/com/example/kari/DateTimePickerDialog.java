package com.example.kari;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.Locale;

public class DateTimePickerDialog {
    private Context context;
    private OnDateTimeSetListener listener;
    private Calendar calendar;

    public interface OnDateTimeSetListener {
        void onDateTimeSet(String date, String time);
    }

    public DateTimePickerDialog(Context context, OnDateTimeSetListener listener) {
        this.context = context;
        this.listener = listener;
        this.calendar = Calendar.getInstance();
    }

    public void show() {
        // Сначала показываем выбор даты
        new DatePickerDialog(context, this::onDateSet,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(year, month, dayOfMonth);

        // После выбора даты показываем выбор времени
        new TimePickerDialog(context, this::onTimeSet,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true)
                .show();
    }

    private void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        // Форматируем дату и время
        String date = String.format(Locale.getDefault(), "%02d.%02d.%04d",
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR));

        String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);

        // Передаем результат обратно
        if (listener != null) {
            listener.onDateTimeSet(date, time);
        }
    }
}