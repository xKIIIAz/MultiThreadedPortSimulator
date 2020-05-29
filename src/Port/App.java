package Port;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame{
    private static Image icon;

    public App() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
