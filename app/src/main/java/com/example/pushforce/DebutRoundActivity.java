package com.example.pushforce;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class DebutRoundActivity extends AppCompatActivity
{
    private int scoreJ1, scoreJ2, scoreJ3, scoreJ4;

    private String nomJ1, nomJ2, nomJ3, nomJ4, nombreJoueurs;

    private Button lancerRound;

    private TextView view_scoreJ1, view_scoreJ2, view_scoreJ3, view_scoreJ4;

    public void setup()
    {
        //à remplacer par intent
        scoreJ1 = 0;
        scoreJ2 = 0;
        scoreJ3 = 0;
        scoreJ4 = 0;

        //Récupérer le nombre de joueurs
        nombreJoueurs = getIntent().getStringExtra("nombre_joueurs");

        //Récupérer les noms des joueurs
        nomJ1 = getIntent().getStringExtra("nom_J1");
        nomJ2 = getIntent().getStringExtra("nom_J2");
        nomJ3 = getIntent().getStringExtra("nom_J3");
        nomJ4 = getIntent().getStringExtra("nom_J4");

        // Récupérer les TextView du scoreboard du xml
        view_scoreJ1 = findViewById(R.id.score_J1);
        view_scoreJ2 = findViewById(R.id.score_J2);
        view_scoreJ3 = findViewById(R.id.score_J3);
        view_scoreJ4 = findViewById(R.id.score_J4);

        //Récupérer le bouton lancerRound du XML
        lancerRound = findViewById(R.id.bouton_lancer_round);
    }
    public void maj_scoreboard()
    {
        view_scoreJ1.setText(nomJ1 + " = " + scoreJ1);
        view_scoreJ2.setText(nomJ2 + " = " + scoreJ2);
        view_scoreJ3.setText(nomJ3 + " = " + scoreJ3);
        view_scoreJ4.setText(nomJ4 + " = " + scoreJ4);
    }

    public void maj_nb_joueurs(String nb_joueurs)
    {


        //Mettre à jour visibilité en fonction du nombre de joueurs
        if (nb_joueurs.equals("1"))
        {
            view_scoreJ1.setVisibility(View.VISIBLE);
            view_scoreJ2.setVisibility(View.GONE);
            view_scoreJ3.setVisibility(View.GONE);
            view_scoreJ4.setVisibility(View.GONE);
        }
        else if (nb_joueurs.equals("2"))
        {
            view_scoreJ1.setVisibility(View.VISIBLE);
            view_scoreJ2.setVisibility(View.VISIBLE);
            view_scoreJ3.setVisibility(View.GONE);
            view_scoreJ4.setVisibility(View.GONE);
        }
        else if (nb_joueurs.equals("3"))
        {
            view_scoreJ1.setVisibility(View.VISIBLE);
            view_scoreJ2.setVisibility(View.VISIBLE);
            view_scoreJ3.setVisibility(View.VISIBLE);
            view_scoreJ4.setVisibility(View.GONE);
        }
        else if (nb_joueurs.equals("4"))
        {
            view_scoreJ1.setVisibility(View.VISIBLE);
            view_scoreJ2.setVisibility(View.VISIBLE);
            view_scoreJ3.setVisibility(View.VISIBLE);
            view_scoreJ4.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_debut_round);

        setup();

        maj_scoreboard();

        maj_nb_joueurs(nombreJoueurs);

        lancerRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //INTENT ROUND EN COURS
                //nb joueurs
                //noms des joueurs
                Intent intent = new Intent(DebutRoundActivity.this, RoundEnCoursActivity.class);
                intent.putExtra("nom_j1", nomJ1);
                intent.putExtra("nom_j2", nomJ2);
                intent.putExtra("nom_j3", nomJ3);
                intent.putExtra("nom_j4", nomJ4);
                intent.putExtra("nb_joueurs", nombreJoueurs);
                startActivity(intent);
            }
        });

    }
}
