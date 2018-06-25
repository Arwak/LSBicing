import io.restassured.path.json.JsonPath;

import java.util.ArrayList;

/**
 * Created by xavierromacastells on 18/12/16.
 */
public class Bicing {
    private ArrayList<Estacio> stations = new ArrayList<>();
    private int updateTime;

    public Bicing(){

    }

    /**
     * Afegeix estacio a l'objecte bicing a la posicio index
     * @param index posicio on es vol afegir l'estacio
     * @param estacio estacio a afegir
     */
    public void add(int index, Estacio estacio){
        stations.add(index, estacio);
    }

    /**
     *
     * @param index sobre el qual es vol l'estacio
     * @return estacio en la posicio index
     */
    public Estacio infoEstacioAtIndex(int index){
        return stations.get(index);
    }

    /**
     *
     * @return # d'estacions
     */
    public int size(){
        return stations.size();
    }

    /**
     * Rebuda una ubicacio busca l'estacio més propera de bicing, configura ruta
     * en funcio de esOrigen, actualitza el ranquing i imprimeix:
     *   -----------------------------------
     *  | Estació origen més propera: ...   |
     *  | Estació destí més propera: ...    |
     *   -----------------------------------
     * @param ubicacio ubicacio
     * @param bicing rep informacio de les estacions de bicing
     * @param esOrigen es tracta d'una ubicacio: true: d'origen, false: destí
     * @param ruta  ruta a configurar
     * @param ranquing ranquing a actualitzar
     * @return un string amb el següent format:
     * "latitud lat, longitud lng"
     * on lat i lng son la latitud i la longitud de l'estacio més propera a l'ubicacio
     */
    public static String imprimeixEstacio(String ubicacio, Bicing bicing,
                                          boolean esOrigen, Ruta ruta, Ranquing ranquing){
        StringBuilder sb = new StringBuilder();
        XTop nou;
        int id = -1;
        int i = 0;
        JsonPath jp = ProtocolRequest.requestJsonPath(Adpatador.adaptaGeo(ubicacio));
        try {
            id = Buscar.buscarDistanciaMinima(bicing, (jp).get("results[0].geometry.location.lat"),
                        (jp).get("results[0].geometry.location.lng"));


        }catch(NullPointerException e){
            sb.setLength(0);
            sb.append(ubicacio);
            if(esOrigen) {
                System.out.println("Error en la primera ubicació");
            }else{
                System.out.println("Error en la segona ubicació");
            }
            return sb.toString();
        }
        sb.append(bicing.infoEstacioAtIndex(id).getStreetName());
        sb.append(", ");
        sb.append(bicing.infoEstacioAtIndex(id).getStreetNumber());
        if(bicing.infoEstacioAtIndex(id).getType().equals("BIKE")) {
            nou = new XTop(sb.toString(), bicing.infoEstacioAtIndex(id).getLatitude(),
                    bicing.infoEstacioAtIndex(id).getLongitude(), true, false);
        }else{
            nou = new XTop(sb.toString(), bicing.infoEstacioAtIndex(id).getLatitude(),
                    bicing.infoEstacioAtIndex(id).getLongitude(), true, true);
        }
        ranquing.add(nou);
        sb.setLength(0);
        if(esOrigen) {
            ruta.setParada1(bicing.infoEstacioAtIndex(id));
            sb.append("Estació origen més porpera: ");
        }else{
            ruta.setParada2(bicing.infoEstacioAtIndex(id));
            sb.append("Estació destí més porpera: ");
        }
        sb.append(bicing.infoEstacioAtIndex(id).getStreetName());
        sb.append(",");
        sb.append(bicing.infoEstacioAtIndex(id).getStreetNumber());
        System.out.println(sb.toString());
        sb.setLength(0);
        sb.append(bicing.infoEstacioAtIndex(id).getLatitude());
        sb.append(",");
        sb.append(bicing.infoEstacioAtIndex(id).getLongitude());


        return sb.toString();
    }

