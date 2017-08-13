package Listeners;

import Frame.CoreFrame;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyMouseListener implements MouseListener {
    public CoreFrame cf;
    public MyMouseListener(CoreFrame cf){ this.cf=cf;}

    public void mouseClicked(MouseEvent e) {
        cf.arg_string.setText("");
        cf.arg_string.setForeground(Color.BLACK);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
