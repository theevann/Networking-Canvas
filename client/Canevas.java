package client;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

import forme.*;
import forme.Rectangle;

@SuppressWarnings("serial")
public class Canevas extends JPanel{
	private static Dimension dimensionCanevas = new Dimension(500, 400);
	public static Fenetre fenetre;
	public boolean clicEnCours = false, clignotement = true;
	private Point point1;
	private Motif motifEnCours;
	private Color Couleur = Color.black;
	private ClignotementPointille threadClignotement = new ClignotementPointille();

	public Canevas(Fenetre f){
		super();
		fenetre = f;
		addMouseListener(new EcouteurSourisCanevas(this));
		addMouseMotionListener(new EcouteurMouvementSourisCanevas(this));
		this.setVisible(false);
	}

	public void paintComponent(Graphics g) {
		  g.setColor(Color.gray);
		  g.fillRect(0, 0, this.getWidth(), this.getHeight());
		  g.drawImage(ImageDessin.imageActuelle.img, 0, 0, null);

		  if(clicEnCours){
			  if(fenetre.getMotifSelectionne() == TypeMotif.SELECTION){
				  motifEnCours.setCouleur(Color.red);// couleur du rectangle de selection
				  motifEnCours.setTaille(4f); // taille du trait du rectangle de selection
				  motifEnCours.setSelection(true);
			  }
			  else
				  motifEnCours.setCouleur(Couleur);

			  motifEnCours.dessinerSur(g);
		  }

		  /**
		   * Gestion du clignotement des elements selectionnes
		   */

		  //Si des colonnes sont selectionnes et que la variable clignotement (mis a jour par le thread) est a true, on va dessiner des pointilles
		  if(OngletHistorique.ongletActuel.getTableau().getSelectedRowCount() > 0 && clignotement){
			  //Si le thread n'est pas en marche, on le lance.
			  if(threadClignotement.getState() != java.lang.Thread.State.RUNNABLE && threadClignotement.getState() != java.lang.Thread.State.TIMED_WAITING ){
				  threadClignotement = new ClignotementPointille();
				  threadClignotement.start();
			  }

			  // On recupere le tableau Jtable
			  JTable tableau = OngletHistorique.ongletActuel.getTableau();
			  // On prend dans le Jtable le numero des motifs e redessiner en pointilles -> on enregistre ces numeros dans un tableu d'int
			  int [] tableauADessine = new int [tableau.getSelectedRowCount()];
			  for(int i = 0 ; i < tableau.getSelectedRowCount() ; i++)
					tableauADessine[i] = tableau.convertRowIndexToModel(tableau.getSelectedRows()[i]);
				//On le trie, ici pas tres utile
				Arrays.sort(tableauADessine);
				//Pour chaque motif dont le rang dans la liste listeMotif est dans le tableau d'int (cad pour chaque motif selectionne), on demande e le redessiner en "mode selectionne", cad en pratique en pointilles
				for(int i : tableauADessine){
					try{
					Motif motifSelectionne = (Motif) ImageDessin.listeMotif.get(i).clone();
					motifSelectionne.setCouleur(new Color((int)((255-motifSelectionne.getCouleur().getRed())*0.8),(int)((255-motifSelectionne.getCouleur().getGreen())*0.8),(int)((255-motifSelectionne.getCouleur().getBlue())*0.8)));
					motifSelectionne.setSelection(true);
					motifSelectionne.dessinerSur(g);
					}catch(IndexOutOfBoundsException e){} // Parfois probleme lors du parcours de la boucle qd quelquechose est supprime en meme temps... Pour ne pas avoir e synchroniser (inutile ici), on gere juste l'exception
				}
		  }
		  //Fin de la gestion du clignotement

	}

	public void masquerCanevas(){
		this.setVisible(false);
	}

	public Point getPoint1() {
		return point1;
	}

	public void setPoint1(Point point1) {
		this.point1 = point1;
	}

	public Motif getMotifEnCours() {
		return motifEnCours;
	}

	public void setMotifEnCours(Motif motifEnCours) {
		this.motifEnCours = motifEnCours;
	}

