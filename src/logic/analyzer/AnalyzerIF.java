/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.analyzer;

import com.atul.JavaOpenCV.Imshow;
import logic.featurepointextractor.EyeBrowsFPE;
import logic.featurepointextractor.EyeIrisesFPE;
import logic.featurepointextractor.FeaturePointsExtractorIF;
import logic.featurepointextractor.MouthFPE;
import logic.helpclass.Drawer;
import logic.helpclass.MatContainer;
import logic.helpclass.Parameters;
import logic.helpclass.Util;
import logic.imageloader.ImageLoaderIF;
import java.util.List;
import logic.helpclass.DetectedFeatures;
import logic.localizator.EyeBrowsLocalizator;
import logic.localizator.EyesLocalizator;
import logic.localizator.FaceLocalizator;
import logic.localizator.LocalizatorIF;
import logic.localizator.MouthLocalizator;
import logic.localizator.NoseLocalizator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 * Main interfce for face analyzing. 
 * Contains all localizators, detectors, image showers and image container.
 * Main function - analyze
 * @author Igor Dumchykov
 */
public class AnalyzerIF 
{
    private static final Logger     LOG = LogManager.getLogger(AnalyzerIF.class);
    
    public Imshow                   imShowOrig = new Imshow("Original Image");
    public Imshow                   imShowProc = new Imshow("Processed Image");
    
    protected                       ImageLoaderIF imLoader;

    public ImageLoaderIF getImLoader() {
        return imLoader;
    }
    
    protected FeaturePointsExtractorIF fetPointExt;
    
    protected LocalizatorIF faceLocalizator, eyePairLocalizator, eyeBrowLocalizator, mouthLocalizator, noseLocalizator;
    protected FeaturePointsExtractorIF irisesFPE, eyeBrowsFPE, mouthFPE;
    
    protected MatContainer container;

    public MatContainer getContainer() {
        return container;
    }
    
    public DetectedFeatures getDetectedFeatures() 
    {
        return container.featuresDeque.poll();
    }
    
    /**previously computed angle between eye centers. 
     * Used in rotateFrame function. Defined as Point, because that function returns
     * rotation matrix. Therefore, I decided to use Point object for containing 
     * previously computed angle as X-coordinate. Y-coordinate containes real angle
     */
    protected Point prevAlpha = null;
    
    public AnalyzerIF()
    {
        container = new MatContainer();
        faceLocalizator = new FaceLocalizator();
        eyePairLocalizator = new EyesLocalizator();
        eyeBrowLocalizator = new EyeBrowsLocalizator();
        mouthLocalizator = new MouthLocalizator();
        noseLocalizator = new NoseLocalizator();
        mouthFPE = new MouthFPE();
        
        irisesFPE = new EyeIrisesFPE();
        eyeBrowsFPE = new EyeBrowsFPE();
    }
    /**
     * Virtual function. 
     * Analyzing frame for face detection, feature points recognition, image rotation.
     */
    public void analyze(){}; 
    
    /**
     * Virtual function. Detects face, eye pair, nose, mouth, eye irises.
     * @return false if error occurs
     */
    protected boolean detectFaceFeatures()
    {
        //Localize face
        if(!faceLocalizator.localize(container))
            return false;
                
        //Localize eyepair
        if(!eyePairLocalizator.localize(container))
            return false;
                
        //Localize eyebrows
        if(!eyeBrowLocalizator.localize(container))
            return false;
   
        //localize mouth
        if(!mouthLocalizator.localize(container))
            return false;
        
        //localize nose
        noseLocalizator.localize(container);

        LOG.info("Detect face features: SUCCESS");
        
        return true;
    }
    
    /**
     * Virtual
     * Rotates frame according to detected eye irises: compares y-coordinates and computes
     * angles between eye centers, calculates rotation matrix and rotates target image.
     * @return -1 if detected irises are not appropriate or computed eye centers angle is wrong, 
     * 0 if rotation matrix is not detected, 1 if success
     */
    public int rotateFrameAndTrackTemplate(         double  in_baseDist, 
                                                    int     in_boundRect,
                                                    Rect[]  in_out_trackRectArr, 
                                                    Mat[]   out_trackTemplateMatArr,
                                                    Point[] in_pointLocationArr,
                                                    Scalar  in_color)
    {
        //check if tracked objects lost location
        double tmp = in_baseDist / (double)in_boundRect;

        if(tmp < Parameters.eyeRectAndBaseDiffMinThresh ||
               tmp > Parameters.eyeRectAndBaseDiffMaxThresh)
        {

            LOG.warn("baseDst: condition FAIL");

            return -1;
        }
        
        if(!trackRectArr(           container.grayFrame, 
                                    container.origFrame, 
                                    in_out_trackRectArr, 
                                    out_trackTemplateMatArr,
                                    in_pointLocationArr, 
                                    in_color))
            return -1;
        
        //save new centers to feature in container
        container.features.eyeBrowCenterPointArr = in_pointLocationArr;
        
        if(in_pointLocationArr.length == 2)
        {
            Drawer.drawRectanglePair(container.origFrame, in_out_trackRectArr[0], in_out_trackRectArr[1], in_color);
            Drawer.drawTrackedEyeCenters(container.origFrame, in_pointLocationArr[0], in_pointLocationArr[1]);
        }
                
        //rotate images: color for watching, gray for further processing 
        //(eye templates rotate by themselves)
        container.rotationMat = getRotationMat( container.origFrame, 
                                                container.faceRect, 
                                                in_pointLocationArr[0], 
                                                in_pointLocationArr[1], 
                                                prevAlpha);   
        
        if(prevAlpha != null && prevAlpha.x < 0)
        {
            LOG.warn("PrevAlpha: RESET");
            prevAlpha = new Point(0.0, 0.0);
            return -1;
        }
        
        if(container.rotationMat == null)
        {
            LOG.warn("Rotation angle is out of +/- " + Parameters.maxIrisAngle);
            return 0;
        }
            
        //save rot angle to features in container
        container.features.faceRotAngle = prevAlpha.y;
        
        Imgproc.warpAffine( container.origFrame, 
                            container.origFrame, 
                            container.rotationMat, 
                            container.origFrame.size(),
                            Imgproc.INTER_LINEAR,
                            Imgproc.BORDER_CONSTANT,
                            new Scalar(0));
        
        Imgproc.warpAffine( container.grayFrame, 
                            container.grayFrame, 
                            container.rotationMat, 
                            container.grayFrame.size(),
                            Imgproc.INTER_LINEAR,
                            Imgproc.BORDER_CONSTANT,
                            new Scalar(0));
            
        //recompute eyebrow interrocular distance
        container.eyeBrowBaseDst = Math.abs(container.eyeBrowBoundRectArr[0].x + container.eyeBrowBoundRectArr[0].width/2
                            - (container.eyeBrowBoundRectArr[1].x + container.eyeBrowBoundRectArr[1].width/2));
                
        LOG.info("eyeBrowBaseDst = " + container.eyeBrowBaseDst);
                    
        return 1;
    }
    
