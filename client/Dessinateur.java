package client;
import java.io.Serializable;


public class Dessinateur implements Serializable{

	private String nom;
	private boolean connecte;
	private Boolean voir;

	public Dessinateur(String nom, boolean connecte) {
		super();
		this.nom = nom;
		this.setConnecte(connecte);
		this.voir = true;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public boolean isConnecte() {
		return connecte;
	}

	public void setConnecte(boolean connecte) {
		this.connecte = connecte;
	}

	public String toString(){
		return nom + ((connecte) ? " (connecte)" : " (deconnecte)");
	}

	public Boolean getVoir() {
		return voir;
	}

	public void setVoir(Boolean voir) {
		this.voir = voir;
	}

}
