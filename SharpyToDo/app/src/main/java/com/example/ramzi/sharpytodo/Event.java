package com.example.ramzi.sharpytodo;

/**
 * Created by ramzi on 01/12/16.
 */
public class Event {
    private int id;
    private String name;
    private float lat;
    private float lng;
    private String login;
    private String time;

    public Event(int id, String name, float lat, float lng, String time, String login) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.login = login;
        this.time = time;
    }
    public Event(int id, String name, float lat, float lng, String time) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.time = time;

    }
    public Event(String name, float lat, float lng, String login, String time) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.login = login;
        this.time = time;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
