package szx.entites;

import com.lifeng.f300.common.entites.LpResponse;

import java.util.List;

/**
 * Created by happen on 2017/11/20.
 */

public class SzxTransResponseT<T> {
    public boolean result;
    public String msg;
    public List<T> data;
}
