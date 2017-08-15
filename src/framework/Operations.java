package framework;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JOptionPane;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;

import Frame.CoreFrame;
import Updater.CoreUpdater;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class Operations {
	private static final boolean DEBUG = true;
	
	public static void LogSetup(Logger log) {
		if (DEBUG) {
			FileHandler fh = null;
			try {
				fh = new FileHandler("gw2_launcher_debug.txt", true);
			} catch (SecurityException | IOException e1) {
				e1.printStackTrace();
			}   
			SimpleFormatter sf = new SimpleFormatter();
			fh.setFormatter(sf);
			log.addHandler(fh);
			log.setLevel(Level.ALL);
		}
	}
	
	public static void closeLogHandlers(Logger log) {
		if (DEBUG) {
			for (Handler e: log.getHandlers()) {
				e.close();
			}
		}
	}
	
	public static void cleanOldLogger() {
		if (DEBUG) {
			File fl=new File("gw2_launcher_debug.txt");
			if (fl.exists()) fl.delete();
		}
		
		
	}
	
	
	public static void installArc(CoreFrame cf, String path) {
    	int dialogButton = JOptionPane.YES_NO_OPTION;
    	File dll=new File(path+"\\bin64\\d3d9.dll");
    	try {
			dll.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println("Installing Arc [InstallARC]");
        
    	Operations.downloadINI(cf,path); //.ini is required for the first install
    	Operations.updateDll(cf,path); //placeholder swapped with the last version of the dll
        //Change status and color of JLabel status
        //log.log( Level.INFO,"ArcDPS installed succesfully");
        cf.setMode("arc_only");
		cf.status.setText("- ArcDPS was installed successfully");
		cf.status.setForeground(new Color(0,102,51));
        
	}
    
	
	public static void renameBGDMinstallArc(CoreFrame cf, String path) {
    	File dll=new File(path+"\\bin64\\d3d9.dll");
    	File chainload=new File(path+"\\bin64\\d3d9_chainload.dll");
    	if (chainload.exists()) chainload.delete();
    	try {
			Files.copy(dll.toPath(), chainload.toPath());
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
        Operations.downloadINI(cf,path); //.ini is required for the first install
        Operations.updateDll(cf,path); //placehold swapped with the last version of the dll
        
            //Change status and color of JLabel status
         //   log.log( Level.INFO,"ArcDPS installed succesfully");
		cf.setMode("both");
		cf.status.setText("- ArcDPS was installed successfully");
		cf.status.setForeground(new Color(0,102,51));;
        }
	
	public static void removeArcRenameBGDM(CoreFrame cf, String path) {
		File dll=new File(path+"\\bin64\\d3d9.dll");
		File ini= new File(path+"\\bin64\\arcdps.ini");
		File chainload=new File(path+"\\bin64\\d3d9_chainload.dll");
		if (dll.exists()) dll.delete();
		if(ini.exists()) ini.delete();
		try {
			Files.copy(chainload.toPath(), dll.toPath());
			cf.setMode("bgdm_only");
			cf.status.setText("- ArcDPS not installed");
			cf.status.setForeground(Color.RED);
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		
		
	}
	
	
	public static void removeArc(CoreFrame cf, String path) {
		System.out.println("Removing arc [removeArc]");
		File dll=new File(path+"\\bin64\\d3d9.dll");
		File dll_old=new File(path+"\\bin64\\d3d9_old.dll");
		File ini= new File(path+"\\bin64\\arcdps.ini");
		if (dll.exists()) {
			System.out.println("cancello dll");
			dll.delete();
		}
		if (dll_old.exists()) {
			System.out.println("cancello dll_old");
			dll_old.delete();
		}
		if(ini.exists()) ini.delete();
		
		cf.setMode("none");
		cf.status.setText("- ArcDPS not installed");
		cf.status.setForeground(Color.RED);
		
		
	}
        

	public static void removeBGDM(CoreFrame cf, String path) {
		File dll=new File(path+"\\bin64\\d3d9_chainload.dll");
		File dll_2= new File(path+"\\bin64\\bgdm.dll");
		if (dll.exists()) dll.delete();
		if(dll_2.exists()) dll_2.delete();
		cf.bgdm_label.setText("- BGDM not installed");
		cf.bgdm_label.setForeground(Color.RED);
		cf.setMode("none");
		
	}
	
	public static void installBGDMwithArc(CoreFrame cf, String path) {
		File download = new File(path+"\\bgdm.zip");
		try {
			FileUtils.copyURLToFile(new URL("https://goo.gl/VvhLcx"),download, 10000, 10000);
			ZipFile zipFile=new ZipFile(path+"\\bgdm.zip");
			zipFile.extractAll(path);
			File dll_config=new File(path+"\\bgdm.dll");
			File dll_destination=new File(path+"\\bin64\\bgdm.dll");
			if (dll_destination.exists()) dll_destination.delete();
			Files.copy(dll_config.toPath(), dll_destination.toPath());
			
			File d3d9=new File(path+"\\d3d9.dll");
			File d3d9_dest=new File(path+"\\bin64\\d3d9_chainload.dll");
			if (d3d9_dest.exists()) d3d9_dest.delete();
			Files.copy(d3d9.toPath(), d3d9_dest.toPath());
			
			File d3d9_old=new File(path+"\\d3d9.dll");
			if (d3d9.exists()) d3d9.delete();
			File bgdm_old=new File(path+"\\bgdm.dll");
			if (bgdm_old.exists()) bgdm_old.delete();
			if(download.exists()) download.delete();
			//Change JLabel's text + color
			cf.bgdm_label.setText(" - BGDM was installed successfully");
			cf.bgdm_label.setForeground(new Color(0,102,51));
			cf.setMode("both");
			

			
		} catch (IOException | ZipException e) {
			// TODO Auto-generated catch block
			cf.bgdm_label.setText("- Cannot download BGDM, check your internet");
			cf.bgdm_label.setForeground(Color.RED);
			e.printStackTrace();
		}
		
		
	}
	

	public static void installBGDM(CoreFrame cf, String path) {
		File download = new File(path+"\\bgdm.zip");
		try {
			//download bgdm from server
			FileUtils.copyURLToFile(new URL("https://goo.gl/VvhLcx"),download, 10000, 10000);
			ZipFile zipFile=new ZipFile(path+"\\bgdm.zip");
			zipFile.extractAll(path); //extract zip
			File dll_config=new File(path+"\\bgdm.dll");
			File dll_destination=new File(path+"\\bin64\\bgdm.dll");
			if (dll_destination.exists()) dll_destination.delete();
			Files.copy(dll_config.toPath(), dll_destination.toPath());
			System.out.println("BGDM zip downloaded");
			File d3d9=new File(path+"\\d3d9.dll");
			File d3d9_dest=new File(path+"\\bin64\\d3d9.dll");
			if (d3d9_dest.exists()) d3d9_dest.delete();
			Files.copy(d3d9.toPath(), d3d9_dest.toPath());
			
			//cleanup
			File d3d9_old=new File(path+"\\d3d9.dll");
			if (d3d9.exists()) d3d9.delete();
			File bgdm_old=new File(path+"\\bgdm.dll");
			if (bgdm_old.exists()) bgdm_old.delete();

			if(download.exists()) download.delete();
			
			//Change JLabel text and status
			cf.bgdm_label.setText(" - BGDM was installed successfully");
			cf.setMode("bgdm_only");
			cf.bgdm_label.setForeground(new Color(0,102,51));
			
		} catch (IOException | ZipException e) {
			// TODO Auto-generated catch block
			cf.bgdm_label.setText("- Cannot download BGDM, check your internet");
			cf.bgdm_label.setForeground(Color.RED);
			e.printStackTrace();
		}
		
		
		
		
	}
	
    
	
	
	public static void downloadINI(CoreFrame cf, String path){

    	File ini=new File(path+"\\bin64\\arcdps.ini");
    	if (ini.exists()) ini.delete();     	//Delete existing ini file to prevent an exception
    	//log.log( Level.INFO,"Downloading configuration file");
    	try {
    		//Download default configuration from the website
			FileUtils.copyURLToFile(new URL("http://www.deltaconnected.com/arcdps/x64/arcdps.ini"),ini, 10000, 10000);
			//log.log( Level.INFO,"archdps.ini installed successfully");
		//Exceptions if something goes wrong (Connection/IO)
    	} catch (IOException e) {
			
			e.printStackTrace();
			cf.status.setText("- Cannot connect to the update server");
            cf.status.setForeground(Color.RED);
            //log.log( Level.SEVERE,"IOException when downloading ini");
		}

    }
	
	
	
	public static void updateDll(CoreFrame cf, String path){
		File dll=new File(path+"\\bin64\\d3d9.dll");
        FileInputStream fis = null;
        String md5_new;
        String md5_old;
        try {
        	
        	//Generate md5 file of the dll installed in the system using some external libraries
        	
            fis = new FileInputStream(dll);
            byte data[] = new byte[0];
            data = org.apache.commons.codec.digest.DigestUtils.md5(fis);
            char md5Chars[] = Hex.encodeHex(data);
            md5_old = String.valueOf(md5Chars); //md5 of the dll

            File md5_download= new File(path+"\\bin64\\arcdps.dll.md5sum"); //Path of the md5 that is going to be downloaded
            //Download the md5 of the last version of ArcDPS from the website
            FileUtils.copyURLToFile(new URL("http://www.deltaconnected.com/arcdps/x64/d3d9.dll.md5sum"),md5_download, 10000, 10000); 
            //Keep the relevant part of the md5
            //log.log( Level.INFO,"Md5 downloaded");
            md5_new=FileUtils.readFileToString(md5_download).substring(0, FileUtils.readFileToString(md5_download).indexOf(" "));
            //log.log( Level.INFO,"Old md5: "+md5_old);
            //log.log( Level.INFO,"New md5: "+md5_new);

            if(!md5_old.equals(md5_new)){ //Different checksum means that a new version must be downloaded
            	//log.log( Level.INFO,"New version available");
                File backup = new File(path+"\\bin64\\d3d9_old.dll");
                if (backup.exists()) backup.delete();  //delete old backup to prevent an exception
                //Create backup copy
                Files.copy(dll.toPath(), backup.toPath());
                //Delete old copy
                dll.delete();
                //Change text of the JLabel status
                cf.status.setText("- Downloading new version...");
                //Download new dll
                FileUtils.copyURLToFile(new URL("http://www.deltaconnected.com/arcdps/x64/d3d9.dll"),dll, 10000, 10000);
                cf.status.setText(" ArcDPS updated");
                cf.status.setForeground(new Color(0,102,51));
            }

            else { //Same checksum means that the user has the most recent version of ArcDPS
            	//log.log( Level.INFO,"ArcDPS already updated");
                cf.status.setText("- ArcDPS is already updated");
                cf.status.setForeground(new Color(0, 102, 51));
            }
            //Delete downloaded md5
            //log.log( Level.INFO,"Removing downloaded md5");
            md5_download.delete();
            fis.close();




        //Exceptions if something goes wrong (Connection/IO)
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            cf.status.setText("  Cannot connect to the update server");
            cf.status.setForeground(Color.RED);
            //log.log( Level.SEVERE,"FileNotFoundException when downloading dll");
        } catch (IOException e) {
            e.printStackTrace();
            cf.status.setText("  Cannot connect to the update server");
            cf.status.setForeground(Color.RED);
            //log.log( Level.SEVERE,"IOException when downloading dll");
        }

    }


	
	
	

}
