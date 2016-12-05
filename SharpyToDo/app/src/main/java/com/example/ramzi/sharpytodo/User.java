package com.example.ramzi.sharpytodo;

/**
 * Created by ramzi on 01/12/16.
 */
public class User {
    private String login;
    private String name;
    private String passwd;

    public User(String login, String name, String passwd) {
        this.login = login;
        this.name = name;
        this.passwd = passwd;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
