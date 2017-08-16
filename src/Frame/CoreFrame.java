package Frame;

import Listeners.MyActionListener;
import Listeners.MyMouseListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class CoreFrame extends JFrame{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JButton startwith= new JButton("Run with ArcDPS");
    private JButton startwithout= new JButton("Run only Gw2");
    public JLabel status = new JLabel ("- Updater is starting...");

    private JLabel path= new JLabel("- Guild Wars 2 executable found");
    private JLabel arg=new JLabel("Arguments: ");
    public JCheckBox autostart= new JCheckBox("Start with these settings each time (Fast-start)");
    public JCheckBox background= new JCheckBox("Hide the GUI when fast-start is activated ");
    public JButton arc= new JButton("Install ArcDPS");
    public JTextField arg_string=new JTextField(20);
    private String path_string;
    private JButton me= new JButton("?");
    private String mode;




    public CoreFrame(String dir){
        super("Guild Wars 2 Launcher");
        path_string=dir;
        
        
        //Adding icon
        try {
			this.setIconImage(ImageIO.read(new File("gw2_64_1-0.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        //Settings of the CoreFrame object 
        this.setSize(325,373);
        this.setVisible(true);
        getContentPane().setLayout(new GridLayout(1,2));
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        arc.setEnabled(false);

        
        
        //Default settings for some elements of the CoreFrame
        arg_string.setText("Example: -autologin, -noaudio, -bmp ");
        arg_string.setForeground(Color.GRAY);
        status.setForeground(new Color(204,102,0));
        arg_string.setSize(3,5);
        path.setForeground(new Color(0,102,51));
        
        //Setting up the action commands to be used by the listeners
        startwith.setActionCommand("with");
        startwithout.setActionCommand("without");
        background.setActionCommand("background");
        arc.setActionCommand("arc");
        
        
        //Create MyActionListener
        MyActionListener mal= new MyActionListener(path_string,this);
        
        //Add Listeners
        arc.addActionListener(mal);
        startwith.addActionListener(mal);
        startwithout.addActionListener(mal);
        background.addActionListener(mal);
        arg_string.addMouseListener(new MyMouseListener(this));

        //Creation of multiple JPanel
        JPanel top =new JPanel (new GridLayout(2,1));
        JPanel bot = new JPanel(new FlowLayout());
        JPanel mid= new JPanel();
        JPanel sel=new JPanel((new FlowLayout(FlowLayout.LEADING)));
        JPanel sel2=new JPanel((new FlowLayout(FlowLayout.LEADING)));
        JPanel install=new JPanel(new FlowLayout());
        JPanel settings=new JPanel(new GridLayout(5,1));
        JPanel main1 =new JPanel (new BorderLayout());
        JPanel about= new JPanel(new BorderLayout());
        
        
        sel.add(status);
        sel2.add(path);
        install.add(arc);
        top.add(sel2);
        top.add(sel);
        mid.setLayout(new BorderLayout(0, 0));
        mid.add(install, BorderLayout.NORTH);
        settings.add(arg);
        settings.add(arg_string);
        settings.add(autostart);
        settings.add(background);
        mid.add(settings);
        bot.add(startwith);
        bot.add(startwithout);
        mid.add(about, BorderLayout.SOUTH);
        about.add(me,BorderLayout.EAST);
        

        main1.add(top,BorderLayout.NORTH);
        main1.add(bot,BorderLayout.SOUTH);
        main1.add(mid,BorderLayout.CENTER);
        getContentPane().add(main1);

        
        //Creating borders
        install.setBorder(BorderFactory.createTitledBorder("Setup"));
        top.setBorder(BorderFactory.createTitledBorder("Status"));
        settings.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Settings"),
                        BorderFactory.createEmptyBorder(5,5,5,5)));
        me.setActionCommand("me");
        me.addActionListener(mal);
        
        
       
                

        
    }
    
    public void setMode(String mode) {
    	this.mode=mode;
    	if (mode.equals("none")) {
    		arc.setText("Install ArcDPS");
    		status.setText("- ArcDPS is not installed");
    		status.setForeground(Color.RED);
    	}

    	else {
    		arc.setText("Remove ArcDPS");
    		
    	}
    	
    }
    
    
    public String getMode() { return mode;}


}
