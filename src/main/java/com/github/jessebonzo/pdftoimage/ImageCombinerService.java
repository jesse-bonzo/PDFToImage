package com.github.jessebonzo.pdftoimage;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Objects;

public class ImageCombinerService extends Service<BufferedImage> {
  private final List<BufferedImage> images;
  private final File saveFile;
  private final String fileFormat;

  public ImageCombinerService(List<BufferedImage> images, File saveFile, String fileFormat) {
    this.images = Objects.requireNonNull(images);
    this.saveFile = Objects.requireNonNull(saveFile);
    this.fileFormat = Objects.requireNonNull(fileFormat);
  }

  @Override
  protected Task<BufferedImage> createTask() {
    return new Task<>() {
      @Override
      protected BufferedImage call() throws Exception {
        int totalHeight =
            images.stream().map(BufferedImage::getHeight).reduce(Integer::sum).orElse(0);
        int width = images.stream().map(BufferedImage::getWidth).reduce(Integer::max).orElse(0);
        int imageType = images.stream().map(BufferedImage::getType).findFirst().orElse(0);
        BufferedImage combinedImage = new BufferedImage(width, totalHeight, imageType);
        int y = 0;
        for (BufferedImage image : images) {
          combinedImage.getGraphics().drawImage(image, (width - image.getWidth()) / 2, y, null);
          y += image.getHeight();
        }

        ImageIO.write(combinedImage, fileFormat, saveFile);

        return combinedImage;
      }
    };
  }
}
