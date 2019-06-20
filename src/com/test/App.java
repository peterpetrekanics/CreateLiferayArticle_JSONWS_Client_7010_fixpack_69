package com.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class App {

    /**
     * CHANGE WITH CORRECT VALUE
     */
    private static final String PORTAL_URL = "http://localhost:8080";
    private static final String URL_ADD_ARTICLE7010 = "/api/jsonws/journal.journalarticle/add-article";
    private static final String URL_ADD_FILE_ENTRY7010 = "/api/jsonws/dlapp/add-file-entry";
    private static final String LOGIN = "test@liferay.com";
    private static final String PASSWORD = "test";
    private static final long GROUP_ID = 20142;
    private static final String DDM_STRUCTURE_KEY = "36675";
    private static final String DDM_TEMPLATE_KEY = "36679";
    private static final String FILEPATH = "/home/peterpetrekanics/dev/eclipse_neon3/java_desktop_workspace/DocAddTest/resources/";
    private static final String FILENAME = "screenshot.png";
    public String folderId;
    public String uuid;
    MultipartEntity mEntity;
    

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.addFileEntry();
        app.addArticle();
    }

	public void addFileEntry() throws Exception {
		HttpPost httpPost = new HttpPost(URL_ADD_FILE_ENTRY7010);
    	mEntity = new MultipartEntity();
    	setFileEntities(httpPost);
    	String response = executeRequest(httpPost);
        JSONObject responseObject = new JSONObject(response);
        folderId = responseObject.get("folderId").toString();
        uuid = responseObject.get("uuid").toString();
	}

	public void setFileEntities(HttpPost httpPost) throws Exception {
		final File file = new File(FILEPATH+ FILENAME);
		
		mEntity.addPart("sourceFileName", new StringBody(FILENAME));
		mEntity.addPart("mimeType", new StringBody("image/png"));
		mEntity.addPart("title", new StringBody("screenshot"));
		mEntity.addPart("changeLog", new StringBody("1.0"));
		mEntity.addPart("description", new StringBody("screenshot"));
		mEntity.addPart("repositoryId", new StringBody("20142"));
		mEntity.addPart("folderId", new StringBody("0"));
		// use the File parameter instead of Bytes:
		mEntity.addPart("file",new FileBody(file));

		httpPost.setEntity(mEntity);
	}

	private void addArticle() throws Exception {
		HttpPost httpPost = new HttpPost(URL_ADD_ARTICLE7010);
        setArticleEntities(httpPost);
        String response = executeRequest(httpPost);
	}

    private void setArticleEntities(HttpPost httpPost) throws Exception {
        String[] names = new String[] {
                "groupId",
                "folderId",
                "classNameId",
                "classPK",
                "articleId",
                "autoArticleId",
                "titleMap",
                "descriptionMap",
                "content",
                "type",
                "ddmStructureKey",
                "ddmTemplateKey",
                "layoutUuid",
                "displayDateMonth",
                "displayDateDay",
                "displayDateYear",
                "displayDateHour",
                "displayDateMinute",
                "expirationDateMonth",
                "expirationDateDay",
                "expirationDateYear",
                "expirationDateHour",
                "expirationDateMinute",
                "neverExpire",
                "reviewDateMonth",
                "reviewDateDay",
                "reviewDateYear",
                "reviewDateHour",
                "reviewDateMinute",
                "neverReview",
                "indexable",
                "smallImage",
                "smallImageURL",
                "smallFile",
                "images",
                "articleURL",
                "serviceContext"
        };

        String titleMap = "{\"en_US\":\"Title SG\"}";
        String descriptionMap = "{\"en_US\":\"Description SG\"}";

        // Using the file details of the file that was uploaded earlier:
        String content = "<root available-locales=\"en_US\" default-locale=\"en_US\"><dynamic-element "
                + "instance-id=\"random\" language-id=\"en_US\"  name=\"image\" type=\"image\" index-type=\"text\"><dynamic-content "
                + "id=\"38897\">documents/" + GROUP_ID + "/"+ folderId + "/"+ FILENAME + "/"+ uuid
                + "</dynamic-content></dynamic-element></root>";

        Calendar calendar = Calendar.getInstance();
        int displayDateMonth = calendar.get(Calendar.MONTH);
        int displayDateDay = calendar.get(Calendar.DAY_OF_MONTH-1);
        int displayDateYear = calendar.get(Calendar.YEAR);
        int displayDateHour = calendar.get(Calendar.HOUR_OF_DAY);
        int displayDateMinute = calendar.get(Calendar.MINUTE);

        // Using an empty value for images because the file was already uploaded earlier with the addFileEntry method:
        String images = "{}";

        String serviceContext = "{\"addGroupPermissions\":false" +
                ",\"addGuestPermissions\":false" +
                ", \"scopeGroupId\":\"" + GROUP_ID + "\"}";

        Object[] values = new Object[] {
                GROUP_ID,           // groupId
                0,                  // folderId
                0,                  // classNameId
                0,                  // classPK
                0,                  // articleId
                true,               // autoArticleId
                titleMap,           // titleMap
                descriptionMap,     // descriptionMap
                content,            // content
                "general",          // type
                DDM_STRUCTURE_KEY,  // ddmStructureKey
                DDM_TEMPLATE_KEY,   // ddmTemplateKey
                "",                 // layoutId
                displayDateMonth,   // displayDateMonth
                displayDateDay,     // displayDateDay
                displayDateYear,    // displayDateYear
                displayDateHour,    // displayDateHour
                displayDateMinute,  // displayDateMinute
                0,                  // expirationDateMonth
                0,                  // expirationDateDay
                0,                  // expirationDateYear
                0,                  // expirationDateHour
                0,                  // expirationDateMinute
                true,               // neverExpire
                0,                  // reviewDateMonth
                0,                  // reviewDateDay
                0,                  // reviewDateYear
                0,                  // reviewDateHour
                0,                  // reviewDateMinute
                true,               // neverReview
                true,               // indexable
                false,              // smallImage
                "",                 // smallImageURL
                null,               // smallFile
                images,             // images
                "",                 // articleURL
                serviceContext      // serviceContext
        };

        httpPost.setEntity(getMultipartEntity(names, values));
    }

    private MultipartEntity getMultipartEntity(String[] names, Object[] values)
            throws Exception {

        MultipartEntity multipartEntity = new MultipartEntity();

        for (int i = 0; i < names.length; i++) {
            multipartEntity.addPart(names[i], getStringBody(values[i]));
        }

        return multipartEntity;
    }

    private ContentBody getStringBody(Object value) throws Exception {
        return new StringBody(String.valueOf(value), Charset.defaultCharset());
    }

    private String executeRequest(HttpRequest request) throws Exception {
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        URL url = new URL(PORTAL_URL);
        HttpHost httpHost = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());

        CredentialsProvider credentialsProvider = defaultHttpClient.getCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope(url.getHost(), url.getPort()),
                new UsernamePasswordCredentials(LOGIN, PASSWORD));

        BasicAuthCache basicAuthCache = new BasicAuthCache();
        BasicScheme basicScheme = new BasicScheme();
        basicAuthCache.put(httpHost, basicScheme);
        BasicHttpContext basicHttpContext = new BasicHttpContext();
        basicHttpContext.setAttribute(ClientContext.AUTH_CACHE, basicAuthCache);

        return defaultHttpClient.execute(httpHost, request, new StringHandler(), basicHttpContext);
    }

    private class StringHandler implements ResponseHandler<String> {
        @Override
        public String handleResponse(HttpResponse response) throws IOException {
            checkStatusCode(response.getStatusLine());
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity == null) {
                return null;
            }
            return EntityUtils.toString(httpEntity);
        }

        protected void checkStatusCode(StatusLine statusLine) throws HttpResponseException {
            if (statusLine.getStatusCode() != 200) {
                throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
            }
        }
    }
}