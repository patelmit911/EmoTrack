package edu.asu.mcgroup27.emotrack.ui;

import android.graphics.Bitmap;

public class DisplayContent {

    private String name;
    private Bitmap thmb;
    private String id;
    private String mediaUri;

    public String getMediaUri() {
        return mediaUri;
    }

    public void setMediaUri(String mediaUri) {
        this.mediaUri = mediaUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
}
