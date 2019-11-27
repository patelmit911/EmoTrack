package edu.asu.mcgroup27.emotrack.database;

import com.google.firebase.database.DatabaseReference;

interface UserDBRefListener {
    public void onObtained(DatabaseReference databaseReference);
}
