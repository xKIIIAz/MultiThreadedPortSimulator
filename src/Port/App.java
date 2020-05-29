package Port;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class App extends JFrame{
    public static int WIDTH = 1000;
    public static int HEIGHT = 900;
    private static Image icon;

    public App() {
        //experimental - it looks better, but throws bugs while in fast simulation mode
        /*try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we)
            {
                System.exit(0);
            }
        });
        setIconImage(icon);
        setTitle("Port");
        AppPanel panel = new AppPanel();
        add(panel);
        setResizable(false);
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        icon = Toolkit.getDefaultToolkit().getImage("lib\\tx\\idle\\blue.png");
        icon = icon.getScaledInstance(258,258, Image.SCALE_DEFAULT);
        App apka = new App();
        apka.pack();
    }
}
