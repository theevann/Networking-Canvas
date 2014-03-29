package ProgrammeClient;
import java.awt.Checkbox;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;


@SuppressWarnings("serial")
public class OngletDessinateur extends JScrollPane {

	//List<Checkbox> listeCheckBox = Collections.synchronizedList(new ArrayList<Checkbox>());
	private JTable tableau;
	private ModeleTableauDessinateur modele;
	private static String  title[] = {"Connecté", "Pseudo", "Voir"};
	private Object[][] data = new Object[MainClient.listeDessinateur.size()][3];
	public static OngletDessinateur ongletActuel;
	
	public OngletDessinateur(JTable tableau) {
		super(tableau);
		this.tableau = tableau;
		this.modele = new ModeleTableauDessinateur(data, title);
		this.tableau.setDefaultRenderer(Icon.class,new IconeCellRenderer());
		this.tableau.setModel(modele);
		
		this.setVisible(true);
		ongletActuel = this;
	}
	
	public void ajouterDessinateur(Dessinateur dessinateur){
		Object [] dataDessinateur = {"","",""};
		dataDessinateur[0] = dessinateur.isConnecte();
		dataDessinateur[1] = dessinateur.getNom();
		dataDessinateur[2] = dessinateur.getVoir();//new JCheckBox("", dessinateur.getVoir());
		modele.ajouterDessinateur(dataDessinateur);
	}
	
	public void connecterDessinateur(Dessinateur dessinateur){
		modele.connecterDessinateur(dessinateur);
	} 
	
	public void deconnecterDessinateur(Dessinateur dessinateur){
		modele.deconnecterDessinateur(dessinateur);
	}

	public void synchroniser() {
		modele.synchroniserDessinateur();		
	}

	public JTable getTableau() {
		return tableau;
	}
	
}
