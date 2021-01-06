package zjjxgobang;

import zjjxgobang.server.GobangClient;

import java.net.InetSocketAddress;


/**
 * 生成游戏界面
 */
public class GamePlayer1 {
    public static void main(String[] args) {
        InetSocketAddress serverAddress = new InetSocketAddress("localhost", 3300);
        GobangClient gobangClient = new GobangClient(serverAddress);

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                gobangClient.createUserFrame();
            }
        });
    }
}
