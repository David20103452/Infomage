
#if(true)
#define LOCAL_LOG
#define LOCAL_LOGD
#endif

#include <jni.h>
#include <android/bitmap.h>




#ifdef LOCAL_LOG
#include <android/log.h>
#endif


/* this function is from Android NDK bitmap-plasma , and modify for green color bug */
static uint16_t  make565(int red, int green, int blue)
{
	return (uint16_t)( ((red   << 8) & 0xf800) |
					   ((green << 3) & 0x07e0) |
					   ((blue  >> 3) & 0x001f) );
}


#include <stdio.h>

extern "C"
{
#define XMD_H
#include "../jpeg-8c/jpeglib.h"
#include "../jpeg-8c/jerror.h"
}


static	void	_JpegError(j_common_ptr cinfo)
{
	char	pszMessage[JMSG_LENGTH_MAX];

	(*cinfo->err->format_message)(cinfo,pszMessage);

	__android_log_print(ANDROID_LOG_INFO,"Test118","error!  %s",pszMessage);
}

static int DrawBitmap(AndroidBitmapInfo* pBitmapInfo, void* pPixels,const char* pszJpegFile)
{
	int			yy;
	int			nJpegLineBytes;			//JPEG1儔僀儞偺僶僀僩悢
	int test=0;
//	int array[1536];

	char*		lpbtBits;
	JSAMPLE*	pSample;
	FILE*		fp;
	JSAMPROW	buffer[1];
	JSAMPLE		tmp;

	jpeg_decompress_struct		cinfo;
	jpeg_error_mgr				jError;

	fp = fopen(pszJpegFile,"rb");
	if(fp == NULL)
		return	0;

	cinfo.err = jpeg_std_error(&jError);			//僄儔乕僴儞僪儔愝掕
	jError.error_exit = _JpegError;					//僄儔乕僴儞僪儔愝掕
	jpeg_create_decompress(&cinfo);

	jpeg_stdio_src(&cinfo, fp);

	(void) jpeg_read_header(&cinfo, TRUE);

	jvirt_barray_ptr* coeffs_array;
	coeffs_array = jpeg_read_coefficients(&cinfo);

	bool done = FALSE;
//	for (int ci = 0; ci < 3; ci++)
//	{
//	    JBLOCKARRAY buffer_one;
//	    JCOEFPTR blockptr_one;
//	    jpeg_component_info* compptr_one;
//	    compptr_one = cinfo.comp_info + ci;
//
//	    for (int by = 0; by < compptr_one->height_in_blocks; by++)
//	    {
//	        buffer_one = (cinfo.mem->access_virt_barray)((j_common_ptr)&cinfo, coeffs_array[ci], by, (JDIMENSION)1, FALSE);
//
//	        for (int bx = 0; bx < compptr_one->width_in_blocks; bx++)
//	        {
//	            blockptr_one = buffer_one[0][bx];
//
//	            for (int bi = 0; bi < 64; bi++)
//	            {
////	                blockptr_one[bi]++;
//	            	array[test] = blockptr_one[bi];
//	                test++;
//	                __android_log_print(ANDROID_LOG_INFO, "JNIMsg", "n=%d",blockptr_one[bi]);
//	            }
//	        }
//	    }
//	}

	//write_jpeg(output, &cinfo, coeffs_array); // saving modified JPEG to the output file

	jpeg_destroy_decompress(&cinfo);
	fclose(fp);
	__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "n=%d",test);
	return	1;
}




