package com.translatedsong.app.resource;

import com.translatedsong.app.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.view.View;

/**
 * Manipula el mostrar u ocultar el System UI (status bar and navigation/system bar)
 */
public class SystemUIManager {

    /**
     * Indica si el system UI (status bar and navigation/system bar) se oculta automaticamente
     * despues de {@link #AUTO_HIDE_DELAY_MILLIS} milisegundos.
     */
    public static final boolean AUTO_HIDE = true;

    /**
     * Si {@link #AUTO_HIDE} es verdadero, indica los miliseguntos que espera antes de
     * ocultar automaticamente.
     */
    public static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Indica si intercala entre mostrar y ocultar al dar clic, o solo mostrar.
     */
    public static final boolean TOGGLE_ON_CLICK = true;

    /**
     * Instance de {@link com.translatedsong.app.util.SystemUiHider} que sirve para manipular el
     * system UI (status bar and navigation/system bar).
     */
    private SystemUiHider mSystemUiHider;

    /**
     * Contructor de la clase
     *
     * @param activity    Actividad sobre la cual se implementara el mostrar u ocultar el System UI
     * @param contentView Vista que representa el Layout principal
     */
    public SystemUIManager(final Activity activity, final View contentView) {
        this(activity, contentView, null);
    }

    /**
     * Contructor de la clase
     *
     * @param activity     Actividad sobre la cual se implementara el mostrar u ocultar el System UI
     * @param contentView  Vista que representa el Layout principal
     * @param controlsView Vista que representa el Layout de los controles que se desean ocultar junto con el System UI
     */
    public SystemUIManager(final Activity activity, final View contentView, final View controlsView) {
        // Crea la instancia de SystemUiHider para manipular el mostrar u ocultar el System UI
        mSystemUiHider = SystemUiHider.getInstance(activity, contentView, SystemUiHider.FLAG_HIDE_NAVIGATION);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (controlsView != null) {
                            //Muestra u Oculta el LayOut de los controles
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                                //si la version es mas actual que HONEYCOMB_MR2 usa animacion
                                controlsView.animate().translationY(visible ? 0 : -controlsView.getHeight()).setDuration(1000);
                            } else {
                                controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                            }
                        }

                        //Si el System UI esta visible, lo oculta despues de AUTO_HIDE_DELAY_MILLIS
                        if (visible && SystemUIManager.AUTO_HIDE) {
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Listener del principal LayOut y al dar clic mustra u oculta el System UI
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });
    }

    private Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Planifica el ocultar el system UI despues de los milisegundos indicados,
     * cancelando cualquier otra planificacion previa.
     */
    public void delayedHide(int delayMillis) {
        //cancelando cualquier otra planificacion previa
        mHideHandler.removeCallbacks(mHideRunnable);
        //planifica el ocultar el system UI despues de los milisegundos indicados
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    /**
     * Planifica el ocultar el system UI despues de AUTO_HIDE_DELAY_MILLIS,
     * cancelando cualquier otra planificacion previa.
     */
    public void delayedHide() {
        delayedHide(AUTO_HIDE_DELAY_MILLIS);
    }
}
