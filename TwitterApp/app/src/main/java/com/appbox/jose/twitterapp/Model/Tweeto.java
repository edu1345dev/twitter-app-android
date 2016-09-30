package com.appbox.jose.twitterapp.Model;

/**
 * Created by edu_1 on 28/09/2016.
 */
public class Tweeto {
    private String text;
    private String name;
    private String photoUrl;
    private long id;

    public Tweeto(String name, String text, String photoUrl, long id){
        this.name = name;
        this.text = text;
        this.photoUrl = photoUrl;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
