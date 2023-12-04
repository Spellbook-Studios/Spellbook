package dk.sebsa.spellbook.data;

import java.io.Serializable;
import java.util.Objects;

public class TestData implements Serializable {
    public final String s;
    public final float f;

    public TestData(String s, float f) {
        this.s = s;
        this.f = f;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestData testData = (TestData) o;
        return Float.compare(f, testData.f) == 0 && Objects.equals(s, testData.s);
    }

    @Override
    public int hashCode() {
        return Objects.hash(s, f);
    }
}
