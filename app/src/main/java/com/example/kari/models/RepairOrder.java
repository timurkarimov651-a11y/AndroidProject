package com.example.kari.models;

import java.io.Serializable;

public class RepairOrder implements Serializable {
    private int id;
    private String ownerName;
    private String phone;
    private String carModel;
    private String date;
    private String time;
    private String problem;
    private String status;

    public RepairOrder() {}

    public RepairOrder(String ownerName, String phone, String carModel, String date, String time, String problem) {
        this.ownerName = ownerName;
        this.phone = phone;
        this.carModel = carModel;
        this.date = date;
        this.time = time;
        this.problem = problem;
        this.status = "active";
    }

    // ДОБАВЛЕН КОНСТРУКТОР С ID И ВРЕМЕНЕМ
    public RepairOrder(int id, String ownerName, String phone, String carModel, String date, String time, String problem, String status) {
        this.id = id;
        this.ownerName = ownerName;
        this.phone = phone;
        this.carModel = carModel;
        this.date = date;
        this.time = time;
        this.problem = problem;
        this.status = status;
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getCarModel() { return carModel; }
    public void setCarModel(String carModel) { this.carModel = carModel; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getProblem() { return problem; }
    public void setProblem(String problem) { this.problem = problem; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}