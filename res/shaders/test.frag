#define AMPLITUDE 0.2
#define SPEED 0.05

varying vec2 texCoord;
varying vec3 color;

uniform sampler2D tex;
uniform sampler2D noise;
uniform float globalTime;

vec4 rgbShift( in vec2 p , in vec4 shift) {
    shift *= 2.0*shift.w - 1.0;
    vec2 rs = vec2(shift.x,-shift.y);
    vec2 gs = vec2(shift.y,-shift.z);
    vec2 bs = vec2(shift.z,-shift.x);

    float r = texture(tex, p+rs, 0.0).x;
    float g = texture(tex, p+gs, 0.0).y;
    float b = texture(tex, p+bs, 0.0).z;

    return vec4(r,g,b,1.0);
}

vec4 vec4pow( in vec4 v, in float p ) {
    return vec4(pow(v.x,p),pow(v.y,p),pow(v.z,p),v.w);
}

vec4 noiseAAA( in vec2 p ) {
    return texture(noise, p, 0.0);
}

void main() {
	vec2 p = texCoord;
    vec4 c = vec4(0.0,0.0,0.0,1.0);

    vec4 shift = vec4pow(noiseAAA(vec2(SPEED*globalTime,2.0*SPEED*globalTime/25.0 )),8.0)
        		*vec4(AMPLITUDE,AMPLITUDE,AMPLITUDE,1.0);;

    c += rgbShift(p, shift);
    //vec4 color = texture2D(tex,texCoord);
    gl_FragColor = c * vec4(color, 1.0);
}