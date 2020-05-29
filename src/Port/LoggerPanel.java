package Port;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class LoggerPanel extends JPanel {
    JTextArea logPrompt;
    public int WIDTH = 800;
    public int HEIGHT = 236;
    public LoggerPanel() {
        setBorder(new TitledBorder(new EtchedBorder(), "Logger"));

        this.logPrompt = new JTextArea(12, 110);
        this.logPrompt.setFont(new Font("Consolas", Font.PLAIN, 14));

        logPrompt.setEditable(false);
        JScrollPane scroll = new JScrollPane(logPrompt);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        new SmartScroller (scroll);
        add(scroll);
        setVisible(true);
    }
    public synchronized void println(String string) {
        this.logPrompt.append(" " + string + "\n");
    }
}
