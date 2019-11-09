package net.servokio.apo;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import net.servokio.apo.App.Start;

public class ProfileController implements Initializable {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane profile_banner;

    @FXML
    private AnchorPane profile_avatar;

    @FXML
    private Text profile_username;


    private static boolean t = true;
    @FXML
    void test(MouseEvent event) {
//        if(t){
//            profile_banner.setStyle("-fx-background-color: #"+Start.color+"; -fx-background-image: url("+Start.background_url+");-fx-background-repeat:stretch;-fx-background-size: cover;");
//            profile_avatar.setStyle("-fx-background-color: #"+Start.color+"; -fx-background-image: url(" + Start.avatar_url + ");-fx-background-repeat:stretch;-fx-background-size: cover;");
//            profile_username.setText(Start.username);
//            t = false;
//        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        profile_banner.setStyle("-fx-background-color: #"+Start.color+"; -fx-background-image: url("+Start.background_url+"); -fx-background-size: cover;");
        profile_avatar.setStyle("-fx-background-color: #"+Start.color+"; -fx-background-image: url(" + Start.avatar_url + "); -fx-background-size: cover;");
        profile_username.setText(Start.username);
    }


    @FXML
    void initialize() {
        assert profile_banner != null : "fx:id=\"profile_banner\" was not injected: check your FXML file '2.fxml'.";
        assert profile_avatar != null : "fx:id=\"profile_avatar\" was not injected: check your FXML file '2.fxml'.";
        assert profile_username != null : "fx:id=\"profile_username\" was not injected: check your FXML file '2.fxml'.";
    }
}
