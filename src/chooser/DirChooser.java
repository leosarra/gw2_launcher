package chooser;

import helpers.LauncherHelper;
import updater.CoreUpdater;

import javax.swing.*;

import java.awt.Toolkit;
import java.io.File;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DirChooser {
    private JFileChooser f=new JFileChooser();
    private boolean cancel=false;
    private boolean fired=false;
    private static Logger log = Logger.getLogger( CoreUpdater.class.getName() );
    
    public void execute() {
    	//Functor pattern
    	LauncherHelper.LogSetup(log,false);
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
            LauncherHelper.closeLogHandlers(log);
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
        LauncherHelper.loadProp(prop,"gw2_launcher.cfg");
        OutputStream output= null;
        prop.put("path", path);
        LauncherHelper.saveProp(prop,"gw2_launcher.cfg");

    }
}
