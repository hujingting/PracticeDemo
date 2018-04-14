package com.tutao.common.dto;

import java.util.List;

/**
 * Created by xianrui on 16/4/5.
 */
public class MaterialListDto extends BaseDto {

    private List<ImageType> child;

    public List<ImageType> getData() {
        return child;
    }

    public void setData(List<ImageType> data) {
        this.child = data;
    }

    public static class ImageType {
        private String id;
        private String name;
        private List<Image> images;
        private boolean is_new;

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

        public List<Image> getImages() {
            return images;
        }

        public void setImages(List<Image> images) {
            this.images = images;
        }

        public boolean is_new() {
            return is_new;
        }

        public void setIs_new(boolean is_new) {
            this.is_new = is_new;
        }


        public static class Image {
            private String id = "";
            private String name;
            private String url;
            private String big_url;
            private String category_id;
            private String pure_color;
            private String localPath;
            private boolean is_new;

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

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getBig_url() {
                return big_url;
            }

            public void setBig_url(String big_url) {
                this.big_url = big_url;
            }

            public String getCategory_id() {
                return category_id;
            }

            public void setCategory_id(String category_id) {
                this.category_id = category_id;
            }

            public boolean getPure_color() {
                return pure_color.equals("0");
            }

            public void setPure_color(String pure_color) {
                this.pure_color = pure_color;
            }

            public String getLocalPath() {
                return localPath;
            }

            public void setLocalPath(String localPath) {
                this.localPath = localPath;
            }

            public boolean is_new() {
                return is_new;
            }

            public void setIs_new(boolean is_new) {
                this.is_new = is_new;
            }


            @Override
            public boolean equals(Object obj) {
                return this == obj || obj instanceof Image && getId().equals(((Image) obj).getId());
            }
        }
    }
}
