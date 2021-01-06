package zjjxgobang.swing.jframe;

import org.apache.ibatis.io.Resources;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import zjjxgobang.swing.listener.FrameSetUndecorated;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Component
public class FindGameFrame extends JFrame {
    JPanel jPanel = new JPanel();
    private int xOld = 0;
    private int yOld = 0;

    public FindGameFrame() throws HeadlessException {
        this.setResizable(false);
        this.setSize(400, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jPanel.setSize(new Dimension(400, 300));
        this.setContentPane(jPanel);

        new FrameSetUndecorated(this).doSet();

        jPanel.setLayout(new BorderLayout());
        WaitPanel waitPanel = new WaitPanel();
        waitPanel.setSize(new Dimension(400,300));
        jPanel.add(waitPanel, BorderLayout.CENTER);

    }

    private class WaitPanel extends JPanel{
        @Override
        protected void paintComponent(Graphics g) {
            try {
                BufferedImage img = ImageIO.read(Resources.getResourceAsStream("gobang_wait.png"));
                int height = img.getHeight(this);
                int width = img.getWidth(this);

                Graphics2D g2d = (Graphics2D) g.create();
                g2d.drawImage(img,0,0,width,height,this);
                g2d.dispose();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
