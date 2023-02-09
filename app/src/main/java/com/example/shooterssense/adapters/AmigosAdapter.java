package com.example.shooterssense.adapters;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shooterssense.R;
import com.example.shooterssense.model.Usuario;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AmigosAdapter
        extends RecyclerView.Adapter<AmigosAdapter.ItemViewHolder>
        implements View.OnClickListener {
    //TERMINADO-REVISADO
    private ArrayList<Usuario> listaUsuarios;
    private FirebaseUser firebaseUser;
    private View.OnClickListener listener;

    public AmigosAdapter(ArrayList<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.amigos_item, parent, false);
        v.setOnClickListener(this);
        ItemViewHolder ivh = new ItemViewHolder(v);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bindItem(listaUsuarios.get(position));

        Usuario user = listaUsuarios.get(position);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        isFollowing(user.getId(), holder.btnFollow);

        if (user.getId().equals(firebaseUser.getUid())) {
            holder.btnFollow.setVisibility(View.GONE);
        }

        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.btnFollow.getText().toString().equals("seguir")) {
                    FirebaseDatabase.getInstance().getReference().child("Conexiones").child(firebaseUser.getUid())
                            .child("siguiendo").child(user.getId()).setValue(true);

                    FirebaseDatabase.getInstance().getReference().child("Conexiones").child(user.getId())
                            .child("seguidores").child(firebaseUser.getUid()).setValue(true);

                } else {
                    MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(v.getRootView().getContext());
                    alertDialog.setMessage("¿Seguro que deseas dejar de seguirlo?");
                    alertDialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase.getInstance().getReference().child("Conexiones").child(firebaseUser.getUid())
                                    .child("siguiendo").child(user.getId()).removeValue();

                            FirebaseDatabase.getInstance().getReference().child("Conexiones").child(user.getId())
                                    .child("seguidores").child(firebaseUser.getUid()).removeValue();
                        }
                    });
                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.create().show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePic;
        TextView username;
        TextView fullname;
        Button btnFollow;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profilePic);
            username = itemView.findViewById(R.id.username);
            fullname = itemView.findViewById(R.id.fullname);
            btnFollow = itemView.findViewById(R.id.btnFollow);
        }

        public void bindItem(Usuario user) {
            username.setText(user.getUsername());
            fullname.setText(user.getFullname());
            Glide.with(profilePic).load(user.getFotoPerfilUrl())
                    .into(profilePic);
        }
    }

    private void isFollowing(String userId, Button button) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Conexiones").child(firebaseUser.getUid()).child("siguiendo");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(userId).exists()) {
                    button.setText("siguiendo");
                } else {
                    button.setText("seguir");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}