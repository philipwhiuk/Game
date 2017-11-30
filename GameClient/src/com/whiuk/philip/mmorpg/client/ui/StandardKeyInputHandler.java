package com.whiuk.philip.mmorpg.client.ui;

import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;

public abstract class StandardKeyInputHandler implements KeyInputHandler {

	@Override
    public final boolean keyEvent(final NiftyInputEvent inputEvent) {
		if (inputEvent == null || inputEvent instanceof NiftyStandardInputEvent) {
            return false;
        }
		return standardKeyEvent((NiftyStandardInputEvent) inputEvent);
    }

	public abstract boolean standardKeyEvent(NiftyStandardInputEvent inputEvent);
}
