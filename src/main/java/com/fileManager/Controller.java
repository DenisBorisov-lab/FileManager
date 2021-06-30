package com.fileManager;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller implements Initializable {
    @FXML
    TableView<FileInformation> table;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableColumn<FileInformation, String> filenameColumn = new TableColumn<>("Name");
        filenameColumn.setCellValueFactory(s -> new SimpleStringProperty(s.getValue().getFileName()));
        filenameColumn.setPrefWidth(240);

        TableColumn<FileInformation, String> fileTypeColumn = new TableColumn<>("Type");
        filenameColumn.setCellValueFactory(s -> new SimpleStringProperty(s.getValue().getType().toString()));
        filenameColumn.setPrefWidth(24);

        table.getColumns().addAll(filenameColumn, fileTypeColumn);
    }

    public void CollectList(Path path) {


        try {
            table.getItems().clear();
            table.getItems().addAll(Files.list(path)
                    .map(FileInformation::new)
                    .collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public static FileInformation test(Path path){
        try {
            return new FileInformation(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void exitAction(ActionEvent actionEvent) {
        Platform.exit();
    }


}
