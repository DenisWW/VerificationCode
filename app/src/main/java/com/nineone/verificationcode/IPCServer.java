package com.nineone.verificationcode;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public abstract class IPCServer extends Service implements IPCBridge.IPCBridgeListener {
    private IPCBridge ipcBridge;

    @Override
    public void onCreate() {
        super.onCreate();
        ipcBridge = new IPCBridge();
        ipcBridge.created(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return ipcBridge.getBinder();
    }

    @Override
    public void onClientMessageReceived(String clientMessage) {
        onMessageReceive(clientMessage);
    }

    @Override
    public void unRegisterCallback() {

    }

    public void sendMessage(String serverMessage) {
        ipcBridge.callMessageToClient(serverMessage);
    }

    protected abstract void onMessageReceive(String clientMessage);

}
