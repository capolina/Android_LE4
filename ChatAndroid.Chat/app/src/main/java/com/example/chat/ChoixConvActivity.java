package com.example.chat;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
            JSONArray convs;
            try {
                convs = o.getJSONArray("conversations");
                for(i=0;i<convs.length();i++) {
                    JSONObject nextConv = (JSONObject) convs.get(i);

                    int id =Integer.parseInt(nextConv.getString("id"));
                    String theme = nextConv.getString("theme");
                    Boolean active = (nextConv.getString("active")).contentEquals("1");

                    gs.alerter("Conv " + id  + " theme = " + theme + " active ?" + active);
                    Conversation c = new Conversation(id, theme, active);

                    listeConvs.addConversation(c);
                }
                remplirSpinner();
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

    private void remplirSpinner() {

        // V2 : Utilisation d'un adapteur customisé qui permet de définir nous-même
        // la forme des éléments à afficher

        Spinner sp = (Spinner) findViewById(R.id.choixConversation_choixConv);

        sp.setAdapter(new MyCustomAdapter(this,
                R.layout.spinner_item,
                listeConvs.getList()));
    }

    public class MyCustomAdapter extends ArrayAdapter<Conversation> {

        private int layoutId;
        private ArrayList<Conversation> dataConvs;

        public MyCustomAdapter(Context context,
                               int itemLayoutId,
                               ArrayList<Conversation> objects) {
            super(context, itemLayoutId, objects);
            layoutId = itemLayoutId;
            dataConvs = objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            //return getCustomView(position, convertView, parent);
            LayoutInflater inflater = getLayoutInflater();
            View item = inflater.inflate(layoutId, parent, false);
            Conversation nextC = dataConvs.get(position);

            TextView label = (TextView) item.findViewById(R.id.spinner_theme);
            label.setText(nextC.getTheme());

            ImageView icon = (ImageView) item.findViewById(R.id.spinner_icon);

            if (nextC.getActive()) {
                icon.setImageResource(R.drawable.icon36);
            } else {
                icon.setImageResource(R.drawable.icongray36);
            }

            return item;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //return getCustomView(position, convertView, parent);
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(layoutId, parent, false);
            Conversation nextC = dataConvs.get(position);

            TextView label = (TextView) row.findViewById(R.id.spinner_theme);
            label.setText(nextC.getTheme());

            ImageView icon = (ImageView) row.findViewById(R.id.spinner_icon);

            if (nextC.getActive()) {
                icon.setImageResource(R.drawable.icon);
            } else {
                icon.setImageResource(R.drawable.icongray);
            }

            return row;
        }
    }
}
