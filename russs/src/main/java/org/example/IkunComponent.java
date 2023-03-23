package org.example;

import com.almasb.fxgl.core.math.Vec2;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class IkunComponent extends Component {
    private static Vec2 acceleration = new Vec2(0,0);

    @Override
    public void onUpdate(double tpf) {


        acceleration.y += tpf * 8;
        if(acceleration.x < 0)
            acceleration.x += tpf * 8;
        if(acceleration.x > 0)
            acceleration.x -= tpf * 8;

        if (acceleration.y < -5)
            acceleration.y = -5;

        if (acceleration.y > 5)
            acceleration.y = 5;

        entity.translate(acceleration.x, acceleration.y);

        if(entity.getBottomY() > getAppHeight()) {
            FXGL.<Main>getAppCast().requestNewGame();
        }
        if(entity.getBottomY() < 100){
            FXGL.<Main>getAppCast().requestNewGame();
        }
        if(entity.getRightX() < 56){
            FXGL.<Main>getAppCast().requestNewGame();
        }
        super.onUpdate(tpf);
    }

    public static void jump(){
        acceleration.addLocal(0,-5);
    }
    public static void forward() {
        acceleration.addLocal(0.5, 0);
    }
    public static void backward() {
        acceleration.addLocal(-0.5, 0);
    }
}
