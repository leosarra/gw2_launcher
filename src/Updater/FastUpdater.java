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
    @SuppressWarnings("unused")
	private File old_dll;
    private int type;
    public FastUpdater(FastFrame cf, String path,int type){
        this.cf=cf;
        this.path=path;
        dll=new File(path+"\\bin64\\d3d9.dll");
        old_dll= new File(path+"\\bin64\\d3d9_old.dll");
        this.type=type;
    }


    public void run() {
        boolean check=dll.exists();
        File old_dll= new File(path+"\\bin64\\d3d9_old.dll");
        File ini=new File(path+"\\bin64\\arcdps.ini");

       // System.out.println("Controllo esistenza");
        if (check){
            updateDll();
            runGW2Fast(type);
        }
        if(!ini.exists()) {
        	int dialogButton = 0;
        	JOptionPane.showConfirmDialog(null,"ArcDPS configuration file not found. Would you like to download a default configoration?","ArcDPS configuration file not detected",dialogButton);
        	if (dialogButton==0){
        		downloadINI();
        	}
        	
        }

        if (!check && old_dll.exists()){
            try {
                Files.copy(old_dll.toPath(), dll.toPath());
                old_dll.delete();
            } catch (IOException e) {
                e.printStackTrace();
                cf.status.setText("  Cannot connect to the update server");
                cf.status.setForeground(Color.RED);
            }
            updateDll();
            runGW2Fast(type);


        }

        if (!check && !old_dll.exists()){
            int dialogButton = JOptionPane.YES_NO_OPTION;
            JOptionPane.showConfirmDialog(null,"ArcDPS not installed. Would you like to install ArcDPS?","ArcDPS not detected",dialogButton);
            if (dialogButton==0){
                try {
                    dll.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    cf.status.setText("  Cannot connect to the update server");
                    cf.status.setForeground(Color.RED);
                }
                downloadINI();
                updateDll();
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
    	if (ini.exists()) ini.delete();
    	System.out.println("Downloading configuration file");
    	try {
			FileUtils.copyURLToFile(new URL("http://www.deltaconnected.com/arcdps/x64/arcdps.ini"),ini, 10000, 10000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cf.status.setText("  Cannot connect to the update server");
            cf.status.setForeground(Color.RED);
		}

    }
    

    @SuppressWarnings("deprecation")
	public void updateDll(){

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(dll);
            byte data[] = new byte[0];
            data = org.apache.commons.codec.digest.DigestUtils.md5(fis);
            char md5Chars[] = Hex.encodeHex(data);
            md5_old = String.valueOf(md5Chars);

            File md5_download= new File(path+"\\bin64\\arcdps.dll.md5sum");
            FileUtils.copyURLToFile(new URL("http://www.deltaconnected.com/arcdps/x64/d3d9.dll.md5sum"),md5_download, 10000, 10000);
            md5_new=FileUtils.readFileToString(md5_download).substring(0, FileUtils.readFileToString(md5_download).indexOf(" "));
            System.out.println(md5_old);
            System.out.println(md5_new);

            if(!md5_old.equals(md5_new)){
                System.out.println("New file disponibile");
                File backup = new File(path+"\\bin64\\d3d9_old.dll");
                if (backup.exists()) backup.delete();
                Files.copy(dll.toPath(), backup.toPath());
                dll.delete();
                System.out.println(backup.exists());
                cf.status.setText("     Downloading new version...");
                FileUtils.copyURLToFile(new URL("http://www.deltaconnected.com/arcdps/x64/d3d9.dll"),dll, 10000, 10000);
                cf.status.setText(" ArcDPS updated");
                cf.status.setForeground(new Color(0,102,51));
            }

            else {
                cf.status.setText("    ArcDPS is already updated");
                cf.status.setForeground(new Color(0, 102, 51));
            }
            md5_download.delete();




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

        if (type==0) runWithoutDPS(path);
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


    public static void runWithoutDPS(String path){
        File dll= new File(path+"\\bin64\\d3d9.dll");
        if (dll.exists()){
            System.out.println("Rimuovo");
            File old = new File(path+"\\bin64\\d3d9_disabled.dll");
            if (old.exists()) old.delete();
            try {
                Files.copy(dll.toPath(), old.toPath());
                dll= new File(path+"\\bin64\\d3d9.dll");
                dll.delete();
                System.out.println(dll.exists());
            } catch (IOException e) {
                e.printStackTrace();
                errorDialog(path);


            }


        }
    }


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
