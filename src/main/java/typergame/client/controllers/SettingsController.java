package typergame.client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import typergame.client.logic.Client;
import typergame.client.message.MessageCreater;
import typergame.client.visualizer.ThemeContext;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    @FXML
    private TextField newName;
    @FXML
    @Getter
    private VBox body;
    @Setter
    private Scene mainScene;
    @Setter
    private Client client;

    public void changeTheme(MouseEvent mouseEvent) {
        if (ThemeContext.currentTheme.equals(ThemeContext.DEFAULT_THEME)) {
            ThemeContext.currentTheme =ThemeContext.DARK_THEME;

            body.setStyle(ThemeContext.DARK_THEME);
        }else {
            ThemeContext.currentTheme =ThemeContext.DEFAULT_THEME;
            body.setStyle(ThemeContext.DEFAULT_THEME);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }

    public void getMainScene(MouseEvent mouseEvent) {
        Stage primaryStage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        ThemeContext.checkTheme(mainScene);
        primaryStage.setScene(mainScene);
        mainScene.getRoot().requestFocus();
    }

    public void saveName(MouseEvent mouseEvent) {
        client.sendMessage(MessageCreater.createNickNameChangeMsg(newName.getText()));
        newName.setText("");
    }
}
