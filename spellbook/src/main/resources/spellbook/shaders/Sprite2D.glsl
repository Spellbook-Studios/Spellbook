#version 330

in vec2 position;
in vec2 textureCords;

out vec4 color;
out vec2 uvCoords;

uniform mat4 projectionViewMatrix;

uniform vec4 matColor;
uniform vec4 offset;

uniform vec2 pixelScale;
uniform mat4 transformMatrix;
uniform vec2 anchor;
uniform vec2 objectScale;

void main()
{
    color = matColor;

    vec4 worldPosition = vec4((position - anchor) * (pixelScale * objectScale), 0, 1) * transformMatrix;
    gl_Position = projectionViewMatrix * worldPosition;
    uvCoords = (textureCords * offset.zw) + offset.xy;
}
// SPELLBOOK END VERTEX SHADER //
#version 330
uniform sampler2D sampler;

in vec4 color;
in vec2 uvCoords;

out vec4 fragColor;

void main()
{
    fragColor = color * texture(sampler, uvCoords);
}