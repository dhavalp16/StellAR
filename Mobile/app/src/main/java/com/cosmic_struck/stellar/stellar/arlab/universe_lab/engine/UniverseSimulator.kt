package com.cosmic_struck.stellar.stellar.arlab.universe_lab.engine

class UniverseSimulator {
    private val engine = PhysicsEngine()

    // Create a simple solar system for testing
    fun createSolarSystem() {
        engine.reset()

        // Sun (stationary at origin)
        engine.addBody(CelestialBody(
            id = "sun",
            position = Vector3D(0f, 0f, 0f),
            velocity = Vector3D(0f, 0f, 0f),
            acceleration = Vector3D(0f, 0f, 0f),
            mass = 1989f, // kg (scaled)
            radius = 69.6f,
            name = "Sun"
        ))

        // Earth (orbiting sun)
        engine.addBody(CelestialBody(
            id = "earth",
            position = Vector3D(149.6f, 0f, 0f),
            velocity = Vector3D(0f, 29.78f, 0f),
            acceleration = Vector3D(0f, 0f, 0f),
            mass = 5.972f,
            radius = 6.371f,
            name = "Earth"
        ))

        // Moon (orbiting earth)
        engine.addBody(CelestialBody(
            id = "moon",
            position = Vector3D(149.6f + 0.384f, 0f, 0f),
            velocity = Vector3D(0f, 29.78f + 1.022f, 0f),
            acceleration = Vector3D(0f, 0f, 0f),
            mass = 0.07342f,
            radius = 1.737f,
            name = "Moon"
        ))
    }

    fun step() = engine.step()
    fun stepMultiple(steps: Int) = engine.stepMultiple(steps)
    fun getEngine(): PhysicsEngine = engine
    fun getBodies(): List<CelestialBody> = engine.getBodies()
}