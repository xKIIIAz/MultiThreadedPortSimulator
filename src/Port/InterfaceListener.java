package Port;

public class InterfaceListener implements Runnable{
    private volatile boolean check;
    private volatile boolean start;
    public volatile boolean reset;
    public volatile boolean pause;

    private int shipCount;
    private int tugCount;
    private int quayCount;
    private int simulationSpeed;
    private Thread simulationThread;
    private MenuPanel menuPanel;
    private LoggerPanel loggerPanel;
    private SimulationPanel simulationPanel;
    private Simulation simulation;

    public InterfaceListener(LoggerPanel loggerPanel, MenuPanel menuPanel, SimulationPanel simulationPanel) {
        this.menuPanel = menuPanel;
        this.loggerPanel = loggerPanel;
        this.simulationPanel = simulationPanel;
        simulation = new Simulation(simulationPanel, loggerPanel, menuPanel);
        check = false;
        start = false;
        reset = false;
        pause = false;
        shipCount = 0;
        tugCount = 0;
        quayCount = 0;
    }

    @Override
    public void run() {
        menuPanel.setInterfaceListener(this);
        simulation.setInterfaceListener(this);
        while(true) {
            listenIfChecked();
            listenIfStart();
            listenIfReset();
            listenIfPause();
            try{
                Thread.sleep(50);
            }catch (InterruptedException e ) {
                e.printStackTrace();
            }
        }
    }

    private void listenIfChecked() {
        if(check) {
            check = false;
            if(!simulation.isStarted()){
                updateShipsAndSpinners();
                updateInfoValues();
            }
        }
    }
    private void listenIfStart() {
        if(start) {
            start = false;
            if(simulation.isStarted()) {
                loggerPanel.println("***********************************************************************");
                loggerPanel.println("*Symulacja już trwa! Spróbuj ją zatrzymać za pomocą przycisku PAUSE.*");
                loggerPanel.println("***********************************************************************");
            }else {
                if(simulation.isPaused()) {
                    simulation.setResume();
                }else if(ableToStart()){
                    simulation.setTugsRequested(tugCount);
                    simulation.setQuaysRequested(quayCount);
                    simulationThread = new Thread(simulation);
                    simulationThread.start();
                }else {
                    loggerPanel.println("Symulacja : Nie udało się rozpocząć symulacji.");
                }
            }
        }
    }
    private void listenIfPause() {
        if(pause) {
            pause = false;
            if(!simulation.isStarted()) {
                loggerPanel.println("*******************************************************************");
                loggerPanel.println("*                  Symulacja już jest zatrzymana.                 *");
                loggerPanel.println("*******************************************************************");
            }
            else {
                simulation.setPause();
            }
        }
    }
    private void listenIfReset() {
        if(reset) {
            reset = false;
            if(!(simulation.isStarted() || simulation.isPaused())) {
                resetPanels();
            }
            else {
                loggerPanel.println("*******************************************************************");
                loggerPanel.println("*            SYMULACJA NADAL TRWA, MUSI DOBIEC DO KOŃCA!          *");
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
        return true;
    }
    private void resetPanels() {
        simulation.stopAndRemoveAll();
        shipCount = 0;
        tugCount = 0;
        quayCount = 0;
        menuPanel.updateInfoPanel(0, 0,0,0);
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
        menuPanel.updateInfoPanel( tugCount, simulation.getShipsOnSea(), simulation.getShipsDocked(), simulation.getShipsTugged());
    }

    public void pokePause() {
        pause = true;
    }
    public void pokeCheck(){
        check = true;
    }
    public void pokeReset() {
        reset = true;
    }
    public void pokeStart() {
        start = true;
    }
}
