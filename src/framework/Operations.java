package framework;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Operations {
	
	public static void LogSetup(Logger log) {
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
	
	public static void closeLogHandlers(Logger log) {
		for (Handler e: log.getHandlers()) {
			e.close();
		}
	}
	
	public static void cleanOldLogger() {
		File fl=new File("gw2_launcher_debug.txt");
		if (fl.exists()) fl.delete();
		
		
	}

}
