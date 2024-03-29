package com.example.chat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.chat.Model.Conversation;
import com.example.chat.Model.Message;
import com.google.gson.Gson;

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

        //gs.alerter(Integer.toString(idConv));

        // On récupère la liste des messages périodiquement
        // action=getMessages&idConv=<ID>

        // NB : l'API fournit une route
        // qui permet d'indiquer le dernier message dont on dispose
        // action=getMessages&idConv=<ID>&idLastMessage=<NUMERO>

        requetePeriodique(5, "recupMessages", Request.Method.GET, null);
        msgLayout = findViewById(R.id.conversation_svLayoutMessages);

        btnOK = findViewById(R.id.conversation_btnOK);
        btnOK.setOnClickListener(this);

        edtMsg = findViewById(R.id.conversation_edtMessage);

        idLastMessage = 0;
    }

    @Override
    public void successCallBack(JSONObject result, String action)
    {
        if(action.contentEquals("recupMessages"))
        {
            loadMessages(result);
        }
        else if(action.contentEquals("postMessage"))
        {
            postMessageCallBack();
        }
    }

    private void loadMessages(JSONObject o) {
        Gson mGson = new Gson();
        Conversation conversation = mGson.fromJson(o.toString(), Conversation.class);
        TextView titre = findViewById(R.id.conversation_titre);
        titre.setText(conversation.getTheme());

        if (conversation.getMessages().size() > 0) {
            for (Message message : conversation.getMessages()) {
                addMessage(message);
            }
            idLastMessage = conversation.getMessages().get(conversation.getMessages().size()-1).getId();
        }

    }

    private void addMessage(Message msg) {
        TextView tv = new TextView(this);
        tv.setText("[" + msg.getAuteur() + "] " + msg.getContenu());
        tv.setTextColor(Color.parseColor(msg.getCouleur()));

        msgLayout.addView(tv);
    }

    private void postMessageCallBack() {
        gs.alerter("Message posté");
        sendLoadMessageRequest();
    }

    public String urlPeriodique(String action) {
        if( action.contentEquals("recupMessages")) {
            String qs = "";

            qs = "conversation/" + idConv;
            qs += "?idLastMessage=" + idLastMessage;

            return qs;
        }
        return "";
    }

    private void sendLoadMessageRequest() {
        String qs = urlPeriodique("recupMessages");
        envoiRequete(qs, "recupMessages", Request.Method.GET, null);
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
                gs.alerter("Error while sending the message");
            }


            String qs = "conversation/" + idConv + "/message";
            envoiRequete(qs, "postMessage", Request.Method.POST, request);
            edtMsg.setText("");
        }

    }
}
