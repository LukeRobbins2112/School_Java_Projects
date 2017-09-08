package gui;

import gui.player.MoviePlayer;
import gui.library.LibraryWin;
import gui.login.LoginWin;
import java.util.ArrayList;
import javafx.application.Application;
import javax.swing.JOptionPane;
import project.DoesNotExistException;
import project.Serializer;
import project.user.UserService;
import project.video.VideoService;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class DisplayFacade {
    
    private static DisplayFacade instance;
    private String currentUser;
    private String currentVideo;
    
    private DisplayFacade() {
        
    }
    
    public static DisplayFacade getInstance() {
        if (instance == null)
            instance = new DisplayFacade();
        return instance;
    }
    
    public void displayLoginWindow() {
        new LoginWin().setVisible(true);
    }
    
    public void acceptCredentials(String uname, String pswd) throws DoesNotExistException {
        if ((!uname.isEmpty() && !pswd.isEmpty()) && UserService.getInstance().validateCredentials(uname, pswd)) {
            currentUser = uname;
            showVideoSelections(uname);
        } else {
            JOptionPane.showMessageDialog(null, "You did not enter a valid Username/Password combination", "Invalid Credentials", JOptionPane.ERROR_MESSAGE);
            new LoginWin().setVisible(true);
        }
    }
    
    public void showVideoSelections(String uname) throws DoesNotExistException {
        ArrayList<String> selections = VideoService.getInstance().getSelections(uname);
        new LibraryWin(selections, currentUser).setVisible(true);
    }
    
    public void acceptSelection(String selection) throws DoesNotExistException {
                
        currentVideo = selection;
        
        String fName = VideoService.getInstance().getFile(selection);
        
        MoviePlayer.setVideo(fName, VideoService.getInstance().getSummary(selection), VideoService.getInstance().getDurationString(selection));
        MoviePlayer.setStartSecond(UserService.getInstance().getVideoStartTime(currentUser, currentVideo));
        if (MoviePlayer.instance == null)
            Application.launch(MoviePlayer.class, fName);
        else {
            MoviePlayer.instance.restart();
        }
        
    }
    
    public void videoPlayerClosed(double seconds) throws DoesNotExistException {
        
        UserService.getInstance().updateVideoRecord(currentUser, currentVideo, seconds);
        Serializer.saveIt();
        System.exit(0);
        
    }
    
}
