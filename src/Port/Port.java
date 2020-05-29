package Port;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Port {
    private volatile int tugsAvailable;
    private volatile int quaysAvailable;
    private List<ReentrantLock> quayList;
    private Semaphore tugs;
    public Port(int tugsRequested, int quaysRequested) {
        this.quaysAvailable = quaysRequested;
        this.tugsAvailable = tugsRequested;
        quayList = new ArrayList<ReentrantLock>();
        for (int i = 0; i<quaysRequested; i++) {
            quayList.add(new ReentrantLock());
        }
        tugs = new Semaphore(tugsRequested,true);
        //konieczne ze względu na możliwość niewystarczającej ilości holownikow
        //jezeli nie zaznaczylibysmy opcji fair, wtedy procesom groziłoby zjawisko
        //zagłodzenia
    }
    public int getPlace() {
        //here ship waits for a place to free
        while(true) {
            //System.out.println("Trying to find a quay...");
            for (int i = 0; i < quaysAvailable; i++) {
                if (quayList.get(i).tryLock()) {
                    return i;
                }
            }
        }
    }
    public void getTugs(int amtOfTugsNeeded) {
        //System.out.println("Trying to find tugs...");
        //here ship tries to accquire enough amount of tugs
        try {
            tugs.acquire(amtOfTugsNeeded);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void releaseTugs(int amtOfTugsNeeded) {
        tugs.release(amtOfTugsNeeded);
    }
    public void releasePlace(int ind) {
        quayList.get(ind).unlock();
    }
}
