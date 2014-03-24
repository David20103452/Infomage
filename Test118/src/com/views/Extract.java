package com.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import crypt.F5Random;
import crypt.Permutation;
import java.io.*;

import org.apache.http.util.EncodingUtils;

import ortega.HuffmanDecode;

public class Extract {
	private static File f; // carrier file
	private static byte[] carrier; // carrier data
	private static int[] coeff; // dct values
	private static FileOutputStream fos; // embedded file (output file)
	private static String embFileName; // output file name
	private static String password;
    private static final String TAG = "text";
    public static String contentCache;

	private static byte[] deZigZag = { 0, 1, 5, 6, 14, 15, 27, 28, 2, 4, 7, 13,
			16, 26, 29, 42, 3, 8, 12, 17, 25, 30, 41, 43, 9, 11, 18, 24, 31,
			40, 44, 53, 10, 19, 23, 32, 39, 45, 52, 54, 20, 22, 33, 38, 46, 51,
			55, 60, 21, 34, 37, 47, 50, 56, 59, 61, 35, 36, 48, 49, 57, 58, 62,
			63 };

	static void usage() {
		System.out.println("java Extract [Options] \"image.jpg\"");
		System.out.println("Options:");
		System.out.println("\t-p password (default: abc123)");
		System.out.println("\t-e extractedFileName (default: output.txt)");
		System.out
				.println("\nAuthor: Andreas Westfeld, westfeld@inf.tu-dresden.de");
	}

