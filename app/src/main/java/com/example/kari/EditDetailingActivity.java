package com.example.kari;

import android.app.AlertDialog;
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
import com.example.kari.models.DetailingOrder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditDetailingActivity extends AppCompatActivity {
    private EditText etOwnerName, etPhone, etEmail, etDeviceModel, etDate, etTime;
    private Button btnUpdate, btnBack, btnComplete, btnDelete;
    private TextView tvCurrentDate;
    private DatabaseHelper db;
    private Calendar calendar;
    private DetailingOrder detailingOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_detailing);

        db = new DatabaseHelper(this);
        calendar = Calendar.getInstance();

        detailingOrder = (DetailingOrder) getIntent().getSerializableExtra("detailing_order");

        if (detailingOrder == null) {
            Toast.makeText(this, "Ошибка загрузки заявки", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupValidation();
        fillData();
        setCurrentDate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCurrentDate();
    }

    private void initViews() {
        etOwnerName = findViewById(R.id.et_owner_name);
        etPhone = findViewById(R.id.et_phone);
        etEmail = findViewById(R.id.et_email);
        etDeviceModel = findViewById(R.id.et_device_model);
        etDate = findViewById(R.id.et_date);
        etTime = findViewById(R.id.et_time);
        btnUpdate = findViewById(R.id.btn_update);
        btnBack = findViewById(R.id.btn_back);
        btnComplete = findViewById(R.id.btn_complete);
        btnDelete = findViewById(R.id.btn_delete);
        tvCurrentDate = findViewById(R.id.tv_current_date);

        btnUpdate.setOnClickListener(v -> updateDetailingOrder());
        btnBack.setOnClickListener(v -> finish());
        btnComplete.setOnClickListener(v -> completeDetailingOrder());
        btnDelete.setOnClickListener(v -> deleteDetailingOrder());

        etDate.setOnClickListener(v -> showDateTimePicker());
        etTime.setOnClickListener(v -> showDateTimePicker());
    }

    private void fillData() {
        etOwnerName.setText(detailingOrder.getOwnerName());
        etPhone.setText(detailingOrder.getPhone());
        etEmail.setText(detailingOrder.getEmail());
        etDeviceModel.setText(detailingOrder.getDeviceModel());
        etDate.setText(detailingOrder.getDate());
        etTime.setText(detailingOrder.getTime());
    }

    private void setCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());
        tvCurrentDate.setText("Сегодня: " + currentDateTime);
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
                            Toast.makeText(EditDetailingActivity.this, "Нельзя выбрать прошедшую дату", Toast.LENGTH_SHORT).show();
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
            public void afterTextChanged(Editable s) {
                String date = s.toString();
                if (date.length() == 10) {
                    if (!isValidDate(date)) {
                        etDate.setError("Неверная дата");
                    } else {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                            Date selectedDate = sdf.parse(date);
                            Calendar selectedCal = Calendar.getInstance();
                            selectedCal.setTime(selectedDate);

                            Calendar tomorrow = Calendar.getInstance();
                            tomorrow.add(Calendar.DAY_OF_MONTH, 1);
                            tomorrow.set(Calendar.HOUR_OF_DAY, 0);
                            tomorrow.set(Calendar.MINUTE, 0);
                            tomorrow.set(Calendar.SECOND, 0);
                            tomorrow.set(Calendar.MILLISECOND, 0);

                            if (selectedCal.before(tomorrow)) {
                                etDate.setError("Нельзя выбрать прошедшую дату");
                            } else {
                                etDate.setError(null);
                            }
                        } catch (ParseException e) {
                            etDate.setError("Неверная дата");
                        }
                    }
                }
            }
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
            public void afterTextChanged(Editable s) {
                String time = s.toString();
                if (time.length() == 5 && !isValidTime(time)) {
                    etTime.setError("Неверное время");
                } else {
                    etTime.setError(null);
                }
            }
        });
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

    private void updateDetailingOrder() {
        String ownerName = etOwnerName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String deviceModel = etDeviceModel.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();

        if (validateInput(ownerName, phone, email, deviceModel, date, time)) {
            detailingOrder.setOwnerName(ownerName);
            detailingOrder.setPhone(phone);
            detailingOrder.setEmail(email);
            detailingOrder.setDeviceModel(deviceModel);
            detailingOrder.setDate(date);
            detailingOrder.setTime(time);

            boolean updated = db.updateDetailingOrder(detailingOrder);
            if (updated) {
                Toast.makeText(this, "Заявка обновлена", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Ошибка обновления заявки", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void completeDetailingOrder() {
        detailingOrder.setStatus("completed");
        boolean updated = db.updateDetailingOrder(detailingOrder);
        if (updated) {
            Toast.makeText(this, "Заявка завершена", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Ошибка завершения заявки", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteDetailingOrder() {
        new AlertDialog.Builder(this)
                .setTitle("Удаление заявки")
                .setMessage("Вы уверены, что хотите удалить эту заявку на детейлинг?")
                .setPositiveButton("Удалить", (dialog, which) -> {
                    boolean deleted = db.deleteDetailingOrder(detailingOrder.getId());
                    if (deleted) {
                        Toast.makeText(this, "Заявка удалена", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Ошибка удаления заявки", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private boolean validateInput(String name, String phone, String email,
                                  String deviceModel, String date, String time) {
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() ||
                deviceModel.isEmpty() || date.isEmpty() || time.isEmpty()) {
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

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Введите корректный email");
            return false;
        }
        return true;
    }
}