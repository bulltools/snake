import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import logic.Snake;
import logic.Food;
import logic.GameControls;

public class App extends Application {

    // Grid configuration
    GridPane grid = new GridPane();
    final int size = 16;
    final int midPoint = size / 2;
    final int cellSize = 35;

    // Snake configuration
    int snakeLength = 2;
    int snakeSpeed = 350;
    Timeline timeline;
    int score = 0;
    private Snake snake;
    
    private boolean snakeMoving = true;

    public static final Color COLOR = Color.RED;
    Label scoreLabel = new Label("Score: " + score);

    // Food configuration
    private Food food;
    java.awt.Point foodPosition;


    Button btn = new Button("Menu");

    @Override
    public void start(Stage primaryStage) {

        snake = new Snake(midPoint, midPoint);
        food = new Food(size);
        
        // Create GameControls object and pass it the snake
        GameControls gameControls = new GameControls(snake);
        
        
        
        // Create GridPane (game board)
        grid.setPrefSize(576, 576);

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Rectangle rect = new Rectangle(cellSize, cellSize);
                rect.setFill(Color.BLUE);
                rect.setStroke(Color.GREEN);
                grid.add(rect, col, row);
            }
        }

        // Create controlBox
        HBox controlBox = new HBox();
        controlBox.setPrefSize(576, 54);
        controlBox.setStyle("-fx-background-color: #424242;");
        btn.setPrefSize(70, 30);
        // btn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size:
        // 15px;");
        controlBox.setAlignment(Pos.CENTER);

        // Create scoreLabel
        
        scoreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
        scoreLabel.setPadding(new Insets(0, 410, 0, 10));

        HBox.setHgrow(scoreLabel, Priority.ALWAYS);

        // Add scoreLabel and menu button to control box
        controlBox.getChildren().addAll(scoreLabel, btn);

        // Add both to a VBox
        VBox root = new VBox(controlBox, grid);

        // Set the scene and stage
        Scene scene = new Scene(root, 576, 630);
        primaryStage.setScene(scene);
        scene.getRoot().requestFocus();
        primaryStage.setResizable(false);
        primaryStage.setTitle("Simple-Snake");

        // Set up a Timeline for snake movement
        timeline = new Timeline(new KeyFrame(Duration.millis(snakeSpeed), event -> {
            if (snakeMoving) {
                snake.move();
                drawSnake(grid);
                checkCollision();
            }
        }));

        scene.setOnKeyPressed(event -> gameControls.handleKeyPress(event.getCode()));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Button action to show modal
        btn.setOnAction(event -> showModal(primaryStage));
        primaryStage.show();
    }

    private void showModal(Stage primaryStage) {
        // New window (Stage)
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initOwner(primaryStage);
        modalStage.setTitle("Modal Window");

        modalStage.initStyle(StageStyle.TRANSPARENT);

        // Contents for the modal
        Button restartGame = new Button("Restart Game");
        Button returnToGame = new Button("Return to Game");
        Button exitGame = new Button("Exit Game");

        // Setting styles and width for buttons
        String buttonStyle = "-fx-background-color: red; -fx-text-fill: white;";
        restartGame.setStyle(buttonStyle);
        returnToGame.setStyle(buttonStyle);
        exitGame.setStyle(buttonStyle);

        restartGame.setMaxWidth(Double.MAX_VALUE);
        returnToGame.setMaxWidth(Double.MAX_VALUE);
        exitGame.setMaxWidth(Double.MAX_VALUE);

        // Set action events for the buttons
        restartGame.setOnAction(event -> {
            // Code to restart the game
            modalStage.close();
        });

        returnToGame.setOnAction(event -> {
            // Code to return to the game
            modalStage.close();
        });

        exitGame.setOnAction(event -> {
            Platform.exit(); // Lukker programmet
        });

        // Arrange buttons vertically
        VBox modalPane = new VBox();
        modalPane.getChildren().addAll(restartGame, returnToGame, exitGame);
        modalPane.setAlignment(Pos.CENTER);
        modalPane.setStyle(
                "-fx-background-color: #f3f3f3;" +
                        "-fx-border-style: solid;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-color: blue;");
        VBox.setMargin(restartGame, new Insets(10, 10, 10, 10));
        VBox.setMargin(returnToGame, new Insets(00, 10, 00, 10));
        VBox.setMargin(exitGame, new Insets(10, 10, 10, 10));

        Scene modalScene = new Scene(modalPane, 140, 130);
        modalStage.setScene(modalScene);
        modalStage.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Methods
    private void drawSnake(GridPane grid) {
        // Clear the previous state of the snake
        grid.getChildren().clear();
        
        // Draw the background grid
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Rectangle rect = new Rectangle(cellSize, cellSize);
                rect.setFill(Color.CORAL);
                rect.setStroke(Color.CORNFLOWERBLUE);
                grid.add(rect, col, row);
            }
        }

        // Draw the food
        drawFood(grid, cellSize);

        // Draw the snake with the updated state
        snake.draw(grid, cellSize);

        eatFood();
    }

    private void checkCollision() {
        // You can add more collision checks here if needed
        if (snake.checkCollision(size)) {
            snakeMoving = false;
            timeline.stop();
            System.out.println("Game Over");
        }
    }

    private void drawFood(GridPane grid, int cellSize) {
        Ellipse foodEllipse = new Ellipse(cellSize / 2.0, cellSize / 2.0); 
        foodEllipse.setFill(Color.RED);

        // Get the position of the food from the Food object
        foodPosition = food.getPosition();

        // Add the food ellipse to the root at the appropriate position
        grid.add(foodEllipse, foodPosition.x, foodPosition.y);
    }

    private void eatFood() {
        Snake.Point headPosition = snake.getHeadPosition();

        if (foodPosition.x == headPosition.x && foodPosition.y == headPosition.y) {
            score = score + 1;
            scoreLabel.setText("Score: " + score);
            
            snakeLength++;
            generateNewFood();

        }
    }

    private void generateNewFood() {
    food.randomizePosition(size); // Assuming you have a method in your Food
    drawFood(grid, cellSize);
    snake.setSnakeSize(snakeLength);
    }

}
