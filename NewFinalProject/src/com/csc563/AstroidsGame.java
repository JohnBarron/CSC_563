package com.csc563;


import com.csc563.AstroidsWindow;
import java.io.IOException;

//import java.lang.InterruptedException;

public class AstroidsGame {

    public static void main(String[] args)/* throws InterruptedException*/ {
        try{
            AstroidsWindow myWindow = new AstroidsWindow();
            Thread t = new Thread();
            t.setDaemon(true);
            t.sleep(Long.MAX_VALUE);
        }
        catch(IOException ex) {
            System.out.println(ex);
        }
        catch(InterruptedException ex){
            System.out.println(ex);
        }
//        t.setPriority(Thread.MAX_PRIORITY);
//        t.start();
    }
}
