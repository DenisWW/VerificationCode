#include <jni.h>
#include <string>
#include <iostream>
#include <fstream>
#include <cmath>
#include <ctime>
#include <android/log.h>
#include "csignal"
#include "pthread.h"
#include "thread"
#include "vector"
#include "list"
#include "glob.h"
#include "zlib.h"
//#include "cgicc/CgiDefs.h"
#include "drm/drm.h"

namespace why {

}
using namespace std;

extern int a;
#define LENGTH 20
#define WIDTH 10
#define NEWLINE '\n'
#define LOG_TAG    "native-dev"
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
struct User {
    char name[10];
    int age;
} user;

extern "C" JNIEXPORT jstring

JNICALL
Java_com_nineone_verificationcode_activity_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C" JNIEXPORT jstring
JNICALL
Java_com_nineone_verificationcode_activity_BesselActivity_stringFromJNI(
        JNIEnv *env,
        jobject, jstring s1, jstring s2) {
//    const int variable = WIDTH;
    string n = "why";
//    User *u1;
//    strcpy(u1->name, "C++");
    time_t now = time(0);
    tm *ltm = localtime(&now);;
    std::string h = to_string(ltm->tm_year + 1900);
    std::string hello = "Hello from C++ " + h;
    const char *tochar = env->GetStringUTFChars(s1, 0);
//    vector<int> vec;
//    int i;
//    list<int> l1;
//    for (int i = 0; i < 5; i++) {
//        vec.push_back(i);
//    }
//    auto v = vec.begin();
//    while (v != vec.end()) {
//        LOGE("vec大小33===%d", v);
//        v++;
//    }

    LOGE("%s", tochar);
    return env->NewStringUTF(hello.c_str());
}



extern "C" JNIEXPORT jstring
JNICALL
Java_com_nineone_verificationcode_activity_BesselActivity_openFile(
        JNIEnv *env, jobject) {
    ofstream ofstream1;
    ofstream1.open("", ios::out | ios::in);
//    ofstream1.seekp()
}

int arrayName[10] = {};
int *p;

double getPosition(int arr[], int size);

string userName(int &x1, int *x2) {
    p = arrayName;
//    int q = getPosition(p, 3);
    int a = p[1];
    int b = 10;
    int &c = b;//& 引用
    static string why = "why";
    try {

    } catch (std::exception msg) {

    }
    return why;
}

int *getRandom() {
    int rp[10];
    return rp;
}

double vals[] = {12};

double getPosition(int arr[], int size) {
    double &p = vals[size];
//    time()
    ofstream *ofstream;
    return p;
}

class Box {


public:
    int size;
    int height;

// 成员函数声明
    int get() const;

    void set(int size, int height);

    friend void A();

    Box();// 这是构造函数声明
    ~Box(); // 这是析构函数声明 delete对象时执行
    Box(const Box &box); //拷贝构造函数通过使用另一个同类型的对象来初始化新创建的对象。复制对象把它作为参数传递给函数。 复制对象，并从函数返回这个对象
    friend class box;

private:
    int m;

};

inline int Box::get() const {
    return size * height;
}

void Box::set(int s, int h) {
    size = s;
    height = h;
}

class ABox {

};

#define  MIN(a, b)( a<b ? a : b)

void signalHandler(int signum) {
//    exit(signum);
}

struct thread_data {
    int thread_id;
    char *message;
};


void *sayHello(void *args) {
    auto *d = (struct thread_data *) args;
    pthread_exit(nullptr);
}
//#include <sys/mman.h>
//#include <sys/stat.h>
//#include <fcntl.h>
//#include <stdio.h>
//#include <stdlib.h>
//#include <unistd.h>

class ShapeBox : public Box, public ABox {
    void get() {
//        malloc()
        Box *b = new Box();
//        MIN(1,2);
//        raise()
        pthread_t pthread;
        struct thread_data data{1, "Test"};
        data.message = "message";
        data.thread_id = 1;
        //参数依次是：创建的线程id，线程参数，调用的函数，传入的函数参数
        pthread_create(&pthread, nullptr, sayHello, (void *) &data);
//        pthread_join()
        //等各个线程退出后，进程才结束，否则进程强制结束了，线程可能还没反应过来；
        pthread_exit(nullptr);
        signal(SIGINT, signalHandler);
        thread t1();
//        pthread_join() 链接
//        pthread_detach() 分离
        pthread_attr_t attr;
        pthread_attr_init(&attr);


//        vector<int> v;
//        v.push_back(2);
//        vector<int>::iterator t = v.begin();
//        while (t != v.end()) {
//
//        }

        cout << "输出是什么呢";
        delete b;
    }
};



