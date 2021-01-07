package zjjxgobang;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import zjjxgobang.game.GobangClient;

import javax.swing.*;
import java.net.SocketException;
import java.net.SocketTimeoutException;


/**
 * 生成游戏界面
 */
public class GamePlayer1 {
    private static ApplicationContext ioc = new ClassPathXmlApplicationContext("GobangApplicationConf.xml");

    public static void main(String[] args) throws SocketException {
        String inetAddress = "localhost";
        int serverPort = 3300 ;
        int clientPort = 5001 ;
        GobangClient gobangClient = ioc.getBean(GobangClient.class);
        gobangClient.setHostName(inetAddress);
        gobangClient.setServerPort(serverPort);
        try {
            gobangClient.setClientPort(clientPort);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "连接超时拒绝登录", "连接失败", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                gobangClient.createUserFrame();
            }
        });
    }
}