extern "C"
JNIEXPORT jintArray JNICALL Java_com_Test118_NativeView_RenderBitmap(JNIEnv * env, jobject  obj, jobject bitmap, jintArray arr, jstring imagePath, jint num)
{
//	AndroidBitmapInfo  info;
//	void*			  pixels;
//	int				ret;
//
//	if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0)
//		return;
//
//	if (info.format != ANDROID_BITMAP_FORMAT_RGB_565)
//		return;
//
//	if ((ret = AndroidBitmap_lockPixels(env, bitmap, &pixels)) < 0)
//		return;

//	jintArray array=(*env)->NewIntArray(env,1536);
//	(*env)->SetIntArrayRegion(env,array,0,1536,body);
//	DrawBitmap(&info,pixels,"/sdcard/encoded.jpg");

	int			yy;
	int			nJpegLineBytes;			//JPEG1儔僀儞偺僶僀僩悢
	int test=0;
	int array[num];
	__android_log_print(ANDROID_LOG_INFO, "num", "number=%d",num);
	jsize len = env->GetArrayLength(arr);//返还这个数组的长度..
	jintArray newArray = env->NewIntArray(len);
	jint* intarr = env->GetIntArrayElements(arr,0);
	for(int a = 0; a <num; a++){
		__android_log_print(ANDROID_LOG_INFO, "test", "n=%d",intarr[a]);
	}


	char*		lpbtBits;
	JSAMPLE*	pSample;
	FILE*		fp;
	JSAMPROW	buffer[1];
	JSAMPLE		tmp;
	jpeg_decompress_struct		cinfo;
		jpeg_error_mgr				jError;
		const char* pszJpegFile = env->GetStringUTFChars(imagePath, false);

		fp = fopen(pszJpegFile,"rb");
		if(fp == NULL)
			return	arr;

		cinfo.err = jpeg_std_error(&jError);			//僄儔乕僴儞僪儔愝掕
		jError.error_exit = _JpegError;					//僄儔乕僴儞僪儔愝掕
		jpeg_create_decompress(&cinfo);

		jpeg_stdio_src(&cinfo, fp);
//		jcopy_markers_setup(&cinfo, JCOPYOPT_ALL);
		(void) jpeg_read_header(&cinfo, TRUE);

		jvirt_barray_ptr* coeffs_array;
		coeffs_array = jpeg_read_coefficients(&cinfo);

		bool done = FALSE;
		int count = 0;
		for (int ci = 0; ci < 3; ci++)
		{
		    JBLOCKARRAY buffer_one;
		    JCOEFPTR blockptr_one;
		    jpeg_component_info* compptr_one;
		    compptr_one = cinfo.comp_info + ci;

		    for (int by = 0; by < compptr_one->height_in_blocks; by++)
		    {
		        buffer_one = (cinfo.mem->access_virt_barray)((j_common_ptr)&cinfo, coeffs_array[ci], by, (JDIMENSION)1, FALSE);

		        for (int bx = 0; bx < compptr_one->width_in_blocks; bx++)
		        {
		            blockptr_one = buffer_one[0][bx];

		            for (int bi = 0; bi < 64; bi++)
		            {
//		            	if(by == 0 && bx == 0 && bi ==2)
//		                blockptr_one[bi] = -17;

//		            	blockptr_one[bi] = intarr[count];
		            	intarr[test] = blockptr_one[bi];
		            	array[test] = blockptr_one[bi];
		                test++;
		                count++;
		                __android_log_print(ANDROID_LOG_INFO, "JNIMsg", "n=%d",blockptr_one[bi]);
		                __android_log_print(ANDROID_LOG_INFO, "JNIMsg", "intarr=%d",intarr[test-1]);
		                __android_log_print(ANDROID_LOG_INFO, "JNIMsg", "array=%d",array[test-1]);
		            }
		        }
		    }

		}
		//fclose(fp);

		//saving modified JPEG to the output file
//		struct jpeg_compress_struct jcs;
//		struct jpeg_error_mgr jem;
//		jcs.err = jpeg_std_error(&jem);
//		jpeg_create_compress(&jcs);
//		jpeg_copy_critical_parameters(&cinfo, &jcs);
//
//		FILE* f;
//		f=fopen("/sdcard/test.jpg","wb");
//		jpeg_stdio_dest(&jcs, f);
//		jpeg_write_coefficients(&jcs,coeffs_array);
//		jpeg_finish_compress(&jcs);
//		jpeg_destroy_compress(&jcs);

//		env->SetIntArrayRegion(newArray,0,len,intarr);
		env->SetIntArrayRegion(newArray,0,len,array);

//		(void) jpeg_finish_decompress(&cinfo);
		jpeg_destroy_decompress(&cinfo);
		fclose(fp);
		__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "n=%d",test);
		__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "arr=%d",arr[0]);
		__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "intarr=%d",intarr[0]);
		__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "newArray=%d",newArray[0]);


	AndroidBitmap_unlockPixels(env, bitmap);
	return newArray;


}

