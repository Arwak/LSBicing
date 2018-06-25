import io.restassured.path.json.JsonPath;
import org.apache.commons.lang3.StringUtils;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by xavierromacastells on 7/1/17.
 * Classe que gestiona la crida a les funcions pertinents per realitzar cada opció
 */
public class GestorLSBicing {

    /**
     * Funcio que entrada una opció fa la crida a la funcio que la gestiona
     * @param opcio Parametre que indica l'opció que ha de ser executada
     * @param ranquing Parametre que guarda el ranquing
     * @param bicing Parametre que conté l'informació de les estacions de bicing
     * @param places Parametre que conte l'informacio extreta del fitxer favorite_places.json
     */
    public static void gestionaOpcioAmbNumero(int opcio, Ranquing ranquing, Bicing bicing, Extraccio places){
        switch (opcio) {
            case 1:
                gestioPrimera();
                break;
            case 2:
                gestioSegona(bicing);
                break;
            case 3:
                gestioTercera(bicing, ranquing);
                break;
            case 4:
                gestioQuarta(bicing);
                break;
            case 5:
                gestioCinquena(ranquing, places);
                break;
            case 6:
                gestioSisena(places);
                break;
            case 7:
                gestioSetena(bicing);
                break;
            case 8:
                gestioVuitena(ranquing);
                break;
            case 9:
                break;
            default:
                System.out.println("Error, opció no disponible");
                break;
        }
    }

    /**
     * Gestiona la primera opció
     * Demana una ubicació a l'usuari i l'obre al Google Maps
     *
     */
    private static void gestioPrimera(){
        Scanner     sc = new Scanner(System.in);    // Variable scanner
        String ubicacio;
        System.out.print("Introdueix una ubicació: ");
        ubicacio = sc.nextLine();
        System.out.println("Carregant Google Maps... ");
        Comunicador.ubicam(ubicacio);

    }

    /**
     * Gestiona la segona opció
     * Demana una ubicació a l'usuari i busca les estacions de bicing més properes
     * @param bicing rep l'informació de les estacions bicing
     */
    private static void gestioSegona(Bicing bicing){
        Scanner     sc = new Scanner(System.in);    // Variable scanner
        String ubicacio;
        System.out.print("Introdueix una ubicació: ");
        ubicacio = sc.nextLine();
        Buscar.buscaCoordenades(ubicacio, bicing);

    }

    /**
     * Gestiona la tercera opció
     * Demana dues ubicacions a l'usuari he elabora una ruta entre elles
     * buscant les estacions més properes de bicing de les dues ubicacions
     * @param bicing rep l'informació de les estacions bicing
     * @param ranquing rep el ranquing i l'actualitza si es degut
     */
    private static void gestioTercera(Bicing bicing, Ranquing ranquing) {
        Ruta ruta = new Ruta();
        StringBuilder sb = new StringBuilder();
        Scanner sc = new Scanner(System.in);    // Variable scanner
        String ubicacio;
        String ubicacio1;
        JsonPath jp;
        ProtocolRequest http = new ProtocolRequest();

        System.out.println("Introdueix una primera ubicació: ");
        ubicacio = sc.nextLine();
        System.out.println("Introdueix una segona ubicació: ");
        ubicacio1 = sc.nextLine();
        ubicacio = Adpatador.sumaBcn(ubicacio);
        ubicacio1 = Adpatador.sumaBcn(ubicacio1);
        jp = new JsonPath(http.sendGet(Adpatador.generaRuta(ubicacio, ubicacio1)));

        if (!http.isError() && jp.get("status").toString().equals("OK") && !jp.get("rows[0].elements[0].status").equals("NOT_FOUND")) {
            sb.append("--------- Generant ruta ---------");
            sb.append(System.getProperty("line.separator"));
            sb.append("-- Origen: ");

            ruta.setSortida(Adpatador.treuClaudator(jp.get("origin_addresses").toString()));

            sb.append(ruta.getSortida());
            sb.append(System.getProperty("line.separator"));
            sb.append("-- Destí: ");
            ruta.setArribada(Adpatador.treuClaudator(jp.get("destination_addresses").toString()));
            sb.append(ruta.getArribada());
            sb.append(System.getProperty("line.separator"));
            sb.append("-- Distància: ");
            sb.append(jp.get("rows[0].elements[0].distance.value").toString());
            ruta.setMetres(jp.get("rows[0].elements[0].distance.value"));
            sb.append(" metres");
            sb.append(System.getProperty("line.separator"));
            sb.append("-- Duració: ");
            sb.append(jp.get("rows[0].elements[0].duration.text").toString());
            System.out.println(sb.toString());
            ubicacio = Bicing.imprimeixEstacio(ubicacio, bicing, true, ruta, ranquing);
            ubicacio1 = Bicing.imprimeixEstacio(ubicacio1, bicing, false, ruta, ranquing);
            sb.setLength(0);
            sb.append("https://www.google.es/maps/dir/");
            sb.append(StringUtils.stripAccents(Adpatador.adaptaString(ruta.getSortida())));
            sb.append("/");
            sb.append(StringUtils.stripAccents(Adpatador.adaptaString(ubicacio)));
            sb.append("/");
            sb.append(StringUtils.stripAccents(Adpatador.adaptaString(ubicacio1)));
            sb.append("/");
            sb.append(StringUtils.stripAccents(Adpatador.adaptaString(ruta.getArribada())));
            sb.append("/data=!4m2!4m1!3e1");

            if (ranquing.esMaxima(ruta)) {
                ranquing.setRuta(ruta);
            }
            Comunicador.obraUrl(sb.toString());
        } else {
            System.out.println("Hi ha hagut un problema, comprova les dades si us plau");
        }

    }

