package com.example.pushforce;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class DebutRoundActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_debut_round);

        // Récupérer les TextView du layout
        TextView view_scoreJ1 = findViewById(R.id.score_J1);
        int scoreJ1 = 0;
        view_scoreJ1.setText(scoreJ1);
    }
}
