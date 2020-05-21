package project;

public class MixGauss {
	/**
	 * Calcule denominateur/score pour une donnee
	 * 
	 * 
	 * @param x tableau de doubles  (la donnee)
	 * @param gho tableau 2D de doubles
	 * @param var tableau 2D de doubles 
	 * @param moy tableau 2D de doubles
	 */
	public static double calculDen(double[] x, double gho[], double[][] var, double[][] moy){
		double den=0;
		double n=1;
		for(int k=0; k<var.length; k++){
			n=1;
			for (int i=0; i<x.length; i++){
				n*=(1/Math.sqrt(2*Math.PI*var[k][i]))*Math.exp(-Math.pow((x[i]-moy[k][i]),2)/(2*var[k][i]));
			}
			den+=gho[k]*n;	
		}
		return den;
	}
	/**
	 * Calcule la densité de chaque gaussienne
	 * 
	 * 
	 * @param r tableau 2D de doubles
	 */
	public static double[] calculRho(double[][] r){
		double[] gho = new double[r[0].length];
		for(int i=0; i<gho.length; i++){
			gho[i] = Tools.sommeColonne(r,i)/r.length;
		}
		return gho;
	}


	/**
	 * renvoie un tableau ou la case d'indice (i,j) correspond a la probabilité que la 
	 donnée i appartienne à la gaussienne j
	 
	 
	 * @param x tableau 2D de doubles  (les donnees)
	 * @param rho tableau 2D de doubles
	 * @param var tableau 2D de doubles 
	 * @param moy tableau 2D de doubles
	 
	 */
	public double[][] assigner(double[][] x, double[] rho, double[][] var, double[][] moy){
		
		double num=1;
		double den=0;

		double[][] t = new double [x.length][var.length];
		for (int d=0; d<x.length; d++){	
			den=calculDen(x[d],rho,var,moy);

			for(int k=0; k< var.length; k++){
				num=1;
				for(int i=0; i<x[0].length; i++){
					num*=(1/Math.sqrt(2*Math.PI*var[k][i]))*Math.exp(-Math.pow((x[d][i]-moy[k][i]),2)/(2*var[k][i]));
				}			
				num*=rho[k];     
				t[d][k]=num/den;
			}							
		}
		return t;
	}
	
	
	/**
	 * renvoie la MAJ de la moyenne de chaque gaussienne (les centres)
	 * 
	 * 
	 * @param r tableau 2D de doubles
	 * @param x tableau 2D de doubles   (les donnees)
	 * @param moy tableau 2D de doubles
	 */
	public static double[][] majMoyenne(double[][] r, double[][] x, double[][] moy){
		double R=0;
		double somme=0;
		double[][] moyenne = new double[moy.length][moy[0].length];
		for(int k=0; k<moy.length; k++){
			R = Tools.sommeColonne(r,k);
			for (int i=0; i<moy[0].length; i++){
				somme=0;
				for(int d=0; d<r.length; d++){
					somme+=r[d][k]*x[d][i];
				}
				moyenne[k][i]=somme/R;
			}
		}
		return moyenne;
	}
	/**
	 * renvoie la MAJ de  la variance de chaque gaussienne
	 * 
	 * 
	 * @param r tableau 2D de doubles	
	 * @param x tableau 2D de doubles  (les donnees)
	 * @param moy tableau 2D de doubles
	 * @param var tableau 2D de doubles
	 */
	public static double[][] majVariance(double[][] r, double[][] x, double[][] var, double[][] moy){
		double R=0;
		double somme=0;
		double[][] variance = new double[var.length][var[0].length];
		for(int k=0; k<var.length; k++){
			R = Tools.sommeColonne(r,k);
			for (int i=0; i<var[0].length; i++){
				somme=0;		
				for(int d=0; d<r.length; d++){	
					somme+=r[d][k]*Math.pow((x[d][i]-moy[k][i]),2);
				}
				variance[k][i]=somme/R;
			}
		}
		return variance;
	}

	
	/**
	 * met à jour les gaussiennes et retourne la distance cumulée 
	 * 
	 * 
	 * @param r tableau 2D de doubles
	 * @param x tableau 2D de doubles  (les donnees)
	 * @param moy tableau 2D de doubles
	 * @param var tableau 2D de doubles
	 * @param rho tableau  de doubles
	 */
	public double deplct(double[][]r, double[][]x,  double[][] moy, double[][] var, double[] rho){	
		double distanceCumulee=0;
		double[][] moyenne = majMoyenne(r,x,moy);
		double[][] variance = majVariance(r,x,var,moy);
		double[] rhoNew = calculRho(r);
		for (int k=0; k<moy.length; k++){
			distanceCumulee+= Tools.distance(moyenne[k], moy[k]);		
		}
		
		Tools.copieTableau2D(moyenne, moy);
		Tools.copieTableau2D(variance, var);
		Tools.copieTableau(rhoNew, rho);
		
		return distanceCumulee;
	}

	/**
	 * calcule le score d'une donnee
	 * 
	 * 
	 * @param x tableau de doubles  (la donnee)
	 * @param moy tableau 2D de doubles
	 * @param var tableau 2D de doubles
	 * @param ro tableau  de doubles
	 */
	public static double scoreUneDonnee(double[] x,  double[][] moy, double[][] var, double[] ro){
		double score = calculDen(x,ro,var,moy);
		return Math.log(score);	
	}

	
	/**
	 * Calcule le score pour un jeu de donnees
	 * 
	 * 
	 * @param x tableau 2D de doubles (les donnees)
	 * @param moy tableau 2D de doubles
	 * @param var tableau 2D de doubles
	 * @param ro tableau  de doubles
	 */
	public  double scoreTotal(double[][] x,  double[][] moy, double[][] var, double[] ro){
		double scoreTotal = 0;
		for(int i=0; i<x.length; i++){
			scoreTotal+= scoreUneDonnee(x[i], moy, var, ro);
		}		
		return scoreTotal/x.length;	
	}

}
