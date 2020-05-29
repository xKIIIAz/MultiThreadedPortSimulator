package Port;

import javax.swing.*;
import java.awt.*;

public class AppPanel extends JPanel {
    public MenuPanel menuPanel;
    RightPanel rightPanel;
    public InterfaceListener interfaceListener;


    public AppPanel() {
        rightPanel = new RightPanel();
        menuPanel = new MenuPanel();
        interfaceListener = new InterfaceListener(rightPanel.log, menuPanel, rightPanel.simPanel);

        add(menuPanel);
        add(rightPanel);
        new Thread(interfaceListener).start();
    }
}
