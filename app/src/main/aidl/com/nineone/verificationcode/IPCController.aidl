// IPCController.aidl
package com.nineone.verificationcode;
import com.nineone.verificationcode.IPCCallback;
// Declare any non-default types here with import statements

interface IPCController {
      void sendMessageToServer(in String message);
      void registerCallback(IPCCallback callback);
      void unRegisterCallback();
}