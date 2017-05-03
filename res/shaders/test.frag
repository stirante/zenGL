#version 330 core

uniform sampler2D u_texture;

in vec2 vTexCoord;
in vec4 vColor;

void main() {
    vec4 texColor = texture2D(u_texture, vTexCoord);

    texColor.rgb = 1.0 - texColor.rgb;

    gl_FragColor = vColor * texColor;
}