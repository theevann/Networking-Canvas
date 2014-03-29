package forme;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.Serializable;

public class Motif implements Serializable, Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Color couleur = Color.black;
	protected TypeMotif type = null;
	int numUser;
	protected float taille = 2.0f;
	protected boolean selection = false;
	protected float[] proportionPointille = {18f,7f};
	private boolean masque = false;
	
	public Motif(TypeMotif type){
		this.setType(type);
	}
	
	public Motif(Color couleur){
		this.couleur = couleur;
	}
	
	public Motif() {
	}
	
	public Motif clone(){
		return null;
	}
	
	public boolean estIntersecteParRect(Rectangle r){
		return false;
	}
	
	public void deplacer(Dimension dim){
	}

	public Color getCouleur() {
		return couleur;
	}
	public void setCouleur(Color couleur) {
		this.couleur = couleur;
	}

	public TypeMotif getType() {
		return type;
	}

	public void setType(TypeMotif type) {
		this.type = type;
	}
	
	public void dessinerSur(Graphics g){
		//System.err.println("Fonction de dessin dans Motif non red\351fini !");
	}

	public void setNumUser(int num) {
		numUser = num;
	}
	
	public int getNumUser() {
		return numUser;
	}

	public String getInfo() {
		return "";
	}

	/**
	 * @return the taille
	 */
	public float getTaille() {
		return taille;
	}

	/**
	 * @param taille the taille to set
	 */
	public void setTaille(float taille) {
		this.taille = taille;
	}

	public void setSelection(boolean b) {
		this.selection  = b;		
	}

	/**
	 * @return the masque
	 */
	public boolean isMasque() {
		return masque;
	}

	/**
	 * @param masque the masque to set
	 */
	public void setMasque(boolean masque) {
		this.masque = masque;
	}
}

