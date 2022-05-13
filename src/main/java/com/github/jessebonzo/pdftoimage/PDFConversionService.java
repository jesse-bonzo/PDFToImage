package com.github.jessebonzo.pdftoimage;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PDFConversionService extends Service<List<BufferedImage>> {

  private final File inputFile;

  public PDFConversionService(File inputFile) {
    this.inputFile = Objects.requireNonNull(inputFile);
  }

  @Override
  protected Task<List<BufferedImage>> createTask() {
    return new Task<>() {
      @Override
      protected List<BufferedImage> call() throws Exception {
        List<BufferedImage> images = new ArrayList<>();
        try (PDDocument document = PDDocument.load(inputFile)) {
          PDFRenderer renderer = new PDFRenderer(document);
          for (int i = 0; i < document.getNumberOfPages(); i++) {
            BufferedImage renderedImage = renderer.renderImage(i);
            if (renderedImage != null) {
              images.add(renderedImage);
            }
          }
        }
        return List.copyOf(images);
      }
    };
  }
}
