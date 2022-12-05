package com.restaurantos.controllers;

import com.restaurantos_db.UserGateway;
import com.restaurantos_domain.AppSecurity;
import com.restaurantos_domain.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserDialogController {
    private static final Logger logger = LogManager.getLogger(UserDialogController.class.getName());

    @FXML
    TextField tf_FirstName, tf_LastName, tf_Email, tf_Password;
    @FXML
    TextField tf_Role, tf_BornDate;
    @FXML
    Text tv_ActionButton;

    Stage stage;
    User user;

    @FXML
    void onBackClicked() {
        stage.close();
    }

    @FXML
    void onActionClicked() {
        UserGateway userGateway = new UserGateway();

        String firstName = tf_FirstName.getText();
        String lastName = tf_LastName.getText();
        String email = tf_Email.getText();
        String password = tf_Password.getText();
        String roleName = tf_Role.getText();
        LocalDate bornDate;
        try {
            bornDate = LocalDate.parse(tf_BornDate.getText(),
                    DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        }catch (DateTimeException e){
            logger.error("Date parse failed\n" + e.getMessage());
            return;
        }

        User.UserRole role = userGateway.findUserRoleByName(roleName);
        if(role == null){
            logger.error("Role not found");
            return;
        }

        if(email.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            logger.info("Fill firstName, lastName and email");
            return;
        }

        if(user != null)
            updateUser(userGateway, firstName, lastName, email, password, role, bornDate);
        else
            createUser(userGateway, firstName, lastName, email, password, role, bornDate);

        if(UserViewController.userViewController != null)
            UserViewController.userViewController.loadUserItems();
        stage.close();
    }

    private void createUser(UserGateway userGateway,
                            String firstName, String lastName,
                            String email, String password,
                            User.UserRole role, LocalDate bornDate) {

        if(password.isEmpty())
            return;

        User userTmp = new User(0, firstName, lastName, bornDate, email, password, role);
        userTmp.setPassword(password);
        if (userGateway.create(userTmp))
            user = userTmp;
    }

    private void updateUser(UserGateway userGateway,
                            String firstName, String lastName,
                            String email, String password,
                            User.UserRole role, LocalDate bornDate){

        User userTmp = new User(user.getUserId(), firstName, lastName, bornDate, email, password, role);
        user = userTmp;

        if(!password.isEmpty()) {
            user.setPassword(password);
            userGateway.update(user);
        }else
            userGateway.updateWithoutPassword(userTmp);
    }

    public void initAsUpdate(Stage stage, User user) {
        this.stage = stage;
        this.user = user;

        tf_FirstName.setText(user.getFirstName());
        tf_LastName.setText(user.getLastName());
        tf_Email.setText(user.getEmail());

        if(!AppSecurity.haveAuthForCreateUser())
            tf_Role.setEditable(false);
        tf_Role.setText(user.getUserRole().getName());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        tf_BornDate.setText(formatter.format(user.getBornDate()));

        tv_ActionButton.setText("Update");
    }

    public void initAsCreate(Stage stage){
        this.stage = stage;

        tv_ActionButton.setText("Create");
    }
}
