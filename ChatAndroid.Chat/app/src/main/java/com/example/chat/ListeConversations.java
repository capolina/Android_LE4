package com.example.chat;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ListeConversations {

    private Boolean connecte;
    @SerializedName("conversations")
    private ArrayList<Conversation> list;

    public ListeConversations() {
        this.list = new ArrayList<>();
    }

    public ArrayList<Conversation> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "ListeConversations{" +
                "list=" + list +
                '}';
    }

}
