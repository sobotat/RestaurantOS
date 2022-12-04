package com.restaurantos.controllers;

import com.restaurantos.*;
import com.restaurantos.gateways.*;
import com.restaurantos.gateways.identity_maps.IdentityMapsHandler;
import com.restaurantos.gateways.unit_of_works.OrderItemUnitOfWork;
import com.restaurantos.gateways.unit_of_works.UnitOfWork;
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
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Controller {
    private static final Logger logger = LogManager.getLogger(Controller.class.getName());
    public static Controller controller = null;
    boolean showMenu = false;

    @FXML
    VBox vBox_List, vBox_Table;
    @FXML
    HBox hbox_ManagerAuth, hbox_BackButton, hbox_UserButton;
    @FXML
    ScrollPane scl_List;
    @FXML
    Text tv_Role, tv_UserName, tv_OrderButton;

    Node currentViewNode, ordersList;
    AppSecurity.ManagerAuth managerAuth;

    public void loadListView(){
        if(!showMenu)
            loadOrderView();
        else
            loadMenuView();
    }

    private void loadOrderView(){
        OrderGateway orderGateway = new OrderGateway();
        LinkedList<Order> orders = orderGateway.findAllForDay(LocalDate.now());

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

    private void loadMenuView(){
        MenuGateway menuGateway = new MenuGateway();
        LinkedList<Menu> menus = menuGateway.findAllMenus();

        if(menus.isEmpty() || !(currentViewNode instanceof ScrollPane))
            return;

        double curVValue = scl_List.getVvalue();
        vBox_List.getChildren().clear();
        MenuController.menuControllers = new LinkedList<>();
        Node[] nodes = new Node[menus.size()];

        double prefHeight = 0;
        try {
            int rowIndex = 0;
            int itemsCountOnRow = 0;

            HBox row = new HBox();
            row.alignmentProperty().set(Pos.CENTER);

            for (int i = 0; i < nodes.length; i++){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(Main.class.getResource("menu-item.fxml"));
                nodes[i] = fxmlLoader.load();

                MenuController menuController = fxmlLoader.getController();
                menuController.setItem(menus.get(i));

                //nodes[i].setOnMouseEntered(event -> nodes[h].setStyle("-fx-background-color: colorLightGray"));
                //nodes[i].setOnMouseExited(event -> nodes[h].setStyle("-fx-background-color: colorDarkGray"));

                final int index = i;
                nodes[i].setOnMouseClicked(event -> {
                    onMenuClicked(index);
                });

                if(itemsCountOnRow == 0) {
                    itemsCountOnRow = (int) (vBox_List.getPrefWidth() / (int) menuController.vbox_Root.getPrefWidth());
                    if(itemsCountOnRow == 0)
                        itemsCountOnRow = 1;
                }
                if(prefHeight == 0)
                    prefHeight = menuController.vbox_Root.getPrefHeight();

                if(rowIndex % itemsCountOnRow == 0 && rowIndex != 0) {
                    vBox_List.getChildren().add(row);
                    row = new HBox();
                    row.alignmentProperty().set(Pos.CENTER);
                }

                MenuController.menuControllers.add(menuController);
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
        tv_OrderButton.setText("Create Order Item");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Main.class.getResource("view.fxml"));
            OrderViewController orderViewController = new OrderViewController();
            fxmlLoader.setController(orderViewController);
            VBox root = fxmlLoader.load();

            root.getChildren().get(0).setOnMouseClicked(event -> {
                backToMain();
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

    public void onMenuClicked(int index){
        MenuController menuController = MenuController.menuControllers.get(index);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Main.class.getResource("view.fxml"));
            MenuViewController menuViewController = new MenuViewController();
            fxmlLoader.setController(menuViewController);
            VBox root = fxmlLoader.load();

            root.getChildren().get(0).setOnMouseClicked(event -> {
                backToMain();
            });

            menuViewController.setUp(menuController.menu);
            double padding = vBox_Table.paddingProperty().getValue().getLeft() * 2;
            menuViewController.updateViewSize(vBox_Table.getWidth() - padding, vBox_Table.getHeight() - padding);

            ordersList = currentViewNode;
            currentViewNode = root;
            vBox_Table.getChildren().clear();
            vBox_Table.getChildren().add(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onShowTodayMenuClicked(){

        MenuGateway menuGateway = new MenuGateway();
        Menu menu = menuGateway.find(0);

        if(menu == null){
            logger.log(Level.ERROR, "Menu Not Found");
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Main.class.getResource("view.fxml"));
            MenuViewController menuViewController = new MenuViewController();
            fxmlLoader.setController(menuViewController);
            VBox root = fxmlLoader.load();

            root.getChildren().get(0).setOnMouseClicked(event -> {
                backToMain();
            });

            menuViewController.setUp(menu);
            double padding = vBox_Table.paddingProperty().getValue().getLeft() * 2;
            menuViewController.updateViewSize(vBox_Table.getWidth() - padding, vBox_Table.getHeight() - padding);

            ordersList = currentViewNode;
            currentViewNode = root;
            vBox_Table.getChildren().clear();
            vBox_Table.getChildren().add(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void backToMain(){
        vBox_Table.getChildren().clear();
        vBox_Table.getChildren().add(ordersList);
        currentViewNode = ordersList;
        loadListView();

        tv_OrderButton.setText("Create Order");
    }

    @FXML
    public void onLogoutClicked(){
        backToMain();
        managerAuth = null;
        LoginController.loginController.initialize();
        AppSecurity.logout();
        updateUserInfo();
    }

    @FXML
    public void onShowMenuClicked(){
        showMenu = !showMenu;
        loadListView();
    }

    @FXML
    public void onCreateMenuClicked(){
        MenuGateway menuGateway = new MenuGateway();
        menuGateway.create(new Menu(0, new Date(), new Date()));
    }

    @FXML
    public void onCreateOrderClicked(){
        if(currentViewNode instanceof ScrollPane) {
            OrderGateway orderGateway = new OrderGateway();
            orderGateway.create(new Order(0, new TableGateway().find(1), LocalDate.now(), false));
            loadListView();
        }else{
            Order order = OrderViewController.orderViewController.order;
            if(order == null)
                return;

            MenuItemGateway menuItemGateway = new MenuItemGateway();
            MenuItem menuItem = menuItemGateway.find(new Random().nextInt(1, 4));
            if(menuItem == null)
                return;

            OrderItem orderItem = new OrderItem( 0 , order, menuItem, new Random().nextInt(1, 5), "Ordered");

            // Unit of Work
            OrderItemUnitOfWork orderItemUnitOfWork = new OrderItemUnitOfWork();
            orderItemUnitOfWork.addToCreate(orderItem);

            orderItemUnitOfWork.committedCallBack = () -> {
                Platform.runLater(() -> {
                    OrderViewController.orderViewController.loadOrderItems();
                });
                logger.log(Level.INFO, "OrderItem Created");
            };

            // Old
            /*
            OrderItemGateway orderItemGateway = new OrderItemGateway();
            orderItemGateway.create(orderItem);
            */
        }
    }

    @FXML
    public void onCreateUserClicked(){

        if(!AppSecurity.getSignInUser().getUserRole().getName().equals("Manager"))
            return;

        LinkedList<User> randomUsers = new LinkedList<>();
        randomUsers.add(new User(0, "Karel", "Random", LocalDate.of(2001, 1, 1), "karel" + new Random().nextInt(99) + "@gmail.com", "1234", new User.UserRole(0, "Manager", "")));
        randomUsers.add(new User(0, "Laura", "New", LocalDate.of(2001, 1, 1), "laura" + new Random().nextInt(99) + "@gmail.com", "1234", new User.UserRole(1, "Service", "")));
        randomUsers.add(new User(0, "Michail", "Novak", LocalDate.of(2001, 1, 1), "michail" + new Random().nextInt(99) + "@gmail.com", "1234", new User.UserRole(2, "Chef", "")));
        randomUsers.add(new User(0, "Emma", "Nobody", LocalDate.of(2001, 1, 1), "emma" + new Random().nextInt(99) + "@gmail.com", "1234", new User.UserRole(1, "Service", "")));

        UserGateway userGateway = new UserGateway();
        User selectedUser = randomUsers.get(new Random().nextInt(randomUsers.size()));
        selectedUser.setPassword(selectedUser.getPassword());
        userGateway.create(selectedUser);

        if(UserViewController.userViewController != null)
            UserViewController.userViewController.loadUserItems();
    }

    @FXML
    public void onManagerAuthClicked(){
        if(managerAuth == null && !AppSecurity.getSignInUser().getUserRole().getName().equals("Manager")) {
            managerAuth = new AppSecurity.ManagerAuth();
        }else if(managerAuth != null) {
            managerAuth.logoutManager();
            managerAuth = null;
        }
    }

    @FXML
    public void onUsersClicked(){
        if(currentViewNode instanceof VBox) {
            showWarning(hbox_UserButton);
            return;
        }

        if(!AppSecurity.getSignInUser().getUserRole().getName().equals("Manager")) {
            showWarning(hbox_UserButton);
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Main.class.getResource("view.fxml"));
            UserViewController userViewController = new UserViewController();
            fxmlLoader.setController(userViewController);
            VBox root = fxmlLoader.load();

            root.getChildren().get(0).setOnMouseClicked(event -> {
                backToMain();
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

        tv_Role.setText(signInUser.getUserRole().getName());
        tv_UserName.setText(signInUser.getFirstName() + " " + signInUser.getLastName());

        String style = hbox_ManagerAuth.getStyle();
        if(signInUser.getUserRole().getName().equals("Manager")){
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
            backToMain();
        else
            showWarning(hbox_BackButton);
    }

    @FXML
    public void onRefreshClicked(){
        IdentityMapsHandler.refresh();
        logger.log(Level.INFO, "IdentityMaps were refreshed");
    }

    private void showWarning(Node node){
        String orgStyle = node.getStyle();
        node.setStyle(orgStyle + "-fx-background-color: colorRed;");

        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                node.setStyle(orgStyle + "-fx-background-color: colorDarkGray;");
            }
        }, 1000);
    }

    public void start(){
        updateUserInfo();
        ordersList = scl_List;
        currentViewNode = vBox_Table.getChildren().get(0);
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