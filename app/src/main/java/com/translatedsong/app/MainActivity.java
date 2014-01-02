package com.translatedsong.app;

import com.translatedsong.app.resource.SongManager;
import com.translatedsong.app.resource.SystemUIManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    /**
     * Instance de {@link com.translatedsong.app.resource.SystemUIManager} que habilita y manupula
     * el mostrar u ocultar el system UI (status bar and navigation/system bar).
     */
    private SystemUIManager systemUIManager;

    /**
     * Instance de {@link com.translatedsong.app.resource.SongManager} que detecta los cuando
     * se reproduce una cancion y almacena la informacion de esta.
     */
    private SongManager songManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);

        //crea la instancia de SystemUIManager para habilitar y manipular el mostrar u ocultar el system UI
        systemUIManager = new SystemUIManager(this, contentView, controlsView);

        //crea la instancia de la clase que escucha cuando se cambia una cancion
        songManager = new SongManager(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (systemUIManager != null) {
            //al arrancar la aplicacion, ocultar el Symtem UI
            systemUIManager.delayedHide(3000);
        }
    }

    public void onChangeSong() {
        TextView text = (TextView) findViewById(R.id.song_info);
        text.setText(getString(R.string.artist) + " : " + songManager.getMetadata().get("artist") + "\n" +
                getString(R.string.album) + " : " + songManager.getMetadata().get("album") + "\n" +
                getString(R.string.track) + " : " + songManager.getMetadata().get("track"));
    }
}
