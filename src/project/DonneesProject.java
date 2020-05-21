package project;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class DonneesProject {

	public static int seed = 2357;
    public static Random GenRdm = new Random(seed);
    
    Color[] tabColor;
    int width;
    int height; 
    
    
    /**
     * initialise K variances à D dimension à n
     * 
     * 
     * @param n : un double (valeur de variance )
     * @param K : un entier (le nombre de clusters)
     * @param D : un entier (dimension)
     */
    public double[][] initialisationVar(double n, int K, int D){
    	double[][] var = new double[K][D];
		for (int i=0; i<var.length; i++){
			for(int j=0; j<var[0].length; j++){
				var[i][j] = n;
			}
		}
    	return var;   	
    }
	/**
	 * intialise les centres; on choisit une donnée au hasard comme centre puis on prend 
	 la plus loin comme deuxieme centre...etc 
	 
	 
     * @param x tableau 2D de doubles (les donnees)
     * @param K un entier (le nombre de clusters)
     */
	public double[][] initialisationCentres(double[][] x, int K){
		double[][] centres = new double[K][x[0].length];
		int i = GenRdm.nextInt(x.length-1);
		Tools.copieTableau(x[i], centres[0]);

		for(int k=1; k<centres.length; k++){
			double[] dist_min = new double[x.length];
			for(int m=0; m<x.length; m++){
				double[][] tmp = new double[k][centres[0].length];
				for(int j=0; j<tmp.length; j++){
					 Tools.copieTableau(centres[j], tmp[j]);
				}
				double[] distances = Tools.distanceCentres(x[m], tmp);
				dist_min[m] = Tools.min(distances);
			}
			 Tools.copieTableau(x[Tools.argMax(dist_min)], centres[k]);
		}		
		return centres;
	}
	/**
	 * initialise la densité
	 * 
	 * 
	 * @param K : un entier (le nombre de clusters)
	 */
	public double[] initialisationDensite(int K){
		double[] gho = new double[K];
		for(int i=0; i<gho.length; i++) 
			gho[i]=(double) 1/K;
		return gho;
	}
	/**
	 * initialise un tableau de double a partir d'une image, chaque ligne correspond 
	 aux composantes RGB d'un pixel d'une image
	 * @param path : String (chemin vers l'image)
	 * @param image : String (nom de l'image)
	 */
    public double[][] initialisationDonnees(String path, String image) throws IOException{
	    // Lecture de l'image ici
	    BufferedImage bui = ImageIO.read(new File(path+image));
	
	    width = bui.getWidth();
	    height = bui.getHeight();

	
	    int pixel = bui.getRGB(0, 0);
	    Color c = new Color(pixel);
	    // Calcul des trois composant de couleurs normalisé à 1
	    double[] pix = new double[3];
	    pix[0] = (double) c.getRed()/255.0;
	    pix[1] = (double) c.getGreen()/255.0;
	    pix[2] = (double) c.getBlue()/255.0;
	
	    int[] im_pixels = bui.getRGB(0, 0, width, height, null, 0, width);
	
	    /** Creation du tableau **/
	    tabColor= new Color[im_pixels.length];
	    for(int i=0 ; i<im_pixels.length ; i++)
	    tabColor[i]=new Color(im_pixels[i]);
	
	    /** inversion des couleurs **/
	    for(int i=0 ; i<tabColor.length ; i++)
	    tabColor[i]=new Color(255-tabColor[i].getRed(),255-tabColor[i].getGreen(),255-tabColor[i].getBlue());
	
	    /** sauvegarde de l'image **/
	    BufferedImage bui_out = new BufferedImage(bui.getWidth(),bui.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
	    for(int i=0 ; i<height ; i++)
	    {
	    for(int j=0 ; j<width ; j++)
	        bui_out.setRGB(j,i,tabColor[i*width+j].getRGB());
	    }
	    ImageIO.write(bui_out, "PNG", new File(path+"Inv"+image));	    
	    
	    double[][] tabTest = new double[im_pixels.length][3];
	   	    
	    for(int i = 0; i<tabTest.length; i++){
	    	tabTest[i][0]= (double) tabColor[i].getRed()/255;
	    	tabTest[i][1]= (double) tabColor[i].getGreen()/255;
	    	tabTest[i][2]= (double) tabColor[i].getBlue()/255;        
	    }
	    return tabTest;
    }
    
    
}
