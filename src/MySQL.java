package progetto1.pkg0;

import java.io.IOException; //  Utilizzato per la gestione delle eccezioni in IO
import java.sql.*;          //  Utilizzato per rendere disponibili gli strumenti necessati per utilizzare un database

/*
*   Classe che si occupa di modellare la connessione al database in base alle esigenze del campionato
*/

public class MySQL {
    // Variabile che si occupa della connessione al database
    private final Connection db;
    // Variabili per le credenziali di accesso al database
    private final String user;
    private final String password;
    private final String nome;
    
    // Costruttore con parametri, per la connessione
    public MySQL(String user, String password, String nome) throws ClassNotFoundException, 
            SQLException, InstantiationException, IllegalAccessException, IOException{
        // Inizializzo i parametri per le credenziali di accesso al database secondo quanto passato dal chiamante
        this.user = user;
        this.password = password;
        this.nome = nome;
        //  Carico il driver JDBC di MySQL
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        //  Mi connetto al database secondo i dati inseriti
        this.db = DriverManager.getConnection("jdbc:mysql://" + this.nome , this.user, this.password);
    }
    
    //  Controlla che l'anno richiesto sia presente nel database
    public boolean isPresent(int anno) throws ClassNotFoundException, 
            SQLException, InstantiationException, IllegalAccessException, IOException{
        boolean b = false;  //  Inizializza la variabile a "false", cioè come se l'anno non ci fosse
        //  Query per la selezione dell'ultimo campionato giocato
        String query = "SELECT * FROM storicocampionato";
        
        try(Statement stmt = (Statement)this.db.createStatement();   ResultSet rs = stmt.executeQuery(query)){
            //  Ciclo di controllo
            while(rs.next()){
                //  Controlla se l'anno è nel database...
                if(anno == rs.getInt("anno"))
                    //  ...viene definita la variabile a true
                    b = true;
            }
        }
        //  Restituisce l'esito della ricerca
        return b;
    }
    //  Restituisce l'anno dell'ultimo campionato disputato
    
