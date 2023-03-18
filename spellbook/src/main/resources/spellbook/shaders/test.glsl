#version 330

layout (location=0) in vec3 inPosition;

void main() {
    gl_Position = vec4(inPosition, 1.0);
}

// SPELLBOOK END VERTEX SHADER //
#version 330

out vec4 fragColor;

void main() {
    fragColor = vec4(1.0, 0.412, 0.706, 1.0);
}