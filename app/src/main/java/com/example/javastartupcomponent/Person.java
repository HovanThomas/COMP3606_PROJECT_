package com.example.javastartupcomponent;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String phone;
    private int score;
    private String date;

    public Person(String name, String phone, int score) {
        this.name = (name != null && !name.trim().isEmpty()) ? name : "Anonymous";
        this.phone = (phone != null && !phone.trim().isEmpty()) ? phone : "N/A";
        this.score = score;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        this.date = sdf.format(new Date());
    }

    public Person(String name, String phone, int score, String date) {
        this.name = name;
        this.phone = phone;
        this.score = score;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Player: " + name + " | Phone: " + phone + " | Score: " + score + " | Date: " + date;
    }
}
