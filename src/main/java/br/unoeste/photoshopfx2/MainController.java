package br.unoeste.photoshopfx2;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.scene.canvas.Canvas;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;

public class MainController implements Initializable {
    public ImageView imageView;
    private File currentfile;
    private File arquivoAtual;
    private Image imagemOriginal;
    private boolean imgAlterada = false;
    private int negativoAtivo = 0;
    private int tonsDeCinzaAtivo = 0;
    private int pretoBrancoAtivo = 0;
    private int espelharHAtivo = 0;
    private int espelharVAtivo = 0;
    private boolean detectarBordAtivo = false;
    private boolean filtoGaussAtivo = false;
    private boolean transMorfoAtivo = false;
    private boolean realcarContra = false;
    private GraphicsContext gc;
    private Canvas canvas;
    private Image imgOriginal2;
    private boolean canetaAtiva = false;
    private Color corCaneta = Color.BLACK;
    @FXML
    private ColorPicker colorPicker;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void onSair(ActionEvent event) {
        if(imgAlterada){
            Alert confirma = new Alert(Alert.AlertType.CONFIRMATION);
            confirma.setTitle("Confirmação de Fechamento");
            confirma.setHeaderText("Você fez alterações na imagem.");
            confirma.setContentText("Deseja salvar as alterações antes de fechar?");

            ButtonType salvarBtn = new ButtonType("Salvar");
            ButtonType naoSalvarBtn = new ButtonType("Não Salvar");
            ButtonType cancelarBtn = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            confirma.getButtonTypes().setAll(salvarBtn,naoSalvarBtn,cancelarBtn);

            Optional<ButtonType> resultado = confirma.showAndWait();

            if(resultado.isPresent()){
                if(resultado.get() == salvarBtn){
                    onSalvar(null);
                    if(!imgAlterada){
                        Platform.exit();
                    }
                }
                else if(resultado.get() == naoSalvarBtn){
                    Platform.exit();
                }
            }
            else{
                Platform.exit();
            }
        }
        else{
            Platform.exit();
        }
    }

    public void onAbrir(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File dirInicial = new File("C://Users//leand//OneDrive//Documentos//minhas fotos");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Todas imagens", "*.png","*.jpg","*.jpeg","*.bmp","*.webp","*.gif")
                ,new FileChooser.ExtensionFilter("JPEG", "*.jpg")
                ,new FileChooser.ExtensionFilter("PNG", "*.png")
                ,new FileChooser.ExtensionFilter("GIF","*.gif")
                ,new FileChooser.ExtensionFilter("BMP", "*.bmp")
                ,new FileChooser.ExtensionFilter("ICO", "*.ico")
        );

        if (dirInicial.exists() && dirInicial.isDirectory()) {
            fileChooser.setInitialDirectory(dirInicial);
        } else {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        }

