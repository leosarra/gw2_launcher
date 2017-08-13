package Updater;

import Frame.FastFrame;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FastUpdater implements Runnable {

    private String md5_old;
    private String md5_new;
    private FastFrame cf;
    private String path;
    private  File dll;
	private File old_dll;
	private File disabled_dll;
    private int type; //0 or 1 accordingly to "gw2_settings.cfg"
    public FastUpdater(FastFrame cf, String path,int type){
        this.cf=cf;
        this.path=path;
        dll=new File(path+"\\bin64\\d3d9.dll"); //dll of ArcDPS
        old_dll= new File(path+"\\bin64\\d3d9_old.dll"); //backup dll of ArcDPS
        disabled_dll= new File(path+"\\bin64\\d3d9_disabled.dll"); //disabled dll of ArcDPS
        this.type=type;
    }


    public void run() {
        boolean check=dll.exists();
        File ini=new File(path+"\\bin64\\arcdps.ini"); //settings file of archdps. Needed to make ArcDPS works

       // System.out.println("Controllo esistenza");
        if (check){
        	//System.out.println("d3d9.dll exists");
            updateDll(); //if d3d9.dll exists check if update is needed
            runGW2Fast(type); //Game is ready to be launched
        }
        if(!ini.exists()) { //If ini file is not detected ask to the user if he would like to restore it with a default version from the website
        	int dialogButton = 0;
        	JOptionPane.showConfirmDialog(null,"ArcDPS configuration file not found. Would you like to download a default configoration?","ArcDPS configuration file not detected",dialogButton);
        	if (dialogButton==0){
        		downloadINI(); //Method used to download the .ini
        	}
        	
        }

        if (!check && old_dll.exists()){
        	//If d3d9.dll is not detected but a backup is found. Rename the backup and check for an update
            try {
                Files.copy(old_dll.toPath(), dll.toPath());
                old_dll.delete();
            } catch (IOException e) {
                e.printStackTrace();
              //Change status and color of JLabel status
                cf.status.setText("  Cannot connect to the update server");
                cf.status.setForeground(Color.RED);
            }
            updateDll();
            runGW2Fast(type); //Game is ready to be launched
 

        }

        if (!check && disabled_dll.exists()){ 
        	//If d3d9.dll is not detected but a backup is found. Rename the backup and check for an update
            try {

                Files.copy(disabled_dll.toPath(), dll.toPath());
                disabled_dll.delete();
            } catch (IOException e) {
                e.printStackTrace();
              //Change status and color of JLabel status
                cf.status.setText("  Cannot connect to the update server");
                cf.status.setForeground(Color.RED);
            }
            updateDll(); //check for update just in case
            runGW2Fast(type); //Game is ready to be launched

        }
        
        if (!check && !old_dll.exists()){
        	//if there is not d3d9.dll and no backup it means that ArcDPS is not installed
            int dialogButton = JOptionPane.YES_NO_OPTION;
            //Ask to the user if he would like to install ArcDPS
            JOptionPane.showConfirmDialog(null,"ArcDPS not installed. Would you like to install ArcDPS?","ArcDPS not detected",dialogButton);
            if (dialogButton==0){
                try {
                    dll.createNewFile(); //placeholder that is going to be updated by updateDll()
                } catch (IOException e) {
                	//Change status and color of JLabel status
                    e.printStackTrace();
                    cf.status.setText("  Cannot connect to the update server");
                    cf.status.setForeground(Color.RED);
                }
                downloadINI(); //.ini is required for the first install
                updateDll(); //placehold swapped with the last version of the dll
                
                //Change status and color of JLabel status
                cf.status.setText("  ArcDPS Installed succesfully");
                cf.status.setForeground(new Color(0,102,51));
                runGW2Fast(type);

            }
            else {

                cf.status.setText("  ArcDPS not installed");
                cf.status.setForeground(Color.RED);
            }
        }
    }

    
    public void downloadINI(){
    	File ini=new File(path+"\\bin64\\arcdps.ini");
    	if (ini.exists()) ini.delete(); //Delete existing ini file to prevent an exception
    	System.out.println("Downloading configuration file");
    	try {
    		//Download default configuration from the website
			FileUtils.copyURLToFile(new URL("http://www.deltaconnected.com/arcdps/x64/arcdps.ini"),ini, 10000, 10000);
			
			//Exceptions if something goes wrong (Connection/IO)
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cf.status.setText("  Cannot connect to the update server");
            cf.status.setForeground(Color.RED);
		}

    }
    

    @SuppressWarnings("deprecation") //Remove warning for a deprecated function
	public void updateDll(){

        FileInputStream fis = null;
        try {
        	//Generate md5 file of the dll installed in the system using some external libraries
        	
            fis = new FileInputStream(dll);
            byte data[] = new byte[0];
            data = org.apache.commons.codec.digest.DigestUtils.md5(fis);
            char md5Chars[] = Hex.encodeHex(data);
            md5_old = String.valueOf(md5Chars);

            File md5_download= new File(path+"\\bin64\\arcdps.dll.md5sum"); //Path of the md5 that is going to be downloaded
            //Download the md5 of the last version of ArcDPS from the website
            FileUtils.copyURLToFile(new URL("http://www.deltaconnected.com/arcdps/x64/d3d9.dll.md5sum"),md5_download, 10000, 10000);
            md5_new=FileUtils.readFileToString(md5_download).substring(0, FileUtils.readFileToString(md5_download).indexOf(" "));
            System.out.println(md5_old);
            System.out.println(md5_new);

            if(!md5_old.equals(md5_new)){ //Different checksum means that a new version must be downlaoded
                System.out.println("New file disponibile");
                File backup = new File(path+"\\bin64\\d3d9_old.dll");
                if (backup.exists()) backup.delete(); //delete old backup to prevent an exception
                //Create backup copy
                Files.copy(dll.toPath(), backup.toPath());
                //Delete old copy
                dll.delete();
                System.out.println(backup.exists());
                //Change text of the JLabel status
                cf.status.setText("     Downloading new version...");
                //Download new dll
                FileUtils.copyURLToFile(new URL("http://www.deltaconnected.com/arcdps/x64/d3d9.dll"),dll, 10000, 10000);
                cf.status.setText(" ArcDPS updated");
                cf.status.setForeground(new Color(0,102,51));
            }

            else { //Same checksum means that the user has the most recent version of ArcDPS
                cf.status.setText("    ArcDPS is already updated");
                cf.status.setForeground(new Color(0, 102, 51));
            }
           //Delete downloaded md5 now useless
            md5_download.delete();



        //Exceptions if something goes wrong (Connection/IO)
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            cf.status.setText("  Cannot connect to the update server");
            cf.status.setForeground(Color.RED);
            errorDialog(path);
        } catch (IOException e) {
            e.printStackTrace();
            cf.status.setText("  Cannot connect to the update server");
            cf.status.setForeground(Color.RED);
            errorDialog(path);
        }

    }




    @SuppressWarnings("unused")
	public void runGW2Fast(int type){

        if (type==0) runWithoutDPS(path); //if type==0 ArcDPS' dll must be disabled
        try {

        	//Create process with some arguments
            List<String> list= Arrays.asList(cf.arg_string.getText().split("\\s*,\\s*"));
            LinkedList<String> exe= new LinkedList<>(list);
            System.out.println(list);
            exe.addFirst(path+"\\Gw2-64.exe");
            Process process = new ProcessBuilder(exe).start();
            cf.dispose();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    public static void runWithoutDPS(String path){
        File dll= new File(path+"\\bin64\\d3d9.dll");
        if (dll.exists()){
            System.out.println("Rimuovo");
            File old = new File(path+"\\bin64\\d3d9_disabled.dll");
            if (old.exists()) old.delete(); //delete an older disabled dll to prevent an exception
            try {
                Files.copy(dll.toPath(), old.toPath()); //rename d3d9.dll to d3d9_disabled.dll
                dll= new File(path+"\\bin64\\d3d9.dll");
                dll.delete();
                System.out.println(dll.exists());
            } catch (IOException e) {
                e.printStackTrace();
                errorDialog(path);


            }


        }
    }

    //Error dialog needed for static methods
    public static void errorDialog(String path){
        int dialogButton = JOptionPane.YES_NO_OPTION;
        JOptionPane.showConfirmDialog(null,"Something went wrong. Check your internet connection. Would you like to run GW2 without ArcDPS?",
                "Updater failed",dialogButton);
        if (dialogButton==1) {
        	System.out.println("Eseguo senza ArcDPS");
            FastUpdater.runWithoutDPS(path);
        }

    }
}
