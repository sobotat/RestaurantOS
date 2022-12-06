package com.restaurantos.controllers;

import com.restaurantos_db.*;
import com.restaurantos_domain.*;
import com.restaurantos.*;
import com.restaurantos_db.identity_maps.IdentityMapsHandler;
import com.restaurantos_domain.unit_of_works.OrderItemUnitOfWork;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
    VBox vBox_List, vBox_Table, vBox_Buttons;
    @FXML
    HBox hbox_ManagerAuth, hbox_BackButton, hbox_UserButton;
    @FXML
    HBox btn_Show, btn_ShowTodayMenu, btn_CreateMenu, btn_CreateOrder, btn_CreateUser, btn_Payment;
    @FXML
    ScrollPane scl_List;
    @FXML
    Text tv_Role, tv_UserName, tv_Show, tv_OrderButton, tv_MenuButton;

    Timer timerRefresh;
    TimerTask timerTask;
    Node currentViewNode, ordersList;
    AppSecurity.ManagerAuth managerAuth;
    LinkedList<HBox> buttons = new LinkedList<>();

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
                OrderItemUnitOfWork orderItemUnitOfWork = new OrderItemUnitOfWork();
                orderItemUnitOfWork.forceCommit();

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
        tv_MenuButton.setText("Create Menu Item");

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

        backToMain();
        MenuGateway menuGateway = new MenuGateway();
        Menu menu = menuGateway.findForDay(LocalDate.now());

        if(menu == null){
            logger.log(Level.ERROR, "Menu Not Found");
            return;
        }

        try {
            tv_MenuButton.setText("Create Menu Item");

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
        tv_MenuButton.setText("Create Menu");
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
        tv_Show.setText((showMenu ? "Show Order" : "Show Menu"));
        loadListView();
    }

    @FXML
    public void onCreateMenuClicked(){
        if(!AppSecurity.haveAuthForCreateMenu()) {
            logger.log(Level.INFO, "User dont have Authority");
            showWarning(btn_CreateMenu);
            return;
        }

        if(currentViewNode instanceof ScrollPane) {
            MenuGateway menuGateway = new MenuGateway();
            if(menuGateway.findForDay(LocalDate.now()) == null) {
                menuGateway.create(new Menu(0, LocalDate.now(), LocalDate.now()));
                loadListView();
            }else{
                if(MenuController.menuControllers.isEmpty()) {
                    return;
                }

                for (MenuController menuController : MenuController.menuControllers) {
                    Menu menu = menuController.menu;
                    if(menu.getDate().equals(LocalDate.now())){
                        menuController.flash();
                    }
                }
                showWarning(btn_CreateMenu);
            }
        }else{
            Menu menu = MenuViewController.menuViewController.menu;
            if(menu == null)
                return;

            MenuItemGateway menuItemGateway = new MenuItemGateway();
            LinkedList<MenuItem> menuItems = menuItemGateway.findAllForMenu(menu);
            FoodGateway foodGateway = new FoodGateway();
            LinkedList<Food> foods = foodGateway.findAllFoods();

            if(foods.isEmpty() || menuItems.size() == foods.size()) {
                showWarning(btn_CreateMenu);
                return;
            }

            Food food = null;
            while (food == null) {
                Food foodTmp = foods.get(new Random().nextInt(0, foods.size()));
                boolean sameFood = false;
                for(MenuItem menuItem : menuItems){
                    if(menuItem.getFood().getFoodId() == foodTmp.getFoodId())
                        sameFood = true;
                }
                if(!sameFood)
                    food = foodTmp;
            }

            MenuItem menuItem = new MenuItem( 0, menu, food, new Random().nextInt(5, 40), food.getCost());

            menuItemGateway.create(menuItem);
            MenuViewController.menuViewController.loadMenuItems();
        }
    }

    @FXML
    public void onCreateOrderClicked(){
        if(!AppSecurity.haveAuthForCreateOrder()) {
            logger.log(Level.INFO, "User dont have Authority");
            showWarning(btn_CreateOrder);
            return;
        }

        if(currentViewNode instanceof ScrollPane) {
            OrderGateway orderGateway = new OrderGateway();
            orderGateway.create(new Order(0, new TableGateway().find(1), LocalDate.now(), false, AppSecurity.getSignInUser().getUserId()));
            loadListView();
        }else{
            Order order = OrderViewController.orderViewController.order;

            MenuGateway menuGateway = new MenuGateway();
            Menu menu = menuGateway.findForDay(LocalDate.now());

            if(order == null || menu == null) {
                logger.log(Level.ERROR, "Order or Menu not found");
                return;
            }

            MenuItemGateway menuItemGateway = new MenuItemGateway();
            LinkedList<MenuItem> menuItems = menuItemGateway.findAllForMenu(menu);

            if(menuItems.isEmpty()) {
                logger.log(Level.ERROR, "Menu is Empty");
                return;
            }

            MenuItem menuItem = menuItems.get(new Random().nextInt(0, menuItems.size()));
            OrderItem orderItem = new OrderItem( 0 , order, menuItem, new Random().nextInt(1, 5), "Ordered", AppSecurity.getSignInUser().getUserId());

            // Unit of Work
            OrderItemUnitOfWork orderItemUnitOfWork = new OrderItemUnitOfWork();
            orderItemUnitOfWork.addToCreate(orderItem);

            OrderViewController.orderViewController.addOrderItem(orderItem);

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
        if(!AppSecurity.haveAuthForCreateUser()) {
            logger.log(Level.INFO, "User dont have Authority");
            showWarning(btn_CreateUser);
            return;
        }

        try {
            // Loading FXML
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Main.class.getResource("user-dialog.fxml"));
            VBox parent = fxmlLoader.load();

            // Setting a Controller
            UserDialogController userDialogController = fxmlLoader.getController();

            // Creating Scene and Stage for Dialog
            Scene scene = new Scene(parent);
            scene.getStylesheets().clear();
            scene.getStylesheets().add(Main.useDarkMode ? Main.darkMode_css : Main.lightMode_css);
            Stage stage = new Stage();
            stage.setAlwaysOnTop(true);
            stage.setResizable(false);
            stage.setTitle("User Info");
            stage.setScene(scene);

            // Adding Stage to Controller
            userDialogController.initAsCreate(stage);

            // Changing style of stage
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);

            // Loading App Icon
            javafx.scene.image.Image icon = new Image("/app_logo.png");
            if (icon.isError()) {
                logger.error("Icon Load Failed");
                logger.error(icon.exceptionProperty().get().getMessage());
            }
            stage.getIcons().add( icon);

            // Starting Dialog
            stage.showAndWait();
        } catch (IOException e) {
            logger.error("User Dialog load failed");
        }
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

        if(!AppSecurity.haveAuthForCreateUser()) {
            try {
                // Loading FXML
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(Main.class.getResource("user-dialog.fxml"));
                VBox parent = fxmlLoader.load();

                // Setting a Controller
                UserDialogController userDialogController = fxmlLoader.getController();

                // Creating Scene and Stage for Dialog
                Scene scene = new Scene(parent);
                scene.getStylesheets().clear();
                scene.getStylesheets().add(Main.useDarkMode ? Main.darkMode_css : Main.lightMode_css);
                Stage stage = new Stage();
                stage.setAlwaysOnTop(true);
                stage.setResizable(false);
                stage.setTitle("User Info");
                stage.setScene(scene);

                // Adding Stage to Controller
                userDialogController.initAsUpdate(stage, AppSecurity.getSignInUser());

                // Changing style of stage
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.DECORATED);

                // Loading App Icon
                javafx.scene.image.Image icon = new Image("/app_logo.png");
                if (icon.isError()) {
                    logger.error("Icon Load Failed");
                    logger.error(icon.exceptionProperty().get().getMessage());
                }
                stage.getIcons().add( icon);

                // Starting Dialog
                stage.showAndWait();
            } catch (IOException e) {
                logger.error("User Dialog load failed");
            }
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

        updateButtonsByAuth();
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
        refresh();
        backToMain();
    }

    private void refresh(){
        IdentityMapsHandler.refresh();
        logger.log(Level.INFO, "IdentityMaps were refreshed");

        loadListView();

        if(timerRefresh != null)
            timerRefresh.cancel();

        timerRefresh = new Timer(true);
        timerRefresh.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> refresh());
            }
        }, 60000);

    }

    @FXML
    public void onPaymentClicked(){
        if(OrderViewController.orderViewController == null) {
            showWarning(btn_Payment);
            return;
        }

        LinkedList<OrderItem> selectedOrderItems = new LinkedList<>();
        LinkedList<OrderItem> allOrderItems = new LinkedList<>();
        for(OrderItemViewController orderItemViewController : OrderItemViewController.orderItemViewControllers){
            if(orderItemViewController.selected){
                selectedOrderItems.add(orderItemViewController.orderItem);
            }
            allOrderItems.add(orderItemViewController.orderItem);
        }

        if(selectedOrderItems.isEmpty()){
            for(OrderItem orderItem : allOrderItems){
                if(orderItem.getState().equals("Served"))
                    selectedOrderItems.add(orderItem);
            }

            if(selectedOrderItems.isEmpty()) {
                showWarning(btn_Payment);
                return;
            }
        }

        Payment payment = new Payment(selectedOrderItems.getFirst().getOrder(), selectedOrderItems, allOrderItems);
        payment.pay();
        payment.exportPayment();

        OrderViewController.orderViewController.loadOrderItems();
        OrderViewController.orderViewController.updateStatus();

        for(OrderItemViewController orderItemViewController : OrderItemViewController.orderItemViewControllers){
            orderItemViewController.unselectItem();
        }
    }

    public void updateButtonsByAuth(){
        if(vBox_Buttons.getChildren().size() > 3)
            vBox_Buttons.getChildren().remove(3, vBox_Buttons.getChildren().size());

        if(AppSecurity.haveAuthForCreateOrder())
            vBox_Buttons.getChildren().add(buttons.get(2));
        if(AppSecurity.haveAuthForCreateMenu())
            vBox_Buttons.getChildren().add(buttons.get(3));
        if(AppSecurity.haveAuthForCreateUser())
            vBox_Buttons.getChildren().add(buttons.get(4));

        vBox_Buttons.getChildren().add(buttons.get(5));
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

        buttons.addAll(List.of(new HBox[]{ btn_Show, btn_ShowTodayMenu, btn_CreateOrder, btn_CreateMenu, btn_CreateUser, btn_Payment}));

        timerRefresh = new Timer(true);
        timerRefresh.schedule( new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> refresh());
            }
        }, 120000);

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