package progetto1.pkg0;

import java.awt.Frame;                  //  Utilizzato per verificare la presenza di altri frame(in GestoreEtichette.MouseClicked)
import java.awt.event.MouseEvent;       //  Utilizzato per gestire gli eventi causati dal mouse(in GestoreEtichette)
import java.awt.event.MouseListener;    //  Utilizzato per creare un nuovo gestore di eventi del mouse
import java.io.IOException;             //  Utilizzato per la gestione delle eccezioni in IO
import java.sql.SQLException;           //  Utilizzato per la gestione degli errori dovuti ai comandi sql
import javax.swing.BorderFactory;       //  Utilizzato per modificare l'estetica delle etichette(in GestoreEtichette.MouseEntered e in GestoreEtichette.MouseExited)
import javax.swing.JButton;             //  Utilizzato per definire un pulsante(in Gestori)
import javax.swing.JLabel;              //  Utilizzato per definire le etichette(in GestoreEtichette)

/*
*   Classe che si occupa di modellare il comportamento di un campionato ad eliminazione, rappresentandolo su di una finestra
*/

public class Campionato{
    private int anno;               //  Variabile che definisce l'anno in cui si è disputato il campionato
    private Squadra squadre[];      //  Vettore che memorizza le squadre che hanno partecipato al campionato
    private Partita partite[];      //  Vettore che memorizza le partite del campionato
    private Interfaccia gui;        //  Variabile che sei occupa di gestire l'interfaccia
    private Posizione tipoPartita;  //  Indica il turno a cui è arrivato il campionato
    private final IstanzaUnica iu;  //  Verifica che vi sia un'istanza unica dell'applicazione
    private MySQL mysql;            //  Si occupa della comunicazione col database
    //  Costruttore senza parametri che riparte dall'ultimo campionato, il resto preso dal database "standard"
    public Campionato(){
        //  Variabile per garantire l'unicità dell'applicazione
        iu = new IstanzaUnica("Campionato_Mondiale");
        //  Verifica se esiste già l'istanza dell'applicazione...
        if(iu.isActivated()){        
            //  ...se vero, allora stampa l'avviso
            javax.swing.JOptionPane.showMessageDialog(null, "Applicazione già avviata", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        else{
            // ...altrimenti segue il normale corso
            try{
                int corrAnno;   //  Variabile di appoggio dove memorizzo l'anno dell'ultimo campionato disputato
                                //      o che si sta disputando
                //  Istanzia il nuovo oggetto per gestire la connessione al database
                this.mysql = new MySQL("root", "", "localhost/progetto");
                //  Inizializzo la variabile di appoggio prendendo l'informazione dal database
                corrAnno = this.mysql.getCorrAnno();
                //  Se è il primo campionato che si disputa...
                if(corrAnno == 0){
                    //  ...allora viene aggiunto subito al database                
                    mysql.putCampionato(2014, mysql.getSquadre());
                    //  e inizializza il campionato all'anno "standard" (2014)
                    this.initCampionato(2014);
                }
                //  altrimenti, se non è il primo...
                else
                    //  ...inizializza il campionato all'ultimo campionato disputato o che si sta disputando
                    this.initCampionato(corrAnno);
            }
            //  Gestisce le eccezioni proprie del campionato
            catch(MieEccezioni my_ex){
                //  Manda un messaggio di errore
                javax.swing.JOptionPane.showMessageDialog(null, my_ex, "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
                //  Se l'errore è stato causato dopo la creazione della finestra...
                if(this.gui != null)
                    //  ...poiché c'è stato un errore, disabilita il pulsante, cioè non dà la possibilità di continuare il campionato
                    this.gui.DisableButton();
            }
            //  Gestione dei problemi legati al database (inteso come struttura, quindi problemi nella connessione, scrittura, lettura, ecc.)
            catch(ClassNotFoundException | SQLException | InstantiationException | 
                    IllegalAccessException | IOException ex){
                //  Manda un messaggio di errore
                javax.swing.JOptionPane.showMessageDialog(null, "Salvataggio corrotto in Campionato()", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);                
                //  Se l'errore è stato causato dopo la creazione della finestra...
                if(this.gui != null)
                    //  ...rende il pulsante inattivo poiché, essendoci stato un errore, non è possibile giocare
                    this.gui.DisableButton();
            }
            //  Gestione del caso in cui siano presenti riferimenti nulli
            catch(java.lang.NullPointerException np_ex){
                //  Manda un messaggio di errore
                javax.swing.JOptionPane.showMessageDialog(null, "Salvataggio squadre corrotto", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
                //  Se l'errore è stato causato dopo la creazione della finestra...
                if(this.gui != null)
                    //  ...rende il pulsante inattivo poiché, essendoci stato un errore, non è possibile giocare
                    this.gui.DisableButton();
            }
            //  Gestione di un'eccezione generica. Essendo un errore non previsto si preferisce chiudere l'applicazione.
            catch(Exception ex){
                javax.swing.JOptionPane.showMessageDialog(null, "Errore nel caricamento del campionato! Impossibile avviare!", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);                
                //  Errore generico, non gestibile. Si preferisce chiudere il programma.
                System.exit(1);
            }
        }
    }    
    //  Costruttore con solo parametro <anno>, il resto preso dal database "standard"
    public Campionato(int anno){
        //  Variabile per garantire l'unicità dell'applicazione
        iu = new IstanzaUnica("Campionato_Mondiale");
        //  Verifica se esiste già l'istanza dell'applicazione...
        if(iu.isActivated()){        
            //  ...se vero, allora stampa l'avviso
            javax.swing.JOptionPane.showMessageDialog(null, "Applicazione già avviata", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        else{
            // ...altrimenti segue il normale corso
            try{
                //  Istanzia il nuovo oggetto per gestire la connessione al database
                this.mysql = new MySQL("root", "", "localhost/progetto");
                //  Controlla che l'anno passato come parametro sia presente nel database, allora...
                if(this.mysql.isPresent(anno))
                    //  ...inizializza il campionato secondo l'anno passatogli come parametro
                    this.initCampionato(anno);
                else
                    throw new MieEccezioni("Data inserita non valida!");
            }
            //  Gestisce le eccezioni proprie del campionato
            catch(MieEccezioni my_ex){
                //  Manda un messaggio di errore
                javax.swing.JOptionPane.showMessageDialog(null, my_ex, "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);               
                //  Se l'errore è stato causato dopo la creazione della finestra...
                if(this.gui != null)
                    //  ...poiché c'è stato un errore, disabilita il pulsante, cioè non dà la possibilità di continuare il campionato
                    this.gui.DisableButton();
            }
            //  Gestione dei problemi legati al database (inteso come struttura, quindi problemi nella connessione, scrittura, lettura, ecc.)
            catch(ClassNotFoundException | SQLException | InstantiationException | 
                    IllegalAccessException | IOException ex){
                //  Manda un messaggio di errore
                javax.swing.JOptionPane.showMessageDialog(null, "Salvataggio corrotto", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);                                
                //  Se l'errore è stato causato dopo la creazione della finestra...
                if(this.gui != null)
                    //  ...rende il pulsante inattivo poiché, essendoci stato un errore, non è possibile giocare
                    this.gui.DisableButton();
            }
            //  Gestione del caso in cui siano presenti riferimenti nulli
            catch(java.lang.NullPointerException np_ex){
                //  Manda un messaggio di errore
                javax.swing.JOptionPane.showMessageDialog(null, "Salvataggio squadre corrotto", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
                //  Se l'errore è stato causato dopo la creazione della finestra...
                if(this.gui != null)
                    //  ...rende il pulsante inattivo poiché, essendoci stato un errore, non è possibile giocare
                    this.gui.DisableButton();
            }
            //  Gestione di un'eccezione generica. Essendo un errore non previsto si preferisce chiudere l'applicazione.
            catch(Exception ex){
                javax.swing.JOptionPane.showMessageDialog(null, "Errore nel caricamento del campionato! Impossibile avviare!", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);                
                //  Errore generico, non gestibile. Si preferisce chiudere il programma.
                System.exit(1);
            }
        }
    }
    //  Costruttore che riparte dall'ultimo campionato, il resto preso dal database definito dai parametri
    public Campionato(String user, String password, String nome){
        //  Variabile per garantire l'unicità dell'applicazione
        iu = new IstanzaUnica("Campionato_Mondiale");
        //  Verifica se esiste già l'istanza dell'applicazione...
        if(iu.isActivated()){        
            //  ...se vero, allora stampa l'avviso
            javax.swing.JOptionPane.showMessageDialog(null, "Applicazione già avviata", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        else{
            // ...altrimenti segue il normale corso
            try{
                int corrAnno;   //  Variabile di appoggio dove memorizzo l'anno dell'utlimo campionato disputato
                                //      o che si sta disputando
                //  Istanzia il nuovo oggetto per gestire la connessione al database
                this.mysql = new MySQL(user, password, nome);
                //  Inizializzo la variabile prendendo l'anno dal database
                corrAnno = this.mysql.getCorrAnno();
                //  Se è il primo campionato che si disputa...
                if(corrAnno == 0){
                    //  ...allora viene aggiunto subito al database                
                    mysql.putCampionato(2014, mysql.getSquadre());
                    //  e inizializza il campionato all'anno "standard" (2014)
                    this.initCampionato(2014);
                }
                //  altrimenti, se non è il primo...
                else
                    //  ...inizializza il campionato all'ultimo campionato disputato o che si sta disputando
                    this.initCampionato(corrAnno);           
            }
            //  Gestisce le eccezioni proprie del campionato
            catch(MieEccezioni my_ex){
                //  Manda un messaggio di errore
                javax.swing.JOptionPane.showMessageDialog(null, my_ex, "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
                //  Se l'errore è stato causato dopo la creazione della finestra...
                if(this.gui != null)
                    //  Poiché c'è stato un errore, disabilita il pulsante, cioè non dà la possibilità di continuare il campionato
                    this.gui.DisableButton();
            }
            //  Gestione dei problemi legati al database (inteso come struttura, quindi problemi nella connessione, scrittura, lettura, ecc.)
            catch(ClassNotFoundException | SQLException | InstantiationException | 
                    IllegalAccessException | IOException ex){
                //  Manda un messaggio di errore
                javax.swing.JOptionPane.showMessageDialog(null, "Salvataggio corrotto", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);                
                //  Se l'errore è stato causato dopo la creazione della finestra...
                if(this.gui != null)
                    //  Rende il pulsante inattivo poiché, essendoci stato un errore, non è possibile giocare
                    this.gui.DisableButton();
            }
            //  Gestione del caso in cui siano presenti riferimenti nulli
            catch(java.lang.NullPointerException np_ex){
                //  Manda un messaggio di errore
                javax.swing.JOptionPane.showMessageDialog(null, "Salvataggio squadre corrotto", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
                //  Se l'errore è stato causato dopo la creazione della finestra...
                if(this.gui != null)
                    //  Rende il pulsante inattivo poiché, essendoci stato un errore, non è possibile giocare
                    this.gui.DisableButton();
            }
            //  Gestione di un'eccezione generica. Essendo un errore non previsto si preferisce chiudere l'applicazione.
            catch(Exception ex){
                javax.swing.JOptionPane.showMessageDialog(null, "Errore nel caricamento del campionato! Impossibile avviare!", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);                
                //  Errore generico, non gestibile. Si preferisce chiudere il programma.
                System.exit(1);
            }
        }
    }
    //  Costruttore seleziona l'anno definito dal parametro, il resto preso dal database definito dai parametri
    public Campionato(String user, String password, String nome, int anno){
        //  Variabile per garantire l'unicità dell'applicazione
        iu = new IstanzaUnica("Campionato_Mondiale");
        //  Verifica se esiste già l'istanza dell'applicazione...
        if(iu.isActivated()){        
            //  ...se vero, allora stampa l'avviso
            javax.swing.JOptionPane.showMessageDialog(null, "Applicazione già avviata", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        else{
            // ...altrimenti segue il normale corso
            try{
                //  Istanzia il nuovo oggetto per gestire la connessione al database
                mysql = new MySQL(user, password, nome);
                //  Controlla che l'anno richiesto sia presente nel database
                if(this.mysql.isPresent(anno))
                    //  Inizializza il campionato secondo l'anno passatogli come parametro
                    initCampionato(anno);
                else
                    throw new MieEccezioni("Data inserita non valida!");
            }
            //  Gestisce le eccezioni proprie del campionato
            catch(MieEccezioni my_ex){
                //  Manda un messaggio di errore
                javax.swing.JOptionPane.showMessageDialog(null, my_ex, "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
                //  Se l'errore è stato causato dopo la creazione della finestra...
                if(this.gui != null)
                    //  Poiché c'è stato un errore, disabilita il pulsante, cioè non dà la possibilità di continuare il campionato
                    this.gui.DisableButton();
            }
            //  Gestione dei problemi legati al database (inteso come struttura, quindi problemi nella connessione, scrittura, lettura, ecc.)
            catch(ClassNotFoundException | SQLException | InstantiationException | 
                    IllegalAccessException | IOException ex){
                //  Manda un messaggio di errore
                javax.swing.JOptionPane.showMessageDialog(null, "Salvataggio corrotto", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);                
                //  Se l'errore è stato causato dopo la creazione della finestra...
                if(this.gui != null)
                    //  Rende il pulsante inattivo poiché, essendoci stato un errore, non è possibile giocare
                    this.gui.DisableButton();
            }
            //  Gestione del caso in cui siano presenti riferimenti nulli
            catch(java.lang.NullPointerException np_ex){
                //  Manda un messaggio di errore
                javax.swing.JOptionPane.showMessageDialog(null, "Salvataggio squadre corrotto", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
                //  Se l'errore è stato causato dopo la creazione della finestra...
                if(this.gui != null)
                    //  Rende il pulsante inattivo poiché, essendoci stato un errore, non è possibile giocare
                    this.gui.DisableButton();
            }
            //  Gestione di un'eccezione generica. Essendo un errore non previsto si preferisce chiudere l'applicazione.
            catch(Exception ex){
                javax.swing.JOptionPane.showMessageDialog(null, "Errore nel caricamento del campionato! Impossibile avviare!", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);                
                //  Errore generico, non gestibile. Si preferisce chiudere il programma.
                System.exit(1);
            }
        }
    }
    
    //  Metodo che inizializza il campionato secondo l'anno passato come parametro
    private void initCampionato(int anno) throws ClassNotFoundException, SQLException, InstantiationException, 
                IllegalAccessException, IOException, MieEccezioni, java.lang.NullPointerException{                
        //  Inizializza le variabili di <Campionato>
        this.anno = anno;
        //  Si estraggono le informazioni delle squadre che stanno partecipando al campionato
        this.squadre = mysql.getSquadreAnno(this.anno);
        this.partite = new Partita[0];
        this.tipoPartita = this.mysql.getTurno(this.anno);               
        //  Essendo <Interfaccia> interna a <Campionato>, se <Interfaccia> 
        //      già era stata definita non serve definirne un'altra.
        if(gui == null){
            //  Viene creata la nuova finestra in cui viene stampato il tabellone
            this.gui = new Interfaccia(new Gestori().new GestorePulsante(), new Gestori().new GestoreEtichette());
            //  Ci sono partite salvate se il campionato considerato non si trova negli "Ottavi"
            if(this.tipoPartita.getTurnoInt() != 0)
                //  Estrae dal database le partite giocate
                this.partite = mysql.getPartite(this.anno);
        }
        //  Si stampa sul tabellone l'anno del campionato
        this.gui.setAnno(anno);
        //  Se il campionato era già stato cominciato...
        if(this.partite.length > 0){            
            //  ...si inizializza il tabellone
            initGUI(this.squadre);
            //  Si riempe il tabellone con le partite giocate fino al turno da giocare
            printPartite(this.partite);
            //  Aggiorna il pulsante di conseguenza
            if(tipoPartita.getTurnoString().equals("Quarti"))
                gui.setTextButton("Gioca Quarti di Finale"); 

                else if(tipoPartita.getTurnoString().equals("Semi"))
                    gui.setTextButton("Gioca Semi-Finali");

                    else if(tipoPartita.getTurnoString().equals("Finale"))
                        gui.setTextButton("Gioca Finale");

                        else if(tipoPartita.getTurnoString().equals("Vincitore"))
                            gui.setTextButton("Gioca Campionato " + (anno + 4));                
        }
        else{
            //  ...altrimenti si parte con un nuovo campionato            
            //  Si inizializza il tabellone con le squadre estratte dal database
            initGUI(this.squadre);
        }
    }
    //  Restituisce l'anno del campionato considerato
    public int getAnno(){   return this.anno;   }
    //  Restituisce gli oggetti di tutte le squadre partecipanti
    public Squadra[] getSquadre() throws java.lang.NullPointerException{
        Squadra[] s = new Squadra[16];
        //  Ciclo di "estrazione" delle squadre con controllo che il puntatore non abbia elementi a "null"
        try{
            for(int i=0; i<this.squadre.length; i++)
                s[i] = new Squadra(this.squadre[i]); 
        }
        //  Gestisce il caso in cui uno degli elementi sia un riferimento a "null"...
        catch(java.lang.NullPointerException ex){
            //  ...rilanciandolo al chiamante
            throw  new java.lang.NullPointerException();
        }
        
        return s;
    }    
    //  Restituisce l'oggetto di una squadra partendo dal suo nome
    public Squadra getSquadra(String nome) throws java.lang.NullPointerException{
        Squadra s = null;
        //  Ciclo di "estrazione" della squadra
        try{
            for(int i=0; i<this.squadre.length; i++)
                if(nome.equals(this.squadre[i].getNome())){
                    s = new Squadra(this.squadre[i]);
                }
        }
        //  Gestisce il caso in cui uno degli elementi sia un riferimento a "null"...
        catch(java.lang.NullPointerException ex){
            //  ...rilanciandolo al chiamante
            throw  new java.lang.NullPointerException();
        }
        //  Restituisce la copia della squadra al chiamante
        return s;
    }
    //  Restituisce il riferimento di una squadra partendo dal suo nome
    private Squadra getRifSquadra(String nome) throws java.lang.NullPointerException{
        Squadra s = null;
        //  Ciclo di "estrazione"
        try{
            for(int i=0; i<this.squadre.length; i++)
                if(nome.equals(this.squadre[i].getNome()))
                    s = this.squadre[i];
        }
        //  Gestisce il caso in cui uno degli elementi sia un riferimento a "null"...
        catch(java.lang.NullPointerException ex){
            //  ...rilanciandolo al chiamante
            throw  new java.lang.NullPointerException();
        }
        //  Restituisce il riferimento della squadra al chiamante
        return s;
    }
    
    // Metodo che si occupa dello svolgimento delle partite di un turno
    public void Gioca(){
        int n = (int)Math.pow(2, (3 - this.tipoPartita.getTurnoInt()));  //  Per ogni turno c'è un numero fisso di partite 
                                                                    //      (Ottavi -> 8, Quarti -> 4, Semi -> 2, Finale -> 1)
        int prev_len = this.partite.length;                         //  Tiene la lunghezza del vettore delle partite prima che venga ridimensionato
        Squadra[] passano = new Squadra[n*2];                       //  Vettore che contiene le squadre che hanno precedentemente passato il turno
        try{
            //  Recupera dal tabellone le squadre che hanno passato il turno
            for(int i=0; i<n*2; i++)
                passano[i] = getRifSquadra(this.gui.getTextSquadre(this.tipoPartita, i));
            //  Allunga il vettore per contenere le future partite di questo turno
            this.partite = (Partita[])java.util.Arrays.copyOf(this.partite, prev_len+n);                        
            //  Ciclo che svolge tutte le partite del turno
            for(int i=0; i<n*2; i+=2){
                //  Inizializza un nuovo oggetto per gestire una partita
                this.partite[prev_len + i/2] = new Partita(passano[i], passano[i+1], 0, 0, this.tipoPartita, i, this.anno);                                
                //  Gioca la partita
                this.partite[prev_len + i/2].Gioca();                                    
                //  Aggiorna i gol fatti e subiti aggiungendo il risultato di questa partita (squadra di casa)
                passano[i].setGolFatti(passano[i].getGolFatti() + this.partite[prev_len + i/2].getGolCasa());
                passano[i].setGolSubiti(passano[i].getGolSubiti() + this.partite[prev_len + i/2].getGolOspite());
                //  Aggiorna i gol fatti e subiti aggiungendo il risultato di questa partita (squadra ospite)
                passano[i+1].setGolFatti(passano[i+1].getGolFatti() + this.partite[prev_len + i/2].getGolOspite());
                passano[i+1].setGolSubiti(passano[i+1].getGolSubiti() + this.partite[prev_len + i/2].getGolCasa());
                //  Inserisce questa partita nel database
                this.mysql.putPartita(this.partite[prev_len + i/2]);
                //  Controlla quale delle due squadre ha vinto, così che viene aggiornata la sua posizione
                if(this.partite[prev_len + i/2].getGolCasa() >= this.partite[prev_len + i/2].getGolOspite())
                    passano[i].nextPos();                    
                else
                    passano[i+1].nextPos();                    
            }
            //  Aggiorna il database con le informazioni delle squadre dopo la partita
            this.mysql.updateSquadre(passano, this.anno);
            //  Stampa sul tabellone il turno appena giocato
            PrintTurno();
            //  Questo turno è finito, passa al successivo
            this.tipoPartita.nextPos();
        }
        //  Gestisce le eccezioni proprie del campionato
        catch(MieEccezioni my_ex){
            javax.swing.JOptionPane.showMessageDialog(null, my_ex, "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
            //  Poiché si è verificato un errore, disabilita il pulsante
            this.gui.DisableButton();
        }
        //  Gestione dei problemi legati al database (inteso come struttura, quindi problemi nella connessione, scrittura, lettura, ecc.)
        catch(ClassNotFoundException | SQLException | InstantiationException | 
                    IllegalAccessException | IOException ex){
                javax.swing.JOptionPane.showMessageDialog(null, "Errore. Impossibile salvare!", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
                //  Poiché si è verificato un errore, disabilita il pulsante
                this.gui.DisableButton();
        }
        //  Gestione del caso in cui siano presenti riferimenti nulli
        catch(java.lang.NullPointerException np_ex){
            javax.swing.JOptionPane.showMessageDialog(null, "Errore. Memoria squadre corrotta!", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
        }        
    }
    //  Metodo che si occupa della stampa del turno attuale del campionato attuale sul tabellone
    private void PrintTurno(){
        int n = 0;                                                          //  Da quale posizione considerare il vettore delle partite
        int a = (int)Math.pow(2, (4 - this.tipoPartita.getTurnoInt()));     //  Calcola quante squadre hanno giocato in questo turno
                                                                            //      (Ottavi -> 16, Quarti -> 8, Semi -> 4, Finale -> 2)
        int i;                                                              //  Variabile indice per i cicli
        Posizione tp;                                                       //  Variabile che indica quel'è il turno successivo
        //  Ciclo di calcolo da quale posizione considerare il vettore delle partite
        for(i=0; i<this.tipoPartita.getTurnoInt(); i++)
            n += (int)Math.pow(2, (3 - i));
                
        try{
            tp = new Posizione(this.tipoPartita.getTurnoInt() + 1);
            //  Ciclo di stampa sul tabellone delle partite
            for(i=0; i<a; i+=2){                                
                //  Stampa i gol fatti dalla squadra di casa sulla propria etichetta
                this.gui.setTextRisultato(String.valueOf(this.partite[n+i/2].getGolCasa()), this.tipoPartita, i);
                //  Stampa i gol fatti dalla squadra ospite sulla propria etichetta
                this.gui.setTextRisultato(String.valueOf(this.partite[n+i/2].getGolOspite()), this.tipoPartita, i+1);
                //  In base al risultato, stampa la squadra vincente nell'etichetta del turno successivo
                if(this.partite[n+i/2].getGolCasa() >= this.partite[n+i/2].getGolOspite())
                    this.gui.setTextSquadre(this.partite[n+i/2].getSquadraCasa().getNome(), tp, i/2);
                else
                    this.gui.setTextSquadre(this.partite[n+i/2].getSquadraOspite().getNome(), tp, i/2);
            }
        }
        catch(MieEccezioni my_ex){
            javax.swing.JOptionPane.showMessageDialog(null, my_ex, "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        catch(java.lang.NullPointerException np_ex){                        
            javax.swing.JOptionPane.showMessageDialog(null, "Errore di lettura!", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);            
        }
    }
    //  Metodo che inizializza il tabellone, cioè scrive i nomi delle squadre sulle etichette degli ottavi
    private void initGUI(Squadra[] p){
        //  Si esegue il controllo che siano state passate 16 squadre (in seguito, è stato un ulteriore controllo
        //      che nessun elemento sia nullo)
        if(p.length == 16){
            //  Cancella tutto il contenuto del tabellone
            this.gui.svuota();
            try{                
                //  Ciclo di scrittura di scrittura dei nomi delle squadre sulle etichette degli ottavi
                for(int i=0; i<16; i+=2){
                    this.gui.setTextSquadre(p[i].getNome(), new Posizione(), i);
                    this.gui.setTextSquadre(p[i+1].getNome(), new Posizione(), i+1);
                }
            }
            //  Gestisce l'eccezione nel caso, nel vettore, vi sia un elemento nullo
            catch(java.lang.NullPointerException ex){
                javax.swing.JOptionPane.showMessageDialog(null, "Salvataggio squadre corrotto", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
                //  Poichè si è verificato un errore, non si può più giocare questo campionato
                this.gui.DisableButton();
            }
            catch(MieEccezioni my_ex){
                javax.swing.JOptionPane.showMessageDialog(null, my_ex, "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    //  Metodo che stampa sul tabellone il campionato dell'anno selezionato
    public void printCampionatoAnno(int anno){
        Partita[] p;        
        //  Controllo sui comandi sul database
        try{
            //  Se il campionato stampato non è quello in corso...
            if(anno != this.anno){
                //  ...inizializza il pulsante per il "ritorno"
                this.gui.setTextButton("Torna al campionato corrente");
                //  Nel caso fosse stato disabilitato, viene riabilitato
                this.gui.EnableButton();
            }
            //  Stampa sul tabellone l'anno selezionato
            this.gui.setAnno(anno);
            //  Estrae tutte le partite giocate nell'anno selezionato
            p = this.mysql.getPartite(anno);                                             
            //  Inizializza il tabellone
            initGUI(this.mysql.getSquadreAnno(anno));
            //  Stampa tutte le informazioni estratte dal database sul tabellone
            printPartite(p);            
        }
        //  Gestore di eccezioni del campionato
        catch(MieEccezioni my_ex){
            //  Se l'anno è l'anno del campionato che si sta disputando, poiché c'è stato un errore, disabilita il pulsante.
            //      Se l'anno è diverso, lascia la possibilità di tornare al campionato corrente.
            if(anno == this.anno)
                this.gui.DisableButton();
            //  Cancella tutto il contenuto del tabellone
            this.gui.svuota();
            //  Stampa un messaggio di errore
            javax.swing.JOptionPane.showMessageDialog(null, my_ex, "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        //  Gestione dei problemi legati al database (inteso come struttura, quindi problemi nella connessione, scrittura, lettura, ecc.)
        catch(ClassNotFoundException | SQLException | InstantiationException | 
                IllegalAccessException | IOException ex){
            //  Se l'anno è l'anno del campionato che si sta disputando, poiché c'è stato un errore, disabilita il pulsante.
            //      Se l'anno è diverso, lascia la possibilità di tornare al campionato corrente.
            if(anno == this.anno)
                this.gui.DisableButton();
            //  Cancella tutto il contenuto del tabellone
            this.gui.svuota();
            //  Stampa un messaggio di errore
            javax.swing.JOptionPane.showMessageDialog(null, "Problema di connessione al salvataggio", "In Principale.java ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }   
    }
    //  Stampa un campionato sul tabellone
    private void printPartite(Partita[] p){
        int num;        //  Indica il numero dell'etichetta su cui stampare
        Posizione pos;  //  Indica il turno da considerare
        try{
            //  Ciclo di stampa delle informazioni
            for (Partita p1 : p) {
                //  Dalla partita considerata, estrae i gol dei padroni di casa e i gol degli ospiti
                int gc = p1.getGolCasa();
                int go = p1.getGolOspite();                    
                //  Viene memorizzato il turno della partita presa in considerazione...
                pos = new Posizione(p1.getTipoPartita().split("_")[0]);
                //  ...e il numero della partita
                num = Integer.decode(p1.getTipoPartita().split("_")[1]);                                    
                //  Scrive sul tabellone il risultato della partita
                this.gui.setTextRisultato(String.valueOf(gc), pos, num);
                this.gui.setTextRisultato(String.valueOf(go), pos, num+1);                                   
                //  Se si è giocata la finale...
                if (pos.getTurnoString().equals("Finale")) {
                    //  ...scrive sul tabellone il vincitore
                    if (p1.getGolCasa() >= p1.getGolOspite()) {
                        this.gui.setTextSquadre(p1.getSquadraCasa().getNome(), new Posizione("Vincitore"), 0);
                    } else {
                        this.gui.setTextSquadre(p1.getSquadraOspite().getNome(), new Posizione("Vincitore"), 0);
                    }
                }
                //  Aggiunge il nome delle squadre alla etichette del turno successivo se le partite di
                //      quest'ultimo non sono state disputate
                pos.nextPos();
                //  In base al risultato, scrive la squadra vincente nell'etichetta del turno successivo
                if(gc >= go)
                    this.gui.setTextSquadre(p1.getSquadraCasa().getNome(), pos, num/2);
                else
                    this.gui.setTextSquadre(p1.getSquadraOspite().getNome(), pos, num/2);            
            }
        }
        catch(MieEccezioni my_ex){
            javax.swing.JOptionPane.showMessageDialog(null, my_ex, "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }        
    //  Stampa le informazioni generali del campionato (cioè l'anno del campionato + la lista delle squdare partecipanti)
    @Override
    public String toString(){
        //  Stampa le informazioni generali del campionato
        String s = "Campionato " + this.anno + "\nSquadre:\n" + this.squadre[0].getPosizione().getTurnoString() + "\n";
        //  Stampa le informazioni delle squadre
        for (Squadra sq : this.squadre) {
            s += sq + "\n";
        }
        return s;
    }
    //  Classe Interna in cui sono gestiti tutti gli eventi necessari all'applicazione
    class Gestori{
        private Object o = null;
        //  Classe che gestisce la premuta del pulsante del tabellone
        class GestorePulsante implements java.awt.event.ActionListener{        
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e){
                //  Viene estratto il pulsante che ha generato l'evento
                JButton button = (JButton)e.getSource();
                //  Viene estratta l'istanza di campionato su cui si è generato l'evento
                Campionato c = Campionato.this;            
                //  Se si stava visualizzando un campionato passato...
                if(button.getText().equals("Torna al campionato corrente")){
                    //  ...si torna al campionato in svolgimento
                    //  Si stampa l'anno del campionato in svolgimento
                    c.gui.setAnno(c.anno);
                    //  Si controlla il turno a cui si era arrivati e in base a quseto si inizializza il pulsante
                    if(c.tipoPartita.getTurnoString().equals("Ottavi"))
                        c.gui.setTextButton("Gioca Ottavi di Finale"); 

                        else if(c.tipoPartita.getTurnoString().equals("Quarti"))
                            c.gui.setTextButton("Gioca Quarti di Finale"); 

                            else if(c.tipoPartita.getTurnoString().equals("Semi"))
                                c.gui.setTextButton("Gioca Semi-Finali");

                                else if(c.tipoPartita.getTurnoString().equals("Finale"))
                                    c.gui.setTextButton("Gioca Finale");

                                    else if(c.tipoPartita.getTurnoString().equals("Vincitore"))
                                        c.gui.setTextButton("Gioca Campionato " + (c.anno + 4));

                    c.printCampionatoAnno(c.anno);
                }
                else{
                    //  Si gioca il turno successivo
                    if(c.tipoPartita.getTurnoString().equals("Ottavi")){               
                        button.setText("Gioca Quarti di Finale");
                        c.Gioca();
                    }
                        else if(c.tipoPartita.getTurnoString().equals("Quarti")){                
                            button.setText("Gioca Semi-Finali");
                            c.Gioca();
                        }
                            else if(c.tipoPartita.getTurnoString().equals("Semi")){                
                                button.setText("Gioca Finale");
                                c.Gioca();
                            }
                                else if(c.tipoPartita.getTurnoString().equals("Finale")){                                    
                                    button.setText("Gioca Campionato " + (c.anno + 4));
                                    c.Gioca();
                                }
                                //  Se il campionato è finito, inzializza il pulsante per cominciare un nuovo campionato
                                    else if(c.tipoPartita.getTurnoString().equals("Vincitore")){
                                        //  Controllo che l'applicazione non vada in crash per i comandi sul database
                                        try{
                                            //  Inserisce questo nuovo campionato
                                            c.mysql.putCampionato(c.getAnno() + 4, c.mysql.getSquadre());
                                            //  Inizializza il nuovo campionato
                                            c.initCampionato(c.anno + 4);                                            
                                            //  Inizializza il pulsante per giocare gli ottavi del nuovo campionato
                                            button.setText("Gioca Ottavi di Finale");
                                        }
                                        catch(ClassNotFoundException | SQLException | InstantiationException | 
                                                IllegalAccessException | IOException ex){
                                            javax.swing.JOptionPane.showMessageDialog(null, "Problema di connessione al salvataggio", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
                                            //  Poiché c'è stato un errore, disabilita il pulsante, cioè non dà la possibilità di continuare il campionato
                                            c.gui.DisableButton();
                                        }
                                        catch(MieEccezioni my_ex){
                                            javax.swing.JOptionPane.showMessageDialog(null, my_ex, "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
                                            //  Poiché c'è stato un errore, disabilita il pulsante, cioè non dà la possibilità di continuare il campionato
                                            c.gui.DisableButton();
                                        }
                                        catch(java.lang.NullPointerException np_ex){
                                            javax.swing.JOptionPane.showMessageDialog(null, "Salvataggio squadre corrotto", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
                                            //  Rende il pulsante inattivo poiché, essendoci stato un errore, non è possibile giocare
                                            c.gui.DisableButton();
                                        }
                                        catch(Exception ex){
                                            javax.swing.JOptionPane.showMessageDialog(null, "Errore nel caricamento", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
                                            //  Rende il pulsante inattivo poiché, essendoci stato un errore, non è possibile giocare
                                            c.gui.DisableButton();
                                        }
                                    }
                }
            }
        }
        //  Classe che gestisce gli eventi del mouse sulle etichette del tabellone
        class GestoreEtichette implements MouseListener{
            //  Gestori degli eventi del mouse sulle etichette
            @Override
            @SuppressWarnings({"empty-statement", "ResultOfObjectAllocationIgnored"})
            public void mouseClicked(MouseEvent m){
                JLabel jlb = (JLabel)m.getSource(); //  Estrae l'etichetta su cui è avvenuto l'evento
                //  Controlla se è stata premuta l'etichetta dell'anno o se una della squadra
                //  ...se è quella dell'anno (con controllo che la richiesta al database vada a buon fine)
                if(jlb.getName().equals("anno")){
                    try{
                        boolean b = true;
                        Integer anni[] = Campionato.this.mysql.getAnni();   //  Prende gli anni delle edizioni passate dal database
                        //  Controlla che ci siano edizioni precedenti
                        if(anni.length > 0){
                            //  Controlla che non sia già stata aperta un finestra di tipo InterfacciaSelAnno
                            for(Frame frame: javax.swing.JFrame.getFrames())
                                if(frame.getName().equals("Scegli un anno")){
                                    o = frame;
                                    b = false;
                                }
                            //  apre una nuova finestra <InterfacciaSelAnno> per permettere all'utente di scegliere quale anno
                            //      visualizzare sul tabellone  (gli anni vengono presi dal database)
                            //  Se non è stata creata una finestra di tipo InterfacciaSelAnno...
                            if(b){
                                //  ...ne crea una
                                o = new InterfacciaSelAnno(anni, new GestoreSelAnno());
                            }
                            else{
                                //  ...se è stata creata ed è visibile, allora...
                                if(((InterfacciaSelAnno)o).isVisible()){
                                    //  ...gli passa il focus
                                    ((InterfacciaSelAnno)o).setVisible(true);
                                }
                                //  altrimenti...
                                else{
                                    //  ...libera le risorse occupate da quella finestra
                                    ((InterfacciaSelAnno)o).dispose();
                                    //  e ne crea una nuova
                                    o = new InterfacciaSelAnno(anni, new GestoreSelAnno());
                                }
                            }
                        }
                        //  altrimenti non si può scegliere nessuna data
                        else
                            javax.swing.JOptionPane.showMessageDialog(null, "Non esistono edizioni di campionato precedenti!", "Campionato Mondiale", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    }
                    catch(ClassNotFoundException | SQLException | InstantiationException | 
                                IllegalAccessException | IOException ex){
                            javax.swing.JOptionPane.showMessageDialog(null, "Problema di connessione", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
                            
                            if(o != null)
                                //  Disabilita il pulsante per la selezione dell'anno
                                ((InterfacciaSelAnno)o).DisableButton();
                    }
                    catch(Exception ex){
                        javax.swing.JOptionPane.showMessageDialog(null, "Problema nell'apertura della finestra!", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
                        if(o != null){
                            //  ...libera le risorse occupate da quella finestra
                            ((InterfacciaSelAnno)o).dispose();
                            o = null;
                        }
                    }
                }
                //  ...se è l'etichetta di una squadra (con controllo che il database non restituisca una squadra "nulla")
                //      (più un controllo per problemi generici)
                else{
                    try{
                        if(!jlb.getText().equals(""))
                            //  apre una nuova finestra dove stampa le informazioni sulla squadra
                            new InterfacciaSquadra(getSquadra(jlb.getText()));
                    }
                    catch(java.lang.NullPointerException np_ex){
                        javax.swing.JOptionPane.showMessageDialog(null, "Problema nella lettura del salvataggio", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                    catch(Exception ex){
                        javax.swing.JOptionPane.showMessageDialog(null, "Problema nell'apertura della finestra", "Campionato Mondiale", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                }
            }        
            @Override
            public void mouseEntered(MouseEvent m){
                JLabel jlb = (JLabel)m.getSource(); //  Estrae l'etichetta su cui è avvenuto l'evento
                //  Al passaggio sull'etichetta, se non vuota, il cursore cambia indicando che si può cliccare su di essa.
                //     Inoltre l'etichetta viene evidenziata.
                if(!jlb.getText().equals("")){
                    jlb.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
                    jlb.setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK, 2));
                }
            }
            @Override
            public void mouseExited(MouseEvent m){
                JLabel jlb = (JLabel)m.getSource(); //  Estrae l'etichetta su cui è avvenuto l'evento
                //  All'uscita dall'etichetta, se non vuota, si fa tornare l'etichetta normale
                if(!jlb.getText().equals(""))
                    jlb.setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK));                
            }
            @Override
            public void mousePressed(MouseEvent m){ }
            @Override
            public void mouseReleased(MouseEvent m){    }
        }
        //  Classe che gestisce la premuta delle pulsante della schermata di selezione dell'anno
        class GestoreSelAnno implements java.awt.event.ActionListener {
            //  Dopo la premuta del pulsante nell'interfaccia di InterfacciaSelAnno, stampa il campionato,
            //      dell'anno selezionato nella combobox, sul tabellone
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e){
                Campionato.this.printCampionatoAnno(((InterfacciaSelAnno)o).getSelectedItem());
            }
        }
    }
}