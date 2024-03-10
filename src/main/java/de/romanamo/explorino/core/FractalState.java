package de.romanamo.explorino.core;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class FractalState {

    /**
     * Side info.
     */
    private final BooleanProperty isSideInfo = new SimpleBooleanProperty(false);

    /**
     * Info Channel.
     */
    private final SimpleIntegerProperty infoChannel = new SimpleIntegerProperty(0);

    /**
     * Display Channel.
     */
    private final SimpleIntegerProperty displayChannel = new SimpleIntegerProperty(0);

    /**
     * SideInfoProperty.
     *
     * @return SideInfoProperty
     */
    public BooleanProperty isSideInfoProperty() {
        return isSideInfo;
    }

    /**
     * InfoChannelProperty.
     *
     * @return infoChannelProperty
     */
    public SimpleIntegerProperty infoChannelProperty() {
        return infoChannel;
    }

    /**
     * DisplayChannelProperty.
     *
     * @return DisplayChannelProperty
     */
    public SimpleIntegerProperty displayChannelProperty() {
        return displayChannel;
    }

    /**
     * Updates info channel.
     */
    public void updateInfoChannel() {
        this.infoChannel.set(infoChannel.get() + 1);
    }

    /**
     * Updates display channel.
     */
    public void updateDisplayChannel() {
        this.displayChannel.set(displayChannel.get() + 1);
    }
}
