package net.integration.framework.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.integration.framework.commons.drdc.util.DrdcUtil;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * <b>描述<b>：文件上传解析类
 * @version v1.0
 */
public class FileOperateUtil {

	private static final Logger log = Logger.getLogger(FileOperateUtil.class);
	
	/**
	 * 解析request中的附件
	 * @param request
	 * @return
	 */
	public static List<List<MultipartFile>> analysis(){
		List<List<MultipartFile>> fls = new ArrayList<List<MultipartFile>>();
		
		HttpServletRequest request = RequestUtil.getRequest();
		if(request instanceof MultipartHttpServletRequest){
			MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
			
			Iterator<String> fileNames = mRequest.getFileNames();
			while (fileNames.hasNext()) {
				String name=fileNames.next();
				List<MultipartFile> storeFiles=new LinkedList<MultipartFile>();
				List<MultipartFile> files = mRequest.getFiles(name);
				
				if(files!=null && !files.isEmpty()){
					for (MultipartFile multipartFile : files) {
						if(multipartFile.getSize()>0){//大于0的文件才能储存
							storeFiles.add(multipartFile);
						}
					}
					if(!storeFiles.isEmpty()){
						fls.add(storeFiles);
					}
				}
			}
		}
		return fls;
	}
	
	
	/**
	 * 解析request中的附件
	 * @param request
	 * @return
	 */
	public static List<File> analysis2FileList(){
		List<File> fls = new ArrayList<File>();
		
		HttpServletRequest request = RequestUtil.getRequest();
		if(request instanceof MultipartHttpServletRequest){
			MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
			Map<String, MultipartFile> fileMap = mRequest.getFileMap(); 
			
			Iterator<Map.Entry<String, MultipartFile>> it = fileMap.entrySet().iterator();
			
			
			while(it.hasNext()){
				 Map.Entry<String, MultipartFile> entry = it.next();
		         MultipartFile mFile = entry.getValue();
		         if(mFile!=null && mFile.getSize()>0){
		             String storeTempLocation = DrdcUtil.getStoreTempLocation();
		             String randomStr = FileUtils.getRandomStr()+".tmp";
		             File file = new File(storeTempLocation+File.separator+DateUtil.getyyyyMMdd()+File.separator+randomStr);
		             FileUtils.mkDirectory(file);
		             
		             try {
						mFile.transferTo(file);
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
		             fls.add(file);
		         }
			}//while
		}
		return fls;
	}
	
	/**
	 * 解析request中的附件
	 * @param request
	 * @return
	 */
	public static File analysis2File(){
		List<File> analysis2FileList = analysis2FileList();
		if(analysis2FileList!=null && !analysis2FileList.isEmpty()){
			return analysis2FileList.get(0);
		}
		return null;
	}
	
	public static String[] splictFileName(String filename){
		if(!StringUtils.isEmpty(filename)){
			String[] split={filename,""};
			if(filename.indexOf(".")!=-1){
				split[0]=filename.substring(0, filename.lastIndexOf("."));
				split[1]=filename.substring(filename.lastIndexOf(".")+1, filename.length());
			}
			return split;
		}
		log.debug("filename为''|null");
		return new String[]{"",""};
	}
	
	
	/**
	 * 根据filename获取表名
	 * @param fileSaveTables
	 * @param filename
	 * @return 如果没有找到，默认返回SystemProperties.getFileSaveTable();
	 */
	public static String getFileSaveTables(String[] fileSaveTables,String filename){
		String bm=null;
		if(fileSaveTables!=null && fileSaveTables.length>0){
			for (String string : fileSaveTables) {
				if(string.startsWith(filename)){
					return string.replace(filename+"_", "");
				}
			}
		}
		if(bm==null){
			bm=SystemProperties.getFileSaveTable();
		}
		return bm;
	}
	
	/**
	 * 根据filename获取存放方式
	 * @param fileSaveTables
	 * @param filename
	 * @return 如果没有找到，默认返回SystemProperties.getFileSaveTable();
	 */
	public static String getFileSaveTypes(String[] fileSaveTypes,String filename){
		String cffs=null;
		if(fileSaveTypes!=null && fileSaveTypes.length>0){
			for (String string : fileSaveTypes) {
				if(string.startsWith(filename)){
					return string.replace(filename+"_", "");
				}
			}
		}
		if(cffs==null){
			cffs=SystemProperties.getFileSaveType();
		}
		return cffs;
	}
	
	
	/**
	 * 前端file对象的name中加入的标记
	 * @param name
	 * @return
	 */
	public static String getFilterName(String name){
		if(name.lastIndexOf("-")!=-1){
			name=name.substring(0, name.lastIndexOf("--"));
		}
		return name;
	}
	
	
	
	
	public static void main(String[] args) {
//		String[] splictFileName = FileOperateUtil.splictFileName("aaaa.zip.rar");
		
//		System.out.println(Arrays.toString(splictFileName));
		
//		String[] ts = {"file1_t_sys_fj","file2_t_sys_fj"};
//		
//		String fileSaveTables = FileOperateUtil.getFileSaveTables(ts, "file3");
//		
//		System.out.println(fileSaveTables);
		
		System.out.println(FileOperateUtil.getFilterName("file6"));
	}
}
