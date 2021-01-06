package zjjxgobang;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import zjjxgobang.game.GobangClient;

import java.net.SocketException;


/**
 * 生成游戏界面
 */
public class GamePlayer1 {
    private static ApplicationContext ioc = new ClassPathXmlApplicationContext("GobangApplicationConf.xml");

    public static void main(String[] args) throws SocketException {
        String inetAddress = "localhost";
        int port = 3300 ;
        GobangClient gobangClient = ioc.getBean(GobangClient.class);
        gobangClient.setHostName(inetAddress);
        gobangClient.setPort(port);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                gobangClient.createUserFrame();
            }
        });
    }
}