extern "C"
JNIEXPORT jintArray JNICALL Java_com_views_InputViaText_RenderBitmap(JNIEnv * env, jobject  obj, jobject bitmap, jintArray arr, jstring imagePath, jint num)
{
//	AndroidBitmapInfo  info;
//	void*			  pixels;
//	int				ret;
//
//	if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0)
//		return;
//
//	if (info.format != ANDROID_BITMAP_FORMAT_RGB_565)
//		return;
//
//	if ((ret = AndroidBitmap_lockPixels(env, bitmap, &pixels)) < 0)
//		return;

//	jintArray array=(*env)->NewIntArray(env,1536);
//	(*env)->SetIntArrayRegion(env,array,0,1536,body);
//	DrawBitmap(&info,pixels,"/sdcard/encoded.jpg");

	int			yy;
	int			nJpegLineBytes;			//JPEG1儔僀儞偺僶僀僩悢
	int test=0;
	int array[num];
	__android_log_print(ANDROID_LOG_INFO, "num", "number=%d",num);
	jsize len = env->GetArrayLength(arr);//返还这个数组的长度..
	jintArray newArray = env->NewIntArray(len);
	jint* intarr = env->GetIntArrayElements(arr,0);
//	for(int a = 0; a <num; a++){
//		__android_log_print(ANDROID_LOG_INFO, "test", "n=%d",intarr[a]);
//	}


	char*		lpbtBits;
	JSAMPLE*	pSample;
	FILE*		fp;
	JSAMPROW	buffer[1];
	JSAMPLE		tmp;
	jpeg_decompress_struct		cinfo;
		jpeg_error_mgr				jError;
		const char* pszJpegFile = env->GetStringUTFChars(imagePath, false);

		fp = fopen(pszJpegFile,"rb");
		if(fp == NULL)
			return	arr;

		cinfo.err = jpeg_std_error(&jError);			//僄儔乕僴儞僪儔愝掕
		jError.error_exit = _JpegError;					//僄儔乕僴儞僪儔愝掕
		jpeg_create_decompress(&cinfo);

		jpeg_stdio_src(&cinfo, fp);
//		jcopy_markers_setup(&cinfo, JCOPYOPT_ALL);
		(void) jpeg_read_header(&cinfo, TRUE);

		jvirt_barray_ptr* coeffs_array;
		coeffs_array = jpeg_read_coefficients(&cinfo);

		bool done = FALSE;
		int count = 0;
		for (int ci = 0; ci < 3; ci++)
		{
		    JBLOCKARRAY buffer_one;
		    JCOEFPTR blockptr_one;
		    jpeg_component_info* compptr_one;
		    compptr_one = cinfo.comp_info + ci;

		    for (int by = 0; by < compptr_one->height_in_blocks; by++)
		    {
		        buffer_one = (cinfo.mem->access_virt_barray)((j_common_ptr)&cinfo, coeffs_array[ci], by, (JDIMENSION)1, FALSE);

		        for (int bx = 0; bx < compptr_one->width_in_blocks; bx++)
		        {
		            blockptr_one = buffer_one[0][bx];

		            for (int bi = 0; bi < 64; bi++)
		            {
//		            	if(by == 0 && bx == 0 && bi ==2)
//		                blockptr_one[bi] = -17;

//		            	blockptr_one[bi] = intarr[count];
		            	intarr[test] = blockptr_one[bi];
		            	array[test] = blockptr_one[bi];
		                test++;
		                count++;
		                __android_log_print(ANDROID_LOG_INFO, "JNIMsg", "test=%d",test);
		                __android_log_print(ANDROID_LOG_INFO, "JNIMsg", "n=%d",blockptr_one[bi]);
		                __android_log_print(ANDROID_LOG_INFO, "JNIMsg", "intarr=%d",intarr[test-1]);
		                __android_log_print(ANDROID_LOG_INFO, "JNIMsg", "array=%d",array[test-1]);
		            }
		        }
		    }

		}
		//fclose(fp);

		//saving modified JPEG to the output file
//		struct jpeg_compress_struct jcs;
//		struct jpeg_error_mgr jem;
//		jcs.err = jpeg_std_error(&jem);
//		jpeg_create_compress(&jcs);
//		jpeg_copy_critical_parameters(&cinfo, &jcs);
//
//		FILE* f;
//		f=fopen("/sdcard/test.jpg","wb");
//		jpeg_stdio_dest(&jcs, f);
//		jpeg_write_coefficients(&jcs,coeffs_array);
//		jpeg_finish_compress(&jcs);
//		jpeg_destroy_compress(&jcs);

//		env->SetIntArrayRegion(newArray,0,len,intarr);
		env->SetIntArrayRegion(newArray,0,len,array);

//		(void) jpeg_finish_decompress(&cinfo);
		jpeg_destroy_decompress(&cinfo);
		fclose(fp);
		__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "n=%d",test);
		__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "arr=%d",arr[0]);
		__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "intarr=%d",intarr[0]);
		__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "newArray=%d",newArray[0]);


	AndroidBitmap_unlockPixels(env, bitmap);
	return newArray;


}