        File file = fileChooser.showOpenDialog(null);
        if(file != null){
            Image image;
            image = new Image(file.toURI().toString());
            imageView.setImage(image);
            imageView.setFitHeight(image.getHeight());
            imageView.setFitWidth(image.getWidth());
            currentfile = file;
            arquivoAtual = file;
        }
    }

    private String getExtencao(String fileName) {
        String ext = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            ext = fileName.substring(i + 1);
        }
        return ext;
    }

    public void onSalvar(ActionEvent event) {
        Alert confirma = new Alert(Alert.AlertType.CONFIRMATION);
        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
        if (currentfile == null){
            alert1.setTitle("Alerta");
            alert1.setHeaderText("Alerta de Salvamento");
            alert1.setContentText("Você não pode salvar algo NULO");
            alert1.showAndWait();
        }
        else{
            confirma.setTitle("Confirma salvamento");
            confirma.setHeaderText("Você deseja salvar ?");
            confirma.setContentText("OBS: Se você salvar, você perdera o arquivo orginal");
            Optional<ButtonType> resultado = confirma.showAndWait();

            if(resultado.isPresent() && resultado.get() == ButtonType.OK){
                if(arquivoAtual != null){
                    try {
                        Image image = imageView.getImage();
                        BufferedImage bimg = SwingFXUtils.fromFXImage(image,null);
                        String ext = getExtencao(arquivoAtual.getName());
                        if (ext.equals("jpg") || ext.equals("jpeg")){
                            File newFile = new File(arquivoAtual.getAbsolutePath().replaceAll("\\.jpg$|\\.jpeg$", ".png"));
                            ImageIO.write(bimg, "png", newFile);
                        }
                        else{
                            ImageIO.write(bimg, ext, arquivoAtual);
                        }
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Confirmação");
                        alert.setHeaderText("Seu arquivo foi salvo com sucesso!");
                        alert.setContentText(null);
                        alert.showAndWait();
                        imgAlterada = false;
                    }catch (IOException e){
                        System.out.println("Erro ao salvar imagem: " + e.getMessage());
                    }
                }
                else{
                    System.out.println("Nenhum arquivo para salvar. Por favor, abra uma imagem primeiro.");
                }
            }
            else{
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Salvamento cancelado");
                alerta.setHeaderText("Seu arquivo não foi salvo e nem substituido");
                alerta.setContentText(null);
                alerta.showAndWait();
            }
        }
    }

    public void onSalvarComo(ActionEvent event) {
        FileChooser fileChooser;
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Todas imagens", "*.png","*.jpg","*.jpeg","*.bmp","*.webp","*.gif")
                ,new FileChooser.ExtensionFilter("JPEG", "*.jpg")
                ,new FileChooser.ExtensionFilter("PNG", "*.png")
                ,new FileChooser.ExtensionFilter("GIF","*.gif")
                ,new FileChooser.ExtensionFilter("BMP", "*.bmp")
                ,new FileChooser.ExtensionFilter("ICO", "*.ico")
        );
        fileChooser.setInitialDirectory(new File("C://Users//leand//OneDrive//Documentos//minhas fotos"));

        File file = fileChooser.showSaveDialog(null);
        if(file != null){
            try {
                Image image = imageView.getImage();
                BufferedImage bimg = SwingFXUtils.fromFXImage(image, null);

                String fileExtension = getExtencao(file.getName());
                if (fileExtension.equalsIgnoreCase("png")) {
                    ImageIO.write(bimg, "png", file);
                }
                else if (fileExtension.equalsIgnoreCase("jpg") || fileExtension.equalsIgnoreCase("jpeg")) {
                    ImageIO.write(bimg, "jpg", file);
                }
                else if (fileExtension.equalsIgnoreCase("gif")) {
                    ImageIO.write(bimg, "gif", file);
                }
                else if (fileExtension.equalsIgnoreCase("bmp")) {
                    ImageIO.write(bimg, "bmp", file);
                }
                else if (fileExtension.equalsIgnoreCase("ico")) {
                    ImageIO.write(bimg, "ico", file);
                }
                else {
                    System.out.println("Formato de imagem não suportado.");
                }
            }catch (IOException e){
                System.out.println("Erro ao salvar a imagem: " + e.getMessage());
            }
        }
    }

    public void onTonsCinza(ActionEvent event) {
        if(tonsDeCinzaAtivo == 1){
            imageView.setImage(imagemOriginal);
            imgAlterada = true;
            tonsDeCinzaAtivo = 0;
        }
        else{
            Image image = imageView.getImage();
            imagemOriginal = imageView.getImage();
            image = Conversora.tonsCinza(image);
            imageView.setImage(image);
            imgAlterada = true;
            tonsDeCinzaAtivo = 1;
        }
    }

    public void onPretoBranco(ActionEvent event) {
        if(pretoBrancoAtivo == 1){
            imageView.setImage(imagemOriginal);
            imgAlterada = true;
            pretoBrancoAtivo = 0;
        }
        else{
            Image image = imageView.getImage();
            imagemOriginal = imageView.getImage();
            image = Conversora.pretoBranco(image);
            imageView.setImage(image);
            imgAlterada = true;
            pretoBrancoAtivo = 1;
        }
    }

    public void onEspelharHorizontal(ActionEvent event) {
        if(espelharHAtivo == 1){
            Image image = imageView.getImage();
            image = Conversora.reverterEspelharHorizontal(image);
            imageView.setImage(image);
            imgAlterada = true;
            espelharHAtivo = 0;
        }
        else{
            Image image = imageView.getImage();
            image = Conversora.espelharHorizontal(image);
            imageView.setImage(image);
            imgAlterada = true;
            espelharHAtivo = 1;
        }

    }

    public void onEspelharVertical(ActionEvent event) {
        if(espelharVAtivo == 1){
            Image image = imageView.getImage();
            image = Conversora.reverterEspelharVertical(image);
            imageView.setImage(image);
            imgAlterada = true;
            espelharVAtivo = 0;
        }
        else{
            Image image = imageView.getImage();
            image = Conversora.espelharVertical(image);
            imageView.setImage(image);
            imgAlterada = true;
            espelharVAtivo = 1;
        }

    }

    public void onNegativo(ActionEvent event) {
        if(negativoAtivo == 1){
            //reverter a imagem para original
            Image image = imageView.getImage();
            image = Conversora.reverterNegativo(image);
            imageView.setImage(image);
            imgAlterada = true;
            negativoAtivo = 0;
        }
        else {
            //aplicar efeito
            Image image = imageView.getImage();
            image = Conversora.negativo(image);
            imageView.setImage(image);
            imgAlterada = true;
            negativoAtivo = 1;
        }
    }

    public void onInfo(ActionEvent event) {
        if(currentfile != null){
            String nomeArq = currentfile.getName();
            double tamArq = currentfile.length();
            String dir = currentfile.getAbsolutePath();

            // Converte o tamanho do arquivo para KB
            double tamArqKB = tamArq / 1024.0;

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setResizable(true);
            alert.getDialogPane().setPrefSize(480,200);
            alert.setTitle("Informações da Imagem");
            alert.setHeaderText("Detalhes da imagem:");
            alert.setContentText(
                            "Nome do arquivo: " + nomeArq + "\n" +
                            "Tamanho do arquivo: " + String.format("%.2f", tamArqKB) + " KB\n" +
                            "Caminho do arquivo: " + dir
            );
            alert.showAndWait();
        }
        else{
            // Caso não haja uma imagem carregada
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Informação");
            alert.setHeaderText(null);
            alert.setContentText("Nenhuma imagem carregada.");
            alert.showAndWait();
        }
    }

    public void onSobre(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("PhotoShopFX - Sobre");
        alert.setHeaderText("PhotoShopFX: Versão 1.1");
        alert.setContentText("Desenvolvido by Leandro Rodrigues");
        alert.showAndWait();
    }

    public void onDetectarBordas(ActionEvent event) {
        if(detectarBordAtivo){
            imageView.setImage(imagemOriginal);
            imgAlterada = true;
            detectarBordAtivo = false;
        }
        else{
            imagemOriginal = imageView.getImage();
            Image image = imageView.getImage();
            image = ImageJConversora.detectarBordasIJ(image);
            imageView.setImage(image);
            detectarBordAtivo = true;
            imgAlterada = true;
        }
    }

    public void onFiltroGaussiano(ActionEvent actionEvent) {
        if(filtoGaussAtivo){
            imageView.setImage(imagemOriginal);
            imgAlterada = true;
            filtoGaussAtivo = false;
        }
        else{
            imagemOriginal = imageView.getImage();
            Image image = imageView.getImage();
            image = ImageJConversora.filtroGaussianoIJ(image);
            imageView.setImage(image);
            filtoGaussAtivo = true;
            imgAlterada = true;
        }
    }

    public void onTransMorfologica(ActionEvent actionEvent) {
        if(transMorfoAtivo){
            imageView.setImage(imagemOriginal);
            imgAlterada = true;
            transMorfoAtivo = false;
        }
        else{
            imagemOriginal = imageView.getImage();
            Image image = imageView.getImage();
            image = ImageJConversora.transformacaoMorfologica(image);
            imageView.setImage(image);
            transMorfoAtivo = true;
            imgAlterada = true;
        }
    }

    public void onRealcarContraste(ActionEvent actionEvent) {
        if(realcarContra){
            imageView.setImage(imagemOriginal);
            imgAlterada = true;
            realcarContra = false;
        }
        else{
            imagemOriginal = imageView.getImage();
            Image image = imageView.getImage();
            image = ImageJConversora.realcarContraste(image);
            imageView.setImage(image);
            realcarContra = true;
            imgAlterada = true;
        }
    }

    public void onMudaCorCaneta(ActionEvent event){
        corCaneta = colorPicker.getValue();
        if(canetaAtiva && gc != null){
            gc.setStroke(corCaneta);
        }
    }

    public void onLigaCaneta(ActionEvent event) {
        if(!canetaAtiva){
            saveImage(imageView.getImage());
            canvas = new Canvas(imageView.getFitWidth(), imageView.getFitHeight());
            gc = canvas.getGraphicsContext2D();
            gc.setStroke(corCaneta);
            gc.setLineWidth(3);

            Image image = imageView.getImage();
            gc.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight());

            canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onMousePressed);
            canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onMouseDragged);
            canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, this::onMouseReleased);

            ((Pane) imageView.getParent()).getChildren().add(canvas);
            canetaAtiva = true;
        }
        else{
            onDesligaCaneta();
            canetaAtiva = false;
        }
    }

    public void onDesligaCaneta() {
        ((Pane) imageView.getParent()).getChildren().remove(canvas);
        canvas = null;
        gc = null;
        canetaAtiva = false;
        loadImage(imgOriginal2);
    }

    private void onMousePressed(MouseEvent event) {
        gc.beginPath();
        gc.moveTo(event.getX(), event.getY());
        gc.stroke();
    }

    private void onMouseDragged(MouseEvent event) {
        gc.lineTo(event.getX(), event.getY());
        gc.stroke();
    }
    
    private void onMouseReleased(MouseEvent event) {
        gc.lineTo(event.getX(), event.getY());
        gc.stroke();
        gc.closePath();
        saveCanvasToImage();
    }

    private void saveCanvasToImage() {
        WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, writableImage);
        imageView.setImage(writableImage);
    }

    public void saveImage(Image image) {
        imgOriginal2 = imageView.getImage();
    }

    public void loadImage(Image image) {
        imageView.setImage(image);
    }
}