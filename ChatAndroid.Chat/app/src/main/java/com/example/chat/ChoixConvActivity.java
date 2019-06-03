package com.example.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.example.chat.Model.Conversation;
import com.example.chat.Model.ListeConversations;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

public class ChoixConvActivity extends RestActivity {

    private ListeConversations listeConvs;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_conversation);

        // Au démarrage de l'activité, réaliser une requete
        // Pour récupérer les conversations
        String qs = "conversation";

        gs.alerter("Chargement des conversations");
        // On se sert des services offerts par RestActivity,
        // qui propose des méthodes d'envoi de requetes asynchrones
        envoiRequete(qs, "recupConversations", Request.Method.GET, null);

        listeConvs = new ListeConversations();


        mRecyclerView = findViewById(R.id.choixConversation_choixConv);
        //mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    @Override
    public void successCallBack(JSONObject result, String action)
    {
        if(action.contentEquals("recupConversations"))
        {
            chargerConvs(result);
        }
    }

    private void chargerConvs(JSONObject o) {
        Gson mGson = new Gson();

        listeConvs = mGson.fromJson(o.toString(), ListeConversations.class);

        //gs.alerter(listeConvs.toString());

        // On peut maintenant appuyer sur le bouton
        //btnOK.setEnabled(true);
        remplirRecyclerView();
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

    private void remplirRecyclerView() {

        mAdapter = new MyAdapter(listeConvs.getList(), this);
        mRecyclerView.setAdapter(mAdapter);
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements View.OnClickListener {

        private List<Conversation> dataModelList;
        private Context mContext;

        // View holder class whose objects represent each list item

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView spinner_icon;
            public TextView spinner_theme;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                spinner_icon = itemView.findViewById(R.id.spinner_icon);
                spinner_theme = itemView.findViewById(R.id.spinner_theme);
            }

            public void bindData(Conversation conversation, Context context) {
                if (conversation.getActive()) {
                    spinner_icon.setImageResource(R.drawable.icon36);
                } else {
                    spinner_icon.setImageResource(R.drawable.icongray36);
                }
                spinner_theme.setText(conversation.getTheme());
            }
        }

        public MyAdapter(List<Conversation> modelList, Context context) {
            dataModelList = modelList;
            mContext = context;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate out card list item

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.spinner_item, parent, false);
            // Return a new view holder
            view.setOnClickListener(this);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            // Bind data for the item at position

            holder.bindData(dataModelList.get(position), mContext);
        }

        @Override
        public int getItemCount() {
            // Return the total number of items

            return dataModelList.size();
        }

        @Override
        public void onClick(final View view) {
            int itemPosition = mRecyclerView.getChildLayoutPosition(view);
            Conversation convSelected = listeConvs.getList().get(itemPosition);

            gs.alerter("Conv sélectionnée : " + convSelected.getTheme()
                    + " id=" + convSelected.getId());

            // On crée un Intent pour changer d'activité
            Intent toShowConv = new Intent(mContext, ShowConvActivity.class);
            Bundle bdl = new Bundle();
            bdl.putInt("idConversation",convSelected.getId());
            toShowConv.putExtras(bdl);
            startActivity(toShowConv);
        }


    }

}
