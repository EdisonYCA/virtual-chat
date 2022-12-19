module com.example.virtual {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.virtual to javafx.fxml;
    exports com.example.virtual;
}