package client.handler.handlerImpl;


import client.visualizer.EventListener;
import client.handler.Handler;
import client.logic.Client;
import client.protocol.Message;
import client.protocol.Type;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
@Slf4j
public class GamePlayHandler implements Handler {
    private String theme;
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
                Pair<Integer, Integer> pair = (Pair) object; //id + cursor on game text
                for (EventListener eventListener : listeners) {
                    eventListener.onEventAction(pair);
                }
            }
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
    public void addEventListener(EventListener eventListener) {
        listeners.add(eventListener);
    }

    @Override
    public int getType() {
        return Type.GAME_PLAY;
    }
}
