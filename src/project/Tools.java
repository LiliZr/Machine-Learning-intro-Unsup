package project;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;

public class Tools {
	/**
     * renvoie l indice du max d'un tableau de doubles x 
     * 
     * 
     * @param x tableau de doubles
    */

	public static int argMax(double[] x){
		int idx=0; double max = x[0];
		for (int i=0; i<x.length; i++){
			if (x[i]>max){
				max = x[i];
				idx = i;
			}
		}
		return idx;
	}

	/**
	 * renvoie la somme de la colonne j du tableau de doubles r

	 * @param j un entier 
	*/

	public static double sommeColonne(double[][] r, int j){
		double somme=0;
		for (int i=0; i<r.length; i++){
			somme+=r[i][j];
		}
		return somme;
	}
	/**
	 * renvoie la somme du tableau de doubles r
	 * 
	 * 
	 * @param r tableau de doubles
	*/
	public static double somme(double[] r){
		double somme=0;
		for (int i=0; i<r.length; i++){
			somme+=r[i];
		}
		return somme;
	}
	/**
	 * calcule la distance entre x et centre
	 * 
	 * 
	 * @param x  tableau de doubles
	 * @param centre  tableau de doubles
	 */

	public static double distance(double[] x, double[] centre){
		double dist=0;
		for (int i=0; i<x.length; i++){
			dist+=Math.pow(centre[i]-x[i], 2); 		
		}
		dist = Math.sqrt(dist);
		return dist;
	}

	/**
	 * calcule la distance entre x et chaque centre
	 * 
	 * 
	 * @param x  tableau de doubles
	 * @param centres  tableau 2D de doubles
	 */
	public static double[] distanceCentres(double[] x, double[][] centres){
		double[] t = new double[centres.length];
		for (int i=0; i<t.length; i++){
			t[i] = distance(x, centres[i]);		//on calcule la distance avec chaque centre
		}
		return t;
	}

	/**
	 * renvoie le minimum d'un tableau
	 * @param t  tableau de doubles
	 */
    public static double min( double[] t){
    	double min= t[0];
    	for (int i =0; i<t.length; i++){
    		if (t[i]<min) min =t[i];
    	}
    	return min;
    }
 
    /**
     * renvoie max de t
     * @param t tableau de doubles
     * @return
     */
    public static double max( double[] t){
    	double max= t[0];
    	for (int i =0; i<t.length; i++){
    		if (t[i]>max) max=t[i];
    	}
    	return max;
    }

    /**
     * Copie t dans 'copie'
     * 
     * 
     * @param t tableau de doubles
     * @param copie tableau de doubles
     */
	public static void copieTableau(double[] t, double[] copie){
		for(int i=0; i<copie.length; i++){
			copie[i]=t[i];
		}
	}
	

	
    /**
     * Copie t dans 'copie'
     * 
     * 
     * @param t tableau 2D de doubles
     * @param copie tableau 2D de doubles
     */
	public static void copieTableau2D(double[][] t, double[][] copie){
		for(int i=0; i<copie.length; i++){
			for(int j=0; j<copie[0].length; j++){
				copie[i][j]=t[i][j];
			}
		}

	}

	/**
	 * renvoie l'histogramme de ech
	 * @param xmin un double
	 * @param xmax un double
	 * @param NbCases un entier 
	 * @param ech tableau  de doubles
	 */
	public static double[][] histogramme(double xmin, double xmax, int NbCases, double[] ech) {
        double[][] Histo = new double[NbCases][2];
        double tailleCase = (xmax-xmin)/NbCases;
        int caseCorrespondante = -1;
        double absc=xmin;
        
        //l'abscisse
        for(int j=0; j <Histo.length;j++){
        	Histo[j][0] = absc +tailleCase ;
        	absc = absc+tailleCase;
        }
        //les frequences
        for(int i=0; i<ech.length; i++) {
        	caseCorrespondante = (int) ((ech[i]-xmin)/tailleCase);
        	if(ech[i]>=xmax)caseCorrespondante=NbCases-1;
        	if(ech[i]<xmin)caseCorrespondante=0;
        	Histo[caseCorrespondante][1]+=1;   	
        }
        return Histo;
    }
	
