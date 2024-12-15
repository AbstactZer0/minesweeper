import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class WindowManager implements WindowListener {
   public void windowClosing(WindowEvent var1) {
      System.out.println("Chiusura");
      System.exit(0);
   }

   public void windowIconified(WindowEvent var1) {
   }

   public void windowDeiconified(WindowEvent var1) {
   }

   public void windowActivated(WindowEvent var1) {
   }

   public void windowDeactivated(WindowEvent var1) {
   }

   public void windowOpened(WindowEvent var1) {
   }

   public void windowClosed(WindowEvent var1) {
   }
}
