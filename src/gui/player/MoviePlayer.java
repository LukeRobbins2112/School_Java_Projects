/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.player;

import gui.DisplayFacade;
import gui.DisplayFacade;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import project.DoesNotExistException;

/**
 *
 * @author Hieldc
 */
public class MoviePlayer extends Application {



    private double originalWidth;
    private double originalHeight;
    private double originalAspect;
    private final Adjusting adjusting = new Adjusting();

    private MediaView view;
    private final VBox vbox = new VBox();
    private static Stage stage;
    private final Timeline slideIn = new Timeline();
    private final Timeline slideOut = new Timeline();
    private MediaPlayer player;
    private final Slider slider = new Slider();
    private static String fileName;
    private static double startTime;
    private static String fullName;
     private static String duration;
     public static MoviePlayer instance;

    public static void setVideo(String file, String fName, String durIn) {
        fileName = file;
        fullName = fName;
        duration = durIn;
    }
    public static void setStartSecond(double videoStartTime) {
        startTime = videoStartTime;
    }

    public void restart() {
        start(new Stage());
    }
    
    @Override
    public void start(Stage stageIn) {

        instance = this;
        stage = stageIn;
        stage.setX(0);
        stage.setY(0);

        stage.setOnHiding(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                event.consume();
                try {
                    DisplayFacade.getInstance().videoPlayerClosed(player.getCurrentTime().toSeconds());
                } catch (DoesNotExistException ex) {
                    Logger.getLogger(MoviePlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        Group root = new Group();
       
        File f = new File(fileName);
        Media media = null;
        try {
            media = new Media(f.toURI().toString());
        } catch (Exception e) {
            System.err.println("Cannot open video file: " + f.getAbsolutePath());
            System.err.println(e.getMessage());
            System.exit(0);
        }
        player = new MediaPlayer(media);
        view = new MediaView(player);

        root.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                slideIn.play();
            }
        });
        root.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                slideOut.play();
            }
        });

        vbox.getChildren().add(slider);

        root.getChildren().add(view);
        root.getChildren().add(vbox);

        Scene scene = new Scene(root, Color.BLACK);
        stage.setScene(scene);
        stage.show();

        player.play();
        player.setOnReady(new Runnable() {
            @Override
            public void run() {
                player.seek(Duration.seconds(startTime));
                originalWidth = player.getMedia().getWidth();
                originalHeight = player.getMedia().getHeight();
                originalAspect = originalWidth / originalHeight;
                stage.setWidth(originalWidth);
                stage.setHeight(originalHeight);
                slider.setMin(0.0);
                slider.setValue(0.0);
                slider.setMax(player.getTotalDuration().toSeconds());
                processResize();
            }
        });

        player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> ov, Duration dur, Duration current) {
                if (!adjusting.adjusting) {
                    slider.setValue(current.toSeconds());
                    stage.setTitle("Now Playing: " + fullName + " [" + calcTime(current) + " of " + duration + "]");
                }
            }
        });

        view.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton() == MouseButton.SECONDARY) {
                    double y = t.getY();
                    double h = view.getFitHeight();
                    if (y > h / 2) {
                        double v = Math.max(0.0, player.getVolume() - 0.1);
                        player.setVolume(v);
                    } else {
                        double v = Math.min(1.0, player.getVolume() + 0.1);
                        player.setVolume(v);
                    }
                } else {
                    if (player.getStatus().equals(MediaPlayer.Status.PLAYING)) {
                        player.pause();
                    } else {
                        player.play();
                    }
                }
            }
        });

        slider.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                player.seek(Duration.seconds(slider.getValue()));
            }
        });

        slider.setOnMouseDragReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                player.seek(Duration.seconds(slider.getValue()));
            }
        });

        slider.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                adjusting.adjusting = true;
            }
        });
        slider.setOnMouseReleased(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                adjusting.adjusting = false;
            }
        });

        view.fitWidthProperty().bind(stage.widthProperty());
        view.fitHeightProperty().bind(stage.heightProperty());

        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                processResize();
            }
        });

        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                processResize();
            }
        });

    }

    private String calcTime(Duration current) {
        int h = (int) current.toHours();
        int m = (int) current.toMinutes() - (60 * h);
        int s = (int) current.toSeconds() - (3600 * h) - (60 * m);
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    private void processResize() {

        stage.setHeight(view.getFitWidth() / originalAspect * 1.04);

        vbox.setMinSize(view.getFitWidth() - 15, 100);
        vbox.setTranslateY(stage.getHeight());

        slideIn.getKeyFrames().clear();

        slideIn.getKeyFrames().addAll(
                new KeyFrame(new Duration(0),
                        new KeyValue(vbox.translateYProperty(), 0),
                        new KeyValue(vbox.opacityProperty(), 0.0)),
                new KeyFrame(new Duration(200),
                        new KeyValue(vbox.translateYProperty(), 3),
                        new KeyValue(vbox.opacityProperty(), 1.0))
        );

        slideOut.getKeyFrames().clear();
        slideOut.getKeyFrames().addAll(
                new KeyFrame(new Duration(0),
                        new KeyValue(vbox.translateYProperty(), 3),
                        new KeyValue(vbox.opacityProperty(), 1.0)),
                new KeyFrame(new Duration(200),
                        new KeyValue(vbox.translateYProperty(), 0),
                        new KeyValue(vbox.opacityProperty(), 0.0))
        );

    }

    class Adjusting {

        public boolean adjusting = false;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
