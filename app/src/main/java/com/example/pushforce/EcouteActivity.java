package com.example.pushforce;

//import technique
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import java.util.ArrayList;

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

    //Attributs (les noms sont parlant c'est comme dans le xml)
    private TextView textCompteur;
    private CountDownTimer compteur;
    private TextView textEcoute;
    private TextView textReconnaissance;
    private SpeechRecognizer reconnaissance;
    //Veleur de TEST ici ----------------------------------------------------
    private static final String ARTISTE_TEST = "Stromae";
    private static final String TITRE_TEST = "Alors on danse";
    //fin valeur de Test ------------------------------------------------------------------
    private VerificationReponse verification;

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

        verification = new VerificationReponse(ARTISTE_TEST, TITRE_TEST); // récupérer la réponse offcielle

        // Le lien entre le Java et le XML qui compte avec findviewbyid mm si on l'a déjà mis au dessus
        textCompteur = findViewById(R.id.textCompteur);
        textEcoute = findViewById(R.id.textEcoute);
        textReconnaissance = findViewById(R.id.textReconnaissance);
        textReconnaissance.setText("");   // on efface le "Validé" du xml

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

    //On démarre la reconnaissance avant le compteur on est fair
    private void demarrerEcoute() {
        demarrerReconnaissance();

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
            public void onFinish() {
                if (reconnaissance != null) {
                    reconnaissance.stopListening();   // ← ici : fin de l'enregistrement à 5 s
                }
                textCompteur.setTextSize(32);
                textEcoute.setText("ECHOUEYYY");
                Log.d(TAG, "compteur fin");
            }
        };
        compteur.start();
    }

    private void demarrerReconnaissance() {
        // Vérification préliminaires
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Log.d(TAG, "reconnaissance indisponible");
            textEcoute.setText("Reconnaissance vocale indisponible");
            return;
        }

        // On prends un micro
        reconnaissance = SpeechRecognizer.createSpeechRecognizer(this);
        reconnaissance.setRecognitionListener(new RecognitionListener() {

            @Override
            public void onReadyForSpeech(Bundle params) {
                Log.d(TAG, "micro prêt");
                textEcoute.setText("Parle !");
                demarrerCompteur();
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.d(TAG, "le joueur commence à parler");
                textEcoute.setText("...");
            }

            @Override
            public void onRmsChanged(float rmsdB) { }

            @Override
            public void onBufferReceived(byte[] buffer) { }

            @Override
            public void onEndOfSpeech() {
                Log.d(TAG, "fin du micro");
                textEcoute.setText("Analyse...");
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                ArrayList<String> textes =
                        partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (textes != null && !textes.isEmpty()) {
                    textReconnaissance.setText(textes.get(0));
                    Log.d(TAG, "partiel : " + textes.get(0));
                }
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> textes =
                        results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                Log.d(TAG, "résultats : " + textes);
                if (textes != null && !textes.isEmpty()) {
                    textReconnaissance.setText(textes.get(0));
                }
                if (compteur != null) {
                    compteur.cancel();
                }
                textEcoute.setText("analyse...");

                //ici on vérifie la réponse (envoi vers verificationReponse)
                int points = verification.calculerPoints(textes);
                Log.d(TAG, "points : " + points);

                if (points == 2) {
                    textEcoute.setText("EHHHH Artiste ET titre ! +2");
                } else if (points == 1) {
                    if (verification.artisteTrouve(textes)) {
                        textEcoute.setText("Artiste trouvé ! +1");
                    } else {
                        textEcoute.setText("Titre trouvé ! +1");
                    }
                } else {
                    textEcoute.setText("AHHH LE PETIOTT !");
                }
            }

            @Override
            public void onError(int erreur) {
                Log.d(TAG, "erreur reconnaissance : " + erreur);
                if (compteur != null) {
                    compteur.cancel();
                }
                if (erreur == SpeechRecognizer.ERROR_NO_MATCH
                        || erreur == SpeechRecognizer.ERROR_SPEECH_TIMEOUT) {
                    textEcoute.setText("Je n'ai rien compris...");
                } else {
                    textEcoute.setText("Erreur micro (" + erreur + ")");
                }
            }

            @Override
            public void onEvent(int eventType, Bundle params) { }
        });

        // 4. Les réglages
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr-FR");
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);

        // 5. C'est parti
        reconnaissance.startListening(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // arreter le timer si l'écran est fermé avant la fin
        if (compteur != null) {
            compteur.cancel();
        }
        if (reconnaissance != null) {
            reconnaissance.destroy();
        }
    }
}