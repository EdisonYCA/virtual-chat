module com.example.virtual {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.chat.virtual.client to javafx.fxml;
    exports com.chat.virtual.client;
    exports com.chat.virtual.server;
    opens com.chat.virtual.server to javafx.fxml;
}