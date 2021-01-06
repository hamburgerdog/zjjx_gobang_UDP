package zjjxgobang.swing.listener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class WindowsClosed extends WindowAdapter {
    private GobangClient gobangClient;

    public WindowsClosed(GobangClient gobangClient) {
        this.gobangClient = gobangClient;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        Player player = gobangClient.getPlayer();
        player.sentCloseMsg();
        System.exit(2);
    }
}