    /**
     * Gestiona la quarta opció
     * Demana un minim de bicicletes disponibles i mostra totes aquelles estacions
     * que contenen més o igual bicicletes
     * @param bicing rep l'informació de les estacions bicing
     */
    private static void gestioQuarta(Bicing bicing){
        Scanner     sc = new Scanner(System.in);    // Variable scanner
        int enter;
        System.out.print("Mínim de bicis disponibles?: ");
        try {
            enter = sc.nextInt();
            Comunicador.minimBicis(bicing, enter);
        }catch(InputMismatchException e){
            System.out.println("Error, el caràcter introduit no és numèric");
        }

    }

    /**
     * Gestiona la cinquena opció
     * Demana un lloc i mostra els resultats trobats(màxim 5) i demana a l'usuari
     * si el vol desar al fitxer favorite_places
     * @param ranquing rep el ranquing i l'actualitza si es degut
     * @param places rep els llocs guardats a favorite_places.json i n'afegeix
     *               si es degut
     */
    private static void gestioCinquena(Ranquing ranquing, Extraccio places){
        Scanner     sc = new Scanner(System.in);    // Variable scanner
        String ubicacio;
        System.out.println("Introdueix un lloc a cercar: ");
        ubicacio = sc.nextLine();
        Buscar.buscarLloc(ubicacio, ranquing, places);

    }

    /**
     * Gestiona la sisena opció
     * Mostra els llocs guardats per l'usuari i ofereix la possibilitat de obrir-lo
     * a Google Maps
     * @param places rep els llocs guardats a favorite_places.json
     */
    private static void gestioSisena(Extraccio places){
        int i = 0;
        int mida;
        int opcio;
        mida = places.getPlaces().size();
        Scanner sc = new Scanner(System.in);
        if(mida > 0) {
            while (i < mida) {
                i++;
                System.out.print(i);
                System.out.print(". ");
                System.out.println(places.getPlace(i - 1).toString());
            }
            System.out.println();

            System.out.print("Selecciona un dels possibles " + mida + " llocs per visualitzar: ");
            try {
                opcio = sc.nextInt();
                System.out.println("Obrint mapa de " + places.getPlace(opcio - 1).getNom().toString() + "...");
                System.out.println(places.getPlace(opcio - 1).getAdreca());
                System.out.println(places.getPlace(opcio - 1).getNom());
                Comunicador.ubicamPlaceId(places.getPlace(opcio - 1).getPlaceId());
            } catch (InputMismatchException e) {
                System.out.println("Error, esperava un enter");
            }

        }else{
            System.out.println("Res a mostrar");
        }
    }

    /**
     * Gestiona la setena opició
     * Busca les dues estacions amb distància màxima entre elles
     * @param bicing rep l'informació de les estacions bicing
     */
    private static void gestioSetena(Bicing bicing){
        Buscar.buscarDistanciaMaxima(bicing);

    }

    /**
     * Gestiona la vuitena opció
     * Imprimeix el ranquing rebut així com la ruta més llarga cercada fins al moment
     * @param ranquing rep el ranquing
     */
    private static void gestioVuitena(Ranquing ranquing){
        System.out.println(ranquing.toString());

    }






}
