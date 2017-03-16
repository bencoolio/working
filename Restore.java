import java.io.*;
import java.util.*;

//a class that deserializes a GreenhouseControls object. Contains a display
//method that fixes the error, logs the fixed error and runs the program
//from where it left off.
class Restore extends GreenhouseControls{
    String filename = "";
    GreenhouseControls gr;
    public long endTime;
    public long elapsedTime;
    public Restore(String filename)throws IOException, 
    ClassNotFoundException,ControllerException{
        this.filename = filename;
        try{  
            //read serialized object from file
            ObjectInputStream i = new ObjectInputStream(
            new FileInputStream(new File(filename))); 
            GreenhouseControls g = (GreenhouseControls)i.readObject();
            gr = g;
            endTime = gr.endTime;
            i.close();
            elapsedTime = System.currentTimeMillis() - g.endTime;
        }catch(IOException e){
            System.out.println(e);
        }
    }
    public void display()throws ControllerException{
        Fixable fx = gr.getFixable(gr.getError());
        fx.fix();
        fx.log();
        gr.run(elapsedTime);
        System.exit(0);
    }
}
