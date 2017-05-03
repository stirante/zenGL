#version 330 core

layout (location = 0) in vec4 position;
layout (location = 1) in vec2 tc;

out vec2 vTexCoord;
out vec4 vColor;

void main() {
    gl_Position = position;
    vTexCoord = tc;
}