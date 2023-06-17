package com.nineone.verificationcode;

import android.content.Context;
import android.content.Intent;

public class IPC {
    public static void startProcess(Context context, Class<? extends IPCServer> ipcServer,  IPCClient ipcClient) {
        context.bindService(new Intent(context, ipcServer), ipcClient, Context.BIND_AUTO_CREATE);
    }
}
