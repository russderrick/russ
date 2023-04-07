package org.example.Component;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.Required;

@Required(OwnerComponent.class)
public class BulletchickenComponent extends Component {
    private OwnerComponent owner;
    private Vec2 acceleration = new Vec2(-2,0);

    public void onUpdate(double tpf){
        entity.translate(acceleration.x, acceleration.y);

    }
}