extern "C"
JNIEXPORT void JNICALL Java_com_views_InputViaText_createImage(JNIEnv * env, jobject  obj, jintArray arr, jstring imagePath, jstring outputPath, jint num)
{
	int			yy;
	int			nJpegLineBytes;			//JPEG1儔僀儞偺僶僀僩悢
	int test=0;
	int array[num];
	jsize len = env->GetArrayLength(arr);//返还这个数组的长度..
	jintArray newArray = env->NewIntArray(len);
	jint* intarr = env->GetIntArrayElements(arr,0);
	for(int a = 0; a <num; a++){
		__android_log_print(ANDROID_LOG_INFO, "test", "n=%d",intarr[a]);
	}


	char*		lpbtBits;
	JSAMPLE*	pSample;
	FILE*		fp;
	JSAMPROW	buffer[1];
	JSAMPLE		tmp;
	jpeg_decompress_struct		cinfo;
		jpeg_error_mgr				jError;
		const char* pszJpegFile = env->GetStringUTFChars(imagePath, false);

		fp = fopen(pszJpegFile,"rb");
		if(fp == NULL)
			return;

		cinfo.err = jpeg_std_error(&jError);			//僄儔乕僴儞僪儔愝掕
		jError.error_exit = _JpegError;					//僄儔乕僴儞僪儔愝掕
		jpeg_create_decompress(&cinfo);

		jpeg_stdio_src(&cinfo, fp);
//		jcopy_markers_setup(&cinfo, JCOPYOPT_ALL);
		(void) jpeg_read_header(&cinfo, TRUE);

		jvirt_barray_ptr* coeffs_array;
		coeffs_array = jpeg_read_coefficients(&cinfo);

		bool done = FALSE;
		int count = 0;
		for (int ci = 0; ci < 3; ci++)
		{
		    JBLOCKARRAY buffer_one;
		    JCOEFPTR blockptr_one;
		    jpeg_component_info* compptr_one;
		    compptr_one = cinfo.comp_info + ci;

		    for (int by = 0; by < compptr_one->height_in_blocks; by++)
		    {
		        buffer_one = (cinfo.mem->access_virt_barray)((j_common_ptr)&cinfo, coeffs_array[ci], by, (JDIMENSION)1, FALSE);

		        for (int bx = 0; bx < compptr_one->width_in_blocks; bx++)
		        {
		            blockptr_one = buffer_one[0][bx];

		            for (int bi = 0; bi < 64; bi++)
		            {
//		            	if(by == 0 && bx == 0 && bi ==2)
//		                blockptr_one[bi] = -17;

		            	blockptr_one[bi] = intarr[count];
		            	intarr[test] = blockptr_one[bi];
		            	array[test] = blockptr_one[bi];
		                test++;
		                count++;
		                __android_log_print(ANDROID_LOG_INFO, "JNIMsg", "n=%d",blockptr_one[bi]);
		                __android_log_print(ANDROID_LOG_INFO, "JNIMsg", "intarr=%d",intarr[test-1]);
//		                __android_log_print(ANDROID_LOG_INFO, "JNIMsg", "array=%d",array[test-1]);
		            }
		        }
		    }

		}
		//fclose(fp);

		//write_jpeg(output, &cinfo, coeffs_array); // saving modified JPEG to the output file
		struct jpeg_compress_struct jcs;
		struct jpeg_error_mgr jem;
		jcs.err = jpeg_std_error(&jem);
		jpeg_create_compress(&jcs);
		jpeg_copy_critical_parameters(&cinfo, &jcs);

		FILE* f;
		f=fopen(env->GetStringUTFChars(outputPath, false),"wb");
		//f=fopen("/sdcard/test.jpg","wb");

//		struct jpeg_transform_info transformoption;
//		jtransform_request_workspace(&cinfo, &transformoption);
//		coeffs_array = jtransform_adjust_parameters(&cinfo,
//		                                                 &jcs,
//		                                                 coeffs_array,
//		                                                 &transformoption)
		jpeg_stdio_dest(&jcs, f);
		jpeg_write_coefficients(&jcs,coeffs_array);
//		jcopy_markers_execute(&srcinfo, &dstinfo, JCOPYOPT_ALL);
		jpeg_finish_compress(&jcs);
		jpeg_destroy_compress(&jcs);

//		env->SetIntArrayRegion(newArray,0,len,intarr);
		env->SetIntArrayRegion(newArray,0,len,array);

//		(void) jpeg_finish_decompress(&cinfo);
		jpeg_destroy_decompress(&cinfo);
		fclose(fp);
		__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "n=%d",test);
		__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "arr=%d",arr[0]);
		__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "intarr=%d",intarr[0]);
		__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "newArray=%d",newArray[0]);
		//__android_log_print(ANDROID_LOG_INFO, "path", env->GetStringUTFChars(imagePath, false));


//	AndroidBitmap_unlockPixels(env, bitmap);
//	return newArray;
}

extern "C"
JNIEXPORT jintArray JNICALL Java_com_views_InputViaFile_RenderBitmap(JNIEnv * env, jobject  obj, jobject bitmap, jintArray arr, jstring imagePath, jint num)
{
//	AndroidBitmapInfo  info;
//	void*			  pixels;
//	int				ret;
//
//	if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0)
//		return;
//
//	if (info.format != ANDROID_BITMAP_FORMAT_RGB_565)
//		return;
//
//	if ((ret = AndroidBitmap_lockPixels(env, bitmap, &pixels)) < 0)
//		return;

//	jintArray array=(*env)->NewIntArray(env,1536);
//	(*env)->SetIntArrayRegion(env,array,0,1536,body);
//	DrawBitmap(&info,pixels,"/sdcard/encoded.jpg");

	int			yy;
	int			nJpegLineBytes;			//JPEG1儔僀儞偺僶僀僩悢
	int test=0;
	int array[num];
	__android_log_print(ANDROID_LOG_INFO, "num", "number=%d",num);
	jsize len = env->GetArrayLength(arr);//返还这个数组的长度..
	jintArray newArray = env->NewIntArray(len);
	jint* intarr = env->GetIntArrayElements(arr,0);
	for(int a = 0; a <num; a++){
		__android_log_print(ANDROID_LOG_INFO, "test", "n=%d",intarr[a]);
	}


	char*		lpbtBits;
	JSAMPLE*	pSample;
	FILE*		fp;
	JSAMPROW	buffer[1];
	JSAMPLE		tmp;
	jpeg_decompress_struct		cinfo;
		jpeg_error_mgr				jError;
		const char* pszJpegFile = env->GetStringUTFChars(imagePath, false);

		fp = fopen(pszJpegFile,"rb");
		if(fp == NULL)
			return	arr;

		cinfo.err = jpeg_std_error(&jError);			//僄儔乕僴儞僪儔愝掕
		jError.error_exit = _JpegError;					//僄儔乕僴儞僪儔愝掕
		jpeg_create_decompress(&cinfo);

		jpeg_stdio_src(&cinfo, fp);
//		jcopy_markers_setup(&cinfo, JCOPYOPT_ALL);
		(void) jpeg_read_header(&cinfo, TRUE);

		jvirt_barray_ptr* coeffs_array;
		coeffs_array = jpeg_read_coefficients(&cinfo);

		bool done = FALSE;
		int count = 0;
		for (int ci = 0; ci < 3; ci++)
		{
		    JBLOCKARRAY buffer_one;
		    JCOEFPTR blockptr_one;
		    jpeg_component_info* compptr_one;
		    compptr_one = cinfo.comp_info + ci;

		    for (int by = 0; by < compptr_one->height_in_blocks; by++)
		    {
		        buffer_one = (cinfo.mem->access_virt_barray)((j_common_ptr)&cinfo, coeffs_array[ci], by, (JDIMENSION)1, FALSE);

		        for (int bx = 0; bx < compptr_one->width_in_blocks; bx++)
		        {
		            blockptr_one = buffer_one[0][bx];

		            for (int bi = 0; bi < 64; bi++)
		            {
//		            	if(by == 0 && bx == 0 && bi ==2)
//		                blockptr_one[bi] = -17;

//		            	blockptr_one[bi] = intarr[count];
		            	intarr[test] = blockptr_one[bi];
		            	array[test] = blockptr_one[bi];
		                test++;
		                count++;
		                __android_log_print(ANDROID_LOG_INFO, "JNIMsg", "n=%d",blockptr_one[bi]);
		                __android_log_print(ANDROID_LOG_INFO, "JNIMsg", "intarr=%d",intarr[test-1]);
		                __android_log_print(ANDROID_LOG_INFO, "JNIMsg", "array=%d",array[test-1]);
		            }
		        }
		    }

		}
		//fclose(fp);

		//saving modified JPEG to the output file
//		struct jpeg_compress_struct jcs;
//		struct jpeg_error_mgr jem;
//		jcs.err = jpeg_std_error(&jem);
//		jpeg_create_compress(&jcs);
//		jpeg_copy_critical_parameters(&cinfo, &jcs);
//
//		FILE* f;
//		f=fopen("/sdcard/test.jpg","wb");
//		jpeg_stdio_dest(&jcs, f);
//		jpeg_write_coefficients(&jcs,coeffs_array);
//		jpeg_finish_compress(&jcs);
//		jpeg_destroy_compress(&jcs);

//		env->SetIntArrayRegion(newArray,0,len,intarr);
		env->SetIntArrayRegion(newArray,0,len,array);

//		(void) jpeg_finish_decompress(&cinfo);
		jpeg_destroy_decompress(&cinfo);
		fclose(fp);
		__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "n=%d",test);
		__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "arr=%d",arr[0]);
		__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "intarr=%d",intarr[0]);
		__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "newArray=%d",newArray[0]);


	AndroidBitmap_unlockPixels(env, bitmap);
	return newArray;


}

