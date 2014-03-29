package ProgrammeClient;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import forme.Motif;


@SuppressWarnings("serial")
public class ModeleTableauHistorique extends AbstractTableModel{
	
	private String[] title;
	private Object[][] data;

	public ModeleTableauHistorique(Object[][] data, String[] title) {
	      this.data = data;
	      this.title = title;
	}

	@Override
	public int getColumnCount() {
		return this.title.length;
	}

	@Override
	public int getRowCount() {
	      return this.data.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		try{
			return this.data[rowIndex][columnIndex];
		}catch(ArrayIndexOutOfBoundsException e){
			//System.err.println("Pbm d'index");
			return new Object();
		}
	}
	
	
	public void setValueAt(Object value, int row, int col) {
		
		if(col == 4){
			int [] row2 = new int [1];
			row2[0] = row;
			if((boolean) value)
				ImageDessin.afficherMotif(row2);
			else
				ImageDessin.masquerMotif(row2);
	    	MainClient.fenetre.getCanevas().repaint();
		}
		
		this.data[row][col] = value;
		this.fireTableCellUpdated(row, col);
	}
	
	public void setValueVisible(Boolean value, int row) {
		this.data[row][4] = value;
		this.fireTableCellUpdated(row, 4);
	}
	
	/**
	* Retourne le titre de la colonne à l'indice spécifié
	*/
	public String getColumnName(int col) {
		return this.title[col];
	}
	
	public Class getColumnClass(int col){
		try{
		return this.data[0][col].getClass();
		}catch(Exception e){
			return String.class;
		}
	}
	
	
	public boolean isCellEditable(int row, int col){
		if(col == 4)
			return true;
		return false; 
	}
	
	
	 public void ajouterMotif(Object[] data){
	      int indice = 0, nbRow = this.getRowCount(), nbCol = this.getColumnCount();
	       
	      Object temp[][] = this.data;
	      this.data = new Object[nbRow+1][nbCol];
	      
	      for(Object[] value : temp)
	         this.data[indice++] = value;
	       
	          
	      this.data[indice] = data;
	      temp = null;
	      //Cette méthode permet d'avertir le tableau que les données
	      //ont été modifiées, ce qui permet une mise à jour complète du tableau
	      this.fireTableDataChanged();
	  }
	  
	 public void supprimerMotif(int num){
		 
		 int i = 0, j = 0;
	     Object temp[][] = new Object[this.getRowCount()-1][this.getColumnCount()];
	       
	      for(Object[] value : this.data){
	         if(i != num){
	            temp[j++] = value;
	         }
	         i++;
	      }
	      this.data = temp;
	      temp = null;
	      //Cette méthode permet d'avertir le tableau que les données
	      //ont été modifiées, ce qui permet une mise à jour complète du tableau
	      this.fireTableDataChanged();
	 }

	public void synchroniserMotif() {
		Object[][] data = new Object[ImageDessin.listeMotif.size()][this.getColumnCount()];
		/*
		if(ImageDessin.listeMotif.size() == 0){
			this.data = new Object[0][this.getColumnCount()];
			this.fireTableDataChanged();
			return;
		}
		*/
		int i = 0;
		
		for(Motif motif : ImageDessin.listeMotif){
			data[i][0] = i+1;
			data[i][1] = (MainClient.listeDessinateur.get(motif.getNumUser())).getNom();
			data[i][2] = motif.getType();
			data[i][3] = motif.getCouleur();
			data[i][4] = !motif.isMasque(); 
			i++;
		}
		
		this.data = data;
		this.fireTableDataChanged();
	}

	public void modifier(int i, Object[] dataMotif) {
		data[i][0] = dataMotif[0];
		data[i][1] = dataMotif[1];
		data[i][2] = dataMotif[2];
		data[i][3] = dataMotif[3];
		data[i][4] = dataMotif[4];
		this.fireTableRowsUpdated(i, i);
	}

}

class ColorCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
 
        Color color = (Color) value;
 
        setText("");
        setBackground(color);
 
        return this;
    }
}
