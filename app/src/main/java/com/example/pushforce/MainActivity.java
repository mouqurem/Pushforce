package com.example.pushforce;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.net.Uri;
import java.util.List;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private String Nombre_joueurs;
    private String Nom_J1;
    private String Nom_J2;
    private String Nom_J3;
    private String Nom_J4;
    private List<Uri> playlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button Bouton_nb_joueur = findViewById(R.id.Bouton_nb_joueur);
        Button bouton_next = findViewById(R.id.Bouton_next);
        EditText Input_nb_joueur = findViewById(R.id.Input_nb_joueur);

        LinearLayout Page_accueil = findViewById(R.id.Page_accueil);

        LinearLayout Layer_1 = findViewById(R.id.Layer_1_Joueur);
        LinearLayout Layer_2 = findViewById(R.id.Layer_2_Joueurs);
        LinearLayout Layer_3 = findViewById(R.id.Layer_3_Joueurs);
        LinearLayout Layer_4 = findViewById(R.id.Layer_4_Joueurs);

        EditText Input_Nom_J1 = findViewById(R.id.Input_Nom_J1);
        EditText Input_Nom_J2 = findViewById(R.id.Input_Nom_J2);
        EditText Input_Nom_J3 = findViewById(R.id.Input_Nom_J3);
        EditText Input_Nom_J4 = findViewById(R.id.Input_Nom_J4);

        Button Bouton_Layer_1 = findViewById(R.id.Bouton_Layer_1);
        Button Bouton_Layer_2 = findViewById(R.id.Bouton_Layer_2);
        Button Bouton_Layer_3 = findViewById(R.id.Bouton_Layer_3);
        Button Bouton_Layer_4 = findViewById(R.id.Bouton_Layer_4);

        ActivityResultLauncher<String[]> Selecteur_musique = registerForActivityResult(
                new ActivityResultContracts.OpenMultipleDocuments(),
                uris -> {
                    if (!uris.isEmpty()) {
                        playlist = uris;
                        String nb_musiques_choisies = "Il y a " + uris.size() + " musiques choisies";
                        Bouton_Layer_1.setText(nb_musiques_choisies);
                        Bouton_Layer_2.setText(nb_musiques_choisies);
                        Bouton_Layer_3.setText(nb_musiques_choisies);
                        Bouton_Layer_4.setText(nb_musiques_choisies);
                    }
                });

        Bouton_nb_joueur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Nombre_joueurs = Input_nb_joueur.getText().toString().trim();

                Input_Nom_J1.setVisibility(View.GONE);
                Input_Nom_J2.setVisibility(View.GONE);
                Input_Nom_J3.setVisibility(View.GONE);
                Input_Nom_J4.setVisibility(View.GONE);

                if (Nombre_joueurs.equals("1")) {
                    Input_Nom_J1.setVisibility(View.VISIBLE);
                } else if (Nombre_joueurs.equals("2")) {
                    Input_Nom_J1.setVisibility(View.VISIBLE);
                    Input_Nom_J2.setVisibility(View.VISIBLE);
                } else if (Nombre_joueurs.equals("3")) {
                    Input_Nom_J1.setVisibility(View.VISIBLE);
                    Input_Nom_J2.setVisibility(View.VISIBLE);
                    Input_Nom_J3.setVisibility(View.VISIBLE);
                } else if (Nombre_joueurs.equals("4")) {
                    Input_Nom_J1.setVisibility(View.VISIBLE);
                    Input_Nom_J2.setVisibility(View.VISIBLE);
                    Input_Nom_J3.setVisibility(View.VISIBLE);
                    Input_Nom_J4.setVisibility(View.VISIBLE);
                }
            }
        });

        bouton_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nom_J1 = Input_Nom_J1.getText().toString().trim();
                Nom_J2 = Input_Nom_J2.getText().toString().trim();
                Nom_J3 = Input_Nom_J3.getText().toString().trim();
                Nom_J4 = Input_Nom_J4.getText().toString().trim();

                Page_accueil.setVisibility(View.GONE);

                if (Nombre_joueurs.equals("1")) {
                    Layer_1.setVisibility(View.VISIBLE);
                } else if (Nombre_joueurs.equals("2")) {
                    Layer_2.setVisibility(View.VISIBLE);
                } else if (Nombre_joueurs.equals("3")) {
                    Layer_3.setVisibility(View.VISIBLE);
                } else if (Nombre_joueurs.equals("4")) {
                    Layer_4.setVisibility(View.VISIBLE);
                }
            }
        });

        Bouton_Layer_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Selecteur_musique.launch(new String[]{"audio/*"});
            }
        });

        Bouton_Layer_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Selecteur_musique.launch(new String[]{"audio/*"});
            }
        });

        Bouton_Layer_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Selecteur_musique.launch(new String[]{"audio/*"});
            }
        });

        Bouton_Layer_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Selecteur_musique.launch(new String[]{"audio/*"});
            }
        });
    }
}