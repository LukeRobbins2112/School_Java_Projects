
package project.user;

import java.io.Serializable;
import project.video.Video;

/**
 *
 * @author LukeRobbins2112
 */
public interface User extends Serializable {
    
    Video.Rating getRatingLevel();
    double getVideoStartTime(String currentVideo);
    boolean hasPassword(String pswdIn);
    void setLastLogin();
    void updateVideoRecord(String currentVideo, double seconds);
    
}
