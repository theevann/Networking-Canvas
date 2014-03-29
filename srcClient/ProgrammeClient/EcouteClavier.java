package ProgrammeClient;
import java.awt.AWTEvent;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractButton;
import javax.swing.JFrame;


public class EcouteClavier implements AWTEventListener {
	private Fenetre fenetre;
	private boolean ctrl = false;

	public EcouteClavier(Fenetre f) {
		super();
		fenetre = f;
	}
	
    public void eventDispatched(AWTEvent event){
          KeyEvent ke = (KeyEvent)event;
          
          if(ke.getID() == KeyEvent.KEY_RELEASED){
        	  //System.out.println("Touche relâch\351e : " + ke.getKeyCode());
        	  
        	  if(ke.getKeyCode() == 127 && !MainClient.fenetre.getEntreeChat().hasFocus() && !OngletHistorique.ongletActuel.getFiltre().hasFocus() && !MainClient.fenetre.getHistoriqueChat().hasFocus()){ // Touche Suppr
        		  OngletHistorique.ongletActuel.getSupprimer().doClick();
        	  }
        	  
        	  else if(ke.getKeyCode() == 27){ // Touche Echap
      			  OngletHistorique.ongletActuel.deselectionner();
        	  }
        	  
        	  else if(ke.getKeyCode() == 90 && ctrl){
        		  ((AbstractButton) MainClient.fenetre.bAnnuler).doClick();
        	  }
        	  
        	  if(ke.getKeyCode() == 17){ // Touche Ctrl
        		  ctrl = false;
        	  }
          }
          
          else if(ke.getID() == KeyEvent.KEY_PRESSED){
        	  if(ke.getKeyCode() == 17){ // Touche Ctrl
        		  ctrl = true;
        	  }
          }else if(ke.getID() == KeyEvent.KEY_TYPED){
          }
    }

	/**
	 * @return the ctrl
	 */
	public boolean isCtrl() {
		return ctrl;
	}
}


