package com.lithium.launcher;

import Chooser.DirChooser;
import Frame.CoreFrame;
import Frame.FastFrame;
import Updater.CoreUpdater;
import Updater.FastUpdater;
import framework.Operations;
import framework.TaskExecutor;

import java.io.*;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
	

    public static void main(String[] args) throws InterruptedException {
    	
    	//Create Logger
    	Logger log = Logger.getLogger( Main.class.getName() );
    	Operations.cleanOldLogger();
    	Operations.LogSetup(log);
    	
    	//Create configuration file if it doesn't exist already
        File config= new File("gw2_launcher.cfg");
        if (!config.exists()) try {
            config.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

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



        TaskExecutor te=TaskExecutor.getInstance();
        String currentDir=new File(".").getAbsolutePath();

        //If the path contained in the settings is valid and faststart is not enabled use CoreFrame
        if (DirChooser.validDir(prop.getProperty("path")) && !prop.getProperty("faststart").equals("yes")) {
        	log.log( Level.INFO, "Found path, no autostart");
            CoreFrame gui = new CoreFrame(prop.getProperty("path"));
            //Import saved args to the CoreFrame
            gui.arg_string.setText(prop.getProperty("args",""));
            gui.setMode(prop.getProperty("mode","none"));
            Operations.closeLogHandlers(log);
            Thread t1 = new Thread(new CoreUpdater(gui, prop.getProperty("path")));
            t1.start();

        }
        //Else if the path contained in the settings is valid and faststart is enabled use FastFrame
        else if (DirChooser.validDir(prop.getProperty("path")) && prop.getProperty("faststart").equals("yes")){
        	log.log( Level.INFO, "Found path, yes autostart");
        	FastFrame gui=null;
        	
        	//Check if background option was previously selected. FastFrame changes accordingly 
            if(prop.getProperty("background").equals("yes")) {
            	log.log( Level.INFO, "Hide Fastframe is selected");
            	gui=new FastFrame(prop.getProperty("path"),true);
            	gui.setMode(prop.getProperty("mode"));
            }
            else {
            	log.log( Level.INFO, "Hide Fastframe is not selected");
            	gui=new FastFrame(prop.getProperty("path"),false);
            	gui.setMode(prop.getProperty("mode"));
            }
             //Import saved args to the CoreFrame
            gui.arg_string.setText(prop.getProperty("args",""));
            if(gui.arg_string.getText().contains("Example")) gui.arg_string.setText("");
            
            //Updater thread is created. If ("type".equals("yes") is yes it means that the preferred option for execution is "Run with ArcDPS" 
            //otherwise is "Run only GW2" ("type".equals("no").
            //Type is one of the settings contained in gw2_launcher.cfg
            Operations.closeLogHandlers(log);
            Thread t1=null;
            if (prop.getProperty("type").equals("yes")) { t1= new Thread(new FastUpdater(gui,prop.getProperty("path"),1));}
            else t1= new Thread(new FastUpdater(gui,prop.getProperty("path"),0));
            t1.start();

        }

        //Else If currentDir is valid we don't need to use the JFileChooser
        else if (DirChooser.validDir(currentDir)){
        	log.log( Level.INFO, "Path not found, no autostart, but valid current dir");
            CoreFrame gui = new CoreFrame(currentDir);
            Operations.closeLogHandlers(log);
            Thread t1 = new Thread(new CoreUpdater(gui, currentDir));
            t1.start();

        }

        //JFileChooser is needed
        else {
        	log.log( Level.INFO, "FileChooser needed");
        	Operations.closeLogHandlers(log);
            DirChooser dir=new DirChooser();
            
            te.perform(dir);
            if(!dir.getCancel() && dir.isFired()) {
                if (dir.getJFileChooser().getSelectedFile()==null) System.exit(0);
                CoreFrame gui = new CoreFrame(dir.getJFileChooser().getSelectedFile().getAbsolutePath());
                Thread t1 = new Thread(new CoreUpdater(gui, dir.getJFileChooser().getSelectedFile().getAbsolutePath()));
                t1.start();
            }

        }


    }
}
