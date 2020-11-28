package client.visualizer;

import client.handler.EventListener;
import javafx.scene.control.Label;
import javafx.scene.text.Text;


public class RoomConnectPrinter implements EventListener<String> {

    private Text textStatus;

    public RoomConnectPrinter(Text textStatus) {
        this.textStatus = textStatus;
    }

    @Override
    public void onEventAction(String object) {
        textStatus.setText(object);
    }
}