package Frame;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class FastFrame extends JFrame{

    @SuppressWarnings("unused")
	private JButton startwith= new JButton("Run with ArcDPS");
    @SuppressWarnings("unused")
	private JButton startwithout= new JButton("Run only GW2 ");
    public JLabel status = new JLabel ("    Updater is starting...");

    private JLabel path= new JLabel("   Guild Wars 2 executable found");
    @SuppressWarnings("unused")
	private JLabel arg=new JLabel("Arguments: ");
    @SuppressWarnings("unused")
	private JCheckBox autostart= new JCheckBox("Start with these settings each time");
    public JTextField arg_string=new JTextField(20);
    @SuppressWarnings("unused")
	private String path_string;





    public FastFrame(String dir, boolean hide){
        super("Guild Wars 2 Launcher");
        status.setForeground(Color.orange);
        path.setForeground(new Color(0, 102, 51));
        JPanel grid=new JPanel(new GridLayout(2,1));
        grid.setBorder(BorderFactory.createTitledBorder("Status"));
        this.add(grid);
        grid.add(path);
        grid.add(status);
        this.setLocationRelativeTo(null);
        this.setSize(310,160);
        if (!hide) this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    }
}
