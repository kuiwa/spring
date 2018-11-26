package com.mymaven.eclipsetips;

public class EclipseTips {

    public static void main(String[] args) {
        int htmlInt = 5;
        Parser parser = new HtmlParser(htmlInt);
        parser.parse("somehtml");
    }
}
