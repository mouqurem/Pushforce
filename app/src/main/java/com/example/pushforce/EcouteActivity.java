package com.example.pushforce;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.TextView;
import android.media.SoundPool;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class EcouteActivity extends AppCompatActivity {

    // ===== Constantes =====
    private static final String TAG = "LOG";
    private static final int DUREE_MAXIMUM_ECOUTE = 5000;   // en ms
    private static final int DELAI_AVANT_RETOUR = 6000;     // temps pour lire le résultat, en ms

    //lien avec moukehhh
    public static final String CLE_POINTS  = "points";
    public static final String CLE_ARTISTE = "artiste";
    public static final String CLE_TITRE   = "titre";
    public static final String CLE_LANGUE  = "langue";
    public static final String CLE_JOUEUR  = "joueur";

    // Valeurs de TEST ---------------------------------------------
    private static final String ARTISTE_TEST = "Stromae";
    private static final String TITRE_TEST = "Alors on danse";
    // fin valeurs de TEST -----------------------------------------

    // ===== Attributs =====
    private String artiste;   //la réponse
    private String titre; //la réponse
    private String langue; //la langue de la musique pour aider google
    private int numeroJoueur; //le joueur
    private TextView textCompteur;
    private TextView textEcoute;
    private TextView textReconnaissance;
    private CountDownTimer compteur;
    private SpeechRecognizer reconnaissance;
    private Intent intentReconnaissance;
    private VerificationReponse verification;
    private boolean tempsEcoule = false;   // si les 5sec sont finies
    private boolean termine = false;       // si c'est déjà fini
    private SoundPool sons;
    private int sonDefaiteSurMauvaiseRep;
    private int sonDefaiteSurCompteur;
    // Popup de permission micro, et ce qu'on fait de la réponse
    private final ActivityResultLauncher<String> demandePermissionMicro =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), accorde -> {
                if (accorde) {
                    Log.d(TAG, "permission micro accordée");
                    demarrerReconnaissance();
                } else {
                    Log.d(TAG, "permission micro refusée");
                    textEcoute.setText("ECHOUEYYY MICRO REFUSEYYYY");
                    terminer(0);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ecoute);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Préparer les effets sonores (chargés à l'avance pour être joués sans délai)
        sons = new SoundPool.Builder().setMaxStreams(3).build();
        sonDefaiteSurMauvaiseRep = sons.load(this, R.raw.ahhpetio, 1);
        sonDefaiteSurCompteur  = sons.load(this, R.raw.echoueyy, 1);

        // Récupérer la chanson envoyée par RoundEnCoursActivity (mouqette)
        Intent recu = getIntent();
        if (recu.hasExtra(CLE_ARTISTE)) {
            artiste = recu.getStringExtra(CLE_ARTISTE);   // peut être null si le mp3 n a pas de métadonnées
            titre   = recu.getStringExtra(CLE_TITRE);
        } else {
            artiste = ARTISTE_TEST;                       // écran lancé seul : mode test
            titre   = TITRE_TEST;
        }
        langue = recu.getStringExtra(CLE_LANGUE);
        if (langue == null) {
            langue = "fr-FR";
        }
        numeroJoueur = recu.getIntExtra(CLE_JOUEUR, 1);
        Log.d(TAG, "chanson reçue : " + artiste + " - " + titre + ", joueur " + numeroJoueur);

        verification = new VerificationReponse(artiste, titre);

        textCompteur = findViewById(R.id.textCompteur);
        textEcoute = findViewById(R.id.textEcoute);
        textReconnaissance = findViewById(R.id.textReconnaissance);
        textReconnaissance.setText("");

        verifierPermissionMicro();
    }

    private void verifierPermissionMicro() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            demarrerReconnaissance();
        } else {
            demandePermissionMicro.launch(Manifest.permission.RECORD_AUDIO);
        }
    }
    // ===== Jouer des sons =====
    private void jouerSon(int son) {
        sons.play(son, 1f, 1f, 1, 0, 1f);
    }
    // ===== Le compteur =====
    private void demarrerCompteur() {
        compteur = new CountDownTimer(DUREE_MAXIMUM_ECOUTE, 1000) {
            @Override
            public void onTick(long msRestantes) {
                long secondes = (msRestantes + 999) / 1000;
                textCompteur.setText(String.valueOf(secondes));
                Log.d(TAG, "tick : " + secondes);
            }

            @Override
            public void onFinish() {
                tempsEcoule = true;
                textCompteur.setText("0");              // le CountDownTimer n'affiche jamais 0 tout seul on est obligéééé
                Log.d(TAG, "compteur fin : aucune réponse");
                if (reconnaissance != null) {
                    reconnaissance.cancel();            // on coupe le micro
                }
                textEcoute.setText("ECHOUEYYYYYY");
                jouerSon(sonDefaiteSurCompteur);
                terminer(0);                            // point perdu
            }
        };
        compteur.start();
    }

    // ===== La reconnaissance vocale =====
    private void demarrerReconnaissance() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Log.d(TAG, "reconnaissance indisponible");
            textEcoute.setText("Reconnaissance vocale indisponible");
            terminer(0);
            return;
        }

        reconnaissance = SpeechRecognizer.createSpeechRecognizer(this);   // UN SEUL micro créé une fois
        reconnaissance.setRecognitionListener(new RecognitionListener() {

            @Override
            public void onReadyForSpeech(Bundle params) {
                Log.d(TAG, "micro prêt");
                if (compteur == null) {          // seulement à la première écoute
                    textEcoute.setText("Parle !");
                    demarrerCompteur();
                }
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
                Log.d(TAG, "fin de la parole");
                textEcoute.setText("Analyse...");
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                ArrayList<String> textes =
                        partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (textes != null && !textes.isEmpty()) {
                    textReconnaissance.setText(textes.get(0));
                }
            }

            @Override
            public void onResults(Bundle results) {
                if (tempsEcoule) {
                    return;                              // echoueyyyy : le temps est déjà fini
                }
                if (compteur != null) {
                    compteur.cancel();                   // réponse reçue : on arrête le compteur
                }

                ArrayList<String> textes =
                        results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                Log.d(TAG, "résultats : " + textes);
                if (textes != null && !textes.isEmpty()) {
                    textReconnaissance.setText(textes.get(0));
                }

                int points = verification.calculerPoints(textes);
                if (points == 2) {
                    textEcoute.setText("EHHHH Artiste ET titre ! +2");
                } else if (points == 1) {
                    if (verification.artisteTrouve(textes)) {
                        textEcoute.setText("Artiste trouvé ! +1");
                    } else {
                        textEcoute.setText("Titre trouvé ! +1");
                    }
                } else {
                    textEcoute.setText("FAUX !\nAHHH LE PETIOTT\nAHHH C'EST ZEROO 0 !");
                    jouerSon(sonDefaiteSurMauvaiseRep);
                }
                terminer(points);
            }

            @Override
            public void onError(int erreur) {
                Log.d(TAG, "erreur reconnaissance : " + erreur);
                if (tempsEcoule) {
                    return;                              // le compteur s'est déjà occupé de la fin
                }
                if (erreur == SpeechRecognizer.ERROR_NO_MATCH
                        || erreur == SpeechRecognizer.ERROR_SPEECH_TIMEOUT) {
                    textEcoute.setText("Alors là j'ai rien capté... répète !");
                    reconnaissance.cancel();                          // on remet le MÊME micro à zéro
                    reconnaissance.startListening(intentReconnaissance); // et on relance
                } else {
                    textEcoute.setText("Erreur micro (" + erreur + ")");
                }
            }

            @Override
            public void onEvent(int eventType, Bundle params) { }
        });

        // Les réglages
        intentReconnaissance = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentReconnaissance.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intentReconnaissance.putExtra(RecognizerIntent.EXTRA_LANGUAGE, langue);
        intentReconnaissance.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        intentReconnaissance.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ArrayList<String> motsAttendus = new ArrayList<>();
            motsAttendus.add(artiste);
            motsAttendus.add(titre);
            intentReconnaissance.putExtra(RecognizerIntent.EXTRA_BIASING_STRINGS, motsAttendus);
        }

        reconnaissance.startListening(intentReconnaissance);
    }

    // ===== Fin : renvoyer les points et revenir à l'écran de Moukehh =====
    private void terminer(int points) {
        if (termine) {
            return;                       // barriere de protecition
        }
        termine = true;
        Log.d(TAG, "fin de l'écoute, points : " + points);

        Intent resultat = new Intent();
        resultat.putExtra(CLE_POINTS, points);
        resultat.putExtra(CLE_JOUEUR, numeroJoueur);
        setResult(RESULT_OK, resultat);   // les points seront récupérés par l'écran de Mouquette

        // On laisse 2 secondes pour lire le message, puis on ferme l'écran
        new Handler(Looper.getMainLooper()).postDelayed(() -> finish(), DELAI_AVANT_RETOUR);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compteur != null) {
            compteur.cancel();
        }
        if (reconnaissance != null) {
            reconnaissance.destroy();
        }
        if (sons != null) {
            sons.release();
        }
    }
}