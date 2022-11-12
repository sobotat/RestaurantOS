module com.restaurantos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    requires com.google.gson;
    requires org.json;

    requires org.apache.logging.log4j;
    requires aparapi;

    opens com.restaurantos to javafx.fxml;
    exports com.restaurantos;
    exports com.restaurantos.controllers;
    opens com.restaurantos.controllers to javafx.fxml;
}