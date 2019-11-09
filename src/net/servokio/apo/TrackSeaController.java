package net.servokio.apo;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import net.servokio.apo.App.Start;
import net.servokio.apo.Utils.HTTPPost;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

public class TrackSeaController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane track_item;

    @FXML
    private TextField track_search;

    @FXML
    void go_search_track(MouseEvent event) {
        search();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void initialize() {
        assert track_item != null : "fx:id=\"track_item\" was not injected: check your FXML file 'download_sound.fxml'.";
        assert track_search != null : "fx:id=\"track_search\" was not injected: check your FXML file 'download_sound.fxml'.";
    }

    private void search(){
        new Thread(() -> {
            try {
                JSONObject ob = HTTPPost.PostReg("https://servokio.ru/api2/sea_music","token="+ Start.token +"&number=0&request="+track_search.getText());
                if(ob.getInt("statscode") == 1){
                    main.log("Start download");
                    String name = track_search.getText();
                    track_search.setText("Downloading...");
                    URL hp = new URL(ob.getString("url"));
                    HttpURLConnection httpConn = (HttpURLConnection) hp.openConnection();
                    httpConn.addRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
                    InputStream stream = httpConn.getInputStream();

                    File fol = new File(new File("").getAbsolutePath(), "downloads");
                    if (!fol.exists()) fol.mkdir();
                    File pictureFile = new File(new File("").getAbsolutePath()+"/downloads", name+".mp3");
                    FileUtils.copyInputStreamToFile(stream, pictureFile);
                    main.log("Done");
                    stream.close();
                    httpConn.disconnect();
                    track_search.setText("");
                    MusicController mc = new MusicController();
                    mc.st.start();
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }).start();
    }
}
