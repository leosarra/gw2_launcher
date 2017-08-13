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
	private JButton startwith= new JButton("Run with ArcDPS");
    private JButton startwithout= new JButton("Run only GW2 ");
    public JLabel status = new JLabel ("    Updater is starting...");

    private JLabel path= new JLabel("   Guild Wars 2 executable found");
    private JLabel arg=new JLabel("Arguments: ");
    public JCheckBox autostart= new JCheckBox("Start with these settings each time (Fast-start)");
    public JCheckBox background= new JCheckBox("Hide the GUI when fast-start is activated ");
    public JTextField arg_string=new JTextField(20);
    private String path_string;
    private JButton me= new JButton("?");




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
        
        //Change look and feel of the Swing JFrame
        try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			
			e.printStackTrace();
		}
        
        //Settings of the CoreFrame object 
        this.setSize(320,270);
        this.setVisible(true);
        this.setLayout(new GridLayout(1,2));
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        
        
        //Default settings for some elements of the CoreFrame
        arg_string.setText("Example: -autologin, -noaudio, -bmp ");
        arg_string.setForeground(Color.GRAY);
        status.setForeground(new Color(204,102,0));
        arg_string.setSize(3,5);
        path.setForeground(new Color(0,102,51));
        
        //Setting up the action commands to be used by the listeners
        startwith.setActionCommand("with");
        startwithout.setActionCommand("without");
        me.setActionCommand("me");
        background.setActionCommand("background");
        
        
        //Create MyActionListener
        MyActionListener mal= new MyActionListener(path_string,this);
        
        //Add Listeners
        startwith.addActionListener(mal);
        startwithout.addActionListener(mal);
        me.addActionListener(mal);
        background.addActionListener(mal);
        arg_string.addMouseListener(new MyMouseListener(this));

        //Creation of multiple JPanel
        JPanel top =new JPanel (new GridLayout(2,1));
        JPanel bot = new JPanel(new FlowLayout());
        JPanel mid= new JPanel(new GridLayout(5,1));
        JPanel sel=new JPanel((new FlowLayout(FlowLayout.LEADING)));
        JPanel sel2=new JPanel((new FlowLayout(FlowLayout.LEADING)));
        JPanel main1 =new JPanel (new BorderLayout());
        JPanel about= new JPanel(new BorderLayout());

        //Add elements to the JPanels already created
        about.add(me,BorderLayout.EAST);
        sel.add(status);
        sel2.add(path);
        top.add(sel2);
        top.add(sel);
        mid.add(arg);
        mid.add(arg_string);
        mid.add(autostart);
        mid.add(background);
        mid.add(about);
        bot.add(startwith);
        bot.add(startwithout);
        main1.add(top,BorderLayout.NORTH);
        main1.add(bot,BorderLayout.SOUTH);
        main1.add(mid,BorderLayout.CENTER);
        this.add(main1);

        
        //Creating borders
        top.setBorder(BorderFactory.createTitledBorder("Status"));
        mid.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Settings"),
                        BorderFactory.createEmptyBorder(5,5,5,5)));

        
    }


}
