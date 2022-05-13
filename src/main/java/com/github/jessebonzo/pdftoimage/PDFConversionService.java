package com.github.jessebonzo.pdftoimage;

import javafx.concurrent.Task;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PDFConversionService extends javafx.concurrent.Service<BufferedImage> {

  private final File inputFile;

  public PDFConversionService(File inputFile) {
    this.inputFile = inputFile;
  }

  @Override
  protected Task<BufferedImage> createTask() {
    return new Task<>() {
      @Override
      protected BufferedImage call() throws Exception {
        List<BufferedImage> images = new ArrayList<>();
        if (inputFile != null) {
          try (PDDocument document = PDDocument.load(inputFile)) {
            PDFRenderer renderer = new PDFRenderer(document);
            for (int i = 0; i < document.getNumberOfPages(); i++) {
              final BufferedImage renderedImage = renderer.renderImage(i);
              if (renderedImage != null) {
                images.add(renderedImage);
              }
            }
          }
        }

        int totalHeight =
            images.stream().map(BufferedImage::getHeight).reduce(Integer::sum).orElse(0);
        int width = images.stream().map(BufferedImage::getWidth).reduce(Integer::min).orElse(0);
        int imageType = images.stream().map(BufferedImage::getType).findFirst().orElse(0);
        BufferedImage combinedImage = new BufferedImage(width, totalHeight, imageType);
        int y = 0;
        for (BufferedImage image : images) {
          combinedImage.getGraphics().drawImage(image, 0, y, null);
          y += image.getHeight();
        }

        ImageIO.write(combinedImage, "png", new File("output.png"));

        return combinedImage;
      }
    };
  }
}
