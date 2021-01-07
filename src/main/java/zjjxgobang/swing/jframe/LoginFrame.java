package zjjxgobang.swing.jframe;

import org.apache.ibatis.io.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import zjjxgobang.game.GobangClient;
import zjjxgobang.swing.jpanel.ConfirmJPanel;
import zjjxgobang.swing.jpanel.InputNormalJPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.SocketTimeoutException;

@org.springframework.stereotype.Component
public class LoginFrame extends JFrame {
    public LoginFrame thisFrame = this;

    @Autowired
    public GobangClient client;

    @Autowired
    public FindGameFrame findGameFrame;

    private LoginFrame loginFrame = this;

    public LoginFrame() throws HeadlessException {
        super("登录用户");
        this.setSize(new Dimension(300, 200));
        this.setResizable(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel contentJpanel = new LoginJPanel();
        this.setContentPane(contentJpanel);
        contentJpanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        contentJpanel.setSize(new Dimension(300, 200));

        InputNormalJPanel emailJpanel = new InputNormalJPanel("邮箱：");
        InputNormalJPanel pwdJpanel = new InputNormalJPanel("密码：", true);

        ConfirmJPanel confirmJpanel = new ConfirmJPanel();
        JButton confirmButton = confirmJpanel.getConfirmButton();

        Box verticalBox = Box.createVerticalBox();
        Component topMargin = Box.createVerticalStrut(30);

        verticalBox.add(topMargin);
        verticalBox.add(emailJpanel);
        verticalBox.add(pwdJpanel);
        verticalBox.add(confirmJpanel);

        contentJpanel.add(verticalBox);

        confirmButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String email = emailJpanel.getMsg();
                String pwd = pwdJpanel.getMsg();
                client.setEmail(email);
                boolean login = client.sendMsg("login;"+email+";"+pwd+";");
                if (!login) {
                    pwdJpanel.cleanText();
                    JOptionPane.showMessageDialog(null, "服务器拒绝登录", "登录失败", JOptionPane.ERROR_MESSAGE);
                    return;
                }else {
                    thisFrame.setVisible(false);
                    findGameFrame.setVisible(true);
                    Thread thread = new Thread(new FindGameTask());
                    thread.start();
                }
            }
        });
    }

    private class LoginJPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            BufferedImage img = null;
            try {
                img = ImageIO.read(Resources.getResourceAsStream("bg.png"));
                int width = img.getWidth(this);
                int height = img.getHeight(this);

                Graphics2D g2d = (Graphics2D) g.create();
                g2d.drawImage(img, 0, 0, width, height, this);
                g2d.dispose();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class FindGameTask implements Runnable{
        @Override
        public void run() {
            boolean findGame = false;
            try {
                findGame = client.findGame();
            } catch (SocketTimeoutException e) {
                System.err.println("服务器连接失败");
                JOptionPane.showMessageDialog(null, "服务器连接失败", "连接失败", JOptionPane.ERROR_MESSAGE);
            }
            if (!findGame){
                JOptionPane.showMessageDialog(null, "服务器找不到对局", "创建对局失败", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

}
