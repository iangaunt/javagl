#version 400 core

in vec3 position;
out vec3 color;

uniform float time = 0.0f;

void main() {
    gl_Position = vec4(position, 1.0);
    color = vec3(position.x + 0.25 + time, 0.33f, position.y + 0.25);
}