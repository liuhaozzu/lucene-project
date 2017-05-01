package com.liuhaozzu.lucene.util;


import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Collection;

/**
 * 分页对象
 *
 * @author meiguiyang
 * @version [版本号, Feb 22, 2013]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class PageUtil implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -123356884225428174L;

    /**
     * 页面的默认大小pagesize
     */
    private static final int PAGE_SIZE = 15;

    // 当前页数
    private int currentIndex = 1;

    // 偏移量,默认展示15条数据
    private int pageSize = PAGE_SIZE;

    // 总数据量
    private int totalRecords = 0;

    // 总页数
    private int totalPages = 0;

    // 前一页页码
    private int previous;

    // 后一页页码
    private int next;

    // 当前页封装的数据
    private Collection<? extends Object> collection;

    // // 临近的页码
    // private List<Integer> nearest;

    /**
     * 获取当前页数<br />
     * 默认值为1 当前页数为空或小于1时,默认第一页<br />
     * 当前页数大于最大页数时,页数为最大页<br />
     *
     * @return 当前页数
     */
    public int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * 配置当前页数、目标页数
     *
     * @param currentIndex 当前页数,目标页数
     */
    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    /**
     * 获取总记录数<br />
     * 未赋值时,结果为NULL
     *
     * @return 总记录数
     */
    public int getTotalRecords() {
        return totalRecords;
    }

    /**
     * 配置总记录数
     *
     * @param totalRecords 总记录数
     */
    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
        this.calc();
    }

    /**
     * 获取总页数
     *
     * @return 总页数
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * 配置总页数,函数由自身调用
     *
     * @param totalPages 总页数
     */
    private void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * 获取前一页的页码
     *
     * @return 前一页页面
     */
    public int getPrevious() {
        return previous;
    }

    /**
     * 配置前一页页面,函数由自身调用
     *
     * @param previous 前一页页码
     */
    private void setPrevious(int previous) {
        this.previous = previous;
    }

    /**
     * 获取下一页页面
     *
     * @return 下一页页码
     */
    public int getNext() {
        return next;
    }

    /**
     * 配置下一页页码,函数由自身调用
     *
     * @param next 下一页页码
     */
    private void setNext(int next) {
        this.next = next;
    }

    /**
     * 获取第一页页码
     *
     * @return 常量值1
     */
    public int getFirst() {
        return 1;
    }

    /**
     * 获取最后一页页码
     *
     * @return 返回最大页数
     */
    public int getLast() {
        return this.getTotalPages();
    }

    /**
     * 获取查询开始记录
     *
     * @return int
     * @see [类、类#方法、类#成员]
     */
    public int getStartRecord() {
        return (currentIndex - 1) * this.pageSize;
    }

    /**
     * 获取查询结束记录
     *
     * @return int
     * @see [类、类#方法、类#成员]
     */
    public int getEndRecord() {
        return this.getStartRecord() + this.pageSize;
    }

    /**
     * 计算参数
     */
    private void calc() {
        if (totalRecords != 0) {
            // 计算总页数
            int pages =
                    this.totalRecords % this.pageSize == 0 ? this.totalRecords / this.pageSize : this.totalRecords
                            / this.pageSize + 1;
            this.setTotalPages(pages);
            // 处理删除 记忆分页问题
            if (this.totalPages < this.currentIndex) {
                this.currentIndex = this.totalPages;
            }
            // 计算前一页
            this.setPrevious(this.currentIndex - 1);
            if (this.previous < 1) {
                this.setPrevious(1);
            }
            // 计算下一页
            this.setNext(this.currentIndex + 1);
            if (this.next > this.totalPages) {
                this.setNext(this.totalPages);
            }
        }
    }

    public Collection<? extends Object> getCollection() {
        return collection;
    }

    public void setCollection(Collection<? extends Object> collection) {
        this.collection = collection;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * copy srcPage中所有属性覆盖当前对象(使用cache的时候是使用)
     *
     * @param srcPage 源分页对象
     * @see [类、类#方法、类#成员]
     */
    public void copyFrom(PageUtil srcPage) {
        BeanUtils.copyProperties(srcPage, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + currentIndex;
        result = prime * result + next;
        result = prime * result + pageSize;
        result = prime * result + previous;
        result = prime * result + totalPages;
        result = prime * result + totalRecords;
        return result;
    }
}
