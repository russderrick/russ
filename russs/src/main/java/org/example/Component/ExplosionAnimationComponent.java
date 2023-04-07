package org.example.Component;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

public class ExplosionAnimationComponent extends Component{
    private AnimatedTexture texture;
    private  AnimationChannel anim1;
    public ExplosionAnimationComponent(){
        anim1 = new AnimationChannel(FXGL.image("explosion.jpg"), 16, 50, 50, Duration.seconds(1), 0, 15);
        texture = new AnimatedTexture(anim1);
        texture.loopAnimationChannel(anim1);
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
        super.onAdded();
    }
}
