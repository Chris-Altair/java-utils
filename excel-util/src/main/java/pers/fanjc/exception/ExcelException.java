package pers.fanjc.exception;

public class ExcelException extends RuntimeException {
    /** 错误码 */
    private String errorCode;
    /** 错误上下文 */
    private String errorMag;

    public ExcelException(String errorCode, String errorMsg){
        super(errorMsg);
        this.errorCode = errorCode;
    }
    public ExcelException(ExcelExceptionEnum orderExceptionEnum){
        super(orderExceptionEnum.getErrorMsg());
        this.errorCode = orderExceptionEnum.getErrorCode();
    }

    public ExcelException(String errorCode, String errorMsg,Throwable throwable){
        super(errorMsg,throwable);
        this.errorCode = errorCode;
    }
    public ExcelException(ExcelExceptionEnum orderExceptionEnum,Throwable throwable){
        super(orderExceptionEnum.getErrorMsg(),throwable);
        this.errorCode = orderExceptionEnum.getErrorCode();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMag() {
        return errorMag;
    }

    public void setErrorMag(String errorMag) {
        this.errorMag = errorMag;
    }
}