    public int getCorrAnno() throws ClassNotFoundException, 
            SQLException, InstantiationException, IllegalAccessException, IOException{
        int anno = 0;    //  Inizializza la variabile in caso non ci fossero campionati
        //  Query per la selezione dell'ultimo campionato giocato
        String query = "SELECT * FROM storicocampionato WHERE anno=(SELECT MAX(anno) FROM storicocampionato)";
        
        try(Statement stmt = (Statement)this.db.createStatement();   ResultSet rs = stmt.executeQuery(query)){
            //  Controlla che siano state disputati campionati...
            if(rs.next()){
                //  ...se è così, estrae l'anno dal database
                anno = rs.getInt("anno");            
            }
        }
        //  Restituisce l'anno trovato al chiamante
        return anno;
    }
    //  Restituisce gli anni in cui si è giocato il campionato
    public Integer[] getAnni() throws ClassNotFoundException, 
            SQLException, InstantiationException, IllegalAccessException, IOException{
        Integer[] anni = new Integer[0];                    //  Definisce il vettore in cui memorizzare temporaneamente gli anni
                                                            //      estratti dal database
        String query = "SELECT * FROM storicocampionato";   //  Definisce la query per controllare che siano stati disputati campionati
        
        try(Statement stmt = (Statement)this.db.createStatement();   ResultSet rs = stmt.executeQuery(query)){
            //  Ciclo di letture del database        
            while(rs.next()){
                //  Ridimensionamento dell'array conservando i vecchi elementi
                anni = (Integer[])java.util.Arrays.copyOf(anni, anni.length+1);                
                //  Inserimento dell'elemento estratto
                anni[anni.length-1] = rs.getInt("anno");
            }
            //  Se sono stati disputati più campionati, elimina dagli anni restituiti l'anno del campionato in corso
            if(anni.length > 0)
                //  Ridimensionamento dell'array eliminando l'anno del campionato in corso
                anni = (Integer[])java.util.Arrays.copyOf(anni, anni.length-1);
        }
        //  Restituisce il vettore al chiamante
        return anni;
    }
    //  Preleva le informazioni dal database riguardo una squadra selezionata attraverso il nome
    public Squadra getSquadra(String nomeSquadra) throws ClassNotFoundException, SQLException, 
            InstantiationException, IllegalAccessException, IOException, MieEccezioni{
        Squadra s;
        String query = "SELECT * FROM squadra WHERE nome='" + nomeSquadra + "'";    // Stringa dove definisco la query da inviare al database
        int j;      //  Variabile indice per il ciclo "while"
        try (Statement stmt_squadra = (Statement) this.db.createStatement();     // Statement per l'invio delle query per la richiesta della squadra
                ResultSet rs_squadra = stmt_squadra.executeQuery(query);    // Set dove vengono memorizzati i risultati della query per quanto riguarda la squadra
                Statement stmt_giocatore = (Statement) this.db.createStatement())
        {
            s = null;               //  Inizializza l'oggetto a "null", nel caso la query non restituisse nulla            
            Giocatore[] g;          //  Vettore di appoggio dove memorizzo i giocatori della squadra
            //  Se la query ha prodotto risultati...
            if(rs_squadra.next()){
                //  ...allroa definisce la nuova query per la richiesta al database dei giocatori della squadra
                query = "SELECT * FROM giocatore WHERE squadra='" + nomeSquadra + "'";
                try(ResultSet rs_giocatore = stmt_giocatore.executeQuery(query))          // Set dove vengono memorizzati i risultati della query per quanto riguarda i giocatori
                {
                    //  Crea il vettore dei 24 giocatori che andranno a comporre la squadra
                    g = new Giocatore[24];
                    //  Ciclo di estrazione dei giocatori della squadra
                    for(j=0; (rs_giocatore.next() && j<24); j++)
                        g[j] = new Giocatore(rs_giocatore.getString("squadra"), rs_giocatore.getString("nome"),
                                rs_giocatore.getString("cognome"), rs_giocatore.getString("ruolo"),
                                rs_giocatore.getInt("numero"), rs_giocatore.getInt("golFatti"),
                                rs_giocatore.getInt("presenze"), rs_giocatore.getInt("attacco"),
                                rs_giocatore.getInt("centrocampo"), rs_giocatore.getInt("difesa"),
                                rs_giocatore.getInt("portiere"), 0);
                    //  Se i giocatori siano in quantità corretta...
                    if(j == 24)
                        //  ...allora creo una nuova istanza della classe <Squadra> con le informazioni estratte
                        s = new Squadra(rs_squadra.getString("nome"), g, rs_squadra.getInt("formazione"));
                    else if(j<24)
                        //  ...altrimenti invia un eccezione al chiamante
                        throw new MieEccezioni("Non ci sono abbastanza giocatori per la squadra " + rs_squadra.getString("nome"));
                }
            }
            //  Altrimenti, se la query non ha prodotto alcun risultato...
            else
                //  ...lancia un'eccezione
                throw new MieEccezioni("La squadra " + nomeSquadra + " non è stata trovata");
        }
        return s;
    }    
    //  Metodo che preleva i dati delle squadre di un certo campionato di un certo anno
    public Squadra[] getSquadreAnno(int anno) throws ClassNotFoundException, SQLException, 
            InstantiationException, IllegalAccessException, IOException, MieEccezioni{        
        String query = "SELECT * FROM hadisputato WHERE hadisputato.anno=" + anno;      // Stringa dove definisco la query da inviare al database
        Squadra[] s;    //  Vettore dove vengono memorizzate le squadre estratte
        int i;          //  Variabile indice del ciclo
        try (Statement stmt_hadisputato = (Statement) this.db.createStatement();    // Statement per l'invio delle query
                ResultSet rs_hadisputato = stmt_hadisputato.executeQuery(query))    // Set dove vengono memorizzati i risultati della query per quanto riguarda la squadra
        {
            s = new Squadra[16];
            //  Ciclo lettura delle squadre presenti nel database
            for(i=0; (rs_hadisputato.next() && i<16); i++){
                s[i] = new Squadra(getSquadra(rs_hadisputato.getString("nomeSquadra")));
                s[i].setGolFatti(rs_hadisputato.getInt("golFatti"));
                s[i].setGolSubiti(rs_hadisputato.getInt("golSubiti"));
                s[i].setPosizione(new Posizione(rs_hadisputato.getString("posizione")));
            }
            //  Se sono state estratte meno di 16 squadre...
            if(i < 16){
                //  ...definisce il vettore a "null" perché l'estrazione è considerata non valida
                s = null;
                //  Lancia un'eccezione perché il numero di squadre è insufficiente
                throw new MieEccezioni("Non ci sono abbastanza squadre");
            }
        }
        return s;
    }    
    //  Metodo che preleva i dati delle squadre nel caso non siano stati disputati mai campionati
    public Squadra[] getSquadre() throws ClassNotFoundException, SQLException, 
            InstantiationException, IllegalAccessException, IOException, MieEccezioni{
        String query = "SELECT * FROM squadra"; //  Stringa dove definisco la query da inviare al database
        Squadra[] s;                            //  Vettore dove vengono memorizzate le squadre estratte dal database
        int i;                                  //  Variabile indice del ciclo
        try (Statement stmt_squadra = (Statement) this.db.createStatement();    // Statement per l'invio delle query
                ResultSet rs_squadra = stmt_squadra.executeQuery(query))        // Set dove vengono memorizzati i risultati della query per quanto riguarda la squadra
        {
            s = new Squadra[16];
            //  Ciclo lettura delle squadre presenti nel database
            for(i=0; (rs_squadra.next() && i<16); i++){
                s[i] = new Squadra(getSquadra(rs_squadra.getString("nome")));
            }
            //  Se sono state estratte meno di 16 squadre...
            if(i < 16){
                //  ...definisce il vettore a "null" perché l'estrazione è considerata non valida
                s = null;
                //  Lancia un'eccezione perché il numero di squadre è insufficiente
                throw new MieEccezioni("Non ci sono abbastanza squadre");
            }
        }
        return s;
    }
    // Restituisce la partita richiesta del campionato nell'anno selezionato
    public Partita getPartita(int anno, String nomePartita) throws ClassNotFoundException, SQLException, 
            InstantiationException, IllegalAccessException, IOException, MieEccezioni{        
        String query = "SELECT * FROM partite WHERE anno=" + anno + " AND nomePartita='" + nomePartita + "'";
        Partita p = null;
                
        try(Statement stmt = (Statement)this.db.createStatement();  //  Statement per l'invio della query
            ResultSet rs = stmt.executeQuery(query))                //  Invia la query per estrarre la partita richiesta dal database
        {
            //  Se la query sia andata a buon fine...
            if(rs.next())
                //  ...ed estrapola le informazioni richieste, creando un nuovo oggetto partita
                p = new Partita(getSquadra(rs.getString("squadraCasa")), getSquadra(rs.getString("squadraOspite")), 0, 0, new Posizione(nomePartita.split("_")[0]), Integer.decode(nomePartita.split("_")[1]), anno, rs.getInt("golCasa"), rs.getInt("golOspite"));                
        }
        return p;
    }    
    // Restituisce tutte le partite del campionato nell'anno selezionato
    public Partita[] getPartite(int anno) throws ClassNotFoundException, SQLException, 
            InstantiationException, IllegalAccessException, IOException, MieEccezioni{                        
        Partita[] p = new Partita[0];   //  Vettore dove si memorizzano le informazioni delle partite        
        Posizione tp = new Posizione(); //  Variabile indice del ciclo do-while
        int j = 0;                      //  Variabile indice del vettore
        int k;                          //  Variabile indice del ciclo while
        try(Statement stmt = (Statement)this.db.createStatement())
        {
            String query = "SELECT * FROM partite WHERE anno=" + anno;
            ResultSet rs = stmt.executeQuery(query);
            //  Controlla che siano state giocate partite in quell'anno        
            if(rs.next()){
                //  Ciclo di estrapolazione delle informazioni, un turno alla volta
                do{
                    //  Re-inizializza la variabile indice del ciclo while
                    k = 0;
                    //  Definisce e invia la query per la richiesta della partita
                    query = "SELECT * FROM partite WHERE anno=" + anno + " AND nomePartita='" + tp.getTurnoString() + "_" + k + "'";
                    rs = stmt.executeQuery(query);                                
                    while(rs.next()){
                        //  Memorizza la lunghezza "precedente" di p
                        j = p.length;
                        //  Allunga il vettore di un elemento
                        p = (Partita[])java.util.Arrays.copyOf(p, p.length+1);
                        //  Aggiunge il nuovo elemento al vettore
                        p[j] = getPartita(anno, rs.getString("nomePartita"));
                        //  Aumenta l'indice (di 2 perché le partite vengono nominate in base alla posizione della squadra
                        //      di casa--> Ottavi_0, Ottavi_2, Ottavi_4,..., Quarti_0, Quarti_2, ecc.
                        k += 2;
                        //  Definisce e invia la query per la richiesta della prossima partita
                        query = "SELECT * FROM partite WHERE anno=" + anno + " AND nomePartita='" + tp.getTurnoString() + "_" + k + "'";
                        rs = stmt.executeQuery(query);
                    }
                //  Passa al prossimo turno
                }while(tp.nextPos());
            }
        }
        //  Se sono state estrapolate delle partite...
        if(p.length > 0){            
            //  ...allora controlla che siano in numero giusto
            tp = new Posizione(p[j].getTipoPartita().split("_")[0]);    //  Memorizza l'ultimo turno giocato
            int n = 0;      //  Variabile dove si memorizzano il numero corretto di partite che devono essere state  giocate
            //  Ciclo di calcolo del numero di partite che devono esser state giocate
            for(int i=0; i<=tp.getTurnoInt(); i++)
                n += (int)Math.pow(2, 3-i);            
            //  Se la lunghezza del vettore è minore a n...
            if(p.length < n)
                //  ...lancia un'eccezione indicando che non ci sono abbastanza partite
                throw new MieEccezioni("Non ci sono abbastanza partite!");
            //  se, invece, il vettore è troppo lungo...
            else if(p.length > n)
                //  ...lancia un'eccezione indicando che ci sono troppe partite
                throw new MieEccezioni("Ci sono troppe partite!");
        }
        //  ...altrimenti, se l'anno considerato è diverso dall'anno del campionato in corso, lancia un'eccezione
        else if(anno != getCorrAnno())
            throw new MieEccezioni("Non ci sono partite!");                
        //  Restituisce, se tutto è stato eseguito correttamente, le informazioni richieste
        return p;
    }      
    //  Restituisce il turno a cui si è arrivati in un campionato di un certo anno
    public Posizione getTurno(int anno) throws ClassNotFoundException, SQLException, 
            InstantiationException, IllegalAccessException, IOException, MieEccezioni{        
        //  Variabile di appoggio per la memorizzazione del turno
        Posizione t = new Posizione();
        //  Definisce la query per estrarre le squadre del campionato di quell'anno (se presenti)
        String query = "SELECT * FROM hadisputato WHERE anno=" + anno;
        //  Creazione dello Statement ed esecuzione della query
        try(Statement stmt = (Statement)this.db.createStatement();   ResultSet rs = stmt.executeQuery(query)){                        
            //  Ciclo di lettura del database
            while(rs.next()){
                //  Se il turno è superiore a quello salvato sulla variabile di appoggio...
                if(new Posizione(rs.getString("Posizione")).getTurnoInt() > t.getTurnoInt())
                    //  ...aggiorna al nuovo turno la variabile di appoggio
                    t = new Posizione(rs.getString("Posizione"));
            }
        }
        //  Restituisce il turno del campionato
        return t;
    }
    
