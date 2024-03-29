package com.nineone.verificationcode.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nineone.verificationcode.R
import com.nineone.verificationcode.utils.SimpleData
import kotlinx.android.synthetic.main.activity_kotlin.*
import kotlinx.android.synthetic.main.activity_kotlin.tv_test
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor

class KotlinActivity : AppCompatActivity() {
    var list: ArrayList<String>? = null;
    var a = 0;
    var b = 1;
    var simpleflow: Flow<String>? = null
    var sharedFlow: MutableStateFlow<String>? = null
    val myViewModel: MyViewModel by lazy {
        ViewModelProvider(this).get(MyViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        val data = null
        Log.e(localClassName, "${data ?: "121131"}")
//        test()
//        val job = testJob()
//        val simpleData = SimpleData("用户名");
//        Log.e(localClassName, "$job+             +${simpleData}")
//        val my = thread { kotlin.run {  }}.start()
//        val myThread = object : Thread() {
//            override fun run() {
//                super.run()
//            }
//        }.start()
        myViewModel.model.postValue("哈哈哈哈")
        Log.e("it====", "===${Intent.FLAG_ACTIVITY_NEW_TASK}");
        tv_test.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
//                GlobalScope.launch(Dispatchers.Default) {
//                    sharedFlow("why");
//                }
                myViewModel.model.observe(this@KotlinActivity) {
                    Log.e("it====", "===${it}");
                }
            }
        })


//        channelFlow<String> {
//
//        }
//       dataEt.transformationMethod = object : ReplacementTransformationMethod() {
//            override fun getOriginal(): CharArray {
//                return charArrayOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
//                    'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', ' ')
//            }
//
//            override fun getReplacement(): CharArray {
//                return charArrayOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
//                    'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '')
//            }
//        }
        runBlocking {
            simpleflow = simpleFlow();
            simpleflow?.let {
                it.map {
                    "string $it"//对参数进行包装
                }
//                    .flatMapMerge {
//                        flow<Int> {
//flattenMerge 不会组合多个 flow ，而是将它们作为单个流执行。相当于rxJava
//                        }
//                    }
//                ?.zip(flow<String> { }, { s: String, s2: String -> })与Rxjava 的zip一致
//                ?.combine()//与 zip 不同的点在于，combine 会将 flowA 发射的 emit 与 flowB **最新发射 **的 emit 组合起来
                    .flowOn(Dispatchers.IO)//flowOn函数方便我们指定上流是否使用子协程执行
                    .transform {
//                    when (it) {
//                        "string why1" -> emit(1)
//                        "string why2" -> emit(2)
//                        "string why3" -> emit(3)
//                    }
                        emit("更改数据")
                    }
//                ?.take(2)//限制次数操作符
                    .collect {
                        Log.e("test=", it.toString() + "  " + Thread.currentThread().name)
                    }
            }
//            flow<String> {
//                emit("发射数据")
//            }


        }
        runBlocking {
            flow<Bitmap> {
                //模拟网络请求
                emit(BitmapFactory.decodeResource(resources, R.mipmap.placeholder))
            }
                .flowOn(Dispatchers.IO)
                .onStart { }
                .onCompletion { }
                .onEach { }
                .onEach { }
                .take(1)
                .drop(1)
                .buffer()
                .distinctUntilChanged()
                .collect {
                    image.setImageBitmap(it)
                }
            channelFlow<String> { }.take(1).collect { }


            (1..4).asFlow().flatMapMerge<Int, String> { flow<String> { emit("") } }
            flowOf((1..4).asFlow(), (2..4).asFlow()).zip(flow<String> { }) { s1, s2 -> }
                .combine(flow<String> { }) { s, b -> }.collect { }
        }


    }
    @Suppress("BlockingMethodInNonBlockingContext")
    fun saveLog(context: Context, value: String, name: String) {
        runBlocking {
            val job = launch {
                val file: File = File(context.filesDir.path + "/${name}.txt")
                var fileOutputStream: FileOutputStream? = null
                try {
                    val parent = file.parentFile
                    if (parent != null && !parent.exists()) parent.mkdirs()
                    if (!file.exists()) file.createNewFile()
                    fileOutputStream = FileOutputStream(file)
                    fileOutputStream.write(encodeToString(value.toByteArray(), DEFAULT).toByteArray())
                    fileOutputStream.flush()

                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    fileOutputStream?.close()
                }
            }
            job.start()
        }
    }

    //channelFlow 返回的是热流 是异步的。
    fun simpleFlow(): Flow<String> =
        flow {//flow 默认是同步的，冷流（类似 懒加载 ，在订阅后发射的数据才存在内存里） 可以通过 flowOn切换线程 切换成子线程后和 channelFlow花的时间差不多
            delay(500)
            emit("模拟网络请求")

        }

    fun simpleMFlow(): MutableStateFlow<String> =
        MutableStateFlow("")


    private fun test() = runBlocking {
//    Log.e(
//        localClassName,
//        "协程执行:${Thread.currentThread().name + "      id==" + Thread.currentThread().id}"
//    )
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


    fun text(string: suspend () -> String) {
        GlobalScope.launch(Dispatchers.IO) {
            var s: String = string()
            Log.e("text", "=====$s");
        }
    }
}

class MyViewModel : ViewModel() {
    val model: MutableLiveData<String> = MutableLiveData()

}
typealias UserCall = (SimpleData) -> Unit