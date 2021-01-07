package zjjxgobang.swing.jframe;

import org.apache.ibatis.io.Resources;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import zjjxgobang.swing.listener.FrameSetUndecorated;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class LoserFrame extends JFrame {

    private int xOld = 0;
    private int yOld = 0;

    public LoserFrame() throws HeadlessException {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(550, 500);
        LoserPannel winnerJPanel = new LoserPannel();
        winnerJPanel.setSize(new Dimension(550,500));
        this.setContentPane(winnerJPanel);
        new FrameSetUndecorated(this).doSet();
    }

    private class LoserPannel extends JPanel{
        @Override
        protected void paintComponent(Graphics g) {
            try {
                BufferedImage img = ImageIO.read(Resources.getResourceAsStream("loser.png"));
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
