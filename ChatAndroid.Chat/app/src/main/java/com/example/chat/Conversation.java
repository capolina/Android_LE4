package com.example.chat;


public class Conversation {

    /*
     * {"connecte":true,
     * "action":"getConversations",
     * "feedback":"entrez action: logout, setPasse(passe),setPseudo(pseudo), setCouleur(couleur),getConversations, getMessages(idConv,[idLastMessage]), setMessage(idConv,contenu), ...",
     * "conversations":[ {"id":"12","active":"1","theme":"Les cours en IAM"},
     *                   {"id":"2","active":"1","theme":"Ballon d'Or"}]}
     * */

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