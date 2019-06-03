package client;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ListIterator;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import forme.Motif;
import forme.TypeMotif;


@SuppressWarnings("serial")
public class Fenetre extends JFrame {
	private JPanel contenant = new JPanel();
	JTabbedPane menuDroite = new JTabbedPane();
	JPanel menuHaut = new JPanel();
	JPanel menuGauche = new JPanel();
	JPanel panneauBas = new JPanel(new BorderLayout());
	JPanel panneauCentre = new JPanel(new BorderLayout());

	JPanel propriete = new JPanel(new GridLayout(1,2));
	JPanel option = new JPanel(new GridLayout(1,4));
	JPanel barreEtat = new JPanel(new BorderLayout());
	Canevas canevas = new Canevas(this);


	BufferedImage imageIcone = new BufferedImage(15, 15, BufferedImage.TYPE_INT_ARGB);
	ImageIcon iconeCouleur = new ImageIcon(imageIcone);

	JButton bDeplacer = new JButton("Deplacer");
	JButton bSelection = new JButton("Selection");
	JButton bFeutre = new JButton("Feutre");
	JButton bLigneDroite = new JButton("Ligne");
	JButton bRectangle = new JButton("Rectangle");
	JButton bCercle = new JButton("Cercle");
	JButton bTexte = new JButton("Texte");
	JButton bConnexion = new JButton("Se connecter a un serveur");
	JButton bSynchroniser = new JButton("Synchroniser");
	JButton bEnregistrer = new JButton("Enregistrer");
	JButton bAnnuler = new JButton("Annuler");
	JButton bCouleur = new JButton("Couleur");
	JButton bTaille = new JButton("Epaisseur");
	JButton bPolice = new JButton("Police");

	JLabel lBarreEtat1 = new JLabel(" FEUTRE", SwingConstants.LEFT);
	JLabel lBarreEtat2 = new JLabel("(0;0) ", SwingConstants.RIGHT);

	OngletDessinateur ongletDessinateurs = new OngletDessinateur(new JTable());
	OngletHistorique ongletHistorique = new OngletHistorique(new JTable());