    //  Metodo che inserisce le informazioni della partita, passata come parametro, nel database
    public void putPartita(Partita p) throws ClassNotFoundException, SQLException, 
            InstantiationException, IllegalAccessException, IOException, MieEccezioni{
        //  Seleziona la partita nel database, se presente (una stessa partita non può essere giocata due volte)
        try (Statement stmt = (Statement) this.db.createStatement();                                    //  Seleziona la partita nel database, se presente (una stessa partita non può essere giocata due volte)
                ResultSet rs = stmt.executeQuery("SELECT * FROM partite WHERE anno=" + p.getAnno() +
                " AND nomePartita='" + p.getTipoPartita() + "'"))                                       //  Esegue la query
        {
            //  Se la partita non è presente nel database allora viene inserita
            if(rs.next() == false){
                String query = "INSERT INTO partite VALUES (" + p.getAnno() + ",'" + p.getTipoPartita() + "','" +
                        p.getSquadraCasa().getNome() + "','" + p.getSquadraOspite().getNome() + "'," +
                        p.getGolCasa() + "," + p.getGolOspite() + ")";
                stmt.executeUpdate(query);
            }
            //  altrimenti invia un'eccezione
            else
                throw new MieEccezioni("Partita già presente!");
        }
    }
    //  Inserisce le informazioni del campionato nel database
    public void putCampionato(int anno, Squadra[] squadre) throws ClassNotFoundException, SQLException, 
            InstantiationException, IllegalAccessException, IOException, MieEccezioni{
        putSquadre(squadre, anno);
        putAnno(anno);        
    }        
    // Inserisce l'anno del campionato nel database
    private void putAnno(int anno) throws ClassNotFoundException, SQLException, 
            InstantiationException, IllegalAccessException, IOException, MieEccezioni{
        String query = "SELECT * FROM storicoCampionato WHERE anno=" + anno;
        try (Statement stmt = (Statement) this.db.createStatement(); ResultSet rs = stmt.executeQuery(query)) //  ()Seleziona l'anno del campionato nel database, se presente
        {       //  Se il campionato richiesto non è stato già giocato...
                if(rs.next() == false){
                    //  Inserisce il nuovo anno di campionato nel database
                    query = "INSERT INTO storicocampionato VALUES (" + anno + ")";
                    stmt.executeUpdate(query);                   
                }
                else
                    throw new MieEccezioni("Impossibile salvare il campionato!\nCampionato già salvato!");
        }
    }
    //  Inserisce le squadre del campionato di un certo anno nel database
    private void putSquadre(Squadra[] squadre, int anno) throws ClassNotFoundException, SQLException, 
            InstantiationException, IllegalAccessException, IOException, MieEccezioni{
        String query = "SELECT * FROM storicoCampionato WHERE anno=" + anno;
        try (Statement stmt = (Statement) this.db.createStatement(); ResultSet rs = stmt.executeQuery(query)) //  Seleziona l'anno del campionato nel database, se presente
        {       //  Se il campionato richiesto non è stato già giocato...
                if(rs.next() == false){                    
                    //  ...Ciclo per l'inserimento delle squadre del campionato nel database
                    for (Squadra item : squadre) {
                        query = "INSERT INTO hadisputato VALUES (" + anno + ", '" + item.getNome() + "', " + 
                                    item.getGolFatti() + ", " + item.getGolSubiti() + ", '" + 
                                    item.getPosizione().getTurnoString() + "')";
                        stmt.executeUpdate(query);
                    }
                }
                else
                    throw new MieEccezioni("Impossibile salvare le squadre del campionato!\nCampionato già disputato!");
        }
    }
    //  Aggiorna i dati delle squadre
    public void updateSquadre(Squadra[] squadre, int anno) throws ClassNotFoundException, SQLException, 
            InstantiationException, IllegalAccessException, IOException, MieEccezioni{                        
        String query = "SELECT * FROM storicoCampionato WHERE anno=" + anno;
        ResultSet rs;
        try (Statement stmt = (Statement) this.db.createStatement()) //  Statement per l'invio delle query                 
        {
            //  Seleziona l'anno del campionato nel database, se presente
            rs = stmt.executeQuery(query);
            //  Se il campionato è presente...
            if(rs.next()){
                for (Squadra item : squadre){
                    query = "SELECT * FROM hadisputato WHERE nomeSquadra='" + item.getNome() + "' AND anno=" + anno;
                    rs = stmt.executeQuery(query);
                    //  Se la squadra è presente nel database allora...
                    if(rs.next())
                        //  ...aggiorna il record
                        query = "UPDATE hadisputato SET golFatti=" + item.getGolFatti() +
                                ", golSubiti=" + item.getGolSubiti() +            
                                ", posizione='" + item.getPosizione().getTurnoString() +
                                "' WHERE nomeSquadra='" + item.getNome() + "' AND anno=" + anno;
                    //  altrimenti...
                    else
                        //  ...lo inserisce
                        query = "INSERT INTO hadisputato (anno, nomeSquadra, golFatti, golSubiti, posizione) "
                                + " VALUES (" + anno + ", '" + item.getNome() + "', " + item.getGolFatti() + ", " + 
                                item.getGolSubiti() + ", '" + item.getPosizione().getTurnoString() + "')";
                    
                    stmt.executeUpdate(query);
                }
            }
            //  altrimenti, invia un'eccezione
            else
                throw new MieEccezioni("Impossibile aggiornare il campionato!\nCampionato non presente nel salvataggio!");
        }
    }
    
    //  Metodo per la chiusura "manuale" del database
    public void dbClose() throws ClassNotFoundException, SQLException, 
            InstantiationException, IllegalAccessException, IOException{
        this.db.close();
    }
    //  Metodo che viene chiamato alla "distruzione" dell'oggetto
    @Override
    protected void finalize() throws Throwable{
        try {
            this.db.close();
        } finally {
            super.finalize();
        }
    }
}