package client;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class BdDialogueEpaisseur extends JDialog {
  private JLabel epaisseurLabel;
  
  private SpinnerModel model1;
  private JSpinner epaisseur;
  private ImageExemple exemple;
  Object donnees;

  public BdDialogueEpaisseur(JFrame parent, String title, boolean modal){
	super(parent, title, modal);
	this.setSize(270, 170);
	this.setLocationRelativeTo(null);
	this.setResizable(false);
	this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	this.initComponent();
	}
 
 public float afficher(){
	this.setVisible(true);
	return (float) donnees;
	}

  private void initComponent(){
	
	donnees = MainClient.fenetre.getEpaisseur();
	model1  = new SpinnerNumberModel(MainClient.fenetre.getEpaisseur(), 0.5f, 20f, 0.1f);
	epaisseur = new JSpinner(model1);
	epaisseur.addChangeListener(new ChangeListener(){
		  public void stateChanged(ChangeEvent evt) {
		    JSpinner spinner = (JSpinner) evt.getSource();
		    exemple.changer(spinner.getValue());
		  }
		});
	
	JPanel panSpinner = new JPanel();
	panSpinner.setBackground(Color.white);
	panSpinner.setPreferredSize(new Dimension(220, 50));
	epaisseur.setPreferredSize(new Dimension(100, 20));
	epaisseurLabel = new JLabel("Epaisseur :");
	panSpinner.add(epaisseurLabel);
	panSpinner.add(epaisseur);
	
	exemple = new ImageExemple(epaisseur.getValue());
	exemple.setBackground(Color.white);
	
	JPanel content = new JPanel(new GridLayout(2,1));
	content.setBackground(Color.white);
	content.setBorder(BorderFactory.createTitledBorder("Epaisseur des formes"));
	content.add(panSpinner);
	content.add(exemple);
	
	JPanel control = new JPanel();
	control.setBackground(Color.white);
	JButton okBouton = new JButton("OK");
	
	okBouton.addActionListener(new ActionListener(){
	public void actionPerformed(ActionEvent arg0) {
		double temp = (double) epaisseur.getValue();
		donnees = (float) temp;
		setVisible(false);
		}
	});

	JButton cancelBouton = new JButton("Annuler");
	cancelBouton.addActionListener(new ActionListener(){
	 public void actionPerformed(ActionEvent arg0) {
		 donnees = MainClient.fenetre.getEpaisseur();
		 setVisible(false);
	 }
	});

	control.add(okBouton);
	control.add(cancelBouton);

	this.getContentPane().add(content, BorderLayout.CENTER);
	this.getContentPane().add(control, BorderLayout.SOUTH);
	}
}

class ImageExemple extends JPanel {
	float epaisseur;
	
    public ImageExemple(float epaisseur) {
    	super();
    	this.setSize(270, 30);
    	this.epaisseur = epaisseur;
    }

    public void changer(Object value) {
    	double temp = (double) value;
    	this.epaisseur = (float) temp;
    	repaint();
	}

	public ImageExemple(Object value) {
    	double temp = (double) value;
    	this.epaisseur = (float) temp;
    }

	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.black);
		g2d.setStroke(new BasicStroke(epaisseur));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawLine(40, 10, this.getWidth()-40, 10);
    }
}
