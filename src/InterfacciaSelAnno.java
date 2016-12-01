package progetto1.pkg0;

import javax.swing.JButton;         //  Usato per creare il pulsante
import javax.swing.JFrame;          //  Usato per creare la finestra
import javax.swing.JComboBox;       //  Usato per creare la combobox

/*
*   Classe che modella la finestra per la selezione dell'anno di campionato da stampare sul tabellone
*/

public class InterfacciaSelAnno extends JFrame{
    private final JComboBox jcb;    //  Combobox per la selezione dell'anno
    private final JButton jbt;      //  Pulsante di stampa
    
    // Costruttore. Parametri:
    //                     Integer[] anni:= Anni di campionato
    //  java.awt.event.ActionListener gsb:= Gestore della premuta del pulsante di selezione
    public InterfacciaSelAnno(Integer[] anni, java.awt.event.ActionListener gsb){
        //  Richiama prima di tutto il costruttore della superclasse
        super();
        //  Definisce le varie proprietà del frame
        this.setTitle("Scegli un anno");
        this.setName("Scegli un anno");
        this.setSize(300, 150);   // In questo caso le dimensioni sono fissate, non modificabili
        this.setResizable(false);
        this.setLayout(null);
        this.setLocation(400, 100);       
        //  Definisce le proprietà della combobox
        this.jcb = new JComboBox(anni);
        this.jcb.setSize(this.getWidth() - 3, 35);
        this.add(this.jcb).setLocation(0, 10);
        //  Definisce le proprietà del pulsante        
        this.jbt = new JButton("Seleziona");
        this.jbt.setSize(100, 40);
        this.jbt.addActionListener(gsb);        
        this.add(this.jbt).setLocation(100, 60);
        //  Rende la finestra visibile
        this.setVisible(true);
    }
    //  Restituisce l'elemento selezionato nella combobox
    public int getSelectedItem(){   return((Integer)this.jcb.getSelectedItem()); }
    //  Disabilita il pulsante
    public void DisableButton(){    this.jbt.setEnabled(false); }
    //  Abilita il pulsante
    public void EnableButton(){    this.jbt.setEnabled(true); }    
}