package com.jzsec.web.apptest;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.jzsec.web.apptest.adapter.FragmentAdapter;
import com.jzsec.web.apptest.dialog.SimpleDialog;
import com.jzsec.web.apptest.fragment.FirstFragment;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dalvik.system.DexFile;

public class FragmentActivity extends androidx.fragment.app.FragmentActivity {

    private final String TAG = FragmentActivity.this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        ViewPager view_pager = findViewById(R.id.view_pager);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        view_pager.setAdapter(adapter);
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            FirstFragment fragment = new FirstFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", i);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        adapter.setFragments(fragments);
        findViewById(R.id.title_tv).setOnClickListener(v -> {
            new SimpleDialog(v.getContext()).show();
//
//            TextView textview = new TextView(v.getContext());
//            textview.setWidth(200);
//            textview.setHeight(200);
//            textview.setText("12345677990-");
//
//            PopupWindow popupWindow = new PopupWindow(textview, LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            popupWindow.setOutsideTouchable(true);
//            popupWindow.showAsDropDown(v, Gravity.CENTER, 100, 200);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(getClass().getSimpleName(), "====onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(getClass().getSimpleName(), "====onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Log.e(getClass().getSimpleName(), "====onResume" + getSourcePaths(this).toString());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final String EXTRACTED_NAME_EXT = ".classes";
    private static final String EXTRACTED_SUFFIX = ".zip";

    private static final String SECONDARY_FOLDER_NAME = "code_cache" + File.separator + "secondary-dexes";

    private static final String PREFS_FILE = "multidex.version";
    private static final String KEY_DEX_NUMBER = "dex.number";

    public List<String> getSourcePaths(Context context) throws PackageManager.NameNotFoundException, IOException {
        ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
        File sourceApk = new File(applicationInfo.sourceDir);

        List<String> sourcePaths = new ArrayList<>();
        sourcePaths.add(applicationInfo.sourceDir); //add the default apk path

        //the prefix of extracted file, ie: test.classes
        String extractedFilePrefix = sourceApk.getName() + EXTRACTED_NAME_EXT;

//        如果VM已经支持了MultiDex，就不要去Secondary Folder加载 Classesx.zip了，那里已经么有了
//        通过是否存在sp中的multidex.version是不准确的，因为从低版本升级上来的用户，是包含这个sp配置的
        if (!isVMMultidexCapable()) {
            //the total dex numbers
            int totalDexNumber = getMultiDexPreferences(context).getInt(KEY_DEX_NUMBER, 1);
            File dexDir = new File(applicationInfo.dataDir, SECONDARY_FOLDER_NAME);

            for (int secondaryNumber = 2; secondaryNumber <= totalDexNumber; secondaryNumber++) {
                //for each dex file, ie: test.classes2.zip, test.classes3.zip...
                String fileName = extractedFilePrefix + secondaryNumber + EXTRACTED_SUFFIX;
                File extractedFile = new File(dexDir, fileName);
                if (extractedFile.isFile()) {
                    sourcePaths.add(extractedFile.getAbsolutePath());
                    //we ignore the verify zip part
                } else {
                    throw new IOException("Missing extracted secondary dex file '" + extractedFile.getPath() + "'");
                }
            }
        }
        Log.e(TAG, "===" + (isVMMultidexCapable() + "  ==" + extractedFilePrefix
                + "   sourceDir===" + applicationInfo.sourceDir
                + "   dataDir===" + applicationInfo.dataDir
                + "   className===" + applicationInfo.className
        ));
        new Thread() {
            @Override
            public void run() {
                super.run();

                DexFile dexfile = null;
                try {
                    if (applicationInfo.sourceDir.endsWith(EXTRACTED_SUFFIX)) {
                        //NOT use new DexFile(path), because it will throw "permission error in /data/dalvik-cache"
                        dexfile = DexFile.loadDex(applicationInfo.sourceDir, applicationInfo.sourceDir + ".tmp", 0);
                    } else {
                        dexfile = new DexFile(applicationInfo.sourceDir);
                    }
                    Enumeration<String> dexEntries = dexfile.entries();
                    while (dexEntries.hasMoreElements()) {
                        String className = dexEntries.nextElement();
//                    if (className.startsWith(packageName)) {
//                        classNames.add(className);
//                    }
                        Log.e(TAG, "  === " + className);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
//        if (Config.debuggable) { // Search instant run support only debuggable
//            sourcePaths.addAll(tryLoadInstantRunDexFile(applicationInfo));
//        }
        return sourcePaths;
    }

    private static SharedPreferences getMultiDexPreferences(Context context) {
        return context.getSharedPreferences(PREFS_FILE, Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ? Context.MODE_PRIVATE : Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
    }

    /**
     * Get instant run dex path, used to catch the branch usingApkSplits=false.
     */
    private List<String> tryLoadInstantRunDexFile(ApplicationInfo applicationInfo) {
        List<String> instantRunSourcePaths = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && null != applicationInfo.splitSourceDirs) {
            // add the split apk, normally for InstantRun, and newest version.
            instantRunSourcePaths.addAll(Arrays.asList(applicationInfo.splitSourceDirs));
            Log.d(TAG, "Found InstantRun support");
        } else {
            try {
                // This man is reflection from Google instant run sdk, he will tell me where the dex files go.
                Class pathsByInstantRun = Class.forName("com.android.tools.fd.runtime.Paths");
                Method getDexFileDirectory = pathsByInstantRun.getMethod("getDexFileDirectory", String.class);
                String instantRunDexPath = (String) getDexFileDirectory.invoke(null, applicationInfo.packageName);

                File instantRunFilePath = new File(instantRunDexPath);
                if (instantRunFilePath.exists() && instantRunFilePath.isDirectory()) {
                    File[] dexFile = instantRunFilePath.listFiles();
                    for (File file : dexFile) {
                        if (null != file && file.exists() && file.isFile() && file.getName().endsWith(".dex")) {
                            instantRunSourcePaths.add(file.getAbsolutePath());
                        }
                    }
                    Log.d(TAG, "Found InstantRun support");
                }

            } catch (Exception e) {
                Log.e(TAG, "InstantRun support error, " + e.getMessage());
            }
        }

        return instantRunSourcePaths;
    }

    private static final int VM_WITH_MULTIDEX_VERSION_MAJOR = 2;
    private static final int VM_WITH_MULTIDEX_VERSION_MINOR = 1;

    /**
     * Identifies if the current VM has a native support for multidex, meaning there is no need for
     * additional installation by this library.
     *
     * @return true if the VM handles multidex
     */
    private boolean isVMMultidexCapable() {
        boolean isMultidexCapable = false;
        String vmName = null;

        try {
            if (isYunOS()) {    // YunOS需要特殊判断
                vmName = "'YunOS'";
                isMultidexCapable = Integer.valueOf(System.getProperty("ro.build.version.sdk")) >= 21;
            } else {    // 非YunOS原生Android
                vmName = "'Android'";
                String versionString = System.getProperty("java.vm.version");
                if (versionString != null) {
                    Matcher matcher = Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?").matcher(versionString);
                    if (matcher.matches()) {
                        try {
                            int major = Integer.parseInt(matcher.group(1));
                            int minor = Integer.parseInt(matcher.group(2));
                            isMultidexCapable = (major > VM_WITH_MULTIDEX_VERSION_MAJOR)
                                    || ((major == VM_WITH_MULTIDEX_VERSION_MAJOR)
                                    && (minor >= VM_WITH_MULTIDEX_VERSION_MINOR));
                        } catch (NumberFormatException ignore) {
                            // let isMultidexCapable be false
                        }
                    }
                }
            }
        } catch (Exception ignore) {

        }

        Log.e(TAG, "VM with name " + vmName + (isMultidexCapable ? " has multidex support" : " does not have multidex support"));
        return isMultidexCapable;
    }

    /**
     * 判断系统是否为YunOS系统
     */
    private static boolean isYunOS() {
        try {
            String version = System.getProperty("ro.yunos.version");
            String vmName = System.getProperty("java.vm.name");
            return (vmName != null && vmName.toLowerCase().contains("lemur"))
                    || (version != null && version.trim().length() > 0);
        } catch (Exception ignore) {
            return false;
        }
    }
}