package model;

import java.io.Serializable;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
/**
 * @author Michael Chiang (mtc166)
 * @author Faizan Dalal (fsd15)
 */

public class SerializeImage implements Serializable {

	private static final long serialVersionUID = 1L;
	private int width, height;
	private int[][] pix;

	/**
	 * Gets the width of image
	 * @return Int width
	 */

	public int getWidth(){
		return width;
	}
	/**

	 * Gets the height of image
	 * @return Int height

	 */
	public int getHeight(){
		return height;
	}
	/**
	 * Retrieves the pixel matrix
	 * @return int[][]
	 */
	public int[][] getPix(){
		return pix;
	}
	/**

	 * Converts Image to 2d array of pixels

	 * @param image
	 */
	public void setImage(Image image){
		width = ((int) image.getWidth());
		height = ((int) image.getHeight());
		pix = new int[width][height];
		
		PixelReader read = image.getPixelReader();
		for(int i = 0; i < width; i++){
			for(int k = 0; k <height; k++){
				pix[i][k] = read.getArgb(i, k);
			}
		}
	}

	/**
	 * Converts the 2d array of pixels to an Image
	 * @return Image object

	 */
	public Image getImage(){
		WritableImage image = new WritableImage(width, height);
		
		PixelWriter writer = image.getPixelWriter();
		for(int i = 0; i < width; i++){
			for(int k = 0; k <height; k++){
				writer.setArgb(i, k, pix[i][k]);
			}
		}
		
		return image;
	}
	/**

	 * checks if two images are equal
	 * @param other	
	 * @return true if they're equal, else false

	 */
	public boolean ImageEqual(SerializeImage other){
		if(height != other.getHeight()){
			return false;
		}
		if(width != other.getWidth()){
			return false;
		}
		for(int i = 0; i < width; i++){
			for(int k = 0; k<height; k++){
				if(pix[i][k] != other.getPix()[i][k]){
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * Default Constructor
	 */
	public SerializeImage(){}
}
