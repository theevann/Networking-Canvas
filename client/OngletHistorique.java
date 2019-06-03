package client;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.TableRowSorter;

import forme.Cercle;
import forme.Feutre;
import forme.LigneDroite;
import forme.Motif;
import forme.Rectangle;
import forme.Texte;
import forme.TypeMotif;


@SuppressWarnings("serial")
public class OngletHistorique extends JPanel implements ActionListener, MouseListener{

	private JScrollPane panneauHaut;
	private JPanel panneauBas = new JPanel(new GridLayout(3,1));
	private JPanel panneauBasBouton1 = new JPanel(new FlowLayout());
	private JPanel panneauBasBouton2 = new JPanel(new FlowLayout());
	private JPanel panneauBasFiltre = new JPanel(new FlowLayout());
	private JTable tableau;
	private ModeleTableauHistorique modele;
	private static String  title[] = {"Ne","Nom", "Type", "Couleur", "Visible"};
	private Object[][] data = new Object[1][5];
	public static OngletHistorique ongletActuel;
	private JLabel label = new JLabel("Filtre : ");
	private TextField filtre;
	private TableRowSorter<ModeleTableauHistorique> sorter;
	private JButton supprimer = new JButton("Supprimer");
	private JButton modifier = new JButton("Modifier");
	private JButton afficher = new JButton("Afficher");
	private JButton masquer = new JButton("Masquer");

	public OngletHistorique(JTable tableau2) {
		this.modele = new ModeleTableauHistorique(data, title);
		this.sorter = new TableRowSorter<ModeleTableauHistorique>(modele);
		this.tableau = new JTable(modele);
		this.tableau.setPreferredScrollableViewportSize(new Dimension(240, 350));
		this.tableau.setDefaultRenderer(Color.class, new ColorCellRenderer());
		this.tableau.setRowSorter(sorter);
		this.panneauHaut = new JScrollPane(this.tableau);
		
		this.tableau.getColumnModel().getColumn(0).setPreferredWidth(30);
		this.tableau.getColumnModel().getColumn(3).setPreferredWidth(30);
		
		this.tableau.addMouseListener(this);
		
		filtre = new TextField(SwingConstants.TRAILING);
		filtre.addTextListener(new EcouteurTexte(this));
		label.setLabelFor(filtre);
		panneauBasFiltre.add(label);
		panneauBasFiltre.add(filtre);
		
		
		supprimer.addActionListener(this);
		modifier.addActionListener(this);
		panneauBasBouton1.add(supprimer);
		panneauBasBouton1.add(modifier);
		
		masquer.addActionListener(this);
		afficher.addActionListener(this);
		panneauBasBouton2.add(afficher);
		panneauBasBouton2.add(masquer);
		
		panneauBas.add(panneauBasBouton2);
		panneauBas.add(panneauBasBouton1);
		panneauBas.add(panneauBasFiltre);

		this.add(panneauHaut);
		this.add(panneauBas);
		
		this.setVisible(true);
		ongletActuel = this;
	}
	
	public void ajouterMotif(Motif motif){
		int [] tableauElmtSelect = new int [tableau.getSelectedRowCount()];
		for(int i = 0 ; i < tableau.getSelectedRowCount() ; i++)
			tableauElmtSelect[i] = tableau.convertRowIndexToModel(tableau.getSelectedRows()[i]);
		Arrays.sort(tableauElmtSelect);
		
		Object [] dataMotif = {"","","","",""};
		dataMotif[0] = modele.getRowCount()+1;
		dataMotif[1] = (MainClient.listeDessinateur.get(motif.getNumUser())).getNom();
		dataMotif[2] = motif.getType();
		dataMotif[3] = motif.getCouleur();
		dataMotif[4] = !motif.isMasque();
		modele.ajouterMotif(dataMotif);
		
		for (int i : tableauElmtSelect){
			i = tableau.convertRowIndexToView(i);
			tableau.getSelectionModel().addSelectionInterval(i,i);
		}
	}

	public void synchroniser() {
		int [] tableauElmtSelect = new int [tableau.getSelectedRowCount()];
		for(int i = 0 ; i < tableau.getSelectedRowCount() ; i++)
			tableauElmtSelect[i] = tableau.convertRowIndexToModel(tableau.getSelectedRows()[i]);
		Arrays.sort(tableauElmtSelect);
		
		modele.synchroniserMotif();
		for (int i : tableauElmtSelect)
			tableau.getSelectionModel().addSelectionInterval(i,i); 
	}
	
