package net.servokio.apo.App;

import javafx.fxml.FXMLLoader;
import net.servokio.apo.TrackController;
import net.servokio.apo.Utils.HTTPPost;
import net.servokio.apo.Utils.findMusic;
import net.servokio.apo.main;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import club.minnced.discord.rpc.*;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Objects;

public class Start {
    public static JSONObject config;

    public static boolean authorized = false;
    public static String username;
    public static String color;
    public static String avatar_url;
    public static String background_url;
    public static int background_filter;
    public static int user_id;
    public static String token;
    public static DiscordRPC drpc;


    public void Start(){
        main.log("Application launch");

        File cf = new File(new File("").getAbsolutePath()+"/config.json");
        if (!cf.exists()){
            main.log("Сreate a configuration file ("+new File("").getAbsolutePath()+"/config.json"+")");
            cf.getParentFile().mkdirs();
            BufferedWriter bw = null;
            try {
                cf.createNewFile();
                bw = new BufferedWriter(new FileWriter(new File("").getAbsolutePath()+"/config.json"));
                bw.write("{\"version\":\"0.0.1\"}");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    bw.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(new File("").getAbsolutePath()+"/config.json"));
            config = new JSONObject(obj.toString());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        main.log(config.getString("version"));

        //Check update
        JSONObject updater = null;
        try {
            updater = HTTPPost.PostReg("https://"+main.stats.getString("domain")+"/api"+main.stats.getInt("api_last_version")+"/update","");
        } catch (Exception e) {
            e.printStackTrace();
            updater = new JSONObject("{\"statscode\":1,\"set_version\":'"+config.getString("version")+"'}");
        }
        if(updater.getInt("statscode") == 1){
            if(!updater.getString("set_version").equals(config.getString("version"))){
                main.log("Please update app");
            }
        }

        JSONObject jo = null;
        try {
            jo = HTTPPost.PostReg("https://"+main.stats.getString("domain")+"/api"+main.stats.getInt("api_last_version")+"/me","");
        } catch (Exception e) {
            jo = new JSONObject("{\"statscode\":\"1\",\"username\":\"OfflineUser\",\"color\":\"#5500ff\",\"avatar_url\":\"assets/img/default-background.png\",\"background_url\":\"assets/img/default-background.png\",\"background_filter\":0,\"ID\":0,\"token\":\"000000000000000\"}");
            main.log("Enabled offline mode. JSON data: "+jo.toString());
            System.out.println(e.getMessage());
        }
        if(jo.getInt("statscode") == 1){
            main.log("User authorized.");
            displayTray("Hello", "test");
            username = jo.getString("username");
            color = jo.getString("color");
            avatar_url = jo.getString("avatar_url");
            background_url = jo.getString("background_url");
            background_filter = jo.getInt("background_filter");
            user_id = jo.getInt("ID");
            token = jo.getString("token");
            try {
                main.profile = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("parents/profile.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            authorized = true;
        } else if(jo.getInt("statscode") == 2 && jo.getString("message").equals("This user is not provided to the system")){
            main.log("Authorization required.");
        } else {
            main.log("Unknown error. Stats code: "+jo.getInt("statscode")+", message: "+jo.getString("message"));
        }
        findMusic.music_list = new HashMap<>();
        TrackController.track_real_names = new HashMap<>();
        String[] path = new File(".").getAbsolutePath().replace("\\","/").split("/");
        findMusic fMusic = new findMusic();
        fMusic.scan(path[0]+"/"+path[1]+"/"+path[2]);
        findMusic.i = 0;

        DiscordRPC lib = DiscordRPC.INSTANCE;
        String applicationId = "574187659354832896";
        String steamId = "";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.spectateGame = (username) -> main.log("Пользователь "+username+" хочет к вам");
        handlers.ready = (user) -> System.out.println("Ready!");
        lib.Discord_Initialize(applicationId, handlers, true, steamId);
        drpc = lib;
        // in a worker thread
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }, "RPC-Callback-Handler").start();
    }

    public void displayTray(String title, String text){
        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();
        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().getImage(Objects.requireNonNull(getClass().getClassLoader().getResource("assets/img/icon.png")));
        //Alternative (if the icon is on the classpath):
        //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));
        TrayIcon trayIcon = new TrayIcon(image, "ServOKio");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        trayIcon.displayMessage(title, text, TrayIcon.MessageType.INFO);
    }

    public static void saveConfig(){
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(new File("").getAbsolutePath()+"/config.json"));
            bw.write(config.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(bw).close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
