package zjjxgobang.swing.jframe;

import org.apache.ibatis.io.Resources;
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

public class LoginFrame extends JFrame {

    private LoginFrame loginFrame = this;

    public LoginFrame(String title) throws HeadlessException {
        super(title);
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

}
