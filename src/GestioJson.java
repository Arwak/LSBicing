import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import io.restassured.path.json.JsonPath;

import java.io.*;

/**
 * Created by xavierromacastells on 7/1/17.
 */
public class GestioJson {
    private final static String PATH_A_JSON = "Fitxers/favorite_places.json";
    public static Extraccio llegeixJson(){
        JsonReader jsonr;
        Gson gson = new Gson();
        Extraccio places = new Extraccio();
        try {
            jsonr = new JsonReader(new FileReader(PATH_A_JSON));
            places = gson.fromJson(jsonr, Extraccio.class);
        } catch (FileNotFoundException e) {
            System.out.println("No trobo el fitxer favorite_places.json");
        } catch (Exception e) {
            System.out.println("No reconec el format del fitxer favorite_places.json");

        }
        if(places == null){
            places = new Extraccio();
        }
        return places;
    }
    public static void guardaJson(JsonPath jp, int index, Extraccio places){
        Gson gson;
        FavoritePlace fp = new FavoritePlace();

        try {
            fp.setNom(jp.get("results[" + index + "].name").toString());
            fp.setAdreca(jp.get("results[" + index + "].vicinity").toString());
            fp.setPlaceId(jp.get("results[" + index + "].place_id").toString());
            places.setPlace(fp);
        }catch(Exception e){
            System.out.println("No tinc suficientes dades per guardar-ho");
        }

        try (Writer writer = new FileWriter(PATH_A_JSON)) {
            gson = new GsonBuilder().create();
            gson.toJson(places, writer);
        } catch (IOException e) {
            System.out.println("Error guardant al fitxer favorite_places.json");
        }
    }
}
