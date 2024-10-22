package br.unoeste.photoshopfx2;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class Conversora {

    static public Image tonsCinza(Image image){
        // converte um Image em BufferedImage
        BufferedImage bimg;
        bimg = SwingFXUtils.fromFXImage(image, null);
        // captura pixels da imagem

        /*              R   G   B   A       */
        int pixel[] = { 0 , 0 , 0 , 0 };
        WritableRaster raster=bimg.getRaster();

        for (int lin = 0; lin < image.getHeight(); lin++) {
            for (int col = 0; col < image.getWidth(); col++) {
                raster.getPixel(col,lin,pixel);  // Obtenha um pixel
                int tonsCinza = (int)(0.299 * pixel[0] + 0.587 * pixel[1] + 0.144 * pixel[2]);

                pixel[0] = pixel[1] = pixel[2] = tonsCinza; //Transforma o pixels

                raster.setPixel(col,lin,pixel);  // Reaplique o pixel
            }
        }
        // se necessário, volte para um Image
        image = SwingFXUtils.toFXImage(bimg, null);
        return image;
    }

    static public Image pretoBranco(Image image){
        BufferedImage bimg;
        bimg = SwingFXUtils.fromFXImage(image, null);
        int pixel[] = { 0 , 0 , 0 , 0 };
        WritableRaster raster=bimg.getRaster();

        for (int lin = 0; lin < image.getHeight(); lin++) {
            for (int col = 0; col < image.getWidth(); col++) {
                raster.getPixel(col,lin,pixel);
                int PeB = (int)(0.299 * pixel[0] + 0.587 * pixel[1] + 0.144 * pixel[2]);

                if(PeB >= 127.7){
                    pixel[0] = pixel[1] = pixel[2] = 255;
                }
                else{
                    pixel[0] = pixel[1] = pixel[2] = 0;
                }
                raster.setPixel(col,lin,pixel);
            }
        }
        image = SwingFXUtils.toFXImage(bimg, null);
        return image;
    }

    static public Image espelharHorizontal(Image image){
        BufferedImage bimg = SwingFXUtils.fromFXImage(image, null);
        BufferedImage bimgaux = new BufferedImage(bimg.getWidth(), bimg.getHeight(), bimg.getType());

        int[] pixel = {0, 0, 0, 0};
        WritableRaster raster = bimg.getRaster();
        WritableRaster rasteraux = bimgaux.getRaster();

        for (int lin = 0; lin < image.getHeight(); lin++) {
            for (int col = 0; col < image.getWidth(); col++) {
                raster.getPixel(col, lin, pixel);
                rasteraux.setPixel(bimg.getWidth() - col - 1, lin, pixel);
            }
        }
        image = SwingFXUtils.toFXImage(bimgaux, null);
        return image;
    }

    public static Image espelharVertical(Image image) {
        BufferedImage bimg = SwingFXUtils.fromFXImage(image, null);
        BufferedImage bimgaux = new BufferedImage(bimg.getWidth(), bimg.getHeight(), bimg.getType());

        int[] pixel = {0, 0, 0, 0};
        WritableRaster raster = bimg.getRaster();
        WritableRaster rasteraux = bimgaux.getRaster();

        for (int lin = 0; lin < image.getHeight(); lin++) {
            for (int col = 0; col < image.getWidth(); col++) {
                raster.getPixel(col, lin, pixel);
                rasteraux.setPixel(col, bimg.getHeight() - lin - 1, pixel);
            }
        }
        image = SwingFXUtils.toFXImage(bimgaux, null);
        return image;
    }

    public static Image negativo(Image image) {
        BufferedImage bimg = SwingFXUtils.fromFXImage(image, null);
        BufferedImage bimgaux = new BufferedImage(bimg.getWidth(), bimg.getHeight(), bimg.getType());

        int[] pixel = {0, 0, 0, 0};
        WritableRaster raster = bimg.getRaster();
        WritableRaster rasteraux = bimgaux.getRaster();

        for (int lin = 0; lin < bimg.getHeight(); lin++) {
            for (int col = 0; col < bimg.getWidth(); col++) {
                raster.getPixel(col, lin, pixel);

                pixel[0] = 255 - pixel[0];
                pixel[1] = 255 - pixel[1];
                pixel[2] = 255 - pixel[2];

                rasteraux.setPixel(col, lin, pixel);
            }
        }
        image = SwingFXUtils.toFXImage(bimgaux,null);
        return image;
    }

    public static Image reverterNegativo(Image img) {
        return negativo(img);
    }

    public static Image reverterEspelharVertical(Image image) {
        return espelharVertical(image);
    }

    public static Image reverterEspelharHorizontal(Image image) {
        return espelharHorizontal(image);
    }
}
