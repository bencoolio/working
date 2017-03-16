//: innerclasses/controller/Controller.java
// The reusable framework for control systems.
// From 'Thinking in Java, 4th ed.' (c) Bruce Eckel 2005
// www.BruceEckel.com. See copyright notice in CopyRight.txt.

/***********************************************************************
 * Adapated for COMP308 Java for Programmer, 
 *		SCIS, Athabasca University
 *
 * Assignment: TME3
 * @author: Steve Leung
 * @author: Sean Connelly
 * @date  : Jan 30, 2017
 *
 */

package tme3;
import java.util.*;
import java.io.Serializable;
import java.io.*;
public class Controller implements Serializable {
    // A class from java.util to hold Event objects:
    private List<Event> eventList = new ArrayList<Event>();
    public void addEvent(Event c) { eventList.add(c); }

    /**
     * Method accepts a 'long' elapsedTime variable, used when restoring
     * the serialized object, the elapsedTime variable is passed to the inner
     * method 'ready()' to compensates for the eventTime calculation.
     * Throws ControllerException when the classes WindowMalfunction and
     * PowerOut use their action() methods.
     *
     * @param elapsedTime  Long type passed to inner 'ready()' method.
     */
    public void run(long elapsedTime) throws ControllerException{
        while(eventList.size() > 0){
            // Make a copy so you're not modifying the list
            // while you're selecting the elements in it:
            for(Event e : new ArrayList<Event>(eventList)){
                if(e.ready(elapsedTime)) {
                 //This code works to remove the malfunction event when 
                 //deserializing the object, but couldn't figure out
                 // how packaging worked to access these classes. 
                 /* if(e instanceof WindowMalfunction
                       || e instanceof PowerOut){
			continue;
                    }*/
                    System.out.println(e);
                    e.action();
                    eventList.remove(e);
                }
            }
        }
    }

    public void shutdown() {}

    // A custom exceptino class, thrown when the inner classes WindowMalfunction
    // and PowerOut, of GreenhouseControls, have their 'action()' method used.
    public class ControllerException extends Exception{
        public ControllerException(String message){
            super(message);
        }
        public String getMessage(){
            return super.getMessage();
        }
    }
} ///:~
