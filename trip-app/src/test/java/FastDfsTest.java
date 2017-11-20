import com.cppteam.common.util.ImageUtils;
import com.cppteam.common.util.FastDFSClient;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import java.io.*;

/**
 * Created by happykuan on 2017/10/28.
 */
public class FastDfsTest {

    @Test
    public void downloadImg() {
//        uploadImg();
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://192.168.229.133/group1/M00/00/00/wKjlhVn0mReAaL0pAA99th8ZtWc370.jpg");

        InputStream inputStream = null;
        CloseableHttpResponse response = null;
        try {
            response = client.execute(httpGet);
            System.out.println(response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();

            int index;
            byte[] bytes = new byte[1024];
            FileOutputStream fos = new FileOutputStream("d:/hello.jpg");
            while((index = stream.read(bytes)) != -1) {
                fos.write(bytes, 0, index);
                fos.flush();
            }
            fos.close();
            stream.close();

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void uploadImg() {
        try {
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:properties/fastdfs-client.properties");
            File f = new File("C:\\Users\\Kim\\Pictures\\22-160R5140330159.jpg");
            ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
            BufferedInputStream in = null;

            in = new BufferedInputStream(new FileInputStream(f));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            byte[] byteArray = bos.toByteArray();

            String s = fastDFSClient.uploadFile(byteArray, "jpg");
            System.out.println(s);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void str() {
        String str = "hello.jpg";
        System.out.println(str.substring(str.lastIndexOf(".") + 1, str.length()));
    }

//    @Test
//    public void delete() {
//        String fileId = "/group1/M00/00/00/wKjlhVn0mReAaL0pAA99th8ZtWc370.jpg";
////        int i = ImageUtils.updateAvatar(fileId, null);
//        System.out.println(i);
//    }

    @Test
    public void upload() {
        String url = "https://ss3.baidu.com/-rVXeDTa2gU2pMbgoY3K/it/u=331642910,199523927&fm=202&mola=new&crop=v1";
        String fileName = ImageUtils.saveImage(url);
        System.out.print(fileName);
    }

    @Test
    public void delete() {
        int i = ImageUtils.deleteImage("group1/M00/00/00/wKjlhVn3nkqAQnBRAA99th8ZtWc950.jpg");
        System.out.print(i);
    }
}
