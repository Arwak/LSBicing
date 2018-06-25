import io.restassured.path.json.JsonPath;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by xavierromacastells on 7/1/17.
 * Classe que gestiona tot
 */
public class Buscar {

    private final static String GOOGLE_LOCATION = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
    /**
     * Funcio que busca la distancia màxima entre totes les estacions que ha rebut
     * @param bicing parametre que conte l'informació de les estacions de bicing
     *               sobre les quals ha de buscar les dues amb distancia maxima
     */
    public static void buscarDistanciaMaxima (Bicing bicing){
        float lat1;
        float lng1;
        float lat;
        float lng;
        int idI;
        int idJ;
        double dist;
        double dist_max;
        Haversine haversine = new Haversine();
        DecimalFormat format = new DecimalFormat("0.00");
        int mida = bicing.size();

        lat1 = (bicing.infoEstacioAtIndex(0).getLatitude());
        lng1 = (bicing.infoEstacioAtIndex(0).getLongitude());

        lat = (bicing.infoEstacioAtIndex(1).getLatitude());
        lng = (bicing.infoEstacioAtIndex(1).getLongitude());

        dist_max = haversine.distance(lat1, lng1, lat, lng);

        idI = 0;
        idJ = 1;
        for (int i = 0; i < mida; i++){
            lat1 = (bicing.infoEstacioAtIndex(i).getLatitude());
            lng1 = (bicing.infoEstacioAtIndex(i).getLongitude());
            for(int j = i + 1; j < mida; j++) {
                lat = (bicing.infoEstacioAtIndex(j).getLatitude());
                lng = (bicing.infoEstacioAtIndex(j).getLongitude());

                dist = haversine.distance(lat1, lng1, lat, lng);

                if (dist > dist_max) {
                    dist_max = dist;
                    idI = i;
                    idJ = j;
                }
            }
        }
        System.out.println("Les estacions més llunyanes entre elles sòn: ");
        bicing.imprimeixEstacio(idI, bicing);
        bicing.imprimeixEstacio(idJ, bicing);
        System.out.print("Amb una distància de: ");
        System.out.print(format.format(dist_max));
        System.out.print("km");
        System.out.println();




    }

    /**
     * Funcio que busca la distancia minima entre una estació i les coordenades
     * rebudes
     * @param bicing paramentre que conte l'informació de les estacions de bicing
     * @param lat coordenada latitud
     * @param lng coordenada longitud
     * @return l'index on es troba l'estació més propera al parametre rebut bicing
     */
    public static int buscarDistanciaMinima (Bicing bicing, float lat, float lng){
        float lat1_min;
        float lng1_min;
        float calcul;
        double dist_min;
        float lat1;
        float lng1;
        int id = 0;
        double dist;

        lat1_min = (bicing.infoEstacioAtIndex(0).getLatitude());
        lng1_min = (bicing.infoEstacioAtIndex(0).getLongitude());

        calcul = (lat1_min-lat)*(lat1_min-lat)+(lng1_min-lng)*(lng1_min-lng);
        dist_min = Math.sqrt(calcul);


        for (int i = 0; i < bicing.size(); i++) {
            lat1 = (bicing.infoEstacioAtIndex(i).getLatitude());
            lng1 = (bicing.infoEstacioAtIndex(i).getLongitude());
            calcul = (lat1-lat)*(lat1-lat)+(lng1-lng)*(lng1-lng);
            dist = Math.sqrt(calcul);

            if (dist < dist_min && bicing.infoEstacioAtIndex(i).getBikes() > 0){
                dist_min = dist;
                id = i;
            }

        }



        return id;
    }

    /**
     * Funcio que reb una ubicació i busca la coordenada lattitud
     * @param ubicacio parametre que conte l'ubicació sobre la qual volem trobar
     *                 la latitud
     * @return la latitud de l'ubicació
     * @throws NullPointerException en cas que l'ubicacio no pugui ser trobada
     */
    private static float buscarLattitude(String ubicacio) throws NullPointerException {
        float lat;
        JsonPath jp = ProtocolRequest.requestJsonPath(ubicacio);

        lat = (jp).get("results[0].geometry.location.lat");

        return lat;

    }

    /**
     * Funcio que reb una ubicació i busca la coordenada longitud
     * @param ubicacio parametre que conte l'ubicació sobre la qual volem trobar
     *                 la longitud
     * @return la longitud de l'ubicació
     * @throws NullPointerException en cas que l'ubicacio no pugui ser trobada
     */
    private static float buscarLongitude(String ubicacio) throws NullPointerException {
        float lng;
        JsonPath jp = ProtocolRequest.requestJsonPath(ubicacio);

        lng = (jp).get("results[0].geometry.location.lng");

        return lng;

    }

    /**
     * Funció que busca el id rebut en bicing
     * @param id el id sobre el qual es vol la posicio en el parametre bicing
     * @param bicing paramentre que conte l'informació de les estacions de bicing
     * @return l'index amb la posició on es troba l'estacio amb l'id rebut
     */
    public static int buscaId(int id, Bicing bicing){
        int i;
        int mida = bicing.size();
        boolean stop = true;
        for(i = 0; i < mida && stop; i++){
            if(bicing.infoEstacioAtIndex(i).getId() == id) {
                stop = false;
            }
        }

        return i - 1;
    }

