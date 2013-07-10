package com.whiuk.philip.mmorpg.client;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.ScrollPanel.AutoScroll;
import de.lessvoid.nifty.controls.scrollpanel.ScrollPanelControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 *
 * @author Philip
 *
 */
public class ChatAreaController implements Controller {
    /**
     *
     */
    private ScrollPanelControl scrollPanel;
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
            final Properties parameter,
            final Attributes controlDefinitionAttributes) {
        this.screen = s;
        scrollPanel = element.findControl("scroll_panel",
                ScrollPanelControl.class);
        textArea = element.findElementByName("text_area");
    }

    @Override
    public final void init(final Properties parameter,
            final Attributes controlDefinitionAttributes) {
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
     *
     * @param auto
     */
    public final void setAutoScroll(final AutoScroll auto) {
        scrollPanel.setAutoScroll(auto);
    }

    /**
     *
     * @return
     */
    public final AutoScroll getAutoScroll() {
        return scrollPanel.getAutoScroll();
    }

    /**
     *
     * @param text
     */
    public final void append(final String text) {
        setText(getText() + text);
    }

    /**
     *
     * @param text
     */
    public final void setText(final String text) {
        textArea.getRenderer(TextRenderer.class).setText(text);
        screen.layoutLayers();
        textArea.setHeight(
                textArea.getRenderer(TextRenderer.class).getTextHeight());
    }

    /**
     *
     * @return
     */
    public final String getText() {
        return textArea.getRenderer(TextRenderer.class).getOriginalText();
    }
}
