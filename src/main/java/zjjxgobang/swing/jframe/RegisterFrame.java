package zjjxgobang.swing.jframe;

import org.apache.ibatis.io.Resources;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.net.Socket;
import java.net.SocketTimeoutException;

@org.springframework.stereotype.Component
public class RegisterFrame extends JFrame {
    @Autowired
    private FindGameFrame findGameFrame;

    private RegisterFrame thisFrame = this;

    @Autowired
    private GobangClient client;


    private RegisterFrame registerFrame = this ;

    public RegisterFrame() throws HeadlessException {
        super("注册窗口");
        this.setSize(new Dimension(300, 200));
        this.setResizable(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel contentJpanel = new RegisterJPanel();
        this.setContentPane(contentJpanel);
        contentJpanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        contentJpanel.setSize(new Dimension(300, 200));

        InputNormalJPanel emailJpanel = new InputNormalJPanel("注册邮箱：");
        InputNormalJPanel pwdJpanel = new InputNormalJPanel("设置密码：", true);
        InputNormalJPanel reconfirmPwdJpanel = new InputNormalJPanel("确认密码：", true);

        ConfirmJPanel confirmJpanel = new ConfirmJPanel();
        JButton confirmButton = confirmJpanel.getConfirmButton();

        Box verticalBox = Box.createVerticalBox();
        Component topMargin = Box.createVerticalStrut(20);

        verticalBox.add(topMargin);
        verticalBox.add(emailJpanel);
        verticalBox.add(pwdJpanel);
        verticalBox.add(reconfirmPwdJpanel);
        verticalBox.add(confirmJpanel);

        contentJpanel.add(verticalBox);

        confirmButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String email = emailJpanel.getMsg();
                String pwd = pwdJpanel.getMsg();
                String rePwd = reconfirmPwdJpanel.getMsg();

                if (!pwd.equals(rePwd)) {
                    JOptionPane.showMessageDialog(null,
                            "两次输入的密码不一致，请重新填写", "注册错误",
                            JOptionPane.WARNING_MESSAGE);
                    pwdJpanel.cleanText();
                    reconfirmPwdJpanel.cleanText();
                }else {
                    boolean canRegister = client.sendMsg("register;" + email + ";" + pwd + ";");
                    if (!canRegister){
                        emailJpanel.cleanText();
                        pwdJpanel.cleanText();
                        reconfirmPwdJpanel.cleanText();
                        JOptionPane.showMessageDialog(null, "服务器拒绝注册", "注册失败", JOptionPane.ERROR_MESSAGE);
                        return;
                    }else {
                        findGameFrame.setVisible(true);
                        thisFrame.setVisible(false);
                        boolean findGame = false;
                        try {
                            findGame = client.findGame();
                        } catch (SocketTimeoutException socketTimeoutException) {
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
        });
    }


    private class RegisterJPanel extends JPanel {
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
}
