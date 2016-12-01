package progetto1.pkg0;

import java.util.Random;    //  Usata per generare numeri casuali

/*
*   Classe che modella il comportamento di una partita di calcio
*/

public class Partita {
    private final Squadra squadraCasa;      //  Oggetto <Squadra> per definire la squadra di casa
    private final Squadra squadraOspite;    //  Oggetto <Squadra> per definire la squadra ospite
    private final int tifoCasa;             //  Indica il tifo per la squadra di casa (funzione non ancora implementata)
    private final int tifoOspite;           //  Indica il tifo per la squadra ospite (funzione non ancora implementata)
    private final int anno;                 //  Indica l'anno in cui si è svolta la partita
    private int golCasa;                    //  Indica i gol realizzati dalla squadra di casa
    private int golOspite;                  //  Indica i gol realizzati dalla squadra ospite
    private final String tipoPartita;       //  Indica il turno della partita
        
    public Partita(){
        squadraCasa = squadraOspite = null;
        tifoCasa = tifoOspite = anno = 0;
        tipoPartita = null;        
    }
    //  Costruttore di una partita da disputare
    //  Parametri:
    //             Squadra <sC>:  Oggetto che identifica la squadra di casa
    //             Squadra <sO>:  Oggetto che identifica la squadra ospite
    //           int <tifoCasa>:  Quantifica il tifo per i padroni di casa (funzionalità non ancora implementata)
    //         int <tifoOspite>:  Quantifica il tifo per i padroni di casa (funzionalità non ancora implementata)
    //  Posizione <tipoPartita>:  Indica il turno che si sta disputando con questa partita
    //                  int <i>:  Indica il numero "identificativo" della partita
    //               int <anno>:  Indica l'anno in cui si è disputata la partita
    public Partita(Squadra sC, Squadra sO, int tifoCasa, int tifoOspite, Posizione tipoPartita, int i, int anno){
        this.squadraCasa = sC;
        this.squadraOspite = sO;
        this.tifoCasa = tifoCasa;
        this.tifoOspite = tifoOspite;       
        this.tipoPartita = tipoPartita.getTurnoString() + "_" + i;  //  Nome della partita
        this.anno = anno;
        this.golCasa = this.golOspite = -1;                         //  Il valore -1 indica una partita non ancora disputata
    }
    //  Costruttore di una partita già disputata
    //  Parametri:
    //             Squadra <sC>:  Oggetto che identifica la squadra di casa
    //             Squadra <sO>:  Oggetto che identifica la squadra ospite
    //           int <tifoCasa>:  Quantifica il tifo per i padroni di casa (funzionalità non ancora implementata)
    //         int <tifoOspite>:  Quantifica il tifo per i padroni di casa (funzionalità non ancora implementata)
    //  Posizione <tipoPartita>:  Indica il turno che si sta disputando con questa partita
    //                  int <i>:  Indica il numero "identificativo" della partita
    //               int <anno>:  Indica l'anno in cui si è disputata la partita
    public Partita(Squadra sC, Squadra sO, int tifoCasa, int tifoOspite, Posizione tipoPartita, int i, int anno, int golCasa, int golOspite){
        this.squadraCasa = sC;
        this.squadraOspite = sO;
        this.tifoCasa = tifoCasa;
        this.tifoOspite = tifoOspite;        
        this.tipoPartita = tipoPartita.getTurnoString() + "_" + i;  //  Nome della partita
        this.anno = anno;        
        this.golCasa = golCasa;
        this.golOspite = golOspite;
    }       
    //  Metodi per la restituizione delle informazioni legate alla partita
    public int getAnno(){   return this.anno;   }
    public int getGolCasa(){    return this.golCasa;    }
    public int getGolOspite(){    return this.golOspite;    }
    public String getTipoPartita(){    return this.tipoPartita;    }
    public Squadra getSquadraCasa(){ return new Squadra(this.squadraCasa); }
    public Squadra getSquadraOspite(){  return new Squadra(this.squadraOspite); }    
    //  Metodo che si occupa di modellare lo svolgimento di una partita
    public void Gioca(){
        Math.random();                          //  Inizializza la funzione <random>
        int i = 1;                              //  Variabile che indica la posizione nel campo per la squadra di casa (-1: gol ospiti, 0: difesa, 1: centrocampo, 2: attacco, 3: gol padroni di casa
        int tempo = 0;                          //  "Orologio" della partita
        int t_min = 3, t_max = 7;               //  Tempo minimo e tempo massimo di durata di un evento
        Random r = new Random();                //  Il tempo richiesto da un "evento" è scelto casuale tra 3 e 7 minuti
        boolean pallaCasa, v_palla;             //  true: Palla ai padroni di casa | false: Palla agli ospiti
        Double perc[] = new Double[3];          //  Valori in percentuale della squadra di casa (quelli della ospite si ricavano di conseguenza)       
                                                //  Casa: (0: difesa, 1: centrocampo, 2: attacco) | Avversaria, per dualità:(0: attacco, 1: centrocampo, 2: difesa)
        //  Controlla che non sia un partita già disputata
        if(golCasa == -1){
            //  Inizializza il valore dei gol delle due squadre
            golCasa = 0;
            golOspite = 0;
            // Perché la difesa + portiere della squadra di casa andrà confrontato con l'attacco della squadra ospite
            perc[0] = 1 - ((double)squadraOspite.getAttacco()) / (squadraOspite.getAttacco()+ squadraCasa.getDifesa() + squadraCasa.getPortiere());
            // Perché il centrocampo della squadra di casa andrà confrontato con il centrocampo della squadra ospite
            perc[1] = 1 - (double)squadraOspite.getCentrocampo() / (squadraCasa.getCentrocampo() + squadraOspite.getCentrocampo());
            // Perché l'attacco della squadra di casa andrà confrontato con la difesa + portiere della squadra ospite
            perc[2] = 1 - (double)(squadraOspite.getDifesa() + squadraOspite.getPortiere())/ (squadraOspite.getDifesa() + squadraOspite.getPortiere() + squadraCasa.getAttacco());                      
            // Si sceglie che batterà il calcio d'inizio, "lanciando la monetina"
            v_palla = Math.random() < 0.5;                        
            // Finché la partita non raggiunge o supera i 90 minuti si gioca
            do{
                tempo += t_min + r.nextInt(t_max - t_min);  // r.nextInt(t_max - t_min) restituirà un valore tra 0 e t_max - t_min
                                                            // sommando t_min avremo un valore casuale compreso tra t_min e t_max
                //  L'azione in svolgimento può essere interrotta dall'altra squadra. Questa evenienza è gestita da un caso "pesato"
                pallaCasa = Math.random() < perc[i];            
                // Controlla se una squadra ha mantenuto il possesso della palla passa all'attacco
                if(pallaCasa == v_palla)
                    // Se è la squadra di casa ad avere il possesso andremo a prendere il suo valore di attacco
                    if(pallaCasa)
                        i++;
                    // altrimenti, se è la squadra ospite ad attaccare, prenderemo il valore di difesa della squadra di casa
                    else
                        i--;                                         
                //  Se i>2 significa che l'attacco di casa ha superato la difesa ospite
                if(i > 2){
                    golCasa++;
                    i = 1;
                    pallaCasa = false;
                }
                //  Se i<0 significa che l'attacco ospite ha superato la difesa di casa
                if(i < 0){
                    golOspite++;
                    i = 1;
                    pallaCasa = true;
                }                                                
                //  Memorizza il vecchio valore di possesso (questo per vedere se l'azione continua in attacco o si ferma a 
                //      centrocampo perché gli avversari hanno rubato palla
                v_palla = pallaCasa;                            
            }while(tempo < 90);                                                                            
        }
        else
            javax.swing.JOptionPane.showMessageDialog(null, "Partita già disputata!", "In Partita", javax.swing.JOptionPane.INFORMATION_MESSAGE);        
    }
    //  Stampa tutte le informazioni legate alla partita
    @Override
    public String toString(){
        return("Anno: " + this.anno + "\nTurno: " + this.tipoPartita + "\nSquadra Casa: " + this.squadraCasa.getNome() +
                    "\t " + this.golCasa + "\nSquadra Ospite: " + this.squadraOspite.getNome() + "\t " + this.golOspite);                                
    }    
    //  Confronta l'istanza con l'oggetto passato come parametro
    @Override
    public boolean equals(Object partita){
        boolean b = false;
        
        if(this.tipoPartita.equals(((Partita)partita).tipoPartita) && this.anno == ((Partita)partita).anno &&
            this.squadraCasa.getNome().equals(((Partita)partita).squadraCasa.getNome()) && this.squadraOspite.getNome().equals(((Partita)partita).squadraOspite.getNome()) &&
            this.tifoCasa == ((Partita)partita).tifoCasa && this.tifoOspite == ((Partita)partita).tifoOspite)
            b = true;
        return b;
    }
}
