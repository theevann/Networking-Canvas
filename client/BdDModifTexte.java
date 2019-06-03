package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import forme.Feutre;
import forme.LigneDroite;
import forme.Texte;

@SuppressWarnings("serial")
public class BdDModifTexte extends JDialog {
	private JLabel couleurLabel, policeLabel, positionLabel;
	private JButton bCouleur = new JButton(), bPolice = new JButton("Hello World !"), cancelBouton, okBouton;
	private Texte texte = null;
  
	private JSpinner positionX, positionY;
	private JTextField txt;
	private JLabel nomLabel;
	
  public BdDModifTexte(JFrame parent, Texte t){
	super(parent, "Modifier le texte", true);
	this.texte = t.clone();
	this.setSize(270, 360);
	this.setLocationRelativeTo(null);
	this.setResizable(false);
	//this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	this.initComponent();
	}
 
 public Texte afficher(){
	this.setVisible(true);
	return texte;
	}

  private void initComponent(){

	JPanel panCouleur = new JPanel();
	panCouleur.setBackground(Color.white);
	panCouleur.setPreferredSize(new Dimension(220, 60));
	bCouleur.setPreferredSize(new Dimension(100, 25));
	bCouleur.setBackground(texte.getCouleur());
	bCouleur.addActionListener(new EcouteurCouleur5(bCouleur));
	couleurLabel = new JLabel("Couleur :");
	panCouleur.setBorder(BorderFactory.createTitledBorder("Couleur du texte"));
	panCouleur.add(couleurLabel);
	panCouleur.add(bCouleur);
	
	JPanel panPolice = new JPanel();
	panPolice.setBackground(Color.white);
	panPolice.setPreferredSize(new Dimension(220, 60));
	bPolice.setPreferredSize(new Dimension(100, 25));
	bPolice.setFont(texte.getPolice());
	bPolice.addActionListener(new EcouteurPolice(bPolice));
	policeLabel = new JLabel("Police :");
	panPolice.setBorder(BorderFactory.createTitledBorder("Police du Texte"));
	panPolice.add(policeLabel);
	panPolice.add(bPolice);
	
	JPanel panTexte = new JPanel();
	panTexte.setBackground(Color.white);
	panTexte.setPreferredSize(new Dimension(220, 60));
	txt = new JTextField(texte.getTexte());
	txt.setPreferredSize(new Dimension(100, 25));
	panTexte.setBorder(BorderFactory.createTitledBorder("Texte"));
	nomLabel = new JLabel("Texte :");
	panTexte.add(nomLabel);
	panTexte.add(txt);
	
	JPanel panPosition = new JPanel();
	JPanel panPosition2 = new JPanel(new GridLayout(2, 2));
	panPosition.setBackground(Color.white);
	panPosition2.setBackground(Color.white);
	panPosition.setPreferredSize(new Dimension(220, 80));
	panPosition2.setPreferredSize(new Dimension(100, 50));
	SpinnerNumberModel model2  = new SpinnerNumberModel(texte.getPoint().x, -10000, 10000, 5);
	SpinnerNumberModel model3  = new SpinnerNumberModel(texte.getPoint().y, -10000, 10000, 5);
	positionX = new JSpinner(model2);
	positionY = new JSpinner(model3);
	positionX.setPreferredSize(new Dimension(100, 20));
	positionY.setPreferredSize(new Dimension(100, 20));
	positionLabel = new JLabel("Position ");
	panPosition.setBorder(BorderFactory.createTitledBorder("Position du texte"));
	panPosition2.add(new JLabel("sur X :"));
	panPosition2.add(positionX);
	panPosition2.add(new JLabel("sur Y :"));
	panPosition2.add(positionY);
	panPosition.add(positionLabel);
	panPosition.add(panPosition2);
	
	JPanel content = new JPanel();
	content.setBackground(Color.white);
	content.add(panCouleur);
	content.add(panPolice);
	content.add(panTexte);
	content.add(panPosition);
	
	JPanel control = new JPanel();
	control.setBackground(Color.white);
	okBouton = new JButton("OK");
	
	okBouton.addActionListener(new ActionListener(){
	public void actionPerformed(ActionEvent arg0) {
		texte.setCouleur(bCouleur.getBackground());
		texte.setPolice(bPolice.getFont());
		texte.setTexte(txt.getText());
		texte.deplacer(new Dimension(-texte.getPoint().x + (int)positionX.getValue(), -texte.getPoint().y + (int)positionY.getValue()));
		setVisible(false);
		}
	});

	cancelBouton = new JButton("Annuler");
	cancelBouton.addActionListener(new ActionListener(){
	 public void actionPerformed(ActionEvent arg0) {
		 texte = null;
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

class EcouteurCouleur5 implements ActionListener{
	
	private JButton b;

	public EcouteurCouleur5(JButton b){
		this.b = b;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		bdDialogueCouleur jop = new bdDialogueCouleur(MainClient.fenetre, "Choisissez la couleur du texte", true, b.getBackground());
		Color couleur = jop.afficher();
		if(couleur != null)
			b.setBackground(couleur);
	}
	
}

class EcouteurPolice implements ActionListener{
	
	private JButton b;

	public EcouteurPolice(JButton b){
		this.b = b;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		BdDialoguePolice jop = new BdDialoguePolice(MainClient.fenetre, "Choisissez la couleur du texte", true, b.getFont());
		Font police = jop.afficher();
		if(police != null){
			//police = police.deriveFont(15f);
			b.setFont(police);
		}
	}
	
}