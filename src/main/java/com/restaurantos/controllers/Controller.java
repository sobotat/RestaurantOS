package com.restaurantos.controllers;

import com.restaurantos.AppSecurity;
import com.restaurantos.Main;
import com.restaurantos.Order;
import com.restaurantos.User;
import com.restaurantos.gateways.OrderGateway;
import com.restaurantos.gateways.UserGateway;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Controller {
    private static final Logger logger = LogManager.getLogger(Controller.class.getName());
    public static Controller controller = null;

    @FXML
    VBox vBox_List, vBox_Table;
    @FXML
    HBox hbox_ManagerAuth;
    @FXML
    ScrollPane scl_List;
    @FXML
    Text tv_Role, tv_UserName;

    Node currentViewNode, ordersList;
    AppSecurity.ManagerAuth managerAuth;

    public void loadListView(){
        OrderGateway orderGateway = new OrderGateway();
        LinkedList<Order> orders = orderGateway.findAllForDay();

        if(orders.isEmpty() || !(currentViewNode instanceof ScrollPane))
            return;

        double curVValue = scl_List.getVvalue();
        vBox_List.getChildren().clear();
        OrderController.orderControllers = new LinkedList<>();
        Node[] nodes = new Node[orders.size()];

        double prefHeight = 0;
        try {
            int rowIndex = 0;
            int itemsCountOnRow = 0;

            HBox row = new HBox();
            row.alignmentProperty().set(Pos.CENTER);

            for (int i = 0; i < nodes.length; i++){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(Main.class.getResource("order-item.fxml"));
                nodes[i] = fxmlLoader.load();

                OrderController orderController = fxmlLoader.getController();
                orderController.setItem(orders.get(i));

                //nodes[i].setOnMouseEntered(event -> nodes[h].setStyle("-fx-background-color: colorLightGray"));
                //nodes[i].setOnMouseExited(event -> nodes[h].setStyle("-fx-background-color: colorDarkGray"));

                final int index = i;
                nodes[i].setOnMouseClicked(event -> {
                    onOrderClicked(index);
                });

                if(itemsCountOnRow == 0) {
                    itemsCountOnRow = (int) (vBox_List.getPrefWidth() / (int) orderController.vbox_Root.getPrefWidth());
                    if(itemsCountOnRow == 0)
                        itemsCountOnRow = 1;
                }
                if(prefHeight == 0)
                    prefHeight = orderController.vbox_Root.getPrefHeight();

                if(rowIndex % itemsCountOnRow == 0 && rowIndex != 0) {
                    vBox_List.getChildren().add(row);
                    row = new HBox();
                    row.alignmentProperty().set(Pos.CENTER);
                }

                OrderController.orderControllers.add(orderController);
                row.getChildren().add(nodes[i]);

                rowIndex++;
            }

            if(!row.getChildren().isEmpty()){
                vBox_List.getChildren().add(row);
            }

            double finalPrefHeight = (vBox_List.getChildren().size() + 1) * prefHeight;
            Platform.runLater(() -> {
                if(scl_List.getHeight() < finalPrefHeight) {
                    vBox_List.setMinHeight(finalPrefHeight);
                    vBox_List.setMaxHeight(finalPrefHeight);
                }
                scl_List.setVvalue(curVValue);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onOrderClicked(int index){
        OrderController orderController = OrderController.orderControllers.get(index);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Main.class.getResource("view.fxml"));
            OrderViewController orderViewController = new OrderViewController();
            fxmlLoader.setController(orderViewController);
            VBox root = fxmlLoader.load();

            root.getChildren().get(0).setOnMouseClicked(event -> {
                backToOrdersList();
            });

            orderViewController.setUp(orderController.order);
            double padding = vBox_Table.paddingProperty().getValue().getLeft() * 2;
            orderViewController.updateViewSize(vBox_Table.getWidth() - padding, vBox_Table.getHeight() - padding);

            ordersList = currentViewNode;
            currentViewNode = root;
            vBox_Table.getChildren().clear();
            vBox_Table.getChildren().add(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void backToOrdersList(){
        vBox_Table.getChildren().clear();
        vBox_Table.getChildren().add(ordersList);
        currentViewNode = ordersList;
        loadListView();
    }

    @FXML
    public void onLogoutClicked(){
        backToOrdersList();
        managerAuth = null;
        LoginController.loginController.initialize();
        AppSecurity.logout();
        updateUserInfo();
    }

    @FXML
    public void onAddOrderClicked(){
        OrderGateway orderGateway = new OrderGateway();
        orderGateway.create(new Order(0, 1, new Date(), false));
        loadListView();
    }

    @FXML
    public void onManagerAuthClicked(){
        if(managerAuth == null && !AppSecurity.getSignInUser().userRole.name.equals("Manager")) {
            managerAuth = new AppSecurity.ManagerAuth();
        }else if(managerAuth != null) {
            managerAuth.logoutManager();
            managerAuth = null;
        }
    }

    @FXML
    public void onUsersClicked(){
        if(currentViewNode instanceof VBox)
            return;

        if(!AppSecurity.getSignInUser().userRole.name.equals("Manager"))
            return;

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Main.class.getResource("view.fxml"));
            UserViewController userViewController = new UserViewController();
            fxmlLoader.setController(userViewController);
            VBox root = fxmlLoader.load();

            root.getChildren().get(0).setOnMouseClicked(event -> {
                backToOrdersList();
            });

            userViewController.setUp();
            double padding = vBox_Table.paddingProperty().getValue().getLeft() * 2;
            userViewController.updateViewSize(vBox_Table.getWidth() - padding, vBox_Table.getHeight() - padding);

            ordersList = currentViewNode;
            currentViewNode = root;
            vBox_Table.getChildren().clear();
            vBox_Table.getChildren().add(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateUserInfo(){
        User signInUser = AppSecurity.getSignInUser();
        if(signInUser == null)
            return;

        tv_Role.setText(signInUser.userRole.name);
        tv_UserName.setText(signInUser.firstName + " " + signInUser.lastName);

        String style = hbox_ManagerAuth.getStyle();
        if(signInUser.userRole.name.equals("Manager")){
            hbox_ManagerAuth.setStyle(style + "-fx-background-color: colorRed;");
        }else{
            hbox_ManagerAuth.setStyle(style + "-fx-background-color: colorDarkGray;");
        }
    }

    @FXML
    public void onDarkModeClicked(){
        Main.setDarkMode(!Main.useDarkMode);
        logger.log(Level.INFO, Main.useDarkMode ? "DarkMode Enabled" : "DarkMode Disabled");
    }

    @FXML
    public void onBackClicked(){
        if(!(currentViewNode instanceof ScrollPane))
            backToOrdersList();
    }

    public void start(){
        updateUserInfo();
        ordersList = scl_List;
        currentViewNode = vBox_Table.getChildren().get(0);

        OrderGateway orderGateway = new OrderGateway();
        for(int i = 0; i < 15; i++){
            orderGateway.create(new Order(0, new Random().nextInt(3), new Date(), false));
        }

        LinkedList<User> users = new LinkedList<>();
        users.add(new User(1, "Karel", "Novak", new Date(2000, Calendar.MARCH, 10), "manager@gmail.com", "1234", new User.UserRole(0, "Manager", "description")));
        users.add(new User(2, "Michal", "Novak", new Date(2000, Calendar.MARCH, 10), "email@gmail.com", "1234", new User.UserRole(2, "Chef", "description")));
        users.add(new User(3, "Karel", "Novak", new Date(2000, Calendar.MARCH, 10), "email2@gmail.com", "1234", new User.UserRole(1, "Service", "description")));
        users.add(new User(4, "Karel", "Novak", new Date(2000, Calendar.MARCH, 10), "email3@gmail.com", "1234", new User.UserRole(1, "Service", "description")));
        users.add(new User(5, "Karel", "Novak", new Date(2000, Calendar.MARCH, 10), "email4@gmail.com", "1234", new User.UserRole(1, "Service", "description")));
        users.add(new User(6, "Karel", "Novak", new Date(2000, Calendar.MARCH, 10), "email5@gmail.com", "1234", new User.UserRole(1, "Service", "description")));
        UserGateway.fakeSetOfDatabase(users);
    }

    // On Start
    public void initialize(){
        vBox_List.setPrefWidth(scl_List.widthProperty().get());
        vBox_List.setMaxWidth(scl_List.widthProperty().get());
        vBox_List.setMinWidth(scl_List.widthProperty().get());

        AtomicReference<Timer> timer = new AtomicReference<>(new Timer(true));

        scl_List.widthProperty().addListener((obs, oldVal, newVal) -> {
            vBox_List.setPrefWidth(scl_List.widthProperty().get());
            vBox_List.setMaxWidth(scl_List.widthProperty().get());
            vBox_List.setMinWidth(scl_List.widthProperty().get());

            timer.get().cancel();
            timer.set(new Timer(true));
            timer.get().schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        loadListView();
                    });
                }
            }, 250);
        });

        scl_List.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (vBox_List.getChildren().size() * 30 < scl_List.heightProperty().get()){
                vBox_List.setMaxHeight(scl_List.heightProperty().get());
                vBox_List.setMinHeight(scl_List.heightProperty().get());
            }else {
                vBox_List.setMaxHeight(vBox_List.getChildren().size() * OrderController.orderControllers.getFirst().vbox_Root.getHeight());
                vBox_List.setMinHeight(vBox_List.getChildren().size() * OrderController.orderControllers.getFirst().vbox_Root.getHeight());
            }
        });



    }
}