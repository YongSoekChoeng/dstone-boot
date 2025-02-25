package net.dstone.common.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ZipDownUtil  extends javax.servlet.http.HttpServlet {
	/**
	 * <code>doGet</code> Process incoming HTTP doGet requests
	 * 
	 * @param request
	 *            FWObject that encapsulates the request to the servlet
	 * @param response
	 *            FWObject that encapsulates the response from the servlet
	 */
	public void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {

		performTask(request, response);

	}

	/**
	 * <code>doPost</code> Process incoming HTTP doGet requests
	 * 
	 * @param request
	 *            FWObject that encapsulates the request to the servlet
	 * @param response
	 *            FWObject that encapsulates the response from the servlet
	 */
	public void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {

		performTask(request, response);

	}

	/**
	 * <code>performTask</code> Process incoming HTTP doGet requests
	 * 
	 * @param request
	 *            FWObject that encapsulates the request to the servlet
	 * @param response
	 *            FWObject that encapsulates the response from the servlet
	 */
	public void performTask(HttpServletRequest req, HttpServletResponse res) {

		String[] ORI_FILE_NAME_ARR = req.getParameterValues("ORI_FILE_NAME");
		String SAVE_FILE_NAME = req.getParameter("SAVE_FILE_NAME");

		if( ORI_FILE_NAME_ARR != null && ORI_FILE_NAME_ARR.length > 0 && !StringUtil.isEmpty(SAVE_FILE_NAME)) {

			ZipOutputStream zos = null;
			
	        // 본격적인 zip 파일을 만들어 내기 위한 로직
	        try{

				if( !SAVE_FILE_NAME.endsWith(".zip") ) {
					SAVE_FILE_NAME = SAVE_FILE_NAME + ".zip";
				}
				SAVE_FILE_NAME = URLEncoder.encode(SAVE_FILE_NAME,"UTF-8").replaceAll("\\+", "%20");
				
		        // ======================== 파일 다운로드 위한 response 세팅 ========================
				res.setContentType("application/zip");
				res.setHeader("Content-Disposition", "attachment; filename=" + new String( SAVE_FILE_NAME.getBytes(StandardCharsets.UTF_8)) + ".zip;");
				res.setStatus(HttpServletResponse.SC_OK);
		        // =============================================================================
				
	        	zos = new ZipOutputStream(res.getOutputStream());
	        	
				for(String ORI_FILE_NAME : ORI_FILE_NAME_ARR) {
	                Path path = Paths.get(ORI_FILE_NAME);
	                try (FileInputStream fis = new FileInputStream(path.toFile())) {
	                    // 압축될 파일명을 ZipEntry에 담아준다
	                    ZipEntry zipEntry = new ZipEntry(path.getFileName().toString());
	                    // 압축될 파일명을 ZipOutputStream 에 담아준다
	                    zos.putNextEntry(zipEntry);
	                    byte[] buffer = new byte[1024];
	                    int length;
	                    while ((length = fis.read(buffer)) >= 0) {
	                        zos.write(buffer, 0, length);
	                    }
	                } catch (FileNotFoundException e) {
	                    res.setStatus(HttpServletResponse.SC_NOT_FOUND);
	                    throw new IllegalArgumentException("파일 변환 작업중, [ " + ORI_FILE_NAME + " ] 파일을 찾을 수 없습니다.");
	                } catch (IOException e) {
	                    res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	                    throw new IllegalArgumentException("파일 변환 작업중, [ " + ORI_FILE_NAME + " ] 파일을 다운로드 할 수 없습니다.");
	                } finally {
	                    // ZipOutputStream 에 담아둔 압축될 파일명을 flush 시켜준다
	                    zos.flush();
	                    // ZipOutputStream 에 담아둔 압축될 파일명을 close 시켜준다
	                    zos.closeEntry();
	                }
				}

	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	        	if( zos != null ) {
	        		try {
	        			zos.close();
					} catch (Exception e2) {
						// TODO: handle exception
					}
	        	}
	        }
		}

	}
}
