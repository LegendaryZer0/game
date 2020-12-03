package typergame.client;

import typergame.client.controllers.*;
import typergame.client.handler.handlerImpl.*;
import typergame.client.logic.Client;
import typergame.client.model.Room;
import typergame.client.visualizer.eventListImpl.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;

@Slf4j
public class ClientStart extends Application {
    public TextField textField;
    public Text textStatus;

    private Parent root;
    private Parent wint;
    private Parent mainGame;
    private Parent loose;
    private Parent records;
    private Parent settings;
    private Controller controller;
    private MainGameController controller1;
    private WinController controller3;
    private LostController controller4;
    private RecordsController controller5;
    private SettingsController settingsController;
    private Client client;
    private Scene scene;
    private FXMLLoader loader;
    private FXMLLoader loader2;
    private FXMLLoader loader3;
    private FXMLLoader loader4;
    private FXMLLoader loader5;
    private FXMLLoader settingsLoader;
    private RoomCreateHandler roomCreateHandler;
    private RoomConnectHandler roomConnectHandler;
    private RivalConnectHandler rivalConnectHandler;
    private GameStartHandler gameStartHandler;
    private GamePlayHandler gamePlayHandler;
    private RecordGetHandler recordGetHandler;
    private RoomCodePrinter roomCodePrinter;
    private RoomConnectPrinter roomConnectPrinter;
    private RivalPrinter rivalPrinter;
    private GameTextPrinter gameTextPrinter;
    private ColorMixPrinter colorMixPrinter;
    private RecordsCodePrinter recordsCodePrinter;

    private volatile Room room;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        loader = new FXMLLoader(getClass().getResource("/chooseRoomType.fxml"));
        loader2 = new FXMLLoader(getClass().getResource("/gameTextMain.fxml"));
        loader3 = new FXMLLoader(getClass().getResource("/Win.fxml"));
        loader4 = new FXMLLoader(getClass().getResource("/lost.fxml"));
        loader5 = new FXMLLoader(getClass().getResource("/RecordTable.fxml"));
        settingsLoader = new FXMLLoader(getClass().getResource("/settingGame.fxml"));

        try {
            loose = loader4.load();
            root = loader.load();
            wint = loader3.load();
            mainGame = loader2.load();
            records = loader5.load();
            settings = settingsLoader.load();


            controller = loader.getController(); //manuController
            controller1 = loader2.getController();//MainGame controller
            controller3 = loader3.getController();//win scene controller
            controller4 = loader4.getController();//loose scene controller
            controller5 = loader5.getController(); //record scene
            settingsController = settingsLoader.getController();
            scene = new Scene(root);


            client = new Client(InetAddress.getByName("127.0.0.1"), 4888);
            controller.setClient(client);
            controller1.setClient(client);
            settingsController.setClient(client);


            Scene gameScene = new Scene(mainGame);
            Scene recordScene = new Scene(records);
            Scene settingsScene = new Scene(settings);

            controller.setGameScene(gameScene);
            controller.setRecordScene(recordScene);
            controller.setSettingsScene(settingsScene);
            controller1.setWinScene(new Scene(wint));
            controller1.setLostScene(new Scene(loose));
            controller3.setScene(scene);
            controller4.setScene(scene);
            controller5.setMainScene(scene);
            settingsController.setMainScene(scene);


            client.connect();

            roomCreateHandler = new RoomCreateHandler(client);
            roomConnectHandler = new RoomConnectHandler(client);
            rivalConnectHandler = new RivalConnectHandler(client);
            gameStartHandler = new GameStartHandler(client);
            gamePlayHandler = new GamePlayHandler(client);
            recordGetHandler = new RecordGetHandler(client);

            room = Room.getActualRoom();
            client.registerListener(roomConnectHandler);
            client.registerListener(roomCreateHandler);
            client.registerListener(rivalConnectHandler);
            client.registerListener(gameStartHandler);
            client.registerListener(gamePlayHandler);
            client.registerListener(recordGetHandler);
            client.start();






            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            primaryStage.show();


           /* textStatus = (Text) scene.lookup("#textStatus");*/
            roomCodePrinter = new RoomCodePrinter(controller1.getRoomCode());
            log.info("Имя геймера с контроллера игры{}",controller1.getGamerTwoName().getText());
            roomConnectPrinter = new RoomConnectPrinter(controller1.getGamerOneName());
            gameTextPrinter = new GameTextPrinter(controller1);
            rivalPrinter = new RivalPrinter(controller1.getGamerOneName(),controller1.getGamerTwoName(),controller1.getGamerThreeName(),controller1.getGamerFourName());//Выводит принтер в контроллер
            colorMixPrinter = new ColorMixPrinter(controller1);
            recordsCodePrinter = new RecordsCodePrinter(controller5.getChart());
           /* roomCreateHandler.addEventListener(roomCodePrinter);*/
            gameStartHandler.addEventListener(gameTextPrinter);
            roomConnectHandler.addEventListener(roomConnectPrinter);
            roomCreateHandler.addEventListener(roomCodePrinter);
            gamePlayHandler.addEventListener(colorMixPrinter);
            recordGetHandler.addEventListener(recordsCodePrinter);


            room.addEventListener(rivalPrinter);//каждый раз когда соперник подключился , отправит всем листенерам Pair

            log.info(" opponent row{}", controller.getOpponentRow());

/*
            //заплатка Todo разобраться с плохим контроллером
            try {
                gameScene.setOnKeyTyped(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent keyEvent) {

                        log.info("Code typed");
                        System.out.println("code");

                        log.info("Taked code{}", keyEvent.getCharacter().toString());


                        if (keyEvent.getCharacter().toLowerCase().equals(controller1.getTextArray()[n].toLowerCase()) ||
                                controller1.getTextArray()[n].equals(" ") && keyEvent.getCharacter().toString().toLowerCase().equals("space")) {
                            controller1.getGameScreen().getChildren().remove(n);
                            controller1.setUtillText(new Text(keyEvent.getCharacter().toLowerCase()));
                            controller1.getUtillText().setFont(Font.font(35));

                            controller1.getUtillText().setStyle("-fx-stroke: #ff00c8");
                            log.info(controller1.getUtillText().getStyle());
                            controller1.getGameScreen().getChildren().add(n, controller1.getUtillText());
                            controller1.getTappedChar().setText(controller1.getUtillText().getText());

                            n++;

                            Message message = MessageCreater.createPlayGameMsg(keyEvent.getCharacter());
                            try {
                                client.sendMessage(message);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            log.info("Совпадение символа {}", n);
                        }
//                        if (controller1.getTextArray()[n].equals(" ") && keyEvent.getCharacter().toString().toLowerCase().equals("space")) {
//
//                            n++;
//                            log.info("Совпадение пробела {}", n);
//                        }
                        if (n == controller1.getTextArray().length) {
                            primaryStage.setScene(new Scene(wint));
                            primaryStage.show();
                        } else
                        log.info("Следующая буква {}", controller1.getTextArray()[n]);

                    }
                });
            } catch (Exception e) {
                //Игрок прнажимал весь текст
                System.out.println(" Игрок пронажимал весь текст");
            }*/


        } catch (IOException e ) {
            e.printStackTrace();
        }


    }
}
