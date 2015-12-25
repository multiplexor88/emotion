/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emotion;

import graphics.DrawedEntireObject;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Stack;
import javafx.application.Application;
import javafx.beans.binding.When;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import javafx.stage.Stage;
import logic.analyzer.AnalyzerIF;
import logic.analyzer.VideoAnalyzer;
import logic.helpclass.DetectedFeatures;
import org.opencv.core.Core;
import org.opencv.core.Mat;

/**
 *
 * @author igor
 */
public class Emotion extends Application {
    
    private DrawedEntireObject      obj;
    private  AnalyzerIF             analyzer = new VideoAnalyzer(logic.helpclass.Parameters.CAMERA_TYPE.USB_MONO, false, 0);
    
    boolean isFirstSaving = true;
    Node n;
    @Override
    public void start(Stage primaryStage) throws IOException {
        //Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
        //Scene scene = new Scene(root);

        Screen screen = Screen.getPrimary();
        double width = screen.getBounds().getWidth();
        double height = screen.getBounds().getHeight();
        
        Pane root = new Pane();
        root.setPrefSize(width/2, height/2);
        
        Button startBtn = new Button("Start");
        startBtn.setPrefSize(100, 50);
        EventHandler<ActionEvent> btnClickedEvent = e->startProcess();
        startBtn.setOnAction(btnClickedEvent);
        root.getChildren().add(startBtn);
        
        obj = new DrawedEntireObject(root); 

 //       drawShapes(root);
        Scene scene = new Scene(root, screen.getBounds().getWidth()/2,screen.getBounds().getHeight()/2);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }
 
    public void drawShapes(Pane root)
    {
        
    }
    public void startProcess()
    {

        new Thread(()->analyzer.analyze()).start();
        new Thread(()->
        {
            while(true)
            {
                DetectedFeatures features = analyzer.getDetectedFeatures();
                
                try {Thread.sleep(1);}  catch (InterruptedException ex){}
                
                if(features != null && features.isStocked)
                {
                    isFirstSaving = true;
                    continue;
                }
                
                if(features != null && isFirstSaving)
                {
                    obj.firstSaving(features);
                    isFirstSaving = false;
                }
                else if(features != null && !isFirstSaving)
                {
                    obj.recomputePosition(features);
                }
            }
        }).start();
        
        
    }
}


/*/*        
        Pane root = new Pane();
        Screen screen = Screen.getPrimary();

//        Line line = new Line(0,0,-500,-500);
        Button b1 = new Button("1");
        b1.setEffect(new DropShadow());
        b1.setRotate(15);
        Button b2 = new Button("2");
        b2.setManaged(false);
        b2.setMaxSize(100,100);
        Button b3 = new Button("3");
        
        b2.relocate(100, 150);b2.resize(100, 100);
        b2.setOnAction(e->
        {
            b3.setVisible(!b3.isVisible());
            if(b3.isVisible())
                b2.setText("Visible");
            else
                b2.setText("Invisible");
        });
      
        drawShapes(root);
//        b2.textProperty().bind(new When(b3.visibleProperty()).then("Make invisible").otherwise("Make visible"));
//        b3.managedProperty().bind(b3.visibleProperty());
        
        root.getChildren().addAll(b1,b2,b3);
       
        //scene.setCursor(Cursor.WAIT);
        
        Scene scene = new Scene(root, screen.getBounds().getWidth()/2,screen.getBounds().getHeight()/2);
        
//        scene.getStylesheets().add(0,"styles/styles.css");
        
        primaryStage.setScene(scene);
        primaryStage.show();
        
        
        System.out.println("Layout bounds");
        System.out.println(b1.getLayoutBounds());
        System.out.println(b2.getLayoutBounds());
        System.out.println(b3.getLayoutBounds());
        
        System.out.println("In local");
        System.out.println(b1.getBoundsInLocal());
        System.out.println(b2.getBoundsInLocal());
        System.out.println(b3.getBoundsInLocal());
        
        System.out.println("In parent");
        System.out.println(b1.getBoundsInParent());
        System.out.println(b2.getBoundsInParent());
        System.out.println(b3.getBoundsInParent());
        //FXController controller = loader.getController();

        //AnalyzerIF analyzer = new VideoAnalyzer(CAMERA_TYPE.USB_MONO, false, 0);
        //analyzer.analyze();**/