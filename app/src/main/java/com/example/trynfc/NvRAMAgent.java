package com.example.trynfc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface NvRAMAgent extends IInterface {

    public static abstract class Stub extends Binder implements NvRAMAgent {
        private static final String DESCRIPTOR = "NvRAMAgent";
        static final int TRANSACTION_readFile = 1;
        static final int TRANSACTION_writeFile = 2;

        private static class Proxy implements NvRAMAgent {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public byte[] readFile(int i) throws RemoteException {
                throw new Error("Unresolved compilation problem: \n\tDuplicate local variable _result\n");
            }

            public int writeFile(int i, byte[] bArr) throws RemoteException {
                throw new Error("Unresolved compilation problem: \n\tDuplicate local variable _result\n");
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static NvRAMAgent asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof NvRAMAgent)) {
                return new Proxy(obj);
            }
            return (NvRAMAgent) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            throw new Error("Unresolved compilation problems: \n\tDuplicate local variable _arg0\n\tDuplicate local variable _result\n");
        }
    }

    byte[] readFile(int i) throws RemoteException;

    int writeFile(int i, byte[] bArr) throws RemoteException;
}
