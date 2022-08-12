package fr.olten.economy.clan.option;

import lombok.Getter;

public enum ClanOption {

    FRIENDLY_FIRE(Boolean.class);

    private @Getter final Class<?> valueType;

    ClanOption(Class<?> valueType) {
        this.valueType = valueType;
    }
}
