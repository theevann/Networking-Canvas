package client;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class bdDialogueCouleur extends JDialog implements ActionListener, KeyListener {

	Color couleur = null;
	int champRouge = 0;
	int champVert = 0;
	int champBleu = 0;
	private Button augmenterRouge;
	private Button diminuerRouge;
	private Button augmenterVert;
	private Button diminuerVert;
	private Button augmenterBleu;
	private Button diminuerBleu;
	private Button assombrir;
	private Button eclaircir;
	private TextField rouge;
	private TextField vert;
	private TextField bleu;
	private Canvas cnv;

	private Panel pan1;
	private Panel pan2;
	private Panel pan3;
	private Panel pan4;

	boolean ok = false;

	public bdDialogueCouleur (JFrame parent, String title, boolean modal){
		super(parent, title, modal);
		this.setSize(250, 200);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		this.initComponent();
	}

	public bdDialogueCouleur (JFrame parent, String title, boolean modal, Color c){
		super(parent, title, modal);
		this.couleur = c;
		this.setSize(250, 200);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		this.initComponent();
	}

	private void initComponent() {
		ok = false;
		setLayout(new BorderLayout());
		if(couleur == null)
			couleur = MainClient.fenetre.getCanevas().getCouleur();

		augmenterRouge = new Button("R+");
		diminuerRouge = new Button("R-");
		augmenterVert = new Button("V+");
		diminuerVert = new Button("V-");
		augmenterBleu = new Button("B+");
		diminuerBleu = new Button("B-");
		assombrir = new Button("+ Sombre");
		eclaircir = new Button("+ Clair");

		rouge = new TextField(couleur.getRed() + "");
		vert = new TextField(couleur.getGreen() + "");
		bleu = new TextField(couleur.getBlue() + "");

		cnv = new Canvas();
		cnv.setBackground(couleur);

		pan1 = new Panel(new GridLayout(1,2));
		pan2 = new Panel(new GridLayout(3,2));
		pan3 = new Panel(new GridLayout(3,1));
		pan4 = new Panel(new GridLayout(2,2));

		pan1.add(assombrir);
		pan1.add(eclaircir);

		pan2.add(augmenterRouge);
		pan2.add(diminuerRouge);
		pan2.add(augmenterVert);
		pan2.add(diminuerVert);
		pan2.add(augmenterBleu);
		pan2.add(diminuerBleu);

		pan3.add(rouge);
		pan3.add(vert);
		pan3.add(bleu);


		pan4.add(cnv);
		pan4.add(pan1);
		pan4.add(pan2);
		pan4.add(pan3);

		augmenterRouge.addActionListener(this);
		diminuerRouge.addActionListener(this);
		augmenterVert.addActionListener(this);
		diminuerVert.addActionListener(this);
		augmenterBleu.addActionListener(this);
		diminuerBleu.addActionListener(this);
		assombrir.addActionListener(this);
		eclaircir.addActionListener(this);
		rouge.addKeyListener(this);
		vert.addKeyListener(this);
		bleu.addKeyListener(this);

		JPanel control = new JPanel();
		control.setBackground(Color.white);
		JButton okBouton = new JButton("OK");

		okBouton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				ok = true;
				setVisible(false);
				}
			});

			JButton cancelBouton = new JButton("Annuler");
			cancelBouton.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent arg0) {
				 couleur = null;
				 setVisible(false);
			 }
			});

			control.add(okBouton);
			control.add(cancelBouton);
			add(control, BorderLayout.SOUTH);
			add(pan4);
	}
	 public Color afficher(){
			this.setVisible(true);
			if(ok)
				return couleur;
			else
				return null;
		}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == augmenterRouge && couleur.getRed() < 255){
			couleur = new Color(couleur.getRed() + 1,couleur.getGreen(),couleur.getBlue());
		}
		else if(evt.getSource() == diminuerRouge && couleur.getRed() > 0){
			couleur = new Color(couleur.getRed() - 1,couleur.getGreen(),couleur.getBlue());
		}
		else if(evt.getSource() == augmenterVert && couleur.getGreen() < 255){
			couleur = new Color(couleur.getRed(),couleur.getGreen() + 1,couleur.getBlue());
		}
		else if(evt.getSource() == diminuerVert && couleur.getGreen() > 0){
			couleur = new Color(couleur.getRed(),couleur.getGreen() - 1,couleur.getBlue());
		}
		else if(evt.getSource() == augmenterBleu && couleur.getBlue() < 255){
			couleur = new Color(couleur.getRed(),couleur.getGreen(),couleur.getBlue() + 1);
		}
		else if(evt.getSource() == diminuerBleu && couleur.getBlue() > 0){
			couleur = new Color(couleur.getRed(),couleur.getGreen(),couleur.getBlue() - 1);
		}
		else if(evt.getSource() == assombrir){
			couleur = couleur.darker();
		}
		else if(evt.getSource() == eclaircir){
			couleur = couleur.brighter();
		}

		cnv.setBackground(couleur);
		rouge.setText(couleur.getRed() + "");
		vert.setText(couleur.getGreen() + "");
		bleu.setText(couleur.getBlue() + "");
		repaint();

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		TextField champ = (TextField) e.getSource();
		String str = champ.getText();
		/*
		if(str.length() == 0){
			//Mettre ici la couleur concernee a 0 ?
		}*/

		if(str.length() > 0 && Integer.parseInt(str) < 256){

			if(champ == rouge )
				couleur = new Color(Integer.parseInt(str),couleur.getGreen(),couleur.getBlue());
			else if(champ == vert)
				couleur = new Color(couleur.getRed(),Integer.parseInt(str),couleur.getBlue());
			else if(champ == bleu)
				couleur = new Color(couleur.getRed(),couleur.getGreen(),Integer.parseInt(str));

			cnv.setBackground(couleur);
			rouge.setText(couleur.getRed() + "");
			vert.setText(couleur.getGreen() + "");
			bleu.setText(couleur.getBlue() + "");
		}
		else if(str.length() > 0 && Integer.parseInt(str) > 255){
			rouge.setText(couleur.getRed() + "");
			vert.setText(couleur.getGreen() + "");
			bleu.setText(couleur.getBlue() + "");
		}

		repaint();
		champ.setCaretPosition(str.length());
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