	private JTextArea historiqueChat = new JTextArea("Chat");
	private JScrollPane historiqueChatScroll = new JScrollPane(historiqueChat);
	private JTextArea entreeChat = new JTextArea();
	private JScrollPane entreeChatScroll = new JScrollPane(entreeChat);
	private JSplitPane splitChat = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, historiqueChatScroll, entreeChatScroll);

	JPopupMenu jpm = new JPopupMenu();
	private JMenuItem remplirForme = new JMenuItem("Forme pleine");
	private JMenuItem contourForme = new JMenuItem("Contour uniquement");

	private boolean plein = false;
	private float epaisseur = 2.0f;
	private Font police = new Font("Arial", Font.PLAIN, 12);
	private TypeMotif motifSelectionne = TypeMotif.FEUTRE;

	final Dimension dimFenetreInit = new Dimension(1070, 700);
	EcouteClavier ecouteur;

	public Fenetre(){
		this.setTitle("Toile Virtuelle - V1");
		this.setPreferredSize(dimFenetreInit);
		this.setMinimumSize(new Dimension(650,500));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		contenant.setBackground(Color.white);
		contenant.setLayout(new BorderLayout());


		remplirForme.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent arg0) {
		        setPlein(true);
		      }
		    });
		contourForme.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent arg0) {
		        setPlein(false);
		      }
		    });
		jpm.add(remplirForme);
		jpm.add(contourForme);

		canevas.setPreferredSize(Canevas.getDimensionCanevas());
		panneauCentre.add(canevas);
		contenant.add(panneauCentre, BorderLayout.CENTER);

		menuGauche.setLayout(new GridLayout(7, 1));
		menuGauche.add(bDeplacer);
		menuGauche.add(bSelection);
		menuGauche.add(bFeutre);
		menuGauche.add(bLigneDroite);
		menuGauche.add(bRectangle);
		menuGauche.add(bCercle);
		menuGauche.add(bTexte);
		contenant.add(menuGauche, BorderLayout.WEST);


		option.add(bConnexion);
		option.add(bSynchroniser);
		option.add(bEnregistrer);
		option.add(bAnnuler);
		propriete.add(bCouleur);
		propriete.add(bTaille);
		//propriete.setPreferredSize(new Dimension(this.getWidth()/7*2, 30));

		menuHaut.setPreferredSize(new Dimension(this.getWidth(), 30));
		menuHaut.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
	    //On positionne la case de depart du composant
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    //La taille en hauteur et en largeur
	    gbc.gridheight = 1;
	    gbc.gridwidth = 4;
	    gbc.fill = GridBagConstraints.HORIZONTAL;;
	    gbc.weightx = 0.5;
	    gbc.insets = new Insets(0,3,0,3);
	    menuHaut.add(option, gbc);
	    //---------------------------------------------
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridwidth = 2;
	    gbc.gridx = 4;
	    gbc.weightx = 0.5;
	    gbc.insets = new Insets(0,0,0,3);
	    menuHaut.add(propriete, gbc);
	    //---------------------------------------------
	    gbc.gridwidth = GridBagConstraints.REMAINDER;
	    gbc.gridx = 6;
	    gbc.weightx = 0.5;
	    gbc.insets = new Insets(0,0,0,3);
	    menuHaut.add(bPolice, gbc);
	    //---------------------------------------------

		contenant.add(menuHaut, BorderLayout.NORTH);

		menuDroite.add("Dessinateurs", ongletDessinateurs);
		menuDroite.add("Historique", ongletHistorique);
		menuDroite.setPreferredSize(new Dimension(270, 400));
		menuDroite.setBorder(BorderFactory.createLineBorder(Color.black));
		contenant.add(menuDroite, BorderLayout.EAST);

		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		Border compound = BorderFactory.createCompoundBorder(
                raisedbevel, loweredbevel);
		barreEtat.add(lBarreEtat1, BorderLayout.WEST);
		barreEtat.add(lBarreEtat2, BorderLayout.EAST);
		barreEtat.setBorder(compound);
		panneauCentre.add(barreEtat, BorderLayout.SOUTH);

		historiqueChat.setEditable(false);
		entreeChat.addKeyListener(new entreeChatListener(entreeChat));
		splitChat.setPreferredSize(new Dimension(900, 150));
		splitChat.setDividerLocation(150*3/5);
		panneauBas.add(splitChat, BorderLayout.CENTER);

		contenant.add(panneauBas, BorderLayout.SOUTH);

		bCercle.addActionListener(new BoutonMenuGaucheListener(this));
		bRectangle.addActionListener(new BoutonMenuGaucheListener(this));
		bLigneDroite.addActionListener(new BoutonMenuGaucheListener(this));
		bTexte.addActionListener(new BoutonMenuGaucheListener(this));
		bFeutre.addActionListener(new BoutonMenuGaucheListener(this));
		bSelection.addActionListener(new BoutonMenuGaucheListener(this));
		bDeplacer.addActionListener(new BoutonMenuGaucheListener(this));

		bFeutre.addMouseListener(new SourisMenuGaucheListener(this));
		bRectangle.addMouseListener(new SourisMenuGaucheListener(this));
		bCercle.addMouseListener(new SourisMenuGaucheListener(this));

		bConnexion.addActionListener(new BoutonMenuHautListener(this));
		bSynchroniser.addActionListener(new BoutonMenuHautListener(this));
		bAnnuler.addActionListener(new BoutonMenuHautListener(this));
		bEnregistrer.addActionListener(new BoutonMenuHautListener(this));
		bCouleur.addActionListener(new BoutonMenuHautListener(this));
		bTaille.addActionListener(new BoutonMenuHautListener(this));
		bPolice.addActionListener(new BoutonMenuHautListener(this));

		actualiserIconeCouleur();
		bCouleur.setIcon(iconeCouleur);

		modeDeconnecte();

		addWindowListener(new EcouterFermetureFenetre());
		ecouteur = new EcouteClavier(this);
		Toolkit.getDefaultToolkit().addAWTEventListener(ecouteur, AWTEvent.KEY_EVENT_MASK);

		this.setContentPane(contenant);
		this.pack();
		this.setVisible(true);
		addComponentListener(new EcouteurRedimension());
	}

	public TypeMotif getMotifSelectionne() {
		return motifSelectionne;
	}

	public void setMotifSelectionne(TypeMotif motifSelectionne) {
		this.motifSelectionne = motifSelectionne;
	}

	public Canevas getCanevas() {
		return canevas;
	}

	public void afficherCanevas() {
		canevas.setVisible(true);
	}

	public void masquerCanevas() {
		canevas.setVisible(false);
	}

	public void actualiserIconeCouleur(){
		Graphics g = imageIcone.createGraphics();
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	    g2d.setColor(canevas.getCouleur());
	    g2d.fillOval(0, 0, 15, 15);
	}

	public void ajouterMessageChat(String message){
		historiqueChat.setText(historiqueChat.getText() + "\n" + message);
		historiqueChat.setCaretPosition(historiqueChat.getText().length());
	}

	public void modeConnecte(){ // Gere uniquement l'AFFICHAGE graphique lors de la connexion
		OngletHistorique.ongletActuel.getTableau().setVisible(true);
		ongletDessinateurs.getTableau().setVisible(true);
		afficherCanevas();
		bSynchroniser.setEnabled(true);
		bEnregistrer.setEnabled(true);
		bAnnuler.setEnabled(true);
		bCouleur.setEnabled(true);
		bTaille.setEnabled(true);
		bPolice.setEnabled(true);
		historiqueChat.setText("Chat");
		entreeChat.setEnabled(true);
		menuDroite.setEnabled(true);
    	bConnexion.setText("Se d\351connecter");
	}

	public void modeDeconnecte(){ // Gere uniquement l'AFFICHAGE graphique lors de la deconnexion
		OngletHistorique.ongletActuel.getTableau().setVisible(false);
		ongletDessinateurs.getTableau().setVisible(false);
		masquerCanevas();
		bSynchroniser.setEnabled(false);
		bEnregistrer.setEnabled(false);
		bAnnuler.setEnabled(false);
		bCouleur.setEnabled(false);
		bTaille.setEnabled(false);
		bPolice.setEnabled(false);
		historiqueChat.setText(" ");
		entreeChat.setEnabled(false);
		menuDroite.setEnabled(false);
		bConnexion.setText("Se connecter a un serveur");
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
		lBarreEtat1.setText(" " + this.getMotifSelectionne().toString() + ((this.getMotifSelectionne() == TypeMotif.RECTANGLE || this.getMotifSelectionne() == TypeMotif.CERCLE || this.getMotifSelectionne() == TypeMotif.FEUTRE)? (this.isPlein() ? " (Plein)" : " (Contour)") :""));
	}

	/**
	 * @return the epaisseur
	 */
	public float getEpaisseur() {
		return epaisseur;
	}

	/**
	 * @param epaisseur the epaisseur to set
	 */
	public void setEpaisseur(float epaisseur) {
		this.epaisseur = epaisseur;
	}

	/**
	 * @return the police
	 */
	public Font getPolice() {
		return police;
	}

	/**
	 * @param police the police to set
	 */
	public void setPolice(Font police) {
		this.police = police;
	}

	public JTextArea getEntreeChat() {
		return entreeChat;
	}

	public JTextArea getHistoriqueChat() {
		return historiqueChat;
	}

}


