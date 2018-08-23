package com.casting.commonmodule.network.base;

public interface NetworkConstant {

    String ResponseCode = "ResponseCode";
    String ResponseMessage = "ResponseMessage";
    String HttpPost = "POST";
    String HttpGet = "GET";
    String HttpPut = "PUT";
    String HttpDelete = "DELETE";
    String ParsingError = "파싱에러";
    String HttpConnectionError = "Http Connection 오류";

    enum NetworkProtocolTag {
        REQUEST_SERVERSTATE,
        REQUEST_MEMBERSCORE,
        REPORT_MEMBER_INFO,
        REQUEST_DAY_SELECTIONS,
        REQUEST_DAY_SELECTION_WORDS,
        REQUEST_QUIZ_WORDLIST,

        REPORT_QUIZ_WORD_SCORE,
        REPORT_PAYPOINT_LOG,

        PURCHASE_CARDWORDSTACK;
    }

    enum SuccessCode {
        SUCCESS(0 , "DEFAULT_SUCCESS"),
        SUCCESS_LOGIN(SUCCESS.getSuccessCode()+100 , "로그인  성공"),
        SUCCESS_REGISTER(SUCCESS.getSuccessCode()+110 , "회원가입 성공"),
        SUCCESS_DAYSELECTIONS(SUCCESS.getSuccessCode()+210 , "과목별 영단어 학습 가능한 일자 리스트 조회 성공"),
        SUCCESS_WORDDETAILED(SUCCESS.getSuccessCode()+221, "영단어 상세정보 조회 성공"),
        SUCCESS_WORDREGISTER(SUCCESS.getSuccessCode()+222, "영단어 등록 성공"),
        SUCCESS_DAYSELECTIONREGISTER(SUCCESS.getSuccessCode()+223, "일자별 섹션 등록 성공"),
        SUCCESS_WORDLIST(SUCCESS.getSuccessCode()+220 , "일자별 영단어 리스트 조회 성공"),
        SUCCESS_CONFIRM_MEMBER_SCORE(SUCCESS.getSuccessCode()+230 , "회원 성적 조회 성공"),
        SUCCESS_OPEN_DOWNLOAD_SOCKET(SUCCESS.getSuccessCode()+310 , "음원 다운로드 소켓 연결 성공");

        private int 	mSuccessCode;
        private String 	mSuccessMsg;

        SuccessCode(int successCode , String strSuccessMsg) {
            mSuccessCode = successCode;
            mSuccessMsg = strSuccessMsg;
        }

        public String getMessage() {
            return mSuccessMsg;
        }

        public int getSuccessCode() {
            return mSuccessCode;
        }
    }

    enum ErrorCode {
        ERROR(-1 , "DEFAULT_ERROR"),
        LOGIN_FAIL(-100 , "로그인에 실패하였습니다."),
        LOGIN_ERROR(-101 , "로그인에 실패하였습니다."),
        REGISTER_MEMBER_FAIL(-110 , "회원가입에 실패하였습니다."),
        REGISTER_MEMBER_DUPLICATED(-111 , "중복되는 아이디 입니다."),
        REGISTER_MEMBER_ERROR(-112 , "회원가입 중에 에러가 발생하였습니다."),
        ERROR_SHOW_DAYSELECTIONS(-121, "학습 가능한 영단어 일자 조회중 에러가 발생하였습니다."),
        ERROR_SHOW_WORDLIST(-122 , "학습 가능한 일자별 영단어 리스트 조회중 에러가 발생하였습니다."),
        ERROR_SHOW_EXAM(-122 , "시험 정보를 조회 하는중에 에러가 발생하였습니다."),
        FAIL_SHOW_MEMBER_SCORE(-123 , "회원의 단어 시험 성적이 조회되지 않습니다."),
        ERROR_SHOW_MEMBER_SCORE(-124 , "회원의 단어 시험 성적 조회중에 에러가 발생하였습니다."),
        ERROR_OPEN_DOWNLOAD_SOCKET(-210 , "음원 다운로드 요청중에 에러가 발생하였습니다.");

        private int	mErrorCode;
        private String mStrErrorMsg;

        ErrorCode(int errorCode , String strErrorMessage) {
            mErrorCode = errorCode;
            mStrErrorMsg = strErrorMessage;
        }

        public int getErrorCode() {
            return mErrorCode;
        }

        public String getErrorMsg() {
            return mStrErrorMsg;
        }
    }
}