	public void synchroniser(int [] num) {
		//1. Regarder si l'un des elements selectionnes est supprime et le suppimer.
		//2. Enlever une unite des qu'ils sont au dessus d'un element supprime

		ArrayList<Integer> listeElmtSelect = new ArrayList<Integer>();
		ArrayList<Integer> listeAAjouter = new ArrayList<Integer>();
		ArrayList<Integer> listeASupprimer = new ArrayList<Integer>();
		
		for(int i = 0 ; i < tableau.getSelectedRowCount() ; i++)
			listeElmtSelect.add(tableau.convertRowIndexToModel(tableau.getSelectedRows()[i]));
		Collections.sort(listeElmtSelect);
		
		// On enleve de la liste des elements selectionnes ceux qui ont ete supprimes
		for(int i = 0 ; i < num.length ; i++){
			if(listeElmtSelect.contains(num[i])){
				listeElmtSelect.remove((Object)num[i]);
			}
		}
		
		//On calcule la nouvelle position des elements e selectionner
		
		for(int j : listeElmtSelect){
			int sup = 0;
			for(int i = 0 ; i < num.length ; i++){
				if(num[i] < j){
					//Si l'element supprime num[i] est inferieur a l'element selectionne j (place dans le tableau), alors il faut decaler l'element selectionne vers le bas, donc diminuer j de un
					//Pour cela, on enregistre combien de nombre a supprimer sont inferieur a j en incrementant sup (le decalage e operer) si c'est le cas.
					sup++;
				}
			}
			if(sup > 0){
				//On modifie la valeur de j, mais comme ce n'est pas possible "facilement", on l'enleve et on remet la valeur modifie apres
				//Pour cela, on enregistre qu'il faudra la supprimer dans les deux liste suivantes :
				listeASupprimer.add(j);
				listeAAjouter.add(j-sup);
			}
		}
		//On fait comme explique precedement
		listeElmtSelect.removeAll(listeASupprimer);
		listeElmtSelect.addAll(listeAAjouter);
		//On synchronise enfin !
		modele.synchroniserMotif();
		//et on selectionne les elements non supprimes
		for (int i : listeElmtSelect){
			i = tableau.convertRowIndexToView(i);
			tableau.getSelectionModel().addSelectionInterval(i,i);
		}
	}
	