class BoutonMenuGaucheListener implements ActionListener{

	Fenetre fenetre;

	public BoutonMenuGaucheListener(Fenetre f){
		this.fenetre = f;
	}

	public void actionPerformed(ActionEvent a) {

		if(a.getSource() == fenetre.bCercle){
			fenetre.setMotifSelectionne(TypeMotif.CERCLE);
		}
		else if(a.getSource() == fenetre.bLigneDroite){
			fenetre.setMotifSelectionne(TypeMotif.LIGNEDROITE);
		}
		else if(a.getSource() == fenetre.bRectangle){
			fenetre.setMotifSelectionne(TypeMotif.RECTANGLE);
		}
		else if(a.getSource() == fenetre.bTexte){
			fenetre.setMotifSelectionne(TypeMotif.TEXTE);
		}
		else if(a.getSource() == fenetre.bFeutre){
			fenetre.setMotifSelectionne(TypeMotif.FEUTRE);
		}
		else if(a.getSource() == fenetre.bSelection){
			fenetre.setMotifSelectionne(TypeMotif.SELECTION);
		}
		else if(a.getSource() == fenetre.bDeplacer){
			fenetre.setMotifSelectionne(TypeMotif.DEPLACER);
		}
		fenetre.setPlein(false);
		fenetre.lBarreEtat1.setText(" " + fenetre.getMotifSelectionne().toString() + ((fenetre.getMotifSelectionne() == TypeMotif.RECTANGLE || fenetre.getMotifSelectionne() == TypeMotif.CERCLE || fenetre.getMotifSelectionne() == TypeMotif.FEUTRE)? " (Contour)": ""));
	}

}

class BoutonMenuHautListener implements ActionListener{

	Fenetre fenetre;

	public BoutonMenuHautListener(Fenetre f){
		this.fenetre = f;
	}

