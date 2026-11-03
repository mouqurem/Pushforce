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
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_debut_round);

        String nombreJoueurs = getIntent().getStringExtra("nombre_joueurs"); //On récupère le nombre de joueurs


        // Récupérer les TextView du layout
        TextView view_scoreJ1 = findViewById(R.id.score_J1);
        int scoreJ1 = 0;
        view_scoreJ1.setText("J1 : " + scoreJ1);

        TextView view_scoreJ2 = findViewById(R.id.score_J2);
        int scoreJ2 = 0;
        view_scoreJ2.setText("J2 : " + scoreJ2);

        TextView view_scoreJ3 = findViewById(R.id.score_J3);
        int scoreJ3 = 0;
        view_scoreJ3.setText("J3 : " + scoreJ3);

        TextView view_scoreJ4 = findViewById(R.id.score_J4);
        int scoreJ4 = 0;
        view_scoreJ4.setText("J4 : " + scoreJ4);

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
    }
}
