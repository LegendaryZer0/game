package typergame.server;


import typergame.server.exception.ServerException;
import typergame.server.handler.handlerImpl.*;
import typergame.server.repository.impl.RecordRepoImpl;

public class Main {
    public static void main(String[] args) throws ServerException {
        Server server = new Server(4888);
        server.registerListener(new GamePlayHandler(server));
        server.registerListener(new GameStartHandler(server));
        server.registerListener(new NameChangeHandler(server));
        server.registerListener(new RecordGetHandler(server, new RecordRepoImpl()));
        server.registerListener(new RoomConnectHandler(server));
        server.registerListener(new RoomCreateHandler(server));
        server.registerListener(new RoomRandomConnectHandler(server));
        server.start();
    }
}
