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
    float val = GRAY * (1.0 + 0.25 * sin(u_time * 4.0));
    gl_FragColor = vec4(val, val, val, 1.0);
}
