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

const float GRAY = 0.482352941;

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoord);
    float value = GRAY * abs(sin(u_time * 2));
	gl_FragColor = vec4(value, value, value, 1.0);
}
