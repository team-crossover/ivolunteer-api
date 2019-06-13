package com.crossover.ivolunteer.util;

import java.util.Objects;

public class ArrayUtils {

    public static <T> boolean contains(T[] array, T targetValue) {
        for (T value : array) {
            if (Objects.equals(value, targetValue))
                return true;
        }
        return false;
    }

    public static <T> boolean containsAny(T[] array, T[] targetValues) {
        for (T value : array) {
            for (T targetValue : targetValues)
                if (Objects.equals(value, targetValue))
                    return true;
        }
        return false;
    }

}
