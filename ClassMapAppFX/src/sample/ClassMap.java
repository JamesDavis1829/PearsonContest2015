package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ClassMap extends Application {


    public Stage primaryStage;
    public static java.util.List<Double> circleX = new ArrayList<>();
    public static java.util.List<Double> circleY = new ArrayList<>();
    static int radius = 250;
    static int noOfCircle = 1;

    @Override
    public void start(Stage primaryStage) throws Exception{

        for (double i = 0; i < 6.28319; i = i + (0.628319)) {
            double x = Math.cos(i) * radius;
            double y = Math.sin(i) * radius;
            circleX.add(x);
            circleY.add(y);
        }

        Image img = new Image("sample/blueIcon.jpg");

        primaryStage.setTitle("Login");
        primaryStage.getIcons().add(img);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.getStyleClass().add("login-root");

        Image icon = new Image("sample/anime.gif");
        ImageView iv = new ImageView();
        iv.setImage(icon);
        iv.setTranslateX(10);
        iv.setFitWidth(215);
        iv.setFitHeight(190);
        iv.setPreserveRatio(true);

        Group root = new Group();
        HBox box = new HBox();
        box.getChildren().add(iv);;
        root.getChildren().add(box);

        GridPane mainPain = new GridPane();
        mainPain.add(root, 0, 0);

        GridPane userPane = new GridPane();

        javafx.scene.control.Label nameLabel = new javafx.scene.control.Label("Username");
        userPane.add(nameLabel,0,0);
        nameLabel.setStyle("-fx-padding: 10px; -fx-text-fill: rgb(0, 126, 229);");

        javafx.scene.control.TextField nameInput = new javafx.scene.control.TextField();
        nameInput.setText("tclinkscales");
        userPane.add(nameInput,1,0);

        GridPane passPane = new GridPane();
        javafx.scene.control.Label passLabel = new javafx.scene.control.Label("Password");
        userPane.add(passLabel,0,2);

        passLabel.setStyle("-fx-padding: 10px; -fx-text-fill: rgb(0, 126, 229);");

        PasswordField passInput = new PasswordField();
        passInput.setText("clinkclink");
        userPane.add(passInput,1,2);

        javafx.scene.control.Button login = new javafx.scene.control.Button("Login");
        login.getStyleClass().add("login-button");
        userPane.add(login, 0,4);

        Hyperlink signup = new Hyperlink();
        signup.setStyle("-fx-padding: 10px");
        signup.setText("Don't have an account?");
        signup.getStyleClass().add("signup");
        signup.setOnAction(e->{
            try {
                Register.register(primaryStage);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        userPane.add(signup, 1, 4);
        mainPain.add(userPane,0,1);

        grid.getChildren().addAll(mainPain);

        login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (DataConnection.login(nameInput.getText(), passInput.getText()) == true) {
                    try {
                        new Login().start(primaryStage);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    Alert.display("Error", "Incorrect username or password.");
                }
            }
        });

        passInput.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
            {
                if (DataConnection.login(nameInput.getText(), passInput.getText()) == true) {
                    try {
                        new Login().start(primaryStage);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    Alert.display("Error", "Incorrect username or password.");
                }
            }
        });

        Scene loginScene = new Scene(grid, 350, 270);
        loginScene.getStylesheets().add("Cobra.css");
        primaryStage.setResizable(false);
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    public static void calculate() {

        double add = 6.28319 / (noOfCircle * 10);
        for (double i = 0; i < 6.2831; i = i + add) {
            double x = Math.cos(i) * radius * noOfCircle;
            double y = Math.sin(i) * radius * noOfCircle;
            circleX.add(x);
            circleY.add(y);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }
}
