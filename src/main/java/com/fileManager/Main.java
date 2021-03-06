package com.fileManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        primaryStage.setTitle("File manager");
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/file.png")));
        primaryStage.setScene(new Scene(root));
        primaryStage.minWidthProperty();
        primaryStage.minHeightProperty();
        primaryStage.show();
    }
}
