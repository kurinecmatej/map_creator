package frames;

import arduino.Map;
import arduino.Tile;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;

/**
 * Trieda na vykreslenie mapy vo full screen mode
 * @author Matej
 */
public final class MapFrame extends JFrame {

    private final Tile[][] pole;
    private final int width;
    private final int height;
    private final Map map;
    private MainFrame JF;

    /**
     * vytvorenie novej mapy
     * @param fileLocation cesta k suboru s parametrami prostredia
     */
    public MapFrame(String fileLocation, MainFrame JF) {
        this.JF = JF;
        map = new Map(fileLocation);
        pole = map.getMapField();
        width = map.getWidth();
        height = map.getHeight();
        createImage();
    }

    /**
     * vytvorenie noveho okna na vykreslovanie
     */
    public void createImage() {
        setTitle("M A P");
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setBackground(Color.WHITE);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        paint(null);
    }

    @Override
    public void paint(Graphics g) {
        try{
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (pole[i][j] == Tile.VOLNE && JF.getFreeOn()) {
                        g.setColor(ColorEnum.FREE.getColor());
                        g.fillOval(j * 1, i * 1, 2, 2);
                    } else if (pole[i][j] == Tile.NEPRESKUMANE && JF.getUnexploredOn()) {
                        g.setColor(ColorEnum.UNEXPLORED.getColor());
                        g.fillOval(j * 1, i * 1, 5, 5);
                    } else if (pole[i][j] == Tile.ROBOTPOS && JF.getRobPosOn()) {
                        g.setColor(ColorEnum.SENSOR.getColor());
                        g.fillOval(j * 1, i * 1, 12, 12);
                    }
                }
            }
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (pole[i][j] == Tile.PREKAZKA && JF.getBariestOn()) {
                        g.setColor(ColorEnum.BARRIERS.getColor());
                        g.fillOval(j * 1, i * 1, 7, 7);
                    }
                }
            }
        } catch (Exception e) {
        }
    }
}
