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

import com.example.shooterssense.MiPerfilActivity;
import com.example.shooterssense.R;
import com.example.shooterssense.adapters.AmigosAdapter;
import com.example.shooterssense.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ComunidadFragment extends Fragment {
    //TERMINADO-REVISADO
    public static final String CLAVE_USUARIO = "USUARIO";

    RecyclerView rvComunidad;
    AmigosAdapter adapter;
    DatabaseReference reference;
    ArrayList<Usuario> listaUsuarios;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comunidad, container, false);

        rvComunidad = view.findViewById(R.id.rvComunidad);
        rvComunidad.setHasFixedSize(true);
        rvComunidad.setLayoutManager(new LinearLayoutManager(getContext()));
        listaUsuarios = new ArrayList<>();

        readUsuarios();
        return view;
    }

    private void readUsuarios() {
        reference = FirebaseDatabase.getInstance().getReference("Usuarios");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Usuario user = snap.getValue(Usuario.class);
                    listaUsuarios.add(user);
                }

                adapter = new AmigosAdapter(listaUsuarios);
                adapter.setListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = rvComunidad.getChildAdapterPosition(v);
                        Usuario user = listaUsuarios.get(i);

                        Intent intent = new Intent(v.getContext(), MiPerfilActivity.class);
                        intent.putExtra(CLAVE_USUARIO, user);
                        startActivity(intent);
                    }
                });
                adapter.notifyDataSetChanged();
                rvComunidad.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}