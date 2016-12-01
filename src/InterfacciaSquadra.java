package progetto1.pkg0;

import java.awt.event.AdjustmentEvent;      //  Oggetto che gestisce l'evento dello scorrimetno della scrollbar
import java.awt.event.AdjustmentListener;   //  Listener per la scrollbar
import javax.swing.JFrame;                  //  Usato per creare la nuova finestra
import javax.swing.JPanel;                  //  Usato per creare il pannello in cui stampare le informazioni della squadra
import javax.swing.JLabel;                  //  Usato per creare le etichette su cui stampare
import javax.swing.JScrollBar;              //  Usato per creare la scrollbar per scorrere la finestra
import javax.swing.BorderFactory;           //  Usato per definire i bordi delle etichette

/*
*   Classe che modella la finestra che stampa le informazioni relativa ad una squadra
*/

public class InterfacciaSquadra extends JFrame{
    private final JPanel jpnl;  //  Pannello in cui stampare le informazioni della squadra
    private final JLabel[] jlb; //  Etichette su cui visualizzare le informazioni
    private JScrollBar jsp;     //  Scrollbar per scorrere la finestra
    
    public InterfacciaSquadra(){
        this.setSize(330, 330);
        jpnl = null;
        jlb = null;
        this.setVisible(true);
    }
    //  Costruttore con parametro:
    //      Squadra s:= la squadra di cui si vogliono stampare le informazioni
    public InterfacciaSquadra(Squadra s){
        //  Inizializzazione frame
        this.setSize(400, 500);
        this.setResizable(false);
        this.setLayout(null);   
        this.setTitle(s.getNome());
        this.setLocation(400, 50);
        this.setName(s.getNome());
        //  Inizializzazione pannello
        jpnl = new JPanel();        
        jpnl.setLayout(null);
        jpnl.setSize(400, 1000);
        //  Definizione delle etichette
        jlb = new JLabel[25];
        //      Proprietà dell'etichetta che contiene eil nome della squadra
        jlb[0] = new JLabel(s.getNome());
        jlb[0].setSize(jpnl.getWidth() - 30, 35);
        jlb[0].setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK, 1));
        jlb[0].setLayout(null);
        // La nuova etichette viene inserita nel pannello
        jpnl.add(jlb[0]).setLocation(0, 0);
        //      Etichette che andranno a contenere le informazioni sui giocatori
        for(int i=0; i<s.getGiocatori().length; i++){            
            jlb[i+1] = new JLabel(s.getGiocatori()[i].toString());
            jlb[i+1].setSize(jpnl.getWidth() - 30, 35);
            jlb[i+1].setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK, 1));
            jlb[i+1].setLayout(null);
            // La nuova etichette viene inserita nel pannello
            jpnl.add(jlb[i+1]).setLocation(0, (i+1)*35);
        }                        
        //  Inizializzazione Barra di scorrimento
        jsp = new JScrollBar(JScrollBar.VERTICAL, 0, 1, 0, (jlb[0].getHeight() + 1)* jlb.length - (this.getHeight() - 4));
        jsp.setSize(30, this.getHeight() - 25);                       
        jsp.setUnitIncrement(35);
        //      Definisce un nuovo gestore per gli eventi della scrollbar
        jsp.addAdjustmentListener(new GestoreScrollBar());
        // La nuova scrollbar viene inserita nel frame
        this.add(jsp).setLocation(jpnl.getWidth() - jsp.getWidth(), 0);
        // Il pannello viene inserito nel frame
        this.add(jpnl);
        // Rende il pannello e il frame visibili
        jpnl.setVisible(true);        
        this.setVisible(true);
    }
    
    //  Il gestore della scrollbar
    class GestoreScrollBar implements AdjustmentListener {
        @Override
        //  Evento dello scorrimento della barra
        public void adjustmentValueChanged(AdjustmentEvent e) {
            //  Ridisegna il pannello
            jpnl.setLocation(0, -e.getValue());
            repaint();
        }
    }
    
}