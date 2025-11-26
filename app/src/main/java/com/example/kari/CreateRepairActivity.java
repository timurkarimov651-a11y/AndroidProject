package com.example.kari;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.kari.database.DatabaseHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateRepairActivity extends AppCompatActivity {
    private EditText etOwnerName, etPhone, etCarModel, etDate, etTime, etProblem;
    private Button btnCreate, btnBack;
    private TextView tvCurrentDate;
    private DatabaseHelper db;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_repair);

        db = new DatabaseHelper(this);
        calendar = Calendar.getInstance();
        initViews();
        setupValidation();
        setCurrentDateTime();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCurrentDateTime();
    }

    private void initViews() {
        etOwnerName = findViewById(R.id.et_owner_name);
        etPhone = findViewById(R.id.et_phone);
        etCarModel = findViewById(R.id.et_car_model);
        etDate = findViewById(R.id.et_date);
        etTime = findViewById(R.id.et_time);
        etProblem = findViewById(R.id.et_problem);
        btnCreate = findViewById(R.id.btn_create);
        btnBack = findViewById(R.id.btn_back);
        tvCurrentDate = findViewById(R.id.tv_current_date);

        setCurrentDateTime();

        btnCreate.setOnClickListener(v -> createRepairOrder());
        btnBack.setOnClickListener(v -> finish());

        // Обработчики для выбора даты и времени
        etDate.setOnClickListener(v -> showDateTimePicker());
        etTime.setOnClickListener(v -> showDateTimePicker());
    }

    private void setCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        String currentTime = timeFormat.format(new Date());

        tvCurrentDate.setText("Сегодня: " + currentDate + " " + currentTime);

        // Устанавливаем текущие дату и время по умолчанию
        if (etDate.getText().toString().isEmpty()) {
            etDate.setText(currentDate);
        }
        if (etTime.getText().toString().isEmpty()) {
            etTime.setText(currentTime);
        }
    }

    private void showDateTimePicker() {
        DateTimePickerDialog dateTimeDialog = new DateTimePickerDialog(this,
                (date, time) -> {
                    // Проверяем, что выбранная дата не раньше завтрашнего дня
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
                        Date selectedDateTime = sdf.parse(date + " " + time);
                        Calendar selectedCal = Calendar.getInstance();
                        selectedCal.setTime(selectedDateTime);

                        Calendar tomorrow = Calendar.getInstance();
                        tomorrow.add(Calendar.DAY_OF_MONTH, 1);
                        tomorrow.set(Calendar.HOUR_OF_DAY, 0);
                        tomorrow.set(Calendar.MINUTE, 0);
                        tomorrow.set(Calendar.SECOND, 0);
                        tomorrow.set(Calendar.MILLISECOND, 0);

                        if (selectedCal.before(tomorrow)) {
                            Toast.makeText(CreateRepairActivity.this, "Нельзя выбрать прошедшую дату", Toast.LENGTH_SHORT).show();
                            setCurrentDateTime(); // Устанавливаем текущую дату
                        } else {
                            etDate.setText(date);
                            etTime.setText(time);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                });
        dateTimeDialog.show();
    }

    private void setupValidation() {
        // Валидация телефона
        etPhone.setInputType(android.text.InputType.TYPE_CLASS_PHONE);

        // Автоматическая расстановка точек в дате
        etDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String current = s.toString();
                if (current.length() == 2 || current.length() == 5) {
                    if (count > before) {
                        etDate.setText(current + ".");
                        etDate.setSelection(current.length() + 1);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Автоматическая расстановка двоеточия во времени
        etTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String current = s.toString();
                if (current.length() == 2 && count > before) {
                    etTime.setText(current + ":");
                    etTime.setSelection(current.length() + 1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void createRepairOrder() {
        String ownerName = etOwnerName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String carModel = etCarModel.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String problem = etProblem.getText().toString().trim();

        if (validateInput(ownerName, phone, carModel, date, time, problem)) {
            boolean inserted = db.addRepairOrder(ownerName, phone, carModel, date, time, problem);
            if (inserted) {
                Toast.makeText(this, "Заявка создана", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Ошибка создания заявки", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInput(String name, String phone, String carModel,
                                  String date, String time, String problem) {
        if (name.isEmpty() || phone.isEmpty() || carModel.isEmpty() ||
                date.isEmpty() || time.isEmpty() || problem.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phone.length() != 11) {
            etPhone.setError("Номер должен содержать 11 цифр");
            return false;
        }
        if (!isValidDate(date)) {
            etDate.setError("Неверная дата");
            return false;
        }
        if (!isValidTime(time)) {
            etTime.setError("Неверное время");
            return false;
        }

        // Дополнительная проверка даты и времени
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            Date selectedDateTime = sdf.parse(date + " " + time);
            Calendar selectedCal = Calendar.getInstance();
            selectedCal.setTime(selectedDateTime);

            Calendar tomorrow = Calendar.getInstance();
            tomorrow.add(Calendar.DAY_OF_MONTH, 1);
            tomorrow.set(Calendar.HOUR_OF_DAY, 0);
            tomorrow.set(Calendar.MINUTE, 0);
            tomorrow.set(Calendar.SECOND, 0);
            tomorrow.set(Calendar.MILLISECOND, 0);

            if (selectedCal.before(tomorrow)) {
                etDate.setError("Нельзя выбрать прошедшую дату");
                return false;
            }
        } catch (ParseException e) {
            etDate.setError("Неверная дата или время");
            return false;
        }

        return true;
    }

    private boolean isValidDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            sdf.setLenient(false);
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isValidTime(String time) {
        return time.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$");
    }
}