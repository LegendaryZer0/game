package typergame.client.visualizer.eventListImpl;


import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import typergame.client.controllers.MainGameController;
import typergame.client.model.Room;
import typergame.client.util.ColorMixer;
import typergame.client.visualizer.EventListener;

@Slf4j
public class ColorMixPrinter implements EventListener<Pair> {
   private MainGameController mainGameController;
   private ObservableList<Node> filed;
   private volatile Room room;
   private int position;
   private Text symb;
   private String paint;
   private Integer colorIndexHome;
   private Integer colorIndexServer;
   private  Integer  color;


   public  ColorMixPrinter(MainGameController controller){
       this.mainGameController = controller;
       filed = controller.getGameScreen().getChildren();

   }

    @Override
    public void onEventAction(Pair object) {
       log.info("library of colors(hash) {}",ColorMixer.getLibraryColor().toString());
       filed.stream().map(x->(Text)x).forEach(x->log.info("Fill of text in text flow {}",x.getFill()));

       room = Room.getActualRoom();
       log.info("room gamers {}",room.getGamers().get());
       position = (int)object.getValue()-1;
       log.info("Pair object key {}, value {}",object.getKey(),object.getValue());
        room.getGamers().ifPresent(x -> {
            x.stream().forEach(gamer -> {
                if(gamer.getId() == (int) object.getKey()) {

                    symb = (Text) filed.get(position);
                    log.info("symb fills {}", symb.getFill().toString().substring(2, 8));
                    switch (symb.getFill().toString().substring(2, 8)) {
                        case "778899" -> {
                            symb.setFill(Paint.valueOf(ColorMixer.getLibraryColor().get(gamer)));
                            log.info("case 778899  gamers color: {}", Paint.valueOf(ColorMixer.getLibraryColor().get(gamer)));
                        }
                        case "000000" -> {
                            symb.setFill(Paint.valueOf(ColorMixer.getLibraryColor().get(gamer)));
                            log.info("case 000000  gamers color: {}", Paint.valueOf(ColorMixer.getLibraryColor().get(gamer)));
                        }
                        case "FF2376" -> {symb.setFill(Paint.valueOf("F5B03B"));
                        log.info("case FF2376");}


                        case "ff0000", "000fff" ->{symb.setFill(Paint.valueOf("D4C80B"));} // смешивание красного с синим ?
                        case "D4C80B"->{symb.setFill(Paint.valueOf("EC7544"));}
                        case "852A26"->{symb.setFill(Paint.valueOf("EC7544"));}
                        default -> {symb.setFill(Paint.valueOf("FF2376"));
                        log.info("case default");}

                    }
                    paint =symb.getFill().toString().substring(2,8);
                    colorIndexHome = Integer.parseInt(paint,16);
                    colorIndexServer = Integer.parseInt(ColorMixer.getLibraryColor().get(gamer),16);

              /*      color = ((colorIndexHome*colorIndexServer+colorIndexHome*colorIndexHome)-colorIndexHome*colorIndexServer);
                    log.info("home color {}",Integer.toString(colorIndexHome,16));
                    log.info("server color {}",Integer.toString(colorIndexServer,16));
                    log.info("final color {}",Integer.toString(color,16).substring(1,7));
                    symb.setFill(Paint.valueOf(Integer.toString(color,16).substring(1,7)));*/



                }
            });
        });

    }
}
