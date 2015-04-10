//import java.lang.InterruptedException;

public class AstroidsGame {

    public static void main(String[] args)/* throws InterruptedException*/ {
        AstroidsWindow myWindow = new AstroidsWindow();
        Thread t = new Thread();
        t.setDaemon(true);
        try{
            t.sleep(Long.MAX_VALUE);
        }
        catch(InterruptedException ex){
            System.out.println(ex);
        }
//        t.setPriority(Thread.MAX_PRIORITY);
//        t.start();
    }
}
