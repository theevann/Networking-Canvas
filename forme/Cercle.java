package forme;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

public class Cercle extends Motif {
	private static final long serialVersionUID = 1L;
	Point centre;
	int rayon = 0;
	private boolean plein = false;
	
	public Cercle(Point centre, int rayon, Color couleur, boolean plein) {
		super(TypeMotif.CERCLE);
		this.couleur = couleur;
		this.centre = centre;
		this.rayon = rayon;
		this.plein = plein;
	}
	
	public Cercle(Point centre, int rayon, boolean plein) {
		super(TypeMotif.CERCLE);
		this.centre = centre;
		this.rayon = rayon;
		this.plein = plein;
	}
	
	public Cercle clone(){
		Cercle cloneCercle = new Cercle(centre, rayon, couleur, plein);
		cloneCercle.setTaille(taille);
		cloneCercle.setType(type);
		cloneCercle.setNumUser(getNumUser());
		return cloneCercle;
	}

	public boolean estIntersecteParRect(Rectangle r){		
		Point rpoint3 = new Point(r.point2.x, r.point1.y);
		Point rpoint4 = new Point(r.point1.x, r.point2.y);
		
		LigneDroite ligne1 = new LigneDroite(r.point1, rpoint3);
		LigneDroite ligne2 = new LigneDroite( rpoint3, r.point2);
		LigneDroite ligne3 = new LigneDroite(r.point2, rpoint4);
		LigneDroite ligne4 = new LigneDroite(rpoint4, r.point1);
		
		if(pointEstDansCercle(r.point1) && pointEstDansCercle(r.point2) && pointEstDansCercle(rpoint3) && pointEstDansCercle(rpoint4))
			return false;
		else if(r.pointEstDansRectangle(centre))
			return true;
		else if(estIntersecteParLigneHorizontale(ligne1) || estIntersecteParLigneVerticale(ligne2) || estIntersecteParLigneHorizontale(ligne3) || estIntersecteParLigneVerticale(ligne4))
			return true;
		return false;
	}
	
	private boolean pointEstDansCercle(Point point1) {
		return Math.hypot(centre.x - point1.x,centre.y - point1.y) < rayon;
	}

	public boolean estIntersecteParLigneHorizontale(LigneDroite l){
		if(Math.abs((centre.y-l.point1.y)) < rayon){
			if(centre.x < Math.min(l.point1.x, l.point2.x))
				return Math.hypot(centre.x - Math.min(l.point1.x, l.point2.x), centre.y - l.point1.y) < rayon;
			else if(centre.x > Math.max(l.point1.x, l.point2.x))
				return Math.hypot(centre.x - Math.max(l.point1.x, l.point2.x), centre.y - l.point1.y) < rayon;
			else
				return true;
		}
		return false;
	}
	
	public boolean estIntersecteParLigneVerticale(LigneDroite l){
		if(Math.abs((centre.x-l.point1.x)) < rayon){
			if(centre.y < Math.min(l.point1.y, l.point2.y))
				return Math.hypot(centre.x - l.point1.x, centre.y - Math.min(l.point1.y, l.point2.y)) < rayon;
			else if(centre.y > Math.max(l.point1.y, l.point2.y))
				return Math.hypot(centre.x - l.point1.x, centre.y - Math.max(l.point1.y, l.point2.y)) < rayon;
			else
				return true;
		}
		return false;
	}
	
	public void deplacer(Dimension dim){
		centre.x += dim.width;
		centre.y += dim.height;
	}

	
	public Point getCentre() {
		return centre;
	}

	public void setCentre(Point centre) {
		this.centre = centre;
	}

	public int getRayon() {
		return rayon;
	}

	public void setRayon(int rayon) {
		this.rayon = rayon;
	}
	
	public String toString() {
		return "Cercle : {" + centre.x + "," + centre.y +"} R: " + rayon + "\n";
	}
	
	public void dessinerSur(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(couleur);
		
		g2d.setStroke(new BasicStroke(getTaille()));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		if(selection && !plein)
			g2d.setStroke(new BasicStroke(getTaille(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, proportionPointille, 5f));
		else if(selection && plein)
			g2d.setStroke(new BasicStroke(7f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, proportionPointille, 5f));
		
		if(!plein || selection)
			g2d.drawOval(centre.x-rayon, centre.y-rayon, 2*rayon, 2*rayon);
		else
			g2d.fillOval(centre.x-rayon, centre.y-rayon, 2*rayon, 2*rayon);

	}

	/**
	 * @return the plein
	 */
	public boolean isPlein() {
		return plein;
	}

	/**
	 * @param plein the plein to set
	 */
	public void setPlein(boolean plein) {
		this.plein = plein;
	}
}
