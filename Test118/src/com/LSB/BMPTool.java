package com.LSB;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.views.MainActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;

/**
 * This is a tool class for converting JPEG to BMP. It is used by the LSB
 * embedding class.
 * 
 * @author Xing Wei(david.wx@foxmail.com)
 * 
 */
public class BMPTool extends Activity {

	protected void writeWord(FileOutputStream stream, int value)
			throws IOException {
		byte[] b = new byte[2];
		b[0] = (byte) (value & 0xff);
		b[1] = (byte) (value >> 8 & 0xff);

		stream.write(b);
	}

	protected void writeDword(FileOutputStream stream, long value)
			throws IOException {
		byte[] b = new byte[4];
		b[0] = (byte) (value & 0xff);
		b[1] = (byte) (value >> 8 & 0xff);
		b[2] = (byte) (value >> 16 & 0xff);
		b[3] = (byte) (value >> 24 & 0xff);

		stream.write(b);
	}

	protected void writeLong(FileOutputStream stream, long value)
			throws IOException {
		byte[] b = new byte[4];
		b[0] = (byte) (value & 0xff);
		b[1] = (byte) (value >> 8 & 0xff);
		b[2] = (byte) (value >> 16 & 0xff);
		b[3] = (byte) (value >> 24 & 0xff);

		stream.write(b);
	}

	public void saveBmp(Bitmap bitmap) {
		if (bitmap == null)
			return;

		int nBmpWidth = bitmap.getWidth();
		int nBmpHeight = bitmap.getHeight();

		int bufferSize = nBmpHeight * (nBmpWidth * 3 + nBmpWidth % 4);

		try {

			// String filename = "/storage/sdcard0/encoded/" + "/temp" + ".bmp";
			String filename = MainActivity.OUTPUT_PATH + "temp.bmp";
			// String filename = "/storage/sdcard0/encoded/SaveBmpTest.bmp";

			File file = new File(filename);
			if (!file.exists()) {
				file.createNewFile();
			}
			// FileOutputStream fileos = new
			// FileOutputStream("/storage/sdcard0/encoded/"+"temp.bmp");
			FileOutputStream fileos = new FileOutputStream(
					MainActivity.OUTPUT_PATH + "temp.bmp");

			int bfType = 0x4d42;
			long bfSize = 14 + 40 + bufferSize;
			int bfReserved1 = 0;
			int bfReserved2 = 0;
			long bfOffBits = 14 + 40;

			writeWord(fileos, bfType);
			writeDword(fileos, bfSize);
			writeWord(fileos, bfReserved1);
			writeWord(fileos, bfReserved2);
			writeDword(fileos, bfOffBits);

			long biSize = 40L;
			long biWidth = nBmpWidth;
			long biHeight = nBmpHeight;
			int biPlanes = 1;
			int biBitCount = 24;
			long biCompression = 0L;
			long biSizeImage = 0L;
			long biXpelsPerMeter = 0L;
			long biYPelsPerMeter = 0L;
			long biClrUsed = 0L;
			long biClrImportant = 0L;

			writeDword(fileos, biSize);
			writeLong(fileos, biWidth);
			writeLong(fileos, biHeight);
			writeWord(fileos, biPlanes);
			writeWord(fileos, biBitCount);
			writeDword(fileos, biCompression);
			writeDword(fileos, biSizeImage);
			writeLong(fileos, biXpelsPerMeter);
			writeLong(fileos, biYPelsPerMeter);
			writeDword(fileos, biClrUsed);
			writeDword(fileos, biClrImportant);

			byte bmpData[] = new byte[bufferSize];
			int wWidth = (nBmpWidth * 3 + nBmpWidth % 4);
			for (int nCol = 0, nRealCol = nBmpHeight - 1; nCol < nBmpHeight; ++nCol, --nRealCol)
				for (int wRow = 0, wByteIdex = 0; wRow < nBmpWidth; wRow++, wByteIdex += 3) {
					int clr = bitmap.getPixel(wRow, nCol);
					bmpData[nRealCol * wWidth + wByteIdex] = (byte) Color
							.blue(clr);
					bmpData[nRealCol * wWidth + wByteIdex + 1] = (byte) Color
							.green(clr);
					bmpData[nRealCol * wWidth + wByteIdex + 2] = (byte) Color
							.red(clr);
				}

			fileos.write(bmpData);
			fileos.flush();
			fileos.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
