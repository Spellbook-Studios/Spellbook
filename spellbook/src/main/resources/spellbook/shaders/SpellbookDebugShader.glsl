#version 330

layout (location=0) in vec3 position;
uniform int mode;

const uint ModeScreenSpace  = 1u;
uniform mat4 projectionViewMatrix;

const uint ModeWorldCamera  = 2u;

void main() {
    if(mode == 2) {
        vec4 worldPosistion = vec4(position, 1);
        gl_Position = projectionViewMatrix * worldPosistion;
    } else if (mode == 1) {
        gl_Position = vec4(position, 1.0);
    }
}

// SPELLBOOK END VERTEX SHADER //
#version 330

uniform vec4 color;
out vec4 fragColor;

void main() {
    fragColor = color;
}