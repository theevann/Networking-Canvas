package forme;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;

public class Texte extends Motif {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Font police = new Font("Arial", Font.PLAIN, 12);
	private int hauteur = 0, largeur = 0;
	Point point;
	String texte = "";
	
	public Texte(Point point, String texte, Font police) {
		super(TypeMotif.TEXTE);
		this.point = point;
		this.police = police;
		this.texte = texte;
	}
	
	public Texte clone(){
		Texte cloneTexte = new Texte(point, texte, police);
		cloneTexte.setCouleur(couleur);
		cloneTexte.setNumUser(getNumUser());
		return cloneTexte;
		
	}
	
	public boolean estIntersecteParRect(Rectangle r){
		Rectangle recTexte = new Rectangle(new Point(point.x, point.y - hauteur), new Point(point.x + largeur, point.y), false);
		return recTexte.estIntersecteParRect(r);
	}
	
	public void deplacer(Dimension dim){
		point.x += dim.width;
		point.y += dim.height;
	}
		
	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}
	
	public void setTaillePolice(int taille) {
		this.police.deriveFont((float)taille);
	}
	
	public void setPolice(Font p){
		this.police = p;
	}

	public String getTexte() {
		return texte;
	}

	public void setTexte(String texte) {
		this.texte = texte;
	}
	
	public String toString() {
		return "Texte : " + texte + "\n";
	}
	
	public void dessinerSur(Graphics g){
		g.setColor(couleur);
		g.setFont(police);
		g.drawString(texte, point.x, point.y);
		
		if(largeur*hauteur == 0){
			FontMetrics fontMetrics = g.getFontMetrics();
			largeur = fontMetrics.stringWidth(texte);
			hauteur = fontMetrics.getHeight();
		}
	}

	public Font getPolice() {
		return police;
	}
}
