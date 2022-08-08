package fr.olten.xmas.ladder;

import fr.olten.xmas.clan.Clan;

import java.util.Random;

public class RankingLadder {

    public static int getRanking(Clan clan){
        // Temporary
        return new Random().nextInt(1, 100);
    }
}