	/**
	 * @return the couleur
	 */
	public Color getCouleur() {
		return Couleur;
	}

	/**
	 * @param couleur the couleur to set
	 */
	public void setCouleur(Color couleur) {
		Couleur = couleur;
	}

	/**
	 * @return the dimensionCanevas
	 */
	public static Dimension getDimensionCanevas() {
		return dimensionCanevas;
	}

	/**
	 * @param dimensionCanevas the dimensionCanevas to set
	 */
	public static void setDimensionCanevas(Dimension dimensionCanevas) {
		int w = 0, h = 0;
		w = MainClient.fenetre.getCanevas().getWidth() - dimensionCanevas.width;
		h = MainClient.fenetre.getCanevas().getHeight() - (dimensionCanevas.height);

		fenetre.setSize((int) (fenetre.getWidth() - w), (int) (fenetre.getHeight()-h));
		fenetre.validate();

		Canevas.dimensionCanevas = dimensionCanevas;
		ImageDessin.setDimension(dimensionCanevas);
	}

}



class EcouteurMouvementSourisCanevas implements MouseMotionListener/*Adapter*/{
	private Canevas canevas;

	public EcouteurMouvementSourisCanevas(Canevas c){
		this.canevas = c;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(canevas.clicEnCours && MainClient.client.isConnecte()){

			if(Canevas.fenetre.getMotifSelectionne() == TypeMotif.CERCLE){
				canevas.setMotifEnCours(new Cercle(canevas.getPoint1(), (int)Math.sqrt((e.getX() - canevas.getPoint1().x)*(e.getX() - canevas.getPoint1().x) + (e.getY() - canevas.getPoint1().y)*(e.getY() - canevas.getPoint1().y)), Canevas.fenetre.isPlein()));
			}
			else if(Canevas.fenetre.getMotifSelectionne() == TypeMotif.LIGNEDROITE){
				canevas.setMotifEnCours(new LigneDroite(canevas.getPoint1(), e.getPoint()));
			}
			else if(Canevas.fenetre.getMotifSelectionne() == TypeMotif.RECTANGLE){
				Point point1 = new Point(Math.min(canevas.getPoint1().x, e.getPoint().x), Math.min(canevas.getPoint1().y, e.getPoint().y));
				Point point2 = new Point(Math.max(canevas.getPoint1().x, e.getPoint().x), Math.max(canevas.getPoint1().y, e.getPoint().y));
				canevas.setMotifEnCours(new Rectangle(point1, point2, Canevas.fenetre.isPlein()));
			}
			else if(Canevas.fenetre.getMotifSelectionne() == TypeMotif.FEUTRE){
				if(!(canevas.getMotifEnCours() instanceof Feutre)){
					canevas.setMotifEnCours(new Feutre());
				}
				((forme.Feutre) (canevas.getMotifEnCours())).ajouterPoint(e.getPoint());
			}
			else if(Canevas.fenetre.getMotifSelectionne() == TypeMotif.SELECTION){
				Point point1 = new Point(Math.min(canevas.getPoint1().x, e.getPoint().x), Math.min(canevas.getPoint1().y, e.getPoint().y));
				Point point2 = new Point(Math.max(canevas.getPoint1().x, e.getPoint().x), Math.max(canevas.getPoint1().y, e.getPoint().y));
				canevas.setMotifEnCours(new Rectangle(point1, point2, false));
			}
			else if(Canevas.fenetre.getMotifSelectionne() == TypeMotif.DEPLACER){
				canevas.setMotifEnCours(new LigneDroite(canevas.getPoint1(), e.getPoint()));
			}
			canevas.getMotifEnCours().setTaille(MainClient.fenetre.getEpaisseur());
			canevas.repaint();
		}
		MainClient.fenetre.lBarreEtat2.setText("(" + e.getX() + " ; " + e.getY() + ") ");
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		MainClient.fenetre.lBarreEtat2.setText("(" + arg0.getX() + " ; " + arg0.getY() + ") ");
	}

}


class EcouteurSourisCanevas extends MouseAdapter{

	private Canevas canevas;

