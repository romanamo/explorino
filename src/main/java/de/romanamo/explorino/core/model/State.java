package de.romanamo.explorino.core.model;

import de.romanamo.explorino.math.Point;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class State {

    BooleanProperty isSideInfo = new SimpleBooleanProperty(false);

    private final SimpleIntegerProperty infoChannel = new SimpleIntegerProperty(0);

    private final SimpleIntegerProperty displayChannel = new SimpleIntegerProperty(0);

    public BooleanProperty isSideInfoProperty() {
        return isSideInfo;
    }

    public SimpleIntegerProperty infoChannelProperty() {
        return infoChannel;
    }

    public SimpleIntegerProperty displayChannelProperty() {
        return displayChannel;
    }

    public void updateInfoChannel() {
        this.infoChannel.set(infoChannel.get() + 1);
    }

    public void updateDisplayChannel() {
        this.displayChannel.set(displayChannel.get() + 1);
    }
}
