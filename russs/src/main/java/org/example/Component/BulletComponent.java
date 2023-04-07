package org.example.Component;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.component.Component;

public class BulletComponent extends Component {

    private Vec2 acceleration = new Vec2(3,0);

    public void onUpdate(double tpf){
        entity.translate(acceleration.x, acceleration.y);

    }
}
