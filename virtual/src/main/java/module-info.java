module com.example.virtual {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.virtual to javafx.fxml;
    opens com.example.virtual.login to javafx.fxml;
    exports com.example.virtual;
    exports com.example.virtual.login;

}