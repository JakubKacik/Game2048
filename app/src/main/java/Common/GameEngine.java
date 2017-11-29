package Common;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.example.czjakac.a2048.R;

/**
 * Created by CZJAKAC on 29.11.2017.
 */

public class GameEngine {

    private static Context context;
    private static MediaPlayer mediaPlayer;

    public static void bounceView(View view) {
        final Animation viewBounceAnimIn = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.bounce);
        BounceInterpolator interpolator = new BounceInterpolator(0.2, 10);
        viewBounceAnimIn.setInterpolator(interpolator);
        view.startAnimation(viewBounceAnimIn);
    }

    public static void vibrate(int miliseconds){
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if(vibrator.hasVibrator()){
            vibrator.vibrate(miliseconds);
        }
    }

    public static void playSound(int id) {
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(context, id);
        if(mediaPlayer != null){
            mediaPlayer.start();
        }
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        GameEngine.context = context;
    }
}
