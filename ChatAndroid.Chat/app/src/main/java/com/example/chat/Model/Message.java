package com.example.chat.Model;


import com.google.gson.annotations.SerializedName;

public class Message {

    /*
     * {"conversationId":1,
     * "active":true,
     * "theme":"Ma nouvelle conversation"
     * ,"messages":[
     *      {"messageId":6,
     *      "userName":"Coco",
     *      "couleur":"black",
     *      "contenu":"My New First Message"},
     *      {"messageId":7,"userName":"Coco","couleur":"black","contenu":"My New Second Message"},{"messageId":8,"userName":"Coco","couleur":"black","contenu":"My New Second Message"},{"messageId":9,"userName":"Coco","couleur":"black","contenu":"My New Second Message"},{"messageId":10,"userName":"Coco","couleur":"black","contenu":"My New Second Message"},{"messageId":11,"userName":"Coco","couleur":"black","contenu":"My New Message"},{"messageId":12,"userName":"Coco","couleur":"black","contenu":"My New Message"}]}
     * */

    @SerializedName("messageId")
    private int id;
    private String contenu;
    @SerializedName("userName")
    private String auteur;
    private String couleur;

    public Message() {
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", contenu='" + contenu + '\'' +
                ", auteur='" + auteur + '\'' +
                ", couleur=" + couleur +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getContenu() {
        return contenu;
    }

    public String getAuteur() {
        return auteur;
    }

    public String getCouleur() {
        return couleur;
    }
}