package graph_encryption.domain.client;

import graph_encryption.domain.model.ImageInfo;
import graph_encryption.domain.model.ImageInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ImageInfoMapper {
    long countByExample(ImageInfoExample example);

    int deleteByExample(ImageInfoExample example);

    int deleteByPrimaryKey(Integer imageId);

    int insert(ImageInfo record);

    int insertSelective(ImageInfo record);

    List<ImageInfo> selectByExampleWithRowbounds(ImageInfoExample example, RowBounds rowBounds);

    List<ImageInfo> selectByExample(ImageInfoExample example);

    ImageInfo selectByPrimaryKey(Integer imageId);

    int updateByExampleSelective(@Param("record") ImageInfo record, @Param("example") ImageInfoExample example);

    int updateByExample(@Param("record") ImageInfo record, @Param("example") ImageInfoExample example);

    int updateByPrimaryKeySelective(ImageInfo record);

    int updateByPrimaryKey(ImageInfo record);
}