package br.com.prottipo01.edu.prottipo_01.configure;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by edu on 31/08/17.
 */

public class validaConexão {

    private static boolean userConectado =true;
    private static Context cont;

    public validaConexão(Context context){ cont = context;}


    public static boolean testaconexão(){

        ConnectivityManager connectivity = (ConnectivityManager) cont.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivity.getActiveNetworkInfo();


        if (netInfo == null) {

           // userConectado = false;
        }


        return userConectado;
    }



}
