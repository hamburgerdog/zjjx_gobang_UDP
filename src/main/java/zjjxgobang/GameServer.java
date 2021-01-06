package zjjxgobang;

import zjjxgobang.server.GobangServer;

import java.net.SocketException;

public class GameServer {
    public static void main(String[] args) throws SocketException {
        GobangServer gobangServer = new GobangServer();
        gobangServer.createGame();
    }
}
