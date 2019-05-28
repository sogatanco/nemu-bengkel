package site.team2dev.nemubengkel;

public class Ulasan {
    public String user;
    public String rating;
    public String ulasan;
    public String date;

    public Ulasan(){

    }

    public String getUser(){
        return user;
    }
    public void setUser(String user){
        this.user=user;
    }

    public String getRating(){
        return rating;
    }
    public void setRating(String rating){
        this.rating=rating;
    }

    public String getUlasan(){
        return ulasan;
    }
    public void setUlasan(String ulasan){
        this.ulasan=ulasan;
    }

    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date=date;
    }

}
