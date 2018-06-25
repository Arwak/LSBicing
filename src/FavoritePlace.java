

/**
 * Created by xavierromacastells on 4/1/17.
 */
public class FavoritePlace {

    private String nom;
    private String adreca;
    private String placeId;
    public FavoritePlace(){

    }

    public String getNom() {
        return nom;
    }

    public String getAdreca() {
        return adreca;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setAdreca(String adreca) {
        this.adreca = adreca;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nom);
        sb.append(" (");
        sb.append(adreca);
        sb.append(")");
        return sb.toString();
    }
}
