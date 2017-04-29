
package project.video;

import java.security.InvalidParameterException;

/**
 *
 * @author LukeRobbins2112
 */
public final class VideoImpl implements Video{
    
    private final String name;
    private final int durationSec;
    private final Rating rating;
    private final String fileName;
    
    public VideoImpl(String name, int durationSec, Rating rating, String fileName){
        this.name = validatedName(name);
        this.durationSec = validatedDurationSec(durationSec);
        this.rating = rating;
        this.fileName = validatedFileName(fileName);
    }
    
    private String validatedName(String nameIn) throws InvalidParameterException{
        if (nameIn == null || nameIn.isEmpty()){
            throw new InvalidParameterException("Name cannot be null or empty");
        }
        return nameIn;
    }
    
    private int validatedDurationSec(int durationSecIn) throws InvalidParameterException {
        if (durationSecIn <= 0){
            throw new InvalidParameterException("The video duration seconds must be greater than zero.");
        }
        return durationSecIn;
    }
    
    private String validatedFileName(String fileName){
        if (fileName == null || fileName.isEmpty()){
            throw new InvalidParameterException("File name must not be null or empty.");
        }
        return fileName;
    }
    
    @Override
    public int durationSec() {
        return this.durationSec;
    }

    @Override
    public String durationString() {
        int hours = this.durationSec / 3600;
        int minutes = (this.durationSec % 3600) / 60;
        int seconds = this.durationSec % 60;
        
        String durationString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return durationString;
    }

    @Override
    public String getFileName() {
        return this.fileName;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Rating getRating() {
        return this.rating;
    }
    
    @Override
    public String toString(){
        String output = String.format("%s\t (%s)  [%s]  <%s>", this.getName(), this.getRating(), 
                this.durationString(), this.getFileName());
        
        return output;
    }
    
    
    
}
