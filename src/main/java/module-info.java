module com.bank {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires json.simple;

    opens com.bank to javafx.fxml;
    exports com.bank;
}