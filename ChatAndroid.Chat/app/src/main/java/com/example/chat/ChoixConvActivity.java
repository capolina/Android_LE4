package com.example.chat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChoixConvActivity extends RestActivity {

    private ListeConversations listeConvs;

    @Override
    public void traiteReponse(JSONObject o, String action){
        if (action == "recupConversations") {
            gs.alerter(o.toString());

            // On transforme notre objet JSON en une liste de "Conversations"
            // On pourrait utiliser la librairie GSON pour automatiser ce processus d'interprétation
            // des objets JSON reçus
            // Cf. poly de centrale "programmation mobile et réalité augmentée"

            // Ici, on se contente de créer une classe "Conversation" et une classe "ListeConversations"
            // On parcourt l’objet JSON pour instancier des Conversations, que l’on insère dans la liste

            /*
             * {"connecte":true,
             * "action":"getConversations",
             * "feedback":"entrez action: logout, setPasse(passe),setPseudo(pseudo), setCouleur(couleur),getConversations, getMessages(idConv,[idLastMessage]), setMessage(idConv,contenu), ...",
             * "conversations":[ {"id":"12","active":"1","theme":"Les cours en IAM"},
             *                   {"id":"2","active":"1","theme":"Ballon d'Or"}]}
             * */

            int i;
            JSONArray convs = null;
            try {
                convs = o.getJSONArray("conversations");
                for(i=0;i<convs.length();i++) {
                    JSONObject nextConv = (JSONObject) convs.get(i);

                    int id =Integer.parseInt(nextConv.getString("id"));
                    String theme = nextConv.getString("theme");
                    Boolean active = ((String) nextConv.getString("active")).contentEquals("1");

                    gs.alerter("Conv " + id  + " theme = " + theme + " active ?" + active);
                    Conversation c = new Conversation(id, theme, active);

                    listeConvs.addConversation(c);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            gs.alerter(listeConvs.toString());

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_conversation);

        listeConvs = new ListeConversations();

        //Au démarrage de l'activité, réalirer une requete
        //Pour récupérer les conversations
        String qs = "action=getConversations";

        //On se sert des services offerts par RestActivity
        //qui propose des méthodes d'envoi des requetes asynchrones
        envoiRequete(qs, "recupConversations");
    }
}
