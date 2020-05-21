package project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Points1D {
	
	/*nombre de clusters/centres*/
    public static int K = 2;  
    /*Dimension*/
    public static int D = 1;
    /*nombres alea*/   
	public static int seed = 1234;
    public static Random GenRdm = new Random(seed);

    
    
	public static void main(String[] args) throws IOException {
						
						/*initialisation des fichiers et dossiers pour reusltats*/
		
		
		String cheminRes = "resultatsMixGauss/Q5_Points1D/";
		File dossier = new File(cheminRes);
		dossier.mkdirs();
		
		FileWriter fw5 = new FileWriter(cheminRes+"Histo.d");
		FileWriter fw6 = new FileWriter(cheminRes+"Gaussiennes1D.d");
		
		
        DonneesProject p = new DonneesProject(); 
        MixGauss m = new MixGauss();
        
	    								/*initialise donnees et CI*/
        System.out.println("# Initialisation des CI et donnees avec Data fourni:");
	    
        
        
	    /*initialise 1000 pts à 1 dim*/
	    double[][] X = new double[1000][1];
	    for(int i=0; i<X.length; i++){
	    	if(i%2==0) X[i][0] = Math.sqrt(0.2)*GenRdm.nextGaussian()-2;
	    	else X[i][0] = Math.sqrt(1.5)*GenRdm.nextGaussian()+3;
	    }

        /*Initialise 2 centres*/
        double[][] moy = p.initialisationCentres(X, K);       
        /*Initialise les variances à 1 */
        double[][] var = p.initialisationVar(1, K, D);
		/*Initialise la densite*/
		double[] gho = p.initialisationDensite(K);
		
		double[][] r = new double[X.length][2];	
		
		
										/*Mise à jour*/
		
		/*Deplacement des gaussienne*/
		System.out.println("# Apprentissage et Mise a jour !");
		
		
		double d = 1;
		double eps = 0.001;
		while(d > eps){
			r = m.assigner(X, gho, var,moy);
			d = m.deplct(r, X, moy,var, gho);
		}

		/* on transforme le vecteur en 2d en 1d*/
		double[] XColonne = new double [X.length];
		for(int i=0; i<XColonne.length; i++) XColonne[i] = X[i][0];
		
		
											/*résultats*/
		
		/*histogramme*/
		double[][] histo = Tools.histogramme(Tools.min(XColonne), Tools.max(XColonne), 13, XColonne);
		/*On normalise*/
		for(int i=0; i<histo.length ; i++) histo[i][1]=histo[i][1]/X.length;
		
		System.out.println(" -- Enregistre dans 'Histo.d' l'histogramme des donnees !");
		Tools.writeTable(histo, fw5);
		fw5.close();
		
		
		
		/*Enregistre dans 'Gaussiennes1D.d' les valeurs de chzque gaussienne*/
		System.out.println(" -- Enregistre dans 'Gaussiennes1D.d' les valeurs de chaque gaussienne !");

		for(int i=0; i<K; i++){
			fw6.write("gaussienne numero "+i +" : \n");
			fw6.write("Moy: "+moy[i][0]+". Var: "+var[i][0]+". Gho: "+ gho[i]+"\n");
		}
		fw6.close();
	}

}
