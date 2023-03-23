package org.example;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.component.Component;

public class ChickenComponent extends Component {
    private Vec2 acceleration = new Vec2(-1,0);
    @Override
    public void onUpdate(double tpf) {
        acceleration.x -=  tpf * 2.5;
        entity.translate(acceleration.x,acceleration.y);
        super.onUpdate(tpf);
    }
}
