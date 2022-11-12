package com.restaurant_os.restaurantos;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public abstract class ViewController {

    @FXML
    Text tv_LeftTitle, tv_RightTitle, tv_Text;
    @FXML
    VBox vbox_List, vbox_Root;
    @FXML
    HBox hbox_Top;

}
