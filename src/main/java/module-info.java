module br.unoeste.photoshopfx2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;
    requires ij;

    opens br.unoeste.photoshopfx2 to javafx.fxml;
    exports br.unoeste.photoshopfx2;

}