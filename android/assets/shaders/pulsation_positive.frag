#ifdef GL_ES
precision mediump float;
precision mediump int;
#else
#define highp;
#endif

uniform sampler2D u_texture;

varying vec4 v_color;
varying vec2 v_texCoord;

uniform float u_time;

const float R = 0.247058824;
const float G = 0.741176471;
const float B = 0.117647059;

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoord);
    float val = 1 + 0.25 * sin(u_time * 4.0);
	gl_FragColor = vec4(R * val, G * val, B * val, 1.0);
}
