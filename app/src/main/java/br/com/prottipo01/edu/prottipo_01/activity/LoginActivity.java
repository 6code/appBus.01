package br.com.prottipo01.edu.prottipo_01.activity;

import android.content.Intent;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import br.com.prottipo01.edu.prottipo_01.R;
import br.com.prottipo01.edu.prottipo_01.configure.configureFirebase;
import br.com.prottipo01.edu.prottipo_01.configure.validaConexão;
import br.com.prottipo01.edu.prottipo_01.model.user;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private Button btnLogar;
    private user usuario;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        VerificaUsuarioLogado();

        email = (EditText) findViewById(R.id.id_email_login);
        senha = (EditText) findViewById(R.id.id_senha_login);
        btnLogar = (Button) findViewById(R.id.id_btn_logar);


        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                usuario = new user();
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());

                if(email.getText().toString().equals("") || senha.getText().toString().equals("")) {

                    Toast.makeText(LoginActivity.this, "Preencha todos os campos!!", Toast.LENGTH_LONG).show();

                }else{validaConex();}
            }
        });
    }



    public void validaConex(){

        validaConexão vld = new validaConexão(LoginActivity.this);

        if(vld.testaconexão() == true) {

            ValidarLogin();

        }else {

            Toast.makeText(LoginActivity.this, "Sem conexão com a internet!!", Toast.LENGTH_LONG).show();

        }
    }



    private void VerificaUsuarioLogado() {

        firebaseAuth = configureFirebase.getFirebaseAuth();
        if (firebaseAuth.getCurrentUser() != null) {
            AbrirTelaPrincipal();
        }


    }

    public void ValidarLogin() {

        firebaseAuth = configureFirebase.getFirebaseAuth();
        firebaseAuth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login feito com sucesso!!", Toast.LENGTH_LONG).show();
                            AbrirTelaPrincipal();

                        } else {

                            String erro = "";

                            try {

                                throw task.getException();

                            } catch (FirebaseAuthInvalidUserException e) {
                                erro = "conta do usuário correspondente ao e-mail não existi ou está desativada";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erro = "talvez sua senha esteja errada";
                            } catch (Exception e) {
                                e.printStackTrace();
                                erro = "Erro ao fazer login!!";
                            }

                            Toast.makeText(LoginActivity.this, "Opa, algo deu errado, " + erro, Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void AbrirTelaPrincipal() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void cadastro(View view) {

        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);

    }
}



