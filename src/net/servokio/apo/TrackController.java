package net.servokio.apo;

import com.mpatric.mp3agic.*;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import net.servokio.apo.Utils.findMusic;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class TrackController implements Initializable {
    @FXML
    private AnchorPane track_item;

    @FXML
    private ImageView track_icon;

    @FXML
    private TextField track_name;

    @FXML
    private Text track_time;

    @FXML
    void initialize() {
        assert track_item != null : "fx:id=\"track_item\" was not injected: check your FXML file 'track.fxml'.";
        assert track_name != null : "fx:id=\"track_name\" was not injected: check your FXML file 'track.fxml'.";
        assert track_time != null : "fx:id=\"track_time\" was not injected: check your FXML file 'track.fxml'.";
        assert track_icon != null : "fx:id=\"track_icon\" was not injected: check your FXML file 'track.fxml'.";
    }

    public static int co = 0;
    public static HashMap<Integer, String> track_real_names;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String name = new File(findMusic.music_list.get(co)).getName().replace(".mp3","");
        try {
            Mp3File mp3file = new Mp3File(findMusic.music_list.get(co));
            if (mp3file.hasId3v2Tag()) {
                ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                byte[] imageData = id3v2Tag.getAlbumImage();
                if (imageData != null) {
                    File fol = new File(new File("").getAbsolutePath(), "art");
                    if (!fol.exists()) fol.mkdir();
                    String iu = new File("").getAbsolutePath()+"/art/"+co+".png";
                    RandomAccessFile file = new RandomAccessFile(iu, "rw");
                    file.write(imageData);
                    file.close();
                    track_icon.setImage(new Image(new File(iu).toURI().toString()));
                }
                double s = mp3file.getLengthInSeconds();
                int c_hours = (int) (s / 3600);
                int c_minutes = (int) ((s % 3600) / 60);
                int c_seconds = (int) (s % 60);
                track_time.setText(mp3file.getBitrate() +  " kbps "  +  (mp3file.isVbr() ? "(VBR)" :  "(CBR)")+" "+String.format("%02d:%02d:%02d", c_hours, c_minutes, c_seconds));
            }
            String artist = "";
            if (mp3file.hasId3v1Tag()) {
                ID3v1 id3v1Tag = mp3file.getId3v1Tag();
                if(!id3v1Tag.getArtist().isEmpty()){
                    artist = id3v1Tag.getArtist()+" - ";
                } else{
                    artist = "";
                }
                if(!id3v1Tag.getTitle().isEmpty()){
                    track_name.setText(id3v1Tag.getTitle());
                    track_real_names.put(co,artist+id3v1Tag.getTitle());
                } else{
                    track_name.setText(name);
                    track_real_names.put(co,artist+name);
                }
            } else{
                track_name.setText(name);
                track_real_names.put(co,artist+name);
            }
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            e.printStackTrace();
        }
        co = co+1;
    }
}
