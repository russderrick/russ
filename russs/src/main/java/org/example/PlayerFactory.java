package org.example;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import javafx.util.Duration;
import org.example.Component.*;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;
import static org.example.EntityType.*;

public class PlayerFactory implements EntityFactory {
    @Spawns("ikun")
    public Entity newikun(SpawnData data){
        return FXGL.entityBuilder(data)
                .at(50,0.5)
                .type(IKUN)
                .bbox(BoundingShape.box(56,100))
                .with(new IkunAnimationComponent())
                .with(new CollidableComponent(true))
                .with(new IkunComponent())
                .build();
    }
    @Spawns("chicken")
    public Entity newchicken(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(CHICKEN)
                .at(1000,720 * Math.random())
                .viewWithBBox("chicken.jpg")
                .with(new CollidableComponent(true))
                .with(new ChickenComponent())
                .build();
    }
    @Spawns("hardchicken")
    public Entity newhardchicken(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(CHICKEN)
                .at(1000 * Math.random(), 720)
                .viewWithBBox("chicken.jpg")
                .with(new CollidableComponent(true))
                .with(new HardchickenComponent())
                .build();
    }
    @Spawns("basketball")
    public Entity newbasketball(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(BASKETBALL)
                .at(1000, 720*Math.random())
                .viewWithBBox("basketball.jpg")
                .with(new CollidableComponent(true))
                .with(new BasketballComponent())
                .build();
    }
    @Spawns("bosschicken")
    public Entity newbosschicken(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(BOSS)
                .at(1000, 300)
                .view("boss.jpg")
                .bbox(BoundingShape.box(150,229))
                .with(new CollidableComponent(true))
                .with(new BosschickenComponent())
                .build();
    }
    @Spawns("bullet")
    public Entity newbullet(SpawnData data){
        Entity ikun = getGameWorld().getSingleton(IKUN);
        return FXGL.entityBuilder(data)
                .type(BULLET)
                .at(ikun.getRightX(), ikun.getY())
                .viewWithBBox("basketball.jpg")
                .with(new CollidableComponent(true))
                .with(new BulletComponent())
                .build();
    }
    @Spawns("bulletchicken")
    public Entity newbulletchicken(SpawnData data){
        Entity boss = getGameWorld().getSingleton(BOSS);
        return FXGL.entityBuilder(data)
                .type(BULLET)
                .at(boss.getX(), boss.getY())
                .viewWithBBox("chicken.jpg")
                .with(new CollidableComponent(true))
                .with(new BulletchickenComponent())
                .build();
    }
    @Spawns("explosion")
    public Entity newexplosion(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(EXPLOSION)
                .with(new ExplosionAnimationComponent())
                .with(new CollidableComponent(true))
                .build();
    }

}
