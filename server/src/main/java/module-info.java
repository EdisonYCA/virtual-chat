module com.example.virtual {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires org.apache.commons.io;

    opens com.chat.virtual.client to javafx.fxml;
    opens com.chat.virtual.server to javafx.fxml;

    exports com.chat.virtual.client;
    exports com.chat.virtual.server;

}