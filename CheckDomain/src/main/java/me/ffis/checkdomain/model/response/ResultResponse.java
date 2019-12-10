package me.ffis.checkdomain.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by fanfan on 2019/12/05.
 */

@Data
@ToString
@NoArgsConstructor
public class ResultResponse {

    //操作是否成功
    private boolean flag;
    //操作代码
    private Integer code;
    //提示信息
    private String message;

    public ResultResponse(boolean flag, Integer code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    public ResultResponse(Result result) {
        this.flag = result.flag();
        this.code = result.code();
        this.message = result.message();
    }

}
