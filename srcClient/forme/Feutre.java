package forme;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;

public class Feutre extends Motif{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Point> listePoint;
	private boolean plein = false;
	
	public Feutre() {
		super(TypeMotif.FEUTRE);
		listePoint = new ArrayList<>();
	}
	
	public Feutre clone(){
		Feutre cloneFeutre = new Feutre();
		cloneFeutre.setTaille(taille);
		cloneFeutre.setType(type);
		cloneFeutre.setCouleur(couleur);
		cloneFeutre.setListePoint((ArrayList<Point>) this.listePoint.clone());
		cloneFeutre.setNumUser(getNumUser());
		return cloneFeutre;
	}
	
	public boolean estIntersecteParRect(Rectangle r){		
		Point pointPrecedent = null;
		LigneDroite ligne = null;
		for(Point point : listePoint){
			if(pointPrecedent  != null){
				ligne = new LigneDroite(pointPrecedent, point);
				if(ligne.estIntersecteParRect(r) || r.pointEstDansRectangle(point)){
					return true;
				}
			}
			pointPrecedent = point;
		}
		return false;
	}
	
	public void deplacer(Dimension dim){
		for(Point point : listePoint){
			point.x += dim.width;
			point.y += dim.height;
		}
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
	
	/**
	 * @ Renvoie la liste des points
	 */
	public ArrayList<Point> getListePoint() {
		return listePoint;
	}
	
	public void setListePoint(ArrayList<Point> listePoint) {
		this.listePoint = listePoint;
	}
	
	public void ajouterPoint(Point point){
		listePoint.add(point);
	}
	
public String toString(){
		String chaine="[ " ;
		
		for(Point point : listePoint){
			chaine += ("(" + point.x + "," + point.y + ") "); 
		}
		chaine += "]";
		return chaine;
	}
	
	public void dessinerSur(Graphics g){
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(couleur);
		
		if(selection)
			g2d.setStroke(new BasicStroke(getTaille(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, proportionPointille, 5f));
		else
			g2d.setStroke(new BasicStroke(getTaille(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		
		int i = 0;
		int x [] = new int[listePoint.size()], y [] = new int[listePoint.size()];
		for(Point point : listePoint){
			x[i] = point.x;
			y[i] = point.y;
			i++;
		}
		if(plein)
			g2d.fillPolygon(x, y, listePoint.size());
		else
			g2d.drawPolyline(x, y, x.length);
		
		
		/*
		for(Point point : listePoint){
			if(pointPrecedent != null)
				g2d.drawLine( pointPrecedent.x, pointPrecedent.y, point.x, point.y);
			pointPrecedent = point;
		}
		//*/
		
	}
}