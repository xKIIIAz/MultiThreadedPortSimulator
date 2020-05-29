package Port;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.util.Random;

public class Ship implements Runnable {
    private ImageIcon imageOnSea;
    private ImageIcon imageDocked;
    private ImageIcon imageTugged;
    private volatile JLabel jLabel;
    private volatile String state = "onSea";
    private Random random;
    private int variantOfShip;
    private int countOfTugsRequired;
    private Port port;
    private String shipName = "";
    private volatile boolean paused = false;

    private Simulation simulation;
    private LoggerPanel loggerPanel;
    private int simulationSpeed;
    private int[] baseTime = {10, 300, 1000};
    private int[] randBound = {5, 125, 200};
    private int[] iterationsCount = {30,5,3};

    public Ship(String shipName,Simulation simulation, LoggerPanel loggerPanel) {
        this.shipName = shipName;
        correctShipName();
        this.loggerPanel = loggerPanel;
        this.simulation = simulation;
        chooseShipType();
        setImages();
        setjLabel();
    }

    private void chooseShipType() {
        random = new Random();
        variantOfShip = random.nextInt(4);
        countOfTugsRequired = variantOfShip+1;
    }
    private void correctShipName() {
        int max = 10;
        int snl = this.shipName.length();
        max = max - snl;
        for (int i = 0; i < max ;i++) {
            this.shipName = this.shipName + " ";
        }
    }
    @Override
    public void run() {
        int myPlace = 0;
        loggerPanel.println(shipName + ": pływam na morzu.");
        doYourThingOnSea();
        for(int i = 0; i < iterationsCount[simulationSpeed]; i++) {
            checkPaused();
            myPlace = port.getPlace();
            loggerPanel.println(shipName + ": znaleziono miejsce na przystani.");
            loggerPanel.println(shipName + ": oczekuję na holowniki, aby móc przybić do portu.");
            checkPaused();
            port.getTugs(countOfTugsRequired);
            loggerPanel.println(shipName + ": uzyskano zgodę na przybicie do portu i następującą liczbę holowników: " + countOfTugsRequired);
            checkPaused();
            loggerPanel.println(shipName + ": trwa holowanie.");
            beTugged();
            checkPaused();
            loggerPanel.println(shipName + ": holowanie zakończone.");
            loggerPanel.println(shipName + ": zwalniam następującą liczbę holowników: " + countOfTugsRequired);
            port.releaseTugs(countOfTugsRequired);
            loggerPanel.println(shipName + ": jestem w porcie.");
            doYourThingAtPort();
            checkPaused();
            loggerPanel.println(shipName + ": czas opuścić port.");
            loggerPanel.println(shipName + ": oczekuję na holowniki, aby móc odbić z portu.");
            port.getTugs(countOfTugsRequired);
            loggerPanel.println(shipName + ": uzyskano zgodę na odbicie z portu i następującą liczbę holowników: " + countOfTugsRequired);
            loggerPanel.println(shipName + ": trwa holowanie.");
            beTugged();
            checkPaused();
            loggerPanel.println(shipName + ": holowanie zakończone.");
            loggerPanel.println(shipName + ": zwalniam następującą liczbę holowników: " + countOfTugsRequired);
            port.releaseTugs(countOfTugsRequired);
            port.releasePlace(myPlace);
            loggerPanel.println(shipName + ": pływam na morzu.");
            doYourThingOnSea();
        }
        simulation.addToFinished();
    }
    private void checkPaused() {
        while (paused) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void beTugged() {
        state = "tugged";
        setjLabel();
        simulation.pokeForUpdate();
        try{
            Thread.sleep(baseTime[simulationSpeed]/2 + random.nextInt(randBound[simulationSpeed]/2));
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void doYourThingAtPort() {
        state = "docked";
        setjLabel();
        simulation.pokeForUpdate();
        try{
            Thread.sleep(baseTime[simulationSpeed] + random.nextInt(randBound[simulationSpeed]));
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void doYourThingOnSea() {
        state = "onsea";
        setjLabel();
        simulation.pokeForUpdate();
        try{
            Thread.sleep(baseTime[simulationSpeed] + random.nextInt(randBound[simulationSpeed]));
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void setImages() {
        if (variantOfShip == 0) {
            imageOnSea = ImgUtil.loadImage("lib/tx/onsea/blue.png");
            imageDocked = ImgUtil.loadImage("lib/tx/idle/blue.png");
            imageTugged = ImgUtil.loadImage("lib/tx/busy/blue.png");
        }
        if (variantOfShip == 1) {
            imageOnSea = ImgUtil.loadImage("lib/tx/onsea/red.png");
            imageDocked = ImgUtil.loadImage("lib/tx/idle/red.png");
            imageTugged = ImgUtil.loadImage("lib/tx/busy/red.png");

        }
        if (variantOfShip == 2) {
            imageOnSea = ImgUtil.loadImage("lib/tx/onsea/green.png");
            imageDocked = ImgUtil.loadImage("lib/tx/idle/green.png");
            imageTugged = ImgUtil.loadImage("lib/tx/busy/green.png");
        }
        if (variantOfShip == 3) {
            imageOnSea = ImgUtil.loadImage("lib/tx/onsea/yellow.png");
            imageDocked = ImgUtil.loadImage("lib/tx/idle/yellow.png");
            imageTugged = ImgUtil.loadImage("lib/tx/busy/yellow.png");
        }
    }
    public void setjLabel() {
        if (state.equalsIgnoreCase("onsea")) {
            jLabel = new JLabel(shipName, imageOnSea, JLabel.CENTER);
        }
        if (state.equalsIgnoreCase("docked")) {
            jLabel = new JLabel(shipName, imageDocked, JLabel.CENTER);
        }
        if (state.equalsIgnoreCase("tugged")) {
            jLabel = new JLabel(shipName, imageTugged, JLabel.CENTER);
        }
        jLabel.setHorizontalTextPosition(JLabel.CENTER);
        jLabel.setVerticalTextPosition(JLabel.BOTTOM);
    }
    public void pause() {
        paused = true;
    }
    public void resume() {
        paused = false;
    }
    public String getState() {
        return state;
    }
    public int getCountOfTugsRequired() {
        return countOfTugsRequired;
    }
    public JLabel getLabel() {
        return jLabel;
    }
    public void setPort(Port port) {
        this.port = port;
    }
    public void setSimulationSpeed(int simulationSpeed) {
        this.simulationSpeed = simulationSpeed;
    }
}