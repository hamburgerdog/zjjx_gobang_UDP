package zjjxgobang;

import zjjxgobang.game.GobangClient;

import java.net.InetSocketAddress;

public class GamePlayer2 {
    public static void main(String[] args) {
        String inetAddress = "localhost";
        int port = 3300 ;
        GobangClient gobangClient = new GobangClient();

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                gobangClient.createUserFrame();
            }
        });
    }
}
