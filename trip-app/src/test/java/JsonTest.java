import com.cppteam.common.util.TripResult;
import com.google.gson.Gson;
import org.junit.Test;

/**
 * Created by happykuan on 2017/10/31.
 */
public class JsonTest {
    @Test
    public void testJson() {
        TripResult result = TripResult.ok("ok", TripResult.ok());
        String json = new Gson().toJson(result, TripResult.class);
        System.err.println(json);

        // -----------------------

        TripResult result1 = new Gson().fromJson(json, TripResult.class);
        System.out.println(result1.getData());
//        String data = (String)result1.getData();
//        System.err.print(data);
        String result2 = new Gson().toJson(result1.getData());

        System.out.println(result2);
    }
}
