package com.mycompany.Multi_Dialog;
import android.content.*;
import java.io.*;

public class File_OpeS
{

	private Context thisConte;
	public String PrevStr="Prev";
	public String tmpStr="tmpPrev";
	
	
	//const
	public File_OpeS(Context conte){
		thisConte=conte;	
	}
	
	////////////
	// Methods
	////////////
	
	//######## << File Check & Copy  ############################################################
	public int ChkFileCopy(File SrcFile,File DstFile,int BufSize) {
		// return 1 = Src is Nothing
		// return 2 = Dst is existing
		// return 4 = DstParent is Nothing
		// return 8 = DstParent is not writable
		// return 0 = Chk is good and copy Src to Dst
		// return -1 = Copying occur Err
		int rtnCD = 0;
		
		//SrcFile exists chk
		if(!SrcFile.exists()){
			rtnCD = rtnCD+1;
		}
		
		//DstFile Nonexists chk
		if(DstFile.getParentFile().exists()){
			if(DstFile.exists()){
				rtnCD = rtnCD+2;
			}
			if(!DstFile.getParentFile().canWrite()){
				rtnCD = rtnCD+8;
			}
		}else{
			rtnCD = rtnCD+4;
		}
		
		//Chk & Copy
		if(rtnCD==0){
			// /sdcard/Download
			try{
				smplFileCopy(SrcFile,DstFile,BufSize);
			}catch(IOException e){
				rtnCD = -1;
			}
		}
		return rtnCD;
	}
	//########  File Check & Copy >>  ############################################################
	
	//######## << smplFileCopy  ############################################################
	public void smplFileCopy(File srcFile,File dstFile,int bufSize)throws IOException{
		// /sdcard/Download
		InputStream inFile = new FileInputStream(srcFile);
		OutputStream outFile = new FileOutputStream(dstFile);
		try{
			byte[] buff = new byte[1024*bufSize];
			int n = 0;
			while(-1 != (n = inFile.read(buff))){
				outFile.write(buff,0,n);
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			inFile.close();
			outFile.close();
		}
	}
	//########  smplFileCopy >>  ############################################################
	
	//######## << ExistDelCopy  ############################################################
	public int ExistDelCopy(File SrcFile,File DstFile,int bufSZ){
		//(1)
		if(!SrcFile.exists()){
			return 2;
		}
		//(2)
		File curParent = DstFile.getParentFile();
		if(!curParent.exists()){
			return 4;
		}
		//(3)
		int RtnAdd =0;
		if(DstFile.exists()){
			//(4)
			if(!DstFile.delete()){
				return 8;
			}
		}else{
			//(5)
			RtnAdd=1;
		}
		//(6)
		try{
			smplFileCopy(SrcFile,DstFile,bufSZ);
		}catch(IOException e){
			return 16+RtnAdd;
		}
		//(7)
		return 0+RtnAdd;
	}
	//########  ExistDelCopy >>  ###########################################################
	
	//######## << Copy2Genes  ############################################################
	public int Copy2Genes(File SrcFile,File DstFile,int bufSz){
		
		//(1)
		if(!SrcFile.exists()){
			return 2;
		}
		//(2)
		File curParent = DstFile.getParentFile();
		if(!curParent.exists()){
			return 4;
		}
		
		File PrevFile = new File(curParent.getAbsolutePath()+"/"+PrevStr+DstFile.getName());
		File tmpPrevFile = new File(curParent.getAbsolutePath()+"/"+tmpStr+DstFile.getName());
		int RtnAdd =0;
		
		//(3)
		if(DstFile.exists()){
			//(4)
			if(PrevFile.exists()){
				//(5)
				if(tmpPrevFile.exists()){
					//(6)
					if(!tmpPrevFile.delete()){
						return 8;
					}
				}
				//(7)
				try{
					smplFileCopy(PrevFile,tmpPrevFile,bufSz);
				}catch(IOException e){
					return 16;
				}
				//(8)
				if(!PrevFile.delete()){
					if(!PrevFile.exists()){
						try{
							smplFileCopy(tmpPrevFile,PrevFile,bufSz);
						}catch(IOException e){
							return 64;
						}
					}
					return 32;
				}
			}
			//(9)
			try{
				smplFileCopy(DstFile,PrevFile,bufSz);
			}catch(IOException e){
				if(tmpPrevFile.exists()){
					try{
						smplFileCopy(tmpPrevFile,PrevFile,bufSz);
					}catch(IOException e2){
						return 256;
					}
				}
				return 128;
			}
			//(10)
			if(!DstFile.delete()){
				if(!DstFile.exists()){
					try{
						smplFileCopy(PrevFile,DstFile,bufSz);
					}catch(IOException e){
						return 1024;
					}
				}
				return 512;
			}
		}else{
			//(11)
			RtnAdd=1;
		}
		
		//(12)
		try{
			smplFileCopy(SrcFile,DstFile,bufSz);
		}catch(IOException e){
			if(DstFile.exists()){
				if(!DstFile.delete()){
					return 4096+RtnAdd;
				}
			}
			try{
				smplFileCopy(PrevFile,DstFile,bufSz);
			}catch(IOException e2){
				return 8192+RtnAdd;
			}
			return 2048+RtnAdd;
		}	
		//(13)
		return 0+RtnAdd;
	}
	//########  Copy2Genes >>  ############################################################
	
}
