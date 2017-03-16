import java.io.*;
import tme3.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.*;
/***********************************************************************
 * GreenhouseControls.java
 *
 * Assignment: TME3
 * @author: Steve Leung
 * @author: Sean Connelly
 * @date  : January 30, 2017
 *
 * This produces a specific application of the control system, all in a 
 * single class. Inner classes allow you to encapsulate different 
 * functionality for each type of event.
 *
 * Instructions: To be compiled and run with command line
 *               java GreenhouseControls -f example2.txt to serialize.
 *               java GreenhouseControls -d dump.out to deserialize 
 *
 **********************************************************************/

public class GreenhouseControls extends Controller {
    private boolean light = false;
    private boolean water = false;
    private String thermostat = "Day";
    private String eventsFile = "examples1.txt";
    private boolean fans = false;
    private boolean windowOk = true;
    private boolean powerOn = true;
    protected int errorCode = 0; 
    protected long endTime = 0;
    
    public class FansOn extends Event {    
        public FansOn(long delayTime) { 
            super(delayTime);
        }
        public void action() {
            fans = true;
        }
        public String toString() { 
            return "Fans are on"; 
        }
    }
 
    public class FansOff extends Event {
        public FansOff(long delayTime) { 
            super (delayTime);
        }
        public void action() {
            fans = false;
        }
        public String toString() { 
            return "Fans are off"; 
        }
    }

    public class LightOn extends Event {
        public LightOn(long delayTime) { 
            super(delayTime); 
        }
        public void action() {
            // Put hardware control code here to
            // physically turn on the light.
            light = true;
        }
        public String toString() { 
            return "Light is on"; 
        }
    }

    public class LightOff extends Event {
        public LightOff(long delayTime) { 
            super(delayTime); 
        }
        public void action() {
            // Put hardware control code here to
            // physically turn off the light.
            light = false;
        }
        public String toString() { 
            return "Light is off";
        }
    }

    public class WaterOn extends Event {
        public WaterOn(long delayTime) { 
            super(delayTime); 
        }
        public void action() {
            // Put hardware control code here.
            water = true;
        }
        public String toString() {
            return "Greenhouse water is on";
        }
    }
 
    public class WaterOff extends Event {
        public WaterOff(long delayTime) { 
            super(delayTime); 
        }
        public void action() {
            // Put hardware control code here.
            water = false;
        }
        public String toString() {
            return "Greenhouse water is off";
        }
    }

    public class ThermostatNight extends Event {
        public ThermostatNight(long delayTime) {
            super(delayTime);
        }
        public void action() {
            // Put hardware control code here.
            thermostat = "Night";
        }
        public String toString() {
            return "Thermostat on night setting";
        }
    }

    public class ThermostatDay extends Event {
        public ThermostatDay(long delayTime) {
            super(delayTime);
        }
        public void action() {
            // Put hardware control code here.
            thermostat = "Day";
        }
        public String toString() {
            return "Thermostat on day setting";
        }
    }

    // An example of an action() that inserts a
    // new one of itself into the event list:
    public class Bell extends Event {
        int rings = 1;
        long eventTime;
   
        public Bell(long delayTime, int rings) { 
            super(delayTime);
            this.rings = rings;
            for(int i=1; i<rings; i++)
            addEvent(new Bell(delayTime+(2000*i),0));
        }
        public  void action() {
	    // nothing to do
        }
        @Override 
        public String toString() { 
            return "Bing!"; 
        }
    }

