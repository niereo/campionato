package progetto1.pkg0;

/*
*   Classe che modella la posizione di campionato
*/

public class Posizione{
    private final String[] turno = {"Ottavi", "Quarti", "Semi", "Finale", "Vincitore"};
    private int posizione;
    //  Inizializza la posizione su 0 --> "Ottavi"
    public Posizione(){       
        this.posizione = 0;
    }
    //  Inizializza la posizione ad una posizione arbitraria tramite indice
    public Posizione(int posizione) throws MieEccezioni{
        if(posizione > -1  &&  posizione < 5)
            this.posizione = posizione;
        else
            throw new MieEccezioni("Posizione non valida");
    }
    //  Inizializza la posizione ad una posizione arbitraria tramite il nome del turno
    public Posizione(String turno) throws MieEccezioni{
        this.posizione = -1;
            for(int i=0; i<this.turno.length; i++)
                if(this.turno[i].equals(turno))
                    this.posizione = i;
        //  Controllo che la stringa passata come parametro sia una stringa valida
        if(this.posizione < 0)
            throw new MieEccezioni("Posizione non valida");
    }
    //  Passa al turno successivo, fino a che non si arriva al limite del vettore
    public boolean nextPos(){
        boolean b = false;
        if(this.posizione < 4){
            this.posizione++;
            b = true;
        }      
        return b;
    }
    //  Restituisce il turno sotto forma di stringa
    public String getTurnoString(){ return this.turno[this.posizione];  }
    //  Restituisce l'indice del vettore <turno>
    public int getTurnoInt(){   return this.posizione;  }
    //  Restituisce una descrizione dell'oggetto    
    @Override
    public String toString(){   return this.turno[this.posizione];  }
    //  Confronta due posizioni e restituiscevero se sono uguali, falso altrimenti
    @Override    
    public boolean equals(Object p){
        boolean b = false;
        //  Se i due oggetti hanno lo stesso valore sono uguali
        if(((Posizione)p).getTurnoInt() == this.posizione)
            b = true;
        
        return b;
    }
}
