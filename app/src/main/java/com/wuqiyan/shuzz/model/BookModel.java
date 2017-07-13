package com.wuqiyan.shuzz.model;

import java.io.Serializable;

/**
 * Created by wuqiyan on 17/6/21.
 */

public class BookModel implements Serializable {
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

