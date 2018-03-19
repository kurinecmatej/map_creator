package arduino;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;

/**
 * Trieda na vytvorenie mapy zo suboru, ktory obsahuje parametre prostredia
 * @author Matej
 */
public class Map extends JFrame {

    private final Tile[][] pole;
    private int width;
    private int height;
    private final int centerX;
    private int centerY;
    private final String fileLocation;

    /**
     * Konstruktor pre vytvorenie objektu Map
     * @param fileLocation nazov suboru, v ktorom su ulozene parametre prostredia
     */
    public Map(String fileLocation) {
        this.fileLocation = fileLocation;
        findMatrixDimension();
        pole = new Tile[height][width];
        centerX = Math.round(width / 2);
        resetMatrix();
        createMapFromFile();
    }
    
//    public Map() {
//        this.fileLocation = "file.txt";
//        findMatrixDimension();
//        pole = new Tile[height][width];
//        centerX = Math.round(width / 2);
//        resetMatrix();
//        createMapFromFile();
//    }
    
    /**
     * 
     * @return dvojrozmerne pole charakterizujuce pravdepodobnostnu mriezku
     */
    public Tile[][] getMapField(){
        return pole;
    }
    
    /**
     * 
     * @return vyska pola
     */
    public int getHeight(){
        return height;
    }
    
    /**
     * 
     * @return sirka pola
     */
    public int getWidth(){
        return width;
    }

    /**
     * metoda na vynulovanie obsahu mriezky
     */
    private void resetMatrix() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pole[i][j] = Tile.NEPRESKUMANE;
            }
        }
    }

    /**
     * spracovanie jedneho udaju zo suboru.
     * Na zaklade modifikovaneho bressenhamovho algoritmu sa do mriezky zapisu informacie o nasnimanej prekazke
     * @param length vzdialenost prekazky od snimaca
     * @param angle uhol, v ktorom sa vykonalo meranie
     */
    public void processLine(int length, int angle) {
        double degrees = angle;
        double radians = Math.toRadians(degrees);
        double lineWidth = length * Math.abs(Math.cos(radians));
        double linePiece = Math.round((length / lineWidth) * 100.0) / 100.0; //cislo na 2 des miesta
        double actLinePos = 0;

        if (angle > 0 && angle < 89) {
            int i, downTile, upTile = 0;
            for (i = centerX; i < centerX + lineWidth - 1; i++) {
                downTile = (int) (Math.round(Math.abs(Math.sin(radians)) * actLinePos));
                actLinePos += linePiece;
                upTile = (int) (Math.round(Math.abs(Math.sin(radians)) * actLinePos));
                downTile = height - downTile - 1;
                upTile = height - upTile - 1;
                for (int j = downTile; j >= upTile; j--) {
                    pole[j][i] = Tile.VOLNE;
                }
                actLinePos += 0.01;
            }
            pole[upTile][i] = Tile.PREKAZKA;
        }

        if (angle > 91 && angle < 180) {
            int i, downTile, upTile = 0;
            for (i = centerX; i > centerX - lineWidth + 1; i--) {
                downTile = (int) (Math.round(Math.abs(Math.sin(radians)) * actLinePos));
                actLinePos += linePiece;
                upTile = (int) (Math.round(Math.abs(Math.sin(radians)) * actLinePos));
                downTile = height - downTile - 1;
                upTile = height - upTile - 1;
                for (int j = downTile; j >= upTile; j--) {
                    pole[j][i] = Tile.VOLNE;
                }
                actLinePos += 0.01;
            }
            pole[upTile][i] = Tile.PREKAZKA;
        }
    }

    /**
     * metoda na spracovanie obsahu suboru s parametrami prostredia
     * @return 0 ak sa podarilo spracovat data, inac info o chybe
     */
    public int createMapFromFile() {
       // File file = new File("file.txt");
        BufferedReader reader = null;
        int angle = 0;
        

        try {
            File file = new File(fileLocation);
            reader = new BufferedReader(new FileReader(file));
            String text = null;
            while ((text = reader.readLine()) != null) {
                processLine((Integer.parseInt(text)), angle);
                angle++;
            }
            if(height > 0)pole[height - 1][centerX] = Tile.ROBOTPOS;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 2;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }
        return 0;
    }

    /**
     * metoda na zistenie rozmerov mriezky na zaklade max nameranej vzdialenosti
     */
    public void findMatrixDimension() {
        File file = new File(fileLocation);
        BufferedReader reader = null;
        int max = 0, read;

        try {
            reader = new BufferedReader(new FileReader(file));
            String text = null;
            while ((text = reader.readLine()) != null) {
                read = Integer.parseInt(text);
                if (read > max) {
                    max = read;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }
        width = 2 * max;
        height = 2 * max;
    }  
}
