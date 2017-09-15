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
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;

import com.lithium.launcher.Main;

import Frame.CoreFrame;


public class Operations {
	private static final boolean DEBUG = false;
	static Logger log = Logger.getLogger( Main.class.getName() );
	
	
	public static void LogSetup(Logger log, boolean operations) {
		if (DEBUG) {
			FileHandler fh = null;
			try {
				if (operations) fh = new FileHandler("gw2_launcher_log_op.txt", true);
				else fh = new FileHandler("gw2_launcher_log.txt", true);
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
			File fl=new File("gw2_launcher_log.txt");
			File fl2=new File("gw2_launcher_log_op.txt");
			if (fl.exists()) fl.delete();
			if (fl2.exists()) fl2.delete();
		}
		
		
	}
	
	
	public static synchronized void installArc(CoreFrame cf, String path) {
    	File dll=new File(path+"\\bin64\\d3d9.dll");
    	try {
			dll.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Operations.downloadINI(cf,path); //.ini is required for the first install
    	Operations.updateDll(cf,path); //placeholder swapped with the last version of the dll
    	File backup = new File(path+"\\bin64\\d3d9_old.dll");
    	if (backup.exists()) backup.delete();
    	try {
			Files.copy(dll.toPath(), backup.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
        //Change status and color of JLabel status
        //log.log( Level.INFO,"ArcDPS installed succesfully");
        cf.setMode("arc_only");
        log.log( Level.INFO, "Arc Installed [InstallARC]");
		cf.status.setText("- ArcDPS was installed successfully");
		cf.status.setForeground(new Color(0,102,51));
		
        
	}
    
	public static synchronized void installLoaderReshade(String path) {
		Operations.LogSetup(log,true);
		log.log( Level.INFO, "Installing reshade support (loader) [installLoaderReshade]");
		File download = new File(path+"\\bin64\\d3d9_chainload.dll");
		if (!download.exists()) {
		try {
			FileUtils.copyURLToFile(new URL("http://www.deltaconnected.com/arcdps/x64/reshade_loader/d3d9_chainload.dll"),download, 10000, 10000);
		 log.log( Level.INFO, "Everything went smooth [installBGDMwithArc]");

		 } catch (IOException e) {
	    // TODO Auto-generated catch block
		 log.log( Level.INFO, "IO/Zip Exception [installLoaderReshade]");
		 e.printStackTrace();
		 }
		}
		Operations.closeLogHandlers(log);
				
	}
		
	//Delete d3d9.dll of Arc
	public static synchronized void removeArc(CoreFrame cf, String path) {
		Operations.LogSetup(log,true);
		log.log( Level.INFO, "Removing arc [removeArc]");
		File dll=new File(path+"\\bin64\\d3d9.dll");
		File dll_old=new File(path+"\\bin64\\d3d9_old.dll");
		File dll_disabled=new File(path+"\\bin64\\d3d9_disabled.dll");
		File reshadeLoader = new File(path+"\\bin64\\d3d9_chainload.dll");
		File btempl= new File(path+"\\bin64\\d3d9_arcdps_buildtemplates.dll");
		File ini= new File(path+"\\bin64\\arcdps.ini");
		if (dll.exists()) {
			dll.delete();
		}
		if (dll_old.exists()) {
			dll_old.delete();
		}
		if (dll_disabled.exists()) dll_disabled.delete();
		if (reshadeLoader.exists()) reshadeLoader.delete();
		if (btempl.exists()) btempl.delete();
			
		if(ini.exists()) ini.delete();
		log.log( Level.INFO, "Everything went smooth [removeArc]");
		cf.setMode("none");
		cf.status.setText("- ArcDPS not installed");
		cf.status.setForeground(Color.RED);
		Operations.closeLogHandlers(log);
		
		
	}

	public static synchronized void removeReshadeLoader(String path) {
		System.out.println("entro");
		File reshadeLoader = new File(path+"\\bin64\\d3d9_chainload.dll");
		if (reshadeLoader.exists()) reshadeLoader.delete();
		
		
	}

	public static synchronized void downloadINI(CoreFrame cf, String path){
		Operations.LogSetup(log,true);
    	File ini=new File(path+"\\bin64\\arcdps.ini");
    	if (ini.exists()) ini.delete();     	//Delete existing ini file to prevent an exception
    	log.log( Level.INFO,"Downloading configuration file");
    	try {
    		//Download default configuration from the website
			FileUtils.copyURLToFile(new URL("http://www.deltaconnected.com/arcdps/x64/arcdps.ini"),ini, 10000, 10000);
			log.log( Level.INFO,"archdps.ini installed successfully");
		//Exceptions if something goes wrong (Connection/IO)
    	} catch (IOException e) {
			
			e.printStackTrace();
			cf.status.setText("- Cannot connect to the update server");
            cf.status.setForeground(Color.RED);
            log.log( Level.SEVERE,"IOException when downloading ini");
		}
    	Operations.closeLogHandlers(log);
    }
	
	
	//method used to download the Buildtemplates' dll
	public static synchronized int installBTempl(CoreFrame cf, String path){
		Operations.LogSetup(log,true);
    	File btempl=new File(path+"\\bin64\\d3d9_arcdps_buildtemplates.dll");
    	if (btempl.exists()) btempl.delete();     	//Delete existing ini file to prevent an exception
    	log.log( Level.INFO,"Downloading Buildtemplates dll");
    	try {
    		//Download default configuration from the website
			FileUtils.copyURLToFile(new URL("http://www.deltaconnected.com/arcdps/x64/buildtemplates/d3d9_arcdps_buildtemplates.dll"),btempl, 10000, 10000);
			log.log( Level.INFO,"Buildtemplates funcionality installed successfully");
			cf.btempl.setText("Remove Buildtemplates");
		//Exceptions if something goes wrong (Connection/IO)
    	} catch (IOException e) {
			
			e.printStackTrace();
			cf.status.setText("- Cannot connect to the update server");
            cf.status.setForeground(Color.RED);
            log.log( Level.SEVERE,"IOException when downloading Buildtemplates dll");
            return -1;
		}
    	Operations.closeLogHandlers(log);
    	return 0;
    }
	
	
	//method used to remove the buildtemplates' dll
	public static synchronized int removeBTempl(CoreFrame cf, String path){
		Operations.LogSetup(log,true);
    	File btempl=new File(path+"\\bin64\\d3d9_arcdps_buildtemplates.dll");
    	if (btempl.exists()) btempl.delete();     	//Delete existing dll 
    	log.log( Level.INFO,"Buildtemplates dll removed");
    	Operations.closeLogHandlers(log);
    	return 0;
    }
	
	
	//used to update arcDPS
	public static synchronized void updateDll(CoreFrame cf, String path){
		Operations.LogSetup(log,true);
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

            fis.close();
            File md5_download= new File(path+"\\bin64\\arcdps.dll.md5sum"); //Path of the md5 that is going to be downloaded
            //Download the md5 of the last version of ArcDPS from the website
            FileUtils.copyURLToFile(new URL("http://www.deltaconnected.com/arcdps/x64/d3d9.dll.md5sum"),md5_download, 10000, 10000); 
            //Keep the relevant part of the md5
            log.log( Level.INFO,"Md5 downloaded");
            md5_new=FileUtils.readFileToString(md5_download).substring(0, FileUtils.readFileToString(md5_download).indexOf(" "));
            log.log( Level.INFO,"Old md5: "+md5_old);
            log.log( Level.INFO,"New md5: "+md5_new);

            if(!md5_old.equals(md5_new)){ //Different checksum means that a new version must be downloaded
            	log.log( Level.INFO,"New version available");
                File backup = new File(path+"\\bin64\\d3d9_old.dll");
                if (backup.exists()) backup.delete();  //delete old backup to prevent an exception
                //Create backup copy
                Files.copy(dll.toPath(), backup.toPath());
                //Delete old copy

                //Change text of the JLabel status
                cf.status.setText("- Downloading new version...");
                //Download new dll
                FileUtils.copyURLToFile(new URL("http://www.deltaconnected.com/arcdps/x64/d3d9.dll"),dll, 10000, 10000);
                cf.status.setText(" ArcDPS updated");
                cf.status.setForeground(new Color(0,102,51));
                File btempl=new File(path+"\\bin64\\d3d9_arcdps_buildtemplates.dll");
                if (btempl.exists()) {
                	Operations.closeLogHandlers(log);
                	Operations.installBTempl(cf, path);
                	Operations.LogSetup(log,true);
                }
                
            }

            else { //Same checksum means that the user has the most recent version of ArcDPS
            	log.log( Level.INFO,"ArcDPS already updated");
                cf.status.setText("- ArcDPS is already updated");
                cf.status.setForeground(new Color(0, 102, 51));
            }
            //Delete downloaded md5
            log.log( Level.INFO,"Removing downloaded md5");
            md5_download.delete();




        //Exceptions if something goes wrong (Connection/IO)
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            cf.status.setText("  Cannot connect to the update server");
            cf.status.setForeground(Color.RED);
            log.log( Level.SEVERE,"FileNotFoundException when downloading dll");
        } catch (IOException e) {
            e.printStackTrace();
            cf.status.setText("  Cannot connect to the update server");
            cf.status.setForeground(Color.RED);
            log.log( Level.SEVERE,"IOException when downloading dll");
        }
        
        Operations.closeLogHandlers(log);
        Operations.installLoaderReshade(path);

    }




	
	
	

}
