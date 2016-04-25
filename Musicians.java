package com.saveliy.third;


public class Musicians {

    private String name;

    private String genre;
    private String smallImage;
    private String bigInage;
    private String description;
    private String website;
    private int songs;
    private int albums;

    public Musicians(){

    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        if(this.genre == null){
            this.genre = genre;
        } else this.genre +=  ", " + genre;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public String getBigInage() {
        return bigInage;
    }

    public void setBigInage(String bigInage) {
        this.bigInage = bigInage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        char[] arr = description.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        this.description = new String(arr);
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getSongs() {
        return songs;
    }

    public void setSongs(int songs) {
        this.songs = songs;
    }

    public int getAlbums() {
        return albums;
    }

    public void setAlbums(int albums) {
        this.albums = albums;
    }






}
