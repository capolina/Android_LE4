package com.example.chat.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListeConversations {

    @SerializedName("conversations")
    private ArrayList<Conversation> list;

    public ListeConversations() {
        super();
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
