package server.handler.implementation;

import server.Server;
import server.exception.ServerException;
import server.handler.Handler;
import server.handler.implementation.helper.ByteArrayGiver;
import server.protocol.Client;
import server.protocol.Message;
import server.protocol.Room;
import server.protocol.Type;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class RoomCreateHandler implements Handler {
    private Server server;
    private Handler messageTransform;

    public RoomCreateHandler(Server server) {
        this.server = server;
        this.messageTransform = new MessageTransform();
    }

    @Override
    public void handleMessage(Client client, Message message) {
        System.out.println(new String(message.getData()));
        try {
            messageTransform.handleMessage(client, message);
            Room room = new Room(Room.createRoomUniqueString());
            room.addClient(client);

            byte[] bytes = ByteArrayGiver.toByteArray(room.getRoomUniqueString());

            Message answer = Message.createMessage(Type.ROOM_CREATE_ANSWER, bytes);
            server.sendMessage(client, answer);
        } catch (ServerException ex) {
            //Add some catch implementation
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getType() {
        return Type.ROOM_CREATE;
    }
}
