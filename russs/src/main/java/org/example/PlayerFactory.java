package org.example;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;

import static org.example.EntityType.CHICKEN;
import static org.example.EntityType.IKUN;

public class PlayerFactory implements EntityFactory {
    @Spawns("ikun")
    public Entity newikun(SpawnData data){
        return FXGL.entityBuilder(data)
                .at(50,0.5)
                .type(IKUN)
                .bbox(BoundingShape.box(56,100))
                .with(new AnimationComponent())
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

}
