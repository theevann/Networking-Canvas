package ProgrammeClient;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import ProgrammeServeur.DataClient;
import forme.*;


public class ServeurReceptionClient implements Runnable {
	
	private DataClient client;
	private boolean connect = true;
	private BufferedReader in = null;
	private ObjectInputStream in2 = null;
	
	public ServeurReceptionClient(DataClient client){
		super();
		this.client = client;
	}

	public void run() {
		String message = null;
		System.out.println("R\351ception lanc\351e");
			
			try {
				
				in = MainClient.client.getInTexte();
				in2 = MainClient.client.getInMotif();
				
				while(connect){
					
		            message = in.readLine();
		            if(message == null)
		            	continue;
		            System.out.println("\n----------\nReception (serveur) : " + message);
		            
		            if(message.substring(0,1).equals("c")){
		            	System.out.println("Message de Chat: " + message.substring(1));
		            	MainClient.fenetre.ajouterMessageChat(message.substring(1));
		            }
		            else if(message.substring(0,1).equals("u") && message.substring(1,2).equals("l")){ 
		            	System.out.print("Suppression des motifs ");
		            	int [] num = (int[]) (in2.readObject());
		            	for(int i = 0 ; i < num.length ; i++)
		            		System.out.print("(" + num[i] + ")");
		            	System.out.println();
		            	ImageDessin.undo(num);
		            	OngletHistorique.ongletActuel.synchroniser(num);
		            }
		            else if(message.substring(0,1).equals("u")){ // u pour undo
		            	System.out.print("Suppression du motif ");
		            	int num = Integer.parseInt(message.substring(1));
		            	System.out.println("(" + num + ")");
		            	ImageDessin.undo(num);
		            	int [] num2 = new int[1];
		            	num2[0] = num;
		            	OngletHistorique.ongletActuel.synchroniser(num2);
		            }
		            else if(message.substring(0,1).equals("m")){
		            	System.out.println("Motif");
		            	Motif motifRecupere = (forme.Motif) (in2.readObject());
		            	OngletHistorique.ongletActuel.ajouterMotif(motifRecupere);
		            	ImageDessin.miseAJourImage(motifRecupere);
		            }
		            else if(message.substring(0,1).equals("b")){ // b pour bouger
		            	System.out.println("D\351placement de Motifs");
		            	
		            	int [] indexMotifs = (int[]) (in2.readObject());
		            	Dimension dim = (Dimension) (in2.readObject());
	
		            	ImageDessin.deplacer(dim, indexMotifs);
		            }
		            else if(message.substring(0,1).equals("k")){ // k parce que j'ai vraiment plus d'inspiration
		            	System.out.println("Modification de Motifs");
		            	List<Motif> listeMotifTemp = (new ArrayList<Motif>());
		            	
		            	int [] indexMotifs = (int[]) (in2.readObject());
		            	listeMotifTemp =  (List<Motif>) (in2.readObject());
		            	
		            	ImageDessin.modifierMotifs(indexMotifs,listeMotifTemp);
		            }
		            else if(message.equals("synchro")){
		            	System.out.println("Synchronisation...");
		            	synchronisation();
		            }
		            else if(message.equals("synchroClient")){
		            	System.out.println("Synchronisation des clients...");
		            	synchronisationClient();
		            }
		            else if(message.substring(0,1).equals("a")){
		            	Dessinateur dessinateur = (Dessinateur) (in2.readObject());
		            	boolean existe = false;
		            	
		            	synchronized (MainClient.listeDessinateur) {
			            	Iterator<Dessinateur> itc =  MainClient.listeDessinateur.iterator();
			            	while(itc.hasNext() && !existe){
			            		Dessinateur dessinateur2 = itc.next();
			            		if(dessinateur2.getNom().equals(dessinateur.getNom())){
			            			dessinateur.setConnecte(true);
			            			OngletDessinateur.ongletActuel.connecterDessinateur(dessinateur);
			            			existe = true;
			            		}
			            	}
			            	if(!existe){
			            		MainClient.listeDessinateur.add(dessinateur);
			            		OngletDessinateur.ongletActuel.ajouterDessinateur(dessinateur);
			            	}
		            	}
		            	
		            	MainClient.fenetre.ajouterMessageChat(" - " + dessinateur.getNom() + " s'est connect\351 - ");
	            		System.out.println(dessinateur.getNom() + " s'est connect\351");
		            }
		            else if(message.substring(0,1).equals("d")){
		            	Dessinateur dessinateur = (Dessinateur) (in2.readObject());
		            	
		            	synchronized (MainClient.listeDessinateur) {
			            	Iterator<Dessinateur> itc =  MainClient.listeDessinateur.iterator();
			            	while(itc.hasNext()){
			            		Dessinateur dessinateur2 = itc.next();
			            		if(dessinateur2.getNom().equals(dessinateur.getNom())){
			            			dessinateur.setConnecte(false);
			            			MainClient.fenetre.ajouterMessageChat(" - " + dessinateur.getNom() + " s'est deconnect\351 - ");
			            			OngletDessinateur.ongletActuel.deconnecterDessinateur(dessinateur);
			            		}
			            	}
		            	} 
		            	
	            		System.out.println(dessinateur.getNom() + " est d\351connect\351");
		            }
		            else if(message.equals("Bye")){
		            	System.out.println("Deconnexion...");
		            	client.deconnecter();
		            	MainClient.fenetre.modeDeconnecte();
		            	connect = false;
		            }
		            
		            MainClient.fenetre.getCanevas().repaint();
		            
	          } // Fin de la boucle de reception
				
	       } catch (IOException e) {
	                 System.err.println("Serveur deconnect\351.");
	                 connect = false;
	                 client.deconnecter();
	                 MainClient.fenetre.modeDeconnecte();
	                 JOptionPane jop = new JOptionPane();
	                 jop.showMessageDialog(MainClient.fenetre, "Vous avez \351t\351 d\351connect\351 du serveur", "Le serveur ne r\351pond plus", JOptionPane.WARNING_MESSAGE);
	       } catch (ClassNotFoundException e) {
                System.err.println("Erreur de conversion de donn\351es");
				e.printStackTrace();
				connect = false;
	       }
      
		

	}

