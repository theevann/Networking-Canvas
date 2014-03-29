package ProgrammeClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import forme.Feutre;
import forme.LigneDroite;

@SuppressWarnings("serial")
public class BdDModifFeutre extends JDialog {
	private JLabel couleurLabel, epaisseurLabel, positionLabel;
	private JButton bCouleur = new JButton(), cancelBouton, okBouton;
	private Feutre feutre = null;
  
	private JSpinner positionX, positionY,epaisseur;
	
  public BdDModifFeutre(JFrame parent, Feutre f){
	super(parent, "Modifier le trait", true);
	this.feutre = f.clone();
	this.setSize(270, 300);
	this.setLocationRelativeTo(null);
	this.setResizable(false);
	//this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	this.initComponent();
	}
 
 public Feutre afficher(){
	this.setVisible(true);
	return feutre;
	}

  private void initComponent(){

	JPanel panCouleur = new JPanel();
	panCouleur.setBackground(Color.white);
	panCouleur.setPreferredSize(new Dimension(220, 60));
	bCouleur.setPreferredSize(new Dimension(100, 25));
	bCouleur.setBackground(feutre.getCouleur());
	bCouleur.addActionListener(new EcouteurCouleur2(bCouleur));
	couleurLabel = new JLabel("Couleur :");
	panCouleur.setBorder(BorderFactory.createTitledBorder("Couleur de la Ligne"));
	panCouleur.add(couleurLabel);
	panCouleur.add(bCouleur);
	
	JPanel panEpaisseur = new JPanel();
	panEpaisseur.setBackground(Color.white);
	panEpaisseur.setPreferredSize(new Dimension(220, 60));
	SpinnerNumberModel model1  = new SpinnerNumberModel(feutre.getTaille(), 0.5f, 20f, 0.1f);
	epaisseur = new JSpinner(model1);
	epaisseur.setPreferredSize(new Dimension(100, 20));
	epaisseurLabel = new JLabel("Epaisseur :");
	panEpaisseur.setBorder(BorderFactory.createTitledBorder("Epaisseur de la ligne"));
	panEpaisseur.add(epaisseurLabel);
	panEpaisseur.add(epaisseur);
	
	JPanel panPosition = new JPanel();
	JPanel panPosition2 = new JPanel(new GridLayout(2, 2));
	panPosition.setBackground(Color.white);
	panPosition2.setBackground(Color.white);
	panPosition.setPreferredSize(new Dimension(220, 80));
	panPosition2.setPreferredSize(new Dimension(100, 50));
	SpinnerNumberModel model2  = new SpinnerNumberModel(feutre.getListePoint().get(0).x, -10000, 10000, 5);
	SpinnerNumberModel model3  = new SpinnerNumberModel(feutre.getListePoint().get(0).y, -10000, 10000, 5);
	positionX = new JSpinner(model2);
	positionY = new JSpinner(model3);
	positionX.setPreferredSize(new Dimension(100, 20));
	positionY.setPreferredSize(new Dimension(100, 20));
	positionLabel = new JLabel("Position ");
	panPosition.setBorder(BorderFactory.createTitledBorder("Position du trait"));
	panPosition2.add(new JLabel("sur X :"));
	panPosition2.add(positionX);
	panPosition2.add(new JLabel("sur Y :"));
	panPosition2.add(positionY);
	panPosition.add(positionLabel);
	panPosition.add(panPosition2);
	
	JPanel content = new JPanel();
	content.setBackground(Color.white);
	content.add(panCouleur);
	content.add(panEpaisseur);
	content.add(panPosition);
	
	JPanel control = new JPanel();
	control.setBackground(Color.white);
	okBouton = new JButton("OK");
	
	okBouton.addActionListener(new ActionListener(){
	public void actionPerformed(ActionEvent arg0) {
		/*donnees[0] = adresse.getText();
		donnees[1] = nom.getText();*/
		feutre.setCouleur(bCouleur.getBackground());
		feutre.setTaille((float) ((double) epaisseur.getValue()));
		feutre.deplacer(new Dimension(-feutre.getListePoint().get(0).x + (int)positionX.getValue(), -feutre.getListePoint().get(0).y + (int)positionY.getValue()));
		setVisible(false);
		}
	});

	cancelBouton = new JButton("Annuler");
	cancelBouton.addActionListener(new ActionListener(){
	 public void actionPerformed(ActionEvent arg0) {
		 feutre = null;
		 setVisible(false);
	 }
	});

	control.add(okBouton);
	control.add(cancelBouton);

	this.getContentPane().add(content, BorderLayout.CENTER);
	this.getContentPane().add(control, BorderLayout.SOUTH);
	}
  
  public void windowClosing(WindowEvent w) {
	  ((AbstractButton) cancelBouton).doClick();
	  }
}

class EcouteurCouleur4 implements ActionListener{
	
	private JButton b;

	public EcouteurCouleur4(JButton b){
		this.b = b;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		bdDialogueCouleur jop = new bdDialogueCouleur(MainClient.fenetre, "Choisissez la couleur du trait", true, b.getBackground());
		Color couleur = jop.afficher();
		if(couleur != null)
			b.setBackground(couleur);
	}
	
}