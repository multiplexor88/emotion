/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emotion;

//import com.atul.JavaOpenCV.Imshow;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.AbstractOwnableSynchronizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import logic.analyzer.AnalyzerIF;
import logic.analyzer.VideoAnalyzer;
import logic.helpclass.Parameters;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

/**
 *
 * @author Administrator
 */
public class FXController 
{
    private  AnalyzerIF             analyzer;
    
    @FXML
    private Button                  startCameraBtn = new Button("Start camera");
    @FXML
    private ImageView               cameraCaptureView;
    @FXML
    private Button                  showCameraCaptureBtn;
    
    @FXML
    private GridPane                 virtualScene;
    
    public FXController()
    {
        startCameraBtn = new Button("Start camera");
        showCameraCaptureBtn = new Button("Show Camera Capture");
        cameraCaptureView = new ImageView();
        analyzer = new VideoAnalyzer(Parameters.CAMERA_TYPE.USB_MONO, false, 0);
        virtualScene = new GridPane();
    }
    
    @FXML
    protected void startFaceFeaturesDetection(ActionEvent event) 
    {
        
        //new Thread(()->analyzer.analyze()).start();
        drawScene();
        
    }
    
    @FXML
    protected void displayCameraCapture(ActionEvent event)
    {
        new Thread(()->
        {
            while(true)
            {
                Mat mat = analyzer.getContainer().origFrame;
                if(mat != null)
                {
                    Image im = toImage(mat);
                    if(im != null)
                    {
                        cameraCaptureView.setImage(im);
                    }
                }
            }
        }).start();
    }
    
    private void drawScene()
    {
        Line line = new Line(0,0,100,100);
 //       virtualScene.getChildren().add(line);
        
        Line line1 = new Line(0,0,100,-100);
        virtualScene.getChildren().add(line1);
        
        Line line0 = new Line(0,0,100,0);
        virtualScene.getChildren().add(line0);
        
        System.out.println(line.getBoundsInParent());
        System.out.println(line.getBoundsInLocal());
        System.out.println(line.getLayoutBounds());
        BorderPane p = null;
        
    }
    
    
    
    private Image toImage( Mat frame )
    { 
    //    if(frame.empty())
    //       return null;
        MatOfByte buffer = new MatOfByte();
        Highgui.imencode(".png", frame, buffer);
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }
    
}
