package net.dstone.common.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import net.dstone.common.config.ConfigProperty;


public class FileUpUtil {

	private static ConfigProperty CONFIG = null; // 프로퍼티 가져오는 bean. 스프링빈 호출하는 방법으로 수정해야 함.

	private Map<String, List<String>> parameters = new HashMap<>();
	private Map<String, FileItem> fileItems = new HashMap<>();
	private Properties uploadInfo = new Properties();
	private boolean isMultiPart = false;
	private HttpServletRequest request;
	private String encoding = "utf-8";

	public FileUpUtil(HttpServletRequest request) throws Exception {
		if( CONFIG == null) {
			CONFIG = SpringUtil.getBean(ConfigProperty.class);
		}
		this.request = request;
		ArrayList<Properties> uploadList = new ArrayList<>();
		String FILEUP_WEB_DIR = CONFIG.getProperty("resources.fileUp.path");
		
		try {
			// Multipart 요청 체크
			isMultiPart = ServletFileUpload.isMultipartContent(request);
			
			int maxPostSize = 15 * 1024 * 1024; // 15MB
			String saveDirectory = FILEUP_WEB_DIR;
			net.dstone.common.utils.FileUtil.makeDir(saveDirectory);
			
			uploadInfo.put("SAVE_DIRECTORY", saveDirectory);
			uploadInfo.put("MAX_POST_SIZE", String.valueOf(maxPostSize));
			uploadInfo.put("UPLOAD_LIST", uploadList);
			
			request.setAttribute("uploadInfo", uploadInfo);
			
			if (isMultiPart) {
				// DiskFileItemFactory 설정
				DiskFileItemFactory factory = new DiskFileItemFactory();
				// 메모리에 저장할 최대 크기 (이를 초과하면 임시 파일로 저장)
				factory.setSizeThreshold(1024 * 1024); // 1MB
				// 임시 파일 저장 디렉토리
				String tmpDri = System.getProperty("java.io.tmpdir") + "/" + DateUtil.getToDate("yyyyMMddHHmmss");
				FileUtil.makeDir(tmpDri);
				factory.setRepository(new File(tmpDri));
				
				// ServletFileUpload 설정
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setFileSizeMax(maxPostSize); // 개별 파일 최대 크기
				upload.setSizeMax(maxPostSize); // 전체 요청 최대 크기
				upload.setHeaderEncoding(encoding);
				
				// 파일 업로드 파싱
				List<FileItem> items = upload.parseRequest(request);
				
				for (FileItem item : items) {
					if (item.isFormField()) {
						// 일반 폼 필드 처리
						String fieldName = item.getFieldName();
						String fieldValue = item.getString(encoding);
						
						if (!parameters.containsKey(fieldName)) {
							parameters.put(fieldName, new ArrayList<>());
						}
						parameters.get(fieldName).add(fieldValue);
					} else {
						// 파일 필드 처리
						String fieldName = item.getFieldName();
						String fileName = item.getName();
						
						if (fileName == null || fileName.trim().isEmpty()) {
							continue;
						}
						
						File tmpFile = null;
						try {
							tmpFile = new File(fileName);
							// 파일명에서 경로 제거 (IE 대응)
							fileName = tmpFile.getName();
							
							// 새로운 파일명 생성
							String fileExt = FileUtil.getFileExt(fileName);
							String newFileName = UUID.randomUUID().toString().replace("-", "") + "." + fileExt;
							
							// 파일 저장
							File savedFile = new File(saveDirectory, newFileName);
							item.write(savedFile);
							
							// 파일 정보 저장
							fileItems.put(fieldName, item);
							
							Properties uploadFileRow = new Properties();
							uploadFileRow.setProperty("SAVED_FILE_NAME", newFileName);
							uploadFileRow.setProperty("CONTENTS_TYPE", item.getContentType());
							uploadFileRow.setProperty("ORIGINAL_FILE_NAME", fileName);
							uploadFileRow.setProperty("FILE_EXTEND", fileExt);
							uploadFileRow.setProperty("FILE_SIZE", String.valueOf(savedFile.length()));
							uploadList.add(uploadFileRow);
						} catch (Exception e) {
							// TODO: handle exception
						} finally {
							if( tmpFile != null ) {
								try {
									tmpFile.delete();
								} catch (Exception e2) {
									// TODO: handle exception
								}
							}
						}
					}
				}
				
				FileUtil.deleteDir(tmpDri);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public String getParameter(String key) {
		String val = null;
		try {
			if (isMultiPart) {
				List<String> values = parameters.get(key);
				if (values != null && !values.isEmpty()) {
					val = values.get(0);
				}
			} else {
				val = request.getParameter(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}

	public String[] getParameterValues(String key) {
		String[] val = null;
		try {
			if (isMultiPart) {
				List<String> values = parameters.get(key);
				if (values != null) {
					val = values.toArray(new String[0]);
				}
			} else {
				val = request.getParameterValues(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}

	public java.util.Enumeration<String> getParameterNames() {
		java.util.Enumeration<String> val = null;
		try {
			if (isMultiPart) {
				val = java.util.Collections.enumeration(parameters.keySet());
			} else {
				val = request.getParameterNames();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}

	public Properties getUploadInfo() {
		return uploadInfo;
	}

	public void setUploadInfo(Properties uploadInfo) {
		this.uploadInfo = uploadInfo;
	}

	/**
	 * 파일명 가져오기
	 */
	public String getFilesystemName(String fieldName) {
		FileItem item = fileItems.get(fieldName);
		if (item != null) {
			// 저장된 파일명은 uploadInfo에서 가져와야 함
			@SuppressWarnings("unchecked")
			ArrayList<Properties> uploadList = (ArrayList<Properties>) uploadInfo.get("UPLOAD_LIST");
			for (Properties prop : uploadList) {
				return prop.getProperty("SAVED_FILE_NAME");
			}
		}
		return null;
	}

	/**
	 * 원본 파일명 가져오기
	 */
	public String getOriginalFileName(String fieldName) {
		FileItem item = fileItems.get(fieldName);
		if (item != null) {
			String fileName = item.getName();
			return new File(fileName).getName();
		}
		return null;
	}

	/**
	 * Content Type 가져오기
	 */
	public String getContentType(String fieldName) {
		FileItem item = fileItems.get(fieldName);
		return item != null ? item.getContentType() : null;
	}

	/**
	 * 파일 객체 가져오기
	 */
	public File getFile(String fieldName) {
		String saveDirectory = uploadInfo.getProperty("SAVE_DIRECTORY");
		String fileName = getFilesystemName(fieldName);
		if (fileName != null) {
			return new File(saveDirectory, fileName);
		}
		return null;
	}
}
