package com.fileManager;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    @FXML
    VBox left;

    @FXML
    VBox right;


    public void exitAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void copy(ActionEvent actionEvent) {
        PanelController leftPanelController = (PanelController) left.getProperties().get("ctrl");
        PanelController rightPanelController = (PanelController) right.getProperties().get("ctrl");

        alert(leftPanelController, rightPanelController);

        PanelController from = null;
        PanelController to = null;
        if (leftPanelController.getSelectedFile() != null){
            from = leftPanelController;
            to = rightPanelController;
        }
        if (rightPanelController.getSelectedFile() != null){
            from = rightPanelController;
            to = leftPanelController;
        }

        Path pathFrom = Paths.get(from.getPath(), from.getSelectedFile());
        Path pathTo = Paths.get(to.getPath()).resolve(pathFrom.getFileName().toString());

        try {
            Files.copy(pathFrom,pathTo);
            to.collectList(Paths.get(to.getPath()));
            from.collectList(Paths.get(from.getPath()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Can't copy " + pathFrom.getFileName(), ButtonType.OK);
            alert.showAndWait();
        }

    }

    public void delete(ActionEvent actionEvent) {

        PanelController leftPanelController = (PanelController) left.getProperties().get("ctrl");
        PanelController rightPanelController = (PanelController) right.getProperties().get("ctrl");

        alert(leftPanelController, rightPanelController);
        PanelController target = null;

        if (leftPanelController.getSelectedFile() != null){
            target = leftPanelController;
        }
        if (rightPanelController.getSelectedFile() != null){
            target = rightPanelController;
        }
        Path path = Paths.get(target.getPath(), target.getSelectedFile());
        try {
            Files.delete(path);
            target.collectList(Paths.get(target.getPath()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Can't delete " + path.getFileName(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void alert(PanelController leftPanelController, PanelController rightPanelController){
        if (leftPanelController.getSelectedFile() == null && rightPanelController.getSelectedFile() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "No selected files", ButtonType.OK);
            alert.showAndWait();
            return;
        }
    }

    public void move(ActionEvent actionEvent) {
        PanelController leftPanelController = (PanelController) left.getProperties().get("ctrl");
        PanelController rightPanelController = (PanelController) right.getProperties().get("ctrl");

        alert(leftPanelController, rightPanelController);

        PanelController from = null;
        PanelController to = null;

        if (leftPanelController.getSelectedFile() != null){
            from = leftPanelController;
            to = rightPanelController;
        }
        if (rightPanelController.getSelectedFile() != null){
            from = rightPanelController;
            to = leftPanelController;
        }
        Path pathFrom = Paths.get(from.getPath(), from.getSelectedFile());
        Path pathTo = Paths.get(to.getPath()).resolve(pathFrom.getFileName().toString());
        try {
            Files.move(pathFrom,pathTo);
            to.collectList(Paths.get(to.getPath()));
            from.collectList(Paths.get(from.getPath()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Can't move " + pathFrom.getFileName(), ButtonType.OK);
            alert.showAndWait();
        }
    }


}
