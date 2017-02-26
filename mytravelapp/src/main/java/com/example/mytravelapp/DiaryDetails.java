package com.example.mytravelapp;

/**
 * Created by Mavis on 26/2/2017.
 */

public class DiaryDetails {
    // Diary basic variables
    private int diary_id;
    private String diary_title;
    private String diary_location;
    private String diary_image;
    private String diary_info;
    private String diary_date;

    // Constructor
    public DiaryDetails(int diary_id, String diary_title, String diary_location, String diary_image, String diary_info, String diary_date){
        this.diary_id = diary_id;
        this.diary_title = diary_title;
        this.diary_location = diary_location;
        this.diary_image = diary_image;
        this.diary_info = diary_info;
        this.diary_date = diary_date;
    }

    // Getters
    public int getDiary_id() { return diary_id; }
    public String getDiary_title() { return diary_title; }
    public String getDiary_location() { return diary_location; }
    public String getDiary_image() { return diary_image; }
    public String getDiary_info() { return diary_info; }
    public String getDiary_date() { return diary_date; }
}
