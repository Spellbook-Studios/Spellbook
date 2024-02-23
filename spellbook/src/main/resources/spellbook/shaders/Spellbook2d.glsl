#version 330 core

layout (location=0) in vec2 position;
layout (location=1) in vec2 texcoord;

out vec2 uvCoords;

uniform mat4 mProj;
uniform mat4 mView;

void main()
{
    uvCoords = texcoord;

    vec4 pos = vec4(position, 0.0, 1.0);
    gl_Position = mProj * mView * pos;
}

// SPELLBOOK END VERTEX SHADER //

#version 330 core

in vec2 uvCoords;

out vec4 fragColor;

uniform sampler2D texSampler;
uniform vec4 matColor;
uniform int colorMode;

void main()
{
    fragColor = matColor * texture(texSampler, uvCoords);
}