package br.com.prottipo01.edu.prottipo_01.activity;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import br.com.prottipo01.edu.prottipo_01.R;
import br.com.prottipo01.edu.prottipo_01.configure.configureFirebase;
import br.com.prottipo01.edu.prottipo_01.configure.validaConexão;
import br.com.prottipo01.edu.prottipo_01.model.user;

public class CadastroActivity extends AppCompatActivity {


    private EditText nome;
    private EditText email;
    private EditText senha;
    private Button BtnCadastrar;
    private user usuario;
    private FirebaseAuth firebaseAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);


        nome = (EditText)findViewById(R.id.id_cadastro_nome);
        email = (EditText)findViewById(R.id.id_cadastro_email);
        senha = (EditText)findViewById(R.id.id_cadastro_senha);
        BtnCadastrar = (Button)findViewById(R.id.id_btn_cadastar);

        BtnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                usuario = new user();

                usuario.setNome(nome.getText().toString());
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());

                if (email.getText().toString().equals("") || senha.getText().toString().equals("")) {
                    Toast.makeText(CadastroActivity.this, "Preencha todos os campos!!", Toast.LENGTH_LONG).show();
                } else {
                    validaConex();
                }
            }
        });
    }


    public void validaConex(){

        validaConexão vld = new validaConexão(CadastroActivity.this);

        if(vld.testaconexão() == true) {
            cadastraUser();
        }else {

            Toast.makeText(CadastroActivity.this, "Sem conexão com a internet!!", Toast.LENGTH_LONG).show();

        }
    }


    public void cadastraUser(){

            firebaseAuth = configureFirebase.getFirebaseAuth();
            firebaseAuth.createUserWithEmailAndPassword(usuario.getEmail(),usuario.getSenha())
                    .addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(CadastroActivity.this, "Sucesso ao cadastrar!!",Toast.LENGTH_LONG).show();

                                FirebaseUser firebaseUser = task.getResult().getUser();
                                usuario.setId(firebaseUser.getUid());
                                usuario.salvar();

                                AbrirTelaPrincipal();

                            }else{

                                String erro = "";

                                try{
                                    throw task.getException();

                                } catch (FirebaseAuthWeakPasswordException e) {
                                    erro = " A senha deve ter pelo menos seis caracteres: user123";

                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    erro = " Digite um e-mail valido: 'exemple@email.com'";
                                } catch (FirebaseAuthUserCollisionException e) {

                                    erro = " Esse e-mail já esta em uso no App!!!";
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    erro = " Erro ao Cadastar";
                                }

                                Toast.makeText(CadastroActivity.this, "Opa, algo deu errado! " + erro,Toast.LENGTH_LONG).show();}
                        }
                    });

        }

    private void AbrirTelaPrincipal(){
        Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    }




