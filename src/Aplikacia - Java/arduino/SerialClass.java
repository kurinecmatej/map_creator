package arduino;

/**
 *
 * @author matej
 */
import frames.MessageBox;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

public class SerialClass implements SerialPortEventListener {

    public SerialPort serialPort;
    /**
     * The port we're normally going to use.
     */
    private final String PORT_NAMES[] = {
        "/dev/tty.usbserial-A9007UX1", // Mac OS X
        "/dev/ttyUSB0", // Linux
        "/dev/ttyACM0", // Linux
        "/dev/usbdev", // Linux
        "/dev/tty", // Linux
        "/dev/serial", // Linux
        "COM3", // Windows
    };

    //public static BufferedReader input;
    private InputStream input;
    private OutputStream output;

    /**
     * 
     * @return vstupny prud
     */
    public InputStream getInputStream() {
        return input;
    }

    /**
     * 
     * @return vystupny prud
     */
    public OutputStream getOutputStream() {
        return output;
    }

    /**
     * Milliseconds to block while waiting for port open
     */
    public final int TIME_OUT = 2000;
    /**
     * Default bits per second for COM port.
     */
    public final int DATA_RATE = 9600;

    /**
     * inicializacia spojenia s arduinom
     * @throws IOException vynimka IO ak sa nenajde zariadenie
     */
    public void initialize() throws IOException {
        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        //First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }
        if (portId == null) {
            System.out.println("Could not find COM port.");
            new MessageBox("Zariadenie nebolo nájdené!");
            throw new IOException();
            //return;
        }

        try {
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);

            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            // open the streams
            //input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            input = serialPort.getInputStream();
            output = serialPort.getOutputStream();
            char ch = 1;
            output.write(ch);

            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * skoncenie spojenia z arduinom
     */
    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    @Override
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                //String inputLine = input.readLine();
                //System.out.println(inputLine);
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }

    }

    /**
     * metoda na zapisanie dat do serioveho portu
     * @param data data na zapisanie do portu
     */
    public synchronized void writeData(String data) {
        System.out.println("Sent: " + data);
        try {
            output.write(data.getBytes());
        } catch (Exception e) {
            System.out.println("could not write to port");
        }
    }

    /*public static void main(String[] args) throws Exception {
     SerialClass main = new SerialClass();
     main.initialize();
     Thread t = new Thread() {
     public void run() {
     //the following line will keep this app alive for 1000 seconds,
     //waiting for events to occur and responding to them (printing incoming messages to console).
     try {
     Thread.sleep(1500);
     writeData("2");
     } catch (InterruptedException ie) {
     }
     }
     };
     t.start();
     System.out.println("Started");
     }*/
}
