package edu.asu.mcgroup27.emotrack.ui;

import android.graphics.Bitmap;

public class DisplayContent {

    private String name;
    private String thmb;
    private String id;
    private String mediaUri;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThmb() {
        return thmb;
    }

    public void setThmb(String thmb) {
        this.thmb = thmb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
