package com.jzsec.web.testa;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import java.io.File;

public class UpgradeHelper {

    public static boolean isHasInstallPermission(Context context) {
        boolean hasInstallPermission = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            hasInstallPermission = context.getPackageManager().canRequestPackageInstalls();
        return hasInstallPermission;
    }
    public  static Intent getInstallIntent(Context context, String apkFilePath) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context,
                    context.getPackageName() + ".provider", new File(apkFilePath));
        } else {
            uri = Uri.fromFile(new File(apkFilePath));
        }
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    public  static void installAPK(Context context, String fullPath) {
        if (TextUtils.isEmpty(fullPath) || !new File(fullPath).exists()) {
            return;
        }
        context.startActivity(getInstallIntent(context,fullPath));
    }
}
