package com.example.pushforce;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.CountDownTimer;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RoundEnCoursActivity extends AppCompatActivity
{
    private Button transiJ1, transiJ2, transiJ3, transiJ4;
    private CountDownTimer timer;
    private TextView chrono;
    private String nomJ1, nomJ2, nomJ3, nomJ4, nombreJoueurs;


    public void setup()
    {
        Intent intent = getIntent();
        nomJ1 = intent.getStringExtra("nom_j1");
        nomJ2 = intent.getStringExtra("nom_j2");
        nomJ3 = intent.getStringExtra("nom_j3");
        nomJ4 = intent.getStringExtra("nom_j4");
        nombreJoueurs = intent.getStringExtra("nb_joueurs");

        transiJ1 = findViewById(R.id.transiJ1);
        transiJ2 = findViewById(R.id.transiJ2);
        transiJ3 = findViewById(R.id.transiJ3);
        transiJ4 = findViewById(R.id.transiJ4);

        transiJ1.setText(nomJ1);
        transiJ2.setText(nomJ2);
        transiJ3.setText(nomJ3);
        transiJ4.setText(nomJ4);

        chrono = findViewById(R.id.chrono);
    }
    public void maj_nb_joueurs(String nb_joueurs)
    {

        //Mettre à jour visibilité en fonction du nombre de joueurs
        if (nb_joueurs.equals("1"))
        {
            transiJ1.setVisibility(View.VISIBLE);
            transiJ2.setVisibility(View.GONE);
            transiJ3.setVisibility(View.GONE);
            transiJ4.setVisibility(View.GONE);
        }
        else if (nb_joueurs.equals("2"))
        {
            transiJ1.setVisibility(View.VISIBLE);
            transiJ2.setVisibility(View.VISIBLE);
            transiJ3.setVisibility(View.GONE);
            transiJ4.setVisibility(View.GONE);
        }
        else if (nb_joueurs.equals("3"))
        {
            transiJ1.setVisibility(View.VISIBLE);
            transiJ2.setVisibility(View.VISIBLE);
            transiJ3.setVisibility(View.VISIBLE);
            transiJ4.setVisibility(View.GONE);
        }
        else if (nb_joueurs.equals("4"))
        {
            transiJ1.setVisibility(View.VISIBLE);
            transiJ2.setVisibility(View.VISIBLE);
            transiJ3.setVisibility(View.VISIBLE);
            transiJ4.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_round_en_cours);

        setup();
        maj_nb_joueurs(nombreJoueurs);


        timer = new CountDownTimer(30000, 100) // durée totale, intervalle de mise à jour
        {
            @Override
            public void onTick(long msRestantes)
            {

                chrono.setText(String.valueOf(msRestantes/1000));
            }

            @Override
            public void onFinish()
            {
                chrono.setText("0");

            }
        }.start();

    }
}
