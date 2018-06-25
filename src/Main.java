
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Main {
    private static final String LINK_BICING = "http://wservice.viabicing.cat/v2/stations";
    public static void main(String[] args) throws Exception {
        Ranquing ranquing;
        Bicing bicing;
        Extraccio places;
        ProtocolRequest http;
        Gson gson = new Gson();
        http = new ProtocolRequest();
        JsonElement json = new JsonParser().parse(http.sendGet(LINK_BICING));

        if (http.isError()){
            System.exit(0);
        }
        places = GestioJson.llegeixJson();
        bicing = gson.fromJson(json, Bicing.class);
        ranquing = new Ranquing();
        gestioMenu(ranquing, bicing, places);

    }

    /**
     * Procediment que uneix la gestio del Menu
     * @param ranquing Parametre que guarda el ranquing
     * @param bicing Parametre que conté l'informació de les estacions de bicing
     * @param places Parametre que conte l'informacio extreta del fitxer favorite_places.json
     */
    private static void gestioMenu(Ranquing ranquing, Bicing bicing, Extraccio places){
        Menu m;
        do {
            m = new Menu();
            GestorLSBicing.gestionaOpcioAmbNumero(m.getOpcio(), ranquing, bicing, places);
        }while (m.getOpcio() != 9);
    }

}
