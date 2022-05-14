package com.github.jessebonzo.pdftoimage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class PDFToImageApplication extends Application {
  @Override
  public void start(Stage primaryStage) throws IOException {
    primaryStage
        .getIcons()
        .add(new Image(PDFToImageApplication.class.getResourceAsStream("logo.png")));
    FXMLLoader fxmlLoader =
        new FXMLLoader(PDFToImageApplication.class.getResource("main-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 800, 600);
    scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
    primaryStage.setTitle("PDF to Image");
    primaryStage.setScene(scene);
    primaryStage.sizeToScene();
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}
