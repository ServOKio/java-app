package net.servokio.apo;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

import club.minnced.discord.rpc.DiscordRichPresence;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import net.servokio.apo.App.Start;
import net.servokio.apo.Utils.findMusic;
import javafx.scene.text.Text;

public class MusicController implements Initializable {

    private MediaPlayer mediaPlayer;


    private VBox b;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Slider track_play;

    @FXML
    private Text curent_music_name;

    @FXML
    private Text curent_music_time;

    @FXML
    private ScrollPane track_list;

    @FXML
    private AnchorPane pre_track_button;

    @FXML
    private AnchorPane media_button;

    @FXML
    private AnchorPane next_track_button;

    @FXML
    private Slider music_volume;

    private String curent_track_color = "#080808";

    @FXML
    void next_track(MouseEvent event) {
        if(!playNext()){
            main.log("Треков больше нет");
            return;
        }
        anim(next_track_button);
    }

    @FXML
    void scan_tracks(MouseEvent event) {
        new Thread(() -> {
            //Дропаем всё на ноль
            findMusic.music_list.clear();
            TrackController.track_real_names.clear();
            findMusic.i = 0;
            TrackController.co = 0;
            b.getChildren().removeAll();

            String[] path = new File(".").getAbsolutePath().replace("\\", "/").split("/");
            findMusic fMusic = new findMusic();

            //Ищем музыку | Наполняется карта с музыкой -> Добавляется ID каждому
            fMusic.scan(path[0] + "/" + path[1] + "/" + path[2]);
            Node[] nodes = new Node[findMusic.music_list.size()];
            //Добавляем поиск
            try {
                Parent sea = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("parents/download_sound.fxml")));
                b.getChildren().add(sea);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Вставляем музыку в бокс
            for (int i = 0; i < nodes.length; i++) {
                try {
                    final int j = i;
                    nodes[i] = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("parents/track.fxml")));
                    //give the items some effect
                    nodes[i].setOnMouseClicked(even -> {
                        track = j;
                        if (!playTrack()) main.log("Selected track is null (Not Found)");
                    });
                    nodes[i].setOnMouseEntered(even -> {
                        if (track != j) nodes[j].lookup("#track_back").setStyle("-fx-background-color : #273286");
                    });
                    nodes[i].setOnMouseExited(even -> {
                        if (track != j) nodes[j].lookup("#track_back").setStyle("-fx-background-color : #101010");
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            b.getChildren().addAll(nodes);
            track_list.setContent(b);
        }).start();
    }

    public Thread st = new Thread(new Runnable() {
        @Override
        //100 = mediaPlayer.getMedia().getDuration()
        //?   = mediaPlayer.getCurrentTime()
        public void run() {
            //Дропаем всё на ноль
            findMusic.music_list.clear();
            TrackController.track_real_names.clear();
            findMusic.i = 0;
            TrackController.co = 0;
            b.getChildren().removeAll();

            String[] path = new File(".").getAbsolutePath().replace("\\", "/").split("/");
            findMusic fMusic = new findMusic();

            //Ищем музыку | Наполняется карта с музыкой -> Добавляется ID каждому
            fMusic.scan(path[0] + "/" + path[1] + "/" + path[2]);
            Node[] nodes = new Node[findMusic.music_list.size()];
            //Добавляем поиск
            try {
                Parent sea = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("parents/download_sound.fxml")));
                b.getChildren().add(sea);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Вставляем музыку в бокс
            for (int i = 0; i < nodes.length; i++) {
                try {
                    final int j = i;
                    nodes[i] = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("parents/track.fxml")));
                    //give the items some effect
                    nodes[i].setOnMouseClicked(even -> {
                        track = j;
                        if (!playTrack()) main.log("Selected track is null (Not Found)");
                    });
                    nodes[i].setOnMouseEntered(even -> {
                        if (track != j) nodes[j].lookup("#track_back").setStyle("-fx-background-color : #273286");
                    });
                    nodes[i].setOnMouseExited(even -> {
                        if (track != j) nodes[j].lookup("#track_back").setStyle("-fx-background-color : #101010");
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            b.getChildren().addAll(nodes);
            track_list.setContent(b);
        }
    });

    @FXML
    void setVolume(MouseEvent event) {
        double v = music_volume.getValue();
        mediaPlayer.setVolume(v);
        Start.config.put("music_votume",v);
        Start.saveConfig();
    }

    @FXML
    void set_play_track(MouseEvent event) {
        //100 = mediaPlayer.getMedia().getDuration().toSeconds()
        //45  = ?
        double set_play = (track_play.getValue()*mediaPlayer.getMedia().getDuration().toSeconds())/100;
        mediaPlayer.seek(Duration.seconds(set_play));
    }

    private boolean p = false;
    private Random random = new Random();
    @FXML
    void pause_and_play(MouseEvent event) {
        if(p){
            mediaPlayer.pause();
            media_button.setStyle("-fx-background-color: #7F40FF; -fx-background-radius: 50%; -fx-background-image: url(assets/img/play.png); -fx-background-size: 35 35; -fx-background-position: center center; -fx-background-repeat: stretch;");
            p = false;
        } else{
            mediaPlayer.play();
            media_button.setStyle("-fx-background-color: #7F40FF; -fx-background-radius: 50%; -fx-background-image: url(assets/img/pause.png); -fx-background-size: 35 35; -fx-background-position: center center; -fx-background-repeat: stretch;");
            p = true;
        }
        anim(media_button);
    }

    @FXML
    void pre_track(MouseEvent event) {
        if(!playPrevision()){
            main.log("Pre. track is null (Not Found)");
            return;
        }
        anim(pre_track_button);
    }

    private int track = 0;

    @FXML
    void initialize(){
        assert music_volume != null : "fx:id=\"music_volume\" was not injected: check your FXML file 'music.fxml'.";
        assert pre_track_button != null : "fx:id=\"pre_track_button\" was not injected: check your FXML file 'music.fxml'.";
        assert media_button != null : "fx:id=\"media_button\" was not injected: check your FXML file 'music.fxml'.";
        assert next_track_button != null : "fx:id=\"next_track_button\" was not injected: check your FXML file 'music.fxml'.";
        assert track_list != null : "fx:id=\"track_list\" was not injected: check your FXML file 'music.fxml'.";
        assert track_play != null : "fx:id=\"track_play\" was not injected: check your FXML file 'music.fxml'.";
        assert curent_music_name != null : "fx:id=\"curent_sound\" was not injected: check your FXML file 'music.fxml'.";
        assert curent_music_time != null : "fx:id=\"curent_music_time\" was not injected: check your FXML file 'music.fxml'.";
    }

    private Node[] n;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            if(Start.config.get("music_votume") == null){
                Start.config.put("music_votume", 0.2);
                Start.saveConfig();
            }
        } catch (Exception e){
            Start.config.put("music_votume", 0.2);
            Start.saveConfig();
        }
        if(Start.config.get("music_votume") == null){
            Start.config.put("music_votume", 0.2);
            Start.saveConfig();
        }
        double vo = Start.config.getDouble("music_votume");
        music_volume.setValue(vo);
        System.out.println(findMusic.music_list.size());
        findMusic.i = 0;
        Node[] nodes = new Node[findMusic.music_list.size()];
        for (int i = 0; i < nodes.length; i++) {
            try {
                final int j = i;
                nodes[i] = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("parents/track.fxml")));
                //give the items some effect
                nodes[i].setOnMouseClicked(event -> {
                    track = j;
                    if(!playTrack()) main.log("Selected track is null (Not Found)");
                });
                nodes[i].setOnMouseEntered(event -> {
                    if(track != j)  nodes[j].lookup("#track_back").setStyle("-fx-background-color : #273286");
                });
                nodes[i].setOnMouseExited(event -> {
                    if(track != j)  nodes[j].lookup("#track_back").setStyle("-fx-background-color : #101010");
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        nodes[0].lookup("#track_back").setStyle("-fx-background-color : "+curent_track_color);
        n = nodes;
        VBox vBox = new VBox();
        b = vBox;
        try {
            Parent sea = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("parents/download_sound.fxml")));
            vBox.getChildren().add(sea);
        } catch (IOException e) {
            e.printStackTrace();
        }
        vBox.getChildren().addAll(nodes);
        track_list.setContent(vBox);
        track_list.setFitToWidth(true);
        Media start = new Media(new File(findMusic.music_list.get(0)).toURI().toString());
        curent_music_name.setText(new File(findMusic.music_list.get(track)).getName().replace(".mp3",""));
        mediaPlayer = new MediaPlayer(start);
        mediaPlayer.setOnPlaying(() -> {
            System.out.println(mediaPlayer.getCurrentTime());
            System.out.println(mediaPlayer.getMedia().getDuration());
        });
        mediaPlayer.setVolume(vo);
        mediaPlayer.setOnEndOfMedia(()-> main.log("Next track is null (Not Found)"));

        track_play.valueProperty().addListener((ov, old_val, new_val) -> {
            int l = 0;
            if (new_val.intValue() > 7) l = new_val.intValue()-7;
            String style = String.format("-fx-background-color: linear-gradient(to right, #5500ff %d%%, #09070e %d%%);", l, new_val.intValue());
            nodes[track].lookup(".load").setStyle(style);
        });
        rub.start();
    }

