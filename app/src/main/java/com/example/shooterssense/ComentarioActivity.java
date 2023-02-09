package com.example.shooterssense;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.shooterssense.adapters.ComentarioAdapter;
import com.example.shooterssense.model.Comentario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ComentarioActivity extends AppCompatActivity {
    //TERMINADO-REVISADO
    RecyclerView rvComentarios;
    EditText etComentario;

    String postId;
    String publisherId;

    FirebaseUser firebaseUser;
    ComentarioAdapter commentAdapter;
    List<Comentario> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentario);

        rvComentarios = findViewById(R.id.rvComentarios);
        rvComentarios.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComentarios.setLayoutManager(linearLayoutManager);
        commentList = new ArrayList<>();
        commentAdapter = new ComentarioAdapter(this, commentList);
        rvComentarios.setAdapter(commentAdapter);

        etComentario = findViewById(R.id.etComentario);
        postId = getIntent().getStringExtra("POST_ID");
        publisherId = getIntent().getStringExtra("PUBLISHER_ID");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        readComments();
    }

    public void enviarComentario(View view) {
        String comentario = etComentario.getText().toString().trim();
        if (comentario.isEmpty()) {
            Toast.makeText(this, "No puedes enviar un comentario vacío", Toast.LENGTH_SHORT).show();
        } else {
            addComentario(comentario);
        }

    }

    private void addComentario(String comentario) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comentarios").child(postId);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("comment", comentario);
        hashMap.put("publisher", firebaseUser.getUid());

        reference.push().setValue(hashMap);
        etComentario.setText("");
    }

    private void readComments() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comentarios").child(postId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Comentario comment = snap.getValue(Comentario.class);
                    commentList.add(comment);
                }

                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}