    public class Restart extends Event {
        public Restart(long delayTime, String filename)
        throws IOException{
            super(delayTime);
            eventsFile = filename;
        }
        //adds events
        public void action(){
            //add a malfunction event
           // addEvent(new WindowMalfunction(5000));   
             addEvent(new PowerOut(5000));
            try{     
                //reads from 'examples' text files.          
                Scanner sc = new Scanner(new FileReader(eventsFile));
                Pattern num = Pattern.compile("\\d+");
                //loops through each line in file
                while (sc.hasNext()) {
                    String temp = sc.next();
                    Matcher matcher = num.matcher(temp);
                    //gets delay time from file for thermostatnight, converts
                    // string to long and adds it as parameter for thermostat delay
                    if(temp.contains("ThermostatNight")){
                        matcher.find();
                        String inNum = matcher.group();
                        long dLay = Long.parseLong(inNum);                   
                        addEvent(new ThermostatNight(dLay));
                    }
                    //gets delay time from file for lightOn, converts
                    // string to long and adds it as parameter for lightOn delay
                    if(temp.contains("LightOn")){
                        matcher.find();
                        String inNum = matcher.group();
                        long dLay = Long.parseLong(inNum);                   
                        addEvent(new LightOn(dLay));
                    }
                    //gets delay time from file for waterOff, converts
                    // string to long and adds it as parameter for waterOff delay
                    if(temp.contains("WaterOff")){
                        matcher.find();
                        String inNum = matcher.group();
                        long dLay = Long.parseLong(inNum);                   
                        addEvent(new WaterOff(dLay));
                    }
                    //gets delay time from file for ThermostatDay, converts
                    // string to long and adds it as parameter for ThermostatDay delay
                    if(temp.contains("ThermostatDay")){
                        matcher.find();
                        String inNum = matcher.group();
                        long dLay = Long.parseLong(inNum);                   
                        addEvent(new ThermostatDay(dLay));
                    }
                    //gets delay time and number of rings from file for Bell, 
                    //converts strings to long and adds it as parameter for Bell 
                    //delay and number of rings
                    if(temp.contains("Bell")){
                        long dLay = 0;
                        long nRings = 0;
                        int count = 1;
                        while(matcher.find()){
                            String inNum = matcher.group();
                            nRings = Long.parseLong(inNum);
                                while(count == 1){
                                dLay = Long.parseLong(inNum);
                                count++;
                            } 
                        }
                     
                        if(dLay == nRings)
                            nRings = 1;
                        addEvent(new Bell(dLay,(int)nRings));
                    }
                    //gets delay time from file for WaterOn, converts string
                    // to long and adds it as parameter for delayTime.
                    if(temp.contains("WaterOn")){
                        matcher.find();
                        String inNum = matcher.group();
                        long dLay = Long.parseLong(inNum);                   
                        addEvent(new WaterOn(dLay));
                    }
                    //gets delay time from file for LightOff, converts string
                    //to long and adds it as parameter for delayTime.
                    if(temp.contains("LightOff")){
                        matcher.find();
                        String inNum = matcher.group();
                        long dLay = Long.parseLong(inNum);                   
                        addEvent(new LightOff(dLay));
                    }
                    //gets delay time from file for Terminate, converts string
                    //to long and adds it as parameter for delayTime.
                    if(temp.contains("Terminate")){
                        matcher.find();
                        String inNum = matcher.group();
                        long dLay = Long.parseLong(inNum);                   
                        addEvent(new Terminate(dLay));
                    }
                    //gets delay time from file for FansOn, converts string
                    //to long and adds it as parameter for delayTime.
                    if(temp.contains("FansOn")){
                        matcher.find();
                        String inNum = matcher.group();
                        long dLay = Long.parseLong(inNum);                   
                        addEvent(new FansOn(dLay));
                    }
                    //gets delay time from file for FansOff, converts string
                    //to long and adds it as parameter for delayTime.
                    if(temp.contains("FansOff")){
                        matcher.find();
                        String inNum = matcher.group();
                        long dLay = Long.parseLong(inNum);                   
                        addEvent(new FansOff(dLay));
                    }
                }//end while
                sc.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }//end action method
        @Override
        public String toString() {
            return "Restarting system";
        }
    }//end restart class

    // A class that demonstrates a malfunction event. An exception is thrown
    // when the action method is used.
    public class WindowMalfunction extends Event {
        public WindowMalfunction(long delayTime){
            super(delayTime);
            windowOk = false;
            errorCode = 1;  
        }
        public void action()throws ControllerException{
            if(errorCode == 1)
                throw new ControllerException("Window Malfunction!!");
        }
        @Override
        public String toString(){
            return "ErrorCode 1.  Window Malfunction!";
        }
    }
   
    // A class that demonstrates a malfunction event. An exceptino is thrown
    // when the action method is used.
    public class PowerOut extends Event {
        public PowerOut(long delayTime){
            super(delayTime);
            powerOn = false; 
            errorCode = 2;
        }
        public void action()throws ControllerException{
            if(errorCode == 2)
                throw new ControllerException("Power Out!!");
        }
        @Override
        public String toString(){
            return "ErrorCode 2. Power is out!";
        }
    }

