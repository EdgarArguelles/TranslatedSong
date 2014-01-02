package com.translatedsong.app.resource;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.translatedsong.app.MainActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Obtiene la informacion de la cansion que esta sonando
 */
public class SongManager {

    /**
     * Almacena la informacion de la cancion actual
     */
    private final Map<String, String> metadata = new HashMap<String, String>();

    /**
     * Crea un Listener que detecta cuando una cancion se esta reproduciendo y almacena su informacion
     */
    public SongManager(final MainActivity activity) {
        //lee todos los eventos del smartphone y filtra los eventos de com.android.music.metachanged
        IntentFilter iF = new IntentFilter();
        iF.addAction("com.android.music.metachanged");

        //cada que se encuentra un eveneto com.android.music.metachanged ejecuta la funcion onReceive
        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                metadata.put("artist", intent.getStringExtra("artist"));
                metadata.put("album", intent.getStringExtra("album"));
                metadata.put("track", intent.getStringExtra("track"));

                //informa a la Activity que ocurrio un evento de cambio de cancion
                activity.onChangeSong();
            }
        };
        activity.registerReceiver(mReceiver, iF);
    }

    /**
     * get Map con la informacion de la cancion actual
     */
    public Map<String, String> getMetadata() {
        return metadata;
    }
}
