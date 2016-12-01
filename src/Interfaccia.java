package progetto1.pkg0;

import javax.swing.BorderFactory;   //  Usato per definire il bordo delle etichette
import javax.swing.JFrame;          //  Usato per la creazione della finestra
import javax.swing.JLabel;          //  Usato per la creazione delle etichette
import javax.swing.JButton;         //  Usato per la creazione del pulsante

/*
*   Classe che modella la finestra su cui è raffigurato il tabellone
*/

public class Interfaccia extends JFrame{
    private final Tabellone jpnl;       //  Variabile che gestisce il tabellone
    private final JLabel[] squadre;     //  Vettore di etichette dove vengono stampati i nomi delle squadre
    private final JLabel[] risultati;   //  Vettore di etichette dove vengono stampati i risultati delle partite
    private final JLabel anno;          //  Etichetta dove viene stampato l'anno del campo visualizzato sul tabellone
    private final JButton jbt;          //  Pulsante del tabellone usato per giocare il turno
    
    //  Inizializza l'interfaccia, restituendo un tabellone vuoto
    Interfaccia(Campionato.Gestori.GestorePulsante gp, Campionato.Gestori.GestoreEtichette ge){
        //  Richiama prima di tutto il costruttore della superclasse
        super();
        //  Definisce la distanza tra le etichette
        int dist = 50;
        //  Definisce le varie proprietà del frame
        this.setTitle("Campionato Mondiale");
        this.setName("Campionato Mondiale");
        this.setSize(1200, 500);   // In questo caso le dimensioni sono fissate, non modificabili
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocation(60, 50);
        //  Definisce l'icona per la finestra
        javax.swing.ImageIcon icon= new javax.swing.ImageIcon("so.gif");
        this.setIconImage(icon.getImage());        
        //  Creazione di un nuovo pannello
        jpnl = new Tabellone(this.getSize());
        //  Definisce le varie proprietà del pannello
        jpnl.setLayout(null);
        //  Aggiunge il pannello al frame
        this.add(jpnl);        
        //  Crea l'etichetta che stampa l'anno "di base" del campionato 
        anno = new JLabel("2014");
        // Definisco le su proprietà
        anno.setSize(150,35);    // Dimensione (larghezza, altezza)
        anno.setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK));
        anno.setHorizontalAlignment(JLabel.CENTER);
        anno.setName("anno");
        anno.addMouseListener(ge);
        //  Questa nuova label viene aggiunta al pannello
        jpnl.add(anno).setLocation((jpnl.getWidth() - anno.getWidth())/2, 0); 
        
        //  Crea il pulsante
        jbt = new JButton("Gioca Ottavi di Finale");
        //  Definisce le proprietà del pulsante        
        jbt.setSize(200, 50);
        jbt.setBackground(java.awt.Color.LIGHT_GRAY);
        jbt.addActionListener(gp);
        //  Aggiunge il pulsante al pannello
        jpnl.add(jbt).setLocation(jpnl.getWidth()/2 - 100, jpnl.getHeight() - 100);                        
               
        //  Crea i nuovi vettori delle etichette del tabellone
        squadre = new JLabel[31];
        risultati = new JLabel[31];
        
        //  Aggiunge le etichette relative agli Ottavi di finale
        AggiungiEtichette(40, 40, dist, 0, 16, "Ottavi_", ge);
        //  Aggiunge le etichette relative ai Quarti di finale
        AggiungiEtichette(80 + squadre[0].getWidth() + risultati[0].getWidth(), 40 + dist/2, dist, 16, 8, "Quarti_", ge);
        //  Aggiunge le etichette relative alle Semifinali
        AggiungiEtichette(120 + 2*(squadre[0].getWidth() + risultati[0].getWidth()), 40 + (dist*3)/2, dist, 24, 4, "Semi_", ge);
        //  Aggiunge le etichette relativa alla Finale
        AggiungiEtichette(160 + 3*(squadre[0].getWidth() + risultati[0].getWidth()), 40 + (dist*7)/2, dist, 28, 2, "Finale_", ge);
        //  Aggiunge le etichette relativa al Vincitore
        AggiungiEtichette((this.getWidth() - (squadre[29].getWidth() + risultati[0].getWidth()))/2, squadre[29].getLocation().y + 80, dist, 30, 1, "Vincitore_", ge);                
        //  Rende il frame visibile
        this.setVisible(true);
    }    
    //  Metodo che si occupa del posizionamento delle etichette nel tabellone
    private void AggiungiEtichette(int X, int Y, int dist, int da, int n, String name, Campionato.Gestori.GestoreEtichette gpe){
        int x, y;       //  Variabili dove si memorizzano le coordinate delle varie etichette
        int ris_w = 20; //  Larghezza delle etichette dei risultati
        int k = 0;      //  Variabile di appoggio per "orientere" le etichette dei risultati in base al lato del
                        //      tabellone in cui si sta disegnando
        
        //  Controlla che non sia il caso dell'etichetta del "Vincitore"
        if(name.equals("Vincitore_"))
            ris_w = 0;
        //  Definisco le coordinate iniziali in cui posizionare le etichette
        x = X;
        y = Y;                                                                             
        //  Posizionamento Etichette Ottavi di Finale
        for(int i=da; i<(da+n); i++){
            //  Crea un nuovo contenitore JLabel (una nuova etichetta)
            squadre[i] = new JLabel();                    
            //  Definisco le su proprietà
            squadre[i].setSize(100 - ris_w,35);    // Dimensione (larghezza, altezza)
            squadre[i].setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK));
            squadre[i].setHorizontalAlignment(JLabel.CENTER);
            squadre[i].setName(name + (i-da));   
            squadre[i].addMouseListener(gpe);                      
            
            //  La seconda metà delle etichette va posizionata all'altra 
            //      estremità della finestra
            if(i == (da + n/2)){
                k = 1;
                x = jpnl.getWidth() - (squadre[i].getWidth() + ris_w + X);
                y = Y;                
            }      
            
            if(risultati != null){
                //  Crea un nuovo contenitore JLabel (una nuova etichetta)
                risultati[i] = new JLabel();                    
                //  Definisco le su proprietà
                risultati[i].setSize(ris_w,35);    // Dimensione (larghezza, altezza)
                risultati[i].setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK));
                risultati[i].setHorizontalAlignment(JLabel.CENTER);
                risultati[i].setName("ris_" + name + i);
                //  Viene aggiunta al pannello
                jpnl.add(risultati[i]).setLocation(x + squadre[i].getWidth()*(1-k), y);
            }            
            //  Aggiunge un'etichetta alla finestra
            jpnl.add(squadre[i]).setLocation(x + k*ris_w, y);
            //  Incremento della posizione sull'asse verticale
            y += (16/n)*dist;
        }
    }
    //  Metodo che si occupa della modifica del contenuto dell'etichetta <anno> del tabellone
    public void setAnno(int anno){
        this.anno.setText(String.valueOf(anno));
    }
    //  Metodo che si occupa della modifica del contenuto di una etichetta delle squadre del tabellone
    @SuppressWarnings("ConvertToStringSwitch")
    public void setTextSquadre(String testo, Posizione turno, int i) throws MieEccezioni{
        int n = getIndiceEtichetteTurno(turno); // Variabile indice del vettore delle etichette
                                                //      Questo perché le etichette degli ottavi sono gli elementi da 0 a 15
                                                //      Le etichette dei quarti sono gli elementi da 16 a 23
                                                //      Le etichette delle semi finali sono gli elementi da 24 a 27
                                                //      Le etichette delle finali sono gli elementi da 28 a 29
                                                //      L'etichetta del vincitore è l'elemento nella posizione 30
        if(i < Math.pow(2, (4 - turno.getTurnoInt())))
            //  Si procede alla modifica del contenuto delle etichette
            squadre[n+i].setText(testo);
        else
            throw new MieEccezioni("Indice non valido");
    }
    //  Metodo che si occupa della modifica del contenuto di tutte le etichette delle squadre del tabellone di un determinato turno
    @SuppressWarnings("ConvertToStringSwitch")
    public void setTextSquadre(String[] testo, Posizione turno) throws MieEccezioni{
        int n = getIndiceEtichetteTurno(turno); // Variabile indice del vettore delle etichette
                                                //      Questo perché le etichette degli ottavi sono gli elementi da 0 a 15
                                                //                      Le etichette dei quarti sono gli elementi da 16 a 23
                                                //                      Le etichette delle semi finali sono gli elementi da 24 a 27
                                                //                      Le etichette delle finali sono gli elementi da 28 a 29
                                                //                      L'etichetta del vincitore è l'elemento nella posizione 30        
        if(testo.length == Math.pow(2, (4 - turno.getTurnoInt())))
            //  Si procede alla modifica del contenuto delle etichette
            for(int i=n; i<n+testo.length; i++)
                squadre[i].setText(testo[i-n]);
        else
            throw new MieEccezioni("Parametro non valido");
    }      
    //  Metodo che si occupa della modifica del contenuto di una etichetta del risultato del tabellone
    @SuppressWarnings("ConvertToStringSwitch")
    public void setTextRisultato(String gol, Posizione turno, int i) throws MieEccezioni{
        int n = getIndiceEtichetteTurno(turno); // Variabile indice del vettore delle etichette
                                                //      Questo perché le etichette degli ottavi sono gli elementi da 0 a 15
                                                //      Le etichette dei quarti sono gli elementi da 16 a 23
                                                //      Le etichette delle semi finali sono gli elementi da 24 a 27
                                                //      Le etichette delle finali sono gli elementi da 28 a 29
                                                //      L'etichetta del vincitore è l'.elemento nella posizione 30
        if(i < Math.pow(2, (4 - turno.getTurnoInt())))
            //  Si procede alla modifica del contenuto delle etichette
            risultati[n+i].setText(gol);
        else
            throw new MieEccezioni("Indice non valido");
    }
    //  Metodo che si occupa della modifica del contenuto di tutte le etichette dei risultati del tabellone di un determinato turno
    @SuppressWarnings("ConvertToStringSwitch")
    public void setTextRisultato(String[] gol, Posizione turno) throws MieEccezioni{
        int n = getIndiceEtichetteTurno(turno); // Variabile indice del vettore delle etichette
                                                //      Questo perché le etichette degli ottavi sono gli elementi da 0 a 15
                                                //      Le etichette dei quarti sono gli elementi da 16 a 23
                                                //      Le etichette delle semi finali sono gli elementi da 24 a 27
                                                //      Le etichette delle finali sono gli elementi da 28 a 29
                                                //      L'etichetta del vincitore è l'elemento nella posizione 30        
        
        if(gol.length == Math.pow(2, (4 - turno.getTurnoInt())))
            //  Si procede alla modifica del contenuto delle etichette
            for(int i=n; i<n+gol.length; i++)
                risultati[i].setText(gol[i-n]);
        else
            throw new MieEccezioni("Parametro non valido");
    }    
    //  Metodo che va a scrivere sul pulsante
    public void setTextButton(String testo){  jbt.setText(testo);    }
    //  Metodo che si occupa di restituire l'indice da cui iniziano l'etichette del turno richiesto
    private int getIndiceEtichetteTurno(Posizione turno) throws MieEccezioni{
        int n = 0;        
        //  Ciclo di calcolo dell'indice
        for(int i=0; i<turno.getTurnoInt(); i++)
            n += Math.pow(2, (4 - i) );        
        
        return n;
    }    
    //  Metodo restituisce il contenuto di una etichetta delle squadre del tabellone
    @SuppressWarnings("ConvertToStringSwitch")
    public String getTextSquadre(Posizione turno, int i) throws MieEccezioni{
        int n = getIndiceEtichetteTurno(turno); //  Variabile indice del vettore delle etichette
                                                //  Questo perché le etichette degli ottavi sono gli elementi da 0 a 15
                                                //      Le etichette dei quarti sono gli elementi da 16 a 23
                                                //      Le etichette delle semi finali sono gli elementi da 24 a 27
                                                //      Le etichette delle finali sono gli elementi da 28 a 29
                                                //      L'etichetta del vincitore è l'elemento nella posizione 30
        String s = null;
        if(i < Math.pow(2, (4 - turno.getTurnoInt())))
            s = squadre[n+i].getText();    //  Estrae il testo dall'etichetta
        else
            throw new MieEccezioni("Indice non valido");
        
        
        return s;
    }
    //  Metodo che restituisce il contenuto di una etichetta del risultato del tabellone
    @SuppressWarnings("ConvertToStringSwitch")
    public String getTextRisultato(Posizione turno, int i) throws MieEccezioni{
        int n = getIndiceEtichetteTurno(turno); //  Variabile indice del vettore delle etichette
                                                //      Questo perché le etichette degli ottavi sono gli elementi da 0 a 15
                                                //      Le etichette dei quarti sono gli elementi da 16 a 23
                                                //      Le etichette delle semi finali sono gli elementi da 24 a 27
                                                //      Le etichette delle finali sono gli elementi da 28 a 29
                                                //      L'etichetta del vincitore è l'elemento nella posizione 30               
        String s = null;
        
        if(i < Math.pow(2, (4 - turno.getTurnoInt())))
            s = risultati[n+i].getText();  //  Estrae il testo dall'etichetta
        else
            throw new MieEccezioni("Indice non valido");
        
        return s;
    }
    //  Restituisce il testo sul pulsante
    public String getTextButton(){  return jbt.getText();    }            
    //  Disabilita il pulsante
    public void DisableButton(){    this.jbt.setEnabled(false); }
    //  Abilita il pulsante
    public void EnableButton(){    this.jbt.setEnabled(true); }
    //  "Pulisce" il tabellone
    public void svuota(){
        Posizione p = new Posizione();  //  Variabile di controllo del ciclo
        int len;
        String[] s;
        try{
            //  Ciclo di cancellazione del tabellone, fino alla fine dei turni
            do{
                //  Calcola la lunghezza del vettore in base al turno considerato
                len = (int)Math.pow(2, (4 - p.getTurnoInt()));
                //  Ridefinisce il vettore si stringhe
                s = new String[len];
                //  Riempe il vettore di stringhe con stringhe vuote
                java.util.Arrays.fill(s, "");
                //  Manda le stringhe sul tabellone
                setTextSquadre(s, p);
                setTextRisultato(s, p);
            }
            while(p.nextPos());
        }
        catch(MieEccezioni my_ex){
            javax.swing.JOptionPane.showMessageDialog(null, my_ex, "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }            
}
