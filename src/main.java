import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
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


public class main {

	static BufferedImage Mat2BufferedImage(Mat matrix)throws Exception {        
	    MatOfByte mob=new MatOfByte();
	    Imgcodecs.imencode(".jpg", matrix, mob);
	    byte ba[]=mob.toArray();

	    BufferedImage bi=ImageIO.read(new ByteArrayInputStream(ba));
	    return bi;
	}
	
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	public static void main(String[] args) {
		
	    Mat frame = new Mat();
	    //0; default video device id
	    VideoCapture camera = new VideoCapture(0);
	    JFrame jframe = new JFrame("Title");
	    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    jframe.setSize(600, 600);
	    
	    JLabel vidpanel = new JLabel();
	    vidpanel.setSize(600, 600);
	    
	    
	    jframe.setContentPane(vidpanel);
	    jframe.setVisible(true);
	    
	    // This is the facial information file.
	    String XMLFile = "C:\\Users\\Lenovo\\eclipse-workspace\\CVTest2\\lib-opencv\\haarcascade_frontalface_default.xml";
	    CascadeClassifier classifier = new CascadeClassifier(XMLFile);
	    
	    while (true) {
	        if (camera.read(frame)) {
	        	
	        	/** 
	        	 * Detect the facial attributed from the Frame Matrix.
	        	 */
	        	MatOfRect facialDetections = new MatOfRect();
	        	classifier.detectMultiScale(frame, facialDetections);
	        	System.out.println("Detected " + facialDetections.toArray().length + " Faces.");
	        	
	        	/**
	        	 *  Learn about the face data.
	        	*/
	        	Vector<Mat> people_face_data = new Vector<Mat>();
	        	for(Rect rect: facialDetections.toArray()) {
	        		Mat faceOnly = frame.submat(rect);
	        		
	        		System.out.println(people_face_data.contains(faceOnly));
	        		Imgcodecs.imwrite(String.valueOf("faces/" + System.currentTimeMillis()) + ".bmp", faceOnly);
	        		
	        		// Append the face data to the array if not already added.
	        		if(!people_face_data.contains(faceOnly)) {
	        			System.out.println("Saving new persons image data.");
	        			people_face_data.add(faceOnly);
	        		}
	        		else {
	        			
	        			// Display the ID of the people found, as they are contaiedn.
	        			System.out.println("Found Existing Person ");
	        		}
	        	}
	        	
	        	/**
	        	 * Render the face detected rectangle onto the Image Matrix
	        	 */
	        	for (Rect rect : facialDetections.toArray()) {
	        			Imgproc.rectangle(
	        	            frame,                                               // where to draw the box
	        	            new Point(rect.x, rect.y),                            // bottom left
	        	            new Point(rect.x + rect.width, rect.y + rect.height), // top right
	        	            new Scalar(0, 0, 255),
	        	            3                                                     // RGB colour
	        	         );
	        			
	        	}
	        	
	            ImageIcon image = null;
				try {
					image = new ImageIcon(Mat2BufferedImage(frame));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				/**
				 *  Final render.
				 */
	            vidpanel.setIcon(image);
	            vidpanel.repaint();

	        }
	    }
	}
	
}
