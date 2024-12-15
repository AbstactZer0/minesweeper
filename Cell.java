import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;

public class Cell extends JButton {
   private int num;
   private boolean checked;
   private boolean blocked;

   private Color defaultColor;

   public Cell(String text) {
      super(text);
   }

   public Cell(int num) {
      super("");
      this.num = num;
      this.checked = false;
      this.blocked = false;
      defaultColor = this.getBackground();
   }

   public int getNum() {
      return this.num;
   }

   public void setNum(int num) {
      this.num = num;
   }

   public void add() {
      ++this.num;
   }

   public void checked() {
      this.setText("");
      this.setIcon((Icon) null);
      this.checked = true;
      if (this.num != 0) {
         this.setText(this.num + "");
      }

      this.setEnabled(false);
   }

   public boolean isChecked() {
      return this.checked;
   }

   public boolean blocked() {
      this.blocked = !this.blocked;
      if (!this.blocked) {
         new ImageIcon("Empty.png");
         this.setBackground(defaultColor);
         this.setIcon((Icon) null);
         return false;
      } else {
         ImageIcon imageIcon = new ImageIcon("Flag.png");
         this.setBackground(Color.YELLOW);
         Image image = imageIcon.getImage().getScaledInstance(30, 30, 4);
         imageIcon = new ImageIcon(image);
         this.setIcon(imageIcon);
         return true;
      }
   }

   public boolean isBlocked() {
      return this.blocked;
   }

   public boolean isMine() {
      return getActionCommand().equals("-1");
   }
}
