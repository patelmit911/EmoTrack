package edu.asu.mcgroup27.emotrack.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

public class FirebaseDBHelper {
    public static void getUserIDRef(String email, final UserDBRefListener listener) {
        DatabaseReference userList = FirebaseDB.getInstance().getReference("userlist");
        userList.orderByValue().equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseReference ref = FirebaseDB.getInstance().getReference("users")
                        .child(dataSnapshot.getKey());
                listener.onObtained(ref);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static DatabaseReference getUserFriendReqs() {
        return FirebaseDB.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("requests");
    }


    public static void getUserFriendReqs(String email, final UserDBRefListener listener) {
        getUserIDRef(email, new UserDBRefListener() {
            @Override
            public void onObtained(DatabaseReference databaseReference) {
                listener.onObtained(databaseReference.child("requests"));
            }
        });
    }

    public static DatabaseReference getUserFriends() {
        return FirebaseDB.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("friends");

    }

    public static DatabaseReference getUser() {
        return FirebaseDB.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public static DatabaseReference getUserEmergencyContact() {
        return FirebaseDB.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("contacts");
    }

    public static void removeItem(final DatabaseReference ref, String item) {
        ref.orderByValue().equalTo(item).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ref.child(dataSnapshot.getKey()).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void insertItem(DatabaseReference ref, String item) {
        ref.push().setValue(item);
    }
}
