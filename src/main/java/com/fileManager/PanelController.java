package com.fileManager;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PanelController implements Initializable {
    @FXML
    TableView<FileInformation> table;

    @FXML
    ComboBox<String> diskBox;

    @FXML
    TextField pathName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableColumn<FileInformation, String> filenameColumn = new TableColumn<>("Name");
        filenameColumn.setCellValueFactory(s -> new SimpleStringProperty(s.getValue().getFileName()));
        filenameColumn.setPrefWidth(240);

        TableColumn<FileInformation, String> fileTypeColumn = new TableColumn<>("Type");
        fileTypeColumn.setCellValueFactory(s -> new SimpleStringProperty(s.getValue().getType()));
        fileTypeColumn.setPrefWidth(60);

        TableColumn<FileInformation, Long> fileSizeColumn = new TableColumn<>("Size");
        fileSizeColumn.setCellValueFactory(s -> new SimpleObjectProperty<>(s.getValue().getValue()));
        fileSizeColumn.setPrefWidth(120);
        fileSizeColumn.setCellFactory(s -> {
            return new TableCell<FileInformation, Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        String text = generateText(item);
                        if (item == -1) {
                            text = null;
                        }
                        setText(text);
                    }
                }
            };
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TableColumn<FileInformation, String> fileDataColumn = new TableColumn<>("Last modified");
        fileDataColumn.setCellValueFactory(s -> new SimpleStringProperty(s.getValue().getLastModified().format(formatter)));
        fileDataColumn.setPrefWidth(240);

        table.getColumns().addAll(filenameColumn, fileTypeColumn, fileSizeColumn, fileDataColumn);
        table.getSortOrder().add(fileTypeColumn);

        diskBox.getItems().clear();
        for (Path path : FileSystems.getDefault().getRootDirectories()) {
            diskBox.getItems().add(path.toString());
        }
        diskBox.getSelectionModel().select(0);

        table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try{
                    if (event.getClickCount() == 2) {
                        Path path = Paths.get(pathName.getText()).resolve(table.getSelectionModel().getSelectedItem().getFileName());
                        if (Files.isDirectory(path)) {
                            collectList(path);
                        }
                    }
                }catch (Exception ex){

                }

            }
        });

        collectList(Paths.get("C:"));

    }

    public void collectList(Path path) {


        try {
            pathName.setText(path.normalize().toAbsolutePath().toString());
            table.getItems().clear();
            table.getItems().addAll(Files.list(path)
                    .map(FileInformation::new)
                    .collect(Collectors.toList()));
            table.sort();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Can't update the list of files!", ButtonType.OK);
            alert.showAndWait();
        }


    }


    public void upAction(ActionEvent actionEvent) {
        Path upperPath = Paths.get(pathName.getText()).getParent();
        if (upperPath != null) {
            collectList(upperPath);
        }
    }

    public void diskSelection(ActionEvent actionEvent) {
        ComboBox<String> comboBox = (ComboBox<String>) actionEvent.getSource();
        collectList(Paths.get(comboBox.getSelectionModel().getSelectedItem()));
    }

    public String getSelectedFile() {
        if (!table.isFocused()) {
            return null;
        }
        return table.getSelectionModel().getSelectedItem().getFileName();
    }

    //возврат пути выбранного файла
    public String getPath() {
        return pathName.getText();
    }

    public String generateText(Long item) {
        String text = "";
        if (item < 1024) {
            text = String.format("%d bytes", item);
        } else if (item >= 1024 && item < Math.pow(2, 20)) {
            text = String.format("%d Кб", item / 1024);
        } else if (item >= Math.pow(2, 20) && item < Math.pow(2, 30)) {
            text = String.format("%f Мб", item / (Math.pow(2, 20)));
        } else if (item >= Math.pow(2, 30) && item < Math.pow(2, 40)) {
            text = String.format("%f Гб", item / (Math.pow(2, 30)));
        } else if (item >= Math.pow(2, 40) && item < Math.pow(2, 50)) {
            text = String.format("%f Тб", item / (Math.pow(2, 40)));
        }
        return text;
    }
}
