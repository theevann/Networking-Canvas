package client;

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

import forme.LigneDroite;

@SuppressWarnings("serial")
public class BdDModifLigne extends JDialog {
	private JLabel couleurLabel, epaisseurLabel, positionLabel, positionLabel2;
	private JButton bCouleur = new JButton(), cancelBouton, okBouton;
	private LigneDroite ligne = null;
  
	private JSpinner positionX, positionY,positionX2, positionY2, epaisseur;
	
  public BdDModifLigne(JFrame parent, LigneDroite l){
	super(parent, "Modifier la Ligne", true);
	this.ligne = l.clone();
	this.setSize(270, 400);
	this.setLocationRelativeTo(null);
	this.setResizable(false);
	//this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	this.initComponent();
	}
 
 public LigneDroite afficher(){
	this.setVisible(true);
	return ligne;
	}

  private void initComponent(){

	JPanel panCouleur = new JPanel();
	panCouleur.setBackground(Color.white);
	panCouleur.setPreferredSize(new Dimension(220, 60));
	bCouleur.setPreferredSize(new Dimension(100, 25));
	bCouleur.setBackground(ligne.getCouleur());
	bCouleur.addActionListener(new EcouteurCouleur2(bCouleur));
	couleurLabel = new JLabel("Couleur :");
	panCouleur.setBorder(BorderFactory.createTitledBorder("Couleur de la Ligne"));
	panCouleur.add(couleurLabel);
	panCouleur.add(bCouleur);
	
	JPanel panEpaisseur = new JPanel();
	panEpaisseur.setBackground(Color.white);
	panEpaisseur.setPreferredSize(new Dimension(220, 60));
	SpinnerNumberModel model1  = new SpinnerNumberModel(ligne.getTaille(), 0.5f, 20f, 0.1f);
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
	panPosition.setPreferredSize(new Dimension(200, 80));
	panPosition2.setPreferredSize(new Dimension(100, 50));
	SpinnerNumberModel model2  = new SpinnerNumberModel(ligne.getPoint1().x, -10000, 10000, 5);
	SpinnerNumberModel model3  = new SpinnerNumberModel(ligne.getPoint1().y, -10000, 10000, 5);
	positionX = new JSpinner(model2);
	positionY = new JSpinner(model3);
	positionX.setPreferredSize(new Dimension(100, 20));
	positionY.setPreferredSize(new Dimension(100, 20));
	positionLabel = new JLabel("Position point 1  ");
	panPosition2.add(new JLabel("sur X :"));
	panPosition2.add(positionX);
	panPosition2.add(new JLabel("sur Y :"));
	panPosition2.add(positionY);
	panPosition.add(positionLabel);
	panPosition.add(panPosition2);
	
	JPanel panPosition3 = new JPanel();
	JPanel panPosition4 = new JPanel(new GridLayout(2, 2));
	panPosition3.setBackground(Color.white);
	panPosition4.setBackground(Color.white);
	panPosition3.setPreferredSize(new Dimension(200, 80));
	panPosition4.setPreferredSize(new Dimension(100, 50));
	SpinnerNumberModel model4  = new SpinnerNumberModel(ligne.getPoint2().x, -10000, 10000, 5);
	SpinnerNumberModel model5  = new SpinnerNumberModel(ligne.getPoint2().y, -10000, 10000, 5);
	positionX2 = new JSpinner(model4);
	positionY2 = new JSpinner(model5);
	positionX2.setPreferredSize(new Dimension(100, 20));
	positionY2.setPreferredSize(new Dimension(100, 20));
	positionLabel2 = new JLabel("Position point 2  ");
	panPosition4.add(new JLabel("sur X :"));
	panPosition4.add(positionX2);
	panPosition4.add(new JLabel("sur Y :"));
	panPosition4.add(positionY2);
	panPosition3.add(positionLabel2);
	panPosition3.add(panPosition4);
	
	JPanel panPositionGlobal = new JPanel();
	panPositionGlobal.setBackground(Color.white);
	panPositionGlobal.add(panPosition);
	panPositionGlobal.add(panPosition3);
	panPositionGlobal.setBorder(BorderFactory.createTitledBorder("Position de la ligne"));
	panPositionGlobal.setPreferredSize(new Dimension(220, 200));
	
	JPanel content = new JPanel();
	content.setBackground(Color.white);
	content.add(panCouleur);
	content.add(panEpaisseur);
	content.add(panPositionGlobal);
	
	JPanel control = new JPanel();
	control.setBackground(Color.white);
	okBouton = new JButton("OK");
	
	okBouton.addActionListener(new ActionListener(){
	public void actionPerformed(ActionEvent arg0) {
		/*donnees[0] = adresse.getText();
		donnees[1] = nom.getText();*/
		ligne.setCouleur(bCouleur.getBackground());
		ligne.setTaille((float) ((double) epaisseur.getValue()));
		ligne.setPoint1(new Point((int) positionX.getValue(), (int) positionY.getValue()));
		ligne.setPoint2(new Point((int) positionX2.getValue(), (int) positionY2.getValue()));
		setVisible(false);
		}
	});

	cancelBouton = new JButton("Annuler");
	cancelBouton.addActionListener(new ActionListener(){
	 public void actionPerformed(ActionEvent arg0) {
		 ligne = null;
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

class EcouteurCouleur2 implements ActionListener{
	
	private JButton b;

	public EcouteurCouleur2(JButton b){
		this.b = b;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		bdDialogueCouleur jop = new bdDialogueCouleur(MainClient.fenetre, "Choisissez la couleur de la Ligne", true, b.getBackground());
		Color couleur = jop.afficher();
		if(couleur != null)
			b.setBackground(couleur);
	}
	
}