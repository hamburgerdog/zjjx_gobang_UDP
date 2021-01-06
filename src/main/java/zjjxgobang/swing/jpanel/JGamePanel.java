package zjjxgobang.swing.jpanel;

import org.apache.ibatis.io.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import zjjxgobang.game.Gobang;
import zjjxgobang.game.GobangClient;
import zjjxgobang.swing.jframe.GameFrame;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 棋盘中的棋子落点
 */
@Component
@Scope(value = "prototype")
public class JGamePanel extends JPanel {
    public final int height = 30;
    public final int width = 30;
    public int Id;
    public final int radis = 5;
    public final Color lineColor = new Color(141, 143, 181);
    public final Color enterColor = new Color(165, 149, 166);

    @Autowired
    private Gobang gobang;

    private GobangClient client;

    public void setId(int id) {
        Id = id;
    }

    public JGamePanel thisPanel = this;

    public JGamePanel() {
        this.setBackground(null);
        this.setOpaque(false);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                enterGobang();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (gobang.getPlayerId() == gobang.getNowPlayeId()){
                    client.sendGobang(Id);
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                thisPanel.repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(lineColor);
        g.drawLine(width / 2, 0, width / 2, height);
        g.drawLine(0, height / 2, width, height / 2);
    }

    public void updateGobang(){
        drawGobang(gobang.getNowPlayeId());
        gobang.changePlayerId();
    }

    public void drawGobang(Integer id){
        Graphics g = this.getGraphics();
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        BufferedImage img=null;
        int width=30;
        int height=30;
        if (id == 1) {
            try {
                img = ImageIO.read(Resources.getResourceAsStream("bang1.png"));
                width = img.getWidth(this);
                height = img.getHeight(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                img = ImageIO.read(Resources.getResourceAsStream("bang2.png"));
                width = img.getWidth(this);
                height = img.getHeight(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        g2d.drawImage(img,0,0,width,height,this);
        g2d.dispose();
    }

    public void enterGobang(){
        Graphics g =  this.getGraphics();
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(enterColor);
        g2d.drawOval((width / 2) - radis, (height / 2) - radis, 2 * radis, 2 * radis);
        g2d.fillOval((width / 2) - radis, (height / 2) - radis, 2 * radis, 2 * radis);
        g2d.dispose();
    }

    public int getId() {
        return Id;
    }
}
