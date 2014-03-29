package ProgrammeServeur;

import forme.Motif;


public class ServeurEmission implements Runnable{

	private Object obj = null;
	private Object [] objs = null;
	private String chaine = null;
	
	public ServeurEmission(Motif motif) {
		this.chaine = "m";
		this.obj = motif;
	}
	
	public ServeurEmission(Object o) {
		this.chaine = null;
		this.obj = o;
	}
	
	public ServeurEmission(String chaine, Object [] obj) {
		this.chaine = chaine;
		this.objs = obj;
	}
	
	public ServeurEmission(String chaine, Object obj) {
		this.chaine = chaine;
		this.obj = obj;
	}
	
	public ServeurEmission(String chaine) {
		this.chaine = chaine;
	}

	@Override
	public void run() {
		String clients = "";
		
		if(chaine != null || obj != null || objs != null){
			if(objs == null && obj != null){
				objs = new Object [1];
				objs[0] = obj;
			}
			for(DataClient client : MainServer.listeClient){
				if(client.isConnecte()){
					clients += client.getNom() + ", ";
					client.envoyer(chaine,objs);
				}
			}
			if(!clients.equals(""))
			System.out.println("Envoi des donn\351es à : " + clients);
		}
		else
			System.err.println("Erreur d'envoi : rien à envoyer");
		
	}
}