package project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Data {
	
	/*Chemin vers fichier gmm_data */
	public static String path = "";   // !!!!! à INIDIQUER!!!!!
	/*Nom du fichier */
	public static String name = "gmm_data.d";
	/*nombre de clusters/centres*/
    public static int K = 4;  
    /*Dimension*/
    public static int D = 2;

    
    
    
	public static void main(String[] args) throws IOException {
		
						/*initialisation des fichiers et dossiers pour reusltats*/
		
		String cheminRes = "resultatsMixGauss/Q4_Data/";
		File dossier = new File(cheminRes);
		dossier.mkdirs();
		
		FileWriter fw3 = new FileWriter(cheminRes+"ScoreData.d",true);
		FileWriter fw4 = new FileWriter(cheminRes+"BestScoresData.d");
		
        DonneesProject p = new DonneesProject(); 
        MixGauss m = new MixGauss();
        
        
        
										/*initialisation*/
	    System.out.println("# Initialisation des CI et donnees avec Data fourni:");
	    							   
	    
	    /*on charge le fichier "gmm_data.d" dans un tableau*/
	    double[][] data = Tools.tableauFichier(path+name,0," ");
        /*Initialise les centres*/
        double[][] moy = p.initialisationCentres(data, K);       
        /*Initialise les variances*/
        double[][] var = p.initialisationVar(1, K, D);
		/*Initialise la densite*/
		double[] gho = p.initialisationDensite(K);
		
		double[][] r = new double[data.length][K];		
		
		
									/*programme principal*/
		
		System.out.println("# Apprentissage et Mise a jour !");
		
		/*Deplacement des gaussienne*/
		double d = 1;
		double eps = 0.001;
		
		while(d > eps){
			r = m.assigner(data,gho,var,moy);
			d = m.deplct(r,data,moy,var,gho);
		}
		
		
										/*résultats*/
				
		System.out.println("# Enregistrement des resultats !");
		
		
		
		/*on ecrit le score dans un fichier*/
		System.out.println(" -- Enregistrement dans 'ScoreData.d' des scores en fonction de K et seed !");
	
		fw3.write(K+"\t"+m.scoreTotal(data,moy,var,gho)+"\t"+DonneesProject.seed+"\n");
		fw3.close();
	    
		/*on transforme le fichier correspondant aux scores en tableau*/
	    double[][] q = Tools.tableauFichier(cheminRes+"scoreData.d",0,"\t");
	    
	    
	    
	    
	    /*on recupere depuis le fichier precdent le meilleur score pour chaque cluster et on ecrit dans un nouveau fichier*/
	    System.out.println(" -- Enregistrement dans 'BestScoresData.d' du meilleur score pour chaque K selon seed ! ");

	    double[][] meilleursScores = Tools.maximums(q);
	    Tools.writeTable(meilleursScores,fw4);
	    fw4.close();
	    

		    
	    

	}

}
