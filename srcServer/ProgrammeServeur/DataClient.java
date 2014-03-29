package ProgrammeServeur;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;


public class DataClient implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Socket socket1, socket2;
	private String nom = null;
	private BufferedReader inTexte = null;
	private ObjectInputStream inMotif = null;
	private PrintWriter outTexte = null;
	private ObjectOutputStream outMotif = null;
	private boolean connecte = true;
	private int numUser;

		public DataClient() {
		}

		public String getNom() {
			return nom;
		}
	
		public void setNom(String nom) {
			this.nom = nom;
		}

		public BufferedReader getInTexte() {
			return inTexte;
		}
		
		public ObjectInputStream getInMotif() {
			return inMotif;
		}
		
		public PrintWriter getOutTexte() {
			return outTexte;
		}

		public ObjectOutputStream getOutMotif() {
			return outMotif;
		}
		
		public boolean isConnecte() {
			return connecte;
		}

		public void setConnecte(boolean connecte) {
			this.connecte = connecte;
		}
		
		public void closeSocket1() {
			try {
				socket1.close();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		
		public void closeSocket2() {
			try {
				socket2.close();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}

		public void setSocket1(Socket socket1) {
			this.socket1 = socket1;
			if(outTexte == null){
				try {
					outTexte = new PrintWriter(this.socket1.getOutputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(inTexte == null){
				try {
					inTexte = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			
		}

		public void setSocket2(Socket socket2) {
			this.socket2 = socket2;
			
			if(outMotif == null){
				try {
					outMotif = new ObjectOutputStream(socket2.getOutputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(inMotif == null){
				try {
					inMotif = new ObjectInputStream(socket2.getInputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public void envoyerMotif(Object motif){
			envoyer("m", motif);
		}
		
		public void envoyer(String chaine){
			envoyer(chaine, null);
		}
		
		public void envoyer(Object obj) {
			envoyer(null, obj);
		}
		
		public synchronized void envoyer(String chaine, Object obj) {
			synchronized(outMotif){
				synchronized(outTexte){
					if(chaine != null){
						outTexte.print(chaine + "\r\n");
						outTexte.flush();
					}
					if(obj != null){
						try{
							outMotif.writeObject(obj);
							outMotif.flush();
						} catch(IOException e){
							 System.err.println("Pas de r\351ponse du serveur distant (" + nom + ") : " + e.getMessage());
							 deconnecter();
						}
					}
				}
			}
		}
		
		public synchronized void envoyer(Object [] obj) {
			envoyer(null, obj);
		}
		
		public synchronized void envoyer(String chaine, Object [] obj) {
			synchronized(outMotif){
				synchronized(outTexte){
					if(chaine != null){
						outTexte.print(chaine + "\r\n");
						outTexte.flush();
					}
					if(obj != null){
						try{
							for(int i = 0 ; i < obj.length ; i++){
								outMotif.writeObject(obj[i]);
								outMotif.flush();
							}
						} catch(IOException e){
							 System.err.println("Pas de r\351ponse du serveur distant (" + nom + ") : " + e.getMessage());
							 deconnecter();
						}
					}
				}
			}
		}
		
		
		public Socket getSocket2() {
			return socket2;
		}
		
		public Socket getSocket1() {
			return socket1;
		}
		
		public String toString(){
			return nom;
		}
		
		public void deconnecter() {
			try {
				inTexte.close();
				inMotif.close();
				outMotif.close();
				outTexte.close();
				socket1.close();
				socket2.close();
			}catch (IOException e) {
				//e.printStackTrace();
			}
			
			inMotif = null;
			inTexte = null;
			outMotif = null;
			outTexte = null;	
			connecte = false;
			
		}

		public int getNumuser() {
			return numUser;
		}
		
		public void setNumuser(int num) {
			numUser = num;
		}
		
}


