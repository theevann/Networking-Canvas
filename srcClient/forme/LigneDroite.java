package forme;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

/** Classe spécialisée de la classe Motif, elle est caractérisé par la défininiton de deux points  qui spécifient les coordonnées du point
 * supérieur gauche et inférieur droit
 */
public class LigneDroite extends Motif{
	private static final long serialVersionUID = 1L;
	
	Point point1;
	Point point2;
	
	public LigneDroite(Point point1, Point point2) {
		super(TypeMotif.LIGNEDROITE);
		this.point1 = point1;
		this.point2 = point2;	
	}
	
	public LigneDroite clone(){
		LigneDroite cloneLigne = new LigneDroite(point1, point2);
		cloneLigne.setCouleur(couleur);
		cloneLigne.setTaille(taille);
		cloneLigne.setType(type);
		cloneLigne.setNumUser(getNumUser());
		return cloneLigne;
	}
	
	public boolean estIntersecteParRect(Rectangle r){		
		Point rpoint3 = new Point(r.point2.x, r.point1.y);
		Point rpoint4 = new Point(r.point1.x, r.point2.y);
		
		LigneDroite ligne1 = new LigneDroite(r.point1, rpoint3);
		LigneDroite ligne2 = new LigneDroite( rpoint3, r.point2);
		LigneDroite ligne3 = new LigneDroite(r.point2, rpoint4);
		LigneDroite ligne4 = new LigneDroite(rpoint4, r.point1);
		
		if(r.pointEstDansRectangle(point1) || r.pointEstDansRectangle(point2)){
			return true;
		}
		else if(estIntersecteParLigneHorizontale(ligne1) || estIntersecteParLigneVerticale(ligne2) || estIntersecteParLigneHorizontale(ligne3) || estIntersecteParLigneVerticale(ligne4))
			return true;
		return false;
	}
	
	public boolean estIntersecteParLigneHorizontale(LigneDroite l){
		// Si les coefficients directeurs sont les mêmes, on renvoie false
		if((point1.y-point2.y) == 0)
			return false;
		
		double x = (l.point1.y*(point2.x-point1.x)+(point2.y*point1.x-point2.x*point1.y))/(point2.y-point1.y); //On calcule l'abscisse du point d'intersection ; l.point1.x égal à l.point2.x
		if(x < Math.max(l.point1.x, l.point2.x) && x > Math.min(l.point1.x, l.point2.x) && (l.point1.y < Math.max(point1.y, point2.y)) && (l.point1.y > Math.min(point1.y, point2.y)))
			return true;
		return false;
	}
	
	public boolean estIntersecteParLigneVerticale(LigneDroite l){
		// Si les coefficients directeurs sont les mêmes, on renvoie false
		if((point1.x-point2.x) == 0)
			return false;
		
		double y = (l.point1.x*(point2.y-point1.y)-(point2.y*point1.x-point2.x*point1.y))/(point2.x-point1.x); //On calcule l'ordonnée du point d'intersection ; l.point1.y égal à l.point2.y
		if(y < Math.max(l.point1.y, l.point2.y) && y > Math.min(l.point1.y, l.point2.y) && (l.point1.x < Math.max(point1.x, point2.x)) && (l.point1.x > Math.min(point1.x, point2.x)))
			return true;
		return false;
	}
	
	public void deplacer(Dimension dim){
		point1.x += dim.width;
		point1.y += dim.height;
		point2.x += dim.width;
		point2.y += dim.height;
	}

	public String toString(){
		return "LigneDroite :  {" + point1.x + "," + point1.y +"} -> {" + point2.x + "," + point2.y + "}\n";
	}

	public Point getPoint1() {
		return point1;
	}

	public void setPoint1(Point point1) {
		this.point1 = point1;
	}

	public Point getPoint2() {
		return point2;
	}

	public void setPoint2(Point point2) {
		this.point2 = point2;
	}
	
	public void dessinerSur(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(couleur);
		if(selection)
			g2d.setStroke(new BasicStroke(getTaille(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, proportionPointille, 5f));
		else
			g2d.setStroke(new BasicStroke(getTaille(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawLine(point1.x, point1.y, point2.x, point2.y);
	}
	
}


