package net.integration.framework.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.integration.framework.dao.impl.BaseDaoImpl;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.web.context.ContextLoader;

/**
 * 
 * 名称：<br/> 
 * 描述： 统一工具类规范使用
 * @version 1.0.0.0
 */
public class ExcelUtil {
	
	/**
	 * 
	 * <b></b>
	 * 描述 ： 创建excel，void。
	 * @param headers
	 * @param list
	 * @param filePath
	 * @throws IOException
	 */
	public static void createExcel(String headers, List list, String filePath)
			throws IOException {
		OutputStream os = new FileOutputStream(filePath);
		HSSFWorkbook book = new HSSFWorkbook();
		book.createSheet("sheet1");
		book.write(os);
	}
	/**
	 * 
	 * <b></b>
	 * 描述 读取excel内容： ，void。
	 * @param fileName
	 */
	public static void readExcel(String fileName){
		try {
			InputStream is = new FileInputStream(fileName);
			HSSFWorkbook book = new HSSFWorkbook(is);
			//获取sheet个数
			int sheetNum = book.getNumberOfSheets();
			for(int i= 0 ;i<sheetNum ; i++){
				HSSFSheet sheet = book.getSheetAt(i);
				//获取行数
				int rowNum = sheet.getLastRowNum()+1;
				for(int j = 1; j<rowNum ;j++){
					HSSFRow row = sheet.getRow(j);
					//获取列数
					int colNum  = row.getLastCellNum();
					for(int k = 0 ; k<colNum ; k++){
						HSSFCell  cell = row.getCell(k);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * <b></b>
	 * 描述 ：解析excle ，List<?>。
	 * @param file
	 * @param classes
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static List<?> getExcelList(File file, Class<?>... classes)
			throws Exception {
		HSSFWorkbook book = new HSSFWorkbook(new BufferedInputStream(
				new FileInputStream(file)));
		List<Object> results = new ArrayList<Object>();
		String errorMsg = "";
		int sheetNum = book.getNumberOfSheets();
		if (sheetNum == 1) {
			for (int i = 0; i < sheetNum; i++) {
				HSSFSheet sheet = book.getSheetAt(i);
				HSSFRow headerRow = sheet.getRow(0);
				int rowNum = sheet.getLastRowNum() + 1;
				int celNum = headerRow.getLastCellNum();
				String[] header = new String[celNum];
				for (int h = 0; h < celNum; h++) {
					header[h] = headerRow.getCell(h).getStringCellValue();
				}
				for (int j = 1; j < rowNum; j++) {
					HSSFRow row = sheet.getRow(j);
					Object object = classes[0].newInstance();
					int colNum = row.getLastCellNum();
					for (int k = 0; k < colNum; k++) {
						HSSFCell cell = row.getCell(k);
						String value = "";
						if (null != cell) {
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							value = cell.getStringCellValue();
						}
						try {
							setObjAttr(object, header[k], cell);
						} catch (Exception e) {
							errorMsg += "第" + (j + 1) + "行，第" + (k + 1)
									+ "列出错，错误信息：[" + value + "] "
									+ e.getMessage() + "\n";
							continue;
						}
					}
					results.add(object);
				}
			}
		} else {
			for (int i = 0; i < sheetNum; i++) {
				List<Object> sheetList = new ArrayList<Object>();
				HSSFSheet sheet = book.getSheetAt(i);
				String sheetName = sheet.getSheetName();
				Class<?> clazz = null;
				for (Class<?> clazs : classes) {
					if (sheetNum == 1)
						break;
					Method method = clazs.getMethod("getTableName");
					String tableName = (String) method.invoke(clazs
							.newInstance());
					if (null != tableName && tableName.equals(sheetName)) {
						clazz = clazs;
						break;
					}
					if (null == clazz && sheetNum > 1)
						continue;
					HSSFRow headerRow = sheet.getRow(0);
					int rowNum = sheet.getLastRowNum() + 1;
					int celNum = headerRow.getLastCellNum();
					String[] header = new String[celNum];
					for (int h = 0; h < celNum; h++) {
						header[h] = headerRow.getCell(h).getStringCellValue();
					}
					for (int j = 1; j < rowNum; j++) {
						HSSFRow row = sheet.getRow(j);
						Object object = clazz.newInstance();
						int colNum = row.getLastCellNum();
						for (int k = 0; k < colNum; k++) {
							HSSFCell cell = row.getCell(k);
							String value = "";
							if (null != cell) {
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								value = cell.getStringCellValue();
							}
							try {
								setObjAttr(object, header[k], cell);
							} catch (Exception e) {
								errorMsg += "第" + (j + 1) + "行，第" + (k + 1)
										+ "列出错，错误信息：[" + value + "] "
										+ e.getMessage() + "\n";
								continue;
							}
						}
						sheetList.add(object);
					}
					results.add(sheetList);
				}
			}

		}
		if (!"".equals(errorMsg)) {
			throw new Exception(errorMsg);
		}
		return results;
	}
	
	/**
	 * 
	 * <b></b>
	 * 描述 ： ，void。
	 * @param obj
	 * @param header
	 * @param cell
	 * @throws Exception
	 */
	private static void setObjAttr(Object obj, String header, HSSFCell cell) throws Exception {
		Class<?> clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		Method method = null;
		for (Field field : fields) {
			Excel excel = field.getAnnotation(Excel.class);
			if (field.isAnnotationPresent(Excel.class)) {
				String name = excel.name();
				if (name.equals(header)) {
					String code = excel.code();
					String type = excel.type();
					int length = excel.length();
					boolean isRequired = excel.isRequired();
					Class<?> typeClass = field.getType();
					String setMethodName = "set" + StringUtils.toUpperChar(code);
					method = clazz.getMethod(setMethodName,
							new Class[] { typeClass });
					String value = "";
					if (null != cell) {
						value = cell.getStringCellValue();
					}
					Object setValue = value;
					if ("java.lang.String".equals(type)) {
						if (isRequired) {
							if (null == cell
									|| "".equals(cell.getStringCellValue())) {
								throw new Exception("不能为空！");
							}
						}
						if (value.length() > length) {
							throw new Exception("长度不能超过" + length + "！");
						}
					} else if ("java.lang.Integer".equals(type)) {
						if (isRequired) {
							try {
								setValue = Integer.valueOf(value);
							} catch (NumberFormatException e) {
								throw new Exception("不是数字！");
							}
						} else {
							if (null == value || "".equals(value)) {
								setValue = null;
							} else {
								try {
									setValue = Integer.valueOf(value);
								} catch (NumberFormatException e) {
									throw new Exception("不是数字！");
								}
							}
						}
					} else if ("java.lang.Double".equals(type)) {
						if (isRequired) {
							if (null == value || "".equals(value)) {
								throw new Exception("不能为空！");
							} else {
								try {
									setValue = Double.valueOf(value);
								} catch (NumberFormatException e) {
									throw new Exception("不是数字！");
								}
							}
						} else {
							if (null == value || "".equals(value)) {
								setValue = null;
							} else {
								try {
									setValue = Double.valueOf(value);
								} catch (NumberFormatException e) {
									throw new Exception("不是数字！");
								}
							}
						}
					} else if ("java.lang.Date".equals(type)) {
						String dataFormat = excel.dataFormat();
						if (isRequired) {
							if (null == value || "".equals(value)) {
								throw new Exception("不能为空！");
							} else {
								try {
									setValue = DateUtil
											.parse(value, dataFormat);
								} catch (NumberFormatException e) {
									throw new Exception("日期格式不正确，请按照："
											+ dataFormat + "！");
								}
							}
						} else {
							if (null == value || "".equals(value)) {
								setValue = null;
							} else {
								try {
									setValue = DateUtil
											.parse(value, dataFormat);
								} catch (NumberFormatException e) {
									throw new Exception("日期格式不正确，请按照："
											+ dataFormat + "！");
								}
							}
						}
					}
					method.invoke(obj, new Object[] { setValue });
					/*查询字典数据*/
					boolean dict  = excel.dict();
					if(dict){
						String bs = excel.bs();
						String tbAttr = excel.tbattr();
						String csAttr = excel.csAttr();
						String dictName = excel.dictName();
						if(!"".equals(bs)&&!"".equals(tbAttr)&&!"".equals(csAttr)&&!"".equals(dictName)){
							BaseDaoImpl dao = (BaseDaoImpl) ContextLoader.getCurrentWebApplicationContext().getBean("baseDao");
							Map<String,String> map = new HashMap<String,String>();
							map.put("bs", bs);
							map.put("bsValue", cell == null?"":cell.getStringCellValue());
							map.put("tbAttr", tbAttr);
							map.put("dictName", dictName);
							List<Map<String,String>> results = dao.findWithMap(map,"excel.getAttr");
							String result = "";
							if(results != null && results.size() >0 ){
								result = results.get(0).get("TBATTR");
							}
							String setMethodNm = "set" + StringUtils.toUpperChar(csAttr);
							method = clazz.getMethod(setMethodNm,new Class[]{String.class});
							method.invoke(obj, new String[]{result});
						}
					}
					break;
				}
			}
		}
	}
	/**
	 * 
	 * <b></b>
	 * 描述 ： ，Object。
	 * @param object
	 * @param attr
	 * @return
	 * @throws Exception
	 */
	private static Object getObjectAttr(Object object, String attr) throws Exception {
		Class<?> clazz = object.getClass();
		Field[] fields = clazz.getDeclaredFields();
		Object result = null;
		for(Field field: fields) {
			Excel excel = field.getAnnotation(Excel.class);
			if(field.isAnnotationPresent(Excel.class)) {
				String name = excel.name();
				if(name.equals(attr)) {
					String code = excel.code();
					String methodName = "get"+StringUtils.toUpperChar(code);
					Method method = clazz.getMethod(methodName);
					result = method.invoke(object);
					String type = excel.type();
					if("java.util.Date".equals(type)) {
						String dataFormat = excel.dataFormat();
						result = DateUtil.format((Date) result, dataFormat);
					}
					break;
				}
			}
		}
		return result;
	}
	/**
	 * 
	 * <b></b>
	 * 描述 ： ，HSSFWorkbook。
	 * @param header
	 * @param list
	 * @param clazz
	 * @return
	 */
	public static HSSFWorkbook listToExcel(String[] header, List<?> list,
			Class<?> clazz)throws  Exception{
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("sheet1");
		HSSFRow headrow = sheet.createRow(0);
		// 单元格样式
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 10);
		cellStyle.setFont(font);
		// 创建表头
		for (int i = 0; i < header.length; i++) {
			HSSFCell cell = headrow.createCell(i);
			cell.setCellValue(header[i]);
			cell.setCellStyle(cellStyle);
		}
		// 创建数据行数
		int rowSize = list.size();
		for (int rowindex = 1; rowindex < rowSize + 1; rowindex++) {
			Object object = clazz.cast(list.get(rowindex - 1));
			HSSFRow bodyrow = sheet.createRow(rowindex);
			for (int colindex = 0; colindex < header.length; colindex++) {
				HSSFCell cell = bodyrow.createCell(colindex);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				Object cellValue = getObjectAttr(object, header[colindex]);
				cell.setCellValue(cellValue == null ? "" : cellValue.toString());
				cell.setCellStyle(cellStyle);
			}
		}
		// 列宽自适应
		for (int i = 0; i < header.length; i++) {
			sheet.autoSizeColumn(i);
		}
		return workbook;
	}
	/**
	 * 
	 * <b></b>
	 * 描述 ： ，InputStream。
	 * @param workbook
	 * @return
	 * @throws IOException
	 */
	public static InputStream toStream(HSSFWorkbook workbook)throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream(512);
		if(output != null){
			workbook.write(output);
		}
		ByteArrayInputStream input  = new ByteArrayInputStream(output.toByteArray());
		return input;
	}
	/**
	 * 
	 * <b></b>
	 * 描述 ： ，void。
	 * @param workbook
	 * @param filePath
	 * @throws IOException
	 */
	public static void toFile(HSSFWorkbook workbook,String filePath)throws IOException{
		 BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(filePath));
		 if(output != null)
			  workbook.write(output);
		 output.close();
	}
	
	public static void exportExcel(List<?> list, String headers) throws IOException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		
		String[] headerList = headers.split(",");
		HSSFWorkbook book = new HSSFWorkbook();
		//创建表头
		HSSFSheet sheet = book.createSheet();
		HSSFRow headerRow = sheet.createRow(0);
		for(int i = 0 ;i <headerList.length;i++ ){
			HSSFCell cell = headerRow.createCell(i);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			CellStyle style = cell.getCellStyle();
			style.setAlignment(CellStyle.ALIGN_CENTER);
			cell.setCellValue(headerList[i]);
		}
		//创建内容
		for (int i = 0; i < list.size(); i++) {
			HSSFRow row = sheet.createRow(i+1);
			Object obj = list.get(i);
			Class clazz = obj.getClass();
			Field[] fields  = clazz.getDeclaredFields();
			Method method = null;
			for (int j = 0; j < headerList.length; j++) {
				for (Field field : fields) {
					Excel excel = field.getAnnotation(Excel.class);
					if(field.isAnnotationPresent(Excel.class)){
						String code = excel.code();
						String name = excel.name();
						if(name.equals(headerList[j])){
							String getMethodName = "get"+ StringUtils.toUpperChar(code);
							method = clazz.getMethod(getMethodName, new Class[]{});
							Object cellValue = method.invoke(obj,new Object[]{});
							HSSFCell cell = row.createCell(j);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue(cellValue == null ?"":cellValue.toString());
							break;
						}
					}
				}
			}
		}
		OutputStream os = new FileOutputStream("/root/桌面/rylb.xls");
		book.write(os);
	}
}
