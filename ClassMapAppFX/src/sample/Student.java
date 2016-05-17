package sample;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;

import javax.swing.*;
import javax.swing.text.Position;
import java.io.File;
import java.sql.SQLException;

/**
 * Created Aaron Martin
 */
public class Student {

    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private GridPane display;

    Student(String first, String last, String user, String mail) {
        this.firstName = first;
        this.lastName = last;
        this.userName = user;
        this.email = mail;
    }

//    public void drawNode() {
//        Text text = new Text( "   First: " + firstName + "\n" + "   Last: " + lastName + "\n" + "   UserName: " + userName + "\n" + "   Email: " + email);
//        text.setBoundsType(TextBoundsType.LOGICAL);
//        //text.setTextAlignment(TextAlignment.LEFT);
//        //text.setWrappingWidth(180.0f);
//
//        double height = (text.getLayoutBounds().getHeight())*8/9;
//        //double width = (text.getLayoutBounds().getWidth())*8/9;
//
//        Rectangle newNode = new Rectangle(0.0f, 0.0f, 200, height + 20.0f);
//        newNode.setFill(Paint.valueOf("white"));
//        newNode.setStroke(Paint.valueOf("black"));
//        StackPane stack = new StackPane();
//        stack.getChildren().addAll(newNode, text);
//        stack.setAlignment(text, Pos.CENTER_LEFT);
//
////        stack.setOnMouseClicked(new EventHandler<MouseEvent>() {
////            @Override
////            public void handle(MouseEvent event) {
////                System.out.println("Clicked" + firstName);
////            }
////        });
//
//
//        display = new GridPane();
//        display.add(stack,0,0);
//        display.setVisible(true);
//
//    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public GridPane getDisplay() {
        return display;
    }
}
