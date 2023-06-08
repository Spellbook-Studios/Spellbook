#version 330

in vec2 position;
in vec2 textureCords;

out vec4 color;
out vec2 uvCoords;

uniform mat4 projection;

uniform vec4 offset;

uniform vec2 pixelScale;
uniform vec2 screenPos;

void main()
{
    gl_Position = projection * vec4((position * pixelScale) + screenPos, 0, 1.0);
    uvCoords = (textureCords * offset.zw) + offset.xy;
}
// SPELLBOOK END VERTEX SHADER //

#version 330

uniform sampler2D sampler;
uniform vec4 color;
uniform int useColor;

in vec2 uvCoords;

out vec4 fragColor;

void main()
{
    if(useColor == 1) {
        fragColor = color;
    } else {
        fragColor = color * texture(sampler, uvCoords);
    }
}