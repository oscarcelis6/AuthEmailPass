package com.oscarcelis.authemailpass;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnRegistrar;
    private Button btnIngresar;
    private Button btnRecuperarClave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });

        btnIngresar = findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingresarUsuarioApp();
            }
        });

        btnRecuperarClave = findViewById(R.id.btnRecuperarClave);
        btnRecuperarClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recuperarClave();
            }
        });
    }

    private void recuperarClave() {
        final String email = edtEmail.getText().toString().trim();

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this,
                                    "Se ha enviado un email a " + email,
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this,
                                    "No se pudo enviar el email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void ingresarUsuarioApp() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,
                    "Digite un email válido", Toast.LENGTH_SHORT).show();
            return;
        }if(TextUtils.isEmpty(password)){
            Toast.makeText(this,
                    "Digite una contraseña válida", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this,
                                    "Bienvenido", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Principal.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(MainActivity.this,
                                    "Verifique los datos ingresados", Toast.LENGTH_SHORT).show();
                                    edtEmail.setText("");
                                    edtPassword.setText("");
                                    return;
                        }
                    }
                });
    }

    private void registrarUsuario() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,
                    "Digite un email válido", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,
                    "Digite una clave válida", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this,
                                    "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Principal.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(MainActivity.this,
                                    "No se pudo regustrar el usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
