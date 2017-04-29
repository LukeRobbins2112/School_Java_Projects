/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.user;

import project.video.Video.Rating;

/**
 *
 * @author LukeRobbins2112
 */
public class UserFactory {
    
    public static UserImpl makeUser(String username, String password, Rating rating){
        return new UserImpl(username, password, rating);
    }
    
}