	public EcouteurSourisCanevas(Canevas c){
		this.canevas = c;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// Si on clic, c'est pour mettre du texte et c'est tout
		if(MainClient.fenetre.getMotifSelectionne() == TypeMotif.TEXTE){
			JOptionPane jop = new JOptionPane();
			String texte = jop.showInputDialog(null, "Texte a inserer : ", "Ecrire du texte", JOptionPane.QUESTION_MESSAGE);
			if(texte != null && texte != ""){
				Texte txt = new Texte(e.getPoint(), texte, Canevas.fenetre.getPolice());
				txt.setCouleur(canevas.getCouleur());
				MainClient.client.envoyerMotif(txt);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(MainClient.fenetre.getMotifSelectionne() == TypeMotif.TEXTE){
			MainClient.fenetre.getCanevas().setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		}
		else if(MainClient.fenetre.getMotifSelectionne() == TypeMotif.DEPLACER){
			MainClient.fenetre.getCanevas().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		}
		else if(MainClient.fenetre.getMotifSelectionne() == TypeMotif.SELECTION){
			MainClient.fenetre.getCanevas().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		else
			MainClient.fenetre.getCanevas().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

	}

	@Override
	public void mouseExited(MouseEvent e) {
		//Si on quitte la zone de dessin, on considere le dessin comme annule. En plus, il est plus complique de recuperer l'information mouseReleased en dehors du Canevas
		canevas.clicEnCours = false;
		canevas.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		canevas.clicEnCours = true;
		canevas.setPoint1(e.getPoint());
		canevas.setMotifEnCours(new Motif());
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		if(canevas.clicEnCours && MainClient.client.isConnecte() && canevas.getMotifEnCours().getType() != null && MainClient.fenetre.getMotifSelectionne() != TypeMotif.SELECTION && MainClient.fenetre.getMotifSelectionne() != TypeMotif.DEPLACER){
			//Si c'est un feutre, on met plein que quand on envoie, sinon c'est incomprehensible quand on dessine
			if((canevas.getMotifEnCours() instanceof Feutre))
				((forme.Feutre) canevas.getMotifEnCours()).setPlein(Canevas.fenetre.isPlein());

			canevas.getMotifEnCours().setCouleur(canevas.getCouleur());
			canevas.getMotifEnCours().setNumUser(MainClient.client.getNumuser());
			MainClient.client.envoyerMotif(canevas.getMotifEnCours());
		}
		//
		else if(MainClient.fenetre.getMotifSelectionne() == TypeMotif.SELECTION && canevas.getMotifEnCours().getType() != null){
			int i = 0;
			if(!MainClient.fenetre.ecouteur.isCtrl())
					OngletHistorique.ongletActuel.deselectionner();
			for(Motif m : ImageDessin.listeMotif){
				if(!m.isMasque() && m.estIntersecteParRect((Rectangle) canevas.getMotifEnCours()) && MainClient.listeDessinateur.get(m.getNumUser()).getVoir())
					OngletHistorique.ongletActuel.selectionnerLigne(i);
				i++;
			}
			canevas.repaint();
		}
		else if(MainClient.fenetre.getMotifSelectionne() == TypeMotif.DEPLACER && canevas.getMotifEnCours().getType() != null){
			Object [] aEnvoyer = new Object [2];
			JTable tableau = OngletHistorique.ongletActuel.getTableau();
			int [] tableauADeplacer = new int [tableau.getSelectedRowCount()];

			for(int i = 0 ; i < tableau.getSelectedRowCount() ; i++)
				tableauADeplacer[i] = tableau.convertRowIndexToModel(tableau.getSelectedRows()[i]);

			aEnvoyer[0] = tableauADeplacer;
			aEnvoyer[1] = new Dimension(e.getX() - canevas.getPoint1().x,e.getY() - canevas.getPoint1().y);

			if(tableauADeplacer.length > 0)
				MainClient.client.envoyer("b",aEnvoyer);
			canevas.repaint();
		}
		else if(canevas.clicEnCours && MainClient.client.isConnecte())
			OngletHistorique.ongletActuel.deselectionner();

		canevas.clicEnCours = false;
	}

}