extern "C"
JNIEXPORT void JNICALL Java_com_views_InputViaFile_createImage(JNIEnv * env, jobject  obj, jintArray arr, jstring imagePath, jstring outputPath, jint num)
{
	int			yy;
	int			nJpegLineBytes;			//JPEG1儔僀儞偺僶僀僩悢
	int test=0;
	int array[num];
	jsize len = env->GetArrayLength(arr);//返还这个数组的长度..
	jintArray newArray = env->NewIntArray(len);
	jint* intarr = env->GetIntArrayElements(arr,0);
	for(int a = 0; a <num; a++){
		__android_log_print(ANDROID_LOG_INFO, "test", "n=%d",intarr[a]);
	}


	char*		lpbtBits;
	JSAMPLE*	pSample;
	FILE*		fp;
	JSAMPROW	buffer[1];
	JSAMPLE		tmp;
	jpeg_decompress_struct		cinfo;
		jpeg_error_mgr				jError;
		const char* pszJpegFile = env->GetStringUTFChars(imagePath, false);

		fp = fopen(pszJpegFile,"rb");
		if(fp == NULL)
			return;

		cinfo.err = jpeg_std_error(&jError);			//僄儔乕僴儞僪儔愝掕
		jError.error_exit = _JpegError;					//僄儔乕僴儞僪儔愝掕
		jpeg_create_decompress(&cinfo);

		jpeg_stdio_src(&cinfo, fp);
//		jcopy_markers_setup(&cinfo, JCOPYOPT_ALL);
		(void) jpeg_read_header(&cinfo, TRUE);

		jvirt_barray_ptr* coeffs_array;
		coeffs_array = jpeg_read_coefficients(&cinfo);

		bool done = FALSE;
		int count = 0;
		for (int ci = 0; ci < 3; ci++)
		{
		    JBLOCKARRAY buffer_one;
		    JCOEFPTR blockptr_one;
		    jpeg_component_info* compptr_one;
		    compptr_one = cinfo.comp_info + ci;

		    for (int by = 0; by < compptr_one->height_in_blocks; by++)
		    {
		        buffer_one = (cinfo.mem->access_virt_barray)((j_common_ptr)&cinfo, coeffs_array[ci], by, (JDIMENSION)1, FALSE);

		        for (int bx = 0; bx < compptr_one->width_in_blocks; bx++)
		        {
		            blockptr_one = buffer_one[0][bx];

		            for (int bi = 0; bi < 64; bi++)
		            {
//		            	if(by == 0 && bx == 0 && bi ==2)
//		                blockptr_one[bi] = -17;

		            	blockptr_one[bi] = intarr[count];
		            	intarr[test] = blockptr_one[bi];
		            	array[test] = blockptr_one[bi];
		                test++;
		                count++;
		                __android_log_print(ANDROID_LOG_INFO, "JNIMsg", "n=%d",blockptr_one[bi]);
		                __android_log_print(ANDROID_LOG_INFO, "JNIMsg", "intarr=%d",intarr[test-1]);
//		                __android_log_print(ANDROID_LOG_INFO, "JNIMsg", "array=%d",array[test-1]);
		            }
		        }
		    }

		}
		//fclose(fp);

		//write_jpeg(output, &cinfo, coeffs_array); // saving modified JPEG to the output file
		struct jpeg_compress_struct jcs;
		struct jpeg_error_mgr jem;
		jcs.err = jpeg_std_error(&jem);
		jpeg_create_compress(&jcs);
		jpeg_copy_critical_parameters(&cinfo, &jcs);

		FILE* f;
		f=fopen(env->GetStringUTFChars(outputPath, false),"wb");
		//f=fopen("/sdcard/test.jpg","wb");

//		struct jpeg_transform_info transformoption;
//		jtransform_request_workspace(&cinfo, &transformoption);
//		coeffs_array = jtransform_adjust_parameters(&cinfo,
//		                                                 &jcs,
//		                                                 coeffs_array,
//		                                                 &transformoption)
		jpeg_stdio_dest(&jcs, f);
		jpeg_write_coefficients(&jcs,coeffs_array);
//		jcopy_markers_execute(&srcinfo, &dstinfo, JCOPYOPT_ALL);
		jpeg_finish_compress(&jcs);
		jpeg_destroy_compress(&jcs);

//		env->SetIntArrayRegion(newArray,0,len,intarr);
		env->SetIntArrayRegion(newArray,0,len,array);

//		(void) jpeg_finish_decompress(&cinfo);
		jpeg_destroy_decompress(&cinfo);
		fclose(fp);
		__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "n=%d",test);
		__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "arr=%d",arr[0]);
		__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "intarr=%d",intarr[0]);
		__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "newArray=%d",newArray[0]);
		//__android_log_print(ANDROID_LOG_INFO, "path", env->GetStringUTFChars(imagePath, false));


