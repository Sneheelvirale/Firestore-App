package com.example.firestoreapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private Button saveBtn;
    private Button updateBtn;
    private Button deleteBtn;
    private Button readBtn;
    private EditText nameET;
    private EditText emailET;
    private TextView textView;

    //Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference friendsRef = db.collection("Users").document("zvRtewREFWLmCkpDtx45");
    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        textView = findViewById(R.id.text);
        saveBtn = findViewById(R.id.saveBtn);
        updateBtn = findViewById(R.id.updateBtn);
        deleteBtn = findViewById(R.id.delteBtn);
        readBtn = findViewById(R.id.readBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveDatatoNewDocument();
            }
        });

        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetAllDocumentInCollection();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateSpecificDocument();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteAll();
            }
        });

    }
    private void SaveDatatoNewDocument(){
        String name = nameET.getText().toString();
        String email = emailET.getText().toString();

        Friend friend = new Friend(name,email);
        collectionReference.add(friend).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String docId = documentReference.getId();
            }
        });
    }

    private void GetAllDocumentInCollection(){
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String data = "";
                //the code is executed when data retrieval is successful
                //the queryDocumentSnapshot contain the documents in the collection
                for (QueryDocumentSnapshot snapshot: queryDocumentSnapshots){

                    //Transforming  snapshots into objects
                    Friend friend = snapshot.toObject(Friend.class);

                    data += "Name: "+friend.getName()+"Email: "+friend.getEmail()+"\n";
                }
                textView.setText(data);
            }
        });
    }

    private void UpdateSpecificDocument(){
        String name = nameET.getText().toString();
        String email = emailET.getText().toString();

        friendsRef.update("name",name);
        friendsRef.update("email",email);

    }

    private void DeleteAll(){
        friendsRef.delete();
    }
}