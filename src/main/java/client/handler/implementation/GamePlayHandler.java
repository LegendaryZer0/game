package client.handler.implementation;


import client.handler.EventListener;
import client.handler.Handler;
import client.logic.Client;
import client.protocol.Message;
import client.protocol.Type;
import javafx.util.Pair;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class GamePlayHandler implements Handler {

    private Client client;
    private ArrayList<EventListener> listeners;

    public GamePlayHandler(Client client) {
        this.client = client;
        this.listeners = new ArrayList<>();
    }


    @Override
    public void handleMessage(Message message) {
        try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(message.getData())) {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object object = objectInputStream.readObject();
            if (object instanceof Pair) {
                Pair<Pair<Integer, String>, Integer> pair = (Pair) object; //<id, name> + cursor on game text
                for (EventListener eventListener : listeners) {
                    eventListener.onEventAction(pair);
                }
            }
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getType() {
        return Type.GAME_PLAY;
    }
}
