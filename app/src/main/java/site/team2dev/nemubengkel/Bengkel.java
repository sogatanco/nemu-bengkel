package site.team2dev.nemubengkel;

public class Bengkel {

    public String namaBengkel;
    public String rating;
    public String urlImage;
    public int approved;
    public int kategori;
    public int idbengkel;
    public int ulasan;
    public String jarak;

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


    public String getRating(){
        return rating;
    }

    public void setRating(String rating){
        this.rating=rating;
    }


    public String gerUrlImage(){
        return urlImage;
    }

    public void setUrlImage(String urlImage){
        this.urlImage=urlImage;
    }


    public int getApproved(){
        return approved;
    }

    public void setApproved(int approved){
        this.approved=approved;
    }


    public int getKategori(){
        return kategori;
    }

    public void setKategori(int kategori){
        this.kategori=kategori;
    }


    public int getIdbengkel(){
        return idbengkel;
    }

    public void setIdbengkel(int idbengkel){
        this.idbengkel=idbengkel;
    }


    public int getUlasan(){
        return ulasan;
    }

    public void setUlasan(int ulasan){
        this.ulasan=ulasan;
    }


    public String getJarak(){
        return jarak;
    }
    public void setJarak(String jarak){
        this.jarak=jarak;
    }

}
