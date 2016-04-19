package com.example;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Administrator on 2016-04-18 15:53
 */
public class Mt {
    public static void main(String[] args) {
        String text = "[list2],[sdfgsfg]";
        String[] str = {"",""};
        boolean matches = Pattern.matches("((\\[.*\\]),*)+", text);
        System.out.println(matches);
        String[] split = text.split("\\[.*\\],*");
        System.out.println(Arrays.toString(split));

    }
}
