package com.github.computer114514.mapper;

import com.github.computer114514.pojo.Cat;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CatMapper {

    @Select("select * from cat where user_id=#{userId}")
    List<Cat> searchCatsList(Long userId);

    @Insert("Insert into cat (user_id, cat_id, name, image_url) VALUES (#{userId},#{id},#{name},#{url}) ")
    void InsertCat(String url,String id,String name,Long userId);

    @Delete("delete from cat where cat_id=#{catId}  and user_id=#{userId}")
    void delCat(String catId,Long userId);

    @Update("update cat set name=#{cat.name},cat.favorability=cat.favorability+#{cat.favorability}," +
            "pet_count=pet_count+#{cat.petCount}," +
            "walk_count=walk_count+#{cat.walkCount} where cat_id=#{cat.catId} and user_id=#{userId};")
    void updateCat(Cat cat,Long userId);

    @Select("select * from cat where cat_id=#{catId} and user_id=#{userId}")
    Cat getCatById(String catId,Long userId);
}
