package com.wuqiyan.shuzz.model;

/**
 * Created by wuqiyan on 17/6/21.
 */

public class IturingBookModel {
        public String bookImgUrl;
        public String bookName;
        public String author;
        public String desc;

        @Override
        public String toString() {
                return bookImgUrl+" "+ bookName+" "+author+" "+desc+"\n";
        }
}