//	AndroidBitmap_unlockPixels(env, bitmap);
//	return newArray;
}

extern "C"
JNIEXPORT void JNICALL Java_com_Test118_NativeView_createImage(JNIEnv * env, jobject  obj, jintArray arr, jstring imagePath, jstring outputPath, jint num)
{
	int			yy;
	int			nJpegLineBytes;			//JPEG1儔僀儞偺僶僀僩悢
	int test=0;
	int array[num];
	jsize len = env->GetArrayLength(arr);//返还这个数组的长度..
	jintArray newArray = env->NewIntArray(len);
	jint* intarr = env->GetIntArrayElements(arr,0);
	for(int a = 0; a <num; a++){
		__android_log_print(ANDROID_LOG_INFO, "test", "n=%d",intarr[a]);
	}


	char*		lpbtBits;
	JSAMPLE*	pSample;
	FILE*		fp;
	JSAMPROW	buffer[1];
	JSAMPLE		tmp;
	jpeg_decompress_struct		cinfo;
		jpeg_error_mgr				jError;
		const char* pszJpegFile = env->GetStringUTFChars(imagePath, false);

		fp = fopen(pszJpegFile,"rb");
		if(fp == NULL)
			return;

		cinfo.err = jpeg_std_error(&jError);			//僄儔乕僴儞僪儔愝掕
		jError.error_exit = _JpegError;					//僄儔乕僴儞僪儔愝掕
		jpeg_create_decompress(&cinfo);

		jpeg_stdio_src(&cinfo, fp);
//		jcopy_markers_setup(&cinfo, JCOPYOPT_ALL);
		(void) jpeg_read_header(&cinfo, TRUE);

		jvirt_barray_ptr* coeffs_array;
		coeffs_array = jpeg_read_coefficients(&cinfo);

		bool done = FALSE;
		int count = 0;
		for (int ci = 0; ci < 3; ci++)
		{
		    JBLOCKARRAY buffer_one;
		    JCOEFPTR blockptr_one;
		    jpeg_component_info* compptr_one;
		    compptr_one = cinfo.comp_info + ci;

		    for (int by = 0; by < compptr_one->height_in_blocks; by++)
		    {
		        buffer_one = (cinfo.mem->access_virt_barray)((j_common_ptr)&cinfo, coeffs_array[ci], by, (JDIMENSION)1, FALSE);

		        for (int bx = 0; bx < compptr_one->width_in_blocks; bx++)
		        {
		            blockptr_one = buffer_one[0][bx];

		            for (int bi = 0; bi < 64; bi++)
		            {
//		            	if(by == 0 && bx == 0 && bi ==2)
//		                blockptr_one[bi] = -17;

		            	blockptr_one[bi] = intarr[count];
		            	intarr[test] = blockptr_one[bi];
		            	array[test] = blockptr_one[bi];
		                test++;
		                count++;
		                __android_log_print(ANDROID_LOG_INFO, "JNIMsg", "n=%d",blockptr_one[bi]);
		                __android_log_print(ANDROID_LOG_INFO, "JNIMsg", "intarr=%d",intarr[test-1]);
//		                __android_log_print(ANDROID_LOG_INFO, "JNIMsg", "array=%d",array[test-1]);
		            }
		        }
		    }

		}
		//fclose(fp);

		//write_jpeg(output, &cinfo, coeffs_array); // saving modified JPEG to the output file
		struct jpeg_compress_struct jcs;
		struct jpeg_error_mgr jem;
		jcs.err = jpeg_std_error(&jem);
		jpeg_create_compress(&jcs);
		jpeg_copy_critical_parameters(&cinfo, &jcs);

		FILE* f;
		f=fopen(env->GetStringUTFChars(outputPath, false),"wb");
		//f=fopen("/sdcard/test.jpg","wb");

//		struct jpeg_transform_info transformoption;
//		jtransform_request_workspace(&cinfo, &transformoption);
//		coeffs_array = jtransform_adjust_parameters(&cinfo,
//		                                                 &jcs,
//		                                                 coeffs_array,
//		                                                 &transformoption)
		jpeg_stdio_dest(&jcs, f);
		jpeg_write_coefficients(&jcs,coeffs_array);
//		jcopy_markers_execute(&srcinfo, &dstinfo, JCOPYOPT_ALL);
		jpeg_finish_compress(&jcs);
		jpeg_destroy_compress(&jcs);

//		env->SetIntArrayRegion(newArray,0,len,intarr);
		env->SetIntArrayRegion(newArray,0,len,array);

//		(void) jpeg_finish_decompress(&cinfo);
		jpeg_destroy_decompress(&cinfo);
		fclose(fp);
		__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "n=%d",test);
		__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "arr=%d",arr[0]);
		__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "intarr=%d",intarr[0]);
		__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "newArray=%d",newArray[0]);
		//__android_log_print(ANDROID_LOG_INFO, "path", env->GetStringUTFChars(imagePath, false));


