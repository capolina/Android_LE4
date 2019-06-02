package com.example.chat;


import android.graphics.Color;

public class Message {

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

    private int id;
    private String contenu;
    private String auteur;
    private Integer couleur;

    // Raccourci : Alt+Ins pour générer getters, setters et constructeurs

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

    public int getCouleur() {
        return couleur;
    }
}