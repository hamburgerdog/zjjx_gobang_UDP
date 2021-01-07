package zjjxgobang.swing.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zjjxgobang.game.GobangClient;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Component
public class MyWindowsCloseListener extends WindowAdapter {
    @Autowired
    GobangClient gobangClient;

    @Override
    public void windowClosing(WindowEvent e) {
        gobangClient.sendExit();
        System.exit(2);
    }
}
