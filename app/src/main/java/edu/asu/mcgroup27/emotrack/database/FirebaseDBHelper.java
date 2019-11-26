package edu.asu.mcgroup27.emotrack.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class FirebaseDBHelper {
    public static DatabaseReference getUserIDRef(String email) {
        DatabaseReference userList = FirebaseDB.getInstance().getReference("userlist");
        DatabaseReference userRef = userList.orderByValue().equalTo(email).getRef();
        return userRef;
    }

    public static DatabaseReference getUserFriendReqs() {
        return FirebaseDB.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("requests");
    }

    public static DatabaseReference getUserFriends() {
        return FirebaseDB.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("friends");
    }

    public static DatabaseReference getUser() {
        return FirebaseDB.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("requests");
    }

    public static void removeItem(DatabaseReference ref, String item) {
        ref.orderByValue().equalTo(item).getRef().setValue(null);
    }

    public static void insertItem(DatabaseReference ref, String item) {
        ref.push().setValue(item);
    }
}
