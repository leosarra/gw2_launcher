package Chooser;

import framework.Task;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class DirChooser implements Task {
    private JFileChooser f=new JFileChooser();
    private boolean cancel=false;

    private boolean fired=false;

    public DirChooser(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(f);

    }
    public void esegui() {

        if (!fired) {


            boolean found = false;

            fired = true;

            
            
            
            JFrame icon= new JFrame();
            try {
				icon.setIconImage(ImageIO.read(new File("gw2_64_1-0.png")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            f.setDialogTitle("Select Guild Wars 2 directory");

            
            while (!found) {
                int input = f.showDialog(icon, "Select");

                if (input == JFileChooser.APPROVE_OPTION) {
                    File file = f.getSelectedFile();
                    System.out.println(file.toString());
                    boolean check = DirChooser.validDir(file.getAbsolutePath());
                    if (check) {
                        found = true;
                    } else {
                        JOptionPane.showMessageDialog(null, "Executable not found. Please select a valid directory", "Directory not valid", JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    cancel = true;
                    break;
                }


            }
        }
    }



    public static boolean validDir(String path){
        boolean result=new File(path+"\\Gw2-64.exe").exists();
        return result;
    }
    public boolean getCancel() { return cancel;}

    public JFileChooser getJFileChooser() { return f;}

    public boolean isFired() {
        return fired;
    }
}