    /**
     * Imprimeix segons els parametres rebuts:
     *   --------------------------------------------------------------------------------------
     *  | L'estació de bicing més propera es troba a: #nom del carrer#, nº #num del carrer#    |
     *  | amb un total de #num de bicis# de #capacitat maxima de bicis# bicicletes disponibles |
     *  | Les estacions més properes son #num estacions properes#:                             |
     *  | -----------------------------------------------                                      |
     *  | #. #nom del carrer#, nº #num del carrer#                                             |
     *  | Amb un total de #capacitat maxima de bicis# disponibles                              |
     *  | -----------------------------------------------                                      |
     *  |                   ... x # Estacions properes                                         |
     *  --------------------------------------------------------------------------------------
     *
     * @param bicing parametre sobre el qual s'extreu informaxcio
     * @param index index de l'estacio de bicing d'on s'ha d'extreure l'informacio
     */
    public static void printarParada(Bicing bicing, int index) {
        StringBuilder sb = new StringBuilder();
        int [] numbers;

        sb.append("L'estació de bicing més propera es troba a: ");
        sb.append(bicing.infoEstacioAtIndex(index).getStreetName());
        sb.append(", nº");
        sb.append( bicing.infoEstacioAtIndex(index).getStreetNumber());
        sb.append(System.lineSeparator());
        sb.append("amb un total de ");
        sb.append(bicing.infoEstacioAtIndex(index).getBikes());
        sb.append(" de ");
        sb.append(Integer.parseInt(bicing.infoEstacioAtIndex(index).getSlots())
                + bicing.infoEstacioAtIndex(index).getBikes());
        sb.append(" bicicletes disponibles.");
        sb.append(System.lineSeparator());
        sb.append("Les estacions més properes són ");

        Comunicador.ubicamPerCoord(bicing.infoEstacioAtIndex(index).getLatitude(),
                bicing.infoEstacioAtIndex(index).getLongitude(), bicing.infoEstacioAtIndex(index).getStreetName());

        String s = bicing.infoEstacioAtIndex(index).getNearbyStations();
        numbers = Buscar.buscaNumeros(s);

        sb.append(numbers.length);
        sb.append(":");
        sb.append(System.lineSeparator());
        sb.append("-----------------------------------------------");


        for(int j = 0; j < numbers.length; j++) {
            int a = Buscar.buscaId(numbers[j], bicing);
            sb.append(System.lineSeparator());
            sb.append(j+1);
            sb.append(". ");
            sb.append(bicing.infoEstacioAtIndex(a).getStreetName());
            sb.append(", nº");
            sb.append(bicing.infoEstacioAtIndex(a).getStreetNumber());
            sb.append(System.lineSeparator());
            sb.append("Amb un total de ");
            sb.append(bicing.infoEstacioAtIndex(a).getBikes());
            sb.append(" de ");
            sb.append(Integer.parseInt(bicing.infoEstacioAtIndex(a).getSlots())
                    + bicing.infoEstacioAtIndex(a).getBikes());
            sb.append(" bicicletes disponibles.");


        }


        System.out.println(sb);



    }

    /**
     * Imprimeix l'informacio de l'estacio
     * @param index index de l'estacio a imprimir
     * @param bicing parametre d'on s'extreuran les estacions
     */
    public static void imprimeixEstacio(int index, Bicing bicing){
        StringBuilder sb = new StringBuilder();
        sb.append(System.getProperty("line.separator"));
        sb.append("*");
        sb.append(System.getProperty("line.separator"));
        sb.append("\tIdentificador:\t");
        sb.append(bicing.infoEstacioAtIndex(index).getId());
        sb.append(System.getProperty("line.separator"));
        sb.append("\tAdreça:\t");
        sb.append(bicing.infoEstacioAtIndex(index).getStreetName());
        sb.append(", ");
        sb.append(bicing.infoEstacioAtIndex(index).getStreetNumber());
        sb.append(System.getProperty("line.separator"));
        sb.append("\tTipus:\t");
        sb.append(bicing.infoEstacioAtIndex(index).getType());
        sb.append(System.getProperty("line.separator"));
        sb.append("\tCapacitat màxima de bicicletes:\t");
        sb.append(bicing.infoEstacioAtIndex(index).getBikes() +
                Integer.parseInt(bicing.infoEstacioAtIndex(index).getSlots()));
        System.out.println(sb.toString());
    }
}
