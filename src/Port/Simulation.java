package Port;

import javax.swing.*;
import java.util.ArrayList;

public class Simulation implements Runnable {

    private boolean started;
    private volatile boolean update;
    private ArrayList<Thread> shipBrains;
    private ArrayList<Ship> ships;
    private SimulationPanel simulationPanel;
    private LoggerPanel loggerPanel;
    private MenuPanel menuPanel;
    private volatile int countOfAllTugsRequired;
    private int minimumTugsRequired;
    private int tugsRequested;
    private int quaysRequested;
    private int simulationSpeed;
    private String[] shipNames = {"Victoria", "Tilly", "Titanic", "Persephone", "Esmeralda",
                                "Rivadavia", "Salta", "Ivone", "Penelope", "Pheme",
                                "Venus", "Bella", "Alice", "Julia", "Jolene",
                                "Veronica", "Lana", "Calypso", "Gaia", "Hebe",
                                "Hera", "Thalia", "Selene", "Rhea", "Pola"};
    private volatile int finishedSimulations;

    public Simulation(SimulationPanel simulationPanel, LoggerPanel loggerPanel, MenuPanel menuPanel) {
        started = false;
        ships = new ArrayList<Ship>();
        shipBrains = new ArrayList<>();
        this.simulationPanel = simulationPanel;
        this.loggerPanel = loggerPanel;
        this.menuPanel = menuPanel;
        this.simulationSpeed = 0;
        countOfAllTugsRequired = 0;
        minimumTugsRequired = 0;
        finishedSimulations = 0;
    }
    @Override
    public void run() {
        loggerPanel.println("Symulator : Rozpoczęto symulację.");
        started = true;
        loggerPanel.println("Symulator : Ustawiam ilosc przystani w porcie: " + quaysRequested);
        loggerPanel.println("Symulator : Ustawiam ilość holowników w porcie: " + tugsRequested);
        Port port = new Port(tugsRequested, quaysRequested);
        for (Ship s: ships) {
            s.setPort(port);
            s.setSimulationSpeed(simulationSpeed);
        }
        for (Ship s: ships) {
            shipBrains.add(new Thread(s));
            shipBrains.get(shipBrains.size()-1).start();
        }
        while(finishedSimulations<ships.size()) {
            try {
                Thread.sleep(30);//konieczne aby event dispatch thread nie zwariował, a symulacja była płynnie wyświetlana.
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
            //jezeli jeszcze wszystkie nie skonczyly to nasluchuj na prosby aktualizacji panelu
            if(update) {
                update = false;
                repaintPanel();
                updateInfoPanel();
            }
        }
        this.finishedSimulations = 0;
        started = false;
        loggerPanel.println("Symulator : Zakończono symulację.");
    }
    public synchronized void addToFinished() {
        //function incrementing counter of ships that finished the simulation
        finishedSimulations++;
    }
    public boolean isStarted() {
        return started;
    }


    public void addShip() {
        int currentIndex = ships.size();
        Ship nShip = new Ship(shipNames[currentIndex],this, loggerPanel);
        ships.add(nShip);
        int newShipCountOfTugsRequired = ships.get(currentIndex).getCountOfTugsRequired();
        //ships.get(currentIndex).setShipName(shipNames[currentIndex]);
        if (minimumTugsRequired < newShipCountOfTugsRequired) {
            minimumTugsRequired = newShipCountOfTugsRequired;
        }
        countOfAllTugsRequired += ships.get(ships.size()-1).getCountOfTugsRequired();
        simulationPanel.addShipRepresentation(nShip.getLabel());
        simulationPanel.panelRepaint();
        loggerPanel.println("Utworzono statek: "+shipNames[currentIndex]);
    }
    public void removeShip() {
        int currentIndex = ships.size()-1;
        countOfAllTugsRequired -= ships.get(currentIndex).getCountOfTugsRequired();
        ships.remove(ships.size()-1);
        int min = 0;
        for(Ship s: ships) {
            if(min < s.getCountOfTugsRequired()) {
                min = s.getCountOfTugsRequired();
            }
        }
        minimumTugsRequired = min;
        simulationPanel.removeShipRepresentation();
        simulationPanel.panelRepaint();
        loggerPanel.println("Usunięto statek: " + shipNames[currentIndex]);
    }
    public void stopAndRemoveAll() {
        /*for (Thread t: shipBrains) {

        }*/
        //stop threads!!!!
        ships.clear();
        minimumTugsRequired = 0;
        countOfAllTugsRequired = 0;
    }
    private void repaintPanel() {
        while(simulationPanel.getRepainting()) { }  //czekaj az skonczy malowac

        simulationPanel.clearPanel();
        for(Ship s : ships) {
            simulationPanel.addShipRepresentation(s.getLabel());
        }
        simulationPanel.panelRepaint();
    }

    private void updateInfoPanel() {
        menuPanel.updateInfoPanel(getShipsOnSea(),getShipsDocked(),getShipsTugged());
    }
    public int getShipsCount() {
        return ships.size();
    }
    public int getCountOfAllTugsRequired() {
        return countOfAllTugsRequired;
    }
    public int getMinimumTugsRequired() {
        return minimumTugsRequired;
    }
    public JLabel getLabelOfIndex(int index) {
        return ships.get(index).getLabel();
    }
    public int getShipsOnSea() {
        int count = 0;
        for (Ship s: ships) {
            if (s.getState().equalsIgnoreCase("onsea")) {
                count++;
            }
        }
        return count;
    }
    public int getShipsDocked() {
        int count = 0;
        for (Ship s: ships) {
            if (s.getState().equalsIgnoreCase("docked")) {
                count++;
            }
        }
        return count;
    }
    public int getShipsTugged() {
        int count = 0;
        for (Ship s: ships) {
            if (s.getState().equalsIgnoreCase("tugged")) {
                count++;
            }
        }
        return count;
    }

    public void setSimulationSpeed(int simulationSpeed) {
        this.simulationSpeed = simulationSpeed;
    }
    public void setTugsRequested(int countOfTugsRequested) { this.tugsRequested = countOfTugsRequested; }
    public void setQuaysRequested(int quaysRequested) {
        this.quaysRequested = quaysRequested;
    }
    public void pokeForUpdate() {
        this.update = true;
    }
}
