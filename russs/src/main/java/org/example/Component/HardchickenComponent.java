package org.example.Component;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

public class HardchickenComponent extends Component {
        private Vec2 acceleration = new Vec2(-2 * Math.random() + 1 ,-0.5 * Math.random());
    @Override
    public void onUpdate(double tpf) {
        acceleration.x += -8 * Math.random() + 4;
        acceleration.y += -0.5 * Math.random() ;
        entity.translate(acceleration.x,acceleration.y);
        super.onUpdate(tpf);
    }
}
