package site.team2dev.nemubengkel;

public class Bengkel {

    public String namaBengkel;
    public int rating;
    public String urlImage;

    public Bengkel(){

    }

//    public Bengkel(String namaBengkel, int rating, String urlImage){
//        this.namaBengkel=namaBengkel;
//        this.rating=rating;
//        this.urlImage=urlImage;
//    }

    public String getNamaBengkel(){
        return namaBengkel;
    }

    public void setNamaBengkel(String namaBengkel){
        this.namaBengkel=namaBengkel;
    }


    public int getRating(){
        return rating;
    }

    public void setRating(int rating){
        this.rating=rating;
    }

    public String gerUrlImage(){
        return urlImage;
    }

    public void setUrlImage(String urlImage){
        this.urlImage=urlImage;
    }




}
