package progetto1.pkg0;

/*
*   Classe che modella un giocatore
*/

public class Giocatore{
    private final String squadra;   //  Il nome della squadra in cui si presenta il giocatore
    private final String nome;      //  Il nome del giocatore
    private final String cognome;   //  Il cognome del giocatore
    private final String ruolo;     //  Ruolo in cui il giocatore preferisce giocare
    private final int num_maglia;   //  Numero di maglia del giocatore
    private final int golFatti;     //  I gol fatti dal giocatore (funzione non ancora implementata)
    private final int presenze;     //  Presenze del giocatore nel torneo (funzione non ancora implementata)
    private final int situazione;   //  Situazione fisica del giocatore (funzione non ancora implementata)
    private final int attacco, centrocampo, difesa, portiere;   //  Caratteristiche del giocatore
    
    
    public Giocatore(){
        squadra = "";
        nome = "";
        cognome = "";
        num_maglia = golFatti = presenze = situazione = 0;
        attacco = centrocampo = difesa = portiere = 0;
        ruolo = "";
    }
    
    public Giocatore(String squadra, String nome, String cognome, String ruolo,
                        int num_maglia, int golFatti, int presenze, int attacco, int centrocampo,
                        int difesa, int portiere, int situazione){
        this.squadra = squadra;
        this.nome = nome;
        this.cognome = cognome;
        this.ruolo = ruolo;
        this.num_maglia = num_maglia;
        this.golFatti = golFatti;
        this.presenze = presenze;
        this.situazione = situazione;
        this.attacco = attacco;
        this.centrocampo = centrocampo;
        this.difesa = difesa;
        this.portiere = portiere;    
    }
    
    public Giocatore(Giocatore clone){
        this.nome       = clone.nome;
        this.cognome    = clone.cognome;
        this.ruolo      = clone.ruolo;
        this.squadra    = clone.squadra;
        this.num_maglia = clone.num_maglia;
        this.golFatti   = clone.golFatti;
        this.presenze   = clone.presenze;
        this.situazione = clone.situazione;
        this.attacco    = clone.attacco;
        this.centrocampo = clone.centrocampo;
        this.difesa     = clone.difesa;
        this.portiere   = clone.portiere;
    }
    //  Metodi per la visiblità (solo in uscità) delle informazioni
    public String getNome(){    return this.nome;   }
    public String getCognome(){    return this.cognome;   }
    public String getNomeSquadra(){    return this.squadra;   }
    public String getRuolo(){   return this.ruolo;  }
    public int getNumero(){     return this.num_maglia; }
    public int getAttacco(){    return this.attacco;    }
    public int getCentrocampo(){    return this.centrocampo;    }
    public int getDifesa(){     return this.difesa; }
    public int getPortiere(){   return this.portiere;   }
    //  Metodo che restituisce in una stringa le informazioni del giocatore
    @Override
    public String toString(){
        return(this.nome + " " + this.cognome + "\n numero:" + this.num_maglia + "\n ruolo:" + this.ruolo + 
                    "\n squadra:" + this.squadra + "\nP: " + this.portiere + "\nD: " + this.difesa +
                    "\n C: " + this.centrocampo + "\nA: " + this.attacco);
    }
    //  Metodo che confronta due oggetti <Giocatore>
    @Override
    public boolean equals(Object giocatore){
        boolean b = false;        
        Giocatore g = (Giocatore)giocatore;
        
        if((this.nome.equals((g.getNome()))) && (this.cognome.equals(g.getCognome())) && (this.num_maglia == g.getNumero()) && 
                (this.ruolo.equals(g.getRuolo())) && (this.squadra.equals(g.getNomeSquadra())) && (this.attacco == g.getAttacco()) &&
                (this.centrocampo == g.getCentrocampo()) && (this.difesa == g.getDifesa()) && (this.portiere == g.getPortiere()))
            b = true;
        return b;
    }
}