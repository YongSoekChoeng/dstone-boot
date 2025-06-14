package net.dstone.common.utils;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.VFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.dstone.common.config.ConfigProperty;
import net.dstone.common.core.BaseObject;

@Component
public class FtpUtil extends BaseObject {

	@Autowired 
	ConfigProperty configProperty; // 프로퍼티 가져오는 bean

	/**
	 * SFTP 파일업로드
	 * @param localFileFullName
	 * @param remoteDir
	 * @throws Exception
	 */
	public void uploadFile(String localFileFullName, String remoteDir) throws Exception {
		String remoteHost = configProperty.getProperty("sftp.host");
		String username = configProperty.getProperty("sftp.username");
		String password = configProperty.getProperty("sftp.password");
	    this.uploadFile(remoteHost, username, password, localFileFullName, remoteDir);
	}

	/**
	 * SFTP 파일업로드
	 * @param remoteHost
	 * @param username
	 * @param password
	 * @param localFileFullName
	 * @param remoteDir
	 * @throws Exception
	 */
	public void uploadFile(String remoteHost, String username, String password, String localFileFullName, String remoteDir) throws Exception {
	    FileSystemManager manager = VFS.getManager();
	    FileObject local = null;
	    FileObject remote = null;
	    boolean isSucceeded = false;
	    try {
	    	localFileFullName = StringUtil.replace(localFileFullName, "\\", "/");
	    	String localFile = "";
	    	if(FileUtil.isFileExist(localFileFullName)) {
	    		if( !StringUtil.isEmpty(remoteDir) ) {
	    			if( remoteDir.startsWith("/")) {
	    				remoteDir = remoteDir.substring(1);
	    			}
	    			if( remoteDir.endsWith("/")) {
	    				remoteDir = remoteDir.substring(0, remoteDir.length()-1);
	    			}
	    		}
	    		// 업로드 대상 로컬 파일
				local = manager.resolveFile(new File(localFileFullName).getAbsolutePath());
				
				// SFTP 경로 (파일명 URL 인코딩 처리 필수)
				localFile = FileUtil.getFileName(localFileFullName, true);
	    		//localFile = URLEncoder.encode(localFile, StandardCharsets.UTF_8.name());
	    		String cmd =  "sftp://" + username + ":" + password + "@" + remoteHost + ":22/" + remoteDir + "/" + localFile;

				//원격 SFTP 파일 객체 생성
				remote = manager.resolveFile(cmd);

	            // 업로드 수행 (로컬 → 원격)
				remote.copyFrom(local, Selectors.SELECT_SELF);

				isSucceeded = true;
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if( local != null ) {
				local.close();
			}
			if( remote != null ) {
				remote.close();
			}
			this.sysout("net.dstone.common.utils.FtpUtil.uploadFile() ::: isSucceeded["+isSucceeded+"]");
		}
	}

	/**
	 * SFTP 파일다운로드
	 * @param localDir
	 * @param remoteFileFullName
	 * @throws Exception
	 */
	public void downloadFile(String localDir, String remoteFileFullName) throws Exception {
		String remoteHost = configProperty.getProperty("sftp.host");
		String username = configProperty.getProperty("sftp.username");
		String password = configProperty.getProperty("sftp.password");
	    this.downloadFile(remoteHost, username, password, localDir, remoteFileFullName);
	}

	/**
	 * SFTP 파일다운로드
	 * @param remoteHost
	 * @param username
	 * @param password
	 * @param localDir
	 * @param remoteFileFullName
	 * @throws Exception
	 */
	public void downloadFile(String remoteHost, String username, String password, String localDir, String remoteFileFullName) throws Exception {
	    FileSystemManager manager = VFS.getManager();
	    FileObject local = null;
	    FileObject remote = null;
	    boolean isSucceeded = false;
	    try {
	    	remoteFileFullName = StringUtil.replace(remoteFileFullName, "\\", "/");
	    	String remoteFile = FileUtil.getFileName(remoteFileFullName, true);
	    	String localFileFullName = "";
    		if( !StringUtil.isEmpty(localDir) ) {
    			if( localDir.endsWith("/")) {
    				localDir = localDir.substring(0, localDir.length()-1);
    			}
    		}
    		localFileFullName = localDir + "/" + remoteFile;
    		
    		if(FileUtil.isFileExist(localFileFullName)) {
    			localFileFullName = FileUtil.getNewFileName(localFileFullName);
    		}
    		
	    	local = manager.resolveFile(localFileFullName);
	    	
			if( remoteFileFullName.startsWith("/")) {
				remoteFileFullName = remoteFileFullName.substring(1);
			}
	    	remote = manager.resolveFile( "sftp://" + username + ":" + password + "@" + remoteHost + "/" + remoteFileFullName );
	    	local.copyFrom(remote, Selectors.SELECT_SELF);
	    	isSucceeded = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if( local != null ) {
				local.close();
			}
			if( remote != null ) {
				remote.close();
			}
			this.sysout("net.dstone.common.utils.FtpUtil.downloadFile() ::: isSucceeded["+isSucceeded+"]");
		}
	}

}
