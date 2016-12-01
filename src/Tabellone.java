package progetto1.pkg0;

import javax.swing.JPanel;  //  Usato per creare il pannello (la classe verrà ereditata per poter essere estesa e disegnare il tabellone)
import java.awt.Graphics;   //  Usato per rendere disponibili i metodi per il disegno (nel metodo "Disegno")
import java.awt.Dimension;  //  Usato per rendere disponibile il tipo dato Dimension (nel costruttore)

/*
*   Classe che disegna il tabellone sulla finestra
*/

public class Tabellone extends JPanel{
    public Tabellone(){
        this.setSize(1200, 500);
    }
    
    public Tabellone(Dimension d){
        this.setSize(d);
    }
    //  Metodo che permette di disegnare sul pannello
    @Override
    public void paintComponent(Graphics g){        
        int w = 100, h = 35;    //  Dimensioni di una etichetta
        int dist = 50;          //  Distanza tra due etichette agli ottavi di finale
        super.paintComponent(g);
        // OTTAVI
        Disegno(16, g, (40 + w), 40 + h/2, dist);
        // QUARTI
        Disegno(8, g, 2*(40 + w), 40 + h/2 + dist/2, dist);
        // SEMIFINALE
        Disegno(4, g, 3*(40 + w), 40 + h/2 + 3*dist/2, dist);
        // FINALE
        g.drawLine(4*(40 + w), 40 + h/2 + 7*dist/2, 4*(40 + w)+80, 40 + h/2 + 7*dist/2);
        // VINCITORE
        g.drawLine(4*(40 + w)+40, 40 + h/2 + 7*dist/2, 4*(40 + w)+40, 40 + 7*dist/2 + 80);
    }
    //  Metodo che disegna le linee sul tabellone
    //  Parametri:
    //       int n:=    Numero di linee da stampare
    //  Graphics g:=    Oggetto che permette di disegnare sul pannello
    //       int X:=    Coordinata iniziale dell'asse delle ascisse
    //       int Y:=    Coordinata iniziale dell'asse delle ordinate
    //    int dist:=    Valore che indica la distanza tra le etichette
    public void Disegno(int n, Graphics g, int X, int Y, int dist){
        int x, y;       //  Coordinate della linea
        int delta = 20; //  Incremento standard delle coordinate
        //  Inizializza il valore delle coordinate al valore passato come parametro
        x = X;
        y = Y;
        
        for(int i=0; i<n; i++){
            //  Se si arriva alla metà si stampa dalla parte opposta del tabellone
            if(i==n/2){
                x = this.getWidth() + 5 - X;
                y = Y;
                delta *= -1;    //  Il disegno non viene più fatto da destra a sinistra, ma da sinistra a destra
            }
            //  Disegna una linea "uscente" dall'etichetta
            g.drawLine(x, y, x+delta, y);
            //  Se si è sulla seconda etichetta (poiché le partite vanno a due squadre ogni volta)...
            if((i%2)==0){
                //  ...disegna la linea verticale che congiunge le due linee uscenti dalle etichette
                g.drawLine(x+delta, y, x+delta, y+dist*(16/n)); 
                //  e disegna un'altra linea che si attacca all'etichetta del turno successivo
                g.drawLine(x+delta, y+dist*(16/n)/2, x+2*delta, y+dist*(16/n)/2); 
            }
            //  Passa alla prossima etichetta
            y += dist*(16/n);                        
        }
    }
}
