package com.example.shooterssense;

import static com.example.shooterssense.R.id.ivPostFoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.shooterssense.fragments.HomeFragment;
import com.example.shooterssense.model.Post;
import com.example.shooterssense.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostDetalleActivity extends AppCompatActivity {
    //TERMINADO-REVISADO
    ImageView ivPostFoto;
    TextView tvTituloAyuda;
    TextView tvDescripcionAyuda;
    TextView tvNombreAy;
    TextView tvOcupacionAy;
    ImageView ivUsuarioAy;
    Button btnAyudar;
    ConstraintLayout infoUsuario;

    String fotoPerfil = "";

    Usuario userBusqueda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detalle);

        ivPostFoto = findViewById(R.id.ivPostFoto);
        tvTituloAyuda = findViewById(R.id.tvTituloAyuda);
        tvDescripcionAyuda = findViewById(R.id.tvDescripcionAyuda);

        ivUsuarioAy = findViewById(R.id.ivUsuarioAy);
        tvNombreAy = findViewById(R.id.tvNombreAy);
        tvOcupacionAy = findViewById(R.id.tvOcupacionAy);
        btnAyudar = findViewById(R.id.btnAyudar);
        infoUsuario = findViewById(R.id.infoUsuario);

        Post post = getIntent().getParcelableExtra(HomeFragment.CLAVE_POST);
        String id = getIntent().getStringExtra("USER");

        Glide.with(this)
                .load(post.getImageUrl())
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .placeholder(new ColorDrawable(this.getResources().getColor(R.color.white)))
                .into(ivPostFoto);

        tvTituloAyuda.setText(post.getTitulo());
        tvDescripcionAyuda.setText(post.getDescripcion());

        FirebaseDatabase.getInstance().getReference("Usuarios")
                .orderByChild("id").equalTo(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            userBusqueda = data.getValue(Usuario.class);
                            tvNombreAy.setText(userBusqueda.getFullname());
                            tvOcupacionAy.setText(userBusqueda.getUsername());
                            fotoPerfil = userBusqueda.getFotoPerfilUrl();
                            Glide.with(PostDetalleActivity.this)
                                    .load(fotoPerfil)
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(ivUsuarioAy);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        btnAyudar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ComentarioActivity.class);
                i.putExtra("POST_ID", post.getId());
                i.putExtra("PUBLISHER_ID", post.getPublisher());
                startActivity(i);
            }
        });

        infoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MiPerfilActivity.class);
                intent.putExtra("USUARIO", userBusqueda);
                startActivity(intent);
            }
        });

    }
}