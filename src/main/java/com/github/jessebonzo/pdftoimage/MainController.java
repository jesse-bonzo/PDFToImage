package com.github.jessebonzo.pdftoimage;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
  @FXML public Pane pane;
  @FXML public MenuBar menuBar;
  @FXML public ListView<BufferedImage> listView;
  @FXML public MenuItem savePNG;

  private final FileChooser pdfFileChooser = new FileChooser();
  private final FileChooser imageFileChooser = new FileChooser();

  public MainController() {
    pdfFileChooser.setTitle("Select PDF");
    pdfFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));

    imageFileChooser.setTitle("Select Image Save Location");
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    listView.setCellFactory(
        bufferedImageListView ->
            new ListCell<>() {
              @Override
              protected void updateItem(BufferedImage bufferedImage, boolean empty) {
                if (empty) {
                  setGraphic(null);
                } else {
                  setGraphic(new ImageView(SwingFXUtils.toFXImage(bufferedImage, null)));
                }
              }
            });
  }

  public void openPDF() {
    File file = pdfFileChooser.showOpenDialog(pane.getScene().getWindow());
    if (file != null) {
      PDFConversionService service = new PDFConversionService(file);
      service.setOnSucceeded(
          event -> {
            List<BufferedImage> images = (List<BufferedImage>) event.getSource().getValue();
            listView.getItems().clear();
            listView.getItems().addAll(images);

            savePNG.setDisable(images.isEmpty());

            images.stream()
                .map(BufferedImage::getWidth)
                .reduce(Integer::max)
                .ifPresent(
                    w -> {
                      listView.setPrefWidth(w);
                      pane.setPrefWidth(w);
                      pane.getScene().getWindow().setWidth(w);
                    });

            images.stream()
                .map(BufferedImage::getHeight)
                .reduce(Integer::max)
                .ifPresent(
                    h -> {
                      listView.setPrefHeight(h);
                      pane.setPrefHeight(h);
                      pane.getScene().getWindow().setHeight(h);
                    });
          });
      service.start();
    }
  }

  public void savePNG() {
    imageFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
    File file = imageFileChooser.showSaveDialog(pane.getScene().getWindow());
    if (file != null) {
      ImageCombinerService service = new ImageCombinerService(listView.getItems(), file, "png");
      service.setOnSucceeded(
          workerStateEvent -> {
            // done
          });
      service.start();
    }
  }
}