    private boolean playTrack(){
        if(findMusic.music_list.get(track) == null){
            return false;
        } else{
            mediaPlayer.stop();
            Media hit = new Media(new File(findMusic.music_list.get(track)).toURI().toString());
            String track_name = new File(findMusic.music_list.get(track)).getName().replace(".mp3","");
            main.log("Playing selected track: "+TrackController.track_real_names.get(track));
            DiscordRichPresence discordPresence = new DiscordRichPresence();
            discordPresence.state = TrackController.track_real_names.get(track);
            discordPresence.details = "Now listening to music";
            discordPresence.startTimestamp = System.currentTimeMillis() / 1000;
            discordPresence.largeImageKey = "0001";
            discordPresence.largeImageText = TrackController.track_real_names.get(track);
            discordPresence.smallImageKey = "0002";
            discordPresence.smallImageText = "ServOKio App 0.0.1";
            Start.drpc.Discord_UpdatePresence(discordPresence);
            for(Node no : n){
                no.lookup("#track_back").setStyle("-fx-background-color : #101010");
            }
            n[track].lookup("#track_back").setStyle("-fx-background-color : "+curent_track_color);
            media_button.setStyle("-fx-background-color: #7F40FF; -fx-background-radius: 50%; -fx-background-image: url(assets/img/pause.png); -fx-background-size: 35 35; -fx-background-position: center center; -fx-background-repeat: stretch;");
            curent_music_name.setText(track_name);
            mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.play();
            mediaPlayer.setOnEndOfMedia(()->{
                if(!playNext()) main.log("Next track is null (Not Found)");
            });
            mediaPlayer.setVolume(music_volume.getValue());
            return true;
        }
    }

