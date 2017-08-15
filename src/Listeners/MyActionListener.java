package Listeners;

import Frame.CoreFrame;
import Updater.CoreUpdater;
import framework.Operations;

import javax.swing.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class MyActionListener implements ActionListener {
    private String path;
    private CoreFrame cf;
    
    //Constructor
    public MyActionListener(String path, CoreFrame cf){
        this.path=path;
        this.cf=cf;
    }

    public void actionPerformed(ActionEvent e) {

    	//if background checkbox is selected the autostart one will be selected automatically
    	if(e.getActionCommand().equals("arc")) {
    		if(cf.getMode().equals("bgdm_only")) {
    			
    			Operations.renameBGDMinstallArc(cf, path);
    			saveConfig(true);
    			
    		}
    		else if(cf.getMode().equals("both")) {
    			Operations.removeArcRenameBGDM(cf, path);

    			saveConfig(true);
    		}
    		else if(cf.getMode().equals("none")) {
    			Operations.installArc(cf, path);
    			saveConfig(true);
    		}
    		else {
    			Operations.removeArc(cf, path);
    			saveConfig(true);
    		}
    		
    		
    		cf.startwith.setEnabled(true);

    	}
    	
    	
    	
    	if(e.getActionCommand().equals("bgdm")) {
    		cf.startwith.setEnabled(true);
    		if(cf.getMode().equals("none")) {
    			Operations.installBGDM(cf,path);

    			saveConfig(true);
    		}
    		else if (cf.getMode().equals("bgdm_only")) {
    			Operations.removeBGDM(cf,path);
    			saveConfig(true);
    			
    		}
    		else if (cf.getMode().equals("arc_only")){
    			Operations.installBGDMwithArc(cf,path);
    			saveConfig(true);
    			
    			
    		}
    		else {
    			Operations.removeBGDM(cf,path);
    			cf.setMode("arc_only");
    			saveConfig(true);
    		}

    		
    	}

    	if(e.getActionCommand().equals("background")){
    		cf.autostart.setSelected(true);
    		
    	}
    	
    	//Show up "About me" Frame
        if (e.getActionCommand().equals("me")){
            JOptionPane.showMessageDialog(null,"Check https://github.com/lithiumSR to stay updated", "About", JOptionPane.INFORMATION_MESSAGE);
        }
        
        //"Run with archdps" button is pressed
        if (e.getActionCommand().equals("with")){

            saveConfig(true);


            runGW2();


        }
        //"Run only GW2" button is pressed
        if (e.getActionCommand().equals("without")){
            saveConfig(false);
            //disable arcdps dll
            CoreUpdater.runWithoutDPS(path);
            runGW2();

        }



    }

    //Used to the save settings in "gw2_launcher.cfg"
    public void saveConfig(boolean typeStart){

        Properties prop = new Properties();
        OutputStream output= null;
        prop.put("path",path);
        if (cf.autostart.isSelected()) {
            prop.put("faststart", "yes");
        }
        else prop.put("faststart","no");

        if (typeStart==true) prop.put("type","yes");
        else prop.put("type","no");

        if(cf.background.isSelected())  prop.put("background", "yes");
        else prop.put("background", "no");
        
        prop.put("mode", cf.getMode());
        
        if(cf.arg_string.getText().contains("Example")) cf.arg_string.setText("");
        prop.put("args",cf.arg_string.getText());
        try {

            output = new FileOutputStream("gw2_launcher.cfg");
            prop.store(output, "Config file for GW2 Launcher");


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    
    @SuppressWarnings("unused")
    //Create process Gw2-64.exe with some arguments
	public void runGW2(){
        try {

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
}
