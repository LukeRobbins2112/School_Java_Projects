
package project.video;

import java.io.Serializable;

/**
 *
 * @author LukeRobbins2112
 */
public interface Video extends Serializable {
    
    public enum Rating{
        G, PG, PG13, R, NC17;
    }
    
    int durationSec();
    String durationString();
    String getFileName();
    String getName();
    Rating getRating();
    
    
    
}
