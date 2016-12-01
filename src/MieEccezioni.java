package progetto1.pkg0;

/*
*   Classe utilizzata per la stampa degli errori inerenti al corretto svolgimento del campionato
*/

public class MieEccezioni extends Exception{
    private final String info;
    public MieEccezioni() {
        info = "";
    }
    //  Costruttore che inizializza la frase da restituire al gestore dell'errore
    public MieEccezioni(String string) {
        info = string;
    }
    //  Restituisce il messaggio di errore
    @Override
    public String toString(){
        return info;
    }
}
