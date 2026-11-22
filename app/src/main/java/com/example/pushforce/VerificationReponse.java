package com.example.pushforce;

import java.text.Normalizer;
import java.util.List;

public class VerificationReponse {

    // Constantes : les bonne réponse pour le son en cours
    private final String artisteAttendu;
    private final String titreAttendu;

    // Constructeur : on crée un vérificateur pour une seule chanson
    public VerificationReponse(String artisteAttendu, String titreAttendu) {
        this.artisteAttendu = artisteAttendu;
        this.titreAttendu = titreAttendu;
    }


    //Les méthodes utilisées par EcouteActivity =====

    public boolean artisteTrouve(List<String> propositions) {
        return estDansPropositions(artisteAttendu, propositions);
    }

    public boolean titreTrouve(List<String> propositions) {
        return estDansPropositions(titreAttendu, propositions);
    }

    public int calculerPoints(List<String> propositions) {
        int points = 0;
        if (artisteTrouve(propositions)) {
            points = points + 1;
        }
        if (titreTrouve(propositions)) {
            points = points + 1;
        }
        return points;
    }

    //La mécanique interne =====

    //Vérifier si la réponse attendue est au moins dans une des propositions
    private boolean estDansPropositions(String attendu, List<String> propositions) {
        if (propositions == null) {
            return false;
        }
        for (String proposition : propositions) {
            if (estDansPhrase(attendu, proposition)) {
                return true;
            }
        }
        return false;
    }

    // Vérifer si tout ce qui est attendu est là ( à peu près )
    private boolean estDansPhrase(String attendu, String phrase) {
        String[] motsAttendus = normaliser(attendu).split(" ");
        String[] motsDits = normaliser(phrase).split(" ");

        for (String motAttendu : motsAttendus) {
            if (!motPresent(motAttendu, motsDits)) {
                return false;      // un seul mot manque => pas trouvé
            }
        }
        return true;
    }

    // Permet d'être plus tolérant parce le micro google ne détecte pas mauvais djo, il détecte mauvais joe c'est tout
    private boolean motPresent(String motAttendu, String[] motsDits) {
        for (String motDit : motsDits) {
            if (distance(motAttendu, motDit) <= tolerance(motAttendu)) {
                return true;
            }
        }
        return false;
    }

    // Nombre d'erreurs acceptées selon la longueur du mot l'ia m'a aidé ici j'avoue la méthpde de Levenshtein je connaissais pas mais c'est pas mal
    private int tolerance(String mot) { //Cette fonction sert à définir les règles, jusqu'à quel taux d'erreur on accepte une réponse
        if (mot.length() <= 2) return 0;
        if (mot.length() <= 5) return 1;
        if (mot.length() <= 9) return 2;
        return 3;
    }

        // "ezvubfebvebf EFFFEFEBBV !" => "ezvubfebvebf efffefebbv"
    private static String normaliser(String texte) {
        String t = texte.toLowerCase();
        t = Normalizer.normalize(t, Normalizer.Form.NFD).replaceAll("\\p{M}", "");  // accents
        t = t.replaceAll("[^a-z0-9 ]", " ");                                      // ponctuation
        t = t.replace("dj", "j").replace("ph", "f").replace("y", "i").replace("ck", "k"); // sons
        t = t.replaceAll(" +", " ").trim();                                       // espaces
        return t;
    }

    // Distance de Levenshtein : nombre minimum de lettres à changer pour passer de a à b

    /*Il remplit un tableau où chaque case dit "combien de modifications pour transformer
    le début du mot A en début du mot B". À chaque case, il choisit la moins chère des
    trois opérations : supprimer, ajouter ou remplacer une lettre. La case en bas à droite
    contient la réponse pour les mots entiers.*/
    private static int distance(String a, String b) {
        int[][] d = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) d[i][0] = i; //mettre mots attendus dans la première colonne
        for (int j = 0; j <= b.length(); j++) d[0][j] = j; //mettre mots dits dans la première ligne

        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                int cout = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1; //calculer les coûts si c'est bon 0 sinon 1
                d[i][j] = Math.min(Math.min(    //gérer la longeur du mot (si redd ou re au lieu de red)
                                d[i - 1][j] + 1,          // supprimer une lettre
                                d[i][j - 1] + 1),         // ajouter une lettre
                        d[i - 1][j - 1] + cout);  // remplacer (ou garder si identique)
            }
        }
        return d[a.length()][b.length()];
    }
}
