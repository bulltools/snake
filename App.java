
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
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
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.Snake;
import ui.ShowModal;
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
    int snakeSpeed = 250;
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

        snake = new Snake(midPoint, midPoint - 1);
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
        // btn.setStyle("-fx-background-color: red; -fx-text-fill: white;
        // -fx-font-size:
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
        btn.setOnAction(event -> {
            new ShowModal(primaryStage);
        });
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    // Methods
    private void drawSnake(GridPane grid) {
        // Clear the previous state of the snake
        grid.getChildren().clear();

        // should be separated from the grid in the start method; background layer
        // should be static
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Rectangle rect = new Rectangle(cellSize, cellSize);
                rect.setFill(Color.LIGHTBLUE);
                rect.setStroke(Color.LIGHTSKYBLUE);
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
            score++;

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
