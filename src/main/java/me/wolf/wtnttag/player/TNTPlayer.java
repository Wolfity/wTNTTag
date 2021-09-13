package me.wolf.wtnttag.player;

import java.util.UUID;

public class TNTPlayer {

    private final UUID uuid;
    private boolean isSpectator, isTagger;

    public TNTPlayer(final UUID uuid) {
        this.uuid = uuid;
        this.isSpectator = false;
        this.isTagger = false;
    }

    public void setSpectator(boolean spectating) {
        isSpectator = spectating;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isSpectator() {
        return isSpectator;
    }

    public boolean isTagger() {
        return isTagger;
    }

    public void setTagger(boolean tagger) {
        isTagger = tagger;
    }
}