	public static boolean extract(int[] coeffArray, String thePassword) {
//		try{
//		Bitmap image = BitmapFactory.decodeFile(MainActivity.OUTPUT_PATH + "32.jpg");
//		FileOutputStream temp = new FileOutputStream("/storage/sdcard0/encoded/temp.jpg");
//		image.compress(Bitmap.CompressFormat.JPEG, 100, temp);
//		}catch(Exception e){}
		
		
		//f=new File("/sdcard/test.jpg");
		boolean success = true;
		embFileName = MainActivity.CONFIG_PATH+"content.txt";
		password = thePassword;
		try {

			//carrier = new byte[(int) f.length()];
			//FileInputStream fis = new FileInputStream(f);
			fos = new FileOutputStream(new File(embFileName));
			//fis.read(carrier);
			//HuffmanDecode hd = new HuffmanDecode(carrier);
			//System.out.println("Huffman decoding starts");
			//Log.i(TAG,"Huffman decoding starts");
//			coeff = hd.decode();
			
			coeff = coeffArray;
			int original[] = new int[coeff.length];
            System.arraycopy(coeff, 0, original, 0, coeff.length);
            //int after[] = new int[coeff.length];
            int number = coeff.length / 64;
            for (int m = 0; m < number; m++) {
                for (int x = 0; x < 64; x++) {
                    coeff[m * 64 + deZigZag[x]] = original[m * 64 + x];
                }
            }
			
//	      try{
//			File file = new File(MainActivity.OUTPUT_PATH+"Extract2.txt");
//			if (!file.exists()) {
//				try {
//					// ��ָ�����ļ����д����ļ�
//					file.createNewFile();
//				} catch (Exception e) {
//				}
//			}
//			FileOutputStream fos = new FileOutputStream(file,true);	
//			
////			int original[] = new int[coeff.length];
////			System.arraycopy(coeff, 0, original, 0, coeff.length);
////			//int after[] = new int[coeff.length];
////			int number = coeff.length/64;
////			for(int i = 0; i < number; i++ ){
////				for(int j = 0; j<64;j++){
////					coeff[i*64+j] = original[i*64+deZigZag[j]];
////				}
////			}
//			
//	      for(int count = 0; count<(coeff.length); count++){
//	      	Integer integer = new Integer(coeff[count]);
//	      	fos.write((integer.toString()+" ").getBytes());
//	      	//Log.i(TAG,values[i]+"" );
//	      }
//	      fos.close();
//	      }catch(Exception e){
//	      	e.printStackTrace();
//	      }
			
			System.out.println("Permutation starts");
			Log.i(TAG,"Permutation starts");
			F5Random random = new F5Random(password.getBytes());
			Permutation permutation = new Permutation(coeff.length, random);
			System.out.println(coeff.length + " indices shuffled");
			int extractedByte = 0;
			int availableExtractedBits = 0;
			int extractedFileLength = 0;
			int nBytesExtracted = 0;
			int shuffledIndex = 0;
			int extractedBit;
			int i;
			System.out.println("Extraction starts");
			Log.i(TAG,"Extraction starts");
			// extract length information
			for (i = 0; availableExtractedBits < 32; i++) {
				shuffledIndex = permutation.getShuffled(i);
				if (shuffledIndex % 64 == 0)
					continue; // skip DC coefficients
				shuffledIndex = shuffledIndex - (shuffledIndex % 64)
						+ deZigZag[shuffledIndex % 64];
				if (coeff[shuffledIndex] == 0)
					continue; // skip zeroes
				if (coeff[shuffledIndex] > 0)
					extractedBit = coeff[shuffledIndex] & 1;
				else
					extractedBit = 1 - (coeff[shuffledIndex] & 1);
				extractedFileLength |= extractedBit << availableExtractedBits++;
			}
			// remove pseudo random pad
			extractedFileLength ^= random.getNextByte();
			extractedFileLength ^= random.getNextByte() << 8;
			extractedFileLength ^= random.getNextByte() << 16;
			extractedFileLength ^= random.getNextByte() << 24;
			int k = extractedFileLength >> 24;
			k %= 32;
			int n = (1 << k) - 1;
			extractedFileLength &= 0x007fffff;
			System.out.println("Length of embedded file: "
					+ extractedFileLength + " bytes");
			availableExtractedBits = 0;
			if (n > 0) {
				int startOfN = i;
				int hash;
				System.out.println("(1, " + n + ", " + k + ") code used");
				Log.i(TAG,"(1, " + n + ", " + k + ") code used");
				extractingLoop: do {
					// 1. read n places, and calculate k bits
					hash = 0;
					int code = 1;
					for (i = 0; code <= n; i++) {
						// check for pending end of coeff
						if (startOfN + i >= coeff.length)
							break extractingLoop;
						shuffledIndex = permutation.getShuffled(startOfN + i);
						if (shuffledIndex % 64 == 0)
							continue; // skip DC coefficients
						shuffledIndex = shuffledIndex - (shuffledIndex % 64)
								+ deZigZag[shuffledIndex % 64];
						if (coeff[shuffledIndex] == 0)
							continue; // skip zeroes
						if (coeff[shuffledIndex] > 0)
							extractedBit = coeff[shuffledIndex] & 1;
						else
							extractedBit = 1 - (coeff[shuffledIndex] & 1);
						if (extractedBit == 1) {
							hash ^= code;
						}
						code++;
					}
					startOfN += i;
					// 2. write k bits bytewise
					for (i = 0; i < k; i++) {
						extractedByte |= ((hash >> i) & 1) << availableExtractedBits++;
						if (availableExtractedBits == 8) {
							// remove pseudo random pad
							extractedByte ^= random.getNextByte();
							fos.write((byte) extractedByte);
							extractedByte = 0;
							availableExtractedBits = 0;
							nBytesExtracted++;
							// check for pending end of embedded data
							if (nBytesExtracted == extractedFileLength)
								break extractingLoop;
						}
					}
				} while (true);
			} else {
				System.out.println("Default code used");
				for (; i < coeff.length; i++) {
					shuffledIndex = permutation.getShuffled(i);
					if (shuffledIndex % 64 == 0)
						continue; // skip DC coefficients
					shuffledIndex = shuffledIndex - (shuffledIndex % 64)
							+ deZigZag[shuffledIndex % 64];
					if (coeff[shuffledIndex] == 0)
						continue; // skip zeroes
					if (coeff[shuffledIndex] > 0)
						extractedBit = coeff[shuffledIndex] & 1;
					else
						extractedBit = 1 - (coeff[shuffledIndex] & 1);
					extractedByte |= extractedBit << availableExtractedBits++;
					if (availableExtractedBits == 8) {
						// remove pseudo random pad
						extractedByte ^= random.getNextByte();
						fos.write((byte) extractedByte);
						extractedByte = 0;
						availableExtractedBits = 0;
						nBytesExtracted++;
						if (nBytesExtracted == extractedFileLength)
							break;
					}
				}
			}
			if (nBytesExtracted < extractedFileLength) {
				System.out.println("Incomplete file: only " + nBytesExtracted
						+ " of " + extractedFileLength + " bytes extracted");
				success = false;
			}
			//fis.close();
			fos.close();
			if(success){
			// Only for F5
    		String fileName = MainActivity.CONFIG_PATH + "content.txt";// �ļ�·��
    		String content = "";    		
    		FileInputStream fin = new FileInputStream(fileName);
    		int length = fin.available();
    		byte[] buffer = new byte[length];
    		fin.read(buffer);
    		content = EncodingUtils.getString(buffer, "GBK");// //��Y.txt�ı�������ѡ����ʵı��룬�����������    	
    		Log.i(TAG, content);
    		DecodeSuccess.content = content;
    		Log.i(TAG, DecodeSuccess.content);
    		fin.close();// �ر���Դ
			}
			
			
    			
		} catch (Exception e) {
			e.printStackTrace();
//			return false;
		}
		return success;
	}
}
