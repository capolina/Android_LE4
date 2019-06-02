package com.example.chat.Model;

public class RestResponse {

    private Boolean connecte;
    private String action;
    private String feedback;

    public RestResponse() {
    }

    public Boolean getConnecte() {
        return connecte;
    }

    public String getAction() {
        return action;
    }

    public String getFeedback() {
        return feedback;
    }

    @Override
    public String toString() {
        return "RestResponse{" +
                "connecte=" + connecte +
                ", action='" + action + '\'' +
                ", feedback='" + feedback + '\'' +
                '}';
    }
}
