package com.example.pushforce;

//import technique
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

//import pour l'affichage bibliothèque hors android
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EcouteActivity extends AppCompatActivity {
    //Déclaration
    //Constantes final
    private static final String TAG = "VOC";
    private static final int DUREE_MAXIMUM_ECOUTE = 5000; // en ms

    //Attributs
    private TextView textCompteur;
    private CountDownTimer compteur;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //Première étape quand l'écran est lancé, bien initialisé l'écran
        super .onCreate(savedInstanceState);
        EdgeToEdge.enable(this); //Pour prendre tout l'écran, this c'est le téléphone
        setContentView(R.layout.activity_ecoute); //lien avec mon truc activté.xml
        //Corriger, prendre des précautions contre le edge to edge on sait jamais
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()); // Récuperer les tailles de bars du sytème
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom); //ajouter une marge
            return insets;
        });

        // LE lien entre le Java et le XML qui compte
        textCompteur = findViewById(R.id.textCompteur);
        demarrerCompteur();
    }
    //Fonction du compteur
    private void demarrerCompteur() {
        compteur = new CountDownTimer(DUREE_MAXIMUM_ECOUTE, 1000) { //la fameux compteur initialisé
            @Override
            public void onTick(long msRestantes) {  //Compter en secondes sur le tick de l'horloge comme en micro
                // Conversion ms en secondes,
                long secondes = (msRestantes + 999) / 1000;
                textCompteur.setText(String.valueOf(secondes));
                Log.d(TAG, "tick : " + secondes);
            }

            @Override
            public void onFinish() { //à la fin du compteur
                textCompteur.setTextSize(32);   // forcé de mettre plus petite, ça déborde sinon
                textCompteur.setText("AHHH LE PETIOTT !");
                Log.d(TAG, "compteur fin");
            }
        };
        compteur.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // arreter le timer si l'écran est fermé avant la fin
        if (compteur != null) {
            compteur.cancel();
        }
    }
}