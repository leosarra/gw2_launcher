package chooser;

import framework.Operations;
import framework.Task;
import updater.CoreUpdater;

import javax.swing.*;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DirChooser implements Task {
    private JFileChooser f=new JFileChooser();
    private boolean cancel=false;

    private boolean fired=false;

    private static Logger log = Logger.getLogger( CoreUpdater.class.getName() );
    
    public void esegui() {
    	//Functor pattern
    	Operations.LogSetup(log,false);
        if (!fired) {


            boolean found = false;

            fired = true;            
            f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            f.setDialogTitle("Select Guild Wars 2 directory");
            //Workaround to add an icon in the JFileChooser dialog
            JFrame icon= new JFrame();
            icon.setLocationRelativeTo(null);
            icon.setUndecorated(true);
            icon.setVisible(true);
            icon.setIconImage(Toolkit.getDefaultToolkit().getImage(DirChooser.class.getResource("/gw2_64_1-1.png")));
            icon.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            //Loop until "Cancel" is pressed or a valid directory is selected
            while (!found) {
                //int input = f.showDialog(icon, "Select");
            	int input = f.showDialog(icon, "Select");
                if (input == JFileChooser.APPROVE_OPTION) {
                    File file = f.getSelectedFile();
                    
                    boolean check = DirChooser.validDir(file.getAbsolutePath());
                    if (check) {
                        found = true;
                        log.log( Level.INFO,"Valid dir selected: "+file.getAbsolutePath());
                        changePathProp(file.getAbsolutePath());
                    } else {
                    	log.log( Level.INFO,"Invalid dir selected: "+file.getAbsolutePath());
                        JOptionPane.showMessageDialog(null, "Executable not found. Please select a valid directory", "Directory not valid", JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    cancel = true;
                    System.exit(0);
                }


            }
            Operations.closeLogHandlers(log);
        }
    }


    //Check if a given path is a valid path for GW2
    public static boolean validDir(String path){
        boolean result=new File(path+"\\Gw2-64.exe").exists();
        return result;
    }
    //setter and getter required for the functor pattern
    public boolean getCancel() { return cancel;}

    public JFileChooser getJFileChooser() { return f;}

    public boolean isFired() {
        return fired;
    }

    public void changePathProp(String path){
         Properties prop = new Properties();
         InputStream input= null;

         try {

             input = new FileInputStream("gw2_launcher.cfg");
             //Import settings
             prop.load(input);
             input.close();

         } catch (FileNotFoundException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         
        OutputStream output= null;
        prop.put("path", path);
        try {

            output = new FileOutputStream("gw2_launcher.cfg");
            prop.store(output, "Config file for GW2 Launcher");
            output.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
