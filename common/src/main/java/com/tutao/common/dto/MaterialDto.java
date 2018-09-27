package com.tutao.common.dto;

import java.util.List;

/**
 * Created by jingting on 2017/6/8.
 */

public class MaterialDto extends BaseDto {
    private ImageIdNewListData data;

    public ImageIdNewListData getData() {
        return data;
    }

    public void setData(ImageIdNewListData data) {
        this.data = data;
    }

    public static class ImageIdNewListData {
        List<ImageCategoryList> category;

        public List<ImageCategoryList> getCategory() {
            return category;
        }

        public void setCategory(List<ImageCategoryList> category) {
            this.category = category;
        }

        public static class ImageCategoryList {
            private String id;
            private String name;
            private String selectedCategoryId;
            List<MaterialListDto.ImageType> child;

            public String getSelectedCategoryId() {
                return selectedCategoryId;
            }

            public void setSelectedCategoryId(String selectedCategoryId) {
                this.selectedCategoryId = selectedCategoryId;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<MaterialListDto.ImageType> getChild() {
                return child;
            }

            public void setChild(List<MaterialListDto.ImageType> child) {
                this.child = child;
            }
        }
    }

}
