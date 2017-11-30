package com.whiuk.philip.mmorpg.client.ui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.controls.ScrollPanel;
import de.lessvoid.nifty.controls.ScrollPanel.AutoScroll;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;

/**
 *
 * @author Philip
 *
 */
class ChatAreaController implements Controller {
    /**
     *
     */
    private ScrollPanel scrollPanel;
    /**
     *
     */
    private Element textArea;
    /**
     *
     */
    private Screen screen;

    @Override
    public final void bind(final Nifty nifty,
            final Screen s, final Element element,
            final Parameters parameters) {
        this.screen = s;
        scrollPanel = element.findNiftyControl("scroll_panel",
                ScrollPanel.class);
        textArea = element.findElementById("text_area");
    }

    @Override
    public final void init(final Parameters parameters) {
    }

    @Override
    public final void onStartScreen() {
    }

    @Override
    public final void onFocus(final boolean getFocus) {
    }

    @Override
    public final boolean inputEvent(final NiftyInputEvent inputEvent) {
        return false;
    }

    /**
     * @param auto The auto scroll
     */
    final void setAutoScroll(final AutoScroll auto) {
        scrollPanel.setAutoScroll(auto);
    }

    /**
     * @return The auto scroll
     */
    final AutoScroll getAutoScroll() {
        return scrollPanel.getAutoScroll();
    }

    /**
     *
     * @param text The text to append.
     */
    @SuppressWarnings("unused")
    private void append(final String text) {
        setText(getText() + text);
    }

    /**
     *
     * @param text The test to the set the chat area to.
     */
    private void setText(final String text) {
        textArea.getRenderer(TextRenderer.class).setText(text);
        screen.layoutLayers();
        textArea.setHeight(
                textArea.getRenderer(TextRenderer.class).getTextHeight());
    }

    /**
     * @return The current text.
     */
    final String getText() {
        return textArea.getRenderer(TextRenderer.class).getOriginalText();
    }

	@Override
	public void onEndScreen() {
		// TODO Auto-generated method stub
		
	}
}
