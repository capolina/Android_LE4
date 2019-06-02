package com.example.chat.Model;

public class Login {

    private String token;

    public Login() {
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "Login{" +
                "token=" + token +
                '}';
    }
}
