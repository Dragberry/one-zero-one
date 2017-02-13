#ifdef GL_ES
precision mediump float;
precision mediump int;
#else
#define highp;
#endif

uniform sampler2D u_texture;

varying vec4 v_color;
varying vec2 v_texCoord;

uniform float progress;

const float OPACITY = 0.9;

void main() {
	vec4 texColor = texture2D(u_texture, v_texCoord);
	float darkness = 1.0 - (progress * 0.8);
	texColor.rgb = mix(texColor.rgb, texColor.rgb * darkness, OPACITY);
	gl_FragColor = vec4(texColor.r, texColor.g, texColor.b, texColor.a);
}
