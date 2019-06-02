package com.example.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.example.chat.Model.Conversation;
import com.example.chat.Model.ListeConversations;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

public class ChoixConvActivity extends RestActivity implements View.OnClickListener {

    private ListeConversations listeConvs;
    private Button btnOK;
    private Spinner sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_conversation);

        // Au démarrage de l'activité, réaliser une requete
        // Pour récupérer les conversations
        String qs = "conversation";

        // On se sert des services offerts par RestActivity,
        // qui propose des méthodes d'envoi de requetes asynchrones
        envoiRequete(qs, Request.Method.GET, null, loadConversationsCallBack());

        listeConvs = new ListeConversations();

        btnOK = findViewById(R.id.choixConversation_btnOK);
        btnOK.setOnClickListener(this);

        sp = (Spinner) findViewById(R.id.choixConversation_choixConv);

    }

    private void chargerConvs(JSONObject o) {
        gs.alerter(o.toString());
        // On transforme notre objet JSON en une liste de "Conversations"
        // On pourrait utiliser la librairie GSON pour automatiser ce processus d'interprétation
        // des objets JSON reçus
        // Cf. poly de centrale "programmation mobile et réalité augmentée"

        // Ici, on se contente de créer une classe "Conversation" et une classe "ListeConversations"
        // On parcourt l’objet JSON pour instancier des Conversations, que l’on insère dans la liste

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

        Gson mGson = new Gson();

        listeConvs = mGson.fromJson(o.toString(), ListeConversations.class);

        gs.alerter(listeConvs.toString());

        // On peut maintenant appuyer sur le bouton
        btnOK.setEnabled(true);
        remplirSpinner();
    }

    private Response.Listener<JSONObject> loadConversationsCallBack() {
        return new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject result) {
                // On a reçu des messages
                // gs.alerter(o.toString());

                chargerConvs(result);
            }
        };
    }

    private void remplirSpinner() {

        // V2 : Utilisation d'un adapteur customisé qui permet de définir nous-même
        // la forme des éléments à afficher

        Spinner sp = (Spinner) findViewById(R.id.choixConversation_choixConv);

        sp.setAdapter(new MyCustomAdapter(this,
                R.layout.spinner_item,
                listeConvs.getList()));
    }

    @Override
    public void onClick(View v) {
        // lors du clic sur le bouton OK,
        // récupérer l'id de la conversation sélectionnée
        // démarrer l'activité d'affichage des messages

        // NB : il faudrait être sur qu'on ne clique pas sur le bouton
        // tant qu'on a pas fini de charger la liste des conversations
        // On indique que le bouton est désactivé au départ.

        Conversation convSelected = (Conversation) sp.getSelectedItem();
        gs.alerter("Conv sélectionnée : " + convSelected.getTheme()
                + " id=" + convSelected.getId());

        // On crée un Intent pour changer d'activité
        Intent toShowConv = new Intent(this, ShowConvActivity.class);
        Bundle bdl = new Bundle();
        bdl.putInt("idConversation",convSelected.getId());
        toShowConv.putExtras(bdl);
        startActivity(toShowConv);
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
