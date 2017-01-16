package com.dat.signallabs.lab2;

import android.content.Context;
import android.net.Uri;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SoundStream {
    byte[] fileHead = new byte[44];

    private int intFromBytes(byte buffer[]) {
        return (buffer[0] & 0xFF)
            | (buffer[1] & 0xFF) << 8
            | (buffer[2] & 0xFF) << 16
            | (buffer[3] & 0xFF) << 24;
    }

    private byte[] intToBytes(int data) {
        byte buffer[] = new byte[4];
        buffer[0] = (byte) (data & 0xFF);
        buffer[1] = (byte) ((data >> 8) & 0xFF);
        buffer[2] = (byte) ((data >> 16) & 0xFF);
        buffer[3] = (byte) ((data >> 24) & 0xFF);
        return buffer;
    }

    private byte[] shortToBytes(short data) {
        byte buffer[] = new byte[2];
        buffer[0] = (byte) (data & 0xFF);
        buffer[1] = (byte) ((data >> 8) & 0xFF);
        return buffer;
    }

    private short shortFromBytes(byte buffer[]) {
        return (short) ((buffer[0] & 0xFF) | (buffer[1] & 0xFF) << 8);
    }

    private double gc = 44100;

    double getGc() {
        return gc;
    }

    public ArrayList<Double> loadSignal(Uri uri, Context context) throws IOException {

        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        DataInputStream stream = new DataInputStream(inputStream);

        stream.read(fileHead);

        DataInputStream headStream = new DataInputStream(new ByteArrayInputStream(fileHead));

        System.out.print((char) headStream.readByte());
        System.out.print((char) headStream.readByte());
        System.out.print((char) headStream.readByte());
        System.out.print((char) headStream.readByte());// head[0]...head[3] – символы RIFF

        byte intBuffer[] = new byte[4];
        headStream.read(
            intBuffer); // head[4]...head[7] – размер файла -8, вычитаются как раз 8 байт. Например, 14395670
        int A = intFromBytes(intBuffer);
        System.out.println(A);

        System.out.print((char) headStream.readByte());
        System.out.print((char) headStream.readByte());
        System.out.print((char) headStream.readByte());
        System.out.print((char) headStream.readByte()); // head[8]...head[11] – символы WAVE

        System.out.print((char) headStream.readByte());
        System.out.print((char) headStream.readByte());
        System.out.print((char) headStream.readByte());
        System.out.print((char) headStream.readByte()); // head[12]...head[15] – символы fmt пробел

        headStream.readInt(); // head[16]=16;  head[17]=0 ; head[18]=0 ; head[19]=0 ;

        headStream.readInt(); // head[20]=0; head[21]=1;  head[22]=1; head[23]=0;

        headStream.read(intBuffer); // head[24]...head[27] – частота дискретизации.
        int gcc = intFromBytes(intBuffer);
        gc = gcc;
        System.out.println(gcc);

        headStream.read(
            intBuffer); // head[28]...head[31] – число байт в секунду. В примере 88200, т.к. каждая амплитуда отдельного звука – 2 байта.
        gcc = intFromBytes(intBuffer);
        System.out.println(gcc);

        headStream.readInt(); // head[32]=2; head[33]=0  количество байт для одного звука head[34]=16; head[35]=0;   количество бит для звука

        char c = (char) headStream.readByte();
        System.out.print(c);
        System.out.print((char) headStream.readByte());
        System.out.print((char) headStream.readByte());
        System.out.print((char) headStream.readByte()); // head[36]...head[39] – символы data

        int count = 0;
        if (c == 'd') {
            headStream.read(intBuffer); // head[40]...head[43] – размер данных
            count = intFromBytes(intBuffer) / 2;
            System.out.println(count);
        } else {
            headStream.read(intBuffer);
            boolean isDate = false;
            while (!isDate) {
                c = (char) stream.readByte();
                if (c == 'd') {
                    c = (char) stream.readByte();
                    if (c == 'a') {
                        c = (char) stream.readByte();
                        if (c == 't') {
                            c = (char) stream.readByte();
                            if (c == 'a') {
                                isDate = true;
                            }
                        }
                    }
                }
            }
            stream.read(intBuffer);
            count = intFromBytes(intBuffer) / 2;
            System.out.println(count);
        }

        //чтоение данных
        ArrayList<Double> readSignal = new ArrayList<Double>();
        byte shortBuffer[] = new byte[2];
        for (int i = 0; i < count; i++) {
            stream.read(shortBuffer);
            short s = shortFromBytes(shortBuffer);

            readSignal.add((double) s);
        }

        stream.close();

        return readSignal;
    }

    public void saveSignal(List<Float> signal, String fileName) throws IOException {
        File root = android.os.Environment.getExternalStorageDirectory();

        File dir = new File(root.getAbsolutePath() + "/download");
        dir.mkdirs();
        File file = new File(dir, fileName);
        int dataSize = signal.size() * 2;
        double value;
        byte buffer[] = intToBytes(dataSize + 36);
        fileHead[4] = buffer[0];
        fileHead[5] = buffer[1];
        fileHead[6] = buffer[2];
        fileHead[7] = buffer[3];
        buffer = intToBytes(dataSize);
        fileHead[36] = 'd';
        fileHead[37] = 'a';
        fileHead[38] = 't';
        fileHead[39] = 'a';
        fileHead[40] = buffer[0];
        fileHead[41] = buffer[1];
        fileHead[42] = buffer[2];
        fileHead[43] = buffer[3];
        FileOutputStream writer = new FileOutputStream(file);
        writer.write(fileHead);
        for (int i = 0; i < signal.size(); i++) {
            value = signal.get(i);
            short data = (short) value;
            byte shortBuffer[] = shortToBytes(data);
            writer.write(shortBuffer);
        }
        writer.close();
    }
}
