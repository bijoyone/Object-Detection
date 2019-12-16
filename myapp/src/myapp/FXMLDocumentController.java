package myapp;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
//import static org.opencv.videoio.Videoio.CAP_PROP_FRAME_HEIGHT;
//import static org.opencv.videoio.Videoio.CAP_PROP_FRAME_WIDTH;

public class FXMLDocumentController implements Initializable 
{    
    private MatOfByte memory;
    private Mat mat;
    private VideoCapture cap;
    private Image image1;   
    private MatOfRect obj;
    private CascadeClassifier objdetect;
    
    @FXML
    private AnchorPane ap;    
    @FXML
    private ImageView p1;    
    
    private Image Mat2BufferedImage(Mat m)
    {
        memory = new MatOfByte();
        Imgcodecs.imencode(".bmp", m, memory);
        return (new Image(new ByteArrayInputStream(memory.toArray())));
    }  
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        mat = new Mat();
        obj = new MatOfRect();
        // objdetect = new CascadeClassifier("haarcascade_frontalface_alt.xml");
        objdetect = new CascadeClassifier("cascade.xml");
        p1 = new ImageView();       
        
        //p1.setFitWidth(640);
        //p1.setFitHeight(480);       
        
        cap = new VideoCapture();     
        //cap.set(CAP_PROP_FRAME_WIDTH, 640);
        //cap.set(CAP_PROP_FRAME_HEIGHT, 480);
        cap.open(0);
               
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), ev -> {
          //  if(cap.isOpened())
            {
                cap.read(mat);
                if(!mat.empty()) 
                {
                    //image1 = Mat2BufferedImage(mat);
                    objdetect.detectMultiScale(mat, obj);
                    for(Rect rect : obj.toArray())
                    {
                        Imgproc.rectangle(mat, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0,255,0));
                    }
                     image1 = Mat2BufferedImage(mat);
                     p1.setImage(image1);                    
                }
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        ap.getChildren().add(p1);        
    }   
}