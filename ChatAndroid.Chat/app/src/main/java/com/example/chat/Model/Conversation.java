package com.example.chat.Model;


import com.google.gson.annotations.SerializedName;

public class Conversation {

    /*
     * {"conversations":[
     *      {"conversationId":1,
     *          "active":true,
     *          "theme":"Ma nouvelle conversation",
     *          "messages":null},
     *       {"conversationId":2,
     *          "active":true,
     *          "theme":"Ma nouvelle conversation 2","messages":null},{"conversationId":3,"active":true,"theme":"Ma nouvelle conversation 2","messages":null},{"conversationId":4,"active":true,"theme":"Ma nouvelle conversation 3","messages":null}]}
     * */

    @SerializedName("conversationId")
    private int id;
    private String theme;
    private Boolean active;

    // Raccourci : Alt+Ins pour générer getters, setters et constructeurs


    public Conversation(int id, String theme, Boolean active) {
        this.id = id;
        this.theme = theme;
        this.active = active;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "id=" + id +
                ", theme='" + theme + '\'' +
                ", active=" + active +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getTheme() {
        return theme;
    }

    public Boolean getActive() {
        return active;
    }
}