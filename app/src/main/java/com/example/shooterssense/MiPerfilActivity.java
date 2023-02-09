package com.example.shooterssense;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.shooterssense.fragments.AmigosFragment;
import com.example.shooterssense.model.Usuario;


public class MiPerfilActivity extends AppCompatActivity {
    //TERMINADO-REVISADO
    ImageView ivMiPerfil;
    TextView tvUsername;
    TextView tvFullname;
    TextView tvBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);

        ivMiPerfil = findViewById(R.id.ivMiPerfil);
        tvUsername = findViewById(R.id.tvUsername);
        tvFullname = findViewById(R.id.tvFullname);
        tvBio = findViewById(R.id.tvBio);

        Usuario user = getIntent().getParcelableExtra(AmigosFragment.CLAVE_USUARIO);

        Glide.with(this)
                .load(user.getFotoPerfilUrl())
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .placeholder(new ColorDrawable(this.getResources().getColor(R.color.white)))
                .into(ivMiPerfil);

        tvUsername.setText(user.getUsername());
        tvFullname.setText(user.getFullname());
        tvBio.setText(user.getBio());
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}