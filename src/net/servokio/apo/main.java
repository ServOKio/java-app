package net.servokio.apo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import net.servokio.apo.App.Start;
import net.servokio.apo.Utils.HTTPGet;
import org.json.JSONObject;

import java.util.Objects;
import java.util.ResourceBundle;

public class main extends Application {
    public static void main(String[] args){
        launch(args);
    }
    private static final int W = 620;
    public static final Duration DURATION = Duration.seconds(0.5);
    public static JSONObject bg,stats;
    public static Stage st,sc;
    public static Parent profile,music;
    static {
        try {
            stats = HTTPGet.req("https://servokio.ru/stats");
            bg = HTTPGet.req("https://servokio.ru/api2/asi");
        } catch (Exception e) {
            bg = new JSONObject("{\"color\":\"5500ff\",\"img_url\":\"assets/img/default-background.png\",\"author\":\"ServOKio (Offline Mode)\"}");
            e.printStackTrace();
        }
    }
    private final Delta dragDelta = new Delta();
    class Delta { double x, y; }

    public static void log(String message){
        System.out.println("[ServOKio App] "+message);
    }

    @Override
    public void start(Stage stage) throws Exception{
        // Создаём основное
        // |
        //  - Проверка на API если пользователь авторизован - - - - Если ошибка соединения оффлайн данные
        //    |                                                   |
        //    Если да проверяем данные и записываем в таблицу     Если ошибок нет и не авторизованждём авторизации
        //    |                                                   |
        //    Загружаем profile.fxml - - - - - - - - - - - - - -  Если да проверяем данные и записываем в таблицу
        //    |
        //    Запускаем main_page.fxml
        //    |
        //   \/
        st = stage;
        ResourceBundle resources = ResourceBundle.getBundle("net.servokio.apo.App.resource");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("parents/sample.fxml")),resources);
        //profile = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("parents/profile.fxml")));
        Start s = new Start();
        s.Start();
        music = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("parents/music.fxml")));
        stage.setTitle("ServOKio v0.0.1");
        stage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root,620,400);
        sc = scene;

        scene.lookup("#background").setStyle("-fx-background-color: #"+bg.getString("color")+"; -fx-background-image: url("+bg.getString("img_url")+");-fx-background-repeat:stretch;-fx-background-position: center center;");
        scene.setFill(Color.TRANSPARENT);
        root.setOnMousePressed(e->{
            dragDelta.x = stage.getX() - e.getScreenX();
            dragDelta.y = stage.getY() - e.getScreenY();
        });
        root.setOnMouseDragged(e->{
            if(stage.isFullScreen()) return;
            stage.setX(e.getScreenX() + dragDelta.x);
            stage.setY(e.getScreenY() + dragDelta.y);
        });
        //root.setOnMouseExited(event -> slideLeft.play());
        stage.setScene(scene);
        stage.getIcons().add(new javafx.scene.image.Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("assets/img/icon.png"))));
        stage.show();
        stage.centerOnScreen();
    }

}
