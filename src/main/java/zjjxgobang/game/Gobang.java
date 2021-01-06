package zjjxgobang.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zjjxgobang.swing.jframe.LoserFrame;
import zjjxgobang.swing.jframe.WinnerFrame;

@Component
public class Gobang {
    private Integer playerId;
    private Integer nowPlayeId = 1;

    @Autowired
    private WinnerFrame winnerFrame;

    public void showWinnerGUI(){
        winnerFrame.setVisible(true);
    }

    @Autowired
    private LoserFrame loserFrame;

    public void showLoserGUI(){
        loserFrame.setVisible(true);
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Integer getNowPlayeId() {
        return nowPlayeId;
    }

    public void setNowPlayeId(Integer nowPlayeId) {
        this.nowPlayeId = nowPlayeId;
    }

    public void changePlayerId(){
        if(nowPlayeId == 1){
            nowPlayeId = 2;
        }else {
            nowPlayeId = 1;
        }
    }
}
