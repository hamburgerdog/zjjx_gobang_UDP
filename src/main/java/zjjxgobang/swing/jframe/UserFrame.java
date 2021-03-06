package zjjxgobang.swing.jframe;

import org.apache.ibatis.io.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import zjjxgobang.game.GobangClient;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@org.springframework.stereotype.Component
public class UserFrame extends JFrame {

    private GobangClient client;

    public GobangClient getClient() {
        return client;
    }

    public void setClient(GobangClient client) {
        this.client = client;
    }

    @Autowired
    LoginFrame loginFrame;

    @Autowired
    RegisterFrame registerFrame;

    public UserFrame() throws HeadlessException {
        super("欢迎来到欢乐五子棋");
        this.setSize(new Dimension(600,500));
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        Container rootPanel = this.getContentPane();

        rootPanel.setSize(new Dimension(600,500));
        rootPanel.setLayout(new BorderLayout());

        JPanel centerPanel = new UserJPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setSize(new Dimension(600,200));


        JButton loginButton = new JButton("登录");
        loginButton.setSize(new Dimension(80,60));
        JButton registerButton = new JButton("注册");
        registerButton.setSize(new Dimension(80,60));

        Box verticalBox = Box.createVerticalBox();

        Box horizontalBox = Box.createHorizontalBox();
        Component centerSplit = Box.createHorizontalStrut(40);
        horizontalBox.add(loginButton);
        horizontalBox.add(centerSplit);
        horizontalBox.add(registerButton);

        Component topMargin = Box.createVerticalStrut(200);
        verticalBox.add(topMargin);
        verticalBox.add(horizontalBox);

        centerPanel.add(verticalBox,BorderLayout.CENTER);

        rootPanel.add(centerPanel,BorderLayout.CENTER);

        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                createLoginFrame();
            }
        });

        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                createRegisterFrame();
            }
        });

    }

    public void createLoginFrame(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                loginFrame.setVisible(true);
            }
        });
    }

    public void createRegisterFrame(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                registerFrame.setVisible(true);
            }
        });
    }

    private class UserJPanel extends JPanel{
        @Override
        protected void paintComponent(Graphics g) {
            BufferedImage img = null;
            try {
                String imgUrl = "UserFrame.png";
                InputStream resource = Resources.getResourceAsStream(imgUrl);
                img = ImageIO.read(resource);
                int width = img.getWidth(this);
                int height = img.getHeight(this);

                Graphics2D g2d = (Graphics2D) g.create();
                g2d.drawImage(img,0,0,width,height,this);
                g2d.dispose();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
