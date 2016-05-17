package sample;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;

import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.*;
import javax.swing.text.Position;


public class Controller {

    public TitledPane mainStage;
    public GridPane root;
    public Pane nodeStage;
    public Pane newNodeStage;
    public Pane newNodeStage2;
    public ScrollPane sidePane;
    public Label welcomeText;
    public Pane cardPane;

    public Pane innerPane;
    int[] array = {0, 5, 1, 6, 2, 7, 3, 8, 4, 9};
    java.util.List<MapNode> masterNode = new ArrayList<>();
    java.util.List<java.util.List<MapNode>> nodeList = new ArrayList<>();
    java.util.List<Line> lineList = new ArrayList<>();
    public ArrayList<MapNode> masterNodeList = new ArrayList<MapNode>();
    public MapNode daroot;
    public Accordion topicMenuAccordion;
    private boolean firstTimePublic = true;
    int prevPaneCordX, prevPaneCordY, prevMouseCordX, prevMouseCordY,diffX, diffY;

    TextNode classNode;
    TextNode newNode;
    ImageNode newImageNode;
    TopicNode newTopicNode;
    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;
    VideoNode videoNode;
    int randomNumber;
    double factor = 1;
    boolean nodedrag = false;
    boolean sideOpen = false;
    boolean cardOpen = false;
    int newNodes = 0;

    int index =0 ;
    int layer = 0;
    File path = new File("./Images/Drag-icon.png");
    javafx.scene.image.Image dragPicture = new javafx.scene.image.Image(path.toURI().toString());
    ImageView newDragView = new ImageView(dragPicture);

    //GridPane sideGrid = new GridPane();
    private static int nodeTrailCount = 0;

    ContextMenu cm = new ContextMenu();
    MenuItem cmItem1 = new MenuItem("Delete");
    MenuItem cmItem2 = new MenuItem("Show Trail");
    MenuItem cmItem3 = new MenuItem("Flash Card");

    /*
    Used to draw a trail of nodes from the selected node on the side Pane
     */
    public void drawNodeTrail(int nodeID) {

        System.out.println(nodeID);
    }

    /*
    recursivePrintTrail is used to recursively find the trail to the selected node
    from the drawNodeTrail method
     */
    private static boolean recursivePrintTrail(MapNode rootNode, int nodeId, GridPane sideGrid) {

        int children = rootNode.children.size();
        if (rootNode.getUniqueId() == nodeId) {
            if (rootNode.getType() == "string") {
                sideGrid.add(((TextNode)rootNode).getNodePane(), 0, nodeTrailCount);
            }
            if (rootNode.getType() == "link") {
                sideGrid.add(((VideoNode)rootNode).getNodePane(), 0, nodeTrailCount);
            }
            if (rootNode.getType() == "image") {
                sideGrid.add(((ImageNode)rootNode).getNodePane(), 0, nodeTrailCount);
            }
            if (rootNode.getType() == "topic") {
                sideGrid.add(((TopicNode)rootNode).getNodePane(), 0, nodeTrailCount);
            }
            nodeTrailCount++;
            return true;
        }
        for (int i = 0; i < children; i++) {
            if (recursivePrintTrail(rootNode.children.get(i), nodeId, sideGrid) == true) {
                if (rootNode.getType() == "string") {
                    sideGrid.add(((TextNode)rootNode).getNodePane(), 0, nodeTrailCount);
                }
                if (rootNode.getType() == "link") {
                    sideGrid.add(((VideoNode)rootNode).getNodePane(), 0, nodeTrailCount);
                }
                if (rootNode.getType() == "image") {
                    sideGrid.add(((ImageNode)rootNode).getNodePane(), 0, nodeTrailCount);
                }
                if (rootNode.getType() == "topic") {
                    sideGrid.add(((TopicNode)rootNode).getNodePane(), 0, nodeTrailCount);
                }
                nodeTrailCount++;
                return true;
            }
        }
        return false;

    }


