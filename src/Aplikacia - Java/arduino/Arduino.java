package arduino;

import frames.ProgressBar;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import frames.*;
import java.io.File;

/**
 * Trieda Arduino. Sluzi na riadenie komunikacie s arduinom
 *
 * @author Matej
 */
public class Arduino extends Thread {

    private InputStream input;
    private OutputStream output;
    private final String fileName;
    private final JFrame myjframe;
    private boolean canScan = true;

    /**
     * konstruktor pre vytvorenie objektu Arduino
     *
     * @param fileName meno zlozky, do ktorej sa maju ukladat parametre
     * prostredia
     * @param jframe objekt hlavneho framu aplikacie
     */
    public Arduino(String fileName, JFrame jframe) {
        this.fileName = fileName;
        this.myjframe = jframe;
    }

    /**
     * metoda na zapisovanie udajov do serioveho portu
     *
     * @param data data, ktore maju byt zapisane
     */
    public synchronized void writeData(String data) {
        System.out.println("Sent: " + data);
        try {
            output.write(data.getBytes());
        } catch (Exception e) {
            System.out.println("could not write to port");
        }
    }

    public void setCanScan(boolean canScan) {
        this.canScan = canScan;
    }

    /**
     * metoda na prevedenie jednotlivych bytov cisla na cele cislo BCD to Int
     *
     * @param b BCD kod cisla
     * @return cislo vo formate integer
     */
    public int byteArrayToInt(byte[] b) {
        String Str = new String(b);
        return Integer.parseInt(Str);
    }

    /**
     * vytvorenie vystupneho prudu; vytvorenie suboru
     *
     * @param name meno vytvorenej zlozky
     * @return smernik na vytvorenu zlozku
     */
    private FileOutputStream createFile(String name) {
        try {
            FileOutputStream out = new FileOutputStream(name, false);
            return out;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Arduino.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * metoda na zapisovanie informacii do suboru
     *
     * @param out smernik na zlozku, do ktorej sa ma zapisovat
     * @param data pole bytov, ktore sa ma zapisat
     */
    private void writeToFile(FileOutputStream out, byte[] data) {
        try {
            out.write(data);
            out.write(13);
            out.write(10);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Arduino.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Arduino.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * metoda na nacitavanie dat zo serioveho portu
     */
    public void readData() {
        int inputData = 0;
        ProgressBar pb = new ProgressBar(this);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileOutputStream file = createFile(fileName);
        writeData("0");
        try {
            for (int i = 0; i < 180;) {
                if (!canScan) {
                    file.close();
                    return;
                }
                pb.setPercentage((int) Math.round(((double) 100 / (double) 180) * (double) i));
                if ((inputData = input.read()) != -1) {
                    writeData("1");
                    while (inputData > -1) {
                        bos.write(inputData);
                        inputData = input.read();
                    }
                    bos.flush();
                    byte[] result = bos.toByteArray();
                    System.out.println(i + ": " + byteArrayToInt(result) + " cm");
                    writeToFile(file, result);
                    bos.reset();
                    writeData("0");
                    i++;
                }
            }
            file.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    @Override
    public void run() {
        try {
            SerialClass serial = new SerialClass();
            serial.initialize();
            input = serial.getInputStream();
            output = serial.getOutputStream();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ie) {
                System.out.println(ie.getMessage());
            }
            canScan = true;
            readData();
            serial.close();
            if (canScan) {
                ((MainFrame) myjframe).createNewMap();
            } else {
                File file = new File("file.txt");
                if (file.exists()) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
