package com.example.cv_project.utils.gamedata;

import android.util.Pair;

public class LineInfo extends Pair<HexPosition, HexPosition> {
    /**
     * Constructor for a Pair.
     *
     * @param first  the first object in the Pair
     * @param second the second object in the pair
     */
    public LineInfo(HexPosition first, HexPosition second) {
        super(first, second);
    }
}
