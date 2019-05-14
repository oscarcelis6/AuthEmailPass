package com.oscarcelis.authemailpass;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import entidades.Usuario;

public class Principal extends AppCompatActivity {

    private Button btnCambiarClave;

    private FirebaseAuth mAuth;

    private Button btnSignOut;
    private Button btnInsertarDatosUser;
    private Button btnConsultarDato;

    private TextView txtCurrentUser;

    private DatabaseReference mDatabase;

    private EditText edtId;
    private EditText edtNombre;
    private EditText edtEmail;


    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios");

        mAuth = FirebaseAuth.getInstance();
        final String usuarioActual = mAuth.getCurrentUser().getEmail().toString();

        txtCurrentUser = findViewById(R.id.txtCurrentUser);
        txtCurrentUser.setText("Está autenticado como " + usuarioActual);

        btnSignOut = findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        btnCambiarClave = findViewById(R.id.btnCambiarClave);
        btnCambiarClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarClave();
            }
        });

        btnInsertarDatosUser = findViewById(R.id.btnInsertarDatosUsuario);

        btnConsultarDato = findViewById(R.id.btnConsultarDato);


        edtNombre = findViewById(R.id.edtNombre);
        edtEmail = findViewById(R.id.edtEmail);
        edtEmail.setText(mAuth.getCurrentUser().getEmail().toString());
        edtId = findViewById(R.id.edtId);


        usuario = new Usuario();


        //Insertar datos en Firebase realtime database
        btnInsertarDatosUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insertarDatosUsuario();

            }
        });

        btnConsultarDato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarDato();
            }
        });


    }

    private void consultarDato() {
        Intent intent = new Intent(getApplicationContext(), ConsultarDato.class);
        startActivity(intent);
    }

    private void insertarDatosUsuario() {
        String idUsuario = edtId.getText().toString().trim();
        String nombreUsuario = edtNombre.getText().toString().trim();
        String emailUsuario = edtEmail.getText().toString().trim();

        usuario.setId(idUsuario);
        usuario.setNombre(nombreUsuario);
        usuario.setEmail(emailUsuario);

        mDatabase.push().setValue(usuario);
        Toast.makeText(Principal.this, "Registro creado con éxito", Toast.LENGTH_SHORT).show();
    }

    private void cambiarClave() {
        final String usuarioActual = mAuth.getCurrentUser().getEmail().toString();

        mAuth.sendPasswordResetEmail(String.valueOf(usuarioActual))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Principal.this,
                                    "Se ha enviado un correo a " + usuarioActual ,
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Principal.this,
                                    "No se ha podido enviar el email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void signOut() {
        mAuth.signOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
