package com.nineone.verificationcode.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.nineone.verificationcode.BookController;
import com.nineone.verificationcode.bean.Book;

import java.util.ArrayList;
import java.util.List;

public class AIDLService extends Service {

    private List<Book> list;

    public AIDLService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        list = new ArrayList<>();
        initData();
    }

    private void initData() {
        Book book1 = new Book("活着");
        Book book2 = new Book("或者");
        Book book3 = new Book("叶应是叶");
        Book book4 = new Book("https://github.com/leavesC");
        Book book5 = new Book("http://www.jianshu.com/u/9df45b87cfdf");
        Book book6 = new Book("http://blog.csdn.net/new_one_object");
        list.add(book1);
        list.add(book2);
        list.add(book3);
        list.add(book4);
        list.add(book5);
        list.add(book6);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    private final BookController.Stub stub = new BookController.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return list;
        }

        @Override
        public void addBookInOut(Book book) throws RemoteException {
            if (book != null) {
                Log.e("addBookInOut","=="+book.name);
            }
        }
    };

}