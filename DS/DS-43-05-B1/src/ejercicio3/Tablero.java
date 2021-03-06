package ejercicio3;
 
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
 
public class Tablero {
     
    private Posicion posiciones_tab[][] = new Posicion[8][8];
    private Pieza piezas[][] = new Pieza[2][16];
             
    public Tablero () { 
        crearTablero();
        crearPiezas();
        piezasInicio();
    }
 
    public void iniciarPartida () { 
        eliminarPiezasTablero();
        piezasInicio();
    }
 
    @Override
    public String toString () { 
        StringBuilder Fern = new StringBuilder();
        for (int i=7; i>=0; i--){
            Fern.append(imprimirFila(i));
            if (!(i==0)) Fern.append ("/");
        }
        return Fern.toString();
    }
 
    public List buscaPiezas ( PiezasTipo pieza ) { 
        List<Posicion> Lista = new ArrayList<Posicion>();
        for (int i=1; i>=0;i--)
            for (int j=0;j<=15;j++)
                if(piezas[i][j].getTipo()==pieza) /*&& !(piezas[i][j].getPos()==null))*/
                    Lista.add(piezas[i][j].getPos());
        return Lista;
    }
 
    public void mover ( Posicion origen , Posicion destino ) {
        if (posicionErronea(origen,destino)) throw new IllegalArgumentException();
             
        int columna_origen,fila_origen;
        int columna_destino,fila_destino;
        Pieza temporal;
        columna_origen = origen.getColumna()-97;
        fila_origen = (int) origen.getFila()-49;
        columna_destino = destino.getColumna()-97;
        fila_destino = (int) destino.getFila()-49;
        if (posiciones_tab[columna_destino][fila_destino].getPieza()!=null){
            temporal = posiciones_tab[columna_destino][fila_destino].getPieza();
            temporal.setPos(null);
        }
        temporal = posiciones_tab[columna_origen][fila_origen].getPieza();
        posiciones_tab[columna_origen][fila_origen].setPieza(null);
        posiciones_tab[columna_destino][fila_destino].setPieza(temporal);
        temporal.setPos(destino);
    }
     
    private boolean posicionErronea(Posicion origen,Posicion destino){
        int columna,fila;
        fila = origen.getFila()-49;
        columna = (int) origen.getColumna()-97;
        if ((columna>7) || (columna<0))return true;
        if ((fila>7) || (fila<0))return true;
        if (posiciones_tab[columna][fila].getPieza()==null) return true;
        fila = destino.getFila()-49;
        columna = (int) destino.getColumna()-97;
        if ((columna>7) || (columna<0))return true;
        if ((fila>7) || (fila<0))return true;
        return false;
    }
     
    private void crearTablero (){
        int f = 0;
        int c = 0;
        for(char j='1'; j<='8';j++){
            for(char i='a'; i<='h';i++){
                posiciones_tab[c][f] = new Posicion (i,j);
                c++;
            }
            c = 0;
            f++;
        }    
    }
     
    private void crearPiezas(){
        int i = 0;
        int matriz_piezas = 0;//0 Rey, 1 Dama, 2-3 Alfil, 4-5 Caballo, 6-7 Torre, 8..15 Peon
        int matriz_color = 0; //0 Blanco, 1 Negro
        for (PiezasColor color : PiezasColor.values()){            
            for (PiezasTipo tipo : PiezasTipo.values()){
                if ((tipo==PiezasTipo.CABALLO) || (tipo == PiezasTipo.TORRE) || (PiezasTipo.ALFIL == tipo)){
                    for (i=0;i<=1; i++){
                        piezas[matriz_color][matriz_piezas] = new Pieza(tipo,color);
                        matriz_piezas++;
                    }                        
                }    
                else if (tipo==PiezasTipo.PEON){
                    for(i=0;i<=7;i++){
                        piezas[matriz_color][matriz_piezas] = new Pieza(tipo,color);
                        matriz_piezas++;
                    }
                }
                    else {
                        piezas[matriz_color][matriz_piezas] = new Pieza(tipo,color);
                        matriz_piezas++;
                    }            
            }
            matriz_color++;
            matriz_piezas = 0;
        }
    }
     
    private void piezasInicio(){
    //Piezas Blancas    
        int j1;
        for (int j = 0; j<=1;j++){
            j1 = j;
            j1 = j * 7;
        //Rey Blanco | Negro        
            posiciones_tab[4][j1].setPieza(piezas[j][0]);
            piezas[j][0].setPos(posiciones_tab[4][j1]);
        //Dama Blanca | Negra 
            posiciones_tab[3][j1].setPieza(piezas[j][1]);
            piezas[j][1].setPos(posiciones_tab[3][j1]);
        //Alfiles Blancos | Negros
            posiciones_tab[2][j1].setPieza(piezas[j][2]);
            piezas[j][2].setPos(posiciones_tab[2][j1]);
            posiciones_tab[5][j1].setPieza(piezas[j][3]);
            piezas[j][3].setPos(posiciones_tab[5][j1]);
        //Caballos Blancos | Negros
            posiciones_tab[1][j1].setPieza(piezas[j][4]);
            piezas[j][4].setPos(posiciones_tab[1][j1]);
            posiciones_tab[6][j1].setPieza(piezas[j][5]);
            piezas[j][5].setPos(posiciones_tab[6][j1]);
        //Torres Blancas | Negros
            posiciones_tab[0][j1].setPieza(piezas[j][6]);
            piezas[j][6].setPos(posiciones_tab[0][j1]);
            posiciones_tab[7][j1].setPieza(piezas[j][7]);
            piezas[j][7].setPos(posiciones_tab[7][j1]);
        //Peones Blancos | Negros
            if (j == 0) j1=1;
            else j1=6;
            for(int i = 0; i<=7; i++){
                posiciones_tab[i][j1].setPieza(piezas[j][i+8]);
                piezas[j][i+8].setPos(posiciones_tab[i][j1]);
            }
        }
    }    
     
    private StringBuilder imprimirFila(int Fila){
        StringBuilder Fern = new StringBuilder();
         
        int acum = 0;
        for (int i=0;i<=7;i++){
            if (posiciones_tab[i][Fila].getPieza()==null){
                acum++;
            }
            else{
                if (acum>0){
                    Fern.append((char) ('0' + acum));
                    acum = 0;
                }
                Pieza temporal = posiciones_tab[i][Fila].getPieza();
                Fern.append(temporal.letraPieza());
            }
        }
        if (acum>0){
            Fern.append((char) ('0' + acum));
            acum = 0;
        }
        return Fern;
    }
    
     
    private void eliminarPiezasTablero(){
        for (int i = 0; i<=7;i++){
            for (int j=0; j<=7; j++){
                posiciones_tab[i][j].setPieza(null);
            }
        }
    }
     
}
