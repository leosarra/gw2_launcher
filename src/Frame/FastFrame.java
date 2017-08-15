package Frame;

import javax.swing.*;

import com.lithium.launcher.Main;

import framework.Operations;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@SuppressWarnings("serial")
public class FastFrame extends JFrame{


	private JButton startwith= new JButton("Run with ArcDPS");
	private JButton startwithout= new JButton("Run only GW2 ");
    public JLabel status = new JLabel ("    Updater is starting...");
    private JLabel path= new JLabel("   Guild Wars 2 executable found");
	private JLabel arg=new JLabel("Arguments: ");
	private JCheckBox autostart= new JCheckBox("Start with these settings each time");
    public JTextField arg_string=new JTextField(20);
	private String path_string;
    public JButton arc= new JButton("Install ArcDPS");
    public JButton bgdm= new JButton("Install BGDM");
	private String mode;

	
	




    public FastFrame(String dir, boolean hide){
        super("Guild Wars 2 Launcher");
        //Settings of the FastFrame object 
        
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(310,160);
        if (!hide) {
        	this.setVisible(true); //if background option is selected the frame will not show up
        }
        
        //JPanel setup 
        JPanel grid=new JPanel(new GridLayout(2,1));
        grid.setBorder(BorderFactory.createTitledBorder("Status"));
        
        //Color setup
        status.setForeground(Color.orange);
        path.setForeground(new Color(0, 102, 51));
        
        //Add elements to the JPanel and FastFrame
        this.add(grid);
        grid.add(path);
        grid.add(status);




    }
    
    public void setMode(String mode) {
    	this.mode=mode;
    }
    
    public String getMode() { return mode;}
    
    
    
}
