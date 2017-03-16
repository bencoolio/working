//: innerclasses/controller/Event.java
// The common methods for any control event.
// From 'Thinking in Java, 4th ed.' (c) Bruce Eckel 2005
// www.BruceEckel.com. See copyright notice in CopyRight.txt.

/***********************************************************************
 * Adapated for COMP308 Java for Programmer, 
 *		SCIS, Athabasca University
 *
 * Assignment: TME3
 * @author: Steve Leung
 * @author: Sean Connelly
 * @date  : Jan. 30, 2017
 *
 * Description: Event abstract class
 *
 */

package tme3;
import java.io.*;
import java.io.Serializable;

public abstract class Event implements Serializable{
    public long eventTime;
    protected final long delayTime;
    public Event(long delayTime) {
        this.delayTime = delayTime;
        start();
    }
    public void start() { // Allows restarting
        eventTime = System.currentTimeMillis() + delayTime;
    }
    public boolean ready(long offset) {
        return System.currentTimeMillis() >= eventTime + offset;
    } 
    public abstract void action() throws Controller.ControllerException;
} ///:~i
