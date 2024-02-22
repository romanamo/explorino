module de.romanamo.explorino {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;
    requires java.logging;

    opens de.romanamo.explorino to javafx.fxml;
    exports de.romanamo.explorino;
}