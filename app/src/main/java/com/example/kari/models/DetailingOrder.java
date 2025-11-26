package com.example.kari.models;

import java.io.Serializable;

public class DetailingOrder implements Serializable {
    private int id;
    private String ownerName;
    private String phone;
    private String email;
    private String deviceModel;
    private String date;
    private String time;
    private String status;

    public DetailingOrder() {}

    public DetailingOrder(String ownerName, String phone, String email, String deviceModel, String date, String time) {
        this.ownerName = ownerName;
        this.phone = phone;
        this.email = email;
        this.deviceModel = deviceModel;
        this.date = date;
        this.time = time;
        this.status = "active";
    }

    // ДОБАВЛЕН КОНСТРУКТОР С ID И ВРЕМЕНЕМ
    public DetailingOrder(int id, String ownerName, String phone, String email, String deviceModel, String date, String time, String status) {
        this.id = id;
        this.ownerName = ownerName;
        this.phone = phone;
        this.email = email;
        this.deviceModel = deviceModel;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDeviceModel() { return deviceModel; }
    public void setDeviceModel(String deviceModel) { this.deviceModel = deviceModel; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}