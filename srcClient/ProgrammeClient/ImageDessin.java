package ProgrammeClient;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import forme.*;


public class ImageDessin {
	static int width =  MainClient.fenetre.getCanevas().getWidth(), height = MainClient.fenetre.getCanevas().getHeight();
	static int freq = 10;// freq = la periode à laquelle on créé une nouvelle image 
	static ImageDessin imageActuelle;
	static List<ImageDessin> listeImage;
	static List<Motif> listeMotif = Collections.synchronizedList(new ArrayList<Motif>());
	private static int nb = 0;
	BufferedImage img;
	int id = -1; // id = numero  du dernier motif dessiné dans la liste des motifs
	
	
	static {
		listeImage = new ArrayList<ImageDessin>();
		BufferedImage imgInit = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		Graphics g = imgInit.createGraphics();
	    g.setColor(Color.white);
	    g.fillRect(0, 0, width, height);
	    imageActuelle = new ImageDessin(imgInit, 0);
		listeImage.add(0,new ImageDessin(imgInit, 0));
	}
	
	public ImageDessin(BufferedImage image){
		this.img = image;
	}
	
	public ImageDessin(BufferedImage image, int id){
		this.img = image;
		this.id = id;
	}
	
	public static void setDimension(Dimension dimension){
		width = dimension.width;
		height = dimension.height;
		BufferedImage imgInit = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		Graphics g = imgInit.createGraphics();
	    g.setColor(Color.white);
	    g.fillRect(0, 0, width, height);
	    listeImage.remove(0);
		listeImage.add(0,new ImageDessin(imgInit, 0));
	}
	
	public static void miseAJourImage(Motif motif){
		synchronized (listeMotif) {
			listeMotif.add(motif);
		}
		
		Dessinateur dessinateur = MainClient.listeDessinateur.get(motif.getNumUser());
		
		System.out.println(" - Reception d'un motif : " + motif.getType());
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.createGraphics();
		g.drawImage(imageActuelle.img, 0, 0, null);
		if(dessinateur.getVoir())
			motif.dessinerSur(g);
		
		imageActuelle = new ImageDessin(image, listeMotif.size()-1);
		if(listeMotif.size()%freq == 0){
			listeImage.add((int)((listeMotif.size())/freq), new ImageDessin(image, listeMotif.size()-1));
		}
		
		nb++;
	}
	
	//Cette classe permet de créer l'image actuelle et les images temporaires à partir d'un certain rang de la liste des motifs
	 public static BufferedImage creerImageDepuis(int idMotif){ // idMotif = num du motif dans la liste
		 int i = 0, imgAUtilise; // i correspond au numero du motif dans la liste
		 BufferedImage image;
		 
		 imgAUtilise = (idMotif+1) - ((idMotif+1)%freq); //Ici, on choisit le rang du motif à partir duquel on va redessiner pour utiliser une image déjà créée.
		 
		 //System.out.println("Image utilisée : " + imgAUtilise/freq + " (" + imgAUtilise + ")");
		 image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		 Graphics g = image.createGraphics();
		 g.drawImage(listeImage.get(imgAUtilise/freq).img, 0, 0, null);
		 
		 synchronized (listeMotif) {
		     Iterator<Motif> iter = listeMotif.iterator();
		     
		     while(iter.hasNext() && i < imgAUtilise){
		    	 iter.next();
		    	 i++;
		     }
		     
		     
		     while(iter.hasNext()){
		    	 Motif motif = iter.next();
		    	 
		    	 Dessinateur dessinateur = MainClient.listeDessinateur.get(motif.getNumUser());
		    	 
		    	 if(dessinateur.getVoir() && !motif.isMasque())
		    		 motif.dessinerSur(g);
		    	 
		    	 if((i+1)%freq == 0){ // Si on est multiple de la période, on enregistre l'image
		    		 BufferedImage imageTemp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
					 Graphics g2 = imageTemp.createGraphics();
					 g2.drawImage(image, 0, 0, null);
		    		 listeImage.add((int)((i+1)/freq), new ImageDessin(imageTemp, i));
		    	 }
		    	 
		    	 i++;
		     } 
		 }
		 
	     imageActuelle = new ImageDessin(image, i-1);
	     return image;
	 }
	 
