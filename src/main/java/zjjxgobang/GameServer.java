package zjjxgobang;

import zjjxgobang.server.GobangServer;

public class GameServer {
    public static void main(String[] args) {
        GobangServer gobangServer = new GobangServer();
        gobangServer.createGame();
    }
}