//	AndroidBitmap_unlockPixels(env, bitmap);
//	return newArray;
}

extern "C"
JNIEXPORT jintArray JNICALL Java_com_Test118_NativeView_decodeEmbededImage(JNIEnv * env, jobject  obj, jintArray arr, jstring imagePath, jint num)
{
	int			yy;
		int			nJpegLineBytes;			//JPEG1儔僀儞偺僶僀僩悢
		int test=0;
		int array[num];
		jsize len = env->GetArrayLength(arr);//返还这个数组的长度..
		jintArray newArray = env->NewIntArray(len);
		jint* intarr = env->GetIntArrayElements(arr,0);

//		for(int a = 0; a <1536; a++){
//			__android_log_print(ANDROID_LOG_INFO, "test", "n=%d",intarr[a]);
//		}


		char*		lpbtBits;
		JSAMPLE*	pSample;
		FILE*		fp;
		JSAMPROW	buffer[1];
		JSAMPLE		tmp;
		jpeg_decompress_struct		cinfo;
			jpeg_error_mgr				jError;
			const char* pszJpegFile = env->GetStringUTFChars(imagePath, false);
			// __android_log_print(ANDROID_LOG_INFO, "imagePath", pszJpegFile);

			fp = fopen(pszJpegFile,"rb");
			if(fp == NULL)
				return	arr;

			cinfo.err = jpeg_std_error(&jError);			//僄儔乕僴儞僪儔愝掕
			jError.error_exit = _JpegError;					//僄儔乕僴儞僪儔愝掕
			jpeg_create_decompress(&cinfo);

			jpeg_stdio_src(&cinfo, fp);
	//		jcopy_markers_setup(&cinfo, JCOPYOPT_ALL);
			(void) jpeg_read_header(&cinfo, TRUE);

			jvirt_barray_ptr* coeffs_array;
			coeffs_array = jpeg_read_coefficients(&cinfo);

			bool done = FALSE;
			int count = 0;
			for (int ci = 0; ci < 3; ci++)
			{
			    JBLOCKARRAY buffer_one;
			    JCOEFPTR blockptr_one;
			    jpeg_component_info* compptr_one;
			    compptr_one = cinfo.comp_info + ci;

			    for (int by = 0; by < compptr_one->height_in_blocks; by++)
			    {
			        buffer_one = (cinfo.mem->access_virt_barray)((j_common_ptr)&cinfo, coeffs_array[ci], by, (JDIMENSION)1, FALSE);

			        for (int bx = 0; bx < compptr_one->width_in_blocks; bx++)
			        {
			            blockptr_one = buffer_one[0][bx];

			            for (int bi = 0; bi < 64; bi++)
			            {
	//		            	if(by == 0 && bx == 0 && bi ==2)
	//		                blockptr_one[bi] = -17;

	//		            	blockptr_one[bi] = intarr[count];
			            	intarr[test] = blockptr_one[bi];
			            	array[test] = blockptr_one[bi];
			                test++;
			                count++;
			                __android_log_print(ANDROID_LOG_INFO, "JNIMsg", "n=%d",blockptr_one[bi]);
			                __android_log_print(ANDROID_LOG_INFO, "JNIMsg", "intarr=%d",intarr[test-1]);
			                __android_log_print(ANDROID_LOG_INFO, "JNIMsg", "array=%d",array[test-1]);
			            }
			        }
			    }

			}
			//fclose(fp);

			//write_jpeg(output, &cinfo, coeffs_array); // saving modified JPEG to the output file
//			struct jpeg_compress_struct jcs;
//			struct jpeg_error_mgr jem;
//			jcs.err = jpeg_std_error(&jem);
//			jpeg_create_compress(&jcs);
//			jpeg_copy_critical_parameters(&cinfo, &jcs);
//
//			FILE* f;
//			f=fopen("/sdcard/test.jpg","wb");
//			jpeg_stdio_dest(&jcs, f);
//			jpeg_write_coefficients(&jcs,coeffs_array);
//			jpeg_finish_compress(&jcs);
//			jpeg_destroy_compress(&jcs);

	//		env->SetIntArrayRegion(newArray,0,len,intarr);
			env->SetIntArrayRegion(newArray,0,len,array);

	//		(void) jpeg_finish_decompress(&cinfo);
			jpeg_destroy_decompress(&cinfo);
			fclose(fp);
			__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "n=%d",test);
			__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "arr=%d",arr[0]);
			__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "intarr=%d",intarr[0]);
			__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "newArray=%d",newArray[0]);


