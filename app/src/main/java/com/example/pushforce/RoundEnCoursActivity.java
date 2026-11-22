package com.example.pushforce;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.CountDownTimer;
import android.media.MediaPlayer;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.ArrayList;

public class RoundEnCoursActivity extends AppCompatActivity
{
    private Button transiJ1, transiJ2, transiJ3, transiJ4;
    private CountDownTimer timer;
    private TextView chrono;
    private String nomJ1, nomJ2, nomJ3, nomJ4, nombreJoueurs, titre, artiste;
    private Uri lien_son;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private ArrayList<Uri> playlist;

    private long tempsRestant = 30000; // temps restant du round, pour reprendre après une mauvaise réponse
    private boolean buzzEnCours = false; // empêche deux joueurs de buzzer en même temps (bug sinon)

    private final ActivityResultLauncher<Intent> lanceurEcoute =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), resultat -> {
                int points = 0;
                int joueur = 0;
                if (resultat.getResultCode() == RESULT_OK && resultat.getData() != null) {
                    points = resultat.getData().getIntExtra(EcouteActivity.CLE_POINTS, 0);
                    joueur = resultat.getData().getIntExtra(EcouteActivity.CLE_JOUEUR, 0);
                }
                buzzEnCours = false;

                if (points > 0) {
                    Scores.ajouter(joueur, points);   // bonne réponse : on note les points
                    allerAfficherReponse();           // et on affiche la réponse
                } else {
                    mediaPlayer.start();              // mauvaise réponse : la musique reprend
                    lancerTimer(tempsRestant);        // et le chrono repart là où il était
                }
            });


    public void setup()
    {
        nomJ1 = getIntent().getStringExtra("nom_J1");
        nomJ2 = getIntent().getStringExtra("nom_J2");
        nomJ3 = getIntent().getStringExtra("nom_J3");
        nomJ4 = getIntent().getStringExtra("nom_J4");
        nombreJoueurs = getIntent().getStringExtra("nb_joueurs");
        playlist = getIntent().getParcelableArrayListExtra("playlist");

        lien_son = getIntent().getParcelableExtra("lien_son");

        transiJ1 = findViewById(R.id.transiJ1);
        transiJ2 = findViewById(R.id.transiJ2);
        transiJ3 = findViewById(R.id.transiJ3);
        transiJ4 = findViewById(R.id.transiJ4);

        transiJ1.setText(nomJ1);
        transiJ2.setText(nomJ2);
        transiJ3.setText(nomJ3);
        transiJ4.setText(nomJ4);

        chrono = findViewById(R.id.chrono);

        transiJ1.setOnClickListener(v -> buzz(1));
        transiJ2.setOnClickListener(v -> buzz(2));
        transiJ3.setOnClickListener(v -> buzz(3));
        transiJ4.setOnClickListener(v -> buzz(4));
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

public void setup_media()
{
//https://developer.android.com/media/platform/mediaplayer/basics?hl=fr
// Attention : Vous devez intercepter ou transmettre IllegalArgumentException et IOException lorsque vous utilisez setDataSource(),
// car le fichier auquel vous faites référence peut ne pas exister.

// On met donc des try catch

    mediaPlayer.setAudioAttributes(
            new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
    );
    try {
        mediaPlayer.setDataSource(getApplicationContext(), lien_son);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
    try {
        mediaPlayer.prepare();
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}

    private void lire_metadonnees(Uri uri)
    {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(getApplicationContext(), uri);

        //Assigner le titre et l'artiste en fonction du lien URI
        titre = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        artiste = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_round_en_cours);

        setup();

        maj_nb_joueurs(nombreJoueurs);

        lire_metadonnees(lien_son);

        setup_media();
        mediaPlayer.start();

        lancerTimer(30000);

    }
    private void lancerTimer(long duree)
    {
        timer = new CountDownTimer(duree, 100) // durée totale, intervalle de mise à jour
        {
            @Override
            public void onTick(long msRestantes)
            {
                tempsRestant = msRestantes; // memoriser les secondes qui reste
                chrono.setText(String.valueOf(msRestantes/1000));
            }

            @Override
            public void onFinish() //Chrono écoulé
            {
                allerAfficherReponse();
            }
        }.start();
    }
    //Un joueur appuie sur  un bouton
    private void buzz(int joueur)
    {
        if (buzzEnCours) {
            return;                       // quelqu'un a déjà buzzé
        }
        buzzEnCours = true;

        timer.cancel();                   // arrête le chrono
        mediaPlayer.pause();              // met la musique en pause (au cas ou le micro l'entend)

        Intent intent = new Intent(this, EcouteActivity.class);
        intent.putExtra(EcouteActivity.CLE_ARTISTE, artiste);
        intent.putExtra(EcouteActivity.CLE_TITRE, titre);
        intent.putExtra(EcouteActivity.CLE_JOUEUR, joueur);
        lanceurEcoute.launch(intent);
    }

    //Mettre les intent dans un fonction parce qu'on rétuilise 2 fois ce code sinon c'etait plus simple
    private void allerAfficherReponse()
    {
        mediaPlayer.stop(); //Arrêter la musique

        Intent intent = new Intent(RoundEnCoursActivity.this, AfficherReponseActivity.class);
        intent.putExtra("titre", titre);
        intent.putExtra("artiste", artiste);
        intent.putExtra("nom_J1", nomJ1);
        intent.putExtra("nom_J2", nomJ2);
        intent.putExtra("nom_J3", nomJ3);
        intent.putExtra("nom_J4", nomJ4);
        intent.putExtra("nb_joueurs", nombreJoueurs);
        intent.putParcelableArrayListExtra("playlist", playlist);

        startActivity(intent);
        finish(); // permet de pas empiler les round dans la mémoires
    }

    // Arreter la musique sah enfin ça s'arretais pas c'était infernal
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        mediaPlayer.release();
    }
}
