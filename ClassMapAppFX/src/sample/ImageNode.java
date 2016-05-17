package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.lang.System;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.nio.file.Files;
import java.io.File;

/**
 * Created by James Davis on 12/19/2015.
 */
public class ImageNode extends MapNode {
    private GridPane nodePane;
    private byte[] imgToByte;
    private String formattedDate;
    private Image image;
    private int diffX, diffY;

    public ImageNode(String in)
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        formattedDate = sdf.format(date);
        this.type = type.image;
        this.setUserVote(Boolean.TRUE);
        this.incrementVoteCounter();
        this.createdBy = DataConnection.loggedUser.getUser();
        this.nodePerm = DataConnection.loggedUser.getAccount();
        image = new Image(in);

        this.drawNode();

    }

    public ImageNode(File in)
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        formattedDate = sdf.format(date);
        this.type = type.image;
        this.setUserVote(Boolean.TRUE);
        this.incrementVoteCounter();
        this.createdBy = DataConnection.loggedUser.getUser();
        this.nodePerm = DataConnection.loggedUser.getAccount();
        this.image = new Image(in.toURI().toString());
        this.drawNode();

    }

    public ImageNode(int id, int pid, Timestamp created, int numVotes, String user, String accountType, String desc)
    {
        this.type = type.image;
        uniqueId = id;
        parent = pid;
        this.timeCreated = created;
        this.votes = numVotes;
        this.createdBy = user;
        this.nodePerm = accountType;
        this.description = desc;

    }

    public void drawNode() {
        ImageView viewer = new ImageView(image);
        viewer.setPreserveRatio(Boolean.TRUE);
        viewer.setFitHeight(80.0f);
        double height = viewer.getBoundsInParent().getHeight() + 15;
        double width = viewer.getBoundsInParent().getWidth() + 30;

        Rectangle newNode = new Rectangle(width, height);
        //if(this.nodePerm.equals("student")) {
            newNode.setFill(Paint.valueOf("white"));
            newNode.setStroke(Color.FORESTGREEN);
        //}
        //else {
          //  newNode.setFill(Paint.valueOf("black"));
            //newNode.setStroke(Paint.valueOf("black"));
        //}

        Image arrow;
        ImageView arrowView;
        if(this.getUserVote() == Boolean.TRUE)
        {
            File path = new File("./Images/arrow-up-icon_voted.png");
            arrow = new Image(path.toURI().toString());

        }
        else
        {
            File path = new File("./Images/arrow-up-icon.png");
            arrow = new Image(path.toURI().toString());
        }

        arrowView = new ImageView(arrow);
        arrowView.setPreserveRatio(Boolean.TRUE);
        arrowView.setFitHeight(20.0f);
        Text numberOfVotes = new Text(" "+votes);
        numberOfVotes.getStyleClass().add("numberOfVotes");
        //numberOfVotes.setStyle("-fx-font: 20 arial");
        //numberOfVotes.setStroke(Color.WHITE);
        //numberOfVotes.setFill(Color.WHITE);
        HBox arr = new HBox();
        arr.getChildren().addAll(arrowView,numberOfVotes);


        arr.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(getUserVote() == Boolean.TRUE)
                {
                    File path = new File("./Images/arrow-up-icon.png");
                    Image newArrow = new Image(path.toURI().toString());
                    ImageView newArrowView = new ImageView(newArrow);
                    newArrowView.setPreserveRatio(Boolean.TRUE);
                    newArrowView.setFitHeight(20.0f);
                    decrementVoteCounter();
                    Text numberOfVotes = new Text(""+(votes));
                    numberOfVotes.getStyleClass().add("numberOfVotes");
                    //numberOfVotes.setStyle("-fx-font: 20 arial");
                    //numberOfVotes.setStroke(Color.WHITE);
                    //numberOfVotes.setFill(Color.WHITE);
                    arr.getChildren().remove(0, 2);
                    arr.getChildren().addAll(newArrowView, numberOfVotes);
                    setUserVote(false);
                    try {
                        sendSelf();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    //setVisible();

                }
                else
                {
                    File path = new File("./Images/arrow-up-icon_voted.png");
                    Image newArrow = new Image(path.toURI().toString());
                    ImageView newArrowView = new ImageView(newArrow);
                    newArrowView.setPreserveRatio(Boolean.TRUE);
                    newArrowView.setFitHeight(20.0f);
                    incrementVoteCounter();
                    Text numberOfVotes = new Text(""+(votes));
                    numberOfVotes.getStyleClass().add("numberOfVotes");
                    //numberOfVotes.setStyle("-fx-font: 20 arial");
                    //numberOfVotes.setStroke(Color.WHITE);
                    //numberOfVotes.setFill(Color.WHITE);
                    arr.getChildren().remove(0, 2);
                    arr.getChildren().addAll(newArrowView, numberOfVotes);
                    setUserVote(true);
                    try {
                        sendSelf();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    //setVisible();
                }
            }
        });

        Rectangle namePlate = new Rectangle(width, 25);
        //namePlate.setFill(Color.FORESTGREEN);
        //namePlate.setStroke(Color.FORESTGREEN);
        namePlate.getStyleClass().add("imgNode");
        Text nameDisplay = new Text(this.createdBy);
        nameDisplay.setBoundsType(TextBoundsType.VISUAL);
        nameDisplay.setWrappingWidth(100.0f);
        nameDisplay.setStroke(Color.WHITE);
        StackPane stack2 = new StackPane();
        stack2.getChildren().addAll(namePlate, nameDisplay);

        StackPane stack = new StackPane();
        stack.getChildren().addAll(newNode, viewer);