	 public static BufferedImage creerImage(){
		return creerImageDepuis(0);
	 }
	 
	 //undo est une la methode qui supprime un ensemble de motif (dont les numeros sont les elements du parametre idASupprimer)
	 public static void undo(int [] idASupprimer){
		synchronized (listeMotif) {
			 for(int i = 0 ; i < idASupprimer.length ; i++){
	     		listeMotif.remove(idASupprimer[i]-i); // Attention, petite astuce
			 }
		}
		 //System.out.println("Nb liste : " + listeMotif.size() +  "\n" + listeMotif);
		 creerImageDepuis(idASupprimer[0] - 1); // On demande à recreer l'image actuelle (et par la meme occasion les images intermédiaires) à partir du rang du motif précédant le 1er motif supprimé
		 nb++;
	 }
	 
	 public static void deplacer(Dimension dim, int [] indexMotifs){
		 Arrays.sort(indexMotifs);
		 for(int i : indexMotifs)
	 		listeMotif.get(i).deplacer(dim);
		 creerImageDepuis(indexMotifs[0]-1);
	 }
	 
	 
	 public static void undo(int idASupprimer){
		 synchronized (listeMotif) {
			 listeMotif.remove(idASupprimer);
		}
		// System.out.println("Nb liste : " + listeMotif.size() +  "\n" + listeMotif);
		 creerImageDepuis(idASupprimer - 1);
		 nb++;
	 }
	 
	 public static void masquerMotif(int [] idAMasquer){
		 Arrays.sort(idAMasquer);
		 
		 synchronized (listeMotif) {
			 for(int i = 0 ; i < idAMasquer.length ; i++){
	     		listeMotif.get(idAMasquer[i]).setMasque(true);
			 }
		}
		 
		 //System.out.println("Nb liste : " + listeMotif.size() +  "\n" + listeMotif);
		 creerImageDepuis(idAMasquer[0] - 1);
	 }
	 
	 public static void afficherMotif(int [] idAAfficher){
		 Arrays.sort(idAAfficher);
		 
		 synchronized (listeMotif) {
			 for(int i = 0 ; i < idAAfficher.length ; i++){
	     		listeMotif.get(idAAfficher[i]).setMasque(false);
			 }
		}
		 
		// System.out.println("Nb liste : " + listeMotif.size() +  "\n" + listeMotif);
		 creerImageDepuis(idAAfficher[0] - 1);
	 }
	 
	 public static void enregister (BufferedImage image){
		 enregister(image, nb + "");
	 }
	 
	 public static void enregister (BufferedImage image, String nomImg){
		 	Iterator writers = ImageIO.getImageWritersByFormatName("png");
	        ImageWriter writer = (ImageWriter) writers.next();
	        if (writer == null) {
	            throw new RuntimeException("PNG not supported?!");
	        }

	        ImageOutputStream out;
			try {
				out = ImageIO.createImageOutputStream(new File("Image " + nomImg  + ".png"));
		        writer.setOutput(out);
				writer.write(image);
				writer.dispose();
				out.close();
				nb++;
			} catch (IOException e) {
				e.printStackTrace();
			}
	 }

	public static void enregisterImageActuelle() {
		enregister(imageActuelle.img);
	}

	public static void modifierMotifs(int[] indexMotifs, List<Motif> listeMotifTemp) {
		int j = 0;
		 synchronized (listeMotif) {
			for(int i : indexMotifs){
	    		listeMotif.remove(i);
	    		listeMotif.add(i, listeMotifTemp.get(j));
	    		OngletHistorique.ongletActuel.modifier(i, listeMotifTemp.get(j));
	    		j++;
			}
		 }
		 creerImageDepuis(indexMotifs[0] - 1);
	}
	 
}
