import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Gui extends JFrame implements ActionListener, MouseListener, Runnable {
   private int MAX_ROW = 20;
   private int MAX_COL = 10;
   private int s;
   private int m;
   private int max_mine = 40;
   private int DIFFICULT = 1;
   private boolean game;
   private JPanel p;
   private JPanel p1;
   private JPanel p2;
   private JTextField txt_time;
   private JTextField txt_mine;
   private Cell[][] c;
   private Thread time;
   private JMenuBar bar;

   private List<Cell> mines;
   private boolean firstClick = true;

   public Gui() {
      this.setTitle("Campo Fiorito");
      this.game = true;
      this.p = new JPanel();
      this.p1 = new JPanel();
      this.p2 = new JPanel(new FlowLayout(1, 1, 1));
      this.p.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      this.p2.setBorder(BorderFactory.createTitledBorder("Info"));
      this.p2.setOpaque(true);
      this.txt_time = new JTextField(10);
      this.txt_time.setEditable(false);
      this.txt_time.setHorizontalAlignment(4);
      this.txt_mine = new JTextField(10);
      this.txt_mine.setEditable(false);
      this.txt_mine.setHorizontalAlignment(4);
      this.txt_mine.setText(this.max_mine + "");
      this.p2.add(this.txt_time);
      this.p2.add(this.txt_mine);
      this.p2.setBackground(Color.WHITE);
      this.c = new Cell[this.MAX_ROW][this.MAX_COL];
      this.time = new Thread(this);
      this.addWindowListener(new WindowManager());
      this.p.setLayout(new BorderLayout());
      this.p1.setLayout(new GridLayout(this.MAX_ROW, this.MAX_COL));
      this.p1.setSize(200, 100);
      this.initializeCell();
      //this.initializeCellNumber();
      this.p.add(this.p1, "Center");
      this.p.add(new JLabel(" ", 0), "South");
      this.p.add(this.p2, "North");
      this.p.add(new JLabel("  "), "West");
      this.p.add(new JLabel("  "), "East");
      this.bar = new JMenuBar();
      this.initializeJMenuBar();
      this.add(this.p);
      this.pack();
      this.time.start();
   }

   public void run() {
      while (true) {
         this.s = 0;
         this.m = 0;

         while (this.game) {
            
            if (this.s < 10) {
               this.txt_time.setText(this.m + ":0" + this.s);
            } else {
               this.txt_time.setText(this.m + ":" + this.s);
            }

            try {
               Thread.sleep(500L);
            } catch (Exception e) {
            }

            boolean isWin = true;

            for (int i = 0; i < this.MAX_ROW; ++i) {
               for (int j = 0; j < this.MAX_COL; ++j) {
                  if (!this.c[i][j].isChecked() && !this.c[i][j].isMine()) {
                     isWin = false;
                     break;
                  }
               }
            }

            if (isWin) {
               JOptionPane.showMessageDialog(this, "You Win", "Winner", 1);
               this.game = false;
            }

            try {
               Thread.sleep(499L);
            } catch (Exception e) {
            }

            if(!firstClick){
               ++this.s;
               if (this.s == 61) {
                  this.s = 0;
                  ++this.m;
               }
            }
         }

         while (!this.game) {
            try {
               Thread.sleep(100L);
            } catch (Exception e) {
            }
         }
      }
   }

   private void initializeCell() {
      for (int i = 0; i < this.MAX_ROW; ++i) {
         for (int j = 0; j < this.MAX_COL; ++j) {
            this.c[i][j] = new Cell(0, i, j);
            this.c[i][j].setFont(new Font("Monospaced", 1, 12));
            this.c[i][j].setMargin(new Insets(0, 0, 0, 0));
            this.p1.add(this.c[i][j]);
            this.c[i][j].addMouseListener(this);
            this.c[i][j].setActionCommand(i + "");
         }
      }

   }

   private void initializeJMenuBar() {
      JMenu menu = new JMenu("game");
      JMenuItem menuItem = new JMenuItem("restart");
      menuItem.addActionListener(this);
      menu.add(menuItem);
      this.bar.add(menu);
      menu = new JMenu("option");
      menuItem = new JMenuItem("9x9 EASY");
      menuItem.setActionCommand("easy");
      menuItem.addActionListener(this);
      menu.add(menuItem);
      menuItem = new JMenuItem("15x15 MEDIUM");
      menuItem.addActionListener(this);
      menuItem.setActionCommand("medium");
      menu.add(menuItem);
      menuItem = new JMenuItem("20x20 HARD");
      menuItem.addActionListener(this);
      menuItem.setActionCommand("hard");
      menu.add(menuItem);
      this.bar.add(menu);
      this.setJMenuBar(this.bar);
   }
   private void initializeMines() {
      initializeMines(-2, -2);
   }

   private void initializeMines(int starty, int startx) {
      Random rand = new Random();
      this.mines = new ArrayList<>();

      for (int i = 0; i < this.max_mine; ++i) {
         int y = rand.nextInt(this.MAX_ROW);
         int x = rand.nextInt(this.MAX_COL);
         if (this.c[y][x].isMine() || (y >= starty - 1 && y <= starty + 1 && x >= startx - 1 && x <= startx + 1)) {
            --i;
         } else {
            this.c[y][x].setActionCommand("-1");
            mines.add(this.c[y][x]);

            for(int dy = -1; dy <= 1; dy++){
               for(int dx = -1; dx <=1; dx++){
                  int updateY = y + dy,
                     updateX = x + dx;
                  if(updateY >= 0 && updateY < MAX_ROW && updateX >= 0 && updateX < MAX_COL){
                     this.c[updateY][updateX].add();
                  }
               }
            }
         }
      }

   }

   private void viewMines() {
      for (Cell mine : mines) {

            ImageIcon imageIcon = new ImageIcon("Mine.png");
            Image image = imageIcon.getImage().getScaledInstance(30, 30, 4);

            if (mine.isBlocked()) {
               mine.setBackground(Color.WHITE);
            } else {
               mine.setBackground(Color.RED);
            }

         mine.setIcon(imageIcon);
         
      }

   }

   private void initializeCellNumber() {
      for (int i = 0; i < this.MAX_ROW; ++i) {
         for (int j = 0; j < this.MAX_COL; ++j) {
            if (!this.c[i][j].isMine()) {
               int tmpy = i - 1;

               for (int k = 0; k < 3; ++k) {
                  int tmpx = j - 1;

                  for (int l = 0; l < 3; ++l) {
                     try
                     {
                           Cell cell = this.c[tmpy][tmpx];
                        if (cell.isMine()) {
                           this.c[i][j].add();
                        }
                     }catch(ArrayIndexOutOfBoundsException e){}
                     ++tmpx;
                  }

                  ++tmpy;
               }
            }
         }
      }

   }

   private void algorithm(int y, int x) {

      if(y >= 0 && y < MAX_ROW && x >= 0 && x < MAX_COL){
         Cell cell = this.c[y][x];
         if (!cell.isChecked()) {
            cell.checked();
            if (cell.isBlocked()) {
               cell.blocked();
               ++this.max_mine;
               this.txt_mine.setText(this.max_mine + "");
            }

            if (cell.getNum() == 0) {
               this.algorithm(y + 1, x);
               this.algorithm(y, x + 1);
               this.algorithm(y - 1, x);
               this.algorithm(y, x - 1);
               this.algorithm(y + 1, x - 1);
               this.algorithm(y + 1, x + 1);
               this.algorithm(y - 1, x - 1);
               this.algorithm(y - 1, x + 1);
            }
         }
      }
   }

   public void actionPerformed(ActionEvent actionEvent) {
      JMenuItem menuItem = new JMenuItem();

      try {
         menuItem = (JMenuItem) actionEvent.getSource();
      } catch (Exception e) {
      }

      String actionCommand = menuItem.getActionCommand();

      switch (actionCommand) {
         case "easy":
            this.MAX_COL = 9;
            this.MAX_ROW = 9;
            this.max_mine = 20;
            this.DIFFICULT = 0;
            break;
         case "medium":
            this.MAX_COL = 15;
            this.MAX_ROW = 15;
            this.max_mine = 40;
            this.DIFFICULT = 1;
            break;
         case "hard":
            this.MAX_COL = 20;
            this.MAX_ROW = 20;
            this.max_mine = 80;
            this.DIFFICULT = 2;
      }

      this.restart();
   }

   private void restart() {
      switch (this.DIFFICULT) {
         case 0:
            this.MAX_COL = 9;
            this.MAX_ROW = 9;
            this.max_mine = 20;
            break;
         case 1:
            this.MAX_COL = 15;
            this.MAX_ROW = 15;
            this.max_mine = 40;
            break;
         case 2:
            this.MAX_COL = 20;
            this.MAX_ROW = 20;
            this.max_mine = 80;
      }

      this.p1.removeAll();
      this.p1.setLayout(new GridLayout(this.MAX_ROW, this.MAX_COL));
      this.c = new Cell[this.MAX_ROW][this.MAX_COL];
      this.game = true;
      this.s = 0;
      this.m = 0;
      this.txt_time.setText(this.m + ":0" + this.s);
      this.txt_mine.setText(this.max_mine + "");
      this.initializeCell();
      firstClick = true;

      //this.initializeCellNumber();
      SwingUtilities.updateComponentTreeUI(this);
   }

   public void mousePressed(MouseEvent mouseEvent) {
   }

   public void mouseReleased(MouseEvent mouseEvent) {
   }

   public void mouseEntered(MouseEvent mouseEvent) {
   }

   public void mouseExited(MouseEvent mouseEvent) {
   }

   public void mouseClicked(MouseEvent mouseEvent) {
      System.out.println(this.c[0][0].getSize());
      if (this.game) {
         Cell cell = new Cell("");

         try {
            cell = (Cell) mouseEvent.getSource();
            System.out.println(cell.getNum());
         } catch (Exception e) {}

         if (SwingUtilities.isLeftMouseButton(mouseEvent) && !cell.isBlocked()) {

            if(firstClick){
               initializeMines(cell.getPosy(), cell.getPosx());
               firstClick = false;
            }

            if (!cell.isMine()) {
               this.algorithm(cell.getPosy(), cell.getPosx());
            }

            if (cell.isMine()) {
               this.game = false;
               JOptionPane.showMessageDialog(this, "time " + this.txt_time.getText(), "Game Over", 1);
               this.viewMines();
            }
         } else if (SwingUtilities.isRightMouseButton(mouseEvent) && !cell.isChecked() && this.max_mine != 0) {
            if (cell.blocked()) {
               --this.max_mine;
            } else {
               ++this.max_mine;
            }

            this.txt_mine.setText(this.max_mine + "");
         }
      } else {
         JOptionPane.showMessageDialog(this, "time " + this.txt_time.getText(), "Game Over", 1);
      }

   }
}
