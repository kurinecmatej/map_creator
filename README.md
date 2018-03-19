# Map_creator developed by Matej Kurinec.

This project is focused on creating 2-dimensional map using robot, 
that was made by myself. This robot worked on principle of 
measuring distances of objects that are in front of him. 
Next based on measured values it creates map of environment and 
draw this map on user interface.

Measuring distances is by ultrasonic device and ATMega 8-bit microcontroller. This is connected to PC through USB port
and data are transfered using RX-TX library to Java app. This app is used as UI.

UI is implemented in Java language and microcontroller is implemented in C.

Project structure:

 1. bin - contains runable files for 32 and 64 bit OS. You can run it by command java -jar MapCreator.jar.
    Just UI will display, but you need hardware for scanning.
 2. doc - contains documentation with examples of running application
 3. src - all source codes

Hope you like it... :-)
