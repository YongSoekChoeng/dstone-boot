package net.dstone.common.utils;

import java.io.File;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.provider.sftp.IdentityInfo;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.sftp.TrustEveryoneUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.dstone.common.config.ConfigProperty;
import net.dstone.common.core.BaseObject;

@Component
public class FtpUtil extends BaseObject {

	@Autowired 
	ConfigProperty configProperty; // 프로퍼티 가져오는 bean

	private FileSystemOptions getAuthOption(String privateKeyPath) throws Exception {
		FileSystemOptions opts = null;
		try {
            opts = new FileSystemOptions();
            SftpFileSystemConfigBuilder builder = SftpFileSystemConfigBuilder.getInstance();

            // 개인 키 파일 경로 설정
            File privateKeyFile = new File(privateKeyPath); // 개인 키 파일의 실제 경로
            //builder.setIdentityProvider(opts, new IdentityInfo(privateKeyFile));
            builder.setIdentities(opts, new File[] {privateKeyFile});
            builder.setStrictHostKeyChecking(opts, "no"); // 주의: 운영에서는 'yes'
            builder.setUserDirIsRoot(opts, false);

            
		} catch (Exception e) {
			throw e;
		}
		return opts;
	}
	
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
		String privateKeyPath = configProperty.getProperty("sftp.private-key-path");
	    this.uploadFile(remoteHost, username, password, privateKeyPath, localFileFullName, remoteDir);
	}

	/**
	 * SFTP 파일업로드
	 * @param remoteHost
	 * @param username
	 * @param password
	 * @param privateKeyPath
	 * @param localFileFullName
	 * @param remoteDir
	 * @throws Exception
	 */
	public void uploadFile(String remoteHost, String username, String password, String privateKeyPath, String localFileFullName, String remoteDir) throws Exception {
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
				
				//원격 SFTP 파일 객체 생성
				if( !StringUtil.isEmpty(privateKeyPath) ) {
					String cmd =  "sftp://" + username + ":" + password + "@" + remoteHost + "/" + remoteDir + "/" + localFile;
					remote = manager.resolveFile(cmd, getAuthOption(privateKeyPath));
				}else {
		    		String cmd =  "sftp://" + username + ":" + password + "@" + remoteHost + "/" + remoteDir + "/" + localFile;
					this.sysout("cmd["+cmd+"]" );
					remote = manager.resolveFile(cmd);
				}

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
		String privateKeyPath = configProperty.getProperty("sftp.private-key-path");
	    this.downloadFile(remoteHost, username, password, privateKeyPath, localDir, remoteFileFullName);
	}

	/**
	 * SFTP 파일다운로드
	 * @param remoteHost
	 * @param username
	 * @param password
	 * @param privateKeyPath
	 * @param localDir
	 * @param remoteFileFullName
	 * @throws Exception
	 */
	public void downloadFile(String remoteHost, String username, String password, String privateKeyPath, String localDir, String remoteFileFullName) throws Exception {
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
    		// 다운로드 로컬 파일
	    	local = manager.resolveFile(localFileFullName);

    		// 다운로드 대상 원격 파일
			if( remoteFileFullName.startsWith("/")) {
				remoteFileFullName = remoteFileFullName.substring(1);
			}
			if( !StringUtil.isEmpty(privateKeyPath) ) {
				String cmd =   "sftp://" + username + ":" + password + "@" + remoteHost + "/" + remoteFileFullName ;
				remote = manager.resolveFile(cmd, getAuthOption(privateKeyPath));
			}else {
	    		String cmd =   "sftp://" + username + ":" + password + "@" + remoteHost + "/" + remoteFileFullName ;
				remote = manager.resolveFile(cmd);
			}

            // 업로드 수행 (원격 → 로컬)
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
	
	/**
	 * SFTP 파일삭제
	 * @param remoteFileFullName
	 * @throws Exception
	 */
	public void deleteFile(String remoteFileFullName) throws Exception {
		String remoteHost = configProperty.getProperty("sftp.host");
		String username = configProperty.getProperty("sftp.username");
		String password = configProperty.getProperty("sftp.password");
		String privateKeyPath = configProperty.getProperty("sftp.private-key-path");
	    this.deleteFile(remoteHost, username, password, privateKeyPath, remoteFileFullName);
	}
	
	/**
	 * SFTP 파일삭제
	 * @param remoteHost
	 * @param username
	 * @param password
	 * @param privateKeyPath
	 * @param remoteFileFullName
	 * @throws Exception
	 */
	public void deleteFile(String remoteHost, String username, String password, String privateKeyPath, String remoteFileFullName) throws Exception {
	    FileSystemManager manager = VFS.getManager();
	    FileObject remote = null;
	    boolean isSucceeded = false;
	    try {
	    	if( !StringUtil.isEmpty(remoteFileFullName) ) {
	    		
	    		remoteFileFullName = StringUtil.replace(remoteFileFullName, "\\", "/");

				// 원격 SFTP 파일 객체 생성
				if( !StringUtil.isEmpty(privateKeyPath) ) {
					String cmd =  "sftp://" + username + ":" + password + "@" + remoteHost + "/" + remoteFileFullName;
					remote = manager.resolveFile(cmd, getAuthOption(privateKeyPath));
				}else {
					String cmd =  "sftp://" + username + ":" + password + "@" + remoteHost + "/" + remoteFileFullName;
					remote = manager.resolveFile(cmd);
				}
				
	            // 삭제 수행 (원격)
				remote.delete( Selectors.SELECT_SELF );

				isSucceeded = true;
	    	}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if( remote != null ) {
				remote.close();
			}
			this.sysout("net.dstone.common.utils.FtpUtil.deleteFile() ::: isSucceeded["+isSucceeded+"]");
		}
	}
	
	/**
	 * SFTP 디렉토리삭제
	 * @param remoteDirFullName
	 * @throws Exception
	 */
	public void deleteDir(String remoteDirFullName) throws Exception {
		String remoteHost = configProperty.getProperty("sftp.host");
		String username = configProperty.getProperty("sftp.username");
		String password = configProperty.getProperty("sftp.password");
		String privateKeyPath = configProperty.getProperty("sftp.private-key-path");
		this.deleteDir(remoteHost, username, password, privateKeyPath, remoteDirFullName);
	}
	
	/**
	 * SFTP 디렉토리삭제
	 * @param remoteHost
	 * @param username
	 * @param password
	 * @param privateKeyPath
	 * @param remoteDirFullName
	 * @throws Exception
	 */
	public void deleteDir(String remoteHost, String username, String password, String privateKeyPath, String remoteDirFullName) throws Exception {
	    FileSystemManager manager = VFS.getManager();
	    FileObject remote = null;
	    boolean isSucceeded = false;
	    try {
	    	if( !StringUtil.isEmpty(remoteDirFullName) ) {
	    		
	    		remoteDirFullName = StringUtil.replace(remoteDirFullName, "\\", "/");

				// 원격 SFTP 파일 객체 생성
				if( !StringUtil.isEmpty(privateKeyPath) ) {
					String cmd =  "sftp://" + username + ":" + password + "@" + remoteHost + "/" + remoteDirFullName;
					remote = manager.resolveFile(cmd, getAuthOption(privateKeyPath));
				}else {
					String cmd =  "sftp://" + username + ":" + password + "@" + remoteHost + "/" + remoteDirFullName;
					remote = manager.resolveFile(cmd);
				}

	            // 삭제 수행 (원격)
				remote.delete( Selectors.SELECT_SELF_AND_CHILDREN );

				isSucceeded = true;
	    	}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if( remote != null ) {
				remote.close();
			}
			this.sysout("net.dstone.common.utils.FtpUtil.deleteDir() ::: isSucceeded["+isSucceeded+"]");
		}
	}

}
