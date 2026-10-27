package com.example.pushforce;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
//ffff
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 1. Liaison avec l'interface XML
        Button Bouton_nb_joueur = findViewById(R.id.Bouton_nb_joueur);
        // On verifier que un champ de texte est créé (EditText) dans XML pour taper le nombre
        EditText Input_nb_joueur = findViewById(R.id.Input_nb_joueur);

        // 2. Remplacement de "if" par le Listener
        Bouton_nb_joueur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // L'équivalent de ton : Nombre_joueurs = input("Nombre_de_joueurs");
                String Nombre_joueurs = Input_nb_joueur.getText().toString().trim();

                if (Nombre_joueurs.equals("1")) {
                    // layout player solo
                    // Plus tard, Player1_Name = EditTextPlayer1.getText().toString();

                } else if (Nombre_joueurs.equals("2")) {
                    // layout player double
                    // Plus tard, Player1_Name = ... et Player2_Name = ...

                } else if (Nombre_joueurs.equals("3")) {
                    // layout player triple

                } else if (Nombre_joueurs.equals("4")) { // Correction : ton pseudo-code avait un deuxième '2'
                    // layout player quatre

                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}