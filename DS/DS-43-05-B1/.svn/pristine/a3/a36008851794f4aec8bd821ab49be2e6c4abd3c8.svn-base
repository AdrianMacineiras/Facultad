package ejercicio4;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
 
public class MesaBillar {
    ArrayList<BolasBillar> cajetin = new ArrayList<>();
    ArrayList<BolasBillar> mesa = new ArrayList<>();
    private EstadoPartida estado;
     
    public MesaBillar(){
        this.estado = EstadoPartida.SIN_EMPEZAR;
        cajetin.addAll(Arrays.asList(BolasBillar.values()));
    }
        
    public void iniciaPartida(){
        cajetin.clear();
        mesa.addAll(Arrays.asList(BolasBillar.values()));
        this.estado = EstadoPartida.EN_JUEGO;
    }
    
    public ArrayList bolasMesa(){
        return mesa;
    }
    
    public ArrayList bolasCajetin(){
        return cajetin;
    }
    
    public boolean esPartidaIniciada(){
        if(this.estado == EstadoPartida.EN_JUEGO){
            return true;
        } else {
            return false;
        }
    }
    
    public boolean meterBola (BolasBillar b ) { 
        if(b == BolasBillar.BLANCA && estado == EstadoPartida.EN_JUEGO){
            return true;
        } else{
            if(b == BolasBillar.BLANCA && estado == EstadoPartida.FINALIZADA){
                    cajetin.add(b);
                    mesa.remove(b);
                    return false;
            } else {
                if(b == BolasBillar.BLANCA && estado == EstadoPartida.SIN_EMPEZAR){
                    return false;
                }else{
                    if(b.getColorBola() == BolasBillar.ColorBola.NEGRO){
                        cajetin.add(b);
                        mesa.remove(b);
                        this.estado = EstadoPartida.FINALIZADA;
                        return false;
                    } else {
                        cajetin.add(b);
                        mesa.remove(b);
                        return true;
                    }
                }
            }
        }
    }
    
    public BolasBillar.TipoBolas obtenerGanador () {
        int i, countL = 0, countR = 0;
        
        for(i = 0; i < cajetin.size(); i++){
            if(cajetin.get(i).getColorBola() == BolasBillar.ColorBola.BLANCO || cajetin.get(i).getColorBola() == BolasBillar.ColorBola.NEGRO){
                continue;
            }else{
                if(cajetin.get(i).getTipoBolas() == BolasBillar.TipoBolas.RAYADA){
                    countR++;
                } else {
                    countL++;
                }
            }
        }
        if(countR > countL){
            return BolasBillar.TipoBolas.RAYADA;
        }else{
            if(countR < countL){
                return BolasBillar.TipoBolas.LISA;
            }else{
                return null;
            }
        }
    }  
}
