package com.github.computer114514.mapper;

import com.github.computer114514.pojo.Cat;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CatMapper {

    @Select("select * from cat")
    public List<Cat> searchCatsList();

    @Insert("Insert into cat (user_id, cat_id, name, image_url) VALUES (133,#{id},#{name},#{url})")
    public void InsertCat(String url,String id,String name);

    @Delete("delete from cat where cat_id=#{id}")
    void delCat(String catId);
}
