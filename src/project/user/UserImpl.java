/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.user;

import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import project.video.Video.Rating;

/**
 *
 * @author LukeRobbins2112
 */
public class UserImpl implements User {

    private String username;
    private String password;
    private HashMap<String, Double> videoRecords;
    private String lastLogin = "Never";
    private Rating ratingLevel;

    public UserImpl(String username, String password, Rating ratingLevel) {
        setUsername(username);
        setPassword(password);
        setRatinglevel(ratingLevel);
        this.videoRecords = new HashMap<String, Double>();
    }

    private void setUsername(String usernameIn) {
        if (usernameIn == null || usernameIn.isEmpty()) {
            throw new InvalidParameterException("Username cannot be null or empty.");
        }
        this.username = usernameIn;
    }

    private void setPassword(String passwordIn) {
        if (passwordIn == null || passwordIn.isEmpty()) {
            throw new InvalidParameterException("Password cannot be null or empty.");
        }
        this.password = passwordIn;
    }

    private void setRatinglevel(Rating ratingLevel) {
        if (ratingLevel == null) {
            throw new InvalidParameterException("Rating cannot be null.");
        }
        this.ratingLevel = ratingLevel;
    }

    @Override
    public Rating getRatingLevel() {
        return this.ratingLevel;
    }

    @Override
    public double getVideoStartTime(String currentVideo) {
        if (this.videoRecords.containsKey(currentVideo))
            return this.videoRecords.get(currentVideo);
        
        return 0.0;
    }

    @Override
    public boolean hasPassword(String pswdIn) {
        return (pswdIn.equals(this.password));
    }

    @Override
    public void setLastLogin() {
        long ms = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        lastLogin = sdf.format(ms);
    }

    /**
     * uses the string and double parameters passed in and puts them into the
     * “videoRecords” HashMap. If the video already had an entry, no special
     * behavior is needed – just overwrite the previous entry.
     *
     * @param currentVideo
     * @param seconds
     */
    @Override
    public void updateVideoRecord(String currentVideo, double seconds) {
        this.videoRecords.put(currentVideo, seconds);
    }
    
    public String toString(){
        String output = this.username + ", Last Login: " + this.lastLogin + "\n";
        for (String videoName: this.videoRecords.keySet()){
            output += "\t " + videoName + ": " + getVideoStartTime(videoName) + "\n";
        }
        
        return output;
    }

}
