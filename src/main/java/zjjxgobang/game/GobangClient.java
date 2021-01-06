package zjjxgobang.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zjjxgobang.swing.jframe.FindGameFrame;
import zjjxgobang.swing.jframe.GameFrame;
import zjjxgobang.swing.jframe.UserFrame;
import zjjxgobang.swing.jpanel.JGamePanel;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

@Component
public class GobangClient {
    @Autowired
    UserFrame userFrame;

    @Autowired
    GameFrame gameFrame;

    @Autowired
    FindGameFrame findGameFrame;

    @Autowired
    Gobang gobang;

    private DatagramSocket socket = new DatagramSocket(0);
    private String hostName;
    private String email;
    private int closeTime;
    private int port;

    public GobangClient() throws SocketException {
        hostName = "localhost";
        port = 3300;
    }

    public int getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(int closeTime) {
        this.closeTime = closeTime;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void createUserFrame() {
        userFrame.setClient(this);
        userFrame.setVisible(true);
    }

    public boolean sendMsg(String msg){
        byte[] requestByte = msg.getBytes();
        byte[] data = new byte[1024];
        try {
            DatagramPacket resquest = new DatagramPacket(requestByte, requestByte.length, InetAddress.getByName(hostName), port);
            DatagramPacket response = new DatagramPacket(data, data.length);
            socket.send(resquest);
            socket.receive(response);
            String responseStr = new String(response.getData(), 0, response.getLength(), "UTF-8");
            System.out.println(responseStr);
            if (!responseStr.equals("ok"))
                return false;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean findGame(){
        String msg = "findGame;"+email+";";
        byte[] requestByte = msg.getBytes();
        byte[] data = new byte[1024];
        try {
            DatagramPacket resquest = new DatagramPacket(requestByte, requestByte.length, InetAddress.getByName(hostName), port);
            DatagramPacket response = new DatagramPacket(data, data.length);
            socket.send(resquest);
            socket.receive(response);
            String responseStr = new String(response.getData(), 0, response.getLength(), "UTF-8");
            System.out.println(responseStr);
            String[] split = responseStr.split(";");
            switch (split[0]){
                case "wait":{
                    Thread.sleep(2000);
                    findGame();
                    break;
                }
                case "begin":{
                    userFrame.setVisible(false);
                    findGameFrame.setVisible(false);
                    gameFrame.setVisible(true);
                    beginGame();
                    break;
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean beginGame(){
        String msg = "beginGame;"+email+";";
        byte[] requestByte = msg.getBytes();
        byte[] data = new byte[1024];
        try {
            DatagramPacket resquest = new DatagramPacket(requestByte, requestByte.length, InetAddress.getByName(hostName), port);
            DatagramPacket response = new DatagramPacket(data, data.length);
            socket.send(resquest);
            socket.receive(response);
            String responseStr = new String(response.getData(), 0, response.getLength(), "UTF-8");
            String[] split = responseStr.split(":");
            if (!split[0].equals("player"))
                return false;
            Integer id = Integer.valueOf(split[1]);
            gobang.setPlayerId(id);
            if (id == 2){
                socket.receive(response);
                responseStr = new String(response.getData(), 0, response.getLength(), "UTF-8");
                String[] split_t = responseStr.split(":");
                if (!split_t.equals("id"))
                    return false;
                ArrayList<JGamePanel> jGamePanels = gameFrame.getjPanelArrayList();
                jGamePanels.get(Integer.valueOf(split_t[1])).updateGobang();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void sendGobang(int id){
        String msg = "gobang;"+id+";";
        byte[] requestByte = msg.getBytes();
        byte[] data = new byte[1024];
        try {
            DatagramPacket resquest = new DatagramPacket(requestByte, requestByte.length, InetAddress.getByName(hostName), port);
            DatagramPacket response = new DatagramPacket(data, data.length);
            socket.send(resquest);
            socket.receive(response);
            String responseStr = new String(response.getData(), 0, response.getLength(), "UTF-8");
            String[] split = responseStr.split(":");
            if (!responseStr.equals("id"))
                return;
            ArrayList<JGamePanel> jGamePanels = gameFrame.getjPanelArrayList();
            jGamePanels.get(Integer.valueOf(split[1])).updateGobang();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
