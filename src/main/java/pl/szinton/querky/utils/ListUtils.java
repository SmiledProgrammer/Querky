package pl.szinton.querky.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    public static <T> List<T> createAndInitList(int size, T initValue) {
        List<T> list = new ArrayList<>();
        while (list.size() < size) {
            list.add(initValue);
        }
        return list;
    }
}