    /**
     * Rebut un place el busca a Barcelona amb un radi de 10km del centre i el
     * guarda si l'usuari ho desitja al fitxer favorite_places.json i n'actualitza
     * el ranquing si es aixi
     * @param place String que conte el nom sobre el qual es vol buscar llocs
     *              relacionats
     * @param ranquing Conte el ranquing i l'actualitza si es degut
     * @param places Conte l'informació del fitxer favorite_places.json i afegeix
     *              informació si es degut
     */
    public static void buscarLloc(String place, Ranquing ranquing, Extraccio places){
        StringBuilder sb = new StringBuilder();
        JsonPath jp;
        float aux;
        jp = ProtocolRequest.requestJsonPath(Adpatador.adaptaGeo("Barcelona"));
        sb.append(GOOGLE_LOCATION);
        aux = jp.get("results[0].geometry.location.lat");
        sb.append(aux);
        sb.append(",");
        aux = (jp).get("results[0].geometry.location.lng");
        sb.append(aux);
        sb.append("&radius=10000");
        sb.append("&name=");
        sb.append(Adpatador.adaptaString(place));
        sb.append("&key=");
        sb.append(Comunicador.getClau2());
        jp = ProtocolRequest.requestJsonPath(sb.toString());
        boolean guardat = false;
        for (int i = 0; i < 5 && !guardat; i++) {
            guardat = gestionaResultat(jp, i, ranquing, places);

        }
    }

    /**
     * Donat un numberArray = "5, 8, 359, 419", separa els numeros per les comes
     * i els introdueix a un array d'enters que retorna
     * @param numberArray reb un seguit de numeros separats per comes
     * @return el numberArray en format int[]
     */
    public static int[] buscaNumeros (String numberArray) {
        ArrayList<Integer> numeros = new ArrayList<>();
        String[] items = numberArray.replaceAll(" ", "").split(",");
        for(int i = 0; i < items.length; i++) {
            numeros.add(Integer.parseInt(items[i]));
        }
        return Arrays.stream(numeros.toArray(new Integer[numeros.size()])).mapToInt(Integer::intValue).toArray();
    }

    /**
     * Donada una ubicació busca les coordenades i l'estació de bicing més propera
     * @param ubicacio ubicació en que es vol les coordenades
     * @param bicing informacio de les estacions de bicing
     */
    public static void buscaCoordenades(String ubicacio, Bicing bicing) {
        String aux;
        float lat = 0;
        float lng = 0;
        aux = Adpatador.sumaBcn(ubicacio);
        aux = Adpatador.adaptaGeo(aux);
        int i;
        try {
            lat = buscarLattitude(aux);
            lng = buscarLongitude(aux);

            i = buscarDistanciaMinima(bicing, lat, lng);
            Bicing.printarParada(bicing, i);
        }catch (NullPointerException e){
            System.out.println("Error, ubicació no trobada");
        }
    }

    /**
     * Funcio que gestiona el resultat del jp introduit a l' index introduit i ho
     * guarda si es necessari a favorite_places.json i actualitza el ranquing
     * @param jp JsonPath que conte l'informació dels llocs
     * @param index index que indica el resultat del JsonPath sobre el qual es vol
     *              gestionar el resultat
     * @param ranquing Conte el ranquing i l'actualitza si es degut
     * @param places Conte l'informació del fitxer favorite_places.json i afegeix
     *              informació si es degut
     * @return true: ha estat desat al fitxer/no 'shan trobat resultats
     *         false: no ha estat desat al fitxer
     */
    private static boolean gestionaResultat(JsonPath jp, int index, Ranquing ranquing, Extraccio places){
        XTop nou;
        boolean entrada = true;
        StringBuilder sb = new StringBuilder();
        String resposta;
        Scanner sc = new Scanner(System.in);
        try {
            sb.append("Lloc: ");
            sb.append(jp.get("results[" + index + "].name").toString());
            sb.append(System.getProperty("line.separator"));
            sb.append("---------------------------------------");
            sb.append(System.getProperty("line.separator"));
            sb.append("Adreça: ");
            sb.append(jp.get("results[" + index + "].vicinity").toString());
            sb.append(System.getProperty("line.separator"));
        }catch (NullPointerException e){
            System.out.println("Cap resultat disponible");
            return true;
        }

        try {
            if (jp.get("results[" + index + "].opening_hours.open_now")) {
                sb.append("Actualment obert");
            } else {
                sb.append("Actualment tancat");
            }
        }catch(NullPointerException e){
            sb.append("No tenim dades de l'horari");
        }
        sb.append(System.getProperty("line.separator"));
        sb.append("Rating del lloc: ");
        try{
            sb.append(jp.get("results["+ index +"].rating").toString());
        }catch(NullPointerException e){
            sb.append("Sense dades");
        }
        do {
            sb.append(System.getProperty("line.separator"));
            sb.append("Vols guardar-lo en el fitxer favorite_places.json? (Si/No)");
            System.out.println(sb.toString());
            resposta = sc.nextLine();

            if (resposta.equals("NO") || resposta.equals("nO") || resposta.equals("No") || resposta.equals("no")) {
                System.out.println("Cap resultat guardat");
                entrada = false;
            } else if (resposta.equals("SI") || resposta.equals("sI") || resposta.equals("Si") || resposta.equals("si")) {
                nou = new XTop(jp.get("results[" + index + "].vicinity").toString(), jp.get("results[" + index + "].geometry.location.lat"), jp.get("results[" + index + "].geometry.location.lng"), false, false);
                ranquing.add(nou);
                GestioJson.guardaJson(jp, index, places);
                System.out.println("Lloc desat en el fitxer");
                return true;
            } else {
                System.out.println("Error, introdueixi Si o No");
            }
            sb.setLength(0);
        }while(entrada);
        return false;
    }

}
