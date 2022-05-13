module com.github.jessebonzo.pdftoimage {
  requires javafx.controls;
  requires javafx.fxml;
  requires org.kordamp.bootstrapfx.core;
  requires org.apache.pdfbox;
  requires java.desktop;
  requires javafx.swing;

  opens com.github.jessebonzo.pdftoimage to
      javafx.fxml;

  exports com.github.jessebonzo.pdftoimage;
}
