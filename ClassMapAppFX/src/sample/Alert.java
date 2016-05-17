package sample;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Alert {

    public static void display(String title, String message)
    {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL); //says we are blocking all
                                                         // other events until this window is handled
        window.setTitle(title);
        window.getIcons().add(new Image("sample/OrangeIcon.png"));
        Label label = new Label(message);
        label.setStyle("-fx-text-fill: rgb(0, 126, 229)");
        Button close = new Button("Close");
        close.setOnAction(e -> window.close());

        close.setOnKeyPressed(e->{
            if (e.getCode()== KeyCode.ENTER)
            {
                window.close();
            }
        });

        close.getStyleClass().add("login-button");
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, close);
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("alert-dialog");
        Scene scene = new Scene(layout, 320, 120);
        scene.getStylesheets().add("Cobra.css");
        window.setScene(scene);
        window.showAndWait(); //shows stage until closed or hidden
    }

}
