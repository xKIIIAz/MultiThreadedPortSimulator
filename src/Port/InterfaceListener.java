package Port;

public class InterfaceListener implements Runnable{
    private volatile boolean check;
    private volatile boolean start;
    public volatile boolean reset;
    private boolean menuActivated;

    private int shipCount;
    private int tugCount;
    private int quayCount;
    private int simulationSpeed;
    private Thread simulationThread;

    private boolean valuesSet = false;
    private MenuPanel menuPanel;
    private LoggerPanel loggerPanel;
    private SimulationPanel simulationPanel;
    private Simulation simulation;

    public InterfaceListener(LoggerPanel loggerPanel, MenuPanel menuPanel, SimulationPanel simulationPanel) {
        this.menuPanel = menuPanel;
        this.loggerPanel = loggerPanel;
        this.simulationPanel = simulationPanel;
        simulation = new Simulation(simulationPanel, loggerPanel, menuPanel);
        menuActivated = false;
        check = false;
        start = false;
        reset = false;
        shipCount = 0;
        tugCount = 0;
        quayCount = 0;
    }

    @Override
    public void run() {
        if(!menuActivated) {        //it is not necessary while we start InterfaceListener only once
            menuActivated = true;
            menuPanel.setInterfaceListener(this);
        }
        while(true) {
            listenIfChecked();
            listenIfStart();
            listenIfReset();
            try{
                Thread.sleep(30);
            }catch (InterruptedException e ) {
                e.printStackTrace();
            }
        }
    }
    private void listenIfChecked() {
        if(check) {
            check = false;
            updateShipsAndSpinners();
            updateInfoValues();
        }
    }
    private void listenIfStart() {
        if(start) {
            start = false;
            if(ableToStart()) {
                simulation.setTugsRequested(tugCount);
                simulation.setQuaysRequested(quayCount);
                simulationThread = new Thread(simulation);
                simulationThread.start();
            }
            else {
                loggerPanel.println("Symulacja : Nie udało się rozpocząć symulacji.");
            }
        }
    }
    private void listenIfReset() {
        if(reset) {
            reset = false;
            if(!simulation.isStarted()){
                resetPanels();
            }
            else {
                loggerPanel.println("*******************************************************************");
                loggerPanel.println("*            SYMULACJA NADAL TRWA, ZACZEKAJ AŻ SIĘ ZAKOŃCZY!      *");
                loggerPanel.println("*******************************************************************");
            }
        }
    }

    private boolean ableToStart() {
        if(shipCount<=0) {
            loggerPanel.println("*******************************************************************");
            loggerPanel.println("*   Niewystarczająca liczba statków aby uruchomić symulację!      *");
            loggerPanel.println("*   Dodaj statki do symulacji w ustawieniach i spróbuj ponownie.  *");
            loggerPanel.println("*******************************************************************");
            return false;
        }
        if(simulation.isStarted()) {
            loggerPanel.println("***********************************************************************");
            loggerPanel.println("*Symulacja nadal trwa! Spróbuj ją zatrzymać za pomocą przycisku RESET.*");
            loggerPanel.println("***********************************************************************");
            return false;
        }
        return true;
    }
    private void resetPanels() {
        simulation.stopAndRemoveAll();
        shipCount = 0;
        tugCount = 0;
        quayCount = 0;
        menuPanel.updateInfoPanel(0,0,0);
        simulationPanel.clearPanel();
        simulationPanel.panelRepaint();
        loggerPanel.logPrompt.setText("");
        menuPanel.resetSettings();
    }

    private void updateShipsAndSpinners() {
        //function reacting to pokeCheck()
        //read simulationSpeed and save it in class's variable
        this.simulationSpeed = menuPanel.getSimulationSpeed();
        this.simulation.setSimulationSpeed(simulationSpeed);
        //assign amount of ships requested to the local variable
        int shipsRequested = menuPanel.getShipCount();

        //add or remove amount of ships depending on difference
        if(shipCount < shipsRequested) {
            int diff = shipsRequested - shipCount;
            for (int i = 0; i < diff; i++) {
                simulation.addShip();
            }
        }else if (shipCount > shipsRequested) {
            int diff = shipCount - shipsRequested;
            for (int i = 0; i < diff; i++) {
                simulation.removeShip();
            }
        }

        //update spinners so that values will not destroy simulation or collide with assumptions given for the task
        if(this.shipCount!=shipsRequested) {
            menuPanel.updateTugSpinner(simulation.getMinimumTugsRequired(), simulation.getCountOfAllTugsRequired());
            menuPanel.updateQuaySpinner(shipsRequested);
            menuPanel.setSpinnerListeners();    //important, because update functions recreate spinners without listeners
        }
        //update local values
        this.quayCount = menuPanel.getQuayCount();
        this.tugCount = menuPanel.getTugCount();
        this.shipCount = shipsRequested;

    }
    private void updateInfoValues() {
        //function connecting information between simulation and MenuPanel's infoPanel
        menuPanel.updateInfoPanel( simulation.getShipsOnSea(), simulation.getShipsDocked(), simulation.getShipsTugged());
    }
    private void startSimulation() {

    }
    private void resetSimulation() {

    }
    public synchronized void pokeCheck(){
        check = true;
    }
    public synchronized void pokeReset() {
        reset = true;
    }
    public synchronized void pokeStart() {
        start = true;
    }
}