    private boolean playNext(){
        track++;
        if(findMusic.music_list.get(track) == null){
            track--;
            return false;
        } else{
            mediaPlayer.stop();
            Media hit = new Media(new File(findMusic.music_list.get(track)).toURI().toString());
            String track_name = new File(findMusic.music_list.get(track)).getName().replace(".mp3","").replace("_"," ");
            main.log("Playing next track: "+track_name);
            n[track-1].lookup("#track_back").setStyle("-fx-background-color : #101010");
            n[track].lookup("#track_back").setStyle("-fx-background-color : "+curent_track_color);
            DiscordRichPresence discordPresence = new DiscordRichPresence();
            discordPresence.state = TrackController.track_real_names.get(track);
            discordPresence.details = "Now listening to music";
            discordPresence.startTimestamp = System.currentTimeMillis() / 1000;
            discordPresence.largeImageKey = "0001";
            discordPresence.largeImageText = TrackController.track_real_names.get(track);
            discordPresence.smallImageKey = "0002";
            discordPresence.smallImageText = "ServOKio App 0.0.1";
            Start.drpc.Discord_UpdatePresence(discordPresence);
            media_button.setStyle("-fx-background-color: #7F40FF; -fx-background-radius: 50%; -fx-background-image: url(assets/img/pause.png); -fx-background-size: 35 35; -fx-background-position: center center; -fx-background-repeat: stretch;");
            curent_music_name.setText(track_name);
            mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.play();
            mediaPlayer.setOnEndOfMedia(()->{
                if(!playNext()) main.log("Next track is null (Not Found)");
            });
            mediaPlayer.setVolume(music_volume.getValue());
            return true;
        }
    }


    private boolean playPrevision(){
        track--;
        if(findMusic.music_list.get(track) == null){
            track++;
            return false;
        } else{
            mediaPlayer.stop();
            Media hit = new Media(new File(findMusic.music_list.get(track)).toURI().toString());
            curent_music_name.setText(new File(findMusic.music_list.get(track)).getName().replace(".mp3","").replace("_"," "));
            n[track+1].lookup("#track_back").setStyle("-fx-background-color : #101010");
            n[track].lookup("#track_back").setStyle("-fx-background-color : "+curent_track_color);
            DiscordRichPresence discordPresence = new DiscordRichPresence();
            discordPresence.state = TrackController.track_real_names.get(track);
            discordPresence.details = "Now listening to music";
            discordPresence.startTimestamp = System.currentTimeMillis() / 1000;
            discordPresence.largeImageKey = "0001";
            discordPresence.largeImageText = TrackController.track_real_names.get(track);
            discordPresence.smallImageKey = "0002";
            discordPresence.smallImageText = "ServOKio App 0.0.1";
            Start.drpc.Discord_UpdatePresence(discordPresence);
            media_button.setStyle("-fx-background-color: #7F40FF; -fx-background-radius: 50%; -fx-background-image: url(assets/img/pause.png); -fx-background-size: 35 35; -fx-background-position: center center; -fx-background-repeat: stretch;");
            mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.play();
            mediaPlayer.setVolume(music_volume.getValue());
            return true;
        }
    }
    private void anim(AnchorPane a){
        TranslateTransition cb = new TranslateTransition(Duration.seconds(0.5), a);
        cb.setFromY(cb.getByY() + 5);
        cb.setToY(cb.getByY());
        cb.setAutoReverse(true);
        cb.setCycleCount(1);
        cb.play();
    }
    private Thread rub = new Thread(new Runnable() {
        @Override
        //100 = mediaPlayer.getMedia().getDuration()
        //?   = mediaPlayer.getCurrentTime()
        public void run() {
            while (true){
                try{
                    Thread.sleep(1000);
                    double s = mediaPlayer.getCurrentTime().toSeconds();
                    double set =(100*s)/mediaPlayer.getMedia().getDuration().toSeconds();
                    int c_hours = (int) (s / 3600);
                    int c_minutes = (int) ((s % 3600) / 60);
                    int c_seconds = (int) (s % 60);
                    curent_music_time.setText(String.format("%02d:%02d:%02d", c_hours, c_minutes, c_seconds));
                    track_play.setValue(set);
                } catch (Exception ignored){

                }
            }
        }
    });
}