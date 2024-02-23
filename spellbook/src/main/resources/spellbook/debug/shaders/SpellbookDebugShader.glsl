#version 330 core

layout (location=0) in vec2 position;
layout (location=1) in vec2 texcoord;

uniform int mode;
const uint ModeScreenSpace  = 1u;
const uint ModeWorldCamera  = 2u;

uniform mat4 mProj;
uniform mat4 mView;

void main() {
    if(mode == 2) {
        vec4 worldPosistion = vec4(position, 0.0, 1.0);
        gl_Position = mProj * mView * worldPosistion;
    } else if (mode == 1) {
        gl_Position = vec4(position, 0.0, 1.0);
    }
}

// SPELLBOOK END VERTEX SHADER //
#version 330 core

uniform vec4 matColor;
out vec4 fragColor;

void main() {
    fragColor = matColor;
}