package cn.wowspeeder.encryption;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class CryptIOBase implements ICrypt {

    protected final String _name;
    protected boolean isForUdp;
    protected final Lock encLock = new ReentrantLock();
    protected final Lock decLock = new ReentrantLock();
    private static InternalLogger logger = InternalLoggerFactory.getInstance(CryptIOBase.class);

    public CryptIOBase(String name, String passWord) {
        _name = name.toLowerCase();
    }

    @Override
    public void isForUdp(boolean isForUdp) {
        this.isForUdp = isForUdp;
    }

    @Override
    public void encrypt(byte[] data, ByteArrayOutputStream stream) {
        synchronized (encLock) {
            stream.reset();
            _encrypt(data, stream);
        }
    }

    @Override
    public void encrypt(byte[] data, int length, ByteArrayOutputStream stream) {
        byte[] d = Arrays.copyOfRange(data, 0, length);
        encrypt(d, stream);
    }

    @Override
    public void decrypt(byte[] data, ByteArrayOutputStream stream) {
        synchronized (decLock) {
            stream.reset();
            _decrypt(data, stream);
        }
    }

    @Override
    public void decrypt(byte[] data, int length, ByteArrayOutputStream stream) {
        byte[] d = Arrays.copyOfRange(data, 0, length);
        decrypt(d, stream);
    }

    protected abstract void _encrypt(byte[] data, ByteArrayOutputStream stream);

    protected abstract void _decrypt(byte[] data, ByteArrayOutputStream stream);

}
