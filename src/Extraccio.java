import java.util.ArrayList;

/**
 * Created by xavierromacastells on 4/1/17.
 */
public class Extraccio {
    private ArrayList<FavoritePlace> places;

    public Extraccio(){
        places = new ArrayList<>(0);
    }

    public ArrayList<FavoritePlace> getPlaces() {
        return places;
    }

    public FavoritePlace getPlace(int index) throws IndexOutOfBoundsException {
        return places.get(index);
    }

    public void setPlaces(ArrayList<FavoritePlace> places) {
        this.places = places;
    }
    public void setPlace(FavoritePlace fp) {

        this.places.add(fp);
    }
    public boolean isEmpty() {
        return places.isEmpty();
    }


}
