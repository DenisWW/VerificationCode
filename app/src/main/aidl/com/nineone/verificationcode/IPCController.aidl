// IPCController.aidl
package com.jzsec.basics.ipc;
import com.jzsec.basics.ipc.IPCCallback;
// Declare any non-default types here with import statements

interface IPCController {
      void sendMessageToServer(in String message);
      void registerCallback(IPCCallback callback);
      void unRegisterCallback();
}