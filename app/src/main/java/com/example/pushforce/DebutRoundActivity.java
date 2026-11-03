package com.example.pushforce;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class DebutRoundActivity extends AppCompatActivity
{

    private int scoreJ1 = 0;
    private int scoreJ2 = 0;
    private int scoreJ3 = 0;
    private int scoreJ4 = 0;

    private String NomJ1, NomJ2, NomJ3, NomJ4;

    private TextView view_scoreJ1, view_scoreJ2, view_scoreJ3, view_scoreJ4;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_debut_round);

        String nombreJoueurs = getIntent().getStringExtra("nombre_joueurs"); //On récupère le nombre de joueurs

        //Récupérer les noms des joueurs
        NomJ1 = getIntent().getStringExtra("nom_J1");
        NomJ2 = getIntent().getStringExtra("nom_J2");
        NomJ3 = getIntent().getStringExtra("nom_J3");
        NomJ4 = getIntent().getStringExtra("nom_J4");


        // Récupérer les TextView du layout
        view_scoreJ1 = findViewById(R.id.score_J1);
        view_scoreJ2 = findViewById(R.id.score_J2);
        view_scoreJ3 = findViewById(R.id.score_J3);
        view_scoreJ4 = findViewById(R.id.score_J4);

        //Mettre à jour visibilité en fonction du nombre de joueurs
        if (nombreJoueurs.equals("1"))
        {
            view_scoreJ1.setVisibility(View.VISIBLE);
            view_scoreJ2.setVisibility(View.GONE);
            view_scoreJ3.setVisibility(View.GONE);
            view_scoreJ4.setVisibility(View.GONE);
        }
        else if (nombreJoueurs.equals("2"))
        {
            view_scoreJ1.setVisibility(View.VISIBLE);
            view_scoreJ2.setVisibility(View.VISIBLE);
            view_scoreJ3.setVisibility(View.GONE);
            view_scoreJ4.setVisibility(View.GONE);
        }
        else if (nombreJoueurs.equals("3"))
        {
            view_scoreJ1.setVisibility(View.VISIBLE);
            view_scoreJ2.setVisibility(View.VISIBLE);
            view_scoreJ3.setVisibility(View.VISIBLE);
            view_scoreJ4.setVisibility(View.GONE);
        }
        else if (nombreJoueurs.equals("4"))
        {
            view_scoreJ1.setVisibility(View.VISIBLE);
            view_scoreJ2.setVisibility(View.VISIBLE);
            view_scoreJ3.setVisibility(View.VISIBLE);
            view_scoreJ4.setVisibility(View.VISIBLE);
        }

        // Mettre à jour le score
        view_scoreJ1.setText(NomJ1 + " = " + scoreJ1);
        view_scoreJ2.setText(NomJ2 + " = " + scoreJ2);
        view_scoreJ3.setText(NomJ3 + " = " + scoreJ3);
        view_scoreJ4.setText(NomJ4 + " = " + scoreJ4);
    }
}