	private void synchronisation() throws ClassNotFoundException, IOException {
		List<Motif> listeMotifTemp = (new ArrayList<Motif>());
		Dimension dimensionCanevasTemp = new Dimension();
		listeMotifTemp =  (List<Motif>) (in2.readObject());
		synchronized (ImageDessin.listeMotif) {
			ImageDessin.listeMotif = listeMotifTemp;
		}
		
		dimensionCanevasTemp = (Dimension) (in2.readObject());
		synchronized (Canevas.getDimensionCanevas()) {
			Canevas.setDimensionCanevas(dimensionCanevasTemp);
		}
		
		System.out.println("Synchronisation Termin\351e !");
		OngletHistorique.ongletActuel.synchroniser();
		ImageDessin.creerImage();
	}
	
	private void synchronisationClient() throws ClassNotFoundException, IOException {
		List<Dessinateur> listeDessinateurs = Collections.synchronizedList(new ArrayList<Dessinateur>());
		listeDessinateurs =(List<Dessinateur>) (in2.readObject());
		MainClient.listeDessinateur = listeDessinateurs;
		for(int i = 0 ; i < listeDessinateurs.size() ; i++){
			if(listeDessinateurs.get(i).getNom().equals(MainClient.client.getNom()))
				MainClient.client.setNumuser(i);
		}
		OngletDessinateur.ongletActuel.synchroniser();
		System.out.println(listeDessinateurs);
		System.out.println("Synchronisation des Clients Termin\351e !");
	}

}
