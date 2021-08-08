package hanium.oldercare.oldercareservice.apinetwork;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orhanobut.logger.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;
import java.util.Map;

public class MyRequestUtility {

    private static String url_db_root = "https://0fabhffmo9.execute-api.ap-northeast-2.amazonaws.com/sw_db";
    private static String url_mail_root = "https://jva5bsamyd.execute-api.ap-northeast-2.amazonaws.com/sw_mail";

    /*
    return: id값과 일치하는 데이터
    param{
        String id: 찾을 id
    }
     */
    public static int getCompareIdAmount(String id) throws Exception {

        HashMap<String, Object> requestData = new HashMap();
        requestData.put("requestType", "exist_id");

        HashMap<String, Object> param = new HashMap();
        param.put("id", id);
        requestData.put("param", param);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requestData);
        String msgMap = HttpConnectionUtility.sendPostREST(url_db_root+"/existid", json);

        Logger.i(msgMap);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse( msgMap );
        JSONObject jsonObj = (JSONObject) obj;

        String resultSet = (String) jsonObj.get("result");

        int cnt = Integer.parseInt(resultSet.toString());

        return cnt;
    }

    /*
    return: email값과 일치하는 데이터
    param{
        String email: 찾을 email
    }
     */
        public static int getCompareEmailAmount(String email) throws Exception {


            HashMap<String, Object> requestData = new HashMap();
            requestData.put("requestType", "exist_email");

            HashMap<String, Object> param = new HashMap();
            param.put("email", email);
            requestData.put("param", param);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(requestData);
            String msgMap = HttpConnectionUtility.sendPostREST(url_db_root+"/existemail", json);

            Logger.i(msgMap);

            JSONParser parser = new JSONParser();
            Object obj = parser.parse( msgMap );
            JSONObject jsonObj = (JSONObject) obj;

            String resultSet = (String) jsonObj.get("result");

            int cnt = Integer.parseInt(resultSet.toString());

            return cnt;
        }

    /*
    return: VOID
    param{
        String email: 인증코드를 보낼 email주소
    }
     */
    public static void requestEmailVerify(String email) throws Exception {


        HashMap<String, Object> requestData = new HashMap();
        requestData.put("requestType", "email_verify");

        HashMap<String, Object> param = new HashMap();
        param.put("email", email);
        requestData.put("param", param);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requestData);
        String msgMap = HttpConnectionUtility.sendPostREST(url_mail_root+"/sendcode", json);

        Logger.i(msgMap);
    }

    /*
    return: boolean
    param{
        String email: 인증코드를 받은 email 주소
        String code: 입력한 인증코드
    }
     */
    public static boolean verifyCodeComapre(String email, String code) throws Exception {

        HashMap<String, Object> requestData = new HashMap();
        requestData.put("requestType", "code_compare");

        HashMap<String, Object> param = new HashMap();
        param.put("email", email);
        param.put("code", code);
        requestData.put("param", param);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requestData);
        String msgMap = HttpConnectionUtility.sendPostREST(url_mail_root+"/comparecode", json);

        Logger.i(msgMap);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse( msgMap );
        JSONObject jsonObj = (JSONObject) obj;

        String resultSet = (String) jsonObj.get("result");

        String resultMsg = String.valueOf(resultSet).trim();
        resultMsg = resultMsg.replace("\"","");
        if(resultMsg.equals("CODE_CONSISTENT")){
            return true;
        } else {
            return false;
        }
    }

    /*
    return: boolean
    param{
        String id: 가입할 id
        String pw: 가입할 pw
        String email: 가입할 email
    }
     */
    public static void register(String id, String pw, String email, String address, String nickname, String age) throws Exception {

        HashMap<String, Object> requestData = new HashMap();
        requestData.put("requestType", "register");

        HashMap<String, Object> param = new HashMap();
        param.put("id", id);
        param.put("pw", pw);
        param.put("email", email);
        param.put("address", address);
        param.put("nickname", nickname);
        param.put("age", age);

        requestData.put("param", param);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requestData);
        String msgMap = HttpConnectionUtility.sendPostREST(url_db_root+"/register", json);

        Logger.i(msgMap);

    }


    /*
    id, pw 를 사용해 서버에서 로그인
    
    return: boolean
    param{
        String id: 입력한 id
        String pw: 입력한 비밀번호
    }
     */
    public static boolean instantLogin(String id, String pw) throws Exception {

        HashMap<String, Object> requestData = new HashMap();
        requestData.put("requestType", "instant_login"); //요청 타입 설정

        HashMap<String, Object> param = new HashMap(); //파라미터 설정
        param.put("id", id);
        param.put("pw", pw);
        requestData.put("param", param);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requestData);
        String msgMap = HttpConnectionUtility.sendPostREST(url_db_root+"/instant_login", json); //기능 요청 주소

        Logger.i(msgMap);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse( msgMap );
        JSONObject jsonObj = (JSONObject) obj;

        String resultSet = (String) jsonObj.get("result"); //응답 저장

        String resultMsg = String.valueOf(resultSet).trim();
        resultMsg = resultMsg.replace("\"","");
        if(resultMsg.equals("LOGIN_SUCCEED")){
            return true;
        } else {
            return false;
        }
    }


    /*
    email 을 통해 id 찾기

    return: String
    param{
        String email: 입력한 email
    }
     */
    public static String find_id(String email) throws Exception {

        HashMap<String, Object> requestData = new HashMap();
        requestData.put("requestType", "find_id"); //요청 타입 설정

        HashMap<String, Object> param = new HashMap(); //파라미터 설정
        param.put("email", email);
        requestData.put("param", param);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requestData);
        String msgMap = HttpConnectionUtility.sendPostREST(url_db_root+"/find_id", json); //기능 요청 주소

        Logger.i(msgMap);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse( msgMap );
        JSONObject jsonObj = (JSONObject) obj;

        String resultSet = (String) jsonObj.get("result"); //응답 저장

        String resultMsg = String.valueOf(resultSet).trim();
        resultMsg = resultMsg.replace("\"","");
        if(resultMsg.equals("NO_MATCH")){
            return null;
        } else {
            return resultMsg;
        }
    }

    /*
    비밀번호 리셋 요청

    return: boolean
    param{
        String email: 리셋할 계정의 email
    }
     */
    public static boolean resetPassword(String email) throws Exception {

        HashMap<String, Object> requestData = new HashMap();
        requestData.put("requestType", "reset_password"); //요청 타입 설정

        HashMap<String, Object> param = new HashMap(); //파라미터 설정
        param.put("email", email);
        requestData.put("param", param);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requestData);
        String msgMap = HttpConnectionUtility.sendPostREST(url_mail_root+"/resetpassword", json); //기능 요청 주소

        Logger.i(msgMap);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse( msgMap );
        JSONObject jsonObj = (JSONObject) obj;

        String resultSet = (String) jsonObj.get("result"); //응답 저장

        String resultMsg = String.valueOf(resultSet).trim();
        resultMsg = resultMsg.replace("\"","");
        if(resultMsg.equals("RESET_SUCCEED")){
            return true;
        } else {
            return false;
        }
    }

}