    public void newFilter() {
        RowFilter<ModeleTableauHistorique, Object> rf = null;
        try {
            rf = RowFilter.regexFilter(filtre.getText(),0,1,2);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == supprimer){
			int [] tableauEnvoye = new int [tableau.getSelectedRowCount()];
			for(int i = 0 ; i < tableau.getSelectedRowCount() ; i++)
				tableauEnvoye[i] = tableau.convertRowIndexToModel(tableau.getSelectedRows()[i]);
			Arrays.sort(tableauEnvoye);
			if(tableauEnvoye.length > 0)
			MainClient.client.envoyer("ul", (tableauEnvoye));
		}
		else if(e.getSource() == masquer){
			int [] tableauAMasque = new int [tableau.getSelectedRowCount()];
			for(int i = 0 ; i < tableau.getSelectedRowCount() ; i++)
				tableauAMasque[i] = tableau.convertRowIndexToModel(tableau.getSelectedRows()[i]);
			Arrays.sort(tableauAMasque);
			if(tableauAMasque.length > 0){
				ImageDessin.masquerMotif(tableauAMasque);
				for(int i : tableauAMasque)
					((ModeleTableauHistorique) tableau.getModel()).setValueVisible(false, i);
			}
		}
		else if(e.getSource() == afficher){
			int [] tableauAAfficher = new int [tableau.getSelectedRowCount()];
			for(int i = 0 ; i < tableau.getSelectedRowCount() ; i++)
				tableauAAfficher[i] = tableau.convertRowIndexToModel(tableau.getSelectedRows()[i]);
			Arrays.sort(tableauAAfficher);
			if(tableauAAfficher.length > 0){
				ImageDessin.afficherMotif(tableauAAfficher);
				for(int i : tableauAAfficher)
					((ModeleTableauHistorique) tableau.getModel()).setValueVisible(true, i);
			}
		}
		else if(e.getSource() == modifier && tableau.getSelectedRowCount() > 0){
			Motif motif = null;
			if(ImageDessin.listeMotif.get(tableau.convertRowIndexToModel(tableau.getSelectedRow())).getType() == TypeMotif.RECTANGLE){
				BdDModifRect jop = new BdDModifRect(MainClient.fenetre, (Rectangle) ImageDessin.listeMotif.get(tableau.convertRowIndexToModel(tableau.getSelectedRow())));
				motif = jop.afficher();
			}
			else if(ImageDessin.listeMotif.get(tableau.convertRowIndexToModel(tableau.getSelectedRow())).getType() == TypeMotif.LIGNEDROITE){
				BdDModifLigne jop = new BdDModifLigne(MainClient.fenetre, (LigneDroite) ImageDessin.listeMotif.get(tableau.convertRowIndexToModel(tableau.getSelectedRow())));
				motif = jop.afficher();
			}
			else if(ImageDessin.listeMotif.get(tableau.convertRowIndexToModel(tableau.getSelectedRow())).getType() == TypeMotif.CERCLE){
				BdDModifCercle jop = new BdDModifCercle(MainClient.fenetre, (Cercle) ImageDessin.listeMotif.get(tableau.convertRowIndexToModel(tableau.getSelectedRow())));
				motif = jop.afficher();
			}
			else if(ImageDessin.listeMotif.get(tableau.convertRowIndexToModel(tableau.getSelectedRow())).getType() == TypeMotif.FEUTRE){
				BdDModifFeutre jop = new BdDModifFeutre(MainClient.fenetre, (Feutre) ImageDessin.listeMotif.get(tableau.convertRowIndexToModel(tableau.getSelectedRow())));
				motif = jop.afficher();
			}
			
			else if(ImageDessin.listeMotif.get(tableau.convertRowIndexToModel(tableau.getSelectedRow())).getType() == TypeMotif.TEXTE){
				BdDModifTexte jop = new BdDModifTexte(MainClient.fenetre, (Texte) ImageDessin.listeMotif.get(tableau.convertRowIndexToModel(tableau.getSelectedRow())));
				motif = jop.afficher();
			}
			
			
			/*int [] tableauEnvoye = new int [tableau.getSelectedRowCount()];
			for(int i = 0 ; i < tableau.getSelectedRowCount() ; i++)
					tableauEnvoye[i] = tableau.convertRowIndexToModel(tableau.getSelectedRows()[i]);*/
			
			if(motif != null){
				int [] tableauEnvoye = new int [1];
				tableauEnvoye[0] = tableau.convertRowIndexToModel(tableau.getSelectedRow());
				
				List<Motif> listeMotifTemp = (new ArrayList<Motif>());
				listeMotifTemp.add(motif);
					
				Arrays.sort(tableauEnvoye);
		    	Object [] aEnvoyer = {tableauEnvoye,listeMotifTemp};
		    	MainClient.client.envoyer("k", aEnvoyer);	
			}
		}
	}

	public JTable getTableau() {
		return tableau;
	}
	
	public void selectionnerLigne(int i){
		i = tableau.convertRowIndexToView(i);
		tableau.getSelectionModel().addSelectionInterval(i,i);
	}
	public void deselectionner(){
		tableau.getSelectionModel().removeSelectionInterval(0, tableau.getRowCount());
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(tableau.getSelectedRowCount() > 0)
			MainClient.fenetre.getCanevas().repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if(tableau.getSelectedRowCount() > 0)
			MainClient.fenetre.getCanevas().repaint();		
	}

	public JButton getSupprimer() {
		return supprimer;
	}

	public TextField getFiltre() {
		return filtre;
	}

	public void modifier(int i, Motif motif) {
		Object [] dataMotif = {"","","","",""};
		dataMotif[0] = i+1;
		dataMotif[1] = (MainClient.listeDessinateur.get(motif.getNumUser())).getNom();
		dataMotif[2] = motif.getType();
		dataMotif[3] = motif.getCouleur();
		dataMotif[4] = !motif.isMasque();
		modele.modifier(i, dataMotif);
	}
	
}

class EcouteurTexte implements TextListener{
	OngletHistorique onglet;
	
	public EcouteurTexte(OngletHistorique ongletHistorique) {
		onglet = ongletHistorique;
	}

	@Override
	public void textValueChanged(TextEvent e) {
		onglet.newFilter();
	}
	
}


