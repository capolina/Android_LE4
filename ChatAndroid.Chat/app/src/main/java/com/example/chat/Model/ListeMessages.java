package com.example.chat.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListeMessages extends RestResponse {

    /*
     * {"connecte":true,
     *  "action":"getMessages",
     *  "feedback":"entrez action: logout, setPasse(passe),setPseudo(pseudo), setCouleurDiese(couleurRGV sans diese), setCouleur(couleur string),getConversations, getMessages(idConv,[idLastMessage]), setMessage(idConv,contenu), ...",
     *  "messages":[{"id":"1","contenu":"message","auteur":"user","couleur":"#"},
     *              {"id":"2","contenu":"message","auteur":"user","couleur":"#"},
     *              {"id":"23","contenu":"balbla","auteur":"user","couleur":"#"},
     *              {"id":"32","contenu":"Varane ballon d'or","auteur":"user","couleur":"#"},
     *              {"id":"33","contenu":"Test","auteur":"user","couleur":"#"}],
     *  "idLastMessage":"33"}
     * */

    @SerializedName("messages")
    private ArrayList<Message> list;
    private int idLastMessage;

    public ListeMessages() {
        super();
        this.list = new ArrayList<>();
    }

    public ArrayList<Message> getList() {
        return list;
    }

    public int getIdLastMessage() {
        return idLastMessage;
    }


    @Override
    public String toString() {
        return "ListeMessages{" +
                super.toString() +
                ", list=" + list +
                ", idLastMessage=" + idLastMessage +
                '}';
    }
}