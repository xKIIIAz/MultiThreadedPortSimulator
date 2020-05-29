package Port;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalBorders;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

public class MenuPanel extends JPanel {
    private JLabel infoStatki, infoHolowniki, infoNaMorzu, infoWPorcie, infoHolowane, infoQuay;
    private JLabel infoCzasSymulacji;
    private JComboBox wyborCzasuSymulacji;
    private JSpinner shipCount, tugboatCount, quayCount;
    private JButton resetButton, startButton;
    private JPanel buttonPanel;
    private JPanel setupPanel;
    private JPanel infoPanel;
    private InterfaceListener interfaceListener;
    private String[] czasy = {"Szybka", "Średnia", "Wolna"};
    private MyChangeListener spinnerChangeListener;

    public MenuPanel() {
        this.interfaceListener = interfaceListener;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(new TitledBorder(new EtchedBorder(), "Ustawienia"));
        createInterface();
        addInterface();
    }
    public void setInterfaceListener(InterfaceListener interfaceListener) {
        this.interfaceListener = interfaceListener;
        spinnerChangeListener = new MyChangeListener(interfaceListener);
        setSpinnerListeners();
        setButtonListeners();
    }
    private void createInterface() {
        infoStatki = new JLabel("Liczba statków: ");
        infoHolowniki = new JLabel("Liczba holowników: ");
        infoNaMorzu = new JLabel("Liczba statków na morzu: 0");
        infoHolowane = new JLabel("Liczba statków holowanych: 0");
        infoWPorcie = new JLabel("Liczba statków w porcie: 0");
        infoQuay = new JLabel("Liczba miejsc: ");
        infoCzasSymulacji = new JLabel("Prędkość symulacji:");
        shipCount = new JSpinner(new SpinnerNumberModel(0,0,25,1));
        quayCount = new JSpinner(new SpinnerNumberModel(1,1,1,1));
        tugboatCount = new JSpinner(new SpinnerNumberModel(0,0,100,1));
        wyborCzasuSymulacji = new JComboBox(czasy);
        wyborCzasuSymulacji.setSelectedIndex(2);
        wyborCzasuSymulacji.setAlignmentX(JComboBox.CENTER_ALIGNMENT);
        ((JLabel)wyborCzasuSymulacji.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
        resetButton = new JButton("Reset");
        startButton = new JButton("Start");
        setupPanel = new JPanel();
        setupPanel.setLayout(new BoxLayout(setupPanel,BoxLayout.PAGE_AXIS));
        setupPanel.setPreferredSize(new Dimension(170,170));
        buttonPanel = new JPanel();
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(new Dimension(180, 66));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
        infoPanel.add(infoNaMorzu);
        infoPanel.add(infoHolowane);
        infoPanel.add(infoWPorcie);
        infoPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        //setPreferredSize(new Dimension(200,700));
    }
    private void addInterface() {
        buttonPanel.add(resetButton);
        buttonPanel.add(startButton);
        setupPanel.add(infoStatki);
        setupPanel.add(shipCount);
        setupPanel.add(infoHolowniki);
        setupPanel.add(tugboatCount);
        setupPanel.add(infoQuay);
        setupPanel.add(quayCount);
        setupPanel.add(infoCzasSymulacji);
        setupPanel.add(wyborCzasuSymulacji);
        setupPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        add(setupPanel);
        add(buttonPanel);
        add(Box.createRigidArea(new Dimension(0, 340)));
        add(infoPanel);
        add(Box.createRigidArea(new Dimension(0, 70)));
    }
    private void setButtonListeners() {
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                interfaceListener.pokeReset();
            }
        });
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                interfaceListener.pokeStart();
            }
        });
        wyborCzasuSymulacji.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                interfaceListener.pokeCheck();
            }
        });
    }

    public void setSpinnerListeners() {
        shipCount.addChangeListener(spinnerChangeListener);
        tugboatCount.addChangeListener(spinnerChangeListener);
        quayCount.addChangeListener(spinnerChangeListener);
    }

    public synchronized void updateInfoPanel(int onSeaCount, int dockedCount, int tuggedCount) {
        infoPanel.remove(infoNaMorzu);
        infoPanel.remove(infoHolowane);
        infoPanel.remove(infoWPorcie);
        if (onSeaCount < 10) {
            infoNaMorzu = new JLabel("Liczba statków na morzu: " + onSeaCount + " ");
        }
        else {
            infoNaMorzu = new JLabel("Liczba statków na morzu: " + onSeaCount);
        }
        if(tuggedCount < 10) {
            infoHolowane = new JLabel("Liczba statków holowanych: " + tuggedCount + " ");
        }
        else{
            infoHolowane = new JLabel("Liczba statków holowanych: " + tuggedCount);
        }
        if(dockedCount < 10) {
            infoWPorcie = new JLabel("Liczba statków w porcie: " + dockedCount + " ");
        }
        else {
            infoWPorcie = new JLabel("Liczba statków w porcie: " + dockedCount);
        }
        infoPanel.add(infoNaMorzu);
        infoPanel.add(infoHolowane);
        infoPanel.add(infoWPorcie);
        infoPanel.revalidate();
        infoPanel.repaint();
    }
    public synchronized void updateTugSpinner(int minimumTugsRequired, int tugsRequired) {
        int lastCount = getTugCount();
        if (lastCount < minimumTugsRequired) {
            lastCount = minimumTugsRequired;
        }
        if(lastCount > tugsRequired) {
            lastCount = tugsRequired;
        }
        this.setupPanel.remove(tugboatCount);
        tugboatCount = new JSpinner(new SpinnerNumberModel(lastCount, minimumTugsRequired, tugsRequired, 1));
        this.setupPanel.add(tugboatCount, 3);
        setupPanel.revalidate();
        setupPanel.repaint();
    }
    public synchronized void updateQuaySpinner(int countOfShips) {
        int lastCount = getQuayCount();
        if (lastCount >= countOfShips) {
            lastCount = countOfShips - 1;
        }
        this.setupPanel.remove(quayCount);
        if (countOfShips == 1 || countOfShips == 0) {
            quayCount = new JSpinner(new SpinnerNumberModel(1, 1, 1, 1));
        }
        else {
            quayCount = new JSpinner(new SpinnerNumberModel(lastCount, 1, countOfShips-1, 1));
        }
        this.setupPanel.add(quayCount, 5);
        setupPanel.revalidate();
        setupPanel.repaint();
    }
    public synchronized void resetSettings() {
        shipCount = new JSpinner(new SpinnerNumberModel(0,0,25,1));
        quayCount = new JSpinner(new SpinnerNumberModel(1,1,1,1));
        tugboatCount = new JSpinner(new SpinnerNumberModel(0,0,100,1));
        setupPanel.remove(1);
        setupPanel.add(shipCount,1);
        setupPanel.remove(3);
        setupPanel.add(tugboatCount,3);
        setupPanel.remove(5);
        setupPanel.add(quayCount,5);
        setSpinnerListeners();
        setupPanel.revalidate();
        setupPanel.repaint();
    }
    public int getSimulationSpeed() {
        return wyborCzasuSymulacji.getSelectedIndex();
    }
    public int getTugCount() {

        return (Integer) tugboatCount.getValue();
    }
    public int getShipCount() {
        try {
            shipCount.commitEdit();
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return (Integer) shipCount.getValue();
    }
    public int getQuayCount() {
        try {
            quayCount.commitEdit();
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return (Integer) quayCount.getValue();
    }
}
