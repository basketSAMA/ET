package com.example.a92317.et;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Emo {
    private int id;
    private String sentence;
    private String time;
    private String label;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getSentence() {
        return sentence;
    }
    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public void setTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        String str = formatter.format(new Date(System.currentTimeMillis()));
        this.time = str;
    }

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
}
