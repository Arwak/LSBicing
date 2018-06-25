import io.restassured.path.json.JsonPath;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by xavierromacastells on 7/1/17.
 * Gestiona la comunicació i l'obertura de links url
 */
public class Comunicador {
    private static final String clau2 = "AIzaSyBl-lJtpNjtGrgLBlQMmMpFgW8K4pAmtQg";
    private static final String clauGeo = "AIzaSyC16tOQAOJfTw8XDpwqPXQ2N7DIw6Zm7fg";
    private static final String GOOGLE_PLACE = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";
    private static final String GOOGLE_MAP = "https://www.google.com/maps/place/";
    private static final String GOOGLE_MARK = "https://maps.googleapis.com/maps/api/staticmap?center=Barcelona&size=1920x1080&maptype=roadmap&markers=color:red";
    private static final String barraVertical = "%7C";
    private static final int MAX_ESTACIONS = 345;

    public static String getClau2() {
        return clau2;
    }
    public static String getClauGeo() {
        return clauGeo;
    }

    /**
     * Rebut el parametre ubicacio comprova que el contingut d'aquest parametre
     * estigui disponible a google maps i si es així obra l'enllaç amb l'ubiació
     * @param ubicacio reb l'ubicació sobre la qual executara la busqueda
     */
    public static void ubicam(String ubicacio) {
        String ubicacioOk, ubicacioLink;
        JsonPath jp;
        ProtocolRequest http = new ProtocolRequest();

        ubicacioOk = Adpatador.sumaBcn(ubicacio);
        ubicacioLink = StringUtils.stripAccents(Adpatador.adaptaGeo(ubicacioOk));
        jp =  new JsonPath(http.sendGet(ubicacioLink));

        if (jp.get("status").toString().equals("OK") && !http.isError()) {

            obraUrl(GOOGLE_MAP + ubicacioOk);

        } else {
            System.out.println("No s'han trobat resultats per aquesta ubicació");
        }

    }

    /**
     * Rebut el parametre placeId busca l'informació d'aquest placeId, comprova que
     * existeixi i si es així busca el url amb l'ubicació del lloc i l'obra
     * @param placeId el identificador del lloc segons google
     */
    public static void ubicamPlaceId(String placeId){
        ProtocolRequest http = new ProtocolRequest();
        JsonPath jp;
        StringBuilder sb = new StringBuilder();

        sb.append(GOOGLE_PLACE);
        sb.append(placeId);
        sb.append("&key=");
        sb.append(clau2);

        jp =  new JsonPath(http.sendGet(sb.toString()));
        if (jp.get("status").toString().equals("OK") && !http.isError()) {
            obraUrl(jp.get("result.url"));
        } else {
            System.out.println("No s'han trobat resultats");
        }

    }

    /**
     * Reb unes coordenades i obra un enllaç a google maps amb la busqueda
     * @param lat coordenada latitud
     * @param lon coordenada longitud
     */
    public static void ubicamPerCoord(float lat, float lon, String nom){
        StringBuilder sb = new StringBuilder();
        sb.append(GOOGLE_MAP);
        sb.append(Adpatador.adaptaString(nom));
        sb.append("+");
        sb.append(lat);
        sb.append("+");
        sb.append(lon);
        obraUrl(sb.toString());
    }

    /**
     * Obra un enllaç amb una imatge de l'ubicació de les estacions de bicing amb
     * el numero minim de bicicletes que ha rebut
     * @param bicing reb l'informació de les estacions de bicing
     * @param numMin enter interpretat com el numero minim de bicicletes a fer la
     *               busqueda
     */
    public static void minimBicis(Bicing bicing, int numMin) {
        Bicing est = new Bicing();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int i_ok = 0;
        int mida = bicing.size();
        while (i < mida && i < MAX_ESTACIONS) {
            if (bicing.infoEstacioAtIndex(i).getBikes() >= numMin) {
                est.add(i_ok, bicing.infoEstacioAtIndex(i));
                i_ok++;
            }
            i++;
        }
        sb.append(GOOGLE_MARK);
        i = 0;
        while(i < est.size()){
            sb.append(barraVertical);
            sb.append(est.infoEstacioAtIndex(i).getLatitude());
            sb.append(",");
            sb.append(est.infoEstacioAtIndex(i).getLongitude());
            sb.append(barraVertical);
            i++;
        }
        System.out.println("Carregant el mapa amb " + i_ok + " estacions...");
        obraUrl(sb.toString());


    }

    /**
     * Obra l'url al navegador
     * @param url string amb l'enllaç a obrir
     */
    public static void obraUrl(String url){
        Desktop desktop = java.awt.Desktop.getDesktop();
        URI oURL;
        try {
            oURL = new URI(url);
            desktop.browse(oURL);
        } catch (IOException e) {
            System.out.println("No puc obrir l'enllaç URL");
        } catch (URISyntaxException e) {
            System.out.println("No puc obrir l'enllaç URL");

        }
    }

}
