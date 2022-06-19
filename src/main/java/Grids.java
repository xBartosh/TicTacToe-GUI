import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Grids {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 750;

    private Button[][] buttons = new Button[3][3];
    private Button playAgainButton;
    private GridPane gameBoard;
    private Scene mainScene;
    private Stage mainStage;
    private String lastSign;
    private VBox mainVbox;
    private String whoWon;
    private Group group;
    private int pointsX;
    private int pointsO;
    private boolean isOturn;
    private Label signO;
    private Label signX;
    private boolean isDraw = false;



    Grids(Stage mainStage){
        this.mainStage = mainStage;
        mainStage.setTitle("Tictactoe");
        mainStage.setResizable(false);
    }

    public void createNewGame(int pointsX, int pointsO, boolean turnO, String lastSign){
        this.pointsX = pointsX;
        this.pointsO = pointsO;
        this.isOturn = turnO;
        this.lastSign = lastSign;
        createMainScene();
        playAgainButton.setVisible(false);
        this.mainStage.setScene(mainScene);
        this.mainStage.show();
    }


    private Scene createMainScene(){
        mainScene = new Scene(createGroup(), WIDTH, HEIGHT );
        return mainScene;
    }

    private Group createGroup(){
        group = new Group();
        group.getChildren().add(createMainVbox());
        group.setLayoutX(50);
        group.setLayoutY(10);
        return group;
    }

    private VBox createMainVbox(){
        mainVbox = new VBox();
        setMainVboxStyle();
        mainVbox.getChildren().addAll(createRestartButton(),createScoreboard(), createGameBoard(), createPlayAgainButton());

        return mainVbox;
    }

    private Button createRestartButton(){
        Button button = new Button("Restart Score");
        setRestarButtonStyle(button);
        setOnMouseEnteredListener(button);
        setOnMouseExitedListener(button);

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(isOturn){
                    isOturn = false;
                    lastSign = "O";
                }else{
                    isOturn = true;
                    lastSign = "X";
                }
                new Grids(mainStage).createNewGame(0,0, isOturn, lastSign);
            }
        });

        return button;
    }

    private HBox createScoreboard(){
        HBox hBox = new HBox();
        setScoreboardStyle(hBox);
        hBox.getChildren().addAll(createXScoreboard(), createOScoreboard());


        return hBox;
    }

    private VBox createXScoreboard(){
        VBox vBox = new VBox();
        setSignsScoreboardsStyle(vBox);
        vBox.getChildren().addAll(createXSign(), createXScore());

        return vBox;
    }

    private VBox createOScoreboard(){
        VBox vBox = new VBox();
        setSignsScoreboardsStyle(vBox);
        vBox.getChildren().addAll(createOSign(), createOScore());

        return vBox;
    }

    private Label createXSign(){
        signX = new Label("X");
        setSignsStyle(signX);
        if(!isOturn){
           setStyleForTurn(signX);
        }else {
            setStyleForNoTurn(signX);
        }

        return signX;
    }

    private Label createOSign(){
        signO = new Label("O");
        setSignsStyle(signO);
        if(isOturn){
            setStyleForTurn(signO);
        }else {
            setStyleForNoTurn(signO);
        }

        return signO;
    }

    private Label createXScore(){
        Label label = new Label("" + pointsX);
        setPointsStyle(label);

        return label;
    }

    private Label createOScore(){
        Label label = new Label("" + pointsO);
        setPointsStyle(label);

        return label;
    }

    private GridPane createGameBoard(){
        gameBoard = new GridPane();
        createTiles();
        gameBoard.setAlignment(Pos.CENTER);

        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                gameBoard.add(buttons[i][j], j, i);
            }
        }
        return gameBoard;
    }

    private Button[][] createTiles(){
        buttons = new Button[3][3];
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                buttons[i][j] = new Button();
                createStyleForTiles(buttons[i][j]);
                createButtonLogic(buttons[i][j]);
            }
        }

        return buttons;
    }

    private void createStyleForTiles(Button button){
        button.setPrefSize(150,150);
        button.setStyle("-fx-background-color: transparent;"+
                "-fx-border-width: 2;"+
                "-fx-border-color: black");
        button.setFont(new Font("Arial",60));
        button.setAlignment(Pos.CENTER);
    }

    private Button createPlayAgainButton(){
        playAgainButton = new Button("Play again");
        setPlayAgainButtonStyle();
        setOnMouseEnteredListener(playAgainButton);
        setOnMouseExitedListener(playAgainButton);



        playAgainButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(isOturn){
                    isOturn = false;
                    lastSign = "O";
                }else{
                    isOturn = true;
                    lastSign = "X";
                }
                if(!isDraw){
                    addPoints();
                }
                new Grids(mainStage).createNewGame(pointsX, pointsO, isOturn, lastSign);

            }
        });

        return playAgainButton;
    }

    private void createButtonLogic(Button button){
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(lastSign.equals("X") && ((Button)(event.getSource())).getText().isEmpty()){
                    setStyleForTurn(signX);
                    setStyleForNoTurn(signO);
                    button.setText("O");
                    lastSign = "O";
                    checkConditions();


                } else if(lastSign.equals("O") && ((Button)(event.getSource())).getText().isEmpty()){
                    setStyleForTurn(signO);
                    setStyleForNoTurn(signX);
                    button.setText("X");
                    lastSign = "X";
                    checkConditions();
                }
            }
        });
    }

    private void createNewLine(Button start, Button end, int how){
        // How
        // 0 horizontal
        // 1 vertical
        // 2 diagonal R
        // 3 diagonal L


        if(how == 0){
            Line line = new Line((start.getLayoutX()+55), (start.getLayoutY() + 285), (end.getLayoutX()+ 155), (end.getLayoutY()+ 285));
            setLineStyle(line);
            group.getChildren().add(line);
        }else if(how == 1){
            Line line = new Line((start.getLayoutX() + 105), (start.getLayoutY() + 235), (end.getLayoutX()+105), (end.getLayoutY()+ 335));
            setLineStyle(line);
            group.getChildren().add(line);
        }else if(how == 2){
            Line line = new Line((start.getLayoutX()+80), (start.getLayoutY()+ 260), (end.getLayoutX()+130), (end.getLayoutY()+310));
            setLineStyle(line);
            group.getChildren().add(line);
        }else{
            Line line = new Line((start.getLayoutX()+130), (start.getLayoutY()+ 260), (end.getLayoutX()+80), (end.getLayoutY()+310));
            setLineStyle(line);
            group.getChildren().add(line);
        }


    }

    private void checkConditions() {
        if ((buttons[0][0].getText()).equals(buttons[0][1].getText()) && (buttons[0][0].getText()).equals(buttons[0][2].getText()) && !(((buttons[0][0].getText())).isEmpty())) {
            disableButtons();
            this.whoWon = buttons[0][0].getText();                          // | X | X | X |  OR    | O | O | O |
            createNewLine(buttons[0][0], buttons[0][2], 0);            // |   |   |   |        |   |   |   |
            playAgainButton.setVisible(true);                               // |   |   |   |        |   |   |   |
        }

        if ((buttons[1][0].getText()).equals(buttons[1][1].getText()) && (buttons[1][0].getText()).equals(buttons[1][2].getText()) && !(((buttons[1][0].getText())).isEmpty())) {
            disableButtons();
            this.whoWon = buttons[1][0].getText();                          // |   |   |   |        |   |   |   |
            createNewLine(buttons[1][0], buttons[1][2], 0);            // | X | X | X |  OR    | O | O | O |
            playAgainButton.setVisible(true);                               // |   |   |   |        |   |   |   |
        }

        if ((buttons[2][0].getText()).equals(buttons[2][1].getText()) && (buttons[2][0].getText()).equals(buttons[2][2].getText()) && !(((buttons[2][0].getText())).isEmpty())) {
            disableButtons();
            this.whoWon = buttons[2][0].getText();                          // |   |   |   |        |   |   |   |
            createNewLine(buttons[2][0], buttons[2][2], 0);            // |   |   |   |        |   |   |   |
            playAgainButton.setVisible(true);                               // | X | X | X |  OR    | O | O | O |
        }

        if ((buttons[0][0].getText()).equals(buttons[1][0].getText()) && (buttons[0][0].getText()).equals(buttons[2][0].getText()) && !(((buttons[0][0].getText())).isEmpty())) {
            disableButtons();
            this.whoWon = buttons[0][0].getText();                          // | X |   |   |  OR    | O |   |   |
            createNewLine(buttons[0][0], buttons[2][0], 1);            // | X |   |   |        | O |   |   |
            playAgainButton.setVisible(true);                               // | X |   |   |        | O |   |   |
        }

        if ((buttons[0][1].getText()).equals(buttons[1][1].getText()) && (buttons[0][1].getText()).equals(buttons[2][1].getText()) && !(((buttons[0][1].getText())).isEmpty())) {
            disableButtons();
            this.whoWon = buttons[0][1].getText();                          // |   | X |   |  OR    |   | O |   |
            createNewLine(buttons[0][1], buttons[2][1], 1);            // |   | X |   |        |   | O |   |
            playAgainButton.setVisible(true);                               // |   | X |   |        |   | O |   |
        }

        if ((buttons[0][2].getText()).equals(buttons[1][2].getText()) && (buttons[0][2].getText()).equals(buttons[2][2].getText()) && !(((buttons[0][2].getText())).isEmpty())) {
            disableButtons();
            this.whoWon = buttons[0][2].getText();                          // |   |   | X |  OR    |   |   | O |
            createNewLine(buttons[0][2],buttons[2][2], 1);             // |   |   | X |        |   |   | O |
            playAgainButton.setVisible(true);                               // |   |   | X |        |   |   | O |
        }

        if ((buttons[0][0].getText()).equals(buttons[1][1].getText()) && (buttons[0][0].getText()).equals(buttons[2][2].getText()) && !(((buttons[0][0].getText())).isEmpty())) {
            disableButtons();
            this.whoWon = buttons[0][0].getText();                          // | X |   |   |  OR    | O |   |   |
            createNewLine(buttons[0][0], buttons[2][2], 2);            // |   | X |   |        |   | O |   |
             playAgainButton.setVisible(true);                              // |   |   | X |        |   |   | O |
        }

        if ((buttons[0][2].getText()).equals(buttons[1][1].getText()) && (buttons[0][2].getText()).equals(buttons[2][0].getText()) && !(((buttons[0][2].getText())).isEmpty())) {
            disableButtons();
            this.whoWon = buttons[0][2].getText();                         // |   |   | X |  OR    |   |   | O |
            createNewLine(buttons[0][2], buttons[2][0], 3);           // |   | X |   |        |   | O |   |
            playAgainButton.setVisible(true);                              // | X |   |   |        | O |   |   |
        }

        if (!((buttons[0][0].getText()).isEmpty()) && !((buttons[0][1].getText()).isEmpty()) && !((buttons[0][2].getText()).isEmpty()) &&
                !((buttons[1][0].getText()).isEmpty()) && !((buttons[1][1].getText()).isEmpty()) && !((buttons[1][2].getText()).isEmpty()) &&
                !((buttons[2][0].getText()).isEmpty()) && !((buttons[2][1].getText()).isEmpty()) && !((buttons[2][2].getText()).isEmpty())) {
            playAgainButton.setVisible(true);
            isDraw = true;
            disableButtons();
        }

    }

    private void disableButtons(){
        for(int i =0;i<buttons.length;i++){
            for(int j=0;j<buttons[i].length;j++){
                buttons[i][j].setDisable(true);
            }
        }
    }

    private void createDrawAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("DRAW!");
        alert.show();
    }

    private void addPoints(){
        if (whoWon.equals("X")){
            pointsX+=1;
        }else if(whoWon.equals("O")) {
            pointsO+=1;
        }
    }

    private void setLineStyle(Line line){
        line.setStyle("-fx-stroke: red;"+
                "-fx-stroke-width: 3");
    }

    private void setPointsStyle(Label label){
        label.setAlignment(Pos.CENTER);
        label.setFont(new Font("Arial", 15));
        label.setStyle("-fx-text-fill: black");
    }

    private void setSignsStyle(Label label){
        label.setAlignment(Pos.CENTER);
        label.setFont(new Font("Arial", 50));
    }

    private void setSignsScoreboardsStyle(VBox vBox){
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
    }

    private void setMainVboxStyle(){
        mainVbox.setPadding(new Insets(30));
        mainVbox.setSpacing(20);
        mainVbox.setAlignment(Pos.CENTER);
    }

    private void setScoreboardStyle(HBox hBox){
        hBox.setStyle("-fx-border-color: black;"+
                "-fx-border-width: 3;");
        hBox.setSpacing(80);
        hBox.setPrefHeight(100);
        hBox.setMaxWidth(200);
        hBox.setAlignment(Pos.CENTER);
    }

    private void setRestarButtonStyle(Button button){
        button.setPrefHeight(40);
        button.setFont(new Font("Arial", 20));
        button.setStyle("-fx-border-color: black;"+
                "-fx-background-color: transparent");
    }

    private void setPlayAgainButtonStyle(){
        playAgainButton.setFont(new Font("Arial", 20));
        playAgainButton.setAlignment(Pos.CENTER);
        playAgainButton.setStyle("-fx-border-color: black;"+
                "-fx-background-color: transparent;"
                );
    }

    private void setStyleForTurn(Label label){
        label.setStyle("-fx-text-fill: red");
    }

    private void setStyleForNoTurn(Label label){
        label.setStyle("-fx-text-fill: black;");
    }

    private void setOnMouseEnteredListener(Button button){
        button.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mainScene.setCursor(Cursor.HAND);
                button.setStyle("-fx-background-color: black;"+
                        "-fx-text-fill:white;"+
                        "-fx-border-color:black");
            }
        });
    }

    private void setOnMouseExitedListener(Button button){
        button.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mainScene.setCursor(Cursor.DEFAULT);
                button.setStyle("-fx-background-color: transparent;"+
                        "-fx-text-fill: black;"+
                        "-fx-border-color: black");
            }
        });

    }
}
