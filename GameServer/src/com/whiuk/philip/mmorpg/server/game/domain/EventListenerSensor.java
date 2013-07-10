package com.whiuk.philip.mmorpg.server.game.domain;

import java.util.EventListener;

import com.whiuk.philip.mmorpg.server.game.ai.Sensor;

/**
 * An implementation of a {@link Sensor} using the {@link EventListener} model.
 * @author Philip
 *
 */
public interface EventListenerSensor extends Sensor, EventListener {

}
