package ProgrammeClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class BdDialogueConnexion extends JDialog {
  private JLabel nomLabel, adresseLabel;
  private JTextField nom, adresse;
  String [] donnees = new String[2];

  public BdDialogueConnexion(JFrame parent, String title, boolean modal){
	super(parent, title, modal);
	this.setSize(270, 200);
	this.setLocationRelativeTo(null);
	this.setResizable(false);
	this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	this.initComponent();
	}
 
 public String[] afficher(){
	this.setVisible(true); // INSTRUCTION BLOQUANTE
	return donnees;
	}

  private void initComponent(){
	
	JPanel panNom = new JPanel();
	panNom.setBackground(Color.white);
	panNom.setPreferredSize(new Dimension(220, 60));
	nom = (MainClient.client.getNom() != null) ? new JTextField(MainClient.client.getNom()) : new JTextField();
	nom.setPreferredSize(new Dimension(100, 25));
	panNom.setBorder(BorderFactory.createTitledBorder("Nom de Dessinateur"));
	nomLabel = new JLabel("Saisir un nom :");
	panNom.add(nomLabel);
	panNom.add(nom);

	JPanel panAdresse = new JPanel();
	panAdresse.setBackground(Color.white);
	panAdresse.setPreferredSize(new Dimension(220, 60));
	adresse = new JTextField();
	adresse.setPreferredSize(new Dimension(100, 25));
	panAdresse.setBorder(BorderFactory.createTitledBorder("Adresse du serveur"));
	adresseLabel = new JLabel("Saisir l'adresse :");
	panAdresse.add(adresseLabel);
	panAdresse.add(adresse);

	JPanel content = new JPanel();
	content.setBackground(Color.white);
	content.add(panNom);
	content.add(panAdresse);

	JPanel control = new JPanel();
	control.setBackground(Color.white);
	JButton okBouton = new JButton("OK");
	
	okBouton.addActionListener(new ActionListener(){
	public void actionPerformed(ActionEvent arg0) {
		donnees[0] = adresse.getText();
		donnees[1] = nom.getText();
		if(donnees[0] == null || donnees[0].equals("") || donnees[1] == null || donnees[1].equals(""))
			donnees = null;
		setVisible(false);
		}
	});

	JButton cancelBouton = new JButton("Annuler");
	cancelBouton.addActionListener(new ActionListener(){
	 public void actionPerformed(ActionEvent arg0) {
		 donnees = null;
		 setVisible(false);
	 }
	});

	control.add(okBouton);
	control.add(cancelBouton);

	this.getContentPane().add(content, BorderLayout.CENTER);
	this.getContentPane().add(control, BorderLayout.SOUTH);
	}
}