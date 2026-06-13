package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

    public class Banner {
        private int bannerId;
        private String titleLine1;
        private String titleLine2;
        private String subtitle;
        private String imageUrl;
        private String targetUrl;
        private String status;
        private int sortOrder;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;

        public int getBannerId() {
            return bannerId;
        }

        public void setBannerId(int bannerId) {
            this.bannerId = bannerId;
        }

        public String getTitleLine1() {
            return titleLine1;
        }

        public void setTitleLine1(String titleLine1) {
            this.titleLine1 = titleLine1;
        }

        public String getTitleLine2() {
            return titleLine2;
        }

        public void setTitleLine2(String titleLine2) {
            this.titleLine2 = titleLine2;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getTargetUrl() {
            return targetUrl;
        }

        public void setTargetUrl(String targetUrl) {
            this.targetUrl = targetUrl;
        }

        public String getStatus() {
            return status == null || status.isBlank() ? "ACTIVE" : status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getSortOrder() {
            return sortOrder;
        }

        public void setSortOrder(int sortOrder) {
            this.sortOrder = sortOrder;
        }

        public LocalDateTime getCreateAt() {
            return createAt;
        }

        public void setCreateAt(LocalDateTime createAt) {
            this.createAt = createAt;
        }

        public LocalDateTime getUpdateAt() {
            return updateAt;
        }

        public void setUpdateAt(LocalDateTime updateAt) {
            this.updateAt = updateAt;
        }

        public String getCreateAtFormatted() {
            if (createAt == null) return "Chưa cập nhật";
            return createAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        }

        public boolean isActive() {
            return "ACTIVE".equals(getStatus());
        }

        public String getStatusLabel() {
            return isActive() ? "Đang hiển thị" : "Đang ẩn";
        }

        public String getStatusClass() {
            return isActive() ? "status-active" : "status-inactive";
        }
}
