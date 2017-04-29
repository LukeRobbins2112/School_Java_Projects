package project;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import project.user.UserService;
import project.video.VideoService;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author LukeRobbins2112
 */
public class Serializer {

    public static void saveIt() {
        HashMap<String, Object> facades = new HashMap<>();
        facades.put("Library", VideoService.getInstance());
        facades.put("User", UserService.getInstance());
        File file = new File("VidPlayer.ser");

        if (file.exists()) {
            file.delete();
        }

        try {
            OutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fileOut);
            oos.writeObject(facades);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void loadIt(File f) {
        HashMap<String, Object> facades;
        try {
            InputStream fileIn = new FileInputStream("VidPlayer.ser");
            ObjectInputStream ois = new ObjectInputStream(fileIn);
            facades = (HashMap<String, Object>) ois.readObject();
            resetFacades(facades);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    private static void resetFacades(HashMap<String, Object> facades) {
        VideoService.reset((VideoService) facades.get("Library"));
        UserService.reset((UserService) facades.get("User"));
    }

}
