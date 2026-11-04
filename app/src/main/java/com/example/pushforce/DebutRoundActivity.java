package com.example.pushforce;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class DebutRoundActivity extends AppCompatActivity
{

    private int scoreJ1, scoreJ2, scoreJ3, scoreJ4;

    private String nomJ1, nomJ2, nomJ3, nomJ4, nombreJoueurs;

    private Button transiJ1, transiJ2, transiJ3, transiJ4;

    private TextView view_scoreJ1, view_scoreJ2, view_scoreJ3, view_scoreJ4;

    private ConstraintLayout layout_scoreboard, layout_quatre_boutons;

    public void maj_scoreboard(String nb_joueurs)
    {
        layout_scoreboard.setVisibility(View.VISIBLE);
        layout_quatre_boutons.setVisibility(View.GONE);

        view_scoreJ1.setText(nomJ1 + " = " + scoreJ1);
        view_scoreJ2.setText(nomJ2 + " = " + scoreJ2);
        view_scoreJ3.setText(nomJ3 + " = " + scoreJ3);
        view_scoreJ4.setText(nomJ4 + " = " + scoreJ4);

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

        //à remplacer par intent
        scoreJ1 = 0;
        scoreJ2 = 0;
        scoreJ3 = 0;
        scoreJ4 = 0;

        //à remplacer par intent
        nombreJoueurs = "4";

        //à remplacer par intent
        nomJ1 = "Rémi";
        nomJ2 = "Doriann";
        nomJ3 = "Houcine";
        nomJ4 = "Rafaël";


        // Récupérer les Layout du xml
        layout_scoreboard = findViewById(R.id.scoreboard);
        layout_quatre_boutons = findViewById(R.id.quatre_boutons);

        // Récupérer les TextView du scoreboard du xml
        view_scoreJ1 = findViewById(R.id.score_J1);
        view_scoreJ2 = findViewById(R.id.score_J2);
        view_scoreJ3 = findViewById(R.id.score_J3);
        view_scoreJ4 = findViewById(R.id.score_J4);

        //récupérer les boutons stop des joueurs du xml
        transiJ1 = findViewById(R.id.stopJ1);
        transiJ2 = findViewById(R.id.stopJ1);
        transiJ3 = findViewById(R.id.stopJ1);
        transiJ4 = findViewById(R.id.stopJ1);

        //mettre les noms des joueurs sur les boutons stop
        transiJ1.setText(nomJ1);
        transiJ1.setText(nomJ2);
        transiJ1.setText(nomJ3);
        transiJ1.setText(nomJ4);


        maj_scoreboard(nombreJoueurs);


    }
}