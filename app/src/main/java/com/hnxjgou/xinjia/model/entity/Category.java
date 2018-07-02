package com.hnxjgou.xinjia.model.entity;

import java.util.List;

/**
 * 分类的类别实体类
 */
public class Category {

    public int CategoryId; // 类别ID
    public List<Category> NodeList; // 子类别列表
    public String CategoryName; // 类别名称
    public String FatherName; // 父类别名称，如果已经是顶级类别则为空。
    public String FatherId; // 父类别ID。如果已经是顶级类别则为0。
    public List<Business> Business; // 商家列表。
    public int CategoryType; // 节点类型。1：根节点  2 叶子节点
    public int Sort; //  排序，由服务器排序。
}
