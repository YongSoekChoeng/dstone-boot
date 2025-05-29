package net.dstone.common.utils;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.VFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.dstone.common.config.ConfigProperty;

@Component
public class FtpUtil {

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
	    		localFile = FileUtil.getFileName(localFileFullName, true);
		    	remote = manager.resolveFile( "sftp://" + username + ":" + password + "@" + remoteHost + "/" + remoteDir + "/" + localFile);
		    	remote.copyFrom(local, Selectors.SELECT_SELF);
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
	    	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if( local != null ) {
				local.close();
			}
			if( remote != null ) {
				remote.close();
			}
		}
	}

}
