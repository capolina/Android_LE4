package com.example.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.example.chat.Model.ListeMessages;
import com.example.chat.Model.Message;
import com.example.chat.TypeAdapter.ColorTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

public class ShowConvActivity extends RestActivity implements View.OnClickListener {

    private int idConv;
    private int idLastMessage;

    private LinearLayout msgLayout;
    private Button btnOK;
    private EditText edtMsg;

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

        requetePeriodique(10, Request.Method.GET, null, loadMessageCallBack());
        msgLayout = findViewById(R.id.conversation_svLayoutMessages);

        btnOK = findViewById(R.id.conversation_btnOK);
        btnOK.setOnClickListener(this);

        edtMsg = findViewById(R.id.conversation_edtMessage);

        idLastMessage = 0;
    }

    private void loadMessages(JSONObject o) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Integer.class, new ColorTypeAdapter());
        Gson mGson = builder.create();

        ListeMessages listeMessages = mGson.fromJson(o.toString(), ListeMessages.class);

        if (listeMessages.getList().size() > 0) {
            for (Message message : listeMessages.getList()) {
                addMessage(message);
            }
            idLastMessage = listeMessages.getList().get(listeMessages.getList().size()-1).getId();
        }

    }

    private void addMessage(Message msg) {
        TextView tv = new TextView(this);
        tv.setText("[" + msg.getAuteur() + "] " + msg.getContenu());
        tv.setTextColor(msg.getCouleur());

        msgLayout.addView(tv);
    }

    private Response.Listener<JSONObject> postMessageCallBack() {
        return new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject result) {
                gs.alerter("Message posté");
            }
        };
    }

    private Response.Listener<JSONObject> loadMessageCallBack() {
        return new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject result) {
                // On a reçu des messages
                // gs.alerter(o.toString());

                loadMessages(result);
            }
        };
    }

    public String urlPeriodique() {
        String qs = "";

        qs = "conversation/" + idConv;
        qs += "?idLastMessage=" + idLastMessage;

        return qs;
    }

    @Override
    public void onClick(View v) {
        // Clic sur OK : on récupère le message
        // conversation_edtMessage

        String msg = edtMsg.getText().toString();

        //Prevent from sending empty messages
        if (!msg.isEmpty()) {
            JSONObject request = new JSONObject();
            try {
                request.put("contenu", msg);
            } catch(JSONException e) {
                gs.alerter("Error while logging in");
            }


            String qs = "conversation/" + idConv + "/message";
            envoiRequete(qs, Request.Method.POST, request, postMessageCallBack());
            edtMsg.setText("");
        }

    }
}
