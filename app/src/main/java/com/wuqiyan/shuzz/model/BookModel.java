package com.wuqiyan.shuzz.model;

/**
 * Created by wuqiyan on 17/6/21.
 */

public class BookModel {
        public String bookImgUrl;
        public String bookName;
        public String author;
        public String desc;
        public String publishingHouse;
        public String contentUrl;

        @Override
        public String toString() {
                return bookName+" "+author+" "+" "+publishingHouse+"\n";
        }
}

