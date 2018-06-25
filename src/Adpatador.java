import org.apache.commons.lang3.StringUtils;

/**
 * Created by xavierromacastells on 7/1/17.
 */
public class Adpatador {

    private final static String GOOGLE_GEO = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    private final static String GOOGLE_DIST = "https://maps.googleapis.com/maps/api/distancematrix/json?";
    /**
     * Substitueix els ' ' del String brut per '+'
     * @param brut string a transformar
     * @return string transformat
     */
    public static String adaptaString(String brut){
        int i = 0;
        StringBuilder sb = new StringBuilder();
        int max = brut.length();
        while (i < max) {
            if(brut.charAt(i) == ' '){
                sb.append("+");
            }else{
                sb.append(brut.charAt(i));
            }

            i++;
        }
        return sb.toString();

    }

    /**
     * Reb una ubicacio i retorna una url per a realitzar una busqueda de geolocalitzacio
     * @param ubi ubicacio
     * @return url preparada per fer una peticio GET
     */
    public static String adaptaGeo(String ubi){
        StringBuilder sb = new StringBuilder();
        sb.append(GOOGLE_GEO);
        sb.append(ubi);
        sb.append("&key=");
        sb.append(Comunicador.getClauGeo());
        return sb.toString();
    }

    /**
     * Reb una string, en treu els caracters especials com (ñ, ç, à...) o passa a(n, c, a)
     * substitueix els ' ' per '+' i suma "Barcelona" a l'ubicacio
     * @param ubicacio ubiacio
     * @return string transformat
     */
    public static String sumaBcn(String ubicacio){
        StringBuilder sb = new StringBuilder();
        sb.append(adaptaString(StringUtils.stripAccents(ubicacio)));
        sb.append("+Barcelona");
        return sb.toString();

    }

    /**
     * Reb una ubiacio d'origen i destí i genera un enllaç url preparat per fer una
     * peticio GET a l'api distanceMatrix de Google
     * @param origen ubicacio inici
     * @param desti ubicacio fi
     * @return url preparat
     */
    public static String generaRuta(String origen, String desti){
        String origin = adaptaString(origen);
        String destination = adaptaString(desti);
        StringBuilder sb = new StringBuilder();
        sb.append(GOOGLE_DIST);
        sb.append("origins=");
        sb.append(origin);
        sb.append("&destinations=");
        sb.append(destination);
        sb.append("&mode=bicycling");
        sb.append("&key=");
        sb.append(Comunicador.getClauGeo());
        return sb.toString();
    }

    /**
     * rebut un string "[foo[] foo foo]" la passa a "foo[] foo foo"
     * elimina el primer i el ultim caracter del String
     * @param cadena per treure[ ]
     * @return retorna el String cadena sense el primer i ultim [
     */
    public static String treuClaudator(String cadena){
        StringBuilder sb = new StringBuilder();
        sb.append(cadena);
        sb.deleteCharAt(0);
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
