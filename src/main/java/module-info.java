module com.restaurantos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    requires scrypt;

    requires com.google.gson;
    requires org.json;

    requires org.apache.logging.log4j;
    requires aparapi;

    requires java.sql;

    opens com.restaurantos to javafx.fxml;
    exports com.restaurantos;
    exports com.restaurantos.controllers;
    opens com.restaurantos.controllers to javafx.fxml;
    exports com.restaurantos_domain;
    opens com.restaurantos_domain to javafx.fxml;
}