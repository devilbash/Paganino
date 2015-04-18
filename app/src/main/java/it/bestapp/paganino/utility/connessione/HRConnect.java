package it.bestapp.paganino.utility.connessione;



import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Entity;
import android.support.v4.app.Fragment;




public class HRConnect {

    public static String SERVER  = "https://www.accenturehrservices.it";
    public static String LOGIN   = "/login.aspx?ReturnUrl=%2facngroup%2fIndex.aspx";
    public static String LISTA   = "/Utility/LoadApp.aspx?appInd=/Services/Allazi/FoglioPagaOnline/Login.aspx&appNum=000";
    public static String BPAGA   = "/Services/Allazi/FoglioPagaOnline/Sito/Modulo.aspx?Tipo=S&Versione=A20<SOST>01&Az=ATS";


    private HttpsURLConnection conn;
    private HttpClient client = null;

    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0";
    boolean loginOk;


    public boolean getLoginCheck(){
        return this.loginOk;
    }


    public HRConnect() {
        loginOk = false;
        client  = new DefaultHttpClient();
    }


    /*
     * login-> restituisco pagina home*/
    public String sendPost(List<NameValuePair> postParams)
            throws Exception {
        URI obj = new URI(SERVER + LOGIN);
        HttpPost post = new HttpPost(obj);

        // add header
        post.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        post.setHeader("Accept-Encoding", "gzip, deflate");
        post.setHeader("Accept-Language", "it-IT,it;q=0.8,en-US;q=0.5,en;q=0.3");
        post.setHeader("Connection", "keep-alive");
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Host", "www.accenturehrservices.it");
        post.setHeader("Referer", "https://www.accenturehrservices.it/login.aspx?ReturnUrl=%2facngroup%2findex.aspx");
        post.setEntity(new UrlEncodedFormEntity(postParams));

        HttpResponse response = client.execute(post);

        int responseCode;
        responseCode = response.getStatusLine().getStatusCode();

        BufferedReader rd = new BufferedReader(new InputStreamReader(response
                .getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }


    /*
     * richiesta generica pagina tramite GET*/
    public String GetPageContent(String des) throws Exception {
        URI obj = new URI(SERVER + des);

        HttpGet request = new HttpGet(obj);
        request.setHeader("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        request.setHeader("Accept-Encoding", "gzip, deflate");
        request.setHeader("Accept-Language",
                "it-IT,it;q=0.8,en-US;q=0.5,en;q=0.3");
        request.setHeader("Cache-Control", "max-age=0");
        request.setHeader("Connection", "keep-alive");
        request.setHeader("Host", "www.accenturehrservices.it");
        request.setHeader("User-Agent", USER_AGENT);

        HttpResponse response = client.execute(request);
        int responseCode = response.getStatusLine().getStatusCode();

        BufferedReader rd = new BufferedReader(new InputStreamReader(response
                .getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }

    /*
     * parser pagina login metodo utility*/
    public List<NameValuePair> getFormParams(String html, String username,
                                             String password) throws UnsupportedEncodingException {

        Document doc = Jsoup.parse(html);
        Element loginform = doc.getElementById("form1");
        Elements inputElements = loginform.getElementsByTag("input");
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        for (Element inputElement : inputElements) {
            String key = inputElement.attr("id");
            String value = inputElement.attr("value");

            if (key.equals("UID"))
                value = username;
            else if (key.equals("pw"))
                value = password;
            else if (key.equals("__VIEWSTATE"))
                value = value;
            else if (key.equals("__EVENTVALIDATION"))
                value = value;
            if (!key.equals("B2"))

                paramList.add(new BasicNameValuePair(key, value));
        }

        return paramList;
    }


    /*
     * verifico autentificazione login */
    public void loginCheck (String page){
        Document doc = Jsoup.parse(page);
        Elements title= doc.head().getElementsByTag("title");
        String titolo = title.text();

        if ( titolo.toString().equalsIgnoreCase("Accenture HR Services") )
            this.loginOk = true;
    }


    public InputStream getPDF(String busta) throws Exception {
        String link = BPAGA.replaceAll("<SOST>",busta);

        URI obj = new URI(SERVER + link);
        HttpGet request = new HttpGet(obj);
        request.setHeader("Accept",	"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        request.setHeader("Accept-Encoding", "gzip, deflate");
        request.setHeader("Accept-Language", "it-IT,it;q=0.8,en-US;q=0.5,en;q=0.3");
        request.setHeader("Cache-Control", "max-age=0");
        request.setHeader("Connection", "keep-alive");
        request.setHeader("Host", "www.accenturehrservices.it");
        request.setHeader("User-Agent", USER_AGENT);

        HttpResponse response = client.execute(request);
        int responseCode = response.getStatusLine().getStatusCode();
        InputStream in = response.getEntity().getContent();
        return in;
    }


}
