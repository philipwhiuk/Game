package com.whiuk.philip.mmorpg.server.game.controller;

import com.whiuk.philip.mmorpg.server.game.domain.PlayerCharacter;

public interface ZoneController {

    void characterEntered(Long zone, PlayerCharacter pc);

}
