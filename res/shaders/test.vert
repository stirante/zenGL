varying vec2 texCoord;
varying vec3 color;

void main() {
    gl_Position = ftransform();
    texCoord = gl_MultiTexCoord0.st;
    color = gl_Color.rgb;
}