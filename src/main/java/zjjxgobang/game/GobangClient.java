package zjjxgobang.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zjjxgobang.swing.jframe.FindGameFrame;
import zjjxgobang.swing.jframe.GameFrame;
import zjjxgobang.swing.jframe.UserFrame;
import zjjxgobang.swing.jpanel.JGamePanel;
import zjjxgobang.swing.listener.MyWindowsCloseListener;

import javax.swing.*;
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

    @Autowired
    MyWindowsCloseListener myWindowsCloseListener;

    private DatagramSocket socket;
    private String hostName;
    private String email;
    private int closeTime;
    private int serverPort;

    public GobangClient() throws SocketException {
    }

    public void setClientPort(int clientPort) throws SocketException {
        socket = new DatagramSocket(clientPort);
        socket.setSoTimeout(100000);
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

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public void createUserFrame() {
        userFrame.setClient(this);
        userFrame.setVisible(true);
    }

    public boolean sendMsg(String msg){
        byte[] requestByte = msg.getBytes();
        byte[] data = new byte[1024];
        try {
            DatagramPacket resquest = new DatagramPacket(requestByte, requestByte.length, InetAddress.getByName(hostName), serverPort);
            DatagramPacket response = new DatagramPacket(data, data.length);
            socket.send(resquest);
            socket.receive(response);
            String responseStr = new String(response.getData(), 0, response.getLength(), "UTF-8");
            System.out.println(responseStr);
            if (!responseStr.equals("ok"))
                return false;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e){
            JOptionPane.showMessageDialog(null, "连接超时拒绝登录", "连接失败", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean findGame() throws SocketTimeoutException{
        String msg = "findGame;"+email+";";
        byte[] requestByte = msg.getBytes();
        byte[] data = new byte[1024];
        try {
            DatagramPacket resquest = new DatagramPacket(requestByte, requestByte.length, InetAddress.getByName(hostName), serverPort);
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
                    gameFrame.setClientAndGobang2Jpanel();
                    gameFrame.setVisible(true);
                    gameFrame.addWindowListener(myWindowsCloseListener);
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
            DatagramPacket resquest = new DatagramPacket(requestByte, requestByte.length, InetAddress.getByName(hostName), serverPort);
            DatagramPacket response = new DatagramPacket(data, data.length);
            socket.send(resquest);
            socket.receive(response);
            String responseStr = new String(response.getData(), 0, response.getLength(), "UTF-8");
            System.out.println(responseStr);
            String[] split = responseStr.split(":");
            if (!split[0].equals("player"))
                return false;
            Integer id = Integer.valueOf(split[1]);
            gobang.setPlayerId(id);
            if (id == 2){
                socket.receive(response);
                responseStr = new String(response.getData(), 0, response.getLength(), "UTF-8");
                String[] split_t = responseStr.split(":");
                if (!split_t[0].equals("id"))
                    return false;
                System.out.println(split_t);
                ArrayList<JGamePanel> jGamePanels = gameFrame.getjPanelArrayList();
                jGamePanels.get(Integer.valueOf(split_t[1])).updateGobang();
            }
        }catch (SocketTimeoutException e){
            JOptionPane.showMessageDialog(null, "连接超时拒绝登录", "连接失败", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void sendGobang(int id){
        String msg = "gobang;"+email+";"+id+";";
        byte[] requestByte = msg.getBytes();
        byte[] data = new byte[1024];
        try {
            DatagramPacket resquest = new DatagramPacket(requestByte, requestByte.length, InetAddress.getByName(hostName), serverPort);
            DatagramPacket response = new DatagramPacket(data, data.length);
            socket.send(resquest);
            socket.receive(response);
            String responseStr = new String(response.getData(), 0, response.getLength(), "UTF-8");
            String[] split = responseStr.split(":");
            if (split[0].equals("exit")){
                JOptionPane.showMessageDialog(null, "对手退出游戏结束", "对局结束", JOptionPane.ERROR_MESSAGE);
                System.exit(4);
                return;
            }
            if (!split[0].equals("id") || split[1].equals("-1"))
                return;
            System.out.println(responseStr);
            ArrayList<JGamePanel> jGamePanels = gameFrame.getjPanelArrayList();
            jGamePanels.get(Integer.valueOf(split[1])).updateGobang();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendEnd(boolean isWinner){
        String msg;
        if (isWinner)
             msg = "end;win;"+email+";";
        else
            msg = "end;defeat;"+email+";";
        byte[] requestByte = msg.getBytes();
        byte[] data = new byte[1024];
        try {
            DatagramPacket resquest = new DatagramPacket(requestByte, requestByte.length, InetAddress.getByName(hostName), serverPort);
            DatagramPacket response = new DatagramPacket(data, data.length);
            socket.send(resquest);
            socket.receive(response);
            String responseStr = new String(response.getData(), 0, response.getLength(), "UTF-8");
            String[] split = responseStr.split(":");
            if (!split[0].equals("gameOver"))
                return;
            System.out.println(responseStr);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendExit(){
        String msg = "exit;";
        byte[] requestByte = msg.getBytes();
        try {
            DatagramPacket resquest = new DatagramPacket(requestByte, requestByte.length, InetAddress.getByName(hostName), serverPort);
            socket.send(resquest);
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
