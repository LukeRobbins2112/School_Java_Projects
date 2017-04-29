/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.video;


import project.DoesNotExistException;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import project.user.UserService;
import project.video.Video.Rating;

/**
 * The VideoService class will own and maintain all existing Video objects. This
 * class will utilize the VideoLoader class to load Video objects from an
 * external XML file. The VideoService will maintain the employee objects in a
 * HashMap<String, Video>. The VideoService should be implemented as a
 * thread-safe Singleton (design pattern). Additionally, the VideoService
 * instance will need to be serialized so this class should implement the
 * Serializable interface.
 *
 * @author LukeRobbins2112
 */
public final class VideoService implements Serializable {
    
    
    private static volatile VideoService instance;
    private final HashMap<String,Video> allVideos;
    private final String mediaBase = "media";
    private final String fileName = "VideoLibrary.xml";

    private VideoService() {
        allVideos = new HashMap<String,Video>();
        loadVideos();
    }
    
    public static VideoService getInstance(){
        
        if(instance == null){
            synchronized (VideoService.class){
                if (instance == null){
                    instance = new VideoService();
                }
            }
        }
       return instance; 
    }
    
    public void loadVideos(){
        HashMap<String,Video> videos = VideoLoader.loadVideoXML(fileName);
        allVideos.putAll(videos);
    }
    
    public static void reset(VideoService vidSvc) throws InvalidParameterException {
        if (vidSvc == null){
            throw new InvalidParameterException("vidSvc is null");
        }
        
        instance = vidSvc;
    }
    
    public ArrayList<String> getSelections(String userName) throws DoesNotExistException  {
        Rating userRating = UserService.getInstance().getRatingLevel(userName);
        ArrayList<String> appropriateVideos = new ArrayList<>();
        
        for (String videoName : allVideos.keySet()){
            if (allVideos.get(videoName).getRating().ordinal() <= userRating.ordinal())
                appropriateVideos.add(videoName);
        }
        
        return appropriateVideos;
    }
    
    public String getFile(String videoName) throws DoesNotExistException{
        if (!allVideos.keySet().contains(videoName)){
            throw new DoesNotExistException("There is no video in the list with the name " + videoName);
        }
        
        return mediaBase + "//" + allVideos.get(videoName).getFileName();
    }
    
    public String getSummary(String videoName) throws DoesNotExistException {
        if (!allVideos.keySet().contains(videoName)){
            throw new DoesNotExistException("There is no video in the list with the name " + videoName);
        }
        
        return String.format("%s (%s)", videoName, allVideos.get(videoName).getRating());
    }
    
    public String getDurationString(String selection) throws DoesNotExistException {
        if (!allVideos.keySet().contains(selection)){
            throw new DoesNotExistException("There is no video in the list with the name " + selection);
        }
        
        return allVideos.get(selection).durationString();
    }
    
    public String toString(){
        
        String output = "";
        
        Map<String, Video> sortedVideos = new TreeMap<String, Video>(allVideos);
        
        for (String name : sortedVideos.keySet()){
            output += sortedVideos.get(name) + "\n";
        }
        
        return output;
    }

}
