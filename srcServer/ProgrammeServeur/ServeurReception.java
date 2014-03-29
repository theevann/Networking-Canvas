package ProgrammeServeur;
import java.awt.Dimension;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import ProgrammeClient.Dessinateur;
import forme.*;


public class ServeurReception implements Runnable {
	
	private DataClient client;
	private Socket socketClient2;
	private boolean connect = true;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private ObjectInputStream in2 = null;
	
	public ServeurReception(Socket socketClient1) {
		client = new DataClient();
		client.setSocket1(socketClient1);
		in = client.getInTexte();
		out = client.getOutTexte();
	}

	public void run() {
		
		authentification();
		synchronisationClient();
		synchronisation();
		
		(new Thread(new ServeurEmission("a", new Dessinateur(client.getNom(), client.isConnecte())))).start();
		
		
		String message = null;
			
			try {
				in2 = client.getInMotif();
				
				while(connect){
				
	            message = in.readLine();
	            if(message == null)
	            	continue;
	            
	            System.out.println("\n----------\nReception: " + client.getNom() + " : (" + message + ")");
	            
	            if(message.substring(0,1).equals("c")){ // c pour chat
	            	System.out.println("Message de Chat : " + message.substring(1));
	            	(new Thread(new ServeurEmission("c("+ client.getNom() +") " + message.substring(1)))).start();
	            }
	            else if(message.substring(0,1).equals("u") && message.substring(1,2).equals("l")){ // ul pour undo list
	            	System.out.print("Suppression des motifs ");
	            	int [] num = (int[]) (in2.readObject());
	            	
	            	for(int i = 0 ; i < num.length ; i++)
	            		System.out.print("(" + num[i] + ")");
	            		
	            	for(int i = 0 ; i < num.length ; i++){
	            		if(MainServer.listeMotif.size() >= num[i]-i)
	            			MainServer.listeMotif.remove(num[i]-i);
	            	}
	            	System.out.println();
	            	(new Thread(new ServeurEmission("ul", num))).start();
	            }
	            else if(message.substring(0,1).equals("u")){// u pour undo
	            	System.out.print("Suppression du motif ");
	            	int num = Integer.parseInt(message.substring(1));
	            	System.out.println("(" + num + ")");
	            	if(MainServer.listeMotif.size() >= num){
		            	MainServer.listeMotif.remove(num);
		            	(new Thread(new ServeurEmission("u" + num))).start();
	            	}
	            	System.out.println();
	            }
	            else if(message.substring(0,1).equals("m")){ // m pour motif
	            	System.out.println("Motif");
	            	
	            	Motif motif = (forme.Motif) (in2.readObject());
	            	motif.setNumUser(client.getNumuser());
	            	MainServer.listeMotif.add(motif);
                    (new Thread(new ServeurEmission(motif))).start();
	            }
	            else if(message.substring(0,1).equals("b")){ // b pour bouger
	            	System.out.println("D\351placement de Motifs");
	            	
	            	int [] indexMotifs = (int[]) (in2.readObject());
	            	Dimension dim = (Dimension) (in2.readObject());
	            	
	            	for(int i : indexMotifs)
	            		MainServer.listeMotif.get(i).deplacer(dim);
	            	Object [] aEnvoyer = {indexMotifs,dim};
                    (new Thread(new ServeurEmission("b",aEnvoyer))).start();
	            }
	            
	            else if(message.substring(0,1).equals("k")){ // k parce que j'ai vraiment plus d'inspiration
	            	System.out.println("Modification de Motifs");
	            	List<Motif> listeMotifTemp = (new ArrayList<Motif>());
	            	
	            	int [] indexMotifs = (int[]) (in2.readObject());
	            	listeMotifTemp =  (List<Motif>) (in2.readObject());
	            	
	            	int j = 0;
	            	synchronized (MainServer.listeMotif) {
	            		for(int i : indexMotifs){
		            		MainServer.listeMotif.remove(i);
		            		listeMotifTemp.get(j).setNumUser(client.getNumuser());
		            		MainServer.listeMotif.add(i, listeMotifTemp.get(j));
		            		j++;
	            		}
	            	}
	            		
	            	Object [] aEnvoyer = {indexMotifs,listeMotifTemp};
                    (new Thread(new ServeurEmission("k",aEnvoyer))).start();
	            }
	            
	            else if(message.equals("Bye")){
	            	System.out.println("Deconnexion de " + client.getNom());
	            	connect = false;
	            	client.envoyer("Bye");
	            	client.deconnecter();
	            	(new Thread(new ServeurEmission("d", new Dessinateur(client.getNom(), client.isConnecte())))).start();
	            }
	            else if(message.equals("synchro")){
	            	synchronisation();
	            }
	            else if(message.equals("synchroClient")){
	            	synchronisationClient();
	            }

	          } // Fin de la boucle de reception
				
	       } catch (IOException e) {
	                 System.err.println("Client " + client.getNom() + " deconnect\351.");
	                 client.deconnecter();
		            (new Thread(new ServeurEmission("d", new Dessinateur(client.getNom(), client.isConnecte())))).start();
	                 //e.printStackTrace();
	                 connect = false;
	       } catch (ClassNotFoundException e) {
                System.err.println("Erreur de conversion de donn\351es");
				//e.printStackTrace();
	       }
      
		

	}
	
