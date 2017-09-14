package br.com.prottipo01.edu.prottipo_01.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.Language;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import br.com.prottipo01.edu.prottipo_01.R;
import br.com.prottipo01.edu.prottipo_01.assyncTask.Download;
import br.com.prottipo01.edu.prottipo_01.configure.configureFirebase;
import br.com.prottipo01.edu.prottipo_01.model.loadMarkers;
import br.com.prottipo01.edu.prottipo_01.model.myLocation;
import br.com.prottipo01.edu.prottipo_01.model.readLocationBD;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int MY_LOCATION_REQUEST_CODE = 1; //codigo para ter controle de que local estou fazendo essa validaçao
    private String[] MinhasPermissoes = new String[]{Manifest.permission.ACCESS_FINE_LOCATION}; //string de permissoes necessarias

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /*************************************************************************************************************/


    /**
     * Somos definidos por nossas ações, não palavras!!!
     */


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latLng = new LatLng(-3.776872, -49.675535);
        CameraPosition update = new CameraPosition(latLng, 13, 0, 0);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(update), 1000, null);

        //aqui eu chamo as funçoes onde eu faço as alterações no meu mapa


        MinhaLocalização();
        LoadMarkers();
        LoadRotas();
        MinhaLocalizaçãoBD();
        // lerLocalizaçãoBD();
     //  navegation();
       // EventoDeClickMarkres();

    }


    /*************************************************************************************************************/


    public void LoadRotas() {

        Intent it = getIntent();
        String[] child = it.getStringArrayExtra("parametros"); //recebendo parametros passados pela activity main

        String url = child[1];
        Download dw = (Download) new Download(MapsActivity.this, mMap).execute(url); //executanod esse processo de forma assincrona

    }


    //verifica se p usuario tem permissao, se nao ele pede permissao
    public void MinhaLocalização() {

        //veririfca se o ususario ja deu a permissao
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            //   mMap.setTrafficEnabled(true); //mostra o trafego

        } else {
            //pede a permissao para o usuario
            String[] p = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, p, MY_LOCATION_REQUEST_CODE);
        }
    }

    //Tratando a resposta da permissao em tempo de execução
    //grantResults retorna o se o usurio deu a permissao
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        //aqui eu implemento a interface padrap do metodo onRequest, por isso eu uso o super
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            mMap.setMyLocationEnabled(true); //caso o usuario de a permissao, ele seta  a localizaçao do usuaio
            //   mMap.setTrafficEnabled(true);
        }

    }


    /*************************************************************************************************************/


    public void LoadMarkers() {

        Intent it = getIntent();
        String[] child = it.getStringArrayExtra("parametros");
        String ref = child[0];


        final FirebaseDatabase database = null;

        final DatabaseReference markers = database.getInstance().getReference().child(ref);

        markers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { //datasnapshot captura qualquer  alteraçoes no BD

                if (dataSnapshot.getChildren() != null) {
                    LoadRotas();
                }

                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                mMap.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshots) {

                    loadMarkers mdlk = dataSnapshot1.getValue(loadMarkers.class);
                    mMap.addMarker(new MarkerOptions().position(new LatLng(mdlk.getLatitude(), mdlk.getLongitude())).title(mdlk.getParada()));
                    markers.keepSynced(true);
                    mMap.getUiSettings().setMapToolbarEnabled(false);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /*************************************************************************************************************/


    public void MinhaLocalizaçãoBD() {

        FirebaseUser user = configureFirebase.getFirebaseAuth().getCurrentUser();

        final String uid = user.getUid();


        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

            @Override
            public void onMyLocationChange(Location location) {

                myLocation my = new myLocation();
                my.setLatitude(location.getLatitude());
                my.setLongitude(location.getLongitude());
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("Usuarios").child(uid).child("OndeEstou").setValue(my);

            }
        });

    }


    /*************************************************************************************************************/
    public void lerLocalizaçãoBD() {

        FirebaseUser user = configureFirebase.getFirebaseAuth().getCurrentUser();
        final String uid = user.getUid();


        final DatabaseReference dtf = configureFirebase.getDatabaseReference().child("Usuarios").child(uid).child("OndeEstou");


        dtf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                readLocationBD locationBD = dataSnapshot.getValue(readLocationBD.class);
                mMap.addMarker(new MarkerOptions().position(new LatLng(locationBD.getLatitude(), locationBD.getLongitude())).title("Lendo Localização"));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    /*************************************************************************************************************/

    public void teste() {


        String serverKey = "AIzaSyDTUt4CBUnZRCCn3OFX1qxEpxKQs05qP5o";

        LatLng origin = new LatLng(-3.779698, -49.675009);
        final LatLng destination = new LatLng(-3.776872, -49.675535);

        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.WALKING)
                .language(Language.PORTUGUESE_BRAZIL)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        // Do something here
                        if (direction.isOK()) {

                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);

                            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                            PolylineOptions polylineOptions = DirectionConverter.createPolyline(MapsActivity.this, directionPositionList, 3, Color.RED);
                            mMap.addPolyline(polylineOptions);
                            // Do something
                        }

                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something here
                    }
                });

    }


    public void navegation() {



        Uri gmmIntentUri = Uri.parse("google.navigation:q=-3.776872, -49.675535");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        startActivity(mapIntent);

    }


    public void EventoDeClickMarkres(){



        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                LatLng latLng = marker.getPosition();
                Toast.makeText(MapsActivity.this, "" + latLng ,Toast.LENGTH_LONG).show();



                return false;
            }
        });

    }

}
