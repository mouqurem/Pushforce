package com.example.pushforce;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AfficherReponseActivity extends AppCompatActivity
{
    private String nomJ1, nomJ2, nomJ3, nomJ4, nombreJoueurs, titre, artiste;
    private TextView view_titre, view_artiste;
    private CountDownTimer timer;
    private ArrayList<Uri> playlist;

    void setup()
    {
        //Récupérer le nombre de joueurs par intent
        nombreJoueurs = getIntent().getStringExtra("nb_joueurs");

        //Récupérer les noms des joueurs par intent
        nomJ1 = getIntent().getStringExtra("nom_J1");
        nomJ2 = getIntent().getStringExtra("nom_J2");
        nomJ3 = getIntent().getStringExtra("nom_J3");
        nomJ4 = getIntent().getStringExtra("nom_J4");
        titre = getIntent().getStringExtra("titre");
        artiste = getIntent().getStringExtra("artiste");
        playlist = getIntent().getParcelableArrayListExtra("playlist");

        //Récupérer les textview du XML
        view_titre = findViewById(R.id.titre);
        view_artiste = findViewById(R.id.artiste);
    }
    void afficher_reponse()
    {
        view_titre.setText(titre);
        view_artiste.setText(artiste);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_afficher_reponse);

        setup();
        afficher_reponse();

        timer = new CountDownTimer(4000, 500) // durée totale, intervalle de mise à jour
        {
            @Override
            public void onTick(long msRestantes)
            {
                //On ne fait rien juste on attend
            }

            @Override
            public void onFinish() //Quand le timer est écoulé, on repart sur un début de round
            {
                Intent intent = new Intent(AfficherReponseActivity.this, DebutRoundActivity.class);
                intent.putExtra("nom_J1", nomJ1);
                intent.putExtra("nom_J2", nomJ2);
                intent.putExtra("nom_J3", nomJ3);
                intent.putExtra("nom_J4", nomJ4);
                intent.putParcelableArrayListExtra("playlist", playlist);
                intent.putExtra("nb_joueurs", nombreJoueurs);

                startActivity(intent);
                finish();
            }
        };

        timer.start();
    }
}
