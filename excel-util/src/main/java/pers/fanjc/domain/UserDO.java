package pers.fanjc.domain;

import pers.fanjc.annotation.ExcelHead;
import pers.fanjc.annotation.ExcelSheet;

import java.io.Serializable;
import java.util.Date;

@ExcelSheet("USERINFO")
public class UserDO implements Serializable {
    private final static Long serialVersionUid = 1L;

    @ExcelHead("ID")
    private String id;
    @ExcelHead("USERNAME")
    private String username;
    @ExcelHead("PASSWORD")
    private String password;
    @ExcelHead(value = "SEX", dict = "{\"0\":\"女\",\"1\":\"男\"}")
    private String sex;
    @ExcelHead("STATUS")
    private Integer status;
    @ExcelHead("POWER")
    private Double power;
    @ExcelHead("REMARK")
    private String remark;
    @ExcelHead("CREATETIME")
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getPower() {
        return power;
    }

    public void setPower(Double power) {
        this.power = power;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
