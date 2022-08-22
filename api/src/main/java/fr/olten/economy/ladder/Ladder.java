package fr.olten.economy.ladder;

import fr.olten.economy.clan.Clan;

import java.util.UUID;

public interface Ladder {

    int getRank(Clan clan);
    int getRank(UUID uuid);
}
