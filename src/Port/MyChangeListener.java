package Port;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MyChangeListener implements ChangeListener {
    InterfaceListener interfaceListener;
    public MyChangeListener(InterfaceListener interfaceListener) {
        this.interfaceListener = interfaceListener;
    }
    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        interfaceListener.pokeCheck();
    }
}
