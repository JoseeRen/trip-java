import com.cppteam.common.util.JWTUtil;
import org.junit.Test;

/**
 * Created by happykuan on 2017/11/3.
 */
public class JWTTest {
    @Test
    public void test() {
        String id = JWTUtil.validToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjYxODk4M2I0LWMwMmYtNDNjYi1hYjIzLWVjNzFhMWU1MTI3NSJ9.8561d4dae0fe257ed1ff3dce8cf0c1198483e750c46d219f499f0d554d8204dc");
        System.out.println(id);
    }
}
