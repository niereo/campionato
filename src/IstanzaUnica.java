package progetto1.pkg0;

/*
*   Classe utilizzata per rendere unica l'istanza dell'applicazione
*/

import java.io.File;                                    //  Utilizzato per la creazione di un nuovo file
import java.io.RandomAccessFile;                        //  Utilizzato per stabilire una connessione al file
import java.nio.channels.OverlappingFileLockException;  //  Utilizzato per gestire l'eccezione del caso in cui il file sia già stato bloccato
import java.nio.channels.FileChannel;                   //  Utilizzato per istanziare un oggetto che definisca un canale di comunicazione con il file
import java.nio.channels.FileLock;                      //  Utilizzato per istanziare un oggeto che blocchi le risorse di un file

public class IstanzaUnica {
    private final String nomeIstanza;   //  Variabile dove viene memorizzato il nome dell'applicazione
    private File file;                  //  Variabile usata per la creazione del file
    private FileChannel channel;        //  Variabile usata per la creazione di un canale di comunicazione col file
    private FileLock lock;              //  Variabile usata per il blocco delle risorse del file
    
    public IstanzaUnica(String nomeIstanza){
        this.nomeIstanza = nomeIstanza;
    }
    //  Metodo che verifica se l'app è già stata attivata o meno
    public boolean isActivated(){
        boolean b = false;  //  Inizializza la variabile su "falso" (cioè non attiva)
        try {            
            //  Crea un file temporaneo su cui bloccare le risorse
            this.file = new File(System.getProperty("user.home"), this.nomeIstanza + ".jtmp");
            //  Crea un canale di sola lettura con il file
            this.channel = new RandomAccessFile(this.file, "rw").getChannel();
                       
            try {
                //  Tenta di bloccare il file
                this.lock = this.channel.tryLock();
            } catch(OverlappingFileLockException e) {
                //  Se fallisce allora il file è già bloccato
                //  Libera le risorse (in questo caso solo il canale)
                closeChannel();
                //  definisce la variabile su vero (l'app è già attiva)
                b = true;                
            }            
            
            if (this.lock == null) {
                //  Libera le risorse (in questo caso solo il canale)
                closeChannel();
                //  definisce la variabile su vero (l'app è già attiva)
                b = true;
            }                     
            
            //  Definisce il comportamento alla chiusura della JVM
            Runtime.getRuntime().addShutdownHook(new Thread() {
                    @Override
                    // Toglie il blocco e distrugge il file quando la JVM viene chiusa
                    public void run() {
                        releaseLock();
                        closeChannel();
                        deleteFile();
                    }
                });            
        }
        catch (Exception e) {
            //  Se c'è stato qualche problema, libera le risorse eventualmente occupate
            releaseLock();
            closeChannel();
            //  definisce la variabile su vero (l'app è già attiva)
            b = true;
        }
        //  Restituisce il risultato al chiamante
        return b;        
    }
    //  Rilascia la risorsa
    private void releaseLock(){
        try {             
            this.lock.release();  
        }catch (Exception e) {  }
    }
    //  Chiude il canale
    private void closeChannel(){
        try {            
            this.channel.close(); 
        }catch (Exception e) {  }
    }        
    //  Cancella il file usato per il blocco delle risorse
    private void deleteFile() {
        try { 
            this.file.delete(); 
        }catch (Exception e) {  }
    }    
}

