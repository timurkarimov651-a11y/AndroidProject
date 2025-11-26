package com.example.kari;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.kari.database.DatabaseHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Button btnCreateRepair, btnCreateDetailing, btnStatistics;
    private TextView tvActiveRepairs, tvActiveDetailing, tvCurrentDate;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        initViews();
        updateActiveOrders();
        setCurrentDate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateActiveOrders();
        setCurrentDate();
    }

    private void initViews() {
        btnCreateRepair = findViewById(R.id.btn_create_repair);
        btnCreateDetailing = findViewById(R.id.btn_create_detailing);
        btnStatistics = findViewById(R.id.btn_statistics);
        tvActiveRepairs = findViewById(R.id.tv_active_repairs);
        tvActiveDetailing = findViewById(R.id.tv_active_detailing);
        tvCurrentDate = findViewById(R.id.tv_current_date);

        btnCreateRepair.setOnClickListener(v ->
                startActivity(new Intent(this, CreateRepairActivity.class)));
        btnCreateDetailing.setOnClickListener(v ->
                startActivity(new Intent(this, CreateDetailingActivity.class)));
        btnStatistics.setOnClickListener(v ->
                startActivity(new Intent(this, StatisticsActivity.class)));
    }

    private void updateActiveOrders() {
        int activeRepairs = db.getActiveRepairsCount();
        int activeDetailing = db.getActiveDetailingCount();
        tvActiveRepairs.setText("Активные заявки на ремонт: " + activeRepairs);
        tvActiveDetailing.setText("Активные заявки на детейлинг: " + activeDetailing);
    }

    private void setCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        tvCurrentDate.setText("Сегодня: " + currentDate);
    }
}