    EventHandler<MouseEvent> onMouseRightClick = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {

            if (event.getButton() == MouseButton.SECONDARY) {
                Object obj = event.getSource();
                //drawNodeTrail((Integer)((GridPane)obj).getUserData());

                cmItem2.setStyle("-fx-text-fill: black");
                cmItem3.setStyle("-fx-text-fill: black");

                cm.hide();
                //cm.getItems().add(cmItem1);
                cm.getItems().add(cmItem2);
                cm.getItems().add(cmItem3);

                    cm.show(nodeStage,event.getSceneX(),event.getScreenY());

//                    cmItem1.setOnAction(new EventHandler<ActionEvent>() {
//                        public void handle(ActionEvent e) {
//
//                        }
//                    });
                    cmItem2.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e) {
                            drawNodeTrail((Integer)((GridPane)obj).getUserData());
                        }
                    });
                    cmItem3.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {

                            for (int i = 0; i < DataConnection.collection.size(); i++) {

                                if (DataConnection.collection.get(i).getUniqueId() == ((Integer)((GridPane)obj).getUserData())) {

                                    paintCardPane(DataConnection.collection.get(i));

                                    if(cardOpen == false) {
                                        cardOpen = true;
                                        final Animation showSidebar = new Transition() {
                                            {
                                                setCycleDuration(Duration.seconds(.6));
                                            }

                                            protected void interpolate(double frac) {
                                                if (cardPane.getTranslateX() != -cardPane.getWidth())
                                                    cardPane.setTranslateX(cardPane.getWidth() * (-frac));
                                            }
                                        };
                                        showSidebar.play();
                                        //System.out.println(showSidebar.statusProperty());
                                    }

                                }
                            }
                        }
                    });
            }
        }
    };

    private void paintCardPane(MapNode node) {
        if(cardOpen == true) {
            cardPane.getChildren().clear();
        }
        if (node.getType() == "string") {
            VBox vbox = new VBox(35);
            vbox.setPrefSize(450.0f, 200.0f);
            vbox.setMaxWidth(450.0f);
            Label contentLabel = new Label(((TextNode)node).getContents());
            contentLabel.setId("contentLabel");
            contentLabel.setMaxWidth(250.0f);
            contentLabel.setWrapText(true);

            Label descrLabel = new Label("Decription: ");
            descrLabel.setId("descrLabel");
            descrLabel.setTranslateX(-160);

            TextArea descriptionWindow = new TextArea();
            descriptionWindow.setId("descrWindow");
            descriptionWindow.setMaxSize(400.0f, 100.0f);
            descriptionWindow.setMinSize(400.0f, 100.0f);
            descriptionWindow.setWrapText(true);
            descriptionWindow.setText(node.getDescription());

            Label defLabel = new Label("Definition: ");
            defLabel.setId("defLabel");
            defLabel.setTranslateX(-160);

            TextArea definitionWindow = new TextArea();
            definitionWindow.setId("defWindow");
            definitionWindow.setMaxSize(400.0f, 100.0f);
            definitionWindow.setMinSize(400.0f, 100.0f);
            definitionWindow.setWrapText(true);
            DictParser dict = new DictParser();
            dict.searchForWord(((TextNode)node).getContents());
            if (dict.getCount() > 0) {
                definitionWindow.setText(dict.getExactDefinition() + "\n\n");

            } else {
                definitionWindow.setText("Not a definable word" + "\n\n");
            }


            HBox hbox = new HBox(25);
            hbox.setPrefSize(100.0f, 100.0f);
            Button back = new Button("Back");

            back.setTranslateY(20);
            back.setTranslateX(15);

            back.setOnAction(new EventHandler<ActionEvent>() {
                 @Override
                 public void handle(ActionEvent event) {

                     cardPane.getChildren().clear();

                     if (cardOpen == true) {

                         cardOpen = false;
                         final Animation hideSidebar = new Transition() {
                             {
                                 setCycleDuration(Duration.seconds(.6));
                             }

                             protected void interpolate(double frac) {
                                 if (cardPane.getTranslateX() != 0) {
                                     cardPane.setTranslateX(cardPane.getTranslateX() * (1-frac));
                                     System.out.println(cardPane.getTranslateX());
                                 }
                             }
                         };
                         hideSidebar.play();
                         //System.out.println(hideSidebar.statusProperty());
                     }
                 }
            });

            hbox.getChildren().add(back);

            vbox.setAlignment(Pos.CENTER);
            vbox.getChildren().addAll(contentLabel, descrLabel, descriptionWindow, defLabel, definitionWindow);

            cardPane.getChildren().addAll(vbox, hbox);
        }

        else if (node.getType() == "topic") {
            VBox vbox = new VBox(35);
            vbox.setPrefSize(450.0f, 200.0f);
            vbox.setMaxWidth(450.0f);
            Label contentLabel = new Label(((TopicNode)node).getContents());
            contentLabel.setId("contentLabel");
            contentLabel.setMaxWidth(250.0f);
            contentLabel.setWrapText(true);

            Label descrLabel = new Label("Description: ");
            descrLabel.setId("descrLabel");
            descrLabel.setTranslateX(-160);

            TextArea descriptionWindow = new TextArea();
            descriptionWindow.setId("descrWindow");
            descriptionWindow.setMaxSize(400.0f, 100.0f);
            descriptionWindow.setMinSize(400.0f, 100.0f);
            descriptionWindow.setWrapText(true);
            descriptionWindow.setText(node.getDescription());

            Label defLabel = new Label("Definition: ");
            defLabel.setId("defLabel");
            defLabel.setTranslateX(-160);

            TextArea definitionWindow = new TextArea();
            definitionWindow.setId("defWindow");
            definitionWindow.setMaxSize(400.0f, 100.0f);
            definitionWindow.setMinSize(400.0f, 100.0f);
            definitionWindow.setWrapText(true);
            DictParser dict = new DictParser();
            dict.searchForWord(((TopicNode)node).getContents());
            if (dict.getCount() > 0) {
                definitionWindow.setText(dict.getExactDefinition() + "\n\n");

            } else {
                definitionWindow.setText("Not a definable word" + "\n\n");
            }


            HBox hbox = new HBox(25);
            hbox.setPrefSize(100.0f, 100.0f);
            Button back = new Button("Back");

            back.setTranslateY(20);
            back.setTranslateX(15);

            back.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    cardPane.getChildren().clear();

                    if (cardOpen == true) {

                        cardOpen = false;
                        final Animation hideSidebar = new Transition() {
                            {
                                setCycleDuration(Duration.seconds(.6));
                            }

                            protected void interpolate(double frac) {
                                if (cardPane.getTranslateX() != 0) {
                                    cardPane.setTranslateX(cardPane.getTranslateX() * (1-frac));
                                    System.out.println(cardPane.getTranslateX());
                                }
                            }
                        };
                        hideSidebar.play();
                        //System.out.println(hideSidebar.statusProperty());
                    }
                }
            });

            hbox.getChildren().add(back);

            vbox.setAlignment(Pos.CENTER);
            vbox.getChildren().addAll(contentLabel, descrLabel, descriptionWindow, defLabel, definitionWindow);

            cardPane.getChildren().addAll(vbox, hbox);
        }

        else if (node.getType() == "image") {
            VBox vbox = new VBox(35);
            vbox.setPrefSize(450.0f, 200.0f);
            vbox.setMaxWidth(450.0f);
            //ImageView viewer = new ImageView(((ImageNode)node).getImage());
            ImageView img = new ImageView(((ImageNode)node).getImage());
            img.setPreserveRatio(true);

            if(((ImageNode)node).getImage().getWidth() > ((ImageNode)node).getImage().getHeight()){
                img.setFitWidth(400.0f);
            }
            else {
                img.setFitHeight(400.0f);
            }
            Label descrLabel = new Label("Decription: ");
            descrLabel.setId("descrLabel");
            descrLabel.setTranslateX(-160);

            TextArea descriptionWindow = new TextArea();
            descriptionWindow.setId("descrWindow");
            descriptionWindow.setMaxSize(400.0f, 100.0f);
            descriptionWindow.setMinSize(400.0f, 100.0f);
            descriptionWindow.setWrapText(true);
            descriptionWindow.setText(node.getDescription());


            HBox hbox = new HBox(25);
            hbox.setPrefSize(100.0f, 100.0f);
            Button back = new Button("Back");

            back.setTranslateY(20);
            back.setTranslateX(15);

            back.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    cardPane.getChildren().clear();

                    if (cardOpen == true) {

                        cardOpen = false;
                        final Animation hideSidebar = new Transition() {
                            {
                                setCycleDuration(Duration.seconds(.6));
                            }

                            protected void interpolate(double frac) {
                                if (cardPane.getTranslateX() != 0) {
                                    cardPane.setTranslateX(cardPane.getTranslateX() * (1-frac));
                                    System.out.println(cardPane.getTranslateX());
                                }
                            }
                        };
                        hideSidebar.play();
                        //System.out.println(hideSidebar.statusProperty());
                    }
                }
            });

            hbox.getChildren().add(back);

            vbox.setAlignment(Pos.CENTER);
            vbox.getChildren().addAll(img, descrLabel, descriptionWindow);

            cardPane.getChildren().addAll(vbox, hbox);
        }

        else if (node.getType() == "link") {
            VBox vbox = new VBox(35);
            vbox.setPrefSize(450.0f, 200.0f);
            vbox.setMaxWidth(450.0f);
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            webEngine.loadContent(((VideoNode)node).getContent_Url());
            webView.setPrefSize(430,350);
            Pane webPane = new Pane(webView);
            //System.out.println(content_Url);

            Label descrLabel = new Label("Decription: ");
            descrLabel.setId("descrLabel");
            descrLabel.setTranslateX(-160);

            TextArea descriptionWindow = new TextArea();
            descriptionWindow.setId("descrWindow");
            descriptionWindow.setMaxSize(400.0f, 100.0f);
            descriptionWindow.setMinSize(400.0f, 100.0f);
            descriptionWindow.setWrapText(true);
            descriptionWindow.setText(node.getDescription());


            HBox hbox = new HBox(25);
            hbox.setPrefSize(100.0f, 100.0f);
            Button back = new Button("Back");

            back.setTranslateY(20);
            back.setTranslateX(15);

            back.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    webView.getEngine().load(null);
                    cardPane.getChildren().clear();

                    if (cardOpen == true) {

                        cardOpen = false;
                        final Animation hideSidebar = new Transition() {
                            {
                                setCycleDuration(Duration.seconds(.6));
                            }

                            protected void interpolate(double frac) {
                                if (cardPane.getTranslateX() != 0) {
                                    cardPane.setTranslateX(cardPane.getTranslateX() * (1-frac));
                                    System.out.println(cardPane.getTranslateX());
                                }
                            }
                        };
                        hideSidebar.play();
                        //System.out.println(hideSidebar.statusProperty());
                    }
                }
            });

            hbox.getChildren().add(back);

            vbox.setAlignment(Pos.CENTER);
            vbox.getChildren().addAll(webPane, descrLabel, descriptionWindow);

            cardPane.getChildren().addAll(vbox, hbox);
        }
    }


    public void getAnalytics(ActionEvent actionEvent) {

        if (DataConnection.loggedUser.getAccount().equals("teacher")) {
            ArrayList<TextNode> collection = new ArrayList<>();
            ArrayList<TopicNode> topicscollection = new ArrayList<>();
            boolean add = false;
            for (int i =0; i<DataConnection.collection.size();i++){
                if(DataConnection.collection.get(i).getType()=="string"){
                    TextNode textNode = (TextNode) DataConnection.collection.get(i);
                    if(collection.size()==0){
                        collection.add(textNode);
                        add = true;
                    }
                    else{
                        add = false;
                        for (int j =0; j< collection.size();j++){
                            if(textNode.getVotes()>collection.get(j).getVotes()){
                                collection.add(j,textNode);
                                add = true;
                                break;
                            }
                        }
                    }
                    if(!add){
                        collection.add(textNode);
                    }
                }
            }

            for (int i =0; i<DataConnection.collection.size();i++){
                if(DataConnection.collection.get(i).getType()=="topic"){
                    TopicNode topicNode = (TopicNode) DataConnection.collection.get(i);
                    if(topicscollection.size()==0){
                        topicscollection.add(topicNode);
                        add = true;
                    }
                    else{
                        add = false;
                        for (int j =0; j< topicscollection.size();j++){
                            if(topicNode.getNoOfChildren()>topicscollection.get(j).getNoOfChildren()){
                                topicscollection.add(j,topicNode);
                                add = true;
                                break;
                            }
                        }
                    }
                    if(!add){
                        topicscollection.add(topicNode);
                    }
                }
            }

            try
            {
                PrintWriter out = new PrintWriter("Node Analytics-Spring 2016.txt");
                out.printf("%-40s","Node");
                out.print("\t\t\t");
                out.printf("%-20s","No. Of Votes");
                out.println();
                out.println();
                for (int i =0; i<collection.size();i++){
                        String str = collection.get(i).getContents();
                        str = str.replaceAll("\n"," ");
                        str = str.replaceAll("\r","");
                        out.printf("%-40s",str);
                        out.print("\t\t\t");
                        out.printf("%-20s",collection.get(i).getVotes());
                        out.println();
                }

                out.println();
                out.printf("%-40s","Topic Nodes : ");
                out.print("\t\t\t");
                out.printf("%-20s","No. Of Children");
                out.println();
                out.println();
                for (int i =0; i<topicscollection.size();i++){
                    String str = topicscollection.get(i).getContents();
                    str = str.replaceAll("\n"," ");
                    str = str.replaceAll("\r","");
                    out.printf("%-40s",str);
                    out.print("\t\t\t");
                    out.printf("%-20s",topicscollection.get(i).getNoOfChildren());
                    out.println();
                }

                out.close();
            }
            catch ( IOException e)
            {

            }
            try
            {
                PrintWriter out = new PrintWriter("Student Analytics-Spring 2016.txt");
                for(int i =0; i< DataConnection.students.size();i++){
                    out.println(DataConnection.students.get(i).getFirstName()+" "+DataConnection.students.get(i).getLastName());
                    out.println();
                    DataConnection.queryUserNodes(DataConnection.students.get(i).getUserName(), out);
                }
                out.close();
            }
            catch ( IOException e)
            {

            }
        }
    }
    public void drawTeacherPanel(ActionEvent actionEvent) {
        if (DataConnection.loggedUser.getAccount().equals("teacher")) {
            if(sideOpen == false) {

                sideOpen = true;

                GridPane displayGrid = new GridPane();
                displayGrid.setMaxWidth(200);
                int numStudents = DataConnection.students.size();
                for (int i = 0; i < numStudents; i++) {

                    Text text = new Text("   First: " + DataConnection.students.get(i).getFirstName() + "\n" + "   Last: " + DataConnection.students.get(i).getLastName() + "\n" + "   UserName: " + DataConnection.students.get(i).getUserName() + "\n" + "   Email: " + DataConnection.students.get(i).getEmail());
                    text.setBoundsType(TextBoundsType.LOGICAL);
                    //text.setTextAlignment(TextAlignment.LEFT);
                    //text.setWrappingWidth(180.0f);

                    double height = (text.getLayoutBounds().getHeight()) * 8 / 9;
                    //double width = (text.getLayoutBounds().getWidth())*8/9;

                    javafx.scene.shape.Rectangle newNode = new javafx.scene.shape.Rectangle(0.0f, 0.0f, 200, height + 20.0f);
                    newNode.setFill(Paint.valueOf("white"));
                    newNode.setStroke(Paint.valueOf("black"));
                    StackPane stack = new StackPane();
                    stack.getChildren().addAll(newNode, text);
                    stack.setAlignment(text, Pos.CENTER_LEFT);

                    int j = i;
                    stack.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            showReset(event);
                            showUserNodes(event, DataConnection.students.get(j).getUserName());
                        }
                    });

                    DataConnection.displayPanes.add(new GridPane());
                    DataConnection.displayPanes.get(i).add(stack, 0, 0);
                    DataConnection.displayPanes.get(i).setVisible(true);

                    displayGrid.add(DataConnection.displayPanes.get(i), 0, i);


                }     //displayGrid.setStyle("-fx-background-color: #000000;");
                sidePane.setContent(displayGrid);
                sidePane.setVisible(true);
            }
            else {
                sidePane.setContent(null);
                sidePane.setVisible(false);
                sideOpen = false;
            }
        }
    }

    public void showNew(ActionEvent actionEvent) {
        if(!firstTimePublic) {
            showHome(actionEvent);
            recursiveShowNew(daroot, DataConnection.loggedUser.getSQLLog());
        }
    }

    public boolean recursiveShowNew(MapNode rootNode, java.sql.Timestamp sqlLog) {

        if(rootNode.getTimeCreated()==null) {
            return true;
        }
        rootNode.previousVote = false;
        int children = rootNode.children.size();

        if (children == 0) {
            rootNode.previousVote = (rootNode.timeCreated.after(sqlLog));
            if (rootNode.previousVote == false) {
                rootNode.getParentLine().setVisible(false);
                if (rootNode.type.toString().equals("string"))
                    ((TextNode) (rootNode)).setVisible();

                if (rootNode.type.toString().equals("image"))
                    ((ImageNode) (rootNode)).setVisible();

                if (rootNode.type.toString().equals("link"))
                    ((VideoNode) (rootNode)).setVisible();

                if (rootNode.type.toString().equals("topic"))
                    ((TopicNode) (rootNode)).setVisible();
            }
        }
        else {
            for (int i = 0; i < children; i++) {
                if(rootNode.previousVote == false)
                    rootNode.previousVote = recursiveShowNew(rootNode.children.get(i), sqlLog);
                else
                    recursiveShowNew(rootNode.children.get(i), sqlLog);
            }
        }

        if (rootNode.getTimeCreated().after(sqlLog)) {
            rootNode.previousVote = true;
        }
        else {
            if(rootNode.uniqueId == 1){

            }
            else {
                if (rootNode.getTimeCreated().after(sqlLog) == false && rootNode.previousVote == false) {
                    rootNode.getParentLine().setVisible(false);
                    if (rootNode.type.toString().equals("string"))
                        ((TextNode) (rootNode)).setVisible();

                    if (rootNode.type.toString().equals("image"))
                        ((ImageNode) (rootNode)).setVisible();

                    if (rootNode.type.toString().equals("link"))
                        ((VideoNode) (rootNode)).setVisible();

                    if (rootNode.type.toString().equals("topic"))
                        ((TopicNode) (rootNode)).setVisible();
                }
            }
        }

        return rootNode.previousVote;
    }


    private class ZoomHandler implements EventHandler<ScrollEvent> {

        private Node nodeToZoom;

        private ZoomHandler(Node nodeToZoom) {
            this.nodeToZoom = nodeToZoom;
        }

        @Override
        public void handle(ScrollEvent scrollEvent) {
            //if (scrollEvent.isControlDown()) {
                    if(factor<=0.45)
                        newDragView.setVisible(false);
                    else
                        newDragView.setVisible(true);
                    final double scale = calculateScale(scrollEvent);
                    nodeToZoom.setScaleX(scale);
                    nodeToZoom.setScaleY(scale);
                    scrollEvent.consume();
            //}
        }

        private double calculateScale(ScrollEvent scrollEvent) {
            double scale = nodeToZoom.getScaleX() + scrollEvent.getDeltaY() / 400;
            factor = scale;
            if(scale<=0.1 ) {
                scale = 0.1;
            }
            if(scale>1)
                scale = 1;
            return scale;
        }
    }

    @FXML
    protected void initialize() {

        nodeStage.addEventFilter(ScrollEvent.ANY, new ZoomHandler(newNodeStage));
        nodeStage.addEventFilter(ScrollEvent.ANY, new ZoomHandler(newNodeStage2));
        newNodeStage.setOnMouseClicked(doNothing);
        //DataConnection.delete();
    }


    public void createTextNode(ActionEvent actionEvent){

        if(!firstTimePublic) {

            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Create Text Node");
            dialog.setHeaderText("Enter the text below.");

            ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField text = new TextField();
            text.setPromptText("Enter Text");
            TextArea description = new TextArea();
            description.setWrapText(true);
            description.setMaxHeight(100);
            description.setPromptText("Enter Description");

            grid.add(new Label("Text : "), 0, 0);
            grid.add(text, 1, 0);
            grid.add(new Label("Description : "), 0, 1);
            grid.add(description, 1, 1);

            Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
            okButton.setDisable(true);

            text.textProperty().addListener((observable, oldValue, newValue) -> {
                okButton.setDisable(newValue.trim().isEmpty());
            });

            dialog.getDialogPane().setContent(grid);

            Platform.runLater(() -> text.requestFocus());

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButtonType) {
                    return new Pair<>(text.getText(), description.getText());
                }
                return null;
            });

            Optional<Pair<String, String>> result = dialog.showAndWait();

            result.ifPresent(textDescription -> {
                newNode = new TextNode(textDescription.getKey());
                newNode.setDescription(textDescription.getValue());
            });

            newNode.setTypeToText();
            //masterNodeList.add(newNode); I think this is still necessary but not for the database
            newNode.getNodePane().setOnMousePressed(OnMousePressedEventHandler);
            newNode.getNodePane().setOnMouseDragged(OnMouseDraggedEventHandler);
            newNode.getNodePane().setOnMouseReleased(OnMouseReleasedEventHandler);
            //newNode.getNodePane().setOnMouseClicked(onMouseRightClick);
            newNodeStage.getChildren().add(newNode.getNodePane());
        }

    }

    public void createTopicNode(ActionEvent actionEvent){

        if(!firstTimePublic) {
            if(DataConnection.loggedUser.getAccount().equals("teacher")) {

                Dialog<Pair<String, String>> dialog = new Dialog<>();
                dialog.setTitle("Create Topic Node");
                dialog.setHeaderText("Enter the text below.");

                ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField text = new TextField();
                text.setPromptText("Enter Text");
                TextArea description = new TextArea();
                description.setWrapText(true);
                description.setMaxHeight(100);
                description.setPromptText("Enter Description");

                grid.add(new Label("Text : "), 0, 0);
                grid.add(text, 1, 0);
                grid.add(new Label("Description : "), 0, 1);
                grid.add(description, 1, 1);

                Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
                okButton.setDisable(true);

                text.textProperty().addListener((observable, oldValue, newValue) -> {
                    okButton.setDisable(newValue.trim().isEmpty());
                });

                dialog.getDialogPane().setContent(grid);

                Platform.runLater(() -> text.requestFocus());

                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == okButtonType) {
                        return new Pair<>(text.getText(), description.getText());
                    }
                    return null;
                });

                Optional<Pair<String, String>> result = dialog.showAndWait();

                result.ifPresent(textDescription -> {
                    newTopicNode = new TopicNode(textDescription.getKey());
                    newTopicNode.setDescription(textDescription.getValue());
                });

                newTopicNode.setTypeToTopic();
                //masterNodeList.add(newNode); I think this is still necessary but not for the database
                newTopicNode.getNodePane().setOnMousePressed(OnMousePressedEventHandler);
                newTopicNode.getNodePane().setOnMouseDragged(OnMouseDraggedEventHandler);
                newTopicNode.getNodePane().setOnMouseReleased(TopicOnMouseReleasedEventHandler);
                //newNode.getNodePane().setOnMouseClicked(onMouseRightClick);
                newNodeStage.getChildren().add(newTopicNode.getNodePane());
            }
        }

    }

    public void printMasterList()
    {
        int size = masterNodeList.size();

        for(int x = 0; x < size; x++)
        {
            System.out.println(masterNodeList.get(x).getSeconds());
        }
    }


    public void createImageNodeURL(ActionEvent actionEvent) {
        if(!firstTimePublic) {

            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Create Image Node from URL");
            dialog.setHeaderText("Enter the URL below.");

            ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField text = new TextField();
            text.setPromptText("Enter Image URL");
            TextArea description = new TextArea();
            description.setWrapText(true);
            description.setMaxHeight(100);
            description.setPromptText("Enter Description");

            grid.add(new Label("URL: "), 0, 0);
            grid.add(text, 1, 0);
            grid.add(new Label("Description : "), 0, 1);
            grid.add(description, 1, 1);

            Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
            okButton.setDisable(true);

            text.textProperty().addListener((observable, oldValue, newValue) -> {
                okButton.setDisable(newValue.trim().isEmpty());
            });

            dialog.getDialogPane().setContent(grid);

            Platform.runLater(() -> text.requestFocus());

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButtonType) {
                    return new Pair<>(text.getText(), description.getText());
                }
                return null;
            });

            Optional<Pair<String, String>> result = dialog.showAndWait();

            result.ifPresent(imageDescription -> {
                newImageNode = new ImageNode(imageDescription.getKey());
                newImageNode.setDescription(imageDescription.getValue());
            });


            newImageNode.setTypeToImage();
            //masterNodeList.add(newNode); I think this is still necessary but not for the database

            newImageNode.getNodePane().setOnMousePressed(OnMousePressedEventHandler);
            newImageNode.getNodePane().setOnMouseDragged(OnMouseDraggedEventHandler);
            newImageNode.getNodePane().setOnMouseReleased(ImageOnMouseReleasedEventHandler);
            newNodeStage.getChildren().add(newImageNode.getNodePane());
        }
    }

    public void createImageNodeFile(ActionEvent actionEvent) throws FileNotFoundException {
        if(!firstTimePublic) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open an image file");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp"));
            File openedFile = fileChooser.showOpenDialog(null);

            newImageNode = new ImageNode(openedFile);
            newImageNode.setTypeToImage();

            TextInputDialog dialog = new TextInputDialog("Enter description for the image");
            dialog.setHeaderText("Enter the description below.");
            dialog.setContentText("Description: ");
            Optional<String> result = dialog.showAndWait();
            newImageNode.setDescription(result.get());

            //masterNodeList.add(newNode); I think this is still necessary but not for the database

            newImageNode.getNodePane().setOnMousePressed(OnMousePressedEventHandler);
            newImageNode.getNodePane().setOnMouseDragged(OnMouseDraggedEventHandler);
            newImageNode.getNodePane().setOnMouseReleased(ImageOnMouseReleasedEventHandler);
            newNodeStage.getChildren().add(newImageNode.getNodePane());
        }
    }

    public void drawWorld(ActionEvent actionEvent) throws InterruptedException {
        if(firstTimePublic) {

            daroot = DataConnection.populate();
            DataConnection.getStudents();
            recursiveDisplay(daroot);
            firstTimePublic = false;
            populateList();
            nodeStage.getChildren().add(newDragView);

            int noNodes = DataConnection.collection.size();

            for(int i = 0; i < noNodes; i++) {
                if (DataConnection.collection.get(i).timeCreated.after(DataConnection.loggedUser.getSQLLog())) {
                    ++newNodes;
                }
            }
            welcomeText.setText("Welcome " + DataConnection.loggedUser.getFirst() + " " + DataConnection.loggedUser.getLast() + "! "
                    + newNodes + " New Nodes have been added!");
        }

    }

    public void showHome(ActionEvent actionEvent){
        if(!firstTimePublic) {
            recursiveShow(daroot);
            //newNodeStage2.setVisible(true);
        }
    }

    public void showReset(MouseEvent actionEvent){
        if(!firstTimePublic) {
            recursiveShow(daroot);
            //newNodeStage2.setVisible(true);
        }
    }

    EventHandler<MouseEvent> doNothing =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    if(t.getButton()== MouseButton.SECONDARY){

                    }
                    else {
                        cm.hide();
                    }
                }
            };

    private void recursiveDisplay(MapNode rootNode) {

        int children = rootNode.children.size();

        if (rootNode.uniqueId == 1 )
        {
            rootNode.setLayer(0);
            newNodeStage.getChildren().add(((TextNode)(rootNode)).getNodePane());
            ((TextNode)(rootNode)).getNodePane().setTranslateX(410);
            ((TextNode)(rootNode)).getNodePane().setTranslateY(225);
            nodeList.add(masterNode);
            ((TextNode) rootNode).getNodePane().setOnMousePressed(doNothing);
            ((TextNode) rootNode).getNodePane().setOnMouseDragged(doNothing);
            ((TextNode) rootNode).getNodePane().setOnMouseReleased(doNothing);
            classNode = (TextNode) rootNode;

        }

        else if (rootNode.getLayer() == 1) {

            if (rootNode.getParentNode().getNoOfChildren() == ClassMap.noOfCircle*10){
                expand();

            }

            if (ClassMap.circleX.size() % 2 == 0) {
                randomNumber = 0;

            } else randomNumber = ClassMap.circleX.size() / 2;

            double newTranslateX = ClassMap.circleX.get(randomNumber);
            double newTranslateY = ClassMap.circleY.get(randomNumber);
            ClassMap.circleX.remove(randomNumber);
            ClassMap.circleY.remove(randomNumber);

            if(rootNode.type.toString().equals("string")){
                newNodeStage.getChildren().add(((TextNode)(rootNode)).getNodePane());
                ((TextNode)(rootNode)).getNodePane().setTranslateX(newTranslateX+410);
                ((TextNode)(rootNode)).getNodePane().setTranslateY(newTranslateY+225);
                ((TextNode)rootNode).getNodePane().setOnMouseClicked(onMouseRightClick);

                if(rootNode.getParentNode().getType().toString().equals("string")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    TextNode textNode =(TextNode) rootNode.getParentNode();

                    line.startXProperty().bind(textNode.getNodePane().layoutXProperty().add((textNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(textNode.getNodePane().layoutYProperty().add(textNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((TextNode)(rootNode)).getNodePane().layoutXProperty().add(((TextNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((TextNode)(rootNode)).getNodePane().layoutXProperty().add(((TextNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                if(rootNode.getParentNode().getType().toString().equals("image")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    ImageNode imageNode =(ImageNode) rootNode.getParentNode();

                    line.startXProperty().bind(imageNode.getNodePane().layoutXProperty().add((imageNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(imageNode.getNodePane().layoutYProperty().add(imageNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((TextNode)(rootNode)).getNodePane().layoutXProperty().add(((TextNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((TextNode)(rootNode)).getNodePane().layoutXProperty().add(((TextNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                if(rootNode.getParentNode().getType().toString().equals("link")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    VideoNode videoNode =(VideoNode) rootNode.getParentNode();

                    line.startXProperty().bind(videoNode.getNodePane().layoutXProperty().add((videoNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(videoNode.getNodePane().layoutYProperty().add(videoNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((TextNode)(rootNode)).getNodePane().layoutXProperty().add(((TextNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((TextNode)(rootNode)).getNodePane().layoutXProperty().add(((TextNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                if(rootNode.getParentNode().getType().toString().equals("topic")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    TopicNode topicNode =(TopicNode) rootNode.getParentNode();

                    line.startXProperty().bind(topicNode.getNodePane().layoutXProperty().add((topicNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(topicNode.getNodePane().layoutYProperty().add(topicNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((TextNode)(rootNode)).getNodePane().layoutXProperty().add(((TextNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((TextNode)(rootNode)).getNodePane().layoutXProperty().add(((TextNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                ((TextNode) rootNode).getNodePane().setOnMousePressed(doNothing);
                ((TextNode) rootNode).getNodePane().setOnMouseDragged(doNothing);
                ((TextNode) rootNode).getNodePane().setOnMouseReleased(doNothing);
                rootNode.setA(newTranslateX+410);
                rootNode.setB(newTranslateY+225);

            }

            //System.out.println(rootNode.uniqueId);


            if(rootNode.type.toString().equals("image")){
                newNodeStage.getChildren().add(((ImageNode)(rootNode)).getNodePane());
                ((ImageNode)(rootNode)).getNodePane().setTranslateX(newTranslateX+410);
                ((ImageNode)(rootNode)).getNodePane().setTranslateY(newTranslateY+225);
                ((ImageNode)rootNode).getNodePane().setOnMouseClicked(onMouseRightClick);

                if(rootNode.getParentNode().getType().toString().equals("string")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    TextNode textNode =(TextNode) rootNode.getParentNode();

                    line.startXProperty().bind(textNode.getNodePane().layoutXProperty().add((textNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(textNode.getNodePane().layoutYProperty().add(textNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((ImageNode)(rootNode)).getNodePane().layoutXProperty().add(((ImageNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((ImageNode)(rootNode)).getNodePane().layoutXProperty().add(((ImageNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                if(rootNode.getParentNode().getType().toString().equals("image")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    ImageNode imageNode =(ImageNode) rootNode.getParentNode();

                    line.startXProperty().bind(imageNode.getNodePane().layoutXProperty().add((imageNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(imageNode.getNodePane().layoutYProperty().add(imageNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((ImageNode)(rootNode)).getNodePane().layoutXProperty().add(((ImageNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((ImageNode)(rootNode)).getNodePane().layoutXProperty().add(((ImageNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                if(rootNode.getParentNode().getType().toString().equals("link")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    VideoNode videoNode =(VideoNode) rootNode.getParentNode();

                    line.startXProperty().bind(videoNode.getNodePane().layoutXProperty().add((videoNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(videoNode.getNodePane().layoutYProperty().add(videoNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((ImageNode)(rootNode)).getNodePane().layoutXProperty().add(((ImageNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((ImageNode)(rootNode)).getNodePane().layoutXProperty().add(((ImageNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                if(rootNode.getParentNode().getType().toString().equals("topic")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    TopicNode topicNode =(TopicNode) rootNode.getParentNode();

                    line.startXProperty().bind(topicNode.getNodePane().layoutXProperty().add((topicNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(topicNode.getNodePane().layoutYProperty().add(topicNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((ImageNode)(rootNode)).getNodePane().layoutXProperty().add(((ImageNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((ImageNode)(rootNode)).getNodePane().layoutXProperty().add(((ImageNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }

                ((ImageNode) rootNode).getNodePane().setOnMousePressed(doNothing);
                ((ImageNode) rootNode).getNodePane().setOnMouseDragged(doNothing);
                ((ImageNode) rootNode).getNodePane().setOnMouseReleased(doNothing);
                rootNode.setA(newTranslateX+410);
                rootNode.setB(newTranslateY+225);

            }

            if(rootNode.type.toString().equals("link")) {
                newNodeStage.getChildren().add(((VideoNode) (rootNode)).getNodePane());
                ((VideoNode)(rootNode)).getNodePane().setTranslateX(newTranslateX+410);
                ((VideoNode)(rootNode)).getNodePane().setTranslateY(newTranslateY+225);
                ((VideoNode)rootNode).getNodePane().setOnMouseClicked(onMouseRightClick);

                if(rootNode.getParentNode().getType().toString().equals("string")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    TextNode textNode =(TextNode) rootNode.getParentNode();

                    line.startXProperty().bind(textNode.getNodePane().layoutXProperty().add((textNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(textNode.getNodePane().layoutYProperty().add(textNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((VideoNode)(rootNode)).getNodePane().layoutXProperty().add(((VideoNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((VideoNode)(rootNode)).getNodePane().layoutXProperty().add(((VideoNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                if(rootNode.getParentNode().getType().toString().equals("image")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    ImageNode imageNode =(ImageNode) rootNode.getParentNode();

                    line.startXProperty().bind(imageNode.getNodePane().layoutXProperty().add((imageNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(imageNode.getNodePane().layoutYProperty().add(imageNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((VideoNode)(rootNode)).getNodePane().layoutXProperty().add(((VideoNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((VideoNode)(rootNode)).getNodePane().layoutXProperty().add(((VideoNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                if(rootNode.getParentNode().getType().toString().equals("link")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    VideoNode videoNode =(VideoNode) rootNode.getParentNode();

                    line.startXProperty().bind(videoNode.getNodePane().layoutXProperty().add((videoNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(videoNode.getNodePane().layoutYProperty().add(videoNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((VideoNode)(rootNode)).getNodePane().layoutXProperty().add(((VideoNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((VideoNode)(rootNode)).getNodePane().layoutXProperty().add(((VideoNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                if(rootNode.getParentNode().getType().toString().equals("topic")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    TopicNode topicNode =(TopicNode) rootNode.getParentNode();

                    line.startXProperty().bind(topicNode.getNodePane().layoutXProperty().add((topicNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(topicNode.getNodePane().layoutYProperty().add(topicNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((VideoNode)(rootNode)).getNodePane().layoutXProperty().add(((VideoNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((VideoNode)(rootNode)).getNodePane().layoutXProperty().add(((VideoNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }

                ((VideoNode) rootNode).getNodePane().setOnMousePressed(doNothing);
                ((VideoNode) rootNode).getNodePane().setOnMouseDragged(doNothing);
                ((VideoNode) rootNode).getNodePane().setOnMouseReleased(doNothing);
                rootNode.setA(newTranslateX+410);
                rootNode.setB(newTranslateY+225);
            }

            if(rootNode.type.toString().equals("topic")){
                newNodeStage.getChildren().add(((TopicNode)(rootNode)).getNodePane());
                ((TopicNode)(rootNode)).getNodePane().setTranslateX(newTranslateX+410);
                ((TopicNode)(rootNode)).getNodePane().setTranslateY(newTranslateY+225);
                ((TopicNode)rootNode).getNodePane().setOnMouseClicked(onMouseRightClick);

                if(rootNode.getParentNode().getType().toString().equals("string")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    TextNode textNode =(TextNode) rootNode.getParentNode();

                    line.startXProperty().bind(textNode.getNodePane().layoutXProperty().add((textNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(textNode.getNodePane().layoutYProperty().add(textNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((TopicNode)(rootNode)).getNodePane().layoutXProperty().add(((TopicNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((TopicNode)(rootNode)).getNodePane().layoutXProperty().add(((TopicNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                if(rootNode.getParentNode().getType().toString().equals("image")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    ImageNode imageNode =(ImageNode) rootNode.getParentNode();

                    line.startXProperty().bind(imageNode.getNodePane().layoutXProperty().add((imageNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(imageNode.getNodePane().layoutYProperty().add(imageNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((TopicNode)(rootNode)).getNodePane().layoutXProperty().add(((TopicNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((TopicNode)(rootNode)).getNodePane().layoutXProperty().add(((TopicNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                if(rootNode.getParentNode().getType().toString().equals("link")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    VideoNode videoNode =(VideoNode) rootNode.getParentNode();

                    line.startXProperty().bind(videoNode.getNodePane().layoutXProperty().add((videoNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(videoNode.getNodePane().layoutYProperty().add(videoNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((TopicNode)(rootNode)).getNodePane().layoutXProperty().add(((TopicNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((TopicNode)(rootNode)).getNodePane().layoutXProperty().add(((TopicNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                if(rootNode.getParentNode().getType().toString().equals("topic")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    TopicNode topicNode =(TopicNode) rootNode.getParentNode();

                    line.startXProperty().bind(topicNode.getNodePane().layoutXProperty().add((topicNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(topicNode.getNodePane().layoutYProperty().add(topicNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((TopicNode)(rootNode)).getNodePane().layoutXProperty().add(((TopicNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((TopicNode)(rootNode)).getNodePane().layoutXProperty().add(((TopicNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                ((TopicNode) rootNode).getNodePane().setOnMousePressed(doNothing);
                ((TopicNode) rootNode).getNodePane().setOnMouseDragged(doNothing);
                ((TopicNode) rootNode).getNodePane().setOnMouseReleased(doNothing);
                rootNode.setA(newTranslateX+410);
                rootNode.setB(newTranslateY+225);

            }

            rootNode.setQuadrant(array[masterNode.size()%10] + 1);
            rootNode.setCircleNo(ClassMap.noOfCircle);
            rootNode.setExpansion(2*rootNode.getParentNode().getExpansionconst());
            rootNode.setChildLimit(2*rootNode.getParentNode().getExpansionconst());
            if (masterNode.size() >= 10){
                rootNode.setOffset(1);
            }
            masterNode.add(rootNode);
            rootNode.getParentNode().setNoOfChildren(rootNode.getParentNode().getNoOfChildren()+1);

        }

        else {
            double newTranslateX;
            double newTranslateY;

            if (rootNode.getParentNode().getNoOfChildren()== rootNode.getParentNode().getChildLimit()){
                expandChildren(rootNode.getParentNode());
            }
            List<Double> X = newCalculateX(rootNode.getParentNode().getCircleNo()*rootNode.getParentNode().getExpansion());
            List<Double> Y = newCalculateY(rootNode.getParentNode().getCircleNo()*rootNode.getParentNode().getExpansion());

            if(rootNode.getParentNode().getExpansion()>3) {

                newTranslateX = X.get(((rootNode.getQuadrant() - 1) * rootNode.getParentNode().getCircleNo() * rootNode.getParentNode().getExpansion()) + rootNode.getParentNode().getNoOfChildren() + (rootNode.getParentNode().getOffset() * rootNode.getParentNode().getExpansion()) + (rootNode.getParentNode().getChildno() * (rootNode.getParentNode().getExpansion()/2) ));
                newTranslateY = Y.get(((rootNode.getQuadrant() - 1) * rootNode.getParentNode().getCircleNo() * rootNode.getParentNode().getExpansion()) + rootNode.getParentNode().getNoOfChildren() + (rootNode.getParentNode().getOffset() * rootNode.getParentNode().getExpansion()) + (rootNode.getParentNode().getChildno() * (rootNode.getParentNode().getExpansion()/2) ));
            }

            else {

                newTranslateX = X.get(((rootNode.getQuadrant() - 1) * rootNode.getParentNode().getCircleNo() * rootNode.getParentNode().getExpansion()) + rootNode.getParentNode().getNoOfChildren() + (rootNode.getParentNode().getOffset() * rootNode.getParentNode().getExpansion()) + (rootNode.getParentNode().getChildno()));
                newTranslateY = Y.get(((rootNode.getQuadrant() - 1) * rootNode.getParentNode().getCircleNo() * rootNode.getParentNode().getExpansion()) + rootNode.getParentNode().getNoOfChildren() + (rootNode.getParentNode().getOffset() * rootNode.getParentNode().getExpansion()) + (rootNode.getParentNode().getChildno()));
            }
            if(rootNode.type.toString().equals("string")){
                newNodeStage.getChildren().add(((TextNode)(rootNode)).getNodePane());
                ((TextNode)(rootNode)).getNodePane().setTranslateX(newTranslateX+410);
                ((TextNode)(rootNode)).getNodePane().setTranslateY(newTranslateY+225);
                ((TextNode)rootNode).getNodePane().setOnMouseClicked(onMouseRightClick);

                if(rootNode.getParentNode().getType().toString().equals("string")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    TextNode textNode =(TextNode) rootNode.getParentNode();

                    line.startXProperty().bind(textNode.getNodePane().layoutXProperty().add((textNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(textNode.getNodePane().layoutYProperty().add(textNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((TextNode)(rootNode)).getNodePane().layoutXProperty().add(((TextNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((TextNode)(rootNode)).getNodePane().layoutXProperty().add(((TextNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                if(rootNode.getParentNode().getType().toString().equals("image")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    ImageNode imageNode =(ImageNode) rootNode.getParentNode();

                    line.startXProperty().bind(imageNode.getNodePane().layoutXProperty().add((imageNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(imageNode.getNodePane().layoutYProperty().add(imageNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((TextNode)(rootNode)).getNodePane().layoutXProperty().add(((TextNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((TextNode)(rootNode)).getNodePane().layoutXProperty().add(((TextNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                if(rootNode.getParentNode().getType().toString().equals("link")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    VideoNode videoNode =(VideoNode) rootNode.getParentNode();

                    line.startXProperty().bind(videoNode.getNodePane().layoutXProperty().add((videoNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(videoNode.getNodePane().layoutYProperty().add(videoNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((TextNode)(rootNode)).getNodePane().layoutXProperty().add(((TextNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((TextNode)(rootNode)).getNodePane().layoutXProperty().add(((TextNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                if(rootNode.getParentNode().getType().toString().equals("topic")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    TopicNode topicNode =(TopicNode) rootNode.getParentNode();

                    line.startXProperty().bind(topicNode.getNodePane().layoutXProperty().add((topicNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(topicNode.getNodePane().layoutYProperty().add(topicNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((TextNode)(rootNode)).getNodePane().layoutXProperty().add(((TextNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((TextNode)(rootNode)).getNodePane().layoutXProperty().add(((TextNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                ((TextNode) rootNode).getNodePane().setOnMousePressed(doNothing);
                ((TextNode) rootNode).getNodePane().setOnMouseDragged(doNothing);
                ((TextNode) rootNode).getNodePane().setOnMouseReleased(doNothing);
                rootNode.setA(newTranslateX+410);
                rootNode.setB(newTranslateY+225);
            }
            // System.out.println(rootNode.uniqueId);


            if(rootNode.type.toString().equals("image")){
                newNodeStage.getChildren().add(((ImageNode)(rootNode)).getNodePane());
                ((ImageNode)(rootNode)).getNodePane().setTranslateX(newTranslateX+410);
                ((ImageNode)(rootNode)).getNodePane().setTranslateY(newTranslateY+225);
                ((ImageNode)rootNode).getNodePane().setOnMouseClicked(onMouseRightClick);

                if(rootNode.getParentNode().getType().toString().equals("string")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    TextNode textNode =(TextNode) rootNode.getParentNode();

                    line.startXProperty().bind(textNode.getNodePane().layoutXProperty().add((textNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(textNode.getNodePane().layoutYProperty().add(textNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((ImageNode)(rootNode)).getNodePane().layoutXProperty().add(((ImageNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((ImageNode)(rootNode)).getNodePane().layoutXProperty().add(((ImageNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                if(rootNode.getParentNode().getType().toString().equals("image")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    ImageNode imageNode =(ImageNode) rootNode.getParentNode();

                    line.startXProperty().bind(imageNode.getNodePane().layoutXProperty().add((imageNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(imageNode.getNodePane().layoutYProperty().add(imageNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((ImageNode)(rootNode)).getNodePane().layoutXProperty().add(((ImageNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((ImageNode)(rootNode)).getNodePane().layoutXProperty().add(((ImageNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                if(rootNode.getParentNode().getType().toString().equals("link")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    VideoNode videoNode =(VideoNode) rootNode.getParentNode();

                    line.startXProperty().bind(videoNode.getNodePane().layoutXProperty().add((videoNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(videoNode.getNodePane().layoutYProperty().add(videoNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((ImageNode)(rootNode)).getNodePane().layoutXProperty().add(((ImageNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((ImageNode)(rootNode)).getNodePane().layoutXProperty().add(((ImageNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                if(rootNode.getParentNode().getType().toString().equals("topic")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    TopicNode topicNode =(TopicNode) rootNode.getParentNode();

                    line.startXProperty().bind(topicNode.getNodePane().layoutXProperty().add((topicNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(topicNode.getNodePane().layoutYProperty().add(topicNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((ImageNode)(rootNode)).getNodePane().layoutXProperty().add(((ImageNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((ImageNode)(rootNode)).getNodePane().layoutXProperty().add(((ImageNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                ((ImageNode) rootNode).getNodePane().setOnMousePressed(doNothing);
                ((ImageNode) rootNode).getNodePane().setOnMouseDragged(doNothing);
                ((ImageNode) rootNode).getNodePane().setOnMouseReleased(doNothing);
                rootNode.setA(newTranslateX+410);
                rootNode.setB(newTranslateY+225);
            }

            if(rootNode.type.toString().equals("link")) {
                newNodeStage.getChildren().add(((VideoNode) (rootNode)).getNodePane());
                ((VideoNode)(rootNode)).getNodePane().setTranslateX(newTranslateX+410);
                ((VideoNode)(rootNode)).getNodePane().setTranslateY(newTranslateY+225);
                ((VideoNode)rootNode).getNodePane().setOnMouseClicked(onMouseRightClick);


                if(rootNode.getParentNode().getType().toString().equals("string")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    TextNode textNode =(TextNode) rootNode.getParentNode();

                    line.startXProperty().bind(textNode.getNodePane().layoutXProperty().add((textNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(textNode.getNodePane().layoutYProperty().add(textNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((VideoNode)(rootNode)).getNodePane().layoutXProperty().add(((VideoNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((VideoNode)(rootNode)).getNodePane().layoutXProperty().add(((VideoNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                if(rootNode.getParentNode().getType().toString().equals("image")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    ImageNode imageNode =(ImageNode) rootNode.getParentNode();

                    line.startXProperty().bind(imageNode.getNodePane().layoutXProperty().add((imageNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(imageNode.getNodePane().layoutYProperty().add(imageNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((VideoNode)(rootNode)).getNodePane().layoutXProperty().add(((VideoNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((VideoNode)(rootNode)).getNodePane().layoutXProperty().add(((VideoNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                if(rootNode.getParentNode().getType().toString().equals("link")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    VideoNode videoNode =(VideoNode) rootNode.getParentNode();

                    line.startXProperty().bind(videoNode.getNodePane().layoutXProperty().add((videoNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(videoNode.getNodePane().layoutYProperty().add(videoNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((VideoNode)(rootNode)).getNodePane().layoutXProperty().add(((VideoNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((VideoNode)(rootNode)).getNodePane().layoutXProperty().add(((VideoNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                if(rootNode.getParentNode().getType().toString().equals("topic")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    TopicNode topicNode =(TopicNode) rootNode.getParentNode();

                    line.startXProperty().bind(topicNode.getNodePane().layoutXProperty().add((topicNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(topicNode.getNodePane().layoutYProperty().add(topicNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((VideoNode)(rootNode)).getNodePane().layoutXProperty().add(((VideoNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((VideoNode)(rootNode)).getNodePane().layoutXProperty().add(((VideoNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }

                ((VideoNode) rootNode).getNodePane().setOnMousePressed(doNothing);
                ((VideoNode) rootNode).getNodePane().setOnMouseDragged(doNothing);
                ((VideoNode) rootNode).getNodePane().setOnMouseReleased(doNothing);
                rootNode.setA(newTranslateX+410);
                rootNode.setB(newTranslateY+225);
            }
            if(rootNode.type.toString().equals("topic")) {
                newNodeStage.getChildren().add(((TopicNode) (rootNode)).getNodePane());
                ((TopicNode)(rootNode)).getNodePane().setTranslateX(newTranslateX+410);
                ((TopicNode)(rootNode)).getNodePane().setTranslateY(newTranslateY+225);
                ((TopicNode)rootNode).getNodePane().setOnMouseClicked(onMouseRightClick);

                if(rootNode.getParentNode().getType().toString().equals("string")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    TextNode textNode =(TextNode) rootNode.getParentNode();

                    line.startXProperty().bind(textNode.getNodePane().layoutXProperty().add((textNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(textNode.getNodePane().layoutYProperty().add(textNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((TopicNode)(rootNode)).getNodePane().layoutXProperty().add(((TopicNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((TopicNode)(rootNode)).getNodePane().layoutXProperty().add(((TopicNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                if(rootNode.getParentNode().getType().toString().equals("image")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    ImageNode imageNode =(ImageNode) rootNode.getParentNode();

                    line.startXProperty().bind(imageNode.getNodePane().layoutXProperty().add((imageNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(imageNode.getNodePane().layoutYProperty().add(imageNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((TopicNode)(rootNode)).getNodePane().layoutXProperty().add(((TopicNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((TopicNode)(rootNode)).getNodePane().layoutXProperty().add(((TopicNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                if(rootNode.getParentNode().getType().toString().equals("link")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    VideoNode videoNode =(VideoNode) rootNode.getParentNode();

                    line.startXProperty().bind(videoNode.getNodePane().layoutXProperty().add((videoNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(videoNode.getNodePane().layoutYProperty().add(videoNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((TopicNode)(rootNode)).getNodePane().layoutXProperty().add(((TopicNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((TopicNode)(rootNode)).getNodePane().layoutXProperty().add(((TopicNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }
                if(rootNode.getParentNode().getType().toString().equals("topic")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    TopicNode topicNode =(TopicNode) rootNode.getParentNode();

                    line.startXProperty().bind(topicNode.getNodePane().layoutXProperty().add((topicNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(topicNode.getNodePane().layoutYProperty().add(topicNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((TopicNode)(rootNode)).getNodePane().layoutXProperty().add(((TopicNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((TopicNode)(rootNode)).getNodePane().layoutXProperty().add(((TopicNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    rootNode.setParentLine(line);
                    newNodeStage2.getChildren().addAll(line);
                }

                ((TopicNode) rootNode).getNodePane().setOnMousePressed(doNothing);
                ((TopicNode) rootNode).getNodePane().setOnMouseDragged(doNothing);
                ((TopicNode) rootNode).getNodePane().setOnMouseReleased(doNothing);
                rootNode.setA(newTranslateX+410);
                rootNode.setB(newTranslateY+225);
            }
            if (nodeList.size() == rootNode.getLayer()-1) {
                List<MapNode> node = new ArrayList<>();
                node.add(rootNode);
                nodeList.add(node);
            } else {
                nodeList.get(rootNode.getLayer()-1).add(rootNode);
            }
            rootNode.setQuadrant(rootNode.getParentNode().getQuadrant());
            rootNode.setCircleNo(rootNode.getParentNode().getCircleNo()*rootNode.getParentNode().getExpansion());
            rootNode.setExpansion(2*rootNode.getParentNode().getExpansionconst());
            rootNode.setChildLimit(2*rootNode.getParentNode().getExpansionconst());
            rootNode.setChildno((rootNode.getParentNode().getNoOfChildren()* 2)+ (rootNode.getParentNode().getChildno() *rootNode.getParentNode().getExpansion()) );
            if(rootNode.getParentNode().getOffset()!=0)
            {
                rootNode.setOffset((rootNode.getParentNode().getNoOfChildren()+rootNode.getParentNode().getOffset()) * 2);
            }
            rootNode.getParentNode().setNoOfChildren(rootNode.getParentNode().getNoOfChildren()+1);
        }
        // System.out.println("Layer "+ rootNode.getLayer());

        for (int i = 0; i < children; i++) {

            if (!rootNode.children.isEmpty()){
                rootNode.children.get(i).setLayer(rootNode.getLayer()+1);
                rootNode.children.get(i).setQuadrant(rootNode.getQuadrant());
                recursiveDisplay(rootNode.children.get(i));
            }
        }
    }

    public List newCalculateX(int a) {
        List<Double> newcircleX = new ArrayList<>();
        double add = 6.28319 / (a * 10);
        for (double i = 0; i < 6.2831; i = i + add) {
            double x = Math.cos(i) * ClassMap.radius * a;
            newcircleX.add(x);
        }
        return newcircleX;
    }

    public List newCalculateY(int a) {
        List<Double> newcircleY = new ArrayList<>();
        double add = 6.28319 / (a * 10);
        for (double i = 0; i < 6.2831; i = i + add) {
            double y = Math.sin(i) * ClassMap.radius * a;
            newcircleY.add(y);

        }
        return newcircleY;
    }

    public void expand(){
        ClassMap.noOfCircle++;

        ClassMap.calculate();
        int j = 0;
        for (int i = 0; i < nodeList.get(0).size(); i++) {

            if (i % 10 == 0 && i != 0) {
                j++;
            }
            if (nodeList.get(0).get(i).getType().equals("string")){
                ((TextNode)nodeList.get(0).get(i)).getNodePane().setTranslateX(ClassMap.circleX.get(((nodeList.get(0).get(i).getQuadrant() - 1) * ClassMap.noOfCircle) + j) + 410);
                ((TextNode)nodeList.get(0).get(i)).getNodePane().setTranslateY(ClassMap.circleY.get(((nodeList.get(0).get(i).getQuadrant() - 1) * ClassMap.noOfCircle) + j) + 225);
                nodeList.get(0).get(i).setCircleNo(ClassMap.noOfCircle);
                nodeList.get(0).get(i).setA(ClassMap.circleX.get(((nodeList.get(0).get(i).getQuadrant() - 1) * ClassMap.noOfCircle) + j) + 410);
                nodeList.get(0).get(i).setB(ClassMap.circleY.get(((nodeList.get(0).get(i).getQuadrant() - 1) * ClassMap.noOfCircle) + j) + 225);
            }
            else if (nodeList.get(0).get(i).getType().equals("image")){
                ((ImageNode)nodeList.get(0).get(i)).getNodePane().setTranslateX(ClassMap.circleX.get(((nodeList.get(0).get(i).getQuadrant() - 1) * ClassMap.noOfCircle) + j) + 410);
                ((ImageNode)nodeList.get(0).get(i)).getNodePane().setTranslateY(ClassMap.circleY.get(((nodeList.get(0).get(i).getQuadrant() - 1) * ClassMap.noOfCircle) + j) + 225);
                nodeList.get(0).get(i).setCircleNo(ClassMap.noOfCircle);
                nodeList.get(0).get(i).setA(ClassMap.circleX.get(((nodeList.get(0).get(i).getQuadrant() - 1) * ClassMap.noOfCircle) + j) + 410);
                nodeList.get(0).get(i).setB(ClassMap.circleY.get(((nodeList.get(0).get(i).getQuadrant() - 1) * ClassMap.noOfCircle) + j) + 225);

            }
            else if (nodeList.get(0).get(i).getType().equals("link")){
                ((VideoNode)nodeList.get(0).get(i)).getNodePane().setTranslateX(ClassMap.circleX.get(((nodeList.get(0).get(i).getQuadrant() - 1) * ClassMap.noOfCircle) + j) + 410);
                ((VideoNode)nodeList.get(0).get(i)).getNodePane().setTranslateY(ClassMap.circleY.get(((nodeList.get(0).get(i).getQuadrant() - 1) * ClassMap.noOfCircle) + j) + 225);
                nodeList.get(0).get(i).setCircleNo(ClassMap.noOfCircle);
                nodeList.get(0).get(i).setA(ClassMap.circleX.get(((nodeList.get(0).get(i).getQuadrant() - 1) * ClassMap.noOfCircle) + j) + 410);
                nodeList.get(0).get(i).setB(ClassMap.circleY.get(((nodeList.get(0).get(i).getQuadrant() - 1) * ClassMap.noOfCircle) + j) + 225);
            }
            else if (nodeList.get(0).get(i).getType().equals("topic")){
                ((TopicNode)nodeList.get(0).get(i)).getNodePane().setTranslateX(ClassMap.circleX.get(((nodeList.get(0).get(i).getQuadrant() - 1) * ClassMap.noOfCircle) + j) + 410);
                ((TopicNode)nodeList.get(0).get(i)).getNodePane().setTranslateY(ClassMap.circleY.get(((nodeList.get(0).get(i).getQuadrant() - 1) * ClassMap.noOfCircle) + j) + 225);
                nodeList.get(0).get(i).setCircleNo(ClassMap.noOfCircle);
                nodeList.get(0).get(i).setA(ClassMap.circleX.get(((nodeList.get(0).get(i).getQuadrant() - 1) * ClassMap.noOfCircle) + j) + 410);
                nodeList.get(0).get(i).setB(ClassMap.circleY.get(((nodeList.get(0).get(i).getQuadrant() - 1) * ClassMap.noOfCircle) + j) + 225);
            }
        }

        for (int i = 0; i < ClassMap.circleX.size(); i++) {
            for (int k = 0; k < ClassMap.noOfCircle - 1; k++) {
                ClassMap.circleX.remove(i);
                ClassMap.circleY.remove(i);
            }

        }
        expandChildren();
    }
    public  void expandChildren(){

        int add;
        for (int j =1 ; j< nodeList.size();j++) {

            for (int i = 0; i < nodeList.get(j).size(); i++) {

                List<Double> X = newCalculateX(nodeList.get(j).get(i).getParentNode().getCircleNo()*nodeList.get(j).get(i).getParentNode().getExpansion());
                List<Double> Y = newCalculateY(nodeList.get(j).get(i).getParentNode().getCircleNo()*nodeList.get(j).get(i).getParentNode().getExpansion());

                if ( nodeList.get(j).get(i).getChildno() > 0) {
                    add = nodeList.get(j).get(i).getChildno() / 2;
                } else add = 0;

                double newTranslateX = X.get(((nodeList.get(j).get(i).getQuadrant() - 1) * nodeList.get(j).get(i).getParentNode().getCircleNo() * nodeList.get(j).get(i).getParentNode().getExpansion()) + add+(nodeList.get(j).get(i).getParentNode().getOffset()*nodeList.get(j).get(i).getParentNode().getExpansion()));
                double newTranslateY = Y.get(((nodeList.get(j).get(i).getQuadrant() - 1) * nodeList.get(j).get(i).getParentNode().getCircleNo() * nodeList.get(j).get(i).getParentNode().getExpansion()) + add+(nodeList.get(j).get(i).getParentNode().getOffset()*nodeList.get(j).get(i).getParentNode().getExpansion()));

                if (nodeList.get(j).get(i).type.toString().equals("string")) {
                    ((TextNode) (nodeList.get(j).get(i))).getNodePane().setTranslateX(newTranslateX + 410);
                    ((TextNode) (nodeList.get(j).get(i))).getNodePane().setTranslateY(newTranslateY + 225);
                    nodeList.get(j).get(i).setCircleNo(nodeList.get(j).get(i).getParentNode().getCircleNo()*nodeList.get(j).get(i).getParentNode().getExpansion());
                    nodeList.get(j).get(i).setA(newTranslateX + 410);
                    nodeList.get(j).get(i).setB(newTranslateY + 225);

                }
                //System.out.println(nodeList.get(j).get(i).uniqueId);


                if (nodeList.get(j).get(i).type.toString().equals("image")) {
                    ((ImageNode) (nodeList.get(j).get(i))).getNodePane().setTranslateX(newTranslateX + 410);
                    ((ImageNode) (nodeList.get(j).get(i))).getNodePane().setTranslateY(newTranslateY + 225);
                    nodeList.get(j).get(i).setCircleNo(nodeList.get(j).get(i).getParentNode().getCircleNo()*nodeList.get(j).get(i).getParentNode().getExpansion());
                    nodeList.get(j).get(i).setA(newTranslateX + 410);
                    nodeList.get(j).get(i).setB(newTranslateY + 225);
                }

                if (nodeList.get(j).get(i).type.toString().equals("link")) {
                    ((VideoNode) (nodeList.get(j).get(i))).getNodePane().setTranslateX(newTranslateX + 410);
                    ((VideoNode) (nodeList.get(j).get(i))).getNodePane().setTranslateY(newTranslateY + 225);
                    nodeList.get(j).get(i).setCircleNo(nodeList.get(j).get(i).getParentNode().getCircleNo()*nodeList.get(j).get(i).getParentNode().getExpansion());
                    nodeList.get(j).get(i).setA(newTranslateX + 410);
                    nodeList.get(j).get(i).setB(newTranslateY + 225);
                }
                if (nodeList.get(j).get(i).type.toString().equals("topic")) {
                    ((TopicNode) (nodeList.get(j).get(i))).getNodePane().setTranslateX(newTranslateX + 410);
                    ((TopicNode) (nodeList.get(j).get(i))).getNodePane().setTranslateY(newTranslateY + 225);
                    nodeList.get(j).get(i).setCircleNo(nodeList.get(j).get(i).getParentNode().getCircleNo()*nodeList.get(j).get(i).getParentNode().getExpansion());
                    nodeList.get(j).get(i).setA(newTranslateX + 410);
                    nodeList.get(j).get(i).setB(newTranslateY + 225);
                }
            }
        }
    }
    public void expandChildren(MapNode node){
        int add;
        if (node.getLayer()==1){
            node.getParentNode().setExpansionconst(node.getParentNode().getExpansionconst()*2);
            for (int i =0;i<nodeList.get(node.getLayer()-1).size();i++){
                nodeList.get(node.getLayer()-1).get(i).setExpansion(2*node.getParentNode().getExpansionconst());
                nodeList.get(node.getLayer()-1).get(i).setChildLimit(2*node.getParentNode().getExpansionconst());

            }
        }
        else{
            for (int i =0;i<nodeList.get(node.getLayer()-2).size();i++ ){
                nodeList.get(node.getLayer()-2).get(i).setExpansionconst(nodeList.get(node.getLayer()-2).get(i).getExpansionconst()*2);
            }

            for (int i =0;i<nodeList.get(node.getLayer()-1).size();i++){
                nodeList.get(node.getLayer()-1).get(i).setExpansion(2*node.getParentNode().getExpansionconst());
                nodeList.get(node.getLayer()-1).get(i).setChildLimit(2*node.getParentNode().getExpansionconst());

            }

        }

        for (int j =node.getLayer() ; j< nodeList.size();j++) {

            for (int i = 0; i < nodeList.get(j).size(); i++) {

                List<Double> X = newCalculateX(nodeList.get(j).get(i).getParentNode().getCircleNo()*nodeList.get(j).get(i).getParentNode().getExpansion());
                List<Double> Y = newCalculateY(nodeList.get(j).get(i).getParentNode().getCircleNo()*nodeList.get(j).get(i).getParentNode().getExpansion());

                if ( nodeList.get(j).get(i).getChildno() > 0) {
                    add = nodeList.get(j).get(i).getChildno() / 2;
                } else add = 0;

                double newTranslateX = X.get(((nodeList.get(j).get(i).getQuadrant() - 1) * nodeList.get(j).get(i).getParentNode().getCircleNo() * nodeList.get(j).get(i).getParentNode().getExpansion()) + add+(nodeList.get(j).get(i).getParentNode().getOffset()*nodeList.get(j).get(i).getParentNode().getExpansion()));
                double newTranslateY = Y.get(((nodeList.get(j).get(i).getQuadrant() - 1) * nodeList.get(j).get(i).getParentNode().getCircleNo() * nodeList.get(j).get(i).getParentNode().getExpansion()) + add+(nodeList.get(j).get(i).getParentNode().getOffset()*nodeList.get(j).get(i).getParentNode().getExpansion()));

                if (nodeList.get(j).get(i).type.toString().equals("string")) {
                    ((TextNode) (nodeList.get(j).get(i))).getNodePane().setTranslateX(newTranslateX + 410);
                    ((TextNode) (nodeList.get(j).get(i))).getNodePane().setTranslateY(newTranslateY + 225);
                    nodeList.get(j).get(i).setCircleNo(nodeList.get(j).get(i).getParentNode().getCircleNo()*nodeList.get(j).get(i).getParentNode().getExpansion());
                    nodeList.get(j).get(i).setA(newTranslateX + 410);
                    nodeList.get(j).get(i).setB(newTranslateY + 225);
                }
                //System.out.println(nodeList.get(j).get(i).uniqueId);


                if (nodeList.get(j).get(i).type.toString().equals("image")) {
                    ((ImageNode) (nodeList.get(j).get(i))).getNodePane().setTranslateX(newTranslateX + 410);
                    ((ImageNode) (nodeList.get(j).get(i))).getNodePane().setTranslateY(newTranslateY + 225);
                    nodeList.get(j).get(i).setCircleNo(nodeList.get(j).get(i).getParentNode().getCircleNo()*nodeList.get(j).get(i).getParentNode().getExpansion());
                    nodeList.get(j).get(i).setA(newTranslateX + 410);
                    nodeList.get(j).get(i).setB(newTranslateY + 225);
                }

                if (nodeList.get(j).get(i).type.toString().equals("link")) {
                    ((VideoNode) (nodeList.get(j).get(i))).getNodePane().setTranslateX(newTranslateX + 410);
                    ((VideoNode) (nodeList.get(j).get(i))).getNodePane().setTranslateY(newTranslateY + 225);
                    nodeList.get(j).get(i).setCircleNo(nodeList.get(j).get(i).getParentNode().getCircleNo()*nodeList.get(j).get(i).getParentNode().getExpansion());
                    nodeList.get(j).get(i).setA(newTranslateX + 410);
                    nodeList.get(j).get(i).setB(newTranslateY + 225);
                    //System.out.println(((VideoNode) (nodeList.get(j).get(i))).getContents());
                }
                if (nodeList.get(j).get(i).type.toString().equals("topic")) {
                    ((TopicNode) (nodeList.get(j).get(i))).getNodePane().setTranslateX(newTranslateX + 410);
                    ((TopicNode) (nodeList.get(j).get(i))).getNodePane().setTranslateY(newTranslateY + 225);
                    nodeList.get(j).get(i).setCircleNo(nodeList.get(j).get(i).getParentNode().getCircleNo()*nodeList.get(j).get(i).getParentNode().getExpansion());
                    nodeList.get(j).get(i).setA(newTranslateX + 410);
                    nodeList.get(j).get(i).setB(newTranslateY + 225);
                    //System.out.println(((VideoNode) (nodeList.get(j).get(i))).getContents());
                }
            }
        }
    }

    public void populateList() {

        if(DataConnection.getTopic == Boolean.FALSE)
        {
            DataConnection.setTopic();
            for(int x = 0; x < DataConnection.topicNameList.size(); x++)
            {
                WebView web = new WebView();
                WebEngine engine = web.getEngine();
                engine.loadContent(DataConnection.htmlList.get(x).toString());
                TitledPane temp = new TitledPane(DataConnection.topicNameList.get(x).toString(),web);
                topicMenuAccordion.getPanes().addAll(temp);
            }
        }
    }

    public void hideNodes(ActionEvent actionEvent) {
        if(!firstTimePublic) {
            showHome(actionEvent);
            recursiveHide(daroot);
        }
    }

    public void showUserNodes(MouseEvent actionEvent, String user) {
        if(!firstTimePublic) {
            recursiveShowUser(daroot, user);
        }
    }

    public boolean recursiveShowUser(MapNode rootNode, String user) {

        rootNode.previousVote = false;
        int children = rootNode.children.size();

        if (children == 0) {
            rootNode.previousVote = (rootNode.getCreatedBy().equals(user));
            if (rootNode.previousVote == false) {
                rootNode.getParentLine().setVisible(false);
                if (rootNode.type.toString().equals("string"))
                    ((TextNode) (rootNode)).setVisible();

                if (rootNode.type.toString().equals("image"))
                    ((ImageNode) (rootNode)).setVisible();

                if (rootNode.type.toString().equals("link"))
                    ((VideoNode) (rootNode)).setVisible();

                if (rootNode.type.toString().equals("topic"))
                    ((TopicNode) (rootNode)).setVisible();
            }
        }
        else {
            for (int i = 0; i < children; i++) {
                if(rootNode.previousVote == false)
                    rootNode.previousVote = recursiveShowUser(rootNode.children.get(i), user);
                else
                    recursiveShowUser(rootNode.children.get(i), user);
            }
        }

        if (rootNode.getCreatedBy().equals(user)) {
            rootNode.previousVote = true;
        }
        else {
            if(rootNode.uniqueId == 1){

            }
            else {
                if (rootNode.getCreatedBy().equals(user) == false && rootNode.previousVote == false) {
                    rootNode.getParentLine().setVisible(false);
                    if (rootNode.type.toString().equals("string"))
                        ((TextNode) (rootNode)).setVisible();

                    if (rootNode.type.toString().equals("image"))
                        ((ImageNode) (rootNode)).setVisible();

                    if (rootNode.type.toString().equals("link"))
                        ((VideoNode) (rootNode)).setVisible();

                    if (rootNode.type.toString().equals("topic"))
                        ((TopicNode) (rootNode)).setVisible();
                }
            }
        }

        return rootNode.previousVote;
    }


    public boolean recursiveHide(MapNode rootNode) {

        rootNode.previousVote = false;
        int children = rootNode.children.size();

        if (children == 0) {
            rootNode.previousVote = rootNode.getUserVote();
            if (rootNode.getUserVote() == false) {
                rootNode.getParentLine().setVisible(false);
                if (rootNode.type.toString().equals("string"))
                    ((TextNode) (rootNode)).setVisible();

                if (rootNode.type.toString().equals("image"))
                    ((ImageNode) (rootNode)).setVisible();

                if (rootNode.type.toString().equals("link"))
                    ((VideoNode) (rootNode)).setVisible();

                if (rootNode.type.toString().equals("topic"))
                    ((TopicNode) (rootNode)).setVisible();
            }
        }
        else {
            for (int i = 0; i < children; i++) {
                if(rootNode.previousVote == false)
                    rootNode.previousVote = recursiveHide(rootNode.children.get(i));
                else
                    recursiveHide(rootNode.children.get(i));
            }
        }

        if (rootNode.getUserVote() == true) {
            rootNode.previousVote = true;
        }
        else {
            if(rootNode.uniqueId == 1){

            }
            else {
                if (rootNode.getUserVote() == false && rootNode.previousVote == false) {
                    rootNode.getParentLine().setVisible(false);
                    if (rootNode.type.toString().equals("string"))
                        ((TextNode) (rootNode)).setVisible();

                    if (rootNode.type.toString().equals("image"))
                        ((ImageNode) (rootNode)).setVisible();

                    if (rootNode.type.toString().equals("link"))
                        ((VideoNode) (rootNode)).setVisible();

                    if (rootNode.type.toString().equals("topic"))
                        ((TopicNode) (rootNode)).setVisible();
                }
            }
        }

        return rootNode.previousVote;
    }

    public void recursiveShow(MapNode rootNode) {

        int children = rootNode.children.size();

        if(rootNode.type.toString().equals("string")) {
            ((TextNode) (rootNode)).makeVisible();
        }

        if(rootNode.type.toString().equals("image")) {
            ((ImageNode) (rootNode)).makeVisible();
        }

        if(rootNode.type.toString().equals("link")) {
            ((VideoNode) (rootNode)).makeVisible();
        }

        if(rootNode.type.toString().equals("topic")) {
            ((TopicNode) (rootNode)).makeVisible();
        }

        for (int i = 0; i < children; i++) {
            if (!rootNode.children.isEmpty())
                recursiveShow(rootNode.children.get(i));
        }
        if(rootNode.getParentLine() != null)
            (rootNode).getParentLine().setVisible(true);

    }

    public void nodeDragMousePressed(MouseEvent m)
    {
        if (!nodedrag){
            prevPaneCordX= (int) newNodeStage.getLayoutX();
            prevPaneCordY= (int) newNodeStage.getLayoutY();
            prevMouseCordX= (int) m.getX();
            prevMouseCordY= (int) m.getY();}
    }

    // set this method on Mouse Drag event for newNodeStage
    public void nodeDragMouseDragged(MouseEvent m)
    {
        if (!nodedrag)
        {
            diffX= (int) (m.getX()- prevMouseCordX);
            diffY= (int) (m.getY()-prevMouseCordY );
            int x = (int) (diffX+newNodeStage.getLayoutX()-root.getLayoutX());
            int y = (int) (diffY+newNodeStage.getLayoutY()-root.getLayoutY());
            if(m.getSceneX() > 0 && m.getSceneY() > 0) {
                if(factor<0.45) {

                }
                else {
                    newNodeStage.setLayoutX(x);
                    newNodeStage.setLayoutY(y);
                    newNodeStage2.setLayoutX(x);
                    newNodeStage2.setLayoutY(y);
                }
            }
        }
    }

    public void createVideoNode(ActionEvent actionEvent) {
        if(!firstTimePublic) {


            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Create Video Node");
            dialog.setHeaderText("Enter the URL below.");

            ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField text = new TextField();
            text.setPromptText("Enter URL");
            TextArea description = new TextArea();
            description.setWrapText(true);
            description.setMaxHeight(100);
            description.setPromptText("Enter Description");

            grid.add(new Label("Enter YouTube URL : "), 0, 0);
            grid.add(text, 1, 0);
            grid.add(new Label("Description : "), 0, 1);
            grid.add(description, 1, 1);

            Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
            okButton.setDisable(true);

            text.textProperty().addListener((observable, oldValue, newValue) -> {
                okButton.setDisable(newValue.trim().isEmpty());
            });

            dialog.getDialogPane().setContent(grid);

            Platform.runLater(() -> text.requestFocus());

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButtonType) {
                    return new Pair<>(text.getText(), description.getText());
                }
                return null;
            });

            Optional<Pair<String, String>> result = dialog.showAndWait();

            result.ifPresent(videoDescription -> {
                if(result.toString().contains("youtube.com/watch?v=")) {

                    videoNode = new VideoNode(videoDescription.getKey());
                    videoNode.setDescription(videoDescription.getValue());
                    videoNode.getNodePane().setOnMousePressed(OnMousePressedEventHandler);
                    videoNode.getNodePane().setOnMouseDragged(OnMouseDraggedEventHandler);
                    videoNode.getNodePane().setOnMouseReleased(VideoOnMouseReleasedEventHandler);
                    newNodeStage.getChildren().add(videoNode.getNodePane());
                }
                else
                    JOptionPane.showMessageDialog(null, "Not a valid youtube URL.");

            });
        }
    }


    public boolean intersection(GridPane pane) {
        boolean intersect = false;

        for (int j = 0; j < nodeList.size(); j++) {


            for (int i = 0; i < nodeList.get(j).size(); i++) {

                if (nodeList.get(j).get(i).getType() == "string") {
                    TextNode textNode = (TextNode) nodeList.get(j).get(i);
                    Bounds bounds = pane.getBoundsInParent();
                    double a = textNode.getA();
                    double b = textNode.getB();
                    if (textNode.getNodePane().intersects(bounds.getMinX() - a, bounds.getMinY() - b, 100, 100)) {
                        intersect = true;
                        index = i;
                        layer = j;
                        break;
                    }
                } else if (nodeList.get(j).get(i).getType() == "image") {
                    ImageNode imageNode = (ImageNode) nodeList.get(j).get(i);
                    Bounds bounds = pane.getBoundsInParent();
                    double a = imageNode.getA();
                    double b = imageNode.getB();
                    if (imageNode.getNodePane().intersects(bounds.getMinX() - a, bounds.getMinY() - b, 100, 100)) {
                        intersect = true;
                        index = i;
                        layer = j;
                        break;
                    }
                }
                else if (nodeList.get(j).get(i).getType() == "link") {
                    VideoNode videoNode = (VideoNode) nodeList.get(j).get(i);
                    Bounds bounds = pane.getBoundsInParent();
                    double a = videoNode.getA();
                    double b = videoNode.getB();
                    if (videoNode.getNodePane().intersects(bounds.getMinX() - a, bounds.getMinY() - b, 100, 100)) {
                        intersect = true;
                        index = i;
                        layer = j;
                        break;
                    }
                }
                else if (nodeList.get(j).get(i).getType() == "topic") {
                    TopicNode topicNode = (TopicNode) nodeList.get(j).get(i);
                    Bounds bounds = pane.getBoundsInParent();
                    double a = topicNode.getA();
                    double b = topicNode.getB();
                    if (topicNode.getNodePane().intersects(bounds.getMinX() - a, bounds.getMinY() - b, 100, 100)) {
                        intersect = true;
                        index = i;
                        layer = j;
                        break;
                    }
                }
            }

        }
        return intersect;

    }


    EventHandler<MouseEvent> OnMouseReleasedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {

            if (nodeList.get(layer).get(index).getType().equals("string") && nodeList.get(layer).get(index) != null ){
                TextNode textNode = (TextNode) nodeList.get(layer).get(index);
                textNode.getNodePane().setStyle(null);
            }
            else if (nodeList.get(layer).get(index).getType().equals("image") && nodeList.get(layer).get(index) != null)
            {
                ImageNode imageNode = (ImageNode) nodeList.get(layer).get(index);

                imageNode.getNodePane().setStyle(null);
            }
            else  if (nodeList.get(layer).get(index).getType().equals("link") && nodeList.get(layer).get(index) != null)
            {
                VideoNode videoNode = (VideoNode) nodeList.get(layer).get(index);

                videoNode.getNodePane().setStyle(null);
            }
            else  if (nodeList.get(layer).get(index).getType().equals("topic") && nodeList.get(layer).get(index) != null)
            {
                TopicNode topicNode = (TopicNode) nodeList.get(layer).get(index);

                topicNode.getNodePane().setStyle(null);
            }

            nodedrag = false;
            Bounds bounds = newNode.getNodePane().getBoundsInParent();
            if (classNode.getNodePane().intersects(bounds.getMinX() - 410, bounds.getMinY() - 225, 100, 100) && masterNode.size() == (ClassMap.noOfCircle * 10)) {
                expand();
            }
            if (classNode.getNodePane().intersects(bounds.getMinX() - 410, bounds.getMinY() - 225, 100, 100)) {

                if (ClassMap.circleX.size() % 2 == 0) {
                    randomNumber = 0;

                } else randomNumber = ClassMap.circleX.size() / 2;

                double newTranslateX = ClassMap.circleX.get(randomNumber);
                double newTranslateY = ClassMap.circleY.get(randomNumber);
                ClassMap.circleX.remove(randomNumber);
                ClassMap.circleY.remove(randomNumber);

                newNode.getNodePane().setTranslateX(newTranslateX + 410);
                newNode.getNodePane().setTranslateY(newTranslateY + 225);

                Line line = new Line();
                line.setStroke(javafx.scene.paint.Color.BLACK);

                line.startXProperty().bind(classNode.getNodePane().layoutXProperty().add(classNode.getNodePane().translateXProperty()));
                line.startYProperty().bind(classNode.getNodePane().layoutYProperty().add(classNode.getNodePane().translateYProperty()));
                line.endXProperty().bind(newNode.getNodePane().layoutXProperty().add(newNode.getNodePane().translateXProperty()));
                line.endYProperty().bind(newNode.getNodePane().layoutXProperty().add(newNode.getNodePane().translateYProperty()));

                line.setStrokeWidth(4);
                newNode.setParentLine(line);
                newNodeStage2.getChildren().addAll(line);
                newNode.setA(newTranslateX + 410);
                newNode.setB(newTranslateY + 225);
                newNode.setQuadrant(array[masterNode.size()%10] + 1);
                newNode.setCircleNo(ClassMap.noOfCircle);
                newNode.setParentNode(classNode);
                newNode.setParent(newNode.getParentNode().getUniqueId());
                newNode.setChildLimit(2*classNode.getExpansionconst());
                newNode.setLayer(0);
                newNode.setExpansion(2*classNode.getExpansionconst());
                if (masterNode.size() >= 10){
                    newNode.setOffset(1);
                }

                if (nodeList.size() == 0) {

                    masterNode.add(newNode);
                    nodeList.add(masterNode);
                } else {
                    nodeList.get(0).add(newNode);
                }

                classNode.setNoOfChildren(classNode.getNoOfChildren()+1);


                newNode.getNodePane().setOnMousePressed(doNothing);
                newNode.getNodePane().setOnMouseDragged(doNothing);
                newNode.getNodePane().setOnMouseReleased(doNothing);
                newNode.getNodePane().setOnMouseClicked(onMouseRightClick);
                DataConnection.addTextNode(newNode);
            } else if (intersection(newNode.getNodePane())) {

                if (nodeList.get(layer).get(index).getNoOfChildren() == nodeList.get(layer).get(index).getChildLimit()){
                    expandChildren(nodeList.get(layer).get(index));

                }

                List<Double> X = newCalculateX(nodeList.get(layer).get(index).getCircleNo() * nodeList.get(layer).get(index).getExpansion());
                List<Double> Y = newCalculateY(nodeList.get(layer).get(index).getCircleNo() * nodeList.get(layer).get(index).getExpansion());

                double newTranslateX = X.get(((nodeList.get(layer).get(index).getQuadrant() - 1) * (nodeList.get(layer).get(index).getCircleNo())*nodeList.get(layer).get(index).getExpansion()) + nodeList.get(layer).get(index).getNoOfChildren() +nodeList.get(layer).get(index).getChildno() +(nodeList.get(layer).get(index).getOffset()*nodeList.get(layer).get(index).getExpansion()));
                double newTranslateY = Y.get(((nodeList.get(layer).get(index).getQuadrant() - 1) * (nodeList.get(layer).get(index).getCircleNo())*nodeList.get(layer).get(index).getExpansion()) + nodeList.get(layer).get(index).getNoOfChildren() +nodeList.get(layer).get(index).getChildno() +(nodeList.get(layer).get(index).getOffset()*nodeList.get(layer).get(index).getExpansion()));
                newNode.getNodePane().setTranslateX(newTranslateX + 410);
                newNode.getNodePane().setTranslateY(newTranslateY + 225);

                Line line = new Line();
                line.setStroke(javafx.scene.paint.Color.BLACK);

                MapNode parentNode = nodeList.get(layer).get(index);
                parentNode.children.add(newNode);

                if (parentNode.getType() == "string") {
                    TextNode textNode = (TextNode) parentNode;
                    line.startXProperty().bind(textNode.getNodePane().layoutXProperty().add(textNode.getNodePane().translateXProperty()));
                    line.startYProperty().bind(textNode.getNodePane().layoutYProperty().add(textNode.getNodePane().translateYProperty()));
                } else if (parentNode.getType() == "image") {

                    ImageNode imageNode = (ImageNode) parentNode;
                    line.startXProperty().bind(imageNode.getNodePane().layoutXProperty().add(imageNode.getNodePane().translateXProperty()));
                    line.startYProperty().bind(imageNode.getNodePane().layoutYProperty().add(imageNode.getNodePane().translateYProperty()));
                }
                else if (parentNode.getType() == "link") {

                    VideoNode videoNode = (VideoNode) parentNode;
                    line.startXProperty().bind(videoNode.getNodePane().layoutXProperty().add(videoNode.getNodePane().translateXProperty()));
                    line.startYProperty().bind(videoNode.getNodePane().layoutYProperty().add(videoNode.getNodePane().translateYProperty()));
                }
                else if (parentNode.getType() == "topic") {

                    TopicNode topicNode = (TopicNode) parentNode;
                    line.startXProperty().bind(topicNode.getNodePane().layoutXProperty().add(topicNode.getNodePane().translateXProperty()));
                    line.startYProperty().bind(topicNode.getNodePane().layoutYProperty().add(topicNode.getNodePane().translateYProperty()));
                }

                line.setStrokeWidth(4);

                line.endXProperty().bind(newNode.getNodePane().layoutXProperty().add(newNode.getNodePane().translateXProperty()));
                line.endYProperty().bind(newNode.getNodePane().layoutXProperty().add(newNode.getNodePane().translateYProperty()));
                newNode.setParentLine(line);
                newNodeStage2.getChildren().addAll(line);


                newNode.setChildno((nodeList.get(layer).get(index).getNoOfChildren()+nodeList.get(layer).get(index).getChildno()) * 2);
                newNode.setA(newTranslateX + 435);
                newNode.setB(newTranslateY + 260);
                newNode.setQuadrant(nodeList.get(layer).get(index).getQuadrant());
                newNode.setCircleNo(nodeList.get(layer).get(index).getCircleNo() *nodeList.get(layer).get(index).getExpansion());
                newNode.setLayer(nodeList.get(layer).get(index).getLayer() + 1);
                newNode.setExpansion(2*nodeList.get(layer).get(index).getExpansionconst());
                newNode.setChildLimit(2*nodeList.get(layer).get(index).getExpansionconst());
                newNode.setParentNode(nodeList.get(layer).get(index));
                newNode.setParent(newNode.getParentNode().getUniqueId());

                if(nodeList.get(layer).get(index).getOffset()!=0)
                {
                    newNode.setOffset((nodeList.get(layer).get(index).getNoOfChildren()+nodeList.get(layer).get(index).getOffset()) * 2);
                }

                nodeList.get(layer).get(index).setNoOfChildren(nodeList.get(layer).get(index).getNoOfChildren() + 1);

                if (nodeList.size() == layer + 1) {
                    List<MapNode> node = new ArrayList<>();
                    node.add(newNode);
                    nodeList.add(node);
                } else {
                    nodeList.get(layer + 1).add(newNode);
                }

                newNode.getNodePane().setOnMousePressed(doNothing);
                newNode.getNodePane().setOnMouseDragged(doNothing);
                newNode.getNodePane().setOnMouseReleased(doNothing);
                newNode.getNodePane().setOnMouseClicked(onMouseRightClick);
                DataConnection.addTextNode(newNode);
            }
        }

    };

    EventHandler<MouseEvent> OnMousePressedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    nodedrag = true;
                    orgSceneX = t.getSceneX();
                    orgSceneY = t.getSceneY();
                    orgTranslateX = ((GridPane) (t.getSource())).getTranslateX();
                    orgTranslateY = ((GridPane) (t.getSource())).getTranslateY();
                }
            };

    EventHandler<MouseEvent> OnMouseDraggedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {

                    if (nodeList.get(layer).get(index).getType().equals("string") && nodeList.get(layer).get(index) != null){
                        TextNode textNode = (TextNode) nodeList.get(layer).get(index);
                        textNode.getNodePane().setStyle(null);
                    }
                    else if (nodeList.get(layer).get(index).getType().equals("image") && nodeList.get(layer).get(index) != null)
                    {
                        ImageNode imageNode = (ImageNode) nodeList.get(layer).get(index);

                        imageNode.getNodePane().setStyle(null);
                    }
                    else  if (nodeList.get(layer).get(index).getType().equals("link") && nodeList.get(layer).get(index) != null)
                    {
                        VideoNode videoNode = (VideoNode) nodeList.get(layer).get(index);

                        videoNode.getNodePane().setStyle(null);
                    }
                    else  if (nodeList.get(layer).get(index).getType().equals("topic") && nodeList.get(layer).get(index) != null)
                    {
                        TopicNode topicNode = (TopicNode) nodeList.get(layer).get(index);

                        topicNode.getNodePane().setStyle(null);
                    }

                    nodedrag = true;
                    double offsetX = t.getSceneX() - orgSceneX;
                    double offsetY = t.getSceneY() - orgSceneY;
                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;
                    ((GridPane) (t.getSource())).setTranslateX(newTranslateX * (1/factor));
                    ((GridPane) (t.getSource())).setTranslateY(newTranslateY * (1/factor));
                    if (intersection((GridPane)t.getSource())){

                        if (nodeList.get(layer).get(index).getType().equals("string") && nodeList.get(layer).get(index) != null){
                            TextNode textNode = (TextNode) nodeList.get(layer).get(index);
                            textNode.getNodePane().setStyle("-fx-background-color: #4D4DFF;");
                        }
                        else if (nodeList.get(layer).get(index).getType().equals("image") && nodeList.get(layer).get(index) != null)
                        {
                            ImageNode imageNode = (ImageNode) nodeList.get(layer).get(index);

                            imageNode.getNodePane().setStyle("-fx-background-color: #4D4DFF;");
                        }
                        else  if (nodeList.get(layer).get(index).getType().equals("link") && nodeList.get(layer).get(index) != null)
                        {
                            VideoNode videoNode = (VideoNode) nodeList.get(layer).get(index);

                            videoNode.getNodePane().setStyle("-fx-background-color: #4D4DFF;");
                        }
                        else  if (nodeList.get(layer).get(index).getType().equals("topic") && nodeList.get(layer).get(index) != null)
                        {
                            TopicNode topicNode = (TopicNode) nodeList.get(layer).get(index);

                            topicNode.getNodePane().setStyle("-fx-background-color: #4D4DFF;");
                        }
                    }

                }
            };

    EventHandler<MouseEvent> ImageOnMouseReleasedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {

            if (nodeList.get(layer).get(index).getType().equals("string") && nodeList.get(layer).get(index) != null){
                TextNode textNode = (TextNode) nodeList.get(layer).get(index);
                textNode.getNodePane().setStyle(null);
            }
            else if (nodeList.get(layer).get(index).getType().equals("image") && nodeList.get(layer).get(index) != null)
            {
                ImageNode imageNode = (ImageNode) nodeList.get(layer).get(index);

                imageNode.getNodePane().setStyle(null);
            }
            else  if (nodeList.get(layer).get(index).getType().equals("link") && nodeList.get(layer).get(index) != null)
            {
                VideoNode videoNode = (VideoNode) nodeList.get(layer).get(index);

                videoNode.getNodePane().setStyle(null);
            }
            else  if (nodeList.get(layer).get(index).getType().equals("topic") && nodeList.get(layer).get(index) != null)
            {
                TopicNode topicNode = (TopicNode) nodeList.get(layer).get(index);

                topicNode.getNodePane().setStyle(null);
            }
            nodedrag = false;
            Bounds bounds = newImageNode.getNodePane().getBoundsInParent();
            if (classNode.getNodePane().intersects(bounds.getMinX() - 410, bounds.getMinY() - 225, 100, 100) && masterNode.size() == (ClassMap.noOfCircle * 10)) {
                expand();
            }
            if (classNode.getNodePane().intersects(bounds.getMinX() - 410, bounds.getMinY() - 225, 100, 100)) {

                if (ClassMap.circleX.size() % 2 == 0) {
                    randomNumber = 0;

                } else randomNumber = ClassMap.circleX.size() / 2;

                double newTranslateX = ClassMap.circleX.get(randomNumber);
                double newTranslateY = ClassMap.circleY.get(randomNumber);
                ClassMap.circleX.remove(randomNumber);
                ClassMap.circleY.remove(randomNumber);

                newImageNode.getNodePane().setTranslateX(newTranslateX + 410);
                newImageNode.getNodePane().setTranslateY(newTranslateY + 225);

                Line line = new Line();
                line.setStroke(javafx.scene.paint.Color.BLACK);

                line.startXProperty().bind(classNode.getNodePane().layoutXProperty().add(classNode.getNodePane().translateXProperty()));
                line.startYProperty().bind(classNode.getNodePane().layoutYProperty().add(classNode.getNodePane().translateYProperty()));
                line.endXProperty().bind(newImageNode.getNodePane().layoutXProperty().add(newImageNode.getNodePane().translateXProperty()));
                line.endYProperty().bind(newImageNode.getNodePane().layoutXProperty().add(newImageNode.getNodePane().translateYProperty()));

                line.setStrokeWidth(4);
                newImageNode.setParentLine(line);
                newNodeStage2.getChildren().addAll(line);
                newImageNode.setA(newTranslateX + 410);
                newImageNode.setB(newTranslateY + 225);
                newImageNode.setQuadrant(array[masterNode.size()%10] + 1);
                newImageNode.setCircleNo(ClassMap.noOfCircle);
                newImageNode.setParentNode(classNode);
                newImageNode.setParent(newImageNode.getParentNode().getUniqueId());
                newImageNode.setChildLimit(2*classNode.getExpansionconst());
                newImageNode.setLayer(0);
                newImageNode.setExpansion(2*classNode.getExpansionconst());
                if (masterNode.size() >= 10){
                    newImageNode.setOffset(1);
                }

                if (nodeList.size() == 0) {

                    masterNode.add(newImageNode);
                    nodeList.add(masterNode);
                } else {
                    nodeList.get(0).add(newImageNode);
                }

                classNode.setNoOfChildren(classNode.getNoOfChildren()+1);


                newImageNode.getNodePane().setOnMousePressed(doNothing);
                newImageNode.getNodePane().setOnMouseDragged(doNothing);
                newImageNode.getNodePane().setOnMouseReleased(doNothing);
                newImageNode.getNodePane().setOnMouseClicked(onMouseRightClick);
                DataConnection.addImageNode(newImageNode);
            } else if (intersection(newImageNode.getNodePane())) {

                if (nodeList.get(layer).get(index).getNoOfChildren() == nodeList.get(layer).get(index).getChildLimit()){
                    expandChildren(nodeList.get(layer).get(index));

                }

                List<Double> X = newCalculateX(nodeList.get(layer).get(index).getCircleNo() * nodeList.get(layer).get(index).getExpansion());
                List<Double> Y = newCalculateY(nodeList.get(layer).get(index).getCircleNo() * nodeList.get(layer).get(index).getExpansion());

                double newTranslateX = X.get(((nodeList.get(layer).get(index).getQuadrant() - 1) * (nodeList.get(layer).get(index).getCircleNo())*nodeList.get(layer).get(index).getExpansion()) + nodeList.get(layer).get(index).getNoOfChildren()+nodeList.get(layer).get(index).getChildno() +(nodeList.get(layer).get(index).getOffset()*nodeList.get(layer).get(index).getExpansion()));
                double newTranslateY = Y.get(((nodeList.get(layer).get(index).getQuadrant() - 1) * (nodeList.get(layer).get(index).getCircleNo())*nodeList.get(layer).get(index).getExpansion()) + nodeList.get(layer).get(index).getNoOfChildren() +nodeList.get(layer).get(index).getChildno()+(nodeList.get(layer).get(index).getOffset()*nodeList.get(layer).get(index).getExpansion()));
                newImageNode.getNodePane().setTranslateX(newTranslateX + 410);
                newImageNode.getNodePane().setTranslateY(newTranslateY + 225);

                Line line = new Line();
                line.setStroke(javafx.scene.paint.Color.BLACK);

                MapNode parentNode = nodeList.get(layer).get(index);
                parentNode.children.add(newImageNode);

                if (parentNode.getType() == "string") {
                    TextNode textNode = (TextNode) parentNode;
                    line.startXProperty().bind(textNode.getNodePane().layoutXProperty().add(textNode.getNodePane().translateXProperty()));
                    line.startYProperty().bind(textNode.getNodePane().layoutYProperty().add(textNode.getNodePane().translateYProperty()));
                } else if (parentNode.getType() == "image") {

                    ImageNode imageNode = (ImageNode) parentNode;
                    line.startXProperty().bind(imageNode.getNodePane().layoutXProperty().add(imageNode.getNodePane().translateXProperty()));
                    line.startYProperty().bind(imageNode.getNodePane().layoutYProperty().add(imageNode.getNodePane().translateYProperty()));
                }
                else if (parentNode.getType() == "link") {

                    VideoNode videoNode = (VideoNode) parentNode;
                    line.startXProperty().bind(videoNode.getNodePane().layoutXProperty().add(videoNode.getNodePane().translateXProperty()));
                    line.startYProperty().bind(videoNode.getNodePane().layoutYProperty().add(videoNode.getNodePane().translateYProperty()));
                }
                else if (parentNode.getType() == "topic") {

                    TopicNode topicNode = (TopicNode) parentNode;
                    line.startXProperty().bind(topicNode.getNodePane().layoutXProperty().add(topicNode.getNodePane().translateXProperty()));
                    line.startYProperty().bind(topicNode.getNodePane().layoutYProperty().add(topicNode.getNodePane().translateYProperty()));
                }

                line.setStrokeWidth(4);

                line.endXProperty().bind(newImageNode.getNodePane().layoutXProperty().add(newImageNode.getNodePane().translateXProperty()));
                line.endYProperty().bind(newImageNode.getNodePane().layoutXProperty().add(newImageNode.getNodePane().translateYProperty()));
                newImageNode.setParentLine(line);
                newNodeStage2.getChildren().addAll(line);


                newImageNode.setChildno((nodeList.get(layer).get(index).getNoOfChildren()+nodeList.get(layer).get(index).getChildno()) * 2);
                newImageNode.setA(newTranslateX + 435);
                newImageNode.setB(newTranslateY + 260);
                newImageNode.setQuadrant(nodeList.get(layer).get(index).getQuadrant());
                newImageNode.setCircleNo(nodeList.get(layer).get(index).getCircleNo() *nodeList.get(layer).get(index).getExpansion());
                newImageNode.setLayer(nodeList.get(layer).get(index).getLayer() + 1);
                newImageNode.setExpansion(2*nodeList.get(layer).get(index).getExpansionconst());
                newImageNode.setChildLimit(2*nodeList.get(layer).get(index).getExpansionconst());
                newImageNode.setParentNode(nodeList.get(layer).get(index));
                newImageNode.setParent(newImageNode.getParentNode().getUniqueId());

                if(nodeList.get(layer).get(index).getOffset()!=0)
                {
                    newImageNode.setOffset((nodeList.get(layer).get(index).getNoOfChildren()+nodeList.get(layer).get(index).getOffset()) * 2);
                }

                nodeList.get(layer).get(index).setNoOfChildren(nodeList.get(layer).get(index).getNoOfChildren() + 1);

                if (nodeList.size() == layer + 1) {
                    List<MapNode> node = new ArrayList<>();
                    node.add(newImageNode);
                    nodeList.add(node);
                } else {
                    nodeList.get(layer + 1).add(newImageNode);
                }

                newImageNode.getNodePane().setOnMousePressed(doNothing);
                newImageNode.getNodePane().setOnMouseDragged(doNothing);
                newImageNode.getNodePane().setOnMouseReleased(doNothing);
                newImageNode.getNodePane().setOnMouseClicked(onMouseRightClick);
                DataConnection.addImageNode(newImageNode);
            }
        }

    };


    EventHandler<MouseEvent> VideoOnMouseReleasedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {

            if (nodeList.get(layer).get(index).getType().equals("string") && nodeList.get(layer).get(index) != null){
                TextNode textNode = (TextNode) nodeList.get(layer).get(index);
                textNode.getNodePane().setStyle(null);
            }
            else if (nodeList.get(layer).get(index).getType().equals("image") && nodeList.get(layer).get(index) != null)
            {
                ImageNode imageNode = (ImageNode) nodeList.get(layer).get(index);

                imageNode.getNodePane().setStyle(null);
            }
            else  if (nodeList.get(layer).get(index).getType().equals("link") && nodeList.get(layer).get(index) != null)
            {
                VideoNode videoNode = (VideoNode) nodeList.get(layer).get(index);

                videoNode.getNodePane().setStyle(null);
            }
            else  if (nodeList.get(layer).get(index).getType().equals("topic") && nodeList.get(layer).get(index) != null)
            {
                TopicNode topicNode = (TopicNode) nodeList.get(layer).get(index);

                topicNode.getNodePane().setStyle(null);
            }

            nodedrag = false;
            Bounds bounds = videoNode.getNodePane().getBoundsInParent();
            if (classNode.getNodePane().intersects(bounds.getMinX() - 410, bounds.getMinY() - 225, 100, 100) && masterNode.size() == (ClassMap.noOfCircle * 10)) {
                expand();
            }
            if (classNode.getNodePane().intersects(bounds.getMinX() - 410, bounds.getMinY() - 225, 100, 100)) {

                if (ClassMap.circleX.size() % 2 == 0) {
                    randomNumber = 0;

                } else randomNumber = ClassMap.circleX.size() / 2;

                double newTranslateX = ClassMap.circleX.get(randomNumber);
                double newTranslateY = ClassMap.circleY.get(randomNumber);
                ClassMap.circleX.remove(randomNumber);
                ClassMap.circleY.remove(randomNumber);

                videoNode.getNodePane().setTranslateX(newTranslateX + 410);
                videoNode.getNodePane().setTranslateY(newTranslateY + 225);

                Line line = new Line();
                line.setStroke(javafx.scene.paint.Color.BLACK);

                line.startXProperty().bind(classNode.getNodePane().layoutXProperty().add(classNode.getNodePane().translateXProperty()));
                line.startYProperty().bind(classNode.getNodePane().layoutYProperty().add(classNode.getNodePane().translateYProperty()));
                line.endXProperty().bind(videoNode.getNodePane().layoutXProperty().add(videoNode.getNodePane().translateXProperty()));
                line.endYProperty().bind(videoNode.getNodePane().layoutXProperty().add(videoNode.getNodePane().translateYProperty()));

                line.setStrokeWidth(4);
                videoNode.setParentLine(line);
                newNodeStage2.getChildren().addAll(line);
                videoNode.setA(newTranslateX + 410);
                videoNode.setB(newTranslateY + 225);
                videoNode.setQuadrant(array[masterNode.size()%10] + 1);
                videoNode.setCircleNo(ClassMap.noOfCircle);
                videoNode.setParentNode(classNode);
                videoNode.setParent(videoNode.getParentNode().getUniqueId());
                videoNode.setChildLimit(2*classNode.getExpansionconst());
                videoNode.setLayer(0);
                videoNode.setExpansion(2*classNode.getExpansionconst());
                if (masterNode.size() >= 10){
                    videoNode.setOffset(1);
                }

                if (nodeList.size() == 0) {

                    masterNode.add(videoNode);
                    nodeList.add(masterNode);
                } else {
                    nodeList.get(0).add(videoNode);
                }

                classNode.setNoOfChildren(classNode.getNoOfChildren()+1);


                videoNode.getNodePane().setOnMousePressed(doNothing);
                videoNode.getNodePane().setOnMouseDragged(doNothing);
                videoNode.getNodePane().setOnMouseReleased(doNothing);
                videoNode.getNodePane().setOnMouseClicked(onMouseRightClick);
                DataConnection.addVideoNode(videoNode);
            } else if (intersection(videoNode.getNodePane())) {

                if (nodeList.get(layer).get(index).getNoOfChildren() == nodeList.get(layer).get(index).getChildLimit()){
                    expandChildren(nodeList.get(layer).get(index));

                }

                List<Double> X = newCalculateX(nodeList.get(layer).get(index).getCircleNo() * nodeList.get(layer).get(index).getExpansion());
                List<Double> Y = newCalculateY(nodeList.get(layer).get(index).getCircleNo() * nodeList.get(layer).get(index).getExpansion());

                double newTranslateX = X.get(((nodeList.get(layer).get(index).getQuadrant() - 1) * (nodeList.get(layer).get(index).getCircleNo())*nodeList.get(layer).get(index).getExpansion()) + nodeList.get(layer).get(index).getNoOfChildren()+nodeList.get(layer).get(index).getChildno() +(nodeList.get(layer).get(index).getOffset()*nodeList.get(layer).get(index).getExpansion()));
                double newTranslateY = Y.get(((nodeList.get(layer).get(index).getQuadrant() - 1) * (nodeList.get(layer).get(index).getCircleNo())*nodeList.get(layer).get(index).getExpansion()) + nodeList.get(layer).get(index).getNoOfChildren() +nodeList.get(layer).get(index).getChildno()+(nodeList.get(layer).get(index).getOffset()*nodeList.get(layer).get(index).getExpansion()));

                videoNode.getNodePane().setTranslateX(newTranslateX + 410);
                videoNode.getNodePane().setTranslateY(newTranslateY + 225);

                Line line = new Line();
                line.setStroke(javafx.scene.paint.Color.BLACK);

                MapNode parentNode = nodeList.get(layer).get(index);
                parentNode.children.add(videoNode);

                if (parentNode.getType() == "string") {
                    TextNode textNode = (TextNode) parentNode;
                    line.startXProperty().bind(textNode.getNodePane().layoutXProperty().add(textNode.getNodePane().translateXProperty()));
                    line.startYProperty().bind(textNode.getNodePane().layoutYProperty().add(textNode.getNodePane().translateYProperty()));
                } else if (parentNode.getType() == "image") {

                    ImageNode imageNode = (ImageNode) parentNode;
                    line.startXProperty().bind(imageNode.getNodePane().layoutXProperty().add(imageNode.getNodePane().translateXProperty()));
                    line.startYProperty().bind(imageNode.getNodePane().layoutYProperty().add(imageNode.getNodePane().translateYProperty()));
                }
                else if (parentNode.getType() == "link") {

                    VideoNode videoNode = (VideoNode) parentNode;
                    line.startXProperty().bind(videoNode.getNodePane().layoutXProperty().add(videoNode.getNodePane().translateXProperty()));
                    line.startYProperty().bind(videoNode.getNodePane().layoutYProperty().add(videoNode.getNodePane().translateYProperty()));
                }
                else if (parentNode.getType() == "topic") {

                    TopicNode topicNode = (TopicNode) parentNode;
                    line.startXProperty().bind(topicNode.getNodePane().layoutXProperty().add(topicNode.getNodePane().translateXProperty()));
                    line.startYProperty().bind(topicNode.getNodePane().layoutYProperty().add(topicNode.getNodePane().translateYProperty()));
                }

                line.setStrokeWidth(4);

                line.endXProperty().bind(videoNode.getNodePane().layoutXProperty().add(videoNode.getNodePane().translateXProperty()));
                line.endYProperty().bind(videoNode.getNodePane().layoutXProperty().add(videoNode.getNodePane().translateYProperty()));
                videoNode.setParentLine(line);
                newNodeStage2.getChildren().addAll(line);


                videoNode.setChildno((nodeList.get(layer).get(index).getNoOfChildren()+nodeList.get(layer).get(index).getChildno()) * 2);
                videoNode.setA(newTranslateX + 435);
                videoNode.setB(newTranslateY + 260);
                videoNode.setQuadrant(nodeList.get(layer).get(index).getQuadrant());
                videoNode.setCircleNo(nodeList.get(layer).get(index).getCircleNo() *nodeList.get(layer).get(index).getExpansion());
                videoNode.setLayer(nodeList.get(layer).get(index).getLayer() + 1);
                videoNode.setExpansion(2*nodeList.get(layer).get(index).getExpansionconst());
                videoNode.setChildLimit(2*nodeList.get(layer).get(index).getExpansionconst());
                videoNode.setParentNode(nodeList.get(layer).get(index));
                videoNode.setParent(videoNode.getParentNode().getUniqueId());

                if(nodeList.get(layer).get(index).getOffset()!=0)
                {
                    videoNode.setOffset((nodeList.get(layer).get(index).getNoOfChildren()+nodeList.get(layer).get(index).getOffset()) * 2);
                }

                nodeList.get(layer).get(index).setNoOfChildren(nodeList.get(layer).get(index).getNoOfChildren() + 1);

                if (nodeList.size() == layer + 1) {
                    List<MapNode> node = new ArrayList<>();
                    node.add(videoNode);
                    nodeList.add(node);
                } else {
                    nodeList.get(layer + 1).add(videoNode);
                }

                videoNode.getNodePane().setOnMousePressed(doNothing);
                videoNode.getNodePane().setOnMouseDragged(doNothing);
                videoNode.getNodePane().setOnMouseReleased(doNothing);
                videoNode.getNodePane().setOnMouseClicked(onMouseRightClick);
                DataConnection.addVideoNode(videoNode);
            }
        }

    };


    EventHandler<MouseEvent> TopicOnMouseReleasedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {

            if (nodeList.get(layer).get(index).getType().equals("string") && nodeList.get(layer).get(index) != null){
                TextNode textNode = (TextNode) nodeList.get(layer).get(index);
                textNode.getNodePane().setStyle(null);
            }
            else if (nodeList.get(layer).get(index).getType().equals("image") && nodeList.get(layer).get(index) != null)
            {
                ImageNode imageNode = (ImageNode) nodeList.get(layer).get(index);

                imageNode.getNodePane().setStyle(null);
            }
            else  if (nodeList.get(layer).get(index).getType().equals("link") && nodeList.get(layer).get(index) != null)
            {
                VideoNode videoNode = (VideoNode) nodeList.get(layer).get(index);

                videoNode.getNodePane().setStyle(null);
            }
            else  if (nodeList.get(layer).get(index).getType().equals("topic") && nodeList.get(layer).get(index) != null)
            {
                TopicNode topicNode = (TopicNode) nodeList.get(layer).get(index);

                topicNode.getNodePane().setStyle(null);
            }
            nodedrag = false;
            Bounds bounds = newTopicNode.getNodePane().getBoundsInParent();
            if (classNode.getNodePane().intersects(bounds.getMinX() - 410, bounds.getMinY() - 225, 100, 100) && masterNode.size() == (ClassMap.noOfCircle * 10)) {
                expand();
            }
            if (classNode.getNodePane().intersects(bounds.getMinX() - 410, bounds.getMinY() - 225, 100, 100)) {

                if (ClassMap.circleX.size() % 2 == 0) {
                    randomNumber = 0;

                } else randomNumber = ClassMap.circleX.size() / 2;

                double newTranslateX = ClassMap.circleX.get(randomNumber);
                double newTranslateY = ClassMap.circleY.get(randomNumber);
                ClassMap.circleX.remove(randomNumber);
                ClassMap.circleY.remove(randomNumber);

                newTopicNode.getNodePane().setTranslateX(newTranslateX + 410);
                newTopicNode.getNodePane().setTranslateY(newTranslateY + 225);

                Line line = new Line();
                line.setStroke(javafx.scene.paint.Color.BLACK);

                line.startXProperty().bind(classNode.getNodePane().layoutXProperty().add(classNode.getNodePane().translateXProperty()));
                line.startYProperty().bind(classNode.getNodePane().layoutYProperty().add(classNode.getNodePane().translateYProperty()));
                line.endXProperty().bind(newTopicNode.getNodePane().layoutXProperty().add(newTopicNode.getNodePane().translateXProperty()));
                line.endYProperty().bind(newTopicNode.getNodePane().layoutXProperty().add(newTopicNode.getNodePane().translateYProperty()));

                line.setStrokeWidth(4);
                newTopicNode.setParentLine(line);
                newNodeStage2.getChildren().addAll(line);
                newTopicNode.setA(newTranslateX + 410);
                newTopicNode.setB(newTranslateY + 225);
                newTopicNode.setQuadrant(array[masterNode.size()%10] + 1);
                newTopicNode.setCircleNo(ClassMap.noOfCircle);
                newTopicNode.setParentNode(classNode);
                newTopicNode.setParent(newTopicNode.getParentNode().getUniqueId());
                newTopicNode.setChildLimit(2*classNode.getExpansionconst());
                newTopicNode.setLayer(0);
                newTopicNode.setExpansion(2*classNode.getExpansionconst());
                if (masterNode.size() >= 10){
                    newTopicNode.setOffset(1);
                }

                if (nodeList.size() == 0) {

                    masterNode.add(newTopicNode);
                    nodeList.add(masterNode);
                } else {
                    nodeList.get(0).add(newTopicNode);
                }

                classNode.setNoOfChildren(classNode.getNoOfChildren()+1);


                newTopicNode.getNodePane().setOnMousePressed(doNothing);
                newTopicNode.getNodePane().setOnMouseDragged(doNothing);
                newTopicNode.getNodePane().setOnMouseReleased(doNothing);
                newTopicNode.getNodePane().setOnMouseClicked(onMouseRightClick);
                DataConnection.addTopicNode(newTopicNode);
            } else if (intersection(newTopicNode.getNodePane())) {

                if (nodeList.get(layer).get(index).getNoOfChildren() == nodeList.get(layer).get(index).getChildLimit()){
                    expandChildren(nodeList.get(layer).get(index));

                }

                List<Double> X = newCalculateX(nodeList.get(layer).get(index).getCircleNo() * nodeList.get(layer).get(index).getExpansion());
                List<Double> Y = newCalculateY(nodeList.get(layer).get(index).getCircleNo() * nodeList.get(layer).get(index).getExpansion());

                double newTranslateX = X.get(((nodeList.get(layer).get(index).getQuadrant() - 1) * (nodeList.get(layer).get(index).getCircleNo())*nodeList.get(layer).get(index).getExpansion()) + nodeList.get(layer).get(index).getNoOfChildren()+nodeList.get(layer).get(index).getChildno() +(nodeList.get(layer).get(index).getOffset()*nodeList.get(layer).get(index).getExpansion()));
                double newTranslateY = Y.get(((nodeList.get(layer).get(index).getQuadrant() - 1) * (nodeList.get(layer).get(index).getCircleNo())*nodeList.get(layer).get(index).getExpansion()) + nodeList.get(layer).get(index).getNoOfChildren() +nodeList.get(layer).get(index).getChildno()+(nodeList.get(layer).get(index).getOffset()*nodeList.get(layer).get(index).getExpansion()));
                newTopicNode.getNodePane().setTranslateX(newTranslateX + 410);
                newTopicNode.getNodePane().setTranslateY(newTranslateY + 225);

                Line line = new Line();
                line.setStroke(javafx.scene.paint.Color.BLACK);

                MapNode parentNode = nodeList.get(layer).get(index);
                parentNode.children.add(newTopicNode);

                if (parentNode.getType() == "string") {
                    TextNode textNode = (TextNode) parentNode;
                    line.startXProperty().bind(textNode.getNodePane().layoutXProperty().add(textNode.getNodePane().translateXProperty()));
                    line.startYProperty().bind(textNode.getNodePane().layoutYProperty().add(textNode.getNodePane().translateYProperty()));
                } else if (parentNode.getType() == "image") {

                    ImageNode imageNode = (ImageNode) parentNode;
                    line.startXProperty().bind(imageNode.getNodePane().layoutXProperty().add(imageNode.getNodePane().translateXProperty()));
                    line.startYProperty().bind(imageNode.getNodePane().layoutYProperty().add(imageNode.getNodePane().translateYProperty()));
                }
                else if (parentNode.getType() == "link") {

                    VideoNode videoNode = (VideoNode) parentNode;
                    line.startXProperty().bind(videoNode.getNodePane().layoutXProperty().add(videoNode.getNodePane().translateXProperty()));
                    line.startYProperty().bind(videoNode.getNodePane().layoutYProperty().add(videoNode.getNodePane().translateYProperty()));
                }
                else if (parentNode.getType() == "topic") {

                    TopicNode topicNode = (TopicNode) parentNode;
                    line.startXProperty().bind(topicNode.getNodePane().layoutXProperty().add(topicNode.getNodePane().translateXProperty()));
                    line.startYProperty().bind(topicNode.getNodePane().layoutYProperty().add(topicNode.getNodePane().translateYProperty()));
                }

                line.setStrokeWidth(4);

                line.endXProperty().bind(newTopicNode.getNodePane().layoutXProperty().add(newTopicNode.getNodePane().translateXProperty()));
                line.endYProperty().bind(newTopicNode.getNodePane().layoutXProperty().add(newTopicNode.getNodePane().translateYProperty()));
                newTopicNode.setParentLine(line);
                newNodeStage2.getChildren().addAll(line);


                newTopicNode.setChildno((nodeList.get(layer).get(index).getNoOfChildren()+nodeList.get(layer).get(index).getChildno()) * 2);
                newTopicNode.setA(newTranslateX + 435);
                newTopicNode.setB(newTranslateY + 260);
                newTopicNode.setQuadrant(nodeList.get(layer).get(index).getQuadrant());
                newTopicNode.setCircleNo(nodeList.get(layer).get(index).getCircleNo() *nodeList.get(layer).get(index).getExpansion());
                newTopicNode.setLayer(nodeList.get(layer).get(index).getLayer() + 1);
                newTopicNode.setExpansion(2*nodeList.get(layer).get(index).getExpansionconst());
                newTopicNode.setChildLimit(2*nodeList.get(layer).get(index).getExpansionconst());
                newTopicNode.setParentNode(nodeList.get(layer).get(index));
                newTopicNode.setParent(newTopicNode.getParentNode().getUniqueId());

                if(nodeList.get(layer).get(index).getOffset()!=0)
                {
                    newTopicNode.setOffset((nodeList.get(layer).get(index).getNoOfChildren()+nodeList.get(layer).get(index).getOffset()) * 2);
                }

                nodeList.get(layer).get(index).setNoOfChildren(nodeList.get(layer).get(index).getNoOfChildren() + 1);

                if (nodeList.size() == layer + 1) {
                    List<MapNode> node = new ArrayList<>();
                    node.add(newTopicNode);
                    nodeList.add(node);
                } else {
                    nodeList.get(layer + 1).add(newTopicNode);
                }

                newTopicNode.getNodePane().setOnMousePressed(doNothing);
                newTopicNode.getNodePane().setOnMouseDragged(doNothing);
                newTopicNode.getNodePane().setOnMouseReleased(doNothing);
                newTopicNode.getNodePane().setOnMouseClicked(onMouseRightClick);
                DataConnection.addTopicNode(newTopicNode);
            }
        }

    };

}

