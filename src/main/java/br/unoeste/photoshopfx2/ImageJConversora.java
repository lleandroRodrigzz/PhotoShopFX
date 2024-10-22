package br.unoeste.photoshopfx2;

import ij.ImagePlus;
import ij.process.ImageProcessor;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import java.awt.image.BufferedImage;

public class ImageJConversora {
    static public Image detectarBordasIJ(Image image){
        ImagePlus imagePlus = new ImagePlus();
        BufferedImage bimg;
        bimg = SwingFXUtils.fromFXImage(image,null);
        imagePlus.setImage(bimg);
        ImageProcessor imageProcessor = imagePlus.getProcessor();
        imageProcessor.findEdges();
        return SwingFXUtils.toFXImage(imagePlus.getBufferedImage(),null);
    }

    public static Image realcarContraste(Image image) {
        BufferedImage bimg = SwingFXUtils.fromFXImage(image,null);
        ImagePlus imagePlus = new ImagePlus("Contraste", bimg);
        ImageProcessor imageProcessor = imagePlus.getProcessor();

        // Aplica o realce de contraste
        imageProcessor.resetMinAndMax();
        imageProcessor.setMinAndMax(50, 200); // Define o intervalo de valores de pixel

        return SwingFXUtils.toFXImage(imagePlus.getBufferedImage(),null);
    }

    public static Image filtroGaussianoIJ(Image image) {
        BufferedImage bimg = SwingFXUtils.fromFXImage(image,null);
        ImagePlus imagePlus = new ImagePlus("Gaussian Blur", bimg);
        ImageProcessor imageProcessor = imagePlus.getProcessor();

        imageProcessor.blurGaussian(5.0);
        return SwingFXUtils.toFXImage(imagePlus.getBufferedImage(),null);
    }

    public static Image transformacaoMorfologica(Image image) {
        BufferedImage bimg = SwingFXUtils.fromFXImage(image,null);
        ImagePlus imagePlus = new ImagePlus("Dilate", bimg);
        ImageProcessor imageProcessor = imagePlus.getProcessor();

        imageProcessor.threshold(128);
        imageProcessor.dilate();
        return SwingFXUtils.toFXImage(imagePlus.getBufferedImage(),null);
    }
}
