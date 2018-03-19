package frames;

import arduino.Map;
import arduino.Tile;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Trieda sluziaca na vytvorenie objektu, v ktorom sa bude vykreslovat mapa
 *
 * @author Matej
 */
public class MyJPanel extends JPanel {

    private Tile[][] pole;
    private int width;
    private int height;
    private int centerX;
    private int centerY;
    private String fileLocation;
    private Map map;
    private MainFrame JF;
    private JScrollPane jsp;
    private final int imWidth;

    /**
     * Konstruktor pre vytvorenie objektu typu MyJPanel
     *
     * @param fileLocation cesta k suboru obsahujuceho parametre prostredia
     * @param JF smernik na hlavny JFrame
     * @param jsp smernik na JScrollPane, v ktorom je tento objekt umiestneny
     */
    public MyJPanel(String fileLocation, MainFrame JF, JScrollPane jsp, int imWidth) {
        this.imWidth = imWidth;
        this.jsp = jsp;
        this.JF = JF;
        map = new Map(fileLocation);
        pole = map.getMapField();
        width = map.getWidth();
        height = map.getHeight();
        setPreferredSize(new java.awt.Dimension(width * imWidth + 20, height * imWidth + 20));
    }

    /**
     * prekreslenie vykreslovacieho platna
     */
    public void render() {
        paintComponent(getGraphics());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (pole[i][j] == Tile.VOLNE && JF.getFreeOn()) {
                        g.setColor(ColorEnum.FREE.getColor());
                        g.fillOval(j * imWidth, i * imWidth, 1 + imWidth, 1 + imWidth);
                    } else if (pole[i][j] == Tile.NEPRESKUMANE && JF.getUnexploredOn()) {
                        g.setColor(ColorEnum.UNEXPLORED.getColor());
                        g.fillOval(j * imWidth, i * imWidth, 1 + imWidth, 1 + imWidth);
                    } else if (pole[i][j] == Tile.ROBOTPOS && JF.getRobPosOn()) {
                        g.setColor(ColorEnum.SENSOR.getColor());
                        g.fillOval(j * imWidth, i * imWidth, 11 + imWidth, 11 + imWidth);
                    }
                }
            }
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (pole[i][j] == Tile.PREKAZKA && JF.getBariestOn()) {
                        g.setColor(ColorEnum.BARRIERS.getColor());
                        g.fillOval(j * imWidth, i * imWidth, 6 + imWidth, 6 + imWidth);
                    }
                }
            }
        } catch (Exception e) {
        }
    }
}
