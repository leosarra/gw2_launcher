package listeners;

import frame.CoreFrame;
import framework.Operations;
import updater.CoreUpdater;
import javax.swing.*;
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
    		
    		if(cf.getMode().equals("none")) {
    			Operations.installArc(cf, path);
    			saveConfig(true);
    			cf.btempl.setEnabled(true);
        		cf.startwith.setEnabled(true);
        		cf.autostart.setEnabled(true);
        		cf.background.setEnabled(true);
        		cf.startwith.setEnabled(true);
    		}
    		else {
    			int dialogButton=JOptionPane.YES_NO_OPTION;
    			int dialogResult=JOptionPane.showConfirmDialog(null,"Would you like to remove ArcDPS' settings files too? \n"
    			 		+ "If you are going to install ArcDPS in the future press 'No'. ","Remove settings?",dialogButton);
    	            if (dialogResult==JOptionPane.YES_OPTION){
    	                Operations.removeArcSetting(cf, path);
    	            }
    			Operations.removeArc(cf, path);
    			saveConfig(true);
    			cf.btempl.setText("Install Buildtemplates");
    			cf.btempl.setEnabled(false);
    			cf.startwith.setEnabled(false);
    			cf.autostart.setEnabled(false);
        		cf.background.setEnabled(false);
    		}


    	}
    	
    	if(e.getActionCommand().equals("btempl_install")) {
    		if(Operations.installBTempl(cf, path)==0) {
    			cf.btempl.setText("Remove Buildtemplates");
    			cf.btempl.setActionCommand("btempl_remove");
    		}
    		
    	}
    
    	if(e.getActionCommand().equals("btempl_remove")) {
    		if (Operations.removeBTempl(cf,path)==0) {
    			cf.btempl.setText("Install Buildtemplates");
    			cf.btempl.setActionCommand("btempl_install");
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
            cf.dispose();
            System.exit(0);
        }
    }

    //Used to the save settings in "gw2_launcher.cfg"
    public void saveConfig(boolean useAddons){
        Properties prop = new Properties();
        OutputStream output= null;
        prop.put("path",path);
        Operations.loadProp(prop,"gw2_launcher.cfg");
        
        if (cf.autostart.isSelected()) {
            prop.put("faststart", "yes");
        }
        else prop.put("faststart","no");

        if (useAddons==true) prop.put("useAddons","yes");
        else prop.put("useAddons","no");

        if(cf.background.isSelected())  prop.put("background", "yes");
        else prop.put("background", "no");
        
        prop.put("mode", cf.getMode());
        
        if(cf.arg_string.getText().contains("Example")) prop.put("args","");
        else prop.put("args",cf.arg_string.getText());
        Operations.saveProp(prop,"gw2_launcher.cfg");
    }
    
    @SuppressWarnings("unused")
    //Create process Gw2-64.exe with some arguments
	public void runGW2(){
        try {
        	if(cf.arg_string.getText().contains("Example")) cf.arg_string.setText("");
            cf.dispose();
            List<String> list= Arrays.asList(cf.arg_string.getText().split("\\s*,\\s*"));
            LinkedList<String> exe= new LinkedList<>(list);
            System.out.println(list);
            exe.addFirst(path+"\\Gw2-64.exe");
            Process process = new ProcessBuilder(exe).start();
            System.exit(0);
        } catch (IOException e1) {
            e1.printStackTrace();
            System.exit(1);
        }

    }
}
