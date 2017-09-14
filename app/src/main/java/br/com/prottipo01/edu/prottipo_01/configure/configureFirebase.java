package br.com.prottipo01.edu.prottipo_01.configure;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by edu on 27/08/17.
 */

public class configureFirebase {

    private static DatabaseReference databaseReference;
    private static FirebaseAuth firebaseAuth;
    private static FirebaseStorage firebaseStorage;


    public static DatabaseReference getDatabaseReference(){

        if(databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference();


        }
        return databaseReference;
    }

    public static FirebaseAuth getFirebaseAuth(){

        if(firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
        }

        return firebaseAuth;
    }

    private static FirebaseStorage getFirebaseStorage(){

        if(firebaseStorage == null){
            firebaseStorage = FirebaseStorage.getInstance();
        }

        return firebaseStorage;

    }



}
