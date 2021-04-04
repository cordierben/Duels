package fr.cordier.duels.Class;

public class Song {
    String title;
    String artist;
    int score;
    long id;
    String image;
    String preview;
    int rank;
    String posGrille;
    String posGrilleAct;

    public Song(Song track, String posGrille, String posGrilleAct) {
        this.title = track.getTitle();
        this.score = track.getScore();
        this.image = track.getImage();
        this.preview = track.getPreview();
        this.rank = track.getRank();
        this.posGrille=posGrille;
        this.posGrilleAct=posGrilleAct;
    }

    public Song(String title, String image, String preview,String posGrille,String posGrilleAct) {
        this.title = title;
        this.image = image;
        this.preview = preview;
        this.posGrille=posGrille;
        this.posGrilleAct=posGrilleAct;
    }

    public Song(String title, String image, String preview) {
        this.title = title;
        this.image = image;
        this.preview = preview;
    }

    public String getPosGrille() {
        return posGrille;
    }

    public void setPosGrille(String posGrille) {
        this.posGrille = posGrille;
    }

    public String getPosGrilleAct() {
        return posGrilleAct;
    }

    public void setPosGrilleAct(String posGrilleAct) {
        this.posGrilleAct = posGrilleAct;
    }

    public Song(String title, int score, String image, String preview, int rank) {
        this.title = title;
        this.score = score;
        this.image = image;
        this.preview = preview;
        this.rank = rank;
    }

    public Song(String title, String image, String preview, int rank) {
        this.title = title;
        this.image = image;
        this.preview = preview;
        this.rank = rank;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Song(String title, int score, String image) {
        this.title = title;
        this.score = score;
        this.image = image;
    }

    public Song(String title, int score) {
        this.title = title;
        this.score = score;
    }

    public Song(String title, String artist,String image,String preview,long id) {
        this.title = title;
        this.artist = artist;
        this.image=image;
        this.preview=preview;
        this.id=id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getAlbum() {
        return image;
    }

    public void setAlbum(String album) {
        this.image = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getId() {
        return id;
    }
}
