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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import forme.Rectangle;

@SuppressWarnings("serial")
public class BdDModifRect extends JDialog {
	private JLabel couleurLabel, radioLabel, epaisseurLabel, positionLabel;
	private JButton bCouleur = new JButton(), cancelBouton, okBouton;
	private Rectangle rectangle = null;
  
	private JSpinner positionX, positionY, epaisseur, hauteur, longueur;
	private JRadioButton jr1 = new JRadioButton("Pave");
	private JRadioButton jr2 = new JRadioButton("Rectangle");
	private ButtonGroup bg = new ButtonGroup();
	
  public BdDModifRect(JFrame parent, Rectangle r){
	super(parent, "Modifier le Rectangle", true);
	this.rectangle = r.clone();
	this.setSize(270, 440);
	this.setLocationRelativeTo(null);
	this.setResizable(false);
	//this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	this.initComponent();
	}
 
 public Rectangle afficher(){
	this.setVisible(true);
	return rectangle;
	}

  private void initComponent(){

	JPanel panCouleur = new JPanel();
	panCouleur.setBackground(Color.white);
	panCouleur.setPreferredSize(new Dimension(220, 60));
	bCouleur.setPreferredSize(new Dimension(100, 25));
	bCouleur.setBackground(rectangle.getCouleur());
	bCouleur.addActionListener(new EcouteurCouleur(bCouleur));
	couleurLabel = new JLabel("Couleur :");
	panCouleur.setBorder(BorderFactory.createTitledBorder("Couleur du Rectangle"));
	panCouleur.add(couleurLabel);
	panCouleur.add(bCouleur);
	
	JPanel panEpaisseur = new JPanel();
	panEpaisseur.setBackground(Color.white);
	panEpaisseur.setPreferredSize(new Dimension(220, 60));
	SpinnerNumberModel model1  = new SpinnerNumberModel(rectangle.getTaille(), 0.5f, 20f, 0.1f);
	epaisseur = new JSpinner(model1);
	epaisseur.setPreferredSize(new Dimension(100, 20));
	epaisseurLabel = new JLabel("Epaisseur :");
	panEpaisseur.setBorder(BorderFactory.createTitledBorder("Epaisseur du Rectangle"));
	panEpaisseur.add(epaisseurLabel);
	panEpaisseur.add(epaisseur);

	JPanel panRadio = new JPanel();
	JPanel panRadio2 = new JPanel(new GridLayout(2, 1));
	panRadio.setBackground(Color.white);
	panRadio2.setBackground(Color.white);
	panRadio.setPreferredSize(new Dimension(220, 60));
	panRadio2.setPreferredSize(new Dimension(100, 30));
	panRadio2.add(jr1);
	panRadio2.add(jr2);
	jr1.setSelected(rectangle.isPlein());
	jr2.setSelected(!rectangle.isPlein());
	jr1.setBackground(Color.white);
	jr2.setBackground(Color.white);
	bg.add(jr1);
    bg.add(jr2);
	radioLabel = new JLabel("Remplissage :");
	panRadio.setBorder(BorderFactory.createTitledBorder("Remplissage du Rectangle"));
	panRadio.add(radioLabel);
	panRadio.add(panRadio2);
	
	JPanel panPosition = new JPanel();
	JPanel panPosition2 = new JPanel(new GridLayout(2, 2));
	panPosition.setBackground(Color.white);
	panPosition2.setBackground(Color.white);
	panPosition.setPreferredSize(new Dimension(220, 80));
	panPosition2.setPreferredSize(new Dimension(100, 40));
	SpinnerNumberModel model2  = new SpinnerNumberModel(rectangle.getPoint1().x, -10000, 10000, 5);
	SpinnerNumberModel model3  = new SpinnerNumberModel(rectangle.getPoint1().y, -10000, 10000, 5);
	positionX = new JSpinner(model2);
	positionY = new JSpinner(model3);
	positionX.setPreferredSize(new Dimension(100, 20));
	positionY.setPreferredSize(new Dimension(100, 20));
	positionLabel = new JLabel("Position   ");
	panPosition.setBorder(BorderFactory.createTitledBorder("Position du Rectangle"));
	panPosition2.add(new JLabel("sur X :"));
	panPosition2.add(positionX);
	panPosition2.add(new JLabel("sur Y :"));
	panPosition2.add(positionY);
	panPosition.add(positionLabel);
	panPosition.add(panPosition2);
	
	
	JPanel panDimension = new JPanel();
	JPanel panDimension2 = new JPanel(new GridLayout(2, 2));
	panDimension.setBackground(Color.white);
	panDimension2.setBackground(Color.white);
	panDimension.setPreferredSize(new Dimension(220, 80));
	panDimension2.setPreferredSize(new Dimension(150, 40));
	SpinnerNumberModel model4  = new SpinnerNumberModel(rectangle.getPoint2().x-rectangle.getPoint1().x, 0, 10000, 5);
	SpinnerNumberModel model5  = new SpinnerNumberModel(rectangle.getPoint2().y-rectangle.getPoint1().y, 0, 10000, 5);
	longueur = new JSpinner(model4);
	hauteur = new JSpinner(model5);
	longueur.setPreferredSize(new Dimension(100, 20));
	hauteur.setPreferredSize(new Dimension(100, 20));
	panDimension.setBorder(BorderFactory.createTitledBorder("Dimension du Rectangle"));
	panDimension2.add(new JLabel("Longueur"));
	panDimension2.add(longueur);
	panDimension2.add(new JLabel("Hauteur"));
	panDimension2.add(hauteur);
	panDimension.add(panDimension2);
	
	JPanel content = new JPanel();
	content.setBackground(Color.white);
	content.add(panCouleur);
	content.add(panEpaisseur);
	content.add(panRadio);
	content.add(panPosition);
	content.add(panDimension);
	
	JPanel control = new JPanel();
	control.setBackground(Color.white);
	okBouton = new JButton("OK");
	
	okBouton.addActionListener(new ActionListener(){
	public void actionPerformed(ActionEvent arg0) {
		/*donnees[0] = adresse.getText();
		donnees[1] = nom.getText();*/
		rectangle.setCouleur(bCouleur.getBackground());
		rectangle.setTaille((float) ((double) epaisseur.getValue()));
		rectangle.setPlein(jr1.isSelected());
		rectangle.setPoint1(new Point((int) positionX.getValue(), (int) positionY.getValue()));
		rectangle.setPoint2(new Point((int) positionX.getValue() + (int) longueur.getValue(), (int) positionY.getValue() + (int) hauteur.getValue()));
		setVisible(false);
		}
	});

	cancelBouton = new JButton("Annuler");
	cancelBouton.addActionListener(new ActionListener(){
	 public void actionPerformed(ActionEvent arg0) {
		 rectangle = null;
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

class EcouteurCouleur implements ActionListener{
	
	private JButton b;

	public EcouteurCouleur(JButton b){
		this.b = b;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		bdDialogueCouleur jop = new bdDialogueCouleur(MainClient.fenetre, "Choisissez la couleur du Rectangle", true, b.getBackground());
		Color couleur = jop.afficher();
		if(couleur != null)
			b.setBackground(couleur);
	}
	
}