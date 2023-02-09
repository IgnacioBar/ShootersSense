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

import com.example.shooterssense.MiPerfilActivity;
import com.example.shooterssense.R;
import com.example.shooterssense.adapters.AmigosAdapter;
import com.example.shooterssense.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AmigosFragment extends Fragment {
    //TERMINADO-REVISADO
    public static final String CLAVE_USUARIO = "USUARIO";

    RecyclerView rvAmigos;
    DatabaseReference reference;
    AmigosAdapter adapter;
    ArrayList<Usuario> listaUsuarios;
    String id;
    ArrayList<String> idList;
    LinearLayout alerta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_amigos, container, false);

        rvAmigos = view.findViewById(R.id.rvAmigos);
        alerta = view.findViewById(R.id.alerta);
        rvAmigos.setHasFixedSize(true);
        rvAmigos.setLayoutManager(new LinearLayoutManager(getContext()));

        listaUsuarios = new ArrayList<>();
        idList = new ArrayList<>();

        id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        readUsuarios();

        return view;
    }

    private void readUsuarios() {
        reference = FirebaseDatabase.getInstance().getReference("Conexiones").child(id).child("siguiendo");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    idList.add(snap.getKey());
                }
                mostrarUsuarios();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void mostrarUsuarios() {
        reference = FirebaseDatabase.getInstance().getReference("Usuarios");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaUsuarios.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Usuario user = snap.getValue(Usuario.class);
                    for (String id : idList) {
                        if (user.getId().equals(id)) {
                            listaUsuarios.add(user);
                        }
                    }
                }

                if (listaUsuarios.isEmpty()) {
                    alerta.setVisibility(View.VISIBLE);
                }

                adapter = new AmigosAdapter(listaUsuarios);
                rvAmigos.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                adapter.setListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = rvAmigos.getChildAdapterPosition(v);
                        Usuario user = listaUsuarios.get(i);

                        Intent intent = new Intent(v.getContext(), MiPerfilActivity.class);
                        intent.putExtra(CLAVE_USUARIO, user);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}