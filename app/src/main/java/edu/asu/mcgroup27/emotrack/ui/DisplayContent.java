package edu.asu.mcgroup27.emotrack.ui;

import android.graphics.Bitmap;

public class DisplayContent {

    private String name;
    private String duration;
    private Bitmap thmb;
    private int id;
    private String mediaUri;
    private long album_id;


    public long getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(long album_id) {
        this.album_id = album_id;
    }

    public String getMediaUri() {
        return mediaUri;
    }

    public void setMediaUri(String mediaUri) {
        this.mediaUri = mediaUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getThmb() {
        return thmb;
    }

    public void setThmb(Bitmap thmb) {
        this.thmb = thmb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

}
