package project;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Walter {
	
	/*Chemin de l'image*/
	public static String path = "";   // !!!!! A INIDIQUER !!!!!
	/*nom de l'image*/
	public static String name = "ww.png";
	/*nombre de clusters/centres*/
    public static int K = 10;  
    /*Dimension*/
    public static int D = 3;		

    
    
    
    
	public static void main(String[] args) throws IOException {
		
							/*initialisation des fichiers et dossiers pour reusltats*/
		
		
		String cheminResImgWw = "resultatsMixGauss/Q3_Walter/Illustrations/";
		String cheminRes = "resultatsMixGauss/Q3_Walter/";
		File dossier = new File(cheminResImgWw);
		dossier.mkdirs();
		
		FileWriter fw1, fw2;
		fw1 = new FileWriter(cheminRes+"ScoreWw.d",true);
		fw2 = new FileWriter(cheminRes+"BestScoresWw.d");

		
		
								/*inisialisation donnees et conditions initiales*/
		
		
		System.out.println("# Initialisation des donnees et des Conditions initiales!");
	
		DonneesProject p = new DonneesProject();
		MixGauss m = new MixGauss();
		
        /*initialise les donnees avec l'image */
        double[][] tabColorNum = p.initialisationDonnees(path, name);
        Color[] tabColor = p.tabColor;
        
        /*Initialise les centres*/
        double[][] moy = p.initialisationCentres(tabColorNum, K);       
        /*Initialise les variances a 0.3*/
        double[][] var = p.initialisationVar(0.3, K, D);
		/*Initialise la densite*/
		double[] gho = p.initialisationDensite(K);
		
		
										/*programme principal*/
		
		
		System.out.println("# Apprentissage et Mise a jour !");
		

		double[][] r = new double[tabColorNum.length][K];
		double d = 1;
		double eps = 0.001;
		
		while(d > eps){		//tant qu'il y'a un grand deplacement
			r = m.assigner(tabColorNum, gho, var, moy);
			d = m.deplct(r,tabColorNum, moy, var,gho);
		}
		
		
											/*resultats*/
		
		
		System.out.println("# Enregistrement des resultats !"); 
		/*on note a chaque execution dans le fichier "scoreWw.d" les scores selon seed et K*/
		
		System.out.println(" -- Enregistrement dans 'ScoreWw.d' des scores en fonction de K et seed !");
		/*Colonne 0 : K. 		Colonne 1 : score. 		Colonne 2 : seed*/
	    
		fw1.write(K+"\t"+m.scoreTotal(tabColorNum,moy,var,gho)+"\t"+DonneesProject.seed+"\n");
		fw1.close();
		
		/*on transforme le fichier correspondant aux scores en tableau*/
	    double[][] q = Tools.tableauFichier(cheminRes+"ScoreWw.d",0,"\t");
	    
	    
		/*On enregistre les pixels correspondants a chaque gaussienne*/
	    
	    System.out.println(" -- Enregistrement des pixels de chaque gaussienne ! ");
	    
	    
	    int g=0;
	    while(g<K){    //pour chaque cluster
	    	Color[] newTabColorImg = new Color[tabColor.length];	 
	        for(int i=0 ; i<newTabColorImg.length ; i++){       		
	        	if (Tools.argMax(r[i])!=g){
	        		newTabColorImg[i] = new Color(255,255,255);	// si n'appartient pas au cluster c'est blanc
	        	}
	        	else newTabColorImg[i] = new Color(255-tabColor[i].getRed(), 255-tabColor[i].getGreen(), 255-tabColor[i].getBlue());		//sinon couleur normale
	        }
	        Tools.saveImagesGauss(newTabColorImg, p.width, p.height, cheminResImgWw, "img"+g); //on enregistre l'image
	        g++;
			 
		}	
	    
	    
	    
		System.out.println(" -- Enregistrement dans 'BestScoresWw.d' du meilleur score de chaque K selon seed !");

	    /*on cherche à partir du fichier precedent le meilleur score pour chaque cluster*/
	    double[][] meilleursScores = Tools.maximums(q);
	    /*on ecrit le cluster et son meilleur score dans un nouveau fichier 'BestScoresWw.d'*/
	    Tools.writeTable(meilleursScores,fw2);
	    fw2.close();
	        
	    
	    
	    
	    /*On enregistre l'image compressées*/
	    System.out.println(" -- Enregistrement de l'image compressee avec K = "+ K+ " !");
	   
	    Color[] tabColorCompressee = new Color[tabColor.length];		 
	    for(int i=0 ; i<tabColorCompressee.length ; i++){
	    	tabColorCompressee[i] = new Color(255-(int) (moy[Tools.argMax(r[i])][0]*255),255-(int) (moy[Tools.argMax(r[i])][1]*255),255-(int) (moy[Tools.argMax(r[i])][2]*255));
	    }
	    Tools.saveImagesGauss(tabColorCompressee, p.width, p.height, cheminResImgWw, "c");	

	}

}