	public void actionPerformed(ActionEvent a) {

		if(a.getSource() == fenetre.bConnexion){
			/*JOptionPane jop1 = new JOptionPane();
			jop1.showMessageDialog(null, InitClient.client.isConnecte(), "Information", JOptionPane.INFORMATION_MESSAGE);//*/

			if(!MainClient.client.isConnecte()){
				//*
				BdDialogueConnexion jop = new BdDialogueConnexion(null, "Connexion e un serveur", true);
			    String [] donnees = jop.afficher();
			    //*/String [] donnees = {"127.0.0.1", "Toto"};

			    if(donnees != null){
			    	//System.out.println("adresse (" + donnees[0] + ")");
			    	(new Thread(new Connexion(donnees[0], donnees[1]))).start();
			    }
			}
			else if(MainClient.client.isConnecte()){
				JOptionPane jop = new JOptionPane();
				int option = JOptionPane.showConfirmDialog(null, "Voulez-vous vous deconnecter du serveur ?", "Deconnexion du serveur", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(option == JOptionPane.YES_OPTION){
					MainClient.client.envoyer("Bye");
					long t = (long) System.currentTimeMillis();
					while(MainClient.client.isConnecte() && System.currentTimeMillis()-t < 1500)
					{
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		else if(a.getSource() == fenetre.bSynchroniser){
			MainClient.client.envoyer("synchro");
			MainClient.client.envoyer("synchroClient");
		}
		else if(a.getSource() == fenetre.bEnregistrer){
			ImageDessin.enregisterImageActuelle();
		}
		else if(a.getSource() == fenetre.bCouleur){
			bdDialogueCouleur jop = new bdDialogueCouleur(fenetre, "Choisissez la couleur", true);
			Color couleur  = jop.afficher();
			if(couleur != null)
				fenetre.getCanevas().setCouleur(couleur);
			fenetre.actualiserIconeCouleur();
		}
		else if(a.getSource() == fenetre.bTaille){
			BdDialogueEpaisseur jop = new BdDialogueEpaisseur(fenetre, "Choisissez la taille", true);
			fenetre.setEpaisseur((float)jop.afficher());
		}
		else if(a.getSource() == fenetre.bPolice){
			BdDialoguePolice jop = new BdDialoguePolice(fenetre, "Choisissez la taille", true);
			fenetre.setPolice(jop.afficher());
		}
		else if(a.getSource() == fenetre.bAnnuler){
			synchronized(ImageDessin.listeMotif){
				Motif motifASupprimer;
				ListIterator<Motif> it = ImageDessin.listeMotif.listIterator(ImageDessin.listeMotif.size());
				while(it.hasPrevious()){
					motifASupprimer = it.previous();
					if(motifASupprimer.getNumUser() == MainClient.client.getNumuser()){
						MainClient.client.envoyer("u" + it.nextIndex());
						break;
					}
				}

			}
		}

	}

}

class entreeChatListener implements KeyListener{
	private JTextArea zoneTexte;

	public entreeChatListener(JTextArea zoneTexte) {
		this.zoneTexte = zoneTexte;
	}

	@Override
	public void keyPressed(KeyEvent event) {
	}

	@Override
	public void keyReleased(KeyEvent event) {
		if(event.getKeyCode() == 10 && zoneTexte.getText().length() > 0){ //Si on appuie sur Entree et qu'on a ecrit qqchose
			if(zoneTexte.getText().substring(0, zoneTexte.getText().length()-1).contains("\n"))
				zoneTexte.setText(zoneTexte.getText().replace("\n".charAt(0), ' '));//On enleve les '\n' qui posent probleme e l'envoi
			MainClient.client.envoyer("c" + zoneTexte.getText().substring(0, zoneTexte.getText().length()-1)); // On envoie
			zoneTexte.setText("");
		}
	}

	@Override
	public void keyTyped(KeyEvent event) {
	}

}

class SourisMenuGaucheListener extends MouseAdapter{
	Fenetre fenetre;

	public SourisMenuGaucheListener(Fenetre f) {
		this.fenetre = f;
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if(event.getButton() == MouseEvent.BUTTON3){
			((AbstractButton) event.getSource()).doClick();
			fenetre.jpm.show((Component) event.getSource(), event.getX(), event.getY());
		}
	}

}

class EcouterFermetureFenetre extends WindowAdapter{
	public void windowClosing(WindowEvent w) {
		if(MainClient.client.isConnecte())
			MainClient.client.envoyer("Bye");
		long t = (long) System.currentTimeMillis();
		while(MainClient.client.isConnecte() && System.currentTimeMillis()-t < 1500)
		{
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	    MainClient.fenetre.dispose();     // liberation des ressources utilisees par la frame
	    System.exit(0);
	  }
}

class EcouteurRedimension extends ComponentAdapter{
	public void componentResized(ComponentEvent e) {
		if(OngletHistorique.ongletActuel != null && OngletHistorique.ongletActuel.getTableau() != null && OngletHistorique.ongletActuel.getTableau().getModel() != null){
			OngletHistorique.ongletActuel.getTableau().setPreferredScrollableViewportSize(new Dimension(240, 300+MainClient.fenetre.getSize().height-MainClient.fenetre.dimFenetreInit.height));
			((ModeleTableauHistorique) OngletHistorique.ongletActuel.getTableau().getModel()).fireTableDataChanged();
		}
	}
}
