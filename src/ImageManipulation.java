import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

//Non-Linear Generative Frame Composition

import javax.imageio.ImageIO;

public class ImageManipulation {
	
	public static int counter = 0;
	
	public static void main(String[] args) throws IOException {

		for(int i = 0; i < 200; i++)
		{
			DoImages(i);
			DoImages(i);		
			DoImages(i);
		}
	}
	
	public static void DoImages(int number) throws IOException
	{
		BufferedImage[] imagesRaw = new BufferedImage[5];
	      //image array set up. size needs to be changed according to input before executing.
	      
	      
	      /*for(int i = 0; i < imagesRaw.length; i++)
	      {
	    	  //I add a letter suffix to the set of images I'm inputting for a given run
	    	  //all the images are pasted in the default package folder, and putting a suffix allows me to chose without a lot of manipulation in the folder
	    	  imagesRaw[i] = ImageIO.read(ImageManipulation.class.getResource("Vid " + (number) + ".jpg"));
	      } */
		  imagesRaw[0] = ImageIO.read(ImageManipulation.class.getResource("Vid " + (number) + ".jpg"));
		  System.out.println("number is " + number);
		  imagesRaw[1] = ImageIO.read(ImageManipulation.class.getResource("Vid " + (number) + ".jpg"));
		  System.out.println("number is " + number);

		  imagesRaw[2] = ImageIO.read(ImageManipulation.class.getResource("Vid " + (number) + ".jpg"));

		  imagesRaw[3] = ImageIO.read(ImageManipulation.class.getResource("Vid " + (199 - number) + ".jpg"));
		  imagesRaw[4] = ImageIO.read(ImageManipulation.class.getResource("Vid " + (199 - number) + ".jpg"));

		  System.out.println("number is " + number);

		  
	      int height = (imagesRaw[0].getHeight() - 130);
	      int width = imagesRaw[0].getWidth();  
	      
	      ArrayList<int[][]> imagesData = new ArrayList<int[][]>(); //we are still storing each image as a 2d array with data of each pixel
	      for(int x = 0; x < imagesRaw.length; x++)
	      {
	    	  imagesData.add(getPixelData(imagesRaw[x])); 
	    	  
	    	  //put all image data in an array list for easier access, see function below 
	    	  //this removes the need for declaring a 3d-array 
	      }
	      
	      
	      
	      int[][] randomImage = new int[height][width];  //2d array which will be filled with image data
	      
	      for(int h = 0; h < height; h++)	//loops through each row of pixels in target image
	      {
	    	 
    		 Random random = new Random();				
		      int randomNum = random.nextInt(imagesRaw.length);	//random number generated, from 0 to number of images
		      int[][] full = imagesData.get(randomNum);	 //random image selected and stored temporarily, probably a better way to do it
		      randomImage[h] = full[h];		// h'th row of pixels in target image is assigned a row of pixel from the random image stored above    	      
		      
	    	 
	      }
	      
	      
	      //here starts the process of writing the image
	      int[] pixelVector = new int[width*height]; //this will be used to convert 2d pixel data to 1d,
	      
	      BufferedImage	image1 = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB); //create new image instance to which we'll feed our data
	      WritableRaster raster = image1.getRaster(); //we will be manipulating pixels of target image through raster
	      
	      for(int i = 0; i < height; i++)
	      {
	    	  for(int j = 0; j < width; j++)
	    	  {
	    		  //converts image 2d data into 1d
	    		  pixelVector[i * width + j] = randomImage[i][j];
	    	  }
	      }
	      raster.setDataElements(0, 0, width, height, pixelVector);		//data fed to raster for setup	
	      
	      String pathOutput = new String();
	      if(counter < 10)
	      {
	    	  pathOutput = "000";
	      }
	      else if(counter >= 10 && counter < 100)
	      {
	    	  pathOutput = "00";
	      }
	      else if(counter >= 100)
	      {
	    	  pathOutput = "0";
	      }
	      ImageIO.write(image1, "JPG", new File("Vid2Pass2.mp4" + pathOutput	 + counter + ".jpg"));	//image created and stored
	      counter++;
	      System.out.println(counter);
	      
	}
	
	public static int[][] getPixelData(BufferedImage image)
	{
		//Code found on stack overflow, added comments to better understand
		final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();	//pixel data is stored in a byte array form, also makes it easier to seperate RGB channels
	      final int width = image.getWidth();
	      final int height = image.getHeight();
	      final boolean hasAlphaChannel = image.getAlphaRaster() != null;	//checks for alpha channel

	      int[][] result = new int[height][width];
	      if (hasAlphaChannel) {
	         final int pixelLength = 4;
	         for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
	            int argb = 0;
	            //bit shifting performed to extract argb values seperately
	            argb += (((int) pixels[pixel] & 0xff) << 24); // alpha	
	            argb += ((int) pixels[pixel + 1] & 0xff); // blue
	            argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
	            argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
	            result[row][col] = argb;	//argb value retrieved for each pixel stored in a 2d array similar to above
	            col++;
	            if (col == width) {
	            	//moves to next row
	               col = 0;	
	               row++;
	            }
	         }
	      }
		else 
		{
			//conditional in position to get data from pictures with both argb or just rgb data
	         final int pixelLength = 3;
	         for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength)
	         {
	            int argb = 0;
	            argb += -16777216; // 255 alpha
	            argb += ((int) pixels[pixel] & 0xff); // blue
	            argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
	            argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
	            result[row][col] = argb;
	            col++;
	            if (col == width) {
	               col = 0;
	               row++;
	            }
	         
	         }
			
		}
		
		return result;
	}
}
