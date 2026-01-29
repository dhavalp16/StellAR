#include <jni.h>
#include <string>
#include <vector>
#include <cmath>
#include <sstream>
#include <iomanip>

// --- Physics Engine Core (The Engine Room) ---

struct Body {
    float x, y, z;
    float vx, vy, vz;
    float mass;
};

// Global storage for the universe
std::vector<Body> universe;
const float G = 1.0f;           // Gravitational Constant (Toy Scale)
const float EPSILON = 0.0001f;  // Softening parameter to prevent divide-by-zero

void stepPhysics(float dt) {
    size_t count = universe.size();

    // 1. Calculate Forces (Gravity)
    // We need a temporary buffer for forces or accelerations to avoid
    // updating positions mid-step which would skew calculations.
    // However, for simplicity in this toy model, we updates velocities then positions.
    // Ideally use Symplectic Euler or Verlet. Here we use semi-implicit Euler.

    for (size_t i = 0; i < count; ++i) {
        float fx = 0, fy = 0, fz = 0;

        for (size_t j = 0; j < count; ++j) {
            if (i == j) continue;

            float dx = universe[j].x - universe[i].x;
            float dy = universe[j].y - universe[i].y;
            float dz = universe[j].z - universe[i].z;

            float distSq = dx*dx + dy*dy + dz*dz + EPSILON;
            float dist = std::sqrt(distSq);

            // F = G * m1 * m2 / r^2
            // We want acceleration: a = F / m1 = G * m2 / r^2
            // Vector form: a_vec = (F / dist) * (vec / dist) -> G * m2 / r^3 * vec
            
            float forceMagnitude = (G * universe[j].mass) / distSq; // Actually this is acceleration logic if we ignore m1 in next step? 
            // Wait, Newton: F = G m1 m2 / r^2. 
            // a1 = F1 / m1 = G m2 / r^2.
            // Direction is normalized vector (dx/dist, dy/dist, dz/dist).
            // So a1_x = (G * m2 / dist^2) * (dx / dist) = G * m2 * dx / dist^3
            
            float invDist3 = 1.0f / (dist * dist * dist);
            float f = G * universe[j].mass * invDist3;

            fx += f * dx;
            fy += f * dy;
            fz += f * dz;
        }

        // 2. Update Velocity (v += a * dt)
        universe[i].vx += fx * dt;
        universe[i].vy += fy * dt;
        universe[i].vz += fz * dt;
    }

    // 3. Update Position (p += v * dt)
    for (size_t i = 0; i < count; ++i) {
        universe[i].x += universe[i].vx * dt;
        universe[i].y += universe[i].vy * dt;
        universe[i].z += universe[i].vz * dt;
    }
}

// --- JNI Bridge (The Phone Lines) ---

extern "C" JNIEXPORT jint JNICALL
Java_com_cosmic_1struck_stellar_MainActivity_nativeAddBody(
        JNIEnv* env,
        jobject /* this */,
        jfloat mass,
        jfloat x, jfloat y, jfloat z,
        jfloat vx, jfloat vy, jfloat vz) {
    
    Body b = {x, y, z, vx, vy, vz, mass};
    universe.push_back(b);
    return (jint)(universe.size() - 1);
}

extern "C" JNIEXPORT void JNICALL
Java_com_cosmic_1struck_stellar_MainActivity_nativeUpdate(
        JNIEnv* env,
        jobject /* this */,
        jfloat deltaTime) {
    stepPhysics(deltaTime);
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_cosmic_1struck_stellar_MainActivity_nativeGetPosition(
        JNIEnv* env,
        jobject /* this */,
        jint index) {
    
    if (index < 0 || index >= universe.size()) {
        return env->NewStringUTF("INVALID_INDEX");
    }

    Body& b = universe[index];
    std::stringstream ss;
    // Format to 3 decimal places for readability
    ss << std::fixed << std::setprecision(3) 
       << b.x << "," << b.y << "," << b.z;
    
    return env->NewStringUTF(ss.str().c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_cosmic_1struck_stellar_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "StellAR Physics Engine: ONLINE (C++)";
    return env->NewStringUTF(hello.c_str());
}
