package client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import server.DataClient;


public class MainClient {

	public static DataClient client;
	public static List<Dessinateur> listeDessinateur = Collections.synchronizedList(new ArrayList<Dessinateur>());
	public static Fenetre fenetre;

	public static void main(String[] args) {
		System.out.print("Initialisation de l'interface graphique...");
		client = new DataClient();
		client.setConnecte(false);
		fenetre = new Fenetre();
		System.out.print(" ok !");
	}

}
