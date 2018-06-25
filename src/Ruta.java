/**
 * Created by xavierromacastells on 6/1/17.
 */
public class Ruta {
    private String sortida;
    private String arribada;
    private Estacio parada1;
    private Estacio parada2;

    private int  metres;
    public Ruta(){
        metres = -1;
    }
    public String getSortida() {
        return sortida;
    }



    public String getArribada() {
        return arribada;
    }

    public float getMetres() {
        return metres;
    }

    public void setSortida(String sortida) {
        this.sortida = sortida;
    }

    public Estacio getParada1() {
        return parada1;
    }

    public void setParada1(Estacio parada1) {
        this.parada1 = parada1;
    }

    public Estacio getParada2() {
        return parada2;
    }

    public void setParada2(Estacio parada2) {
        this.parada2 = parada2;
    }

    public void setArribada(String arribada) {
        this.arribada = arribada;
    }

    public void setMetres(int metres) {
        this.metres = metres;
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(System.getProperty("line.separator"));
        sb.append("Adreça de sortida:\t");
        sb.append(this.getSortida());
        sb.append(System.getProperty("line.separator"));
        sb.append("Primera estació bicing:\t");
        sb.append(this.getParada1().getStreetName());
        sb.append(", ");
        sb.append(this.getParada1().getStreetNumber());
        sb.append(System.getProperty("line.separator"));
        sb.append("Darrera estació bicing:\t");
        sb.append(this.getParada2().getStreetName());
        sb.append(", ");
        sb.append(this.getParada2().getStreetNumber());
        sb.append(System.getProperty("line.separator"));
        sb.append("Adreça d'arribada:\t");
        sb.append(this.getArribada());
        sb.append(System.getProperty("line.separator"));
        sb.append("Distancia: ");
        sb.append(metres/1000.0f);
        sb.append("km");
        return sb.toString();
    }

}
