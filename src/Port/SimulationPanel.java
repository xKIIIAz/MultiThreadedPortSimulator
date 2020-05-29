package Port;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SimulationPanel extends JPanel {
    int WIDTH = 800;
    int HEIGHT = 470;
    ArrayList<JLabel> representations;
    Color bgColor;

    public SimulationPanel() {
        representations = new ArrayList<JLabel>();
        setLayout(new GridLayout(0,5));
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        bgColor = new Color(145, 193, 216);

    }
    public void addShipRepresentation(JLabel jLabel) {
        representations.add(jLabel);
        add(jLabel);
    }
    public void removeShipRepresentation() {
        remove(representations.get(representations.size()-1));
        representations.remove(representations.size()-1);
    }
    public void clearPanel() {
        removeAll();
        representations.clear();
    }
    public void panelRepaint() {
        revalidate();
        repaint();
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(bgColor);
        g.fillRect(0,0,WIDTH,HEIGHT);
    }
}


