package Port;

import javax.swing.*;

public class RightPanel extends JPanel {
    public SimulationPanel simPanel;
    public LoggerPanel log;
    public RightPanel(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        simPanel = new SimulationPanel();
        log = new LoggerPanel();
        add(simPanel);
        add(log);
    }
    public LoggerPanel getLogger() {
        return log;
    }
}
