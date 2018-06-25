
import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by xavierromacastells on 6/1/17.
 */
public class Ranquing {
    private LinkedList<XTop> fifo;
    private Ruta ruta;

    public Ranquing(){
        fifo = new LinkedList<>();
        ruta = new Ruta();
    }

    /**
     * Comprova si la ruta pasada per parametre te major distancia a la ruta
     * de l'objecte
     * @param ruta ruta
     * @return true: és més gran, false: és més petita o igual
     */
    public boolean esMaxima(Ruta ruta){
        if(ruta.getMetres() > this.ruta.getMetres()){
            return true;
        }
        return false;
    }

    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }

    /**
     * Afegeix un nou element al ranquing o si ja hi es el reordena i elimina el
     * ultim en cas d'haver-n'hi més de 10
     * @param nou node  a afegir
     */
    public void add(XTop nou){
        int index = jaHiEs(nou);
        if(index == -1){
            fifo.add(nou);
        }else{
            fifo.get(index).incrementaCop();
        }
        Collections.sort(fifo);
        if(fifo.size() > 10) {
            fifo.removeFirst();
        }

    }

    /**
     * Busca si el pendent ja esta al ranquing
     * @param pendent node a comprovar
     * @return index amb la posicio que ocupa al ranquing o -1 si no esta al ranquing
     */
    private int jaHiEs(XTop pendent){
        int i = 0;
        while (i < fifo.size()) {
            if(pendent.equals(fifo.get(i))){
                return i;
            }
            i++;
        }
        return -1;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int size;
        if (fifo.isEmpty()){
            sb.append("No diposes de suficient informacio per executar aquesta opcio");

        }else {
            size = fifo.size();
            for (int i = 0; i < size; i++) {
                sb.append(fifo.get(i).toString(size - i));
                sb.append(System.getProperty("line.separator"));
            }
            if(ruta.getMetres() > 0){
                sb.append("Ruta amb major recorregut: ");
                sb.append(ruta.toString());
            }
        }

        return sb.toString();
    }

}
