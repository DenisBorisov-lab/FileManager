package com.fileManager;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

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
        if (leftPanelController.getSelectedFile() != null) {
            from = leftPanelController;
            to = rightPanelController;
        }
        if (rightPanelController.getSelectedFile() != null) {
            from = rightPanelController;
            to = leftPanelController;
        }

        Path pathFrom = Paths.get(from.getPath(), from.getSelectedFile());
        Path pathTo = Paths.get(to.getPath()).resolve(pathFrom.getFileName().toString());

        try {
            if (!Files.isDirectory(pathFrom)) {
                Files.copy(pathFrom, pathTo);
            } else {
                copyDirectory(pathFrom.toFile(), pathTo.toFile());
            }
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
        PanelController another = null;

        if (leftPanelController.getSelectedFile() != null) {
            target = leftPanelController;
            another = rightPanelController;
        }
        if (rightPanelController.getSelectedFile() != null) {
            target = rightPanelController;
            another = leftPanelController;
        }
        Path path = Paths.get(target.getPath(), target.getSelectedFile());
        try {
            if (Files.isDirectory(path)) {
                Files.walk(path)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } else {
                Files.delete(path);
            }
            target.collectList(Paths.get(target.getPath()));
            another.collectList(Paths.get(another.getPath()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Can't delete " + path.getFileName(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void alert(PanelController leftPanelController, PanelController rightPanelController) {
        if (leftPanelController.getSelectedFile() == null && rightPanelController.getSelectedFile() == null) {
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

        if (leftPanelController.getSelectedFile() != null) {
            from = leftPanelController;
            to = rightPanelController;
        }
        if (rightPanelController.getSelectedFile() != null) {
            from = rightPanelController;
            to = leftPanelController;
        }
        Path pathFrom = Paths.get(from.getPath(), from.getSelectedFile());
        Path pathTo = Paths.get(to.getPath()).resolve(pathFrom.getFileName().toString());
        try {
            if (!Files.isDirectory(pathFrom)) {
                Files.move(pathFrom, pathTo);
            } else {
                copyDirectory(pathFrom.toFile(), pathTo.toFile());
                Files.walk(pathFrom)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);

            }
            to.collectList(Paths.get(to.getPath()));
            from.collectList(Paths.get(from.getPath()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Can't move " + pathFrom.getFileName(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void copyDirectory(File sourceLocation, File targetLocation)
            throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {

            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }


}
