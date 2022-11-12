module com.restaurant_os.restaurantos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    requires com.google.gson;
    requires org.json;

    requires org.apache.logging.log4j;
    requires aparapi;

    opens com.restaurant_os.restaurantos to javafx.fxml;
    exports com.restaurant_os.restaurantos;
}