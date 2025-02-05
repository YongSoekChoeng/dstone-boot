package net.dstone.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;

public class GoogleDriveServiceTest {

	private static String CREDENTIALS_FILE_PATH = "C:/WorkShop/D/AppHome/framework/dstone-boot/src/main/resources/google/client_secret_318736365485-68nq1p97g9u3t9isu7a50km7upsiggc6.apps.googleusercontent.com.json";
	private static String APPLICATION_NAME = "H-Energy";
	private static JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private static List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY, DriveScopes.DRIVE_READONLY);
	
    private static String DIRECTORY_ID = "10yrMF5dUx-8tQvDJOFY_RguB1vXN8PKN";

    /**
     * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
     * globally shared instance across your application.
     */
    private static FileDataStoreFactory dataStoreFactory;

    private static String UPLOAD_FILE_PATH = DIRECTORY_ID;
    private static String DIR_FOR_DOWNLOADS = "C:/Temp";
    private static java.io.File UPLOAD_FILE = new java.io.File(UPLOAD_FILE_PATH); 
	// 인증토큰 저장할 path
	private static final String TOKENS_DIRECTORY_PATH = "tokens";


    /** Directory to store user credentials. */
    private static java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".store/drive_sample");

    /** Global Drive API client. */
    private static Drive drive;
    


//    public static Credential authorize() throws IOException, GeneralSecurityException {
//    	GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(CREDENTIALS_FILE_PATH)).createScoped(SCOPES);
//        return credential;
//    }


    

    /** Authorizes the installed application to access user's protected data. */
    public static Credential authorize(final NetHttpTransport HTTP_TRANSPORT) throws Exception {

		// 1. OAuth 클라이언트 키 파일이 있는지 체크 
		InputStream in = new FileInputStream(CREDENTIALS_FILE_PATH);
		if (in == null) {
			throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
		}
System.out.println("===============>>> line 73");    
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

System.out.println("===============>>> line 76");   
		// 2. 유저 인증 플로우 생성 및 인증토큰 가져오기 
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES)
						.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
						.setAccessType("offline")
						.build();

System.out.println("===============>>> line 84");   
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(9090).build();

System.out.println("===============>>> line 87");   
		Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

System.out.println("===============>>> line 90");   
		return credential;
		
    }
    
    

    
    public static void main(String[] args) throws GeneralSecurityException, IOException, Exception {
    	
    	
//        Preconditions.checkArgument(
//                !UPLOAD_FILE_PATH.startsWith("Enter ") && !DIR_FOR_DOWNLOADS.startsWith("Enter "),
//                "Please enter the upload file path and download directory in %s", GoogleDriveServiceTest.class);

        try {

			final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        	
            // authorization 
            Credential credential = authorize(HTTP_TRANSPORT);
            // set up the global Drive instance
            drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(
                APPLICATION_NAME).build();
            
//            FileList list = drive.files().list()
//            		.setQ("name='공유스프레드시트1'")
//                    .setCorpora("allDrives")
//                    .setIncludeItemsFromAllDrives(true)
//                    .setSupportsAllDrives(true)
////                    .setIncludeTeamDriveItems(true)
////                    .setSupportsTeamDrives(true)
//                    .setPageSize(10)
//                    .setFields("nextPageToken, files(id, name, owners, parents, createdTime, description, mimeType, "
//                            + "webViewLink, webContentLink, fileExtension, iconLink, thumbnailLink, driveId, originalFilename)")
//                    .setPageToken(TOKENS_DIRECTORY_PATH).execute();
            
            FileList list = drive.files().list()
                    .setPageSize(10)
                    .setFields("nextPageToken, files(id, name)")
                    .execute();
//            pageToken = list.getNextPageToken();
           
            for(Object file : list.getFiles()) {
                System.out.print("file name: " + file);                    
            }
           
            System.out.print("===============>>> connected !!!");         
            
        } catch (Exception e) {
           	e.printStackTrace();
        }
        


    }
}
