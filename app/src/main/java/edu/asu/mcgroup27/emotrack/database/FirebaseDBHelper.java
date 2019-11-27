package edu.asu.mcgroup27.emotrack.database;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

public class FirebaseDBHelper {

    private static void getUserIDRef(String email, final UserDBRefListener listener) {
        DatabaseReference userList = FirebaseDB.getInstance().getReference("userlist");
        userList.orderByValue().equalTo(email).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    DatabaseReference ref = FirebaseDB.getInstance().getReference("users")
                            .child(snapshot.getKey());
                    listener.onObtained(ref);
                }

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

    public static void getUserFriends(String email, final UserDBRefListener listener) {
        getUserIDRef(email, new UserDBRefListener() {
            @Override
            public void onObtained(DatabaseReference databaseReference) {
                listener.onObtained(databaseReference.child("friends"));
            }
        });
    }

    public static DatabaseReference getUser() {
        return FirebaseDB.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public static DatabaseReference getUserMetaDataRef() {
        return FirebaseDB.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("data");
    }

    public static void getUserMetaDataRef(String email, final UserDBRefListener listener) {
        getUserIDRef(email, new UserDBRefListener() {
            @Override
            public void onObtained(DatabaseReference databaseReference) {
                listener.onObtained(databaseReference.child("data"));
            }
        });
    }

    public static void getUserMetaData(final UserMetaDataListener listener) {
        getUserMetaDataRef(FirebaseAuth.getInstance().getCurrentUser().getEmail(), new UserDBRefListener() {
            @Override
            public void onObtained(DatabaseReference databaseReference) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserMetaData userMetaData = dataSnapshot.getValue(UserMetaData.class);
                        listener.onObtained(userMetaData);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public static void getUserMetaData(String email, final UserMetaDataListener listener) {
        getUserMetaDataRef(email, new UserDBRefListener() {
            @Override
            public void onObtained(DatabaseReference databaseReference) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserMetaData userMetaData = dataSnapshot.getValue(UserMetaData.class);
                        listener.onObtained(userMetaData);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public static void getUser(String email, final UserDBRefListener listener) {
        getUserIDRef(email, new UserDBRefListener() {
            @Override
            public void onObtained(DatabaseReference databaseReference) {
                listener.onObtained(databaseReference);
            }
        });
    }

    public static DatabaseReference getUserEmergencyContact() {
        return FirebaseDB.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("contacts");
    }

    public static void getUserEmergencyContact(String email, final UserDBRefListener listener) {
        getUserIDRef(email, new UserDBRefListener() {
            @Override
            public void onObtained(DatabaseReference databaseReference) {
                listener.onObtained(databaseReference.child("contacts"));
            }
        });
    }

    public static void removeItem(final DatabaseReference ref, String item) {
        ref.orderByValue().equalTo(item).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ref.child(snapshot.getKey()).removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void insertItem(final DatabaseReference ref, final String item) {
        ref.orderByValue().equalTo(item).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    ref.push().setValue(item);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
