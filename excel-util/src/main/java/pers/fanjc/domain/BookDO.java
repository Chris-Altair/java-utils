package pers.fanjc.domain;

import pers.fanjc.annotation.ExcelHead;
import pers.fanjc.annotation.ExcelSheet;

import java.io.Serializable;
import java.util.Date;

@ExcelSheet("BOOKINFO")
public class BookDO implements Serializable {
    private final static Long serialVersionUid = 1L;

    @ExcelHead("ID")
    private String id;
    @ExcelHead("BOOKNAME")
    private String bookname;
    @ExcelHead("AUTHOR")
    private String author;
    @ExcelHead("STATUS")
    private Integer status;
    @ExcelHead("SIZE")
    private Integer size;
    @ExcelHead("CREATETIME")
    private Date createTime;
    @ExcelHead("UPDATETIME")
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
