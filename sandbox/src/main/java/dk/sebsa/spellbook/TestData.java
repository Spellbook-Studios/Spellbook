package dk.sebsa.spellbook;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public class TestData implements Serializable {
    public String s;

    public String getS() {
        return s;
    }

    public TestData setS(String s) {
        this.s = s;
        return this;
    }
}
