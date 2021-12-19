package com.nineone.verificationcode.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.nineone.verificationcode.R
import com.nineone.verificationcode.service.SPUtils
import com.nineone.verificationcode.utils.RetrofitCoroutineDSL
import com.nineone.verificationcode.utils.SimpleData
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import java.util.*
import kotlin.concurrent.thread
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import  kotlinx.android.synthetic.main.activity_kotlin.*

class KotlinActivity : AppCompatActivity() {
    var list: ArrayList<String>? = null;
    var a = 0;
    var b = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        Log.e(localClassName, "${mainLooper.thread.name + "      id==" + mainLooper.thread.id}")
//        test()
        val job = testJob()
        val simpleData = SimpleData("用户名");
        Log.e(localClassName, "$job+             +${simpleData}")

        val (sex) = simpleData;
        val my = thread { }
        val myThread = object : Thread() {
            override fun run() {
                super.run()
            }
        }.start()
        b = (a + b);
        var v = b.rangeTo(10);
        Log.e("v", "=====" + v);
//        if (a + b > 0) {
//
//        }
        tv_test.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun test() = runBlocking {

        Log.e(
            localClassName,
            "协程执行:${Thread.currentThread().name + "      id==" + Thread.currentThread().id}"
        )
        delay(1000)

    }

    //在 JVM 上 delay 实际上是在一个 ScheduledExcecutor 里面添加了一个延时任务，因此会发生线程切换
    private fun testJob() = GlobalScope.launch(MyContinuationInterceptor()) {
//        Log.e(localClassName, "协程执行:${Thread.currentThread().name + "      id==" + Thread.currentThread().id}")
        delay(1000)
//        testSuspend()
//        Log.e(localClassName, "协程执行2:${Thread.currentThread().name + "      id==" + Thread.currentThread().id}")
        val one = GlobalScope.async {
            delay(1000)
            Log.e(
                localClassName,
                "协程执行3:${Thread.currentThread().name + "      id==" + Thread.currentThread().id}"
            )
            "hhhhh"

        }
        val two = GlobalScope.async {
            delay(3000)
            Log.e(
                localClassName,
                "协程执行4:${Thread.currentThread().name + "      id==" + Thread.currentThread().id}"
            )

        }
        launch { }
        val t = one.await() + two.await()
        Log.e(localClassName, "===${t}+“          ”+${getName()}");

    }

    private suspend fun testSuspend() {
        delay(1000)
    }

    private suspend fun getName(): String {

        return "mingzi";

    }

    private fun testVoid(string: String) {
        Log.e(localClassName, "");
    }

    class MyContinuationInterceptor : ContinuationInterceptor {
        override val key = ContinuationInterceptor
        override fun <T> interceptContinuation(continuation: Continuation<T>) =
            MyContinuation(continuation)

    }

    class MyContinuation<T>(val continuation: Continuation<T>) : Continuation<T> {
        override val context = continuation.context
        override fun resumeWith(result: Result<T>) {
            Log.e("<MyContinuation>", "${result}")
            continuation.resumeWith(result)
        }

    }

}

typealias UserCall = (SimpleData) -> Unit