package forme;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

/** Classe specialisee de la classe Motif, elle est caracterise par la defininiton de deux points qui specifient les coordonnees du point
 * superieur gauche et inferieur droit
 */
public class Rectangle extends Motif {
	private static final long serialVersionUID = 1L;
	/**
	 * Point superieur gauche  du rectangle
	 */
	Point point1;
	/**
	 * Point superieur droit du rectangle
	 */
	Point point2;
	/**
	 * booleen qui vaut true si on dessine un pave et false si c'est un rectangle
	 */
	private boolean plein = false;

	public Rectangle(Point point1, Point point2, boolean plein) {
		super(TypeMotif.RECTANGLE);
		this.point1 = point1;
		this.point2 = point2;
		this.plein = plein;
	}

	public Rectangle(Point point1, Point point2, Color couleur, boolean plein) {
		super(TypeMotif.RECTANGLE);
		this.point1 = point1;
		this.point2 = point2;
		this.couleur = couleur;
		this.plein = plein;
	}

	public Rectangle clone(){
		Rectangle cloneRectangle = new Rectangle(point1, point2, couleur, plein);
		cloneRectangle.setTaille(taille);
		cloneRectangle.setType(type);
		cloneRectangle.setNumUser(getNumUser());
		return cloneRectangle;
	}

	public boolean estIntersecteParRect(Rectangle r){
		Point rpoint3 = new Point(r.point2.x, r.point1.y);
		Point rpoint4 = new Point(r.point1.x, r.point2.y);

		if(r.point1.y < point2.y && r.point2.y > point1.y && r.point1.x < point2.x && r.point2.x > point1.x){
			if(pointEstDansRectangle(r.point1) && pointEstDansRectangle(r.point2) && pointEstDansRectangle(rpoint3) && pointEstDansRectangle(rpoint4))
				return false;
			else
				return true;
		}
		return false;
	}

	public boolean pointEstDansRectangle(Point p){
		if(p.x >= this.point1.x && p.x <= this.point2.x && p.y >= this.point1.y && p.y <= this.point2.y)
			return true;
		return false;
	}

	public void deplacer(Dimension dim){
		point1.x += dim.width;
		point1.y += dim.height;
		point2.x += dim.width;
		point2.y += dim.height;
	}

	/*
	public boolean ligneIntersecteRectangle(Point p){
		if(p.x >= point1.x && p.x <= point2.x && p.y >= point1.y && p.y <= point2.y)
			return true;
		return false;
	}
	*/
	public String toString(){
		return "Rectangle : {" + point1.x + "," + point1.y +"} -> {" + point2.x + "," + point2.y + "}\n";
	}
	public String getInfo(){
		return "{" + point1.x + "," + point1.y +"} -> {" + point2.x + "," + point2.y + "}";
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

		g2d.setStroke(new BasicStroke(getTaille()));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		if(selection && !plein)
			g2d.setStroke(new BasicStroke(getTaille(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, proportionPointille, 5f));
		else if(selection && plein)
			g2d.setStroke(new BasicStroke(7f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, proportionPointille, 5f));

		if(!plein || selection)
			g2d.drawRect(point1.x, point1.y, point2.x-point1.x, point2.y-point1.y);
		else
			g2d.fillRect(point1.x, point1.y, point2.x-point1.x, point2.y-point1.y);

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
