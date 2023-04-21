package org.example.Component;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppHeight;

public class BasketballComponent extends Component {
    private Vec2 acceleration = new Vec2(-4,-4*Math.random()+2);
    @Override
    public void onUpdate(double tpf) {
        if(entity.getBottomY() > getAppHeight() ||entity.getBottomY() < 100)
            acceleration.y = - acceleration.y;
        entity.translate(acceleration.x, acceleration.y);
        super.onUpdate(tpf);
    }
}