//        stack.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                if(event.getButton() == MouseButton.SECONDARY)
//                    showStage();
//            }
//        });
        nodePane = new GridPane();
        nodePane.add(arr,0,0);
        nodePane.add(stack,0,1);
        if(this.uniqueId != 1)
            nodePane.add(stack2,0,2);

        nodePane.getStyleClass().add("node-box");

        nodePane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent m) {
                if(m.getButton() == MouseButton.PRIMARY) {
                    nodePane.setLayoutX(m.getSceneX() - nodePane.getWidth() / 2);
                    nodePane.setLayoutY(m.getSceneY() - nodePane.getHeight());
                }
            }
        });


        Tooltip tooltip;
        tooltip = new Tooltip("Image Node");
        Tooltip.install(nodePane,tooltip);

        nodePane.setUserData(uniqueId);

    }

    public void setTypeToImage()
    {
        this.type = classification.image;
    }

    public GridPane getNodePane()
    {
        return this.nodePane;
    }

    public String getFormattedDate() { return this.formattedDate; }

    public byte[] imageToByteArray()
    {
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, "jpg", s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s.toByteArray();

    }

    public void sendSelf() throws SQLException { DataConnection.addUpvote(this); }

    public void setImage(Image node) { this.image = node; }

    public String getAccountPerm() { return this.nodePerm; }

    public void makeVisible() {
        this.nodePane.setVisible(true);
    }

    public void setVisible() {
        this.nodePane.setVisible(false);
    }

    public void showStage(){
        Image logo = new Image("sample/OrangeIcon.png");
        Stage newStage = new Stage();
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.setTitle("Image Viewer");
        newStage.getIcons().add(logo);
        newStage.setResizable(false);
        VBox comp = new VBox();
        ImageView img = new ImageView(image);
        img.setPreserveRatio(true);
        img.setFitHeight(400.0f);
        double height = img.getBoundsInParent().getHeight();
        double width = img.getBoundsInParent().getWidth();

        comp.getChildren().add(img);

        Scene stageScene = new Scene(comp, width, height);
        newStage.setScene(stageScene);
        newStage.setResizable(false);
        newStage.show();
    }

    public void makeNode() {
        this.drawNode();
    }

    public String getDescription() { return this.description; }

    public Image getImage() { return this.image; }

}
