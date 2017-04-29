
import gui.DisplayFacade;
import java.io.File;
import javax.swing.UIManager;
import project.Serializer;
import project.user.UserService;
import project.video.VideoService;


public class Driver {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {

        setupLookAndFeel();
        File f = new File("VidPlayer.ser");
        if (f.exists()) {
            Serializer.loadIt(f);
        }
        System.out.println(UserService.getInstance());
        System.out.println(VideoService.getInstance());

        DisplayFacade.getInstance().displayLoginWindow();
    }

    private static void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
