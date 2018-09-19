import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2017/12/13.
 */

public class md5 {
    public static void main(String[] args) {
        try {
            String fileMD5 = getFileMD5(
//                    "C:\\Users\\Administrator\\Desktop\\APK\\app-release_legu_signed_zipalign.apk");
                    "C:\\Users\\Administrator\\Desktop\\APK\\Android_2.3.1.2.apk");
//                    "C:\\Users\\Administrator\\Desktop\\APK\\app-release_legu_ShunLianPower_signed_zipalign.apk");
            System.out.println("md5:"+fileMD5);
            // TODO: 2017/6/10 0010 1f6c334c01a63d94062fa42778b1be38
            //1f6c334c01a63d94062fa42778b1be38
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取文件的md5值
     * @param path 文件路径
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String getFileMD5(String path) throws NoSuchAlgorithmException, IOException {

        File file = new File(path);

        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest;
        FileInputStream in;
        byte buffer[] = new byte[1024];
        int len;
        digest = MessageDigest.getInstance("MD5");
        in = new FileInputStream(file);
        while ((len = in.read(buffer, 0, 1024)) != -1) {
            digest.update(buffer, 0, len);
        }
        in.close();
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }
}
