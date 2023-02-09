package com.example.shooterssense.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shooterssense.PostDetalleActivity;
import com.example.shooterssense.R;
import com.example.shooterssense.adapters.PostAdapter;
import com.example.shooterssense.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    //TERMINADO-REVISADO
    public static final String CLAVE_POST = "POST";

    RecyclerView rvLoop;
    PostAdapter postAdapter;
    List<Post> postList;
    LinearLayout alertaHome;
    private List<String> followingList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        alertaHome = view.findViewById(R.id.alertaHome);
        rvLoop = view.findViewById(R.id.rvLoop);
        rvLoop.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvLoop.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);
        rvLoop.setAdapter(postAdapter);

        checkFollowing();
        return view;
    }

    private void checkFollowing() {
        followingList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Conexiones")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("siguiendo");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followingList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    followingList.add(snap.getKey());
                }

                readPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Post post = snap.getValue(Post.class);
                    for (String id : followingList) {
                        if (post.getPublisher().equals(id)) {
                            postList.add(post);
                        }
                    }
                }

                if (!postList.isEmpty()) {
                    alertaHome.setVisibility(View.INVISIBLE);
                }

                postAdapter.setListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = rvLoop.getChildAdapterPosition(v);
                        Post post = postList.get(i);

                        Intent intentDetalle = new Intent(getContext(), PostDetalleActivity.class);
                        intentDetalle.putExtra(CLAVE_POST, post);
                        intentDetalle.putExtra("USER", post.getPublisher());
                        startActivity(intentDetalle);

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