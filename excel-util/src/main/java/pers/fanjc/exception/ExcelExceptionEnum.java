package pers.fanjc.exception;

public enum ExcelExceptionEnum {
    /** 文件格式异常 */
    FILE_FORMAT_EXCEPTION("EU001","文件格式异常"),
    /** 没有sheet注解 */
    NO_FIND_EXCEL_SHEET_EXCEPTION("EU002","未找到class的ExcelSheet注解")
    ;

    private String errorCode;
    private String errorMsg;

    ExcelExceptionEnum(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
