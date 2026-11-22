package com.example.pushforce;

// Les points de chaque joueur, gardés pendant toute la partie
public class Scores {

    private static final int[] points = new int[5];   // cases 1 à 4 (la case 0 ne sert pas)

    public static void ajouter(int joueur, int nb) {
        if (joueur >= 1 && joueur <= 4) {
            points[joueur] = points[joueur] + nb;
        }
    }

    public static int get(int joueur) {
        if (joueur >= 1 && joueur <= 4) {
            return points[joueur];
        }
        return 0;
    }

    public static void reinitialiser() {
        for (int i = 0; i < points.length; i++) {
            points[i] = 0;
        }
    }
}