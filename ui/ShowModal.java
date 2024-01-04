package ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ShowModal {
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
            modalStage.close();
        });

        returnToGame.setOnAction(event -> {
            // Code to return to the game
            modalStage.close();
        });

        exitGame.setOnAction(event -> {
            Platform.exit(); // Lukker programmet
        });

        // VBox to hold the buttons
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

    public ShowModal(Stage primaryStage) {
        showModal(primaryStage);
    }

}
