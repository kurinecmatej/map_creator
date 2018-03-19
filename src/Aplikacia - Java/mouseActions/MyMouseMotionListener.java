package mouseActions;

import java.awt.event.MouseEvent;
import javax.swing.JLabel;

/**
 * Trieda na ziskavanie informacii o kurzore
 * @author Matej
 */
public class MyMouseMotionListener implements java.awt.event.MouseMotionListener{
    private final JLabel statusBar;
    
    /**
     * vytvorenie noveho objektu
     * @param statusBar vrstva, na ktorej sa budu informacie vypisovat
     */
    public MyMouseMotionListener(JLabel statusBar){
        this.statusBar = statusBar;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        String text = "   ";
        text += "X,Y: " + (e.getPoint().x+1) + "," + (e.getPoint().y+1) + " px";
        statusBar.setText(text);
    }
    
}
