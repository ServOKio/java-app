package net.servokio.apo;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import javafx.scene.text.Text;
import net.servokio.apo.App.Start;
import net.servokio.apo.Utils.HTTPPost;
import org.json.JSONObject;

public class Controller implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Pane background;

    @FXML
    private AnchorPane module;

    @FXML
    private AnchorPane user_panel;

    @FXML
    private TextField i_username;

    @FXML
    private TextField i_fnumber;

    @FXML
    private TextField i_fword;

    @FXML
    private TextField i_fgcm;

    @FXML
    private Text i_stats;

    @FXML
    private AnchorPane cB;

    @FXML
    private AnchorPane mB;

    @FXML
    private AnchorPane fB;

    private static boolean testauth;
    @FXML
    void authTest(MouseEvent event) {
        if(!testauth){
            testauth = true;
            buttonAnimation();
            String username = i_username.getText();
            String oq = i_fnumber.getText();
            String twq = i_fword.getText();
            String thq = i_fgcm.getText();
            JSONObject ob = null;
            try {
                ob = HTTPPost.PostReg("https://"+main.stats.getString("domain")+"/api"+main.stats.getInt("api_last_version")+"/auth","username="+username+"&question_one="+oq+"&question_two="+twq+"&question_three="+thq);
            } catch (Exception e){
                e.printStackTrace();
            }
            if(ob.getInt("statscode") == 2 && ob.getString("message").equals("This user does not exist")){
                i_stats.setFill(Paint.valueOf("#fd8838"));
                i_stats.setText("This user does not exist");
            } else if(ob.getInt("statscode") == 2 && ob.getString("message").equals("Somethings wrong.")){
                i_stats.setFill(Paint.valueOf("#fd389"));
                i_stats.setText("Something wrong with the answers.");
            } else if(ob.getInt("statscode") == 1 && ob.getString("answer").equals("Changes accepted")){
                i_stats.setFill(Paint.valueOf("#5ec760"));
                i_stats.setText("You have successfully logged !");
                Start.authorized = true;
                login();
            } else {
                i_stats.setFill(Paint.valueOf("#50f"));
                i_stats.setText("Check if all fields are filled in.");
            }
            testauth = false;
        }
    }

    @FXML
    void closeApp(MouseEvent event) {
        Start.drpc.Discord_Shutdown();
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void fullScreenApp(MouseEvent event) {
        if(main.st.isFullScreen()){
            main.st.setFullScreen(false);
        } else{
            main.st.setFullScreen(true);
            main.st.centerOnScreen();
        }
    }

    @FXML
    void minimizeApp(MouseEvent event) {
        try {
            DoubleProperty stageX = new SimpleDoubleProperty();
            stageX.addListener((observable, oldValue, newValue) -> {
                if (newValue != null && newValue.doubleValue() != Double.NaN) main.st.setX(newValue.doubleValue());
            });
            stageX.set(main.st.getX());

            final Timeline slideLeft = new Timeline(
                    new KeyFrame(main.DURATION, new KeyValue(stageX,-main.st.getWidth()+10, Interpolator.EASE_BOTH)),
                    new KeyFrame(main.DURATION.multiply(2))
            );
//            po = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("parents/po.fxml")));
//            vBox.getChildren().removeAll();
//            vBox.getChildren().addAll(po);
            slideLeft.play();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    @FXML
    void initialize() {
        assert background != null : "fx:id=\"background\" was not injected: check your FXML file '1.fxml'.";
        assert user_panel != null : "fx:id=\"user_panel\" was not injected: check your FXML file '1.fxml'.";
        assert i_username != null : "fx:id=\"i_username\" was not injected: check your FXML file '1.fxml'.";
        assert i_fnumber != null : "fx:id=\"i_fnumber\" was not injected: check your FXML file '1.fxml'.";
        assert i_fword != null : "fx:id=\"i_fword\" was not injected: check your FXML file '1.fxml'.";
        assert i_fgcm != null : "fx:id=\"i_fgcm\" was not injected: check your FXML file '1.fxml'.";
        assert cB != null : "fx:id=\"cB\" was not injected: check your FXML file '1.fxml'.";
        assert mB != null : "fx:id=\"mB\" was not injected: check your FXML file '1.fxml'.";
        assert fB != null : "fx:id=\"fB\" was not injected: check your FXML file '1.fxml'.";
        assert i_stats != null : "fx:id=\"i_stats\" was not injected: check your FXML file '1.fxml'.";
        assert module != null : "fx:id=\"module\" was not injected: check your FXML file 'sample.fxml'.";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void buttonAnimation() {
        TranslateTransition cb = new TranslateTransition(Duration.seconds(0.5), cB);
        cb.setFromY(cb.getByY() - 5);
        cb.setToY(cb.getByY());
        cb.setAutoReverse(true);
        cb.setCycleCount(1);
        cb.play();
        cb.setOnFinished(event -> {
            TranslateTransition fb = new TranslateTransition(Duration.seconds(0.5), fB);
            fb.setFromY(fb.getByY() - 5);
            fb.setToY(fb.getByY());
            fb.setAutoReverse(true);
            fb.setInterpolator(Interpolator.EASE_OUT);
            fb.setCycleCount(1);
            fb.play();
            fb.setOnFinished(even -> {
                TranslateTransition mb = new TranslateTransition(Duration.seconds(0.5), mB);
                mb.setFromY(mb.getByY() - 5);
                mb.setToY(mb.getByY());
                mb.setAutoReverse(true);
                mb.setInterpolator(Interpolator.EASE_OUT);
                mb.setCycleCount(1);
                mb.play();
            });
        });
    }

    @FXML
    void testAutoAuth(MouseEvent event) {
        if(Start.authorized){
            Start.authorized = false;
            login();
        }
    }

    private void login(){
        main.sc.lookup("#background").setStyle("-fx-background-color: black;");
        user_panel.getChildren().removeAll();
        user_panel.getChildren().setAll(main.profile);
        System.out.println(Start.background_url);
        openMusic();
    }
    private void openMusic(){
        module.getChildren().removeAll();
        module.getChildren().setAll(main.music);
    }
}
