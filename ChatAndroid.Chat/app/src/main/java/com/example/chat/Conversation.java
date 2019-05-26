package com.example.chat;


public class Conversation {

    private int id;
    private String nom;
    private int active;

    public String toString()
    {
        return nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public Conversation(int id, String nom,int active) {
        this.id = id;
        this.nom = nom;
        this.active = active;
    }

}