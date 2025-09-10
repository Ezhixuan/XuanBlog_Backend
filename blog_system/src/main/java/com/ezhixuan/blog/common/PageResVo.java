package com.ezhixuan.blog.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageResVo<T> {

  /** 总记录数 */
  private long totalRow;

  /** 数据 */
  private List<T> list;

  /** 总页数 */
  private long totalPage;

  public PageResVo(List<T> list) {
    this.list = list;
    this.totalRow = list.size();
    this.totalPage = 1;
  }

  public PageResVo() {
    this.totalRow = 0;
    this.totalPage = 1;
    this.list = new ArrayList<>();
  }

  public PageResVo(IPage<T> page) {
    this.list = page.getRecords();
    this.totalPage = page.getPages();
    this.totalRow = page.getTotal();
  }
}
