package sample;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * Created by Aaron on 1/23/2016.
 */
public class FlashCard {

    private int uniqueId;
    private Image imgContent;
    private String content;
    private String description;
    private String type;
    private String contentURL;

    public FlashCard(int id, String incomingContent, String incomingDescription, String thisType) {
        uniqueId = id;
        content = incomingContent;
        description = incomingDescription;
        type = thisType;
        contentURL = "<iframe width=\"560\" height=\"350\" src=\"" + content
                + "\" frameborder=\"0\" allowfullscreen></iframe>";

        this.drawFlashCard();
    }

    public FlashCard(MapNode incomingNode) {

        if (incomingNode.getType().equals("string")) {
            content = ((TextNode)incomingNode).getContents();
            description = ((TextNode)incomingNode).getDescription();
            type = "Text";
        }
        else if (incomingNode.getType().equals("link")) {
            content = ((VideoNode)incomingNode).getContents();
            description = ((VideoNode)incomingNode).getDescription();
            type = "Video";
        }
        else if (incomingNode.getType().equals("image")) {
            imgContent = ((ImageNode)incomingNode).getImage();
            description = ((ImageNode)incomingNode).getDescription();
            type = "Image";
        }
        else if (incomingNode.getType().equals("topic")) {
            content = ((TopicNode)incomingNode).getContents();
            description = ((TopicNode)incomingNode).getDescription();
            type = "Topic";
        }

        this.drawFlashCard();

    }

    private void drawFlashCard() {
        Stage cardWindow = new Stage();
        cardWindow.setTitle(type + " Card");
        cardWindow.getIcons().add(new Image("sample/OrangeIcon.png"));

        TextArea descriptionWindow = new TextArea(description);
        TextArea definitionWindow = new TextArea();

        GridPane masterGrid = new GridPane();
        Label headerLabel = new Label("Current Node Content: ");
        headerLabel.setId("flashHead");
//        headerLabel.setTextFill(Color.web("#303f9f"));
        masterGrid.add(headerLabel, 0 , 0);

        StackPane webPane = new StackPane();

        /*
        Setting content into masterGrid
         */
        if (type.equals("Text") || type.equals("Topic")) {
            Label contentLabel = new Label(content);
            contentLabel.setId("flashContent");
//            contentLabel.setTextFill(Color.web("#303F9F"));
//            contentLabel.setFont(new Font("Arial", 30));
            masterGrid.add(contentLabel, 0, 1);
        }
        else if (type.equals("Image")) {
            ImageView setImage = new ImageView(imgContent);
            setImage.setPreserveRatio(true);

            if(imgContent.getHeight() > 400.0f)
                setImage.setFitHeight(400.0f);
            else if (imgContent.getWidth() > 500.0f)
                setImage.setFitWidth(500.0f);

            masterGrid.add(setImage, 0, 1);
        }
        else {
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            webEngine.loadContent(contentURL);
            webView.setMaxSize(600.0f, 400.0f);
            webPane.getChildren().add(webView);
            masterGrid.add(webPane, 0, 1);
            System.out.println(contentURL);
        }

        GridPane otherContent  = new GridPane();
        Label discLabel = new Label("Description: ");
        discLabel.setId("descLabel");
        otherContent.add(discLabel, 0, 0);

        Label defLabel = new Label("Definition: ");
        defLabel.setId("defLabel");
        otherContent.add(defLabel, 1, 0);

        descriptionWindow.setMaxSize(300.0f, 200.0f);
        descriptionWindow.setMinSize(300.0f, 200.0f);
        descriptionWindow.setWrapText(true);
        otherContent.add(descriptionWindow, 0, 1);

        definitionWindow.setMaxSize(300.0f, 200.0f);
        definitionWindow.setMinSize(300.0f, 200.0f);
        if (type.equals("Text")) {
            DictParser dict = new DictParser();
            dict.searchForWord(content);
            if (dict.getCount() > 0) {
                definitionWindow.setText(dict.getExactDefinition() + "\n\n");

            } else {
                definitionWindow.setText("Not a definable word" + "\n\n");
            }
        }
        definitionWindow.setWrapText(true);

        otherContent.add(definitionWindow, 1, 1);
        masterGrid.add(otherContent, 0, 2);

        Pane backGround = new Pane();
        if(type == "Text" || type == "Topic") {
            backGround.setPrefSize(600.0f, 400.0f);
        }
        else
            backGround.setPrefSize(600.0f, 675.0f);
        backGround.setId("flash-bg");
        Pane gridBack = new Pane();
        gridBack.setPrefSize(550.0f, 650.0f);
        backGround.getChildren().add(masterGrid);

        Scene scene = new Scene(backGround);
        scene.getStylesheets().add("Cobra.css");
        cardWindow.setScene(scene);
        cardWindow.showAndWait();
    }

}

