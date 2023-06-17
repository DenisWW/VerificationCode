package com.nineone.verificationcode;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.jzsec.basics.ipc.IPCCallback;
import com.jzsec.basics.ipc.IPCController;

public abstract class IPCClient implements ServiceConnection {
    private IPCController controller;

    public IPCClient() {
    }

    private final IPCCallback callback = new IPCCallback.Stub() {

        @Override
        public void callMessageToClient(String serverMessage) {
            onMessageReceive(serverMessage);
        }
    };


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        controller = IPCController.Stub.asInterface(service);
        try {
            controller.registerCallback(callback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        controller = null;
    }

    protected void sendMessage(String clientMessage) {
        if (controller != null) {
            try {
                controller.sendMessageToServer(clientMessage);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }

    public void unRegisterCallback() {
        if (controller != null) {
            try {
                controller.unRegisterCallback();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract void onMessageReceive(String serverMessage);

}
