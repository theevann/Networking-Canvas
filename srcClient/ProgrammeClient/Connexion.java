package ProgrammeClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;


public class Connexion implements Runnable {
	private String adresse = "127.0.0.1";
	private String nom = null;
	public static Socket socket1 = null, socket2 = null; //1 pour le texte, 2 pour les motifs
	
	public Connexion(String adresse, String nom){
		this.adresse = adresse;
		this.nom = nom;
		Connexion.socket1 = null;
		Connexion.socket2 = null;
	}

	public Connexion() {
		Connexion.socket1 = null;
		Connexion.socket2 = null;
	}
	
	public void run(){
		lancerConnexion();
	}

	public void lancerConnexion() {
		String lu;
		boolean ok = false;
		
		try {
	         
	        System.out.println("\n\nDemande de connexion : (" + adresse +") ...");
	        socket1 = new Socket(adresse,2013);
	        MainClient.client.setSocket1(socket1);
	        System.out.println("Connexion \351tablie avec le serveur");
	        
	      	PrintWriter out = MainClient.client.getOutTexte();
	        BufferedReader in = MainClient.client.getInTexte();
	        
	        //Envoi du nom
	        lu = in.readLine();
	        System.out.print("\nAuthentification...");
	        while(!ok){
		        
		        if(lu.equals("nom")){
		        }
		        else{
		        	System.err.println("\nLe serveur n'a pas envoy\351 la requ\352te attendue : " + lu);
		        	System.exit(0);
		        }
		        
		        out.println(nom);
		        out.flush();
		        
		        lu = in.readLine();
		        
		        if(lu.equals("nom")){
		        	//System.out.println("\nNom d\351j\340 utilis\351");
		        	JOptionPane jop = new JOptionPane();
		        	nom = jop.showInputDialog(null, "Nom :", "Nom d\351j\340 utilis\351 !", JOptionPane.QUESTION_MESSAGE);
		        }
		        else if(lu.equals("ok"))
			        ok = true;
	        }
	        System.out.println(" ok !");
	        MainClient.client.setNom(nom);
	        
	        //Creation du socket 2 (envoi de motif)
	        socket2 = new Socket(adresse,2014);
	        MainClient.client.setSocket2(socket2);
	        MainClient.client.setConnecte(true);
	        (new Thread(new ServeurReceptionClient(MainClient.client))).start();
	        MainClient.fenetre.modeConnecte();
	        
	    } catch (UnknownHostException e) {
	      System.err.println("Impossible de se connecter \340 l'adresse " + adresse);
	    } catch (IOException e) {
	      System.err.println("Aucun serveur à l'\351coute du port 2013 \340 l'adresse " + adresse);
	      
	      e.getStackTrace();
	    }
	}

}
