package client;

public class ClignotementPointille extends Thread {

	public ClignotementPointille(){
	}
	
	public void run(){
		
		while(OngletHistorique.ongletActuel.getTableau().getSelectedRowCount() > 0){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			MainClient.fenetre.getCanevas().clignotement = !MainClient.fenetre.getCanevas().clignotement;
			MainClient.fenetre.getCanevas().repaint();
		}
		MainClient.fenetre.getCanevas().clignotement = true;
	}
	
}
