package zjjxgobang;

import zjjxgobang.game.GobangClient;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;


/**
 * 生成游戏界面
 */
public class GamePlayer1 {
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
