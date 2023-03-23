package org.example;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppHeight;

public class AnimationComponent extends Component {
    private AnimatedTexture texture;
    private AnimationChannel anim1, anim2, anim3, anim4, anim5, anim6, anim7, anim8;
    public AnimationComponent(){
        anim1 = new AnimationChannel(FXGL.image("ikun3.jpg"), 9, 56, 100, Duration.seconds(1), 0, 8);
        anim2 = new AnimationChannel(FXGL.image("ikun3.jpg"), 9, 56, 100, Duration.seconds(1), 1, 1 );
        texture = new AnimatedTexture(anim2);
    }

    @Override
    public void onUpdate(double tpf) {

        if(entity.getBottomY() < getAppHeight()){
            if(texture.getAnimationChannel() == anim2)
                texture.loopAnimationChannel(anim1);
        }
        else{
            texture.loopAnimationChannel(anim2);
        }
        super.onUpdate(tpf);
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
        super.onAdded();
    }
}
