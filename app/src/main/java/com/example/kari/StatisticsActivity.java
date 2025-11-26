package com.example.kari;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kari.adapters.DetailingAdapter;
import com.example.kari.adapters.RepairAdapter;
import com.example.kari.database.DatabaseHelper;
import com.example.kari.models.DetailingOrder;
import com.example.kari.models.RepairOrder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StatisticsActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private TextView tvTotalRepair, tvTotalDetailing, tvRepairInWork,
            tvDetailingInWork, tvRepairCompleted, tvDetailingCompleted, tvCurrentDate;
    private RecyclerView rvRepairOrders, rvDetailingOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        db = new DatabaseHelper(this);
        initViews();
        loadStatistics();
        loadAllOrders();
        setCurrentDate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStatistics();
        loadAllOrders();
        setCurrentDate();
    }

    private void initViews() {
        tvTotalRepair = findViewById(R.id.tv_total_repair);
        tvTotalDetailing = findViewById(R.id.tv_total_detailing);
        tvRepairInWork = findViewById(R.id.tv_repair_in_work);
        tvDetailingInWork = findViewById(R.id.tv_detailing_in_work);
        tvRepairCompleted = findViewById(R.id.tv_repair_completed);
        tvDetailingCompleted = findViewById(R.id.tv_detailing_completed);
        tvCurrentDate = findViewById(R.id.tv_current_date);

        rvRepairOrders = findViewById(R.id.rv_repair_orders);
        rvDetailingOrders = findViewById(R.id.rv_detailing_orders);

        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadStatistics() {
        tvTotalRepair.setText("Общее количество заявок на ремонт: " + db.getTotalRepairCount());
        tvTotalDetailing.setText("Общее количество заявок на детейлинг: " + db.getTotalDetailingCount());
        tvRepairInWork.setText("Заявки на ремонт в работе: " + db.getActiveRepairsCount());
        tvDetailingInWork.setText("Заявки на детейлинг в работе: " + db.getActiveDetailingCount());
        tvRepairCompleted.setText("Заявки на ремонт выполнены: " + db.getCompletedRepairsCount());
        tvDetailingCompleted.setText("Заявки на детейлинг выполнены: " + db.getCompletedDetailingCount());
    }

    private void loadAllOrders() {
        List<RepairOrder> repairOrders = db.getAllRepairOrders();
        List<DetailingOrder> detailingOrders = db.getAllDetailingOrders();

        RepairAdapter repairAdapter = new RepairAdapter(repairOrders);
        DetailingAdapter detailingAdapter = new DetailingAdapter(detailingOrders);

        rvRepairOrders.setLayoutManager(new LinearLayoutManager(this));
        rvDetailingOrders.setLayoutManager(new LinearLayoutManager(this));
        rvRepairOrders.setAdapter(repairAdapter);
        rvDetailingOrders.setAdapter(detailingAdapter);
    }

    private void setCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        tvCurrentDate.setText("Сегодня: " + currentDate);
    }
}