        /**
     * Obtain rotation matrix for further image rotation
     * Angle controlling algorithm: compute angle between eyes
     *                              check angle range: if it is bigger
     *                                  or smaller than threshold, we return null
     *                                  and freeze frame
     *                              check anle changing speed: compute first 
     *                                  deriviation on time. In other words, we
     *                                  compute differences betwee angle that was 
     *                                  computed before and now. If angle value
     *                                  changes faster than threshold, we reset
     *                                  angle and compute all face features from
     *                                  the begininig. Otherwise, compute rotation
     *                                  matrix.
     * @param frame
     * @param faceRect
     * @param prevalpha angle, computed previously. If it is null, do not compute,
     * because we are dealing with a static image
     * @param p1
     * @param p2
     * @return rotation matrix
     */
    public Mat getRotationMat(Mat frame, Rect faceRect, Point p1, Point p2, Point prevAlpha)
    {
        //compute angle
        double deltaX = p1.x - p2.x;
        double deltaY = p1.y - p2.y;
        double alpha = Math.tan(deltaY/deltaX) * 180. / Math.PI*(-1);
        
        double absAlpha = Math.abs(alpha);
        
        if(absAlpha > Parameters.maxIrisAngle)
            return null;

        if(prevAlpha != null)
        {
            double alphaDiff = Math.abs(absAlpha - prevAlpha.x);
            
            LOG.info("AlphaDif = " + alphaDiff);
            
            if(alphaDiff > Parameters.maxPrevAngDiff)
            { 
                prevAlpha.x = -1;
                return null;
            }
            prevAlpha.x = absAlpha;
        }
        
        //save roteted angle
        prevAlpha.y = alpha;
        
        Point centerPoint = new Point(faceRect.x + faceRect.width/2, faceRect.y + faceRect.height/2);

        return Imgproc.getRotationMatrix2D(centerPoint, -alpha, 1);
    }
    
    /**
     * Tracks rectangle patterns on image.
     * Algorithm:   check computed distance between patterns for thresh
     *                  if not excceeds: 
     *                      for each rectangle
     *                          track template, find new Rect location
     *                              if not null 
     *                                  save point coordinates
     *                              end if
     *                      end for
     *                      draw rectangle pair and centers
     *                  end if
     * @param in_srcMat
     * @param in_baseDist
     * @param in_boundRect
     * @param in_out_trackRectArr
     * @param out_trackTemplateMatArr
     * @param in_pointLocationArr
     * @param in_color
     * @return false if error, true otherwise
     */
    protected boolean trackRectArr( Mat     in_srcGrayMat, 
                                    Mat     in_srcColorMat,
                                    Rect[]  in_out_trackRectArr, 
                                    Mat[]   out_trackTemplateMatArr,
                                    Point[] in_pointLocationArr,
                                    Scalar  in_color)
    {                 
        
        //save prev location for further shifting computation
        
        //find new rect location
        for(int i = 0; i < 2; i++)
        {
            in_out_trackRectArr[i] = Util.trackTemplate(in_srcGrayMat, 
                    in_out_trackRectArr[i], out_trackTemplateMatArr[i]);
            
            //check if eyes lost image
            if(in_out_trackRectArr[i] == null)
            {

                LOG.warn("Tracking pattern is out of image scope");

                return false;
            } 
            
            //new center
            in_pointLocationArr[i] = new Point(in_out_trackRectArr[i].x + in_out_trackRectArr[i].width/2,
                    in_out_trackRectArr[i].y + in_out_trackRectArr[i].height/2);
            
            LOG.info("New centers: " + in_pointLocationArr[i].x + " " + in_pointLocationArr[i].y);
        }  
        return true;
    }
    
    /**
     * Reorganizes all detected points into array list by next protocol:
     * ArrayList Protocol:
     *                      Number of detected Faces
     *                      Faces coordinates: left-up, right-down
     *                          For each detected face eyes coordinates: 
     *                              for each eye points number, coord
     *                          For each detected face nose coordinate
     *                          For each detected face eyebrows coordinates
     *                              for each eye points number, coord
     *                          For each detected face mouth coordinates:
     *                              Points number, coordiantes
     */
    public List preparePointArray()
    {
        return null;
    }
    
    public double getFaceAngle()
    {
        if(prevAlpha != null)
            return prevAlpha.y;
        return 0.0;
    }
}
