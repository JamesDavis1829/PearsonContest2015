package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.MenuItem;

public class Login extends Application{

    //public static void main(String [] args) { launch(args); }

    public void start(Stage primaryStage) throws Exception {

        DataConnection.getToken();
        Parent root = FXMLLoader.load(getClass().getResource("ClassMapDataFile.fxml"));
        primaryStage.setTitle("Class Map");
        primaryStage.setResizable(true);
        primaryStage.getIcons().add(new javafx.scene.image.Image("sample/blueIcon.jpg"));

        Dimension screenRes = Toolkit.getDefaultToolkit().getScreenSize();
        Scene scene = new Scene(root, (screenRes.width)-200, (screenRes.height)-200);

        root.getStyleClass().add("classmap-root");
        scene.getStylesheets().add("Cobra.css");

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
