package project;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Mnms {
	
	/*Chemin de l'image*/
	public static String path = "";   //!!!!!! A INIDIQUER!!!!!
	/*nom de l'image*/
	public static String name = "mms.png";
	/*nombre de clusters/centres*/
    public static int K = 10;  
    /*Dimension*/
    public static int D = 3;		



    
	public static void main(String[] args) throws IOException {
		
		
						/*initialisation des fichiers et dossiers pour reusltats*/
		
		String cheminResImgMms = "resultatsMixGauss/Q1-Q2_Mms/Illustrations/";
		String cheminRes = "resultatsMixGauss/Q1-Q2_Mms/";
		File dossier = new File(cheminResImgMms);
		dossier.mkdirs();


		FileWriter fw1, fw2;
		fw1 = new FileWriter(cheminRes+"ScoreMms.d",true);
		fw2 = new FileWriter(cheminRes+"BestScores.d");



									/*initialisation*/
		System.out.println("# Initialisation des donnees et des Conditions initiales!");
		
        DonneesProject p = new DonneesProject(); 
        MixGauss m = new MixGauss();
        
        
        
        /*initialise les donnees avec l'image des m&ms*/
        double[][] tabColorNum = p.initialisationDonnees(path, name);
        Color[] tabColor = p.tabColor;
        
        /*Initialise les centres*/
        double[][] moy = p.initialisationCentres(tabColorNum, K);       
        /*Initialise les variances*/
        double[][] var = p.initialisationVar(0.3, K, D);
		/*Initialise la densite*/
		double[] gho = p.initialisationDensite(K);
		
				
		
									/*programme principal*/
		System.out.println("# Apprentissage et Mise a jour !");
		
		double[][] r = new double[tabColorNum.length][K];
		double d = 1;
		double eps = 0.001;
		
		while(d > eps){		//tant qu'il y'a un grand deplacement
			r = m.assigner(tabColorNum,gho,var,moy);
			d = m.deplct(r,tabColorNum,moy,var,gho);
		}



										/*Résultats*/
		System.out.println("# Resultats !"+ "\n"+ "-Q1:");
		
/**Q1**/		
		/*on note a chaque execution dans le fichier "scoreMms" les scores selon seed et K*/
		System.out.println(" -- Enregistrement dans 'ScoreMms.d' des scores en fonction de K et seed");
		
		
		/* dans le fichier : Colonne 0 : K. 		Colonne 1 : score. 		Colonne 2 : seed*/
		fw1.write(K+"\t"+m.scoreTotal(tabColorNum,moy,var,gho)+"\t"+DonneesProject.seed+"\n");
		fw1.close();
		
		
		
		/*on transforme le fichier correspondant aux scores en tableau*/
	    double[][] q = Tools.tableauFichier(cheminRes+"scoreMms.d",0,"\t");
	    
	
	     
		/*On enregistre les pixels correspondants a chaque gaussienne*/
	    System.out.println(" -- Enregistrement des pixels de chaque gaussienne.");
	    
	    
	    int n=0;
	    while(n<K){
	    	Color[] newTabColor = new Color[tabColor.length];	 
	        for(int i=0 ; i<newTabColor.length ; i++){       		
	        	if (Tools.argMax(r[i])!=n){// si n'appartient pas a la gaussienne actuelle c'est blanc
	        		
	        		newTabColor[i] = new Color(255,255,255);
	        	} // sinon c'est la couleur originale du pixel
	        	
	        	else newTabColor[i] = new Color(255-tabColor[i].getRed(), 255-tabColor[i].getGreen(), 255-tabColor[i].getBlue());
	        }
	        Tools.saveImagesGauss(newTabColor, p.width, p.height, cheminResImgMms, "img"+n);
	        n++;
			 
		}	
		
		

	    
	    
/**Q2**/
	    System.out.println("-Q2: \n --Enregistrement dans 'BestScores.d' du meilleur score de chaque K selon seed.");
	    
	    	    
	    /*on cherche le meilleur score pour chaque cluster a partir du fichier precedent*/
	    double[][] meilleursScores = Tools.maximums(q);
	    /*on ecrit le cluster et son meilleur score dans un nouveau  fichier*/
	    Tools.writeTable(meilleursScores,fw2);
	    fw2.close();
	        
  
/**Q.Bonus**/
	    
	    System.out.println("-Q.Bonus :\n --Enregistrement de l'image compressee avec k = "+K);
		
	    Color[] newTabColor = new Color[tabColor.length];		 
	    for(int i=0 ; i<newTabColor.length ; i++){
	    	newTabColor[i] = new Color(255-(int) (moy[Tools.argMax(r[i])][0]*255),255-(int) (moy[Tools.argMax(r[i])][1]*255),255-(int) (moy[Tools.argMax(r[i])][2]*255));
	    }
	    Tools.saveImagesGauss(newTabColor, p.width, p.height, cheminResImgMms, "c");	    
	    
		
		
	    
	}
	
		
}
