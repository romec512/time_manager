package Source;

import android.view.View;
import android.view.animation.TranslateAnimation;

public class AnimationHelper {
    private View view;

    public AnimationHelper(View _view){
        view = _view;
    }

    public void slideIn(int offset){
        TranslateAnimation animate = new TranslateAnimation(0,0,offset,0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }

}
