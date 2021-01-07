package zjjxgobang.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zjjxgobang.swing.jframe.LoserFrame;
import zjjxgobang.swing.jframe.WinnerFrame;

import java.util.HashMap;

@Component
public class Gobang {
    private Integer playerId;
    private Integer nowPlayeId = 1;

    private HashMap<Integer, Integer> gobangMap = new HashMap<>();

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

    @Autowired
    public GobangClient client;

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

    public void changePlayer(){
        if(nowPlayeId == 1){
            nowPlayeId = 2;
        }else {
            nowPlayeId = 1;
        }
    }

    public boolean hasPutGobang(int id){
        if (gobangMap.get(id)==null)
            return false;
        else
            return true;
    }

    public boolean isEnd(int thisId) {
        int left_up = -21;
        int left_down = 19;
        int right_up = -19;
        int right_down = 21;
        int left = -1;
        int right = 1;
        int up = -20;
        int down = 20;


        if (searchGobang(this, thisId, left_up)) {
            return true;
        }
        if (searchGobang(this, thisId, left_down)) {
            return true;
        }
        if (searchGobang(this, thisId, left)) {
            return true;
        }
        if (searchGobang(this, thisId, right_up)) {
            return true;
        }
        if (searchGobang(this, thisId, right_down)) {
            return true;
        }
        if (searchGobang(this, thisId, right)) {
            return true;
        }
        if (searchGobang(this, thisId, up)) {
            return true;
        }
        if (searchGobang(this, thisId, down)) {
            return true;
        }
        return false;
    }

    public boolean searchGobang(Gobang gobang, int thisId, int way) {
        int tmp;
        int time;
        Integer playerId = gobang.gobangMap.get(thisId);
        time = 1;
        tmp = thisId + way;
        while (true) {
            if (time == 5) {
                nowPlayeId = 0;     //  lock click
                if (playerId == this.playerId) {
                    showWinnerGUI();
                    client.sendEnd(true);
                } else {
                    showLoserGUI();
                    client.sendEnd(false);
                }
                return true;
            }
            if (gobang.gobangMap.get(tmp) != null) {
                if (gobang.gobangMap.get(tmp).equals(playerId)) {
                    time++;
                } else break;
            } else break;
            tmp = tmp + way;
        }
        return false;
    }

    public void putGobang(Integer gobangId){
        gobangMap.put(gobangId,nowPlayeId);
    }

}
