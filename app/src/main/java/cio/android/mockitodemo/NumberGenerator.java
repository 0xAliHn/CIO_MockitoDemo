package cio.android.mockitodemo;

import java.util.Random;

public final class NumberGenerator {
    public int randomize(int i) {
        return new Random().nextInt(i);
    }

}
