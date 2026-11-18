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


    public void setup()
    {
        nomJ1 = "Remi";
        nomJ2 = "Doriann";
        nomJ3 = "Rafael";
        nomJ4 = "Houcine";
        nombreJoueurs = "4";

        //lien_son =
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

        titre = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        artiste = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

        transiJ1.setText(titre);
        transiJ2.setText(artiste);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_round_en_cours);



        setup();
        maj_nb_joueurs(nombreJoueurs);


        //https://stackoverflow.com/questions/11705692/android-how-to-get-a-single-songs-id-info-from-a-know-uri?rq=3
        //Assigner le titre et l'artiste en fonction du lien URI
        //Peu importe le son, on veut juste simuler la sélection
        ActivityResultLauncher<String[]> Selecteur_musique = registerForActivityResult(
                new ActivityResultContracts.OpenMultipleDocuments(),
                uris -> {

                    if (!uris.isEmpty()) {
                        playlist = new ArrayList<>(uris);
                    }
                    lien_son = playlist.get(0);

                    lire_metadonnees(lien_son);


                    setup_media();
                    mediaPlayer.start();
                });
        Selecteur_musique.launch(new String[]{"audio/*"});



        timer = new CountDownTimer(30000, 100) // durée totale, intervalle de mise à jour
        {
            @Override
            public void onTick(long msRestantes)
            {

                chrono.setText(String.valueOf(msRestantes/1000));
            }

            @Override
            public void onFinish() //Chrono écoulé
            {
                mediaPlayer.stop(); //Arrêter la musique

                Intent intent = new Intent(RoundEnCoursActivity.this, AfficherReponseActivity.class);
                intent.putExtra("titre", titre);
                intent.putExtra("artiste", artiste);


                startActivity(intent);

            }
        }.start();

    }
}
