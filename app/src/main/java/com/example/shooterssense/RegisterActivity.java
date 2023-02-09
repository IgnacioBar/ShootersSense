package com.example.shooterssense;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    //TERMINADO-REVISADO
    EditText etNombreR;
    EditText etUsuarioR;
    EditText etEmailR;
    EditText etPassR;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        etNombreR = findViewById(R.id.etNombreR);
        etUsuarioR = findViewById(R.id.etUsuarioR);
        etEmailR = findViewById(R.id.etEmailR);
        etPassR = findViewById(R.id.etContraseñaR);

        auth = FirebaseAuth.getInstance();

    }

    public void signUpUser(View view) {
        pd = new ProgressDialog(RegisterActivity.this);
        pd.setMessage("Por favor espere...");
        pd.show();

        String sFullname = etNombreR.getText().toString().trim();
        String sUsername = etUsuarioR.getText().toString().trim();
        String sEmail = etEmailR.getText().toString().trim();
        String sPassword = etPassR.getText().toString().trim();

        if (sFullname.isEmpty() || sUsername.isEmpty() || sEmail.isEmpty() || sPassword.isEmpty()) {
            Toast.makeText(this, "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show();
            pd.dismiss();

        } else if (sPassword.length() < 8) {
            Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show();

        } else {
            registrarUsuario(sFullname, sUsername, sEmail, sPassword);
        }
    }

    private void registrarUsuario(String sFullname, String sUsername, String sEmail, String sPassword) {
        auth.createUserWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userId = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userId);

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("id", userId);
                            hashMap.put("fullname", sFullname);
                            hashMap.put("username", sUsername);
                            hashMap.put("fotoPerfilUrl", "https://firebasestorage.googleapis.com/v0/b/fir-1-4cac7.appspot.com/o/user.png?alt=media&token=256e0a83-bc34-436e-b78f-d012aa5ad14f");
                            hashMap.put("bio", "");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        pd.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });

                        } else {
                            pd.dismiss();
                            Toast.makeText(RegisterActivity.this, "Ya hay un usuario registrado con este mail",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void irLogin(View view) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}