	/**
	 * sauvegarde une image  de width*height a partir d'un tableau de Color 'newTabColor'

	 * 
	 * @param newTabColor tableau de Color
	 * @param width un entier
	 * @param height un entier
	 * @param path chemin ou on enregistre l'image
	 * @param name nom de l'image
	 * @throws IOException
	 */
	public static void saveImagesGauss(Color[] newTabColor,  int width, int height, String path, String name) throws IOException{	        
	    BufferedImage bui_out = new BufferedImage(width, height,BufferedImage.TYPE_3BYTE_BGR);
	    for(int i=0 ; i<height ; i++){
	    	for(int j=0 ; j<width ; j++){
	        	bui_out.setRGB(j,i,newTabColor[i*width+j].getRGB());
	        }
	    }
	    ImageIO.write(bui_out, "PNG", new File(path+name+".png"));
	}

	/**
	 * transforme fichier en tableau
	 * 
	 * 
	 * @param fichierName : String. nom du fichier
	 * @param l : un entier. à partir de quelle ligne du fichier on commence 
	 * @param sep : String. le separateur utilisé 
	 */
	public static double[][] tableauFichier(String fichierName, int l, String sep) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(fichierName));
		String ligne;
		int nbLigne=0;
		int nbColonne=0;
		
		//on recupere le nombre de lignes et de colonnes
		while ((ligne = reader.readLine()) != null) {
		   String[] mots = ligne.split(sep);
		   if(nbLigne==0) nbColonne = mots.length;
		   nbLigne++;
		}
		//on cree le tableau 
		double[][] tab = new double[nbLigne-l][nbColonne];
		nbLigne=0;
		reader = new BufferedReader(new FileReader(fichierName));
		while ((ligne = reader.readLine()) != null) {
			if (nbLigne>=l){ //à partir de cette ligne
				String[] mots = ligne.split(sep);
				if(mots.length==nbColonne){
					for(int i=0; i<mots.length; i++){
						//System.out.println(mots[i]);
						tab[nbLigne-l][i] =  Double.valueOf(mots[i]);			
					}
				}			
			}
			nbLigne++;
		}
		return tab;
	}
	
	/**
	 * prend un tableau (le fichier traduit en tableau) avec la premiere colonne 
	 correspond à k et la deuxieme aux scores et renvoie un tableau 
	 ou la premiere colonne correspond a k et la deuxieme colonne son meilleur score ssocié
	 * 
	 * 
	 * @param tableau : tableau 2D de doubles. 
	 */
	public static double[][] maximums(double[][] tableau){
		//cree une arraylist avec le nombre de centres utilisé 
		ArrayList <Double> centres = new ArrayList <Double>();
		for (int i=0; i<tableau.length; i++){
			if (!centres.contains(tableau[i][0])) centres.add(tableau[i][0]);	
		}
		//trie l'arraylist
		Collections.sort(centres);
		//convertit larraylist en tableau et trouve le score max qui correspond a chaque nombre de clusters utilisé
		
		double[][] t = new double[centres.size()][2];
		int i=0;
		for (Double d : centres){
			t[i][0]=d;
			int k=0;
			for(int j=0; j<tableau.length; j++){ 
				if (tableau[j][0]==d){ 
					if (k==0) t[i][1] = tableau[j][1];
					if (t[i][1]<tableau[j][1]) t[i][1]=tableau[j][1]; //on cherche le maximum pour un cluster
					k++;
				}
			}
			i++;
		}
		
		return t;
	}
	

	/**
	 * ecrit dans un fichier fw, le tableau t
	 * @param t tableau 2D de doubles
	 * @param fw le fichier
	 */
	
	public static void writeTable(double[][] t, FileWriter fw) throws IOException{
	    for(int i=0; i<t.length; i++){
	    	for(int j=0; j<t[0].length; j++){
	    		fw.write(t[i][j]+"\t");
	    	}
	    	fw.write("\n");
	    }
		
	}
}
