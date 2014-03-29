package ProgrammeServeur;
import java.awt.Dimension;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import forme.Motif;


public class MainServer {

	static ServerSocket socketServeur1;
	static ServerSocket socketServeur2;
	static List<Motif> listeMotif = Collections.synchronizedList(new ArrayList<Motif>());
	static List<DataClient> listeClient = Collections.synchronizedList(new ArrayList<DataClient>());
	static Dimension dimensionCanevas = new Dimension(700, 500);
	
	public static void main(String[] args) {
		try {
            socketServeur1 = new ServerSocket(2013);
            socketServeur2 = new ServerSocket(2014);
            
            InetAddress thisIp = InetAddress.getLocalHost(); 
            
            System.out.println("Adresse du serveur : " + thisIp.getHostAddress());
            System.out.println("Serveur Texte  lanc\351\nA l'\351coute du port " + socketServeur1.getLocalPort());
            System.out.println("Serveur Dessin lanc\351\nA l'\351coute du port " + socketServeur2.getLocalPort()); 
            
            while(true){ 
            Socket socketClient = socketServeur1.accept();
            System.out.println("\nConnexion d'un nouveau participant : " + socketClient.getLocalAddress()); 
            (new Thread(new ServeurReception(socketClient))).start();
            }
            
             
        } catch (IOException e) {
            System.err.println("Un des ports de connexion est d\351jà utilis\351 !");
        }
		
	}

}
