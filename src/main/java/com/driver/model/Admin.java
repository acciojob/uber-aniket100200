package com.driver.model;

import javax.persistence.*;

@Entity
@Table
public class Admin
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String username;
    String password;
    public  Admin()
    {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}