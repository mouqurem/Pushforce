package com.example.pushforce;

//import technique
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import android.Manifest;
import android.content.pm.PackageManager;

//import pour l'affichage bibliothèque hors android
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

public class EcouteActivity extends AppCompatActivity {
    //Déclaration
    //Constantes final
    private static final String TAG = "LOG";
    private static final int DUREE_MAXIMUM_ECOUTE = 5000; // en ms

    //Attributs
    private TextView textCompteur;
    private CountDownTimer compteur;
    private TextView textEcoute;

    // Demander une permission au téléphone et à l'utilisateur, et ce qu'on fait avec la réponse
    private final ActivityResultLauncher<String> demandePermissionMicro =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), accorde ->{ // demandé à l'utilisateur le micro
                if (accorde) {
                    Log.d(TAG, "permission micro accordee"); // message d'info'
                    demarrerEcoute();
                }
                else {
                    Log.d(TAG, "permission micro echoueyy");
                    textEcoute.setText("ECHOUEYYY MICRO REFUSEYYYY");
                }
            });

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
        textEcoute = findViewById(R.id.textEcoute);

        //lancer le programme
        verifierPermissionMicro();
    }

    //Fonction dont le nom est parlant
    private void verifierPermissionMicro() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {   //ligne bien complexe pour dire est ce que "si la permission est accordée"
            //cas accordée
            demarrerEcoute();
            } else {
            //cas pas accordée
            demandePermissionMicro.launch(Manifest.permission.RECORD_AUDIO);
        }
    }

    //On démarre le compteur après l'écoute on est fair play
    private void demarrerEcoute() {
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