    // A class that fixes the errorCode. Has fix method that restores the error
    // variables and a log method that prints the fixed error and writes the fix
    // to the file 'fix.log'.
    public class PowerOn implements Fixable {
        public void fix(){
            powerOn = true;
            errorCode = 0;
        }
        public void log(){
            try{
                BufferedWriter bw =
                new BufferedWriter(new FileWriter("fix.log"));
                Date date = new Date();
                String content = "Error Code 2"+
                ": Power Now On. "+date+"\n";
                System.out.println(content);
                bw.write(content);
                bw.close();

            }catch(IOException e){
                System.out.println(e);
            }
        }
    }

    // A class that fixes the errorCode. Has fix method that restores the error
    // variables and a log method that prints the fixed error and writes the fix
    // to the file 'fix.log'.
    public class FixWindow implements Fixable {
        public void fix(){
            windowOk = true;
            errorCode = 0;
        }
        public void log(){
            try{
                BufferedWriter bw =
                new BufferedWriter(new FileWriter("fix.log"));
                Date date = new Date();
                String content = "Error Code 1"+
                ": Window Malfunction Fixed. "+date+"\n";
                System.out.println(content);
                bw.write(content);
                bw.close();
            }catch(IOException e){
                System.out.println(e);
            }
        }
    }
        
    public class Terminate extends Event {
        public Terminate(long delayTime) { 
            super(delayTime); 
        }
        public void action() { 
            System.exit(0); 
        }
        public String toString() { 
            return "Terminating";  
        }
    }

//--GreenhouseControls methods----------------------------------------
     
    public static void printUsage() {
        System.out.println("Correct format: ");
        System.out.println("  java GreenhouseControls -f <filename>, or");
        System.out.println("  java GreenhouseControls -d dump.out");
    }
    // A method called when an exception is thrown. It shutsdown the program.
    public void shutdown(){
        System.out.println("Emergency Shutdown");
        System.exit(0);
    }
    // A method that returns the error code.
    public int getError() {
        return this.errorCode;
    }
    /**
     * A method that returns a Fixable object.
     *
     * @param errorcode  Type int passed and evaluated, returning a FixWindow or
     * PowerOn object, based on the errorcode that is passed.
     */
    public Fixable getFixable(int errorcode){
        errorCode = errorcode;
        if(errorCode == 1){
            return new FixWindow();
        }else if(errorCode == 2){
            return new PowerOn();
        }
        return null;
    }

//---------------------------------------------------------
    public static void main(String[] args)throws ControllerException,
        ClassNotFoundException,IOException {
	try {
	    String option = args[0];
	    String filename = args[1];

	    if ( !(option.equals("-f")) && !(option.equals("-d")) ) {
		System.out.println("Invalid option");
		printUsage();
	    }

	    GreenhouseControls gc = new GreenhouseControls();
	    if (option.equals("-f"))  {
		try{
          gc.addEvent(gc.new Restart(0,filename));
          }catch(IOException f){
         System.out.println(f);
} 
          
         }   

            if (option.equals("-d")) {
                try{
                    Restore rs = new Restore(filename);
                    rs.display();
                }catch(Exception p){
                    System.out.println(p);
                }
            }//end -d if 

            try{ 
                gc.run(0);  
            }catch(ControllerException e){
                System.out.println(e);
                // writes the error thrown to the file 'error.log' and
                // also prints the message.           
                try{
                    BufferedWriter bw =
                    new BufferedWriter(new FileWriter("error.log"));
                    Date date = new Date();
                    String content = "Error Code "+gc.getError()+
                    ": Power Out. "+date+"\n";
                    System.out.println(content); 
                    bw.write(content);
                    bw.close();
                    gc.endTime = System.currentTimeMillis();
                    //writes the GreenhouseControls object to the file 'dump.out'.
                    ObjectOutput out = new ObjectOutputStream(
                    new FileOutputStream("dump.out"));
                    out.writeObject(gc);

                }catch(IOException a){
                    System.out.println(a);
                }
                gc.shutdown();
            }//end gc.run() try catch
    
	}catch (ArrayIndexOutOfBoundsException e) {
	    System.out.println("Invalid number of parameters");
	    printUsage();
	}
    }
} ///:~

