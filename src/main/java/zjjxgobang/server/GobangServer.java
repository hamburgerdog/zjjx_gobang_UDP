package zjjxgobang.server;

import org.apache.ibatis.session.SqlSession;
import zjjxgobang.dao.GobangPlayerDao;
import zjjxgobang.pojo.GobangPlayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Set;


public class GobangServer {

    zjjxgobang.Utils.mybatisUtil mybatisUtil;

    private HashMap<String, Integer> playerMap = new HashMap<>();
    private HashMap<String, DatagramPacket> requestMap = new HashMap<>();

    private DatagramSocket socket = new DatagramSocket(3300);

    public GobangServer() throws SocketException {
    }

    public void createGame() {
        int game_sum = 0;
        byte[] inBytes = new byte[1024];
        DatagramPacket request = new DatagramPacket(inBytes, 0, inBytes.length);
        try {
            while (true) {
                socket.receive(request);
                String requestStr = new String(request.getData(), 0, request.getLength(), "UTF-8");
                System.out.println(requestStr);
                String[] split = requestStr.split(";");
                SqlSession sqlSession = mybatisUtil.getSqlSession();
                GobangPlayerDao gobangPlayerDao = sqlSession.getMapper(GobangPlayerDao.class);
                switch (split[0]) {
                    case "login": {
                        GobangPlayer gobangPlayer = gobangPlayerDao.searchPlayerByEmail(split[1]);
                        if (gobangPlayer == null) {
                            sendErr(request);
                            break;
                        }
                        if (gobangPlayer.getPwd().equals(split[2])) {
                            byte[] data = "ok".getBytes();
                            DatagramPacket response = new DatagramPacket(data, data.length, request.getAddress(), request.getPort());
                            socket.send(response);
                        } else {
                            sendErr(request);
                        }
                        sqlSession.close();
                        break;
                    }
                    case "register": {
                        gobangPlayerDao.registerPlayer(split[1], split[2]);
                        GobangPlayer gobangPlayer = gobangPlayerDao.searchPlayerByEmail(split[1]);
                        if (gobangPlayer == null) {
                            sendErr(request);
                        } else {
                            byte[] data = "ok".getBytes();
                            DatagramPacket response = new DatagramPacket(data, data.length, request.getAddress(), request.getPort());
                            socket.send(response);
                        }
                        break;
                    }
                    case "findGame": {
                        if (playerMap.get(split[1]) == null) {
                            game_sum += 1;
                            playerMap.put(split[1], game_sum);
                        } else if (game_sum == 2) {
                            byte[] data = "begin;".getBytes();
                            DatagramPacket response = new DatagramPacket(data, data.length, request.getAddress(), request.getPort());
                            socket.send(response);
                            break;
                        }
                        byte[] data = "wait;".getBytes();
                        DatagramPacket response = new DatagramPacket(data, data.length, request.getAddress(), request.getPort());
                        socket.send(response);
                        break;
                    }
                    case "beginGame": {
                        requestMap.put(split[1], saveRequest(request));
                        if (playerMap.get(split[1]) == 1) {
                            byte[] data = "player:1".getBytes();
                            DatagramPacket response = new DatagramPacket(data, data.length, request.getAddress(), request.getPort());
                            socket.send(response);
                            break;
                        } else {
                            byte[] data = "player:2".getBytes();
                            DatagramPacket response = new DatagramPacket(data, data.length, request.getAddress(), request.getPort());
                            socket.send(response);
                            break;
                        }
                    }
                    case "gobang": {
                        Set<String> keySet = requestMap.keySet();
                        for (String key : keySet) {
                            if (key.equals(split[1]))
                                continue;
                            DatagramPacket playerRequest = requestMap.get(key);
                            byte[] data = ("id:"+split[2]).getBytes();
                            DatagramPacket response = new DatagramPacket(data, data.length, playerRequest.getAddress(), playerRequest.getPort());
                            socket.send(response);
                        }
                        break;
                    }
                    case "end":{
                        playerMap.remove(split[2]);
                        game_sum -= 1;
                        byte[] data = "gameOver".getBytes();
                        DatagramPacket response = new DatagramPacket(data, data.length, request.getAddress(), request.getPort());
                        socket.send(response);
                        if (split[1].equals("win")){
                            DatagramPacket winner = requestMap.get(split[2]);
                            gobangPlayerDao.updateWinNumByEmail(split[2]);
                            data = "id:-1".getBytes();
                            DatagramPacket winResponse = new DatagramPacket(data, data.length, winner.getAddress(), winner.getPort());
                            socket.send(winResponse);
                        }else
                            gobangPlayerDao.updateDefeatNumByEmail(split[2]);
                        break;
                    }
                    case "exit":{
                        Set<String> keySet = requestMap.keySet();
                        for (String key : keySet) {
                            DatagramPacket playerRequest = requestMap.get(key);
                            byte[] data = "exit:".getBytes();
                            DatagramPacket response = new DatagramPacket(data, data.length, playerRequest.getAddress(), playerRequest.getPort());
                            socket.send(response);
                        }
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendErr(DatagramPacket request) {
        byte[] data = "err".getBytes();
        DatagramPacket response = new DatagramPacket(data, data.length, request.getAddress(), request.getPort());
        try {
            socket.send(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DatagramPacket saveRequest(DatagramPacket request){
        DatagramPacket result = new DatagramPacket(request.getData(),request.getLength(),request.getSocketAddress());
        return result;
    }

}
