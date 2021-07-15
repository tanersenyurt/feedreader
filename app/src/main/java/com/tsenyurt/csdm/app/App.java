/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.tsenyurt.csdm.app;

import com.tsenyurt.csdm.list.LinkedList;

import static com.tsenyurt.csdm.utilities.StringUtils.join;
import static com.tsenyurt.csdm.utilities.StringUtils.split;
import static com.tsenyurt.csdm.app.MessageUtils.getMessage;

import org.apache.commons.text.WordUtils;

public class App {
    public static void main(String[] args) {
        LinkedList tokens;
        tokens = split(getMessage());
        String result = join(tokens);
        System.out.println(WordUtils.capitalize(result));
    }
}
