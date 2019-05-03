package site.team2dev.nemubengkel;

public class Bengkel {

    public String namaBengkel;
    public int rating;

    public Bengkel(){

    }

    public Bengkel(String namaBengkel, int rating){
        this.namaBengkel=namaBengkel;
        this.rating=rating;
    }

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




}
