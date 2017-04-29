/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.user;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import project.DoesNotExistException;
import project.video.Video;
import project.video.Video.Rating;
import project.video.VideoLoader;

/**
 *
 * @author LukeRobbins2112
 */
public final class UserService implements Serializable {
    
    private static volatile UserService instance;
    private final HashMap<String,User> allUsers;
    private final String fileName = "Users.xml";
    
    private UserService(){
        this.allUsers = new HashMap<>();
        loadUsers();
    }
    
    public static UserService getInstance(){
        if (instance == null){
            synchronized (UserService.class){
                if (instance == null){
                    instance = new UserService();
                }
            }
        }
        
        return instance;
    }
    
    public void loadUsers(){
        HashMap<String, User> users = UserLoader.loadUserXML(fileName);
        this.allUsers.putAll(users);
    }
    
    public static void reset(UserService usrSvc){
        if (usrSvc == null){
            throw new InvalidParameterException("vidSvc is null");
        }
        instance = usrSvc;
    }
    
    public boolean validateCredentials(String uname, String pswd) throws DoesNotExistException{
        if (!allUsers.containsKey(uname)){
            throw new DoesNotExistException("Username is not in the list");
        }
        return (allUsers.get(uname).hasPassword(pswd));
    }
    
    public void updateVideoRecord(String uname, String vidName, double seconds) throws DoesNotExistException{
        if (!allUsers.containsKey(uname)){
            throw new DoesNotExistException("Username is not in the list");
        }
        allUsers.get(uname).updateVideoRecord(vidName, seconds);
    }
    
    public double getVideoStartTime(String uname, String vidName) throws DoesNotExistException{
        if (!allUsers.containsKey(uname)){
            throw new DoesNotExistException("Username is not in the list");
        }
        return allUsers.get(uname).getVideoStartTime(vidName);
    }
    
    public Rating getRatingLevel(String uname) throws DoesNotExistException{
        if (!allUsers.containsKey(uname)){
            throw new DoesNotExistException("Username is not in the list");
        }
        return allUsers.get(uname).getRatingLevel();
    }
    
    public String toString(){
        
        String output = "";
        
        Map<String, User> sortedUsers = new TreeMap<String, User>(allUsers);
        
        for (String user : sortedUsers.keySet()){
            output += sortedUsers.get(user) + "\n";
        }
        
        return output;
    }
}
