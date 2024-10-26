package org.landon.physics;

import org.joml.Vector3f;
import org.landon.core.Time;
import org.landon.math.Transform;

import java.util.ArrayList;
import java.util.List;

public class MovingBody {

    private Transform transform;
    private Vector3f velocity;
    private Vector3f acceleration;

    private float mass;

    private List<Vector3f> forces;

    public MovingBody(Transform transform, float mass) {
        this.transform = transform;
        this.mass = mass;
        this.velocity = new Vector3f();
        this.acceleration = new Vector3f();
        this.forces = new ArrayList<>();

        forces.add(new Vector3f(0, -9.8f * mass, 0));
    }

    public void approximate(float dt) {
        float x = 0.5f * acceleration.x * dt * dt + velocity.x * dt;
        float y = 0.5f * acceleration.y * dt * dt + velocity.y * dt;
        float z = 0.5f * acceleration.z * dt * dt + velocity.z * dt;

        velocity.add(acceleration.mul(dt));
        transform.getPosition().add(x, y, z);
    }

    public void update() {
        for (Vector3f force : forces) {
            applyForce(force);
        }
        approximate((float) Time.getDelta());
    }

    public void applyForce(Vector3f force) {
        acceleration.add(force.div(mass));
    }

    public Transform getTransform() {
        return transform;
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
    }

    public Vector3f getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector3f acceleration) {
        this.acceleration = acceleration;
    }

}
