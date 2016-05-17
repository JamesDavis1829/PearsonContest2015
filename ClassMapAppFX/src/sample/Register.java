package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Register
{
    public static void register(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Register");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        ///grid.setGridLinesVisible(true);

        Label first_name = new Label("First Name: ");
        GridPane.setConstraints(first_name, 0, 0);

        first_name.setId("signup-label");

        TextField first_nameInput = new TextField();
        GridPane.setConstraints(first_nameInput, 1, 0);

        Label last_name = new Label("Last Name: ");
        GridPane.setConstraints(last_name, 0, 1);

        last_name.setId("signup-label");

        TextField last_nameInput = new TextField();
        GridPane.setConstraints(last_nameInput, 1, 1);

        Label email = new Label("Email: ");
        GridPane.setConstraints(email, 0, 2);
        TextField emailInput = new TextField();
        GridPane.setConstraints(emailInput, 1, 2);

        email.setId("signup-label");

        Label username = new Label("Username: ");
        GridPane.setConstraints(username, 0, 3);
        TextField usernameInput = new TextField();
        GridPane.setConstraints(usernameInput, 1, 3);
        ToggleGroup toggle = new ToggleGroup();

        username.setId("signup-label");

        RadioButton teacher = new RadioButton("Teacher");
        RadioButton student = new RadioButton("Student");
        teacher.setToggleGroup(toggle);
        student.setToggleGroup(toggle);

        teacher.setId("signup-label");
        student.setId("signup-label");

        GridPane.setConstraints(teacher, 0, 4);
        GridPane.setConstraints(student, 1, 4);

        Label password = new Label("Password: ");
        GridPane.setConstraints(password, 0, 5);
        PasswordField passwordInput = new PasswordField();
        GridPane.setConstraints(passwordInput, 1, 5);

        password.setId("signup-label");

        Label confirmPassword = new Label("Confirm Password: ");
        GridPane.setConstraints(confirmPassword, 0, 6);
        PasswordField confirmPasswordInput = new PasswordField();
        GridPane.setConstraints(confirmPasswordInput, 1, 6);

        confirmPassword.setId("signup-label");

        Button save = new Button("Save");
        save.getStyleClass().add("login-button");
        GridPane.setConstraints(save, 0,7);
        grid.getStyleClass().add("login-root");
        Button back = new Button("Back");
        back.getStyleClass().add("login-button");
        GridPane.setConstraints(back, 0, 8);

        grid.setAlignment(Pos.CENTER);

        save.setOnAction(e -> {
            if(first_nameInput.getText().isEmpty() || last_nameInput.getText().isEmpty() || emailInput.getText().isEmpty() || usernameInput.getText().isEmpty() || (!teacher.isSelected() && !student.isSelected()) || passwordInput.getText().isEmpty() || confirmPasswordInput.getText().isEmpty()) {
                Alert.display("Error", "Fill in all blanks.");
            }
            else {
                if (passwordInput.getText().equals(confirmPasswordInput.getText())) {
                    String permission = null;
                    if (teacher.isSelected()) {
                        permission = "teacher";
                    } else {
                        permission = "student";
                    }

                    DataConnection.addMember(emailInput.getText(), usernameInput.getText(), passwordInput.getText(), first_nameInput.getText(), last_nameInput.getText(), permission);
                    Alert.display("Successful", "You are now registered. Enjoy our program.");
                    try {
                        new ClassMap().start(primaryStage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    Alert.display("Error", "Passwords do not match.");
                }
            }
        });

        confirmPasswordInput.setOnKeyPressed(e -> {
            if(e.getCode()== KeyCode.ENTER) {
                if (first_nameInput.getText().isEmpty() || last_nameInput.getText().isEmpty() || emailInput.getText().isEmpty() || usernameInput.getText().isEmpty() || (!teacher.isSelected() && !student.isSelected()) || passwordInput.getText().isEmpty() || confirmPasswordInput.getText().isEmpty()) {
                    Alert.display("Error", "Fill in all blanks.");
                } else {
                    if (passwordInput.getText().equals(confirmPasswordInput.getText())) {
                        String permission = null;
                        if (teacher.isSelected()) {
                            permission = "teacher";
                        } else {
                            permission = "student";
                        }

                        DataConnection.addMember(emailInput.getText(), usernameInput.getText(), passwordInput.getText(), first_nameInput.getText(), last_nameInput.getText(), permission);
                        Alert.display("Successful", "You are now registered. Enjoy our program.");
                        try {
                            new ClassMap().start(primaryStage);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        Alert.display("Error", "Passwords do not match.");
                    }
                }
            }
        });

        back.setOnAction(e->{
            try{
                new ClassMap().start(primaryStage);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        });

        grid.getChildren().addAll(first_name, first_nameInput, last_name, last_nameInput, email, emailInput, username, usernameInput, teacher, student, password, passwordInput, confirmPassword, confirmPasswordInput, save, back);
        Scene registerScene = new Scene(grid, 300, 325);
        registerScene.getStylesheets().add("Cobra.css");
        primaryStage.setScene(registerScene);
        primaryStage.show();
    }
}