//		AndroidBitmap_unlockPixels(env, bitmap);
		return newArray;

}

extern "C"
JNIEXPORT jintArray JNICALL Java_com_views_DecodePasswordInput_decodeEmbededImage(JNIEnv * env, jobject  obj, jintArray arr, jstring imagePath, jint num)
{
	int			yy;
		int			nJpegLineBytes;			//JPEG1儔僀儞偺僶僀僩悢
		int test=0;
		int array[num];
		jsize len = env->GetArrayLength(arr);//返还这个数组的长度..
		jintArray newArray = env->NewIntArray(len);
		jint* intarr = env->GetIntArrayElements(arr,0);

//		for(int a = 0; a <1536; a++){
//			__android_log_print(ANDROID_LOG_INFO, "test", "n=%d",intarr[a]);
//		}


		char*		lpbtBits;
		JSAMPLE*	pSample;
		FILE*		fp;
		JSAMPROW	buffer[1];
		JSAMPLE		tmp;
		jpeg_decompress_struct		cinfo;
			jpeg_error_mgr				jError;
			const char* pszJpegFile = env->GetStringUTFChars(imagePath, false);
			// __android_log_print(ANDROID_LOG_INFO, "imagePath", pszJpegFile);

			fp = fopen(pszJpegFile,"rb");
			if(fp == NULL)
				return	arr;

			cinfo.err = jpeg_std_error(&jError);			//僄儔乕僴儞僪儔愝掕
			jError.error_exit = _JpegError;					//僄儔乕僴儞僪儔愝掕
			jpeg_create_decompress(&cinfo);

			jpeg_stdio_src(&cinfo, fp);
	//		jcopy_markers_setup(&cinfo, JCOPYOPT_ALL);
			(void) jpeg_read_header(&cinfo, TRUE);

			jvirt_barray_ptr* coeffs_array;
			coeffs_array = jpeg_read_coefficients(&cinfo);

			bool done = FALSE;
			int count = 0;
			for (int ci = 0; ci < 3; ci++)
			{
			    JBLOCKARRAY buffer_one;
			    JCOEFPTR blockptr_one;
			    jpeg_component_info* compptr_one;
			    compptr_one = cinfo.comp_info + ci;

			    for (int by = 0; by < compptr_one->height_in_blocks; by++)
			    {
			        buffer_one = (cinfo.mem->access_virt_barray)((j_common_ptr)&cinfo, coeffs_array[ci], by, (JDIMENSION)1, FALSE);

			        for (int bx = 0; bx < compptr_one->width_in_blocks; bx++)
			        {
			            blockptr_one = buffer_one[0][bx];

			            for (int bi = 0; bi < 64; bi++)
			            {
	//		            	if(by == 0 && bx == 0 && bi ==2)
	//		                blockptr_one[bi] = -17;

	//		            	blockptr_one[bi] = intarr[count];
			            	intarr[test] = blockptr_one[bi];
			            	array[test] = blockptr_one[bi];
			                test++;
			                count++;
			                __android_log_print(ANDROID_LOG_INFO, "JNIMsg", "n=%d",blockptr_one[bi]);
			                __android_log_print(ANDROID_LOG_INFO, "JNIMsg", "intarr=%d",intarr[test-1]);
			                __android_log_print(ANDROID_LOG_INFO, "JNIMsg", "array=%d",array[test-1]);
			            }
			        }
			    }

			}
			//fclose(fp);

			//write_jpeg(output, &cinfo, coeffs_array); // saving modified JPEG to the output file
//			struct jpeg_compress_struct jcs;
//			struct jpeg_error_mgr jem;
//			jcs.err = jpeg_std_error(&jem);
//			jpeg_create_compress(&jcs);
//			jpeg_copy_critical_parameters(&cinfo, &jcs);
//
//			FILE* f;
//			f=fopen("/sdcard/test.jpg","wb");
//			jpeg_stdio_dest(&jcs, f);
//			jpeg_write_coefficients(&jcs,coeffs_array);
//			jpeg_finish_compress(&jcs);
//			jpeg_destroy_compress(&jcs);

	//		env->SetIntArrayRegion(newArray,0,len,intarr);
			env->SetIntArrayRegion(newArray,0,len,array);

	//		(void) jpeg_finish_decompress(&cinfo);
			jpeg_destroy_decompress(&cinfo);
			fclose(fp);
			__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "n=%d",test);
			__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "arr=%d",arr[0]);
			__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "intarr=%d",intarr[0]);
			__android_log_print(ANDROID_LOG_INFO, "JNIMsg", "newArray=%d",newArray[0]);


//		AndroidBitmap_unlockPixels(env, bitmap);
		return newArray;

}
