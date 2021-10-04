package hanium.oldercare.oldercareservice.apinetwork;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.orhanobut.logger.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;

import java.util.HashMap;


import hanium.oldercare.oldercareservice.utility.ParseManager;

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
    public static boolean verifyCodeCompare(String email, String code) throws Exception {

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
    public static void register(String id, String pw, String email, String nickname, String phone) throws Exception {

        HashMap<String, Object> requestData = new HashMap();
        requestData.put("requestType", "register");

        HashMap<String, Object> param = new HashMap();
        param.put("id", id.trim());
        param.put("pw", pw.trim());
        param.put("email", email.trim());
        param.put("nickname", nickname.trim());
        param.put("phone", phone.trim());

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

    /*
        비밀번호 변경 요청

        return: boolean
        param{
            String id: 로그인된 사용자 id
            String new_pw: 입력한 비밀번호
        }
         */
    public static boolean editPassword(String id, String new_pw) throws Exception {

        HashMap<String, Object> requestData = new HashMap();
        requestData.put("requestType", "user_password_update"); //요청 타입 설정

        HashMap<String, Object> param = new HashMap(); //파라미터 설정
        param.put("id", id);
        param.put("pw", new_pw);
        requestData.put("param", param);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requestData);
        String msgMap = HttpConnectionUtility.sendPostREST(url_db_root+"/user_password_update", json); //기능 요청 주소

        Logger.i(msgMap);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse( msgMap );
        JSONObject jsonObj = (JSONObject) obj;

        String resultSet = (String) jsonObj.get("result"); //응답 저장

        String resultMsg = String.valueOf(resultSet).trim();
        resultMsg = resultMsg.replace("\"","");
        if(resultMsg.equals("PW_UPDATE_SUCCEED")){
            return true;
        } else {
            return false;
        }
    }

    /*최지혜:2021-09-27
       사용자 정보 변경 요청

       return: boolean
       param{
            String id: 로그인된 id
            String pw: 변경할 pw
            String nickName: 변경할 nickName
            String phoneNumber: 변경할 phoneNumber

       }
        */
    public static boolean editUserInfo(String id, String pw, String nickName, String phoneNumber, String new_pw) throws Exception {

        HashMap<String, Object> requestData = new HashMap();
        requestData.put("requestType", "user_info_update"); //요청 타입 설정

        HashMap<String, Object> param = new HashMap(); //파라미터 설정
        param.put("id", id);
        param.put("pw", pw);
        param.put("nickname", nickName.trim());
        param.put("phone", phoneNumber.trim());
        param.put("new_pw", new_pw.trim());

        requestData.put("param", param);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requestData);
        String msgMap = HttpConnectionUtility.sendPostREST(url_db_root+"/user_info/update", json); //기능 요청 주소

        Logger.i(msgMap);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse( msgMap );
        JSONObject jsonObj = (JSONObject) obj;

        String resultSet = (String) jsonObj.get("result"); //응답 저장

        String resultMsg = String.valueOf(resultSet).trim();
        resultMsg = resultMsg.replace("\"","");
        if(resultMsg.equals("UserInfo_UPDATE_SUCCEED")){
            return true;
        } else {
            return false;
        }
    }
    /*최지혜:2021-09-27
    로그인 정보를 이용해 사용자 정보 가져오기

    return: String
    param{
        String ID: 로그인된 ID
    }
     */
    public static JSONArray getUserInfo(String id, String pw) throws Exception {

        HashMap<String, Object> requestData = new HashMap();
        requestData.put("requestType", "get_user_info"); //요청 타입 설정

        HashMap<String, Object> param = new HashMap(); //파라미터 설정
        param.put("id", id);
        param.put("pw", pw);
        requestData.put("param", param);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requestData);
        String msgMap = HttpConnectionUtility.sendPostREST(url_db_root+"/user_info/get", json); //기능 요청 주소

        Logger.i(msgMap);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse( msgMap );
        JSONObject jsonObj = (JSONObject) obj;

        String resultStr = (String) jsonObj.get("result");
        JSONArray resultSet = (JSONArray)parser.parse(resultStr);
        JSONArray userInfo = (JSONArray)resultSet.get(0);

        return userInfo;
    }

    /*
    디바이스 목록 가져오기, pw도 맞아야 가져옴

    return: JSONArray
    param{
        String id: 디바이스 id
        String id: 디바이스 pw
    }
     */
    public static JSONArray getDeviceList(String id, String pw) throws Exception {

        HashMap<String, Object> requestData = new HashMap();
        requestData.put("requestType", "get_device_list"); //요청 타입 설정

        HashMap<String, Object> param = new HashMap(); //파라미터 설정
        param.put("id", id);
        param.put("pw", pw);
        requestData.put("param", param);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requestData);
        String msgMap = HttpConnectionUtility.sendPostREST(url_db_root+"/device/get/list", json); //기능 요청 주소

        Logger.i(msgMap);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse( msgMap );
        JSONObject jsonObj = (JSONObject) obj;

        String resultStr = (String)jsonObj.get("result");

        JSONArray result = (JSONArray) parser.parse(resultStr);

        return result;
    }

    /*
    디바이스 정보 가져오기, pw도 맞아야 가져옴
    
    return: boolean
    param{
        String id: 디바이스 id
        String id: 디바이스 pw
    }
     */
    public static JSONArray getDeviceInfo(String id, String pw) throws Exception {

        HashMap<String, Object> requestData = new HashMap();
        requestData.put("requestType", "get_device_info"); //요청 타입 설정

        HashMap<String, Object> param = new HashMap(); //파라미터 설정
        param.put("id", id);
        param.put("pw", pw);
        requestData.put("param", param);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requestData);
        String msgMap = HttpConnectionUtility.sendPostREST(url_db_root+"/device/get/info", json); //기능 요청 주소

        Logger.i(msgMap);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse( msgMap );
        JSONObject jsonObj = (JSONObject) obj;

        String resultStr = (String)jsonObj.get("result");

        JSONArray result = (JSONArray) parser.parse(resultStr);

        return result;
    }

    public static JSONArray getDoorLogs(String id, String pw) throws Exception {

        HashMap<String, Object> requestData = new HashMap();
        requestData.put("requestType", "get_door_logs"); //요청 타입 설정

        HashMap<String, Object> param = new HashMap(); //파라미터 설정
        param.put("id", id);
        param.put("pw", pw);
        requestData.put("param", param);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requestData);
        String msgMap = HttpConnectionUtility.sendPostREST(url_db_root+"/device/get/door_logs", json); //기능 요청 주소

        Logger.i(msgMap);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse( msgMap );
        JSONObject jsonObj = (JSONObject) obj;

        String resultStr = (String)jsonObj.get("result");

        JSONArray result = (JSONArray) parser.parse(resultStr);

        return result;

    }

    public static JSONArray getSpeakerLogs(String id, String pw) throws Exception {

        HashMap<String, Object> requestData = new HashMap();
        requestData.put("requestType", "get_speaker_logs"); //요청 타입 설정

        HashMap<String, Object> param = new HashMap(); //파라미터 설정
        param.put("id", id);
        param.put("pw", pw);
        requestData.put("param", param);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requestData);
        String msgMap = HttpConnectionUtility.sendPostREST(url_db_root+"/device/get/speaker_logs", json); //기능 요청 주소

        Logger.i(msgMap);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse( msgMap );
        JSONObject jsonObj = (JSONObject) obj;

        String resultStr = (String)jsonObj.get("result");

        JSONArray result = (JSONArray) parser.parse(resultStr);

        return result;
    }

    /*
    id, pw 디바이스 인증

    return: boolean
    param{
        String id: 디바이스 id
        String pw: 디바이스 pw
    }
     */
    public static boolean deviceCredential(String id, String pw) throws Exception {

        HashMap<String, Object> requestData = new HashMap();
        requestData.put("requestType", "device_credential"); //요청 타입 설정

        HashMap<String, Object> param = new HashMap(); //파라미터 설정
        param.put("id", id);
        param.put("pw", pw);
        requestData.put("param", param);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requestData);
        String msgMap = HttpConnectionUtility.sendPostREST(url_db_root+"/device/credential", json); //기능 요청 주소

        Logger.i(msgMap);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse( msgMap );
        JSONObject jsonObj = (JSONObject) obj;

        String resultSet = (String) jsonObj.get("result"); //응답 저장

        String resultMsg = String.valueOf(resultSet).trim();
        resultMsg = resultMsg.replace("\"","");
        if(resultMsg.equals("DEVICE_CREDENTIAL_SUCCEED")){
            return true;
        } else {
            return false;
        }
    }

    /*
    디바이스 정보 설정

    return: boolean
    param{
        String id: id
        String pw: pw
        String name: 대상자 이름
        String age: 대상자 연령
        String address: 대상자 주소
        String desc: 대상자 특이사항
    }
     */
    public static boolean setDeviceProfile(String id, String pw, String name, String age, String address, String desc) throws Exception {

        HashMap<String, Object> requestData = new HashMap();
        requestData.put("requestType", "device_set_profile"); //요청 타입 설정

        HashMap<String, Object> param = new HashMap(); //파라미터 설정
        param.put("id", id);
        param.put("pw", pw);
        param.put("name", name);
        param.put("age", age);
        param.put("address", address);
        param.put("desc", desc);
        requestData.put("param", param);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requestData);
        String msgMap = HttpConnectionUtility.sendPostREST(url_db_root+"/device/set_profile", json); //기능 요청 주소

        Logger.i(msgMap);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse( msgMap );
        JSONObject jsonObj = (JSONObject) obj;

        String resultSet = (String) jsonObj.get("result"); //응답 저장

        String resultMsg = String.valueOf(resultSet).trim();
        resultMsg = resultMsg.replace("\"","");
        if(resultMsg.equals("DEVICE_PROFILE_SUCCEED")){
            return true;
        } else {
            return false;
        }
    }

    public static boolean setDevicePassword(String id, String pw, String new_pw) throws Exception {

        HashMap<String, Object> requestData = new HashMap();
        requestData.put("requestType", "device_edit_password"); //요청 타입 설정

        HashMap<String, Object> param = new HashMap(); //파라미터 설정
        param.put("id", id);
        param.put("pw", pw);
        param.put("new_pw", new_pw);
        requestData.put("param", param);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requestData);
        String msgMap = HttpConnectionUtility.sendPostREST(url_db_root+"/device/set_password", json); //기능 요청 주소

        Logger.i(msgMap);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse( msgMap );
        JSONObject jsonObj = (JSONObject) obj;

        String resultSet = (String) jsonObj.get("result"); //응답 저장

        String resultMsg = String.valueOf(resultSet).trim();
        resultMsg = resultMsg.replace("\"","");
        if(resultMsg.equals("DEVICE_EDIT_PASSWORD_SUCCEED")){
            return true;
        } else {
            return false;
        }
    }

    public static boolean deviceAdd(String user_id, String user_pw, String device_id, String device_pw) throws Exception {

        HashMap<String, Object> requestData = new HashMap();
        requestData.put("requestType", "device_add"); //요청 타입 설정

        HashMap<String, Object> param = new HashMap(); //파라미터 설정
        param.put("user_id", user_id);
        param.put("user_pw", user_pw);
        param.put("device_id", device_id);
        param.put("device_pw", device_pw);
        requestData.put("param", param);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requestData);
        String msgMap = HttpConnectionUtility.sendPostREST(url_db_root+"/device/add", json); //기능 요청 주소

        Logger.i(msgMap);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse( msgMap );
        JSONObject jsonObj = (JSONObject) obj;

        String resultSet = (String) jsonObj.get("result"); //응답 저장

        String resultMsg = String.valueOf(resultSet).trim();
        resultMsg = resultMsg.replace("\"","");
        if(resultMsg.equals("DEVICE_ADD_SUCCEED")){
            return true;
        } else {
            return false;
        }
    }

    public static boolean deviceDelete(String user_id, String user_pw, String device_id) throws Exception {

        HashMap<String, Object> requestData = new HashMap();
        requestData.put("requestType", "device_delete"); //요청 타입 설정

        HashMap<String, Object> param = new HashMap(); //파라미터 설정
        param.put("user_id", user_id);
        param.put("user_pw", user_pw);
        param.put("device_id", device_id);
        requestData.put("param", param);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requestData);
        String msgMap = HttpConnectionUtility.sendPostREST(url_db_root+"/device/delete", json); //기능 요청 주소

        Logger.i(msgMap);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse( msgMap );
        JSONObject jsonObj = (JSONObject) obj;

        String resultSet = (String) jsonObj.get("result"); //응답 저장

        String resultMsg = String.valueOf(resultSet).trim();
        resultMsg = resultMsg.replace("\"","");
        if(resultMsg.equals("DEVICE_DELETE_SUCCEED")){
            return true;
        } else {
            return false;
        }
    }

}
