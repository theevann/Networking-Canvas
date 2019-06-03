package client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class BdDialoguePolice extends JDialog implements ActionListener {
public ImageExemple2 exemple;
Font donnees;

private String[] sizes;

private String[] styles;

private static String[] fonts;

private VerticalBox font_choice;

private VerticalBox style_choice;

private JButton cancel;

private Box prov_valid;

private Box global;

private Box settings;

private Box valid;

private AbstractButton ok;

private VerticalBox size_choice;

Font police = null;
	
	static{
		fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	}

  public BdDialoguePolice(JFrame parent, String title, boolean modal){
	super(parent, title, modal);
	this.donnees = MainClient.fenetre.getPolice();
	this.setSize(400, 350);
	this.setLocationRelativeTo(null);
	this.setResizable(false);
	this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	this.initComponent();
	}
  
  public BdDialoguePolice(JFrame parent, String title, boolean modal, Font pol){
		super(parent, title, modal);
		this.donnees = MainClient.fenetre.getPolice();
		this.police = pol;
		this.setSize(400, 350);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		this.initComponent();
		}
 
 public Font afficher(){
	this.setVisible(true);
	return donnees;
	}

  private void initComponent(){
	
	  	if(police == null)
	  		police = MainClient.fenetre.getPolice();
	  	
		sizes = new String[]{"6","8","10","11","12","14","16","18","20","22","24","26","28","30","32","34","36","40","44","48"};
		styles = new String[]{"Normal", "Gras", "Italique", "Gras et Italique"};
		 
		//Creation des box JTextField + JList permettant de choisir
		font_choice = new VerticalBox(fonts, false, this);
		style_choice = new VerticalBox(styles, false, this);
		size_choice = new VerticalBox(sizes, true, this);
		
		JButton okBouton = new JButton("OK");
		okBouton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				getter();
				donnees = police;
				//System.out.println(police.getName() + "  " + police.getSize());
				setVisible(false);
				}
			});

		JButton cancelBouton = new JButton("Annuler");
		cancelBouton.addActionListener(new ActionListener(){
		 public void actionPerformed(ActionEvent arg0) {
			 donnees = MainClient.fenetre.getPolice();
			 setVisible(false);
		 }
		 });
		
		//Box contenant les boutons
		prov_valid = Box.createHorizontalBox();
		prov_valid.add(cancelBouton);
		prov_valid.add(Box.createHorizontalStrut(5));
		prov_valid.add(okBouton);
		 
		
		
		global = Box.createVerticalBox();
		 
		//Box gerant la mise en page des boutons
		valid = Box.createHorizontalBox();
		valid.add(Box.createGlue());
		valid.add(prov_valid);
		 
		//Box gerant la mise en page des listes de choix
		settings = Box.createHorizontalBox();
		settings.add(font_choice);
		settings.add(Box.createHorizontalStrut(5));
		settings.add(style_choice);
		settings.add(Box.createHorizontalStrut(5));
		settings.add(size_choice);
		 
		exemple = new ImageExemple2(police);
		exemple.setPreferredSize(new Dimension(400, 70));
		exemple.setBackground(Color.white);
		exemple.setBorder(BorderFactory.createTitledBorder("Exemple"));
		
		size_choice.result.addActionListener(this);
		font_choice.result.addActionListener(this);
		
		/*
	JPanel content = new JPanel(new GridLayout(2,1));
	content.setBackground(Color.white);
	content.setBorder(BorderFactory.createTitledBorder("Epaisseur des formes"));
	content.add(panSpinner);
	content.add(exemple);
	*/
	
	//Box contenant l'integralite des composants
	global.add(settings);
	global.add(Box.createVerticalStrut(5));
	global.add(exemple);
	global.add(Box.createVerticalStrut(5));
	global.add(valid);
	
	setter(police);
	this.getContentPane().add(global, BorderLayout.CENTER);
	}
  
	public void setter(Font f){
		font_choice.result.setText(f.getName());
		size_choice.result.setText(String.valueOf(f.getSize()));
		int prov = f.getStyle();
		String prov2 = "";
		if(prov==Font.PLAIN){prov2 = "Normal";System.out.println("Normal");}
		else if(prov==Font.BOLD){prov2 = "Gras";System.out.println("Gras");}
		else if(prov==Font.ITALIC){prov2 = "Italique";}
		else if(prov==Font.BOLD+Font.ITALIC){prov2 = "Gras et Italique";}
		style_choice.result.setText(prov2);
	}

	/**
* Retourne la police de caracteres choisie par l'utilisateur
* @return L'objet Font contenant les choix de l'utilisateur
*/
public void getter(){
	String prov = style_choice.result.getText();
	int styl = 0;
	
	if(prov.equals("Normal")){
		styl = Font.PLAIN;
	}
	else if(prov.equals("Gras")){
		styl = Font.BOLD;
	}
	else if(prov.equals("Italique")){
		styl = Font.ITALIC;
	}
	else if(prov.equals("Gras et Italique")){
		styl = Font.BOLD+Font.ITALIC;
	}
	if(size_choice.result.getText().equals("")==false){
		police = (new Font(font_choice.result.getText(), styl, Integer.parseInt(size_choice.result.getText())));
	}
	else{
		JOptionPane.showMessageDialog(null, "Vous n'avez pas sp\u00e9cifi\u00e9 de taille !", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
	}
}

	@Override
	public void actionPerformed(ActionEvent e) {
		getter();
		exemple.changer(police);
	}
	
}

class ImageExemple2 extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Font police;
	
    public ImageExemple2(Font police) {
    	super();
    	this.setSize(270, 70);
    	this.police = police;
    }

	public void changer(Font police2) {
		police = police2;
		repaint();
	}

	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(MainClient.fenetre.getCanevas().getCouleur());
		g2d.setFont(police);
		g2d.drawString("Hello World", 10, this.getHeight()-15); 
	}
}

@SuppressWarnings("serial")
class VerticalBox extends Box implements ListSelectionListener{
/**
* Constructeur de la classe VerticalBox
* @param content La liste de choix disponibles
* @param is_editable Le caractere editable de la liste
* @param parent Le panneau appelant
*/
	public VerticalBox(String[] content, boolean is_editable, JDialog parent){
		super(1);
		this.parent = parent;
		result = new JTextField();
		result.setEditable(is_editable);
		choix = new JList<String>(content);
		choix.setSelectionMode(0);
		choix.setVisibleRowCount(10);
		choix.addListSelectionListener(this);
		defil = new JScrollPane(choix);
	 
		add(result);
		add(Box.createVerticalStrut(3));
		add(defil);
	}
 
/**
* L'ecouteur associe a la liste capturant la selection de ses elements
*/
	public void valueChanged(ListSelectionEvent e){
		if(!e.getValueIsAdjusting()){
			result.setText((String)choix.getSelectedValue());
			((BdDialoguePolice) parent).getter();
			((BdDialoguePolice) parent).exemple.changer(((BdDialoguePolice) parent).police);
		}
	}
	JTextField result; private JList<String> choix; private JScrollPane defil; private JDialog parent;
}
