package org.example.Component;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.TimerAction;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppHeight;
import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

public class BosschickenComponent extends Component {

    private Vec2 acceleration = new Vec2(0, -1);
    @Override
    public void onUpdate(double tpf) {
        if(entity.getBottomY() > getAppHeight() ||entity.getBottomY() < 229)
            acceleration.y = - acceleration.y;
        entity.translate(acceleration);
        super.onUpdate(tpf);
    }
    public void firechicken(){
        spawn("bulletchicken", new SpawnData(0, 0).put("owner", getEntity()));

    }

}