	private void synchronisationClient() {
		try {
			client.envoyer("synchroClient");
			client.getOutMotif().reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<Dessinateur> listeAEnvoyer = Collections.synchronizedList(new ArrayList<Dessinateur>());
		
		synchronized (MainServer.listeClient) {
			Iterator<DataClient> itc =  MainServer.listeClient.iterator();
			while(itc.hasNext()){
				DataClient clt = itc.next();
				Dessinateur dessinateur = new Dessinateur(clt.getNom(), clt.isConnecte());
				listeAEnvoyer.add(dessinateur);
			}
		} 
		
		client.envoyer(listeAEnvoyer);

	}

	private void synchronisation() {
		try {
			client.envoyer("synchro");
			client.getOutMotif().reset();
			client.envoyer(MainServer.listeMotif);
			client.envoyer(MainServer.dimensionCanevas);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void authentification() {
		
		try {
                 
			boolean ok = false;
			DataClient clt = null;
            String nom;
            int numDansListe = -1, i = 0;
			
            do{
            	out.println("nom");
                out.flush();
            	
            	nom = in.readLine();
            	ok = true;
            	i = 0;
            	
            	synchronized (MainServer.listeClient) {
	            	Iterator<DataClient> itc =  MainServer.listeClient.iterator();
	            	while(itc.hasNext() && ok){
	            		clt = itc.next();
	            		if(clt.getNom().equals(nom)){
	            			if(clt.isConnecte())
		            			ok = false;
		            		else if(!clt.isConnecte()){
		            		itc.remove();
		            		numDansListe = i;
		            		}
		            	}
	            		i++;
	            	}
	            	if(nom.equals("null") || nom.length() <= 0)
	            		ok = false;
	            		
            	}
            	
            } while(!ok);
            
            out.println("ok");
            out.flush();
            
            client.setNom(nom);
            
            
            socketClient2 = MainServer.socketServeur2.accept(); // Acceptation du socket 2
            client.setSocket2(socketClient2);
            
            if(numDansListe < 0){
            	client.setNumuser(MainServer.listeClient.size());
            	MainServer.listeClient.add(client);
            }
            else if(numDansListe >= 0){
            	client.setNumuser(numDansListe);
            	MainServer.listeClient.add(numDansListe, client);
            }
            
            client.setConnecte(true);
            System.out.println("Nouveau client authentifié : " + nom + "\n");  
            
        } catch (IOException e) {
            System.err.println("Le client ne r\351pond pas : " + e.getMessage());
        }
		
	}


}
