package client;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;


@SuppressWarnings("serial")
public class ModeleTableauDessinateur extends AbstractTableModel{

	private String[] title;
	private Object[][] data;
	private static BufferedImage iconeConnecte = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
	private static BufferedImage iconeNonConnecte = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);

	static {
		Graphics g = getIconeConnection(true).createGraphics();
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	    g2d.setColor(Color.green);
	    g2d.fillOval(0, 0, 10, 10);

	    Graphics gg = getIconeConnection(false).createGraphics();
		Graphics2D gg2d = (Graphics2D) gg;
		gg2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	    gg2d.setColor(Color.red);
	    gg2d.fillOval(0, 0, 10, 10);
	}

	public ModeleTableauDessinateur(Object[][] data, String[] title) {
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
		return this.data[rowIndex][columnIndex];
	}


	public void setValueAt(Object value, int row, int col) {

		if(col == 2){
			MainClient.listeDessinateur.get(row).setVoir((Boolean) value);
	    	ImageDessin.creerImage();
	    	MainClient.fenetre.getCanevas().repaint();
		}
		this.data[row][col] = value;
	}

	/**
	* Retourne le titre de la colonne e l'indice specifie
	*/
	public String getColumnName(int col) {
		return this.title[col];
	}

	public Class getColumnClass(int col){
		if(col == 0)
			return Icon.class;
		return this.data[0][col].getClass();
	}


	public boolean isCellEditable(int row, int col){
		if(getValueAt(0, col) instanceof Boolean)
			return true;
		return false;
	}


	// Methode pr mettre a jour les connexions

	 public void ajouterDessinateur(Object[] data){
	      int indice = 0, nbRow = this.getRowCount(), nbCol = this.getColumnCount();

	      Object temp[][] = this.data;
	      this.data = new Object[nbRow+1][nbCol];

	      for(Object[] value : temp)
	         this.data[indice++] = value;


	      this.data[indice] = data;
	      temp = null;
	      //Cette methode permet d'avertir le tableau que les donnees
	      //ont ete modifiees, ce qui permet une mise e jour complete du tableau
	      this.fireTableDataChanged();
	  }

	  public void connecterDessinateur(Dessinateur dessinateur){
	      int i = 0, indice = 0;

	      for(Object[] value : this.data){
		         if(value[1].equals(dessinateur.getNom())){
		        	 indice = i;
		         }
		         i++;
	      }


	      this.data[indice][0] = true;

	      //Cette methode permet d'avertir le tableau que les donnees
	      //ont ete modifiees, ce qui permet une mise e jour complete du tableau
	      this.fireTableDataChanged();
	  }

	  public void deconnecterDessinateur(Dessinateur dessinateur){
	      int i = 0, indice = 0;

	      for(Object[] value : this.data){
		         if(value[1].equals(dessinateur.getNom()))
		        	 indice = i;
		         i++;
	      }

	      this.data[indice][0] = false;

	      //Cette methode permet d'avertir le tableau que les donnees
	      //ont ete modifiees, ce qui permet une mise e jour complete du tableau
	      this.fireTableDataChanged();
	  }

	public void synchroniserDessinateur() {

		Object[][] data = new Object[MainClient.listeDessinateur.size()][3];

		int i = 0;

		for(Dessinateur dessinateur : MainClient.listeDessinateur){
			data[i][0] = (dessinateur.isConnecte() ? true : false);
			data[i][1] = dessinateur.getNom();
			data[i][2] = dessinateur.getVoir();
			i++;
		}

		this.data = data;
		this.fireTableDataChanged();
	}

	/**
	 * @param b
	 * @renvoie l'icone verte
	 */
	public static BufferedImage getIconeConnection(boolean connecte) {
		if(connecte)
			return iconeConnecte;
		else
			return iconeNonConnecte;
	}

}

class IconeCellRenderer extends DefaultTableCellRenderer {
    private Icon connecte;
    private Icon deconnecte;

    public IconeCellRenderer() {
        super();

        connecte = new ImageIcon(ModeleTableauDessinateur.getIconeConnection(true));
        deconnecte = new ImageIcon(ModeleTableauDessinateur.getIconeConnection(false));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        Boolean estConnecte = (Boolean)value;

    	setHorizontalAlignment(SwingConstants.CENTER);
        setText("");

        if(estConnecte){
            setIcon(connecte);
        } else {
            setIcon(deconnecte);
        }

        return this;
    }

}
