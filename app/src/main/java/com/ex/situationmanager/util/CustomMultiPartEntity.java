package com.ex.situationmanager.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
 
public class CustomMultiPartEntity extends MultipartEntity
{
 
	public ProgressListener listener;
	public static boolean  isStop = false;
 
	public CustomMultiPartEntity(final ProgressListener listener)
	{
		super();
		isStop = false;
		this.listener = listener;
	}
 
	public CustomMultiPartEntity(final HttpMultipartMode mode, final ProgressListener listener)
	{
		super(mode);
		this.listener = listener;
	}
 
	public CustomMultiPartEntity(HttpMultipartMode mode, final String boundary, final Charset charset, final ProgressListener listener)
	{
		super(mode, boundary, charset);
		this.listener = listener;
	}
 
	@Override
	public void writeTo(final OutputStream outstream) throws IOException
	{
		super.writeTo(new CountingOutputStream(outstream, this.listener));
	}
 
	public static interface ProgressListener
	{
		void transferred(long num);
	}
 
	//以묒?
	public void stop(){
		isStop = true;
	}
	
	public static class CountingOutputStream extends FilterOutputStream
	{
 
		private final ProgressListener listener;
		private long transferred;
 
		public CountingOutputStream(final OutputStream out, final ProgressListener listener)
		{
			super(out);
			this.listener = listener;
			this.transferred = 0;
		}
 
		public void write(byte[] b, int off, int len) throws IOException
		{
			//以묒??씤 寃쎌슦 ?뙆?씪 ?쟾?넚 ?븞?븿
			if(!isStop){
				out.write(b, off, len);
			}
			
			this.transferred += len;
			this.listener.transferred(this.transferred);
		}
 
		public void write(int b) throws IOException
		{
			//以묒??씤 寃쎌슦 ?뙆?씪 ?쟾?넚 ?븞?븿			
			if(!isStop){
				out.write(b);
			}
			
			this.transferred++;
			this.listener.transferred(this.transferred);
		}
	}
}
