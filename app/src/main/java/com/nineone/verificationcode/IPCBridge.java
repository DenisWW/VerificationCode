package com.nineone.verificationcode;

import android.os.IBinder;
import android.os.RemoteException;

import com.jzsec.basics.ipc.IPCCallback;
import com.jzsec.basics.ipc.IPCController;

class IPCBridge {
    private IPCController.Stub stub;
    private IPCBridgeListener listener;
    private IPCCallback callback;

    public IPCBridge() {
    }

    public void created(IPCBridgeListener ipcBridgeListener) {
        listener = ipcBridgeListener;
        stub = new IPCController.Stub() {

            @Override
            public void sendMessageToServer(String message) throws RemoteException {
                if (listener != null) listener.onClientMessageReceived(message);

            }

            @Override
            public void registerCallback(IPCCallback ipcCallback) {
                callback = ipcCallback;
            }

            @Override
            public void unRegisterCallback() {
                if (listener != null) listener.unRegisterCallback();
                callback = null;
            }
        };
    }

    public IBinder getBinder() {
        return stub;
    }

    void callMessageToClient(String serverMessage) {
        if (callback != null) {
            try {
                callback.callMessageToClient(serverMessage);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    interface IPCBridgeListener {
        void onClientMessageReceived(String clientMessage);

        void unRegisterCallback();
    }

}
