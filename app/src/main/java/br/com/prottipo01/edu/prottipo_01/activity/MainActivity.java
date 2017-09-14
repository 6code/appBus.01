package br.com.prottipo01.edu.prottipo_01.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import br.com.prottipo01.edu.prottipo_01.R;
import br.com.prottipo01.edu.prottipo_01.configure.configureFirebase;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    Button buttonLogout;
    Button buttonRapidinho;
    Button buttonCidade;

    String BDmarcadores;
    String BDrotasURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLogout = (Button) findViewById(R.id.id_btn_logout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseAuth = configureFirebase.getFirebaseAuth();
                firebaseAuth.signOut();
                finish();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        buttonRapidinho = (Button) findViewById(R.id.id_Button_rota_rapidinho);
        buttonRapidinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BDmarcadores = "Paradas-rapidinho";
                BDrotasURL = "https://firebasestorage.googleapis.com/v0/b/marine-embassy-175612.appspot.com/o/rapidinho.kml?alt=media&token=aa9621d0-ce1a-450a-8ae5-f8a3d5f6928d";

                String[] parametros = new String[]{BDmarcadores,BDrotasURL};

                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("parametros",parametros);

                startActivity(intent);

            }
        });

        buttonCidade = (Button) findViewById(R.id.id_Button_rota_cidade);
        buttonCidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BDmarcadores = "Paradas-cidade";
                BDrotasURL = "https://firebasestorage.googleapis.com/v0/b/marine-embassy-175612.appspot.com/o/cidade.kml?alt=media&token=7eea8242-2946-413c-aba8-9ee296ad0190";

                String[] parametros = new String[]{BDmarcadores,BDrotasURL};

                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("parametros",parametros);

                startActivity(intent);


            }
        });



    }

}
