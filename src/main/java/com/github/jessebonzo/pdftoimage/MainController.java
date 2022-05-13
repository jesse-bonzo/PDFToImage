package com.github.jessebonzo.pdftoimage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
  @FXML public Pane pane;

  private final FileChooser pdfFileChooser = new FileChooser();

  public MainController() {
    pdfFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {}

  public void openPDF() {
    File file = pdfFileChooser.showOpenDialog(pane.getScene().getWindow());
    if (file != null) {
      PDFConversionService service = new PDFConversionService(file);
      service.setOnSucceeded(
          event -> {
            BufferedImage pdfImage = (BufferedImage) event.getSource().getValue();
          });
      service.start();
    }
  }
}
