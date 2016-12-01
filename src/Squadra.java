package progetto1.pkg0;

/*
*   Classe che modella una squadra
*/

public class Squadra {
    private final String nome;      //  nome della squadra
    private Giocatore giocatori[];  //  i giocatori della squadra
                                    //      Si suppone che i primi undici siano i titolari
    private int formazione;         //  definisce la formazione (es. 433, 442, ecc..)
    private int golFatti;           //  memorizza i gol fatti da una squadra nel campionato
    private int golSubiti;          //  memorizza i gol subiti da una squadra nel campionato
    private Posizione posizione;    //  memorizza la posizione raggiunta dalla squadra nel campionato
    
    public Squadra(){
        nome = "";
        giocatori = null;
        formazione = golFatti = golSubiti = 0;
        posizione = null;
    }
    //  Costruttore con parametri: <nome della squadra>, <tutti i giocatori della squadra>, <formazione della squadra>
    public Squadra(String nome, Giocatore giocatori[], int formazione){
        this.nome = nome;
        this.giocatori = new Giocatore[24];
        
        for(int i=0; i<24; i++)
            this.giocatori[i] = new Giocatore(giocatori[i]);
        
        this.formazione = formazione;
        golFatti = golSubiti = 0;
        this.posizione = new Posizione();
    }
    //  Clona l'oggetto corrente su di un altro oggetto
    public Squadra(Squadra clone){
        this.nome = clone.nome;
        this.giocatori = new Giocatore[24];
        
        for(int i=0; i<24; i++)
            this.giocatori[i] = new Giocatore(clone.giocatori[i]);        
        
        this.formazione = clone.formazione;
        this.golFatti = clone.golFatti;
        this.golSubiti = clone.golSubiti;
        this.posizione = clone.posizione;
    }
    // Metodo che si occupa di definire la squadra che scenderà in campo.
    //  Con controllo che <titolari> sia lungo 11
    public void setTitolari(Giocatore[] titolari) throws MieEccezioni{
        int j;  //  Variabile indice di appoggio
        if(titolari.length == 11)
            try{
                //  Scorre il vettore <titolari>
                for(int i=0; i<11; i++){
                    //  Se il giocatore da posizionare è diverso da quello che è presente nella posizione
                    if(titolari[i].getNumero() != this.giocatori[i].getNumero()){
                        //  ...cerca il giocatore nel vettore <titolari> all'interno di <giocatori>
                        j = cerca(titolari[i].getNumero());
                        //  Se il giocatore è presente nella rosa...
                        if(j<24)
                            //  ...scambia il giocatore con il giocatore che attualmente occupa la posizione
                            scambia(this.giocatori[i].getNumero(), this.giocatori[j].getNumero());
                        //  altrimenti...
                        else
                            //  ...lancia un'eccezione perché il giocatore non è stato trovato
                            throw new MieEccezioni("\n" + titolari[i].toString() + " non presente in questa squadra!");
                    }
                }
            }
            //  Controllo che nessun elemento del vettore sia a "null"
            catch(NullPointerException np_ex){
                throw new MieEccezioni("Parametro non valido!");
            }
        else
            throw new MieEccezioni("Numero di giocatori errato");
    }
    //  Metodo che definisce la <formazione> della squadra
    public void setFormazione(int formazione){  this.formazione = formazione;   }
    //  Metodo che definisce i <golFatti> dalla squadra in questo campionato
    public void setGolFatti(int gf){  this.golFatti = gf;   }
    //  Metodo che definisce i <golSubiti> dalla squadra in questo campionato
    public void setGolSubiti(int gs){  this.golSubiti = gs;   }
    //  Metodo che definisce la <posizione> raggiunta nel campionato dalla squadra
    public void setPosizione(Posizione p){  this.posizione = p; }
    //  Restituisce il nome della squadra
    public String getNome(){    return this.nome;   }
    //  Restituisce tutti i giocatori presenti nella squadra in un array di <Giocatore> passato come parametro
    public void getGiocatori(Giocatore[] giocatori){
        for(int i=0; i<24; i++)
            giocatori[i] = new Giocatore(this.giocatori[i]);
    }    
    //  Restituisce un vettore con tutti i giocatori presenti nella squadra
    public Giocatore[] getGiocatori( ){
        Giocatore[] giocatori = new Giocatore[24];
        for(int i=0; i<24; i++)
            giocatori[i] = new Giocatore(this.giocatori[i]);
        
        return giocatori;
    }
    //  Restituisce un oggetto <Giocatore> contente le informazioni del giocatore richiesto
    public Giocatore getGiocatore(int numero_maglia){
        Giocatore g = null;
        int i = cerca(numero_maglia);   //  Ricerca dell'elemento
        //  Controlla che l'elemento sia presente
        if(i < this.giocatori.length)
            g = new Giocatore(this.giocatori[i]);    //  Copia dell'oggetto richiesto
        //  Restituisce l'elemento cercato (a null se non è stato trovato)
        return g;
    }
    //  Restituisce un oggetto <Giocatore> contente le informazioni del giocatore richiesto
    public Giocatore getGiocatore(String nome, String cognome){
        Giocatore g = null;
        //  Ciclo di ricerca
        for (Giocatore item : this.giocatori)
            if (item.getNome().equals(nome) && item.getCognome().equals(cognome))
                g = new Giocatore(item);    //  Copia dell'oggetto richiesto                    
        //  Restituisce l'elemento cercato (a null se non è stato trovato)
        return g;
    }
    //  Restituisce il valore di attacco della squadra
    public int getAttacco(){
        int n = formazione%10;  //  Prende l'lultima cifra a destra di <formazione>, quella che indica il numero di attaccanti
        int attacco = 0;        //  Inizializza il valore
        //  Ciclo dove somma i valori di <attacco> degli attaccanti
        for(int i=0; i<n; i++)
                attacco += this.giocatori[11-i].getAttacco();        
        //  Restituisce il valore calcolato
        return attacco;
    }
    //  Restituisce il valore del centrocampo della squadra
    public int getCentrocampo(){
        int da = formazione/100;        //  Prende l'ultima cifra a sinistra di <formazione>
        int a = da +(formazione/10)%10; //  Prende la cifra centrale di <formazione>
                                        //  (es: la formazione è 433, il vettore è lungo 11. Nelle prime 4 posizioni abbiamo i difensiore,
                                        //      nelle successive 3 abbiamo centrocampisti e nelle rimanenti abbiamo gli attaccanti. Per cui,
                                        //      nel vettore, i centrocampisti occupano dalla 5° posizione (cioè, nel vettore sarà l'indice 4) 
                                        //      alla 7° posizione (cioè, nel vettore l'indice 6). Si partirà da 4 (ultima cifra di <formazione>)
                                        //      fino a minore di 7(4 + la cifra centrale di <formazione>);
        int centrocampo = 0;            //  Inizializza il valore
        //  Ciclo dove somma i valori di <centrocampo> dei centrocampisti
        for(int i=da; i<a; i++)
                centrocampo += this.giocatori[i].getCentrocampo();
        //  Restituisce il valore calcolato
        return centrocampo;
    }
    //  Restituisce il valore della difesa della squadra
    public int getDifesa(){
        int n=formazione/100;   //  Prende l'lultima cifra a sinistra di <formazione>
        int difesa = 0;         //  Inizializza il valore
        //  Ciclo dove somma i valori di <difesa> dei difensori
        for(int i=0; i<n; i++)
                difesa += this.giocatori[i].getDifesa();
        //  Restituisce il valore calcolato
        return difesa;
    }
    //  Restituisce il valore del portiere della squadra
    public int getPortiere(){
        return this.giocatori[0].getPortiere();                
    }
    //  Restituisce i gol fatti
    public int getGolFatti(){   return this.golFatti;   }
    //  Restituisce i gol subiti
    public int getGolSubiti(){  return this.golSubiti;  }
    //  Restituisce la posizione della squadra nel campionato
    public Posizione getPosizione(){   return this.posizione;  }
    //  Ricerca un giocatore nella squadra attraverso il numero della maglia
    private int cerca(int n){
        int i;
        for(i=0; i<24 && n!=this.giocatori[i].getNumero(); i++);
        //  Restituisce l'indice della posizione in cui ha trovato il giocatore
        return i;
    }
    //  Scambia la posizione di due giocatori all'interno del vettore che
    //      memorizza i giocatori, in base al numero della maglia
    private void scambia(int n_g1, int n_g2){
        Giocatore g = this.giocatori[n_g1];
        this.giocatori[n_g1] = this.giocatori[n_g2];
        this.giocatori[n_g2] = g;
    }
    //  Aumenta la posizione della squadra
    public void nextPos(){
        this.posizione.nextPos();
    }
    //  Stampa le informazioni della squadra
    @Override
    public String toString(){
        String s = "Squadra " + this.nome + "\ngol fatti: " + this.golFatti +
                    "\ngol subiti: " + this.golSubiti + "\nPosizione: " +
                    this.posizione.getTurnoString() + "\nGiocatori:\n";
        
        for (Giocatore g : this.giocatori) {
            s += g + "\n";
        }
            
        return s;
    }
    //  Confronta l'istanza con un altro oggetto
    @Override
    public boolean equals(Object squadra){
        boolean b = true;
        int i;
        Squadra s = (Squadra)squadra;
        //  Controlla che i nomi delle due squadre siano uguali
        if(!this.nome.equals(s.getNome()))
            b = false;        
        //  Ciclo di confronto dei giocatori
        for(int j=0; j<24 && b; j++){
            //  Confronta i giocatori delle due squadre
            for(i=0; i<24 && !this.giocatori[i].equals(s.giocatori[i]); i++);
            //  Se non è arrivato alla fine significa che non sono uguali
            if(i != 24) b = false;
        }
        
        return b;
    }
}