package net.servokio.apo.Utils;

import net.servokio.apo.main;

import java.io.File;
import java.util.HashMap;

public class findMusic {
    public static HashMap<Integer, String> music_list;
    public static int i = 0;
    public void scan(String path){
        File index = new File(path);
        String[] files = index.list();
        if(files == null) return;
        for (String file : files) {
            //System.out.println(path);
            if(file.endsWith(".mp3")){
                main.log("New track in: "+path+"/"+file);
                music_list.put(i,path+"/"+file);
                i++;
            }
            if(new File(path+"/"+file).isDirectory()){
                scan(path+"/"+file);
            }
        }
        //main.log("Треков в списке: "+music_list.size());
    }
}
