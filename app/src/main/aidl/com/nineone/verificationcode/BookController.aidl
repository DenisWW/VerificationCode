// BookController.aidl
package com.nineone.verificationcode;

// Declare any non-default types here with import statements
import com.nineone.verificationcode.bean.Book;
interface BookController {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);
      List<Book> getBookList();
      void addBookInOut(in Book book);

}