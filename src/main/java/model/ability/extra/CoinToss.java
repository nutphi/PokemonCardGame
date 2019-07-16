package model.ability.extra;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by nutph on 6/16/2017.
 */
public enum CoinToss {
    HEAD,TAIL;

    /**
     * list when created random toss
     * with an unmodifiable view of the specific collection
     */
    private static final List<CoinToss> VALUES = Collections
            .unmodifiableList(Arrays.asList(values()));

    /**
     * this is used to random HEAD/TAIL
     */
    private static final Random RANDOM = new Random();

    /**
     * size of coin
     */
    private static final int SIZE = VALUES.size();

    public static CoinToss random() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
