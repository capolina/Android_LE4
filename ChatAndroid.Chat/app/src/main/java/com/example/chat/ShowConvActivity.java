package com.example.chat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShowConvActivity extends RestActivity implements View.OnClickListener {

    private int idConv;
    private int idLastMessage = 0;

    private LinearLayout msgLayout;
    private Button btnOK;
    private EditText edtMsg;

    @Override
    public void traiteReponse(JSONObject o, String action) {
        if (action.contentEquals("posterMessage")) {
            gs.alerter("retour de la requete posterMessage");
        }
        if (action.contentEquals("chargement_messages")) {
            // On a reçu des messages
            // gs.alerter(o.toString());

            loadMessages(o);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_conversation);

        Bundle bdl = getIntent().getExtras();
        idConv = bdl.getInt("idConversation");

        gs.alerter(Integer.toString(idConv));

        // On récupère la liste des messages périodiquement
        // action=getMessages&idConv=<ID>

        // NB : l'API fournit une route
        // qui permet d'indiquer le dernier message dont on dispose
        // action=getMessages&idConv=<ID>&idLastMessage=<NUMERO>

        //requetePeriodique(10, "chargement_messages");
        msgLayout = findViewById(R.id.conversation_svLayoutMessages);

        btnOK = findViewById(R.id.conversation_btnOK);
        btnOK.setOnClickListener(this);

        edtMsg = findViewById(R.id.conversation_edtMessage);
    }

    private void loadMessages(JSONObject o) {
        try {
            // parcours des messages
            JSONArray messages = o.getJSONArray("messages");
            int i;
            for(i=0;i<messages.length();i++) {
                addMessage((JSONObject) messages.get(i));
            }

            // mise à jour du numéro du dernier message
            idLastMessage = Integer.parseInt(o.getString("idLastMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addMessage(JSONObject msg) throws JSONException {
        String contenu =  msg.getString("contenu");
        String auteur =  msg.getString("auteur");
        String couleur =  msg.getString("couleur");

        TextView tv = new TextView(this);
        tv.setText("[" + auteur + "] " + contenu);
        tv.setTextColor(Color.parseColor(couleur));

        msgLayout.addView(tv);
    }

    public String urlPeriodique() {
        String qs = "";

        qs = "action=getMessages&idConv=" + idConv;
        qs += "&idLastMessage=" + idLastMessage;

        return qs;
    }

    @Override
    public void onClick(View v) {
        // Clic sur OK : on récupère le message
        // conversation_edtMessage

        String msg = edtMsg.getText().toString();
        String qs="action=setMessage&idConv=" + idConv +"&contenu=" + msg;

        //envoiRequete(qs,"posterMessage");

        edtMsg.setText("");
    }
}
