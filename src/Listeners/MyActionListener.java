package Listeners;

import Frame.CoreFrame;
import Updater.CoreUpdater;

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
    public MyActionListener(String path, CoreFrame cf){
        this.path=path;
        this.cf=cf;
    }

    public void actionPerformed(ActionEvent e) {

    	if(e.getActionCommand().equals("background")){
    		cf.autostart.setSelected(true);
    		
    	}
        if (e.getActionCommand().equals("me")){
            JOptionPane.showMessageDialog(null,"Check https://github.com/lithiumGN to stay updated", "About", JOptionPane.INFORMATION_MESSAGE);
        }
        if (e.getActionCommand().equals("with")){

            saveConfig(true);


            runGW2();


        }
        if (e.getActionCommand().equals("without")){
            saveConfig(false);
            CoreUpdater.runWithoutDPS(path);
            runGW2();

        }



    }

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
