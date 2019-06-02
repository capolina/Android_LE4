package com.example.chat;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Login {

    private Boolean connecte;

    public Login() {
    }

    public Boolean getConnecte() {
        return connecte;
    }

    @Override
    public String toString() {
        return "Login{" +
                "connecte=" + connecte +
                '}';
    }
}
