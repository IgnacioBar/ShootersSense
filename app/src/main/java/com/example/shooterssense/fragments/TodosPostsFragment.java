package com.example.shooterssense.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shooterssense.PostDetalleActivity;
import com.example.shooterssense.R;
import com.example.shooterssense.adapters.PostAdapter;
import com.example.shooterssense.model.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TodosPostsFragment extends Fragment {
    //TERMINADO-REVISADO
    public static final String CLAVE_POST = "POST";

    RecyclerView rvLoop;
    PostAdapter postAdapter;
    List<Post> postList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todos_posts, container, false);

        rvLoop = view.findViewById(R.id.rvTodosPosts);
        rvLoop.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvLoop.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);
        rvLoop.setAdapter(postAdapter);

        readPosts();

        return view;
    }

    private void readPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Post post = snap.getValue(Post.class);
                    postList.add(post);
                }

                postAdapter.setListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = rvLoop.getChildAdapterPosition(v);
                        Post post = postList.get(i);

                        Intent intentall = new Intent(getContext(), PostDetalleActivity.class); //Crear Detalle post
                        intentall.putExtra(CLAVE_POST, post);
                        intentall.putExtra("USER", post.getPublisher());
                        startActivity(intentall);
                    }
                });
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}