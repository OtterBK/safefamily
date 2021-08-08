package hanium.oldercare.oldercareservice.handlermessage;

public enum RegisterMessage {

    ALREADY_USE_ID, CANNOT_USE_ID, CAN_USE_ID, ID_LENGTH_LOW, ID_INIT, PW_LENGTH_LOW,
    ALREADY_USE_EMAIL, SEND_CODE_SUCCESS, CODE_TIMEOUT, CAN_USE_EMAIL, CANNOT_USE_EMAIL, SEND_CODE_FAIL,
    CODE_NULL, CODE_CONSISTENT, CODE_INCONSISTENT, REGISTER_DONE, REGISTER_FAIL,
    SENDING_PROGRESS, SENDING_DONE, FIND_ID_SUCCEED, FIND_ID_FAIL, NO_USER_INFO, PASSWORD_RESET_SUCCEED, PASSWORD_RESET_FAIL

}
