
/**
 * Created by xavierromacastells on 6/1/17.
 */
public class XTop implements Comparable<XTop> {
    private String adress;
    private float latitud;
    private float longitud;
    private boolean esEstacio;
    private boolean esElectrica;
    private int repeticions;

    public XTop(){

    }
    public XTop(String adress, float latitud, float longitud, boolean esEstacio, boolean esElectrica){
        this.adress = adress;
        this.latitud = latitud;
        this.longitud = longitud;
        this.esEstacio = esEstacio;
        this.esElectrica = esElectrica;
        this.repeticions = 1;
    }
    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public void setEsEstacio(boolean esEstacio) {
        this.esEstacio = esEstacio;
    }

    public void setEsElectrica(boolean esElectrica) {
        this.esElectrica = esElectrica;
    }

    public int getRepeticions() {
        return repeticions;
    }

    public String getAdress() {
        return adress;
    }

    public float getLatitud() {
        return latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public boolean isEsEstacio() {
        return esEstacio;
    }

    public boolean isEsElectrica() {
        return esElectrica;
    }

    /**
     * incrementa en 1 la variable repeticions
     */
    public void incrementaCop(){
        this.repeticions++;
    }

    /**
     * Retorna l'informacio de l'objecte amb el # i al davant
     * @param i numero a posar al davant
     * @return informacio
     */
    public String toString(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append(")");
        sb.append(System.getProperty("line.separator"));
        sb.append("\tAdreça: ");
        sb.append(adress);
        sb.append(System.getProperty("line.separator"));
        sb.append("\tLatitud: ");
        sb.append(latitud);
        sb.append(System.getProperty("line.separator"));
        sb.append("\tLongitud: ");
        sb.append(longitud);
        sb.append(System.getProperty("line.separator"));
        if(esEstacio){
            sb.append("\tÉs estació de bicing, tipus ");
            if(esElectrica){
                sb.append("electrica");
            }else{
                sb.append("no electrica");
            }
        }
        return sb.toString();

    }

    @Override
    public boolean equals(Object obj) {
        XTop o = (XTop) obj;
        return adress.equals(o.getAdress()) && latitud == o.getLatitud() &&
                longitud == o.getLongitud() && esEstacio == esEstacio &&
                esElectrica == esElectrica;
    }

    @Override
    public int compareTo(XTop o) {
        return this.getRepeticions() - o.getRepeticions();

    }
}
