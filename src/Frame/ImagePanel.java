package Frame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("serial")
public class ImagePanel extends JFrame{

    public ImagePanel() {
        try {
            ImageIO.read(new File("GW2pof.jpg"));
            System.out.println("Immagine trovata");
        } catch (IOException ex) {
           System.out.println("Errore");
        }
    